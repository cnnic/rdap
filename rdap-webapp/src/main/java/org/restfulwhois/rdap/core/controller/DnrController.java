/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.restfulwhois.rdap.core.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.util.DomainUtil;
import org.restfulwhois.rdap.core.common.util.IpUtil;
import org.restfulwhois.rdap.core.common.util.IpUtil.IpVersion;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.restfulwhois.rdap.core.exception.DecodeException;
import org.restfulwhois.rdap.core.model.Domain;
import org.restfulwhois.rdap.core.model.DomainSearchType;
import org.restfulwhois.rdap.core.model.Nameserver;
import org.restfulwhois.rdap.core.model.QueryUri;
import org.restfulwhois.rdap.core.model.RedirectResponse;
import org.restfulwhois.rdap.core.queryparam.DomainSearchParam;
import org.restfulwhois.rdap.core.queryparam.NameserverQueryParam;
import org.restfulwhois.rdap.core.queryparam.QueryParam;
import org.restfulwhois.rdap.search.bean.DomainSearch;
import org.restfulwhois.rdap.search.bean.NameserverSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for DNR.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class DnrController extends BaseController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DnrController.class);
    /**
     * nameserver query URI.
     */
    private static final String SERVICE_URI_NS_Q 
                    = QueryUri.NAMESERVER.getName();
    /**
     * domain query URI.
     */
    private static final String SERVICE_URI_DOMAIN_Q 
                   = QueryUri.DOMAIN.getName();

    /**
     * 
     * <pre>
     * query domain by domain name.
     * URI:/domain/{domainName}
     * 
     * If domain tld is configured in 'inTlds' and not in 'notInTlds'
     *  of rdap.properties file, will query in local registry; 
     * If domain tld is configured in 'notInTlds' and not in 'inTlds',
     *  will query for redirect.
     *   
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param domainName
     *            is a fully-qualified (relative to the root) domain name
     *            [RFC1594] in either the in-addr.arpa or ip6.arpa zones (for
     *            RIRs) or a fully-qualified domain name in a zone administered
     *            by the server operator (for DNRs).
     * @param request
     *            request.
     * @return JSON formated result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = { "/domain/{domainName}" },
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryDomain(@PathVariable String domainName,
            HttpServletRequest request) throws DecodeException {
        domainName = queryParser.getLastSplitInURI(request);
        String decodeDomain = domainName;
        String punyDomainName = decodeDomain;
        decodeDomain =
                DomainUtil.urlDecodeAndReplaceAsciiToLowercase(domainName);
        if (!DomainUtil.validateDomainNameIsValidIdna(decodeDomain,false)) {
            return RestResponseUtil.createResponse400();
        }
        decodeDomain = StringUtil.foldCaseAndNormalization(decodeDomain);
        LOGGER.debug("after foldCaseAndNormalization: {}", decodeDomain);
        try {
            // long lable exception
            punyDomainName = DomainUtil.geneDomainPunyName(decodeDomain);
        } catch (Exception e) {
            return RestResponseUtil.createResponse400();
        }
        decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);

        QueryParam queryParam =
                queryParser.parseDomainQueryParam(decodeDomain, punyDomainName);
        if (queryService.tldInThisRegistry(queryParam)) {
            return queryDomainInThisRegistry(queryParam);
        }
        return queryRedirectDomainOrNs(queryParam, domainName);
    }

    /**
     * query redirect domain or nameserver.
     * 
     * @param queryParam
     *            queryParam.
     * @param paramName
     *            the string param.
     * @return ResponseEntity.
     */
    private ResponseEntity queryRedirectDomainOrNs(QueryParam queryParam,
            String paramName) {
        LOGGER.debug("   queryRedirectDomainOrNs:{}", queryParam);
        RedirectResponse redirect = redirectService.queryDomain(queryParam);
        String servicePartUri = SERVICE_URI_DOMAIN_Q;
        if (queryParam instanceof NameserverQueryParam) {
            servicePartUri = SERVICE_URI_NS_Q;
        }
        if (null != redirect && StringUtils.isNotBlank(redirect.getUrl())) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(paramName,
                            servicePartUri, redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        LOGGER.debug("   redirect not found.{},return 404.", queryParam);
        return RestResponseUtil.createResponse404();
    }

    /**
     * query domain in this registry.
     * 
     * @param queryParam
     *            queryParam.
     * @return ResponseEntity.
     */
    private ResponseEntity queryDomainInThisRegistry(QueryParam queryParam) {
        LOGGER.debug("   queryDomainInThisRegistry:{}", queryParam);
        Domain domain = queryService.queryDomain(queryParam);
        if (null != domain) {
            LOGGER.debug("   found domain:{}", queryParam);
            if (!accessControlManager.hasPermission(domain)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(domain);
            return RestResponseUtil.createResponse200(domain);
        }
        LOGGER.debug("   domain not found,return 404. {}", queryParam);
        return RestResponseUtil.createResponse404();
    }

    /**
     * <pre>
     * search domain by domain name, nsLdhName or nsIp.
     * URI:/domains?name={domain name} 
     * or /domains?nsLdhName={nsLdhName}
     * or /domains?nsIp={nsIp}
     * 
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param name
     *            is a fully-qualified (relative to the root) domain name
     *            [RFC1594] in either the in-addr.arpa or ip6.arpa zones (for
     *            RIRs) or a fully-qualified domain name in a zone administered
     *            by the server operator (for DNRs).
     * @param request
     *            quest for httpServlet.
     * @param response
     *            response for httpServlet.
     * @return JSON formated result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = "/domains", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity searchDomain(
            @RequestParam(required = false) String name,
            HttpServletRequest request, HttpServletResponse response)
            throws DecodeException {
        String lastSpliInURI = queryParser.getLastSplitInURI(request);
        if (!"domains".equals(lastSpliInURI)) {
            return RestResponseUtil.createResponse400();
        }
        String decodeDomain = "";
        final String[] strParamOrg =
                {
                        DomainSearchType.NAME.value(),
                        DomainSearchType.NSLDHNAME.value(),
                        DomainSearchType.NSIP.value() };
        String nameParam = queryParser.getFirstParameter(request, strParamOrg);
        DomainSearchParam domainSearchParam;
        if (StringUtils.isBlank(nameParam)) {
            return RestResponseUtil.createResponse400();
        }

        if (0 == nameParam.compareTo(DomainSearchType.NAME.value())) {
            // search by domain name
            name =
                    queryParser.getParameter(request,
                            DomainSearchType.NAME.value());
            decodeDomain = name;
            try {
                decodeDomain = DomainUtil.iso8859Decode(name);
                decodeDomain =
                        DomainUtil
                                .urlDecodeAndReplaceAsciiToLowercase(decodeDomain);
            } catch (Exception e) {
                return RestResponseUtil.createResponse400();
            }
            if (StringUtils.isBlank(decodeDomain)) {
                return RestResponseUtil.createResponse400();
            }
            if (!StringUtil.checkIsValidSearchPattern(decodeDomain)) {
                return RestResponseUtil.createResponse422();
            }
            if (!DomainUtil.validateSearchStringIsValidIdna(decodeDomain)) {
                return RestResponseUtil.createResponse400();
            }
            decodeDomain = StringUtil.foldCaseAndNormalization(decodeDomain);
            decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);
            domainSearchParam =
                    (DomainSearchParam) queryParser.parseDomainSearchParam(
                            decodeDomain, decodeDomain);
            // set search by domain name
            domainSearchParam.setSearchByParam(DomainSearchType.NAME.value());
        } else if (0 == nameParam.compareTo(DomainSearchType.NSLDHNAME.value())) {
            // search by nsLdhName
            name =
                    queryParser.getParameter(request,
                            DomainSearchType.NSLDHNAME.value());
            // search by name
            String decodeNameserver = name;
            try {
                decodeNameserver = DomainUtil.iso8859Decode(name);
                decodeNameserver =
                        DomainUtil
                                .urlDecodeAndReplaceAsciiToLowercase(decodeNameserver);
            } catch (Exception e) {
                return RestResponseUtil.createResponse400();
            }
            if (StringUtils.isBlank(decodeNameserver)) {
                return RestResponseUtil.createResponse400();
            }
            if (!StringUtil.checkIsValidSearchPattern(decodeNameserver)) {
                return RestResponseUtil.createResponse422();
            }
            if (!DomainUtil.validateSearchLdnName(decodeNameserver)) {
                return RestResponseUtil.createResponse400();
            }
            if (!DomainUtil.validateSearchStringIsValidIdna(decodeNameserver)) {
                return RestResponseUtil.createResponse400();
            }
            decodeNameserver =
                    StringUtil.foldCaseAndNormalization(decodeNameserver);
            decodeNameserver = DomainUtil.deleteLastPoint(decodeNameserver);
            domainSearchParam =
                    (DomainSearchParam) queryParser.parseDomainSearchParam(
                            decodeNameserver, decodeNameserver);
            // set search by strNsLdhName
            domainSearchParam.setSearchByParam(DomainSearchType.NSLDHNAME
                    .value());

        } else if (0 == nameParam.compareTo(DomainSearchType.NSIP.value())) {
            // search by nsIp
            name =
                    queryParser.getParameter(request,
                            DomainSearchType.NSIP.value());
            // checkIP
            IpVersion ipVersion = IpUtil.getIpVersionOfIp(name);
            if (ipVersion.isNotValidIp()) {
                return RestResponseUtil.createResponse400();
            }
            name = StringUtils.lowerCase(name);
            domainSearchParam =
                    (DomainSearchParam) queryParser.parseDomainSearchParam(
                            name, name);
            // set search by strNsLdhName
            domainSearchParam.setSearchByParam(DomainSearchType.NSIP.value());
        } else {
            return RestResponseUtil.createResponse400();
        }
        DomainSearch domainSearch =
                searchService.searchDomain(domainSearchParam);
        if (null != domainSearch) {           
            if (domainSearch.getTruncatedInfo() != null 
                   && domainSearch.getTruncatedInfo()
                   .getHasNoAuthForAllObjects()) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(domainSearch);
            return RestResponseUtil.createResponse200(domainSearch);
        }

        return RestResponseUtil.createResponse404();
    }

    /**
     * <pre>
     * query nameserver by nameserver name.
     * URI:/nameserver/{nameserver name}
     * 
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param nsName
     *            represents information regarding DNS name servers used in both
     *            forward and reverse DNS. RIRs and some DNRs register or expose
     *            nameserver information as an attribute of a domain name, while
     *            other DNRs model nameservers as "first class objects".
     * @param request
     *            request.
     * @return JSON formatted result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = { "/nameserver/{nsName}" },
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryNameserver(@PathVariable String nsName,
            HttpServletRequest request) throws DecodeException {
        nsName = queryParser.getLastSplitInURI(request);
        String decodeNS = nsName;
        String punyNSName = decodeNS;
        decodeNS = DomainUtil.urlDecodeAndReplaceAsciiToLowercase(nsName);
        if (!DomainUtil.validateDomainNameIsValidIdna(decodeNS,false)) {
            return RestResponseUtil.createResponse400();
        }
        decodeNS = StringUtil.foldCaseAndNormalization(decodeNS);
        LOGGER.debug("after foldCaseAndNormalization: {}", decodeNS);
        try {
            // long lable exception
            punyNSName = DomainUtil.geneDomainPunyName(decodeNS);
        } catch (Exception e) {
            return RestResponseUtil.createResponse400();
        }
        decodeNS = DomainUtil.deleteLastPoint(decodeNS);

        QueryParam queryParam =
                queryParser.parseNameserverQueryParam(decodeNS, punyNSName);
        if (queryService.tldInThisRegistry(queryParam)) {
            return queryNsInThisRegistry(queryParam);
        }
        return queryRedirectDomainOrNs(queryParam, nsName);
    }

    /**
     * query nameserver in this registry.
     * 
     * @param queryParam
     *            queryParam.
     * @return ResponseEntity.
     */
    private ResponseEntity queryNsInThisRegistry(QueryParam queryParam) {
        LOGGER.debug("   queryNsInThisRegistry:{}", queryParam);
        Nameserver ns = queryService.queryNameserver(queryParam);
        if (null != ns) {
            LOGGER.debug("   found ns:{}", queryParam);
            if (!accessControlManager.hasPermission(ns)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(ns);
            return RestResponseUtil.createResponse200(ns);
        }
        LOGGER.debug("   ns not found,return 404. {}", queryParam);
        return RestResponseUtil.createResponse404();
    }

    /**
     * 
     * <pre>
     * search nameserver by name or ip.
     * URI:/nameservers?name={nsName}  OR /nameservers?ip={ip} 
     * 
     * parameter 'ip' can only be precise ip address. 
     * 
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param name
     *            is a fully-qualified (relative to the root) domain name
     *            [RFC1594] in either the in-addr.arpa or ip6.arpa zones (for
     *            RIRs) or a fully-qualified domain name in a zone administered
     *            by the server operator (for DNRs).
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @return JSON formated result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = "/nameservers", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity searchNameserver(
            @RequestParam(required = false) String name,
            HttpServletRequest request, HttpServletResponse response)
            throws DecodeException {
        String lastSpliInURI = queryParser.getLastSplitInURI(request);
        if (!"nameservers".equals(lastSpliInURI)) {
            return RestResponseUtil.createResponse400();
        }
        final String strIp = "ip";
        final String strName = "name";
        NameserverQueryParam nsQueryParam = null;
        final String[] strParamOrg = {
                strIp, strName };
        String nameParam = queryParser.getFirstParameter(request, strParamOrg);
        if (StringUtils.isBlank(nameParam)) {
            return RestResponseUtil.createResponse400();
        }
        if (0 == nameParam.compareTo(strIp)) {
            // search by IP
            name = queryParser.getParameter(request, strIp);
            // checkIP
            IpVersion ipVersion = IpUtil.getIpVersionOfIp(name);
            if (ipVersion.isNotValidIp()) {
                return RestResponseUtil.createResponse400();
            }
            name = StringUtils.lowerCase(name);
            nsQueryParam =
                    (NameserverQueryParam) queryParser
                            .parseNameserverQueryParam(name, name);
            nsQueryParam.setIsSearchByIp(true);
        } else if (0 == nameParam.compareTo(strName)) {
            // search by name
            name = queryParser.getParameter(request, strName);
            String decodeNameserver = name;

            try {
                decodeNameserver = DomainUtil.iso8859Decode(name);
                decodeNameserver =
                        DomainUtil
                                .urlDecodeAndReplaceAsciiToLowercase(decodeNameserver);
            } catch (Exception e) {
                return RestResponseUtil.createResponse400();
            }
            if (StringUtils.isBlank(decodeNameserver)) {
                return RestResponseUtil.createResponse400();
            }
            if (!StringUtil.checkIsValidSearchPattern(decodeNameserver)) {
                return RestResponseUtil.createResponse422();
            }
            if (!DomainUtil.validateSearchStringIsValidIdna(decodeNameserver)) {
                return RestResponseUtil.createResponse400();
            }
            decodeNameserver =
                    StringUtil.foldCaseAndNormalization(decodeNameserver);
            decodeNameserver = DomainUtil.deleteLastPoint(decodeNameserver);

            nsQueryParam =
                    (NameserverQueryParam) queryParser
                            .parseNameserverQueryParam(decodeNameserver,
                                    decodeNameserver);
            nsQueryParam.setIsSearchByIp(false);
        } else {
            return RestResponseUtil.createResponse400();
        }

        NameserverSearch nsSearch =
                searchService.searchNameserver(nsQueryParam);

        if (null != nsSearch) {
           // if (nsSearch.getHasNoAuthForAllObjects()) {
            if (nsSearch.getTruncatedInfo() != null 
                    && nsSearch.getTruncatedInfo()
                    .getHasNoAuthForAllObjects()) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(nsSearch);
            return RestResponseUtil.createResponse200(nsSearch);
        }
        return RestResponseUtil.createResponse404();
    }
}
