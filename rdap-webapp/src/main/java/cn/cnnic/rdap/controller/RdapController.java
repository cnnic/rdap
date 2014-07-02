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
package cn.cnnic.rdap.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.DomainSearch;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntitySearch;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.NameserverQueryParam;
import cn.cnnic.rdap.bean.NameserverSearch;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.RedirectResponse;
import cn.cnnic.rdap.common.util.AutnumValidator;
import cn.cnnic.rdap.common.util.DomainUtil;
import cn.cnnic.rdap.common.util.IpUtil;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.common.util.StringUtil;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.service.AccessControlManager;
import cn.cnnic.rdap.service.QueryService;
import cn.cnnic.rdap.service.RedirectService;
import cn.cnnic.rdap.service.SearchService;
import cn.cnnic.rdap.service.impl.ResponseDecorator;

/**
 * controller for query and search.All methods return message in JSON format.
 * 
 * @author jiashuo
 * 
 */
@Controller
@RequestMapping("/{dot}well-known/rdap")
public class RdapController {
    /**
     * autnum query URI.
     */
    private static final String SERVICE_URI_AS_Q = "/autnum/";
    /**
     * ip query URI.
     */
    private static final String SERVICE_URI_IP_Q = "/ip/";
    /**
     * nameserver query URI.
     */
    private static final String SERVICE_URI_NS_Q = "/nameserver/";
    /**
     * domain query URI.
     */
    private static final String SERVICE_URI_DOMAIN_Q = "/domain/";
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RdapController.class);
    /**
     * query service.
     */
    @Autowired
    private QueryService queryService;
    /**
     * search service.
     */
    @Autowired
    private SearchService searchService;
    /**
     * query parser.
     */
    @Autowired
    private QueryParser queryParser;
    /**
     * responseDecorator.
     */
    @Autowired
    private ResponseDecorator responseDecorator;

    /**
     * access control manager.
     */
    @Autowired
    private AccessControlManager accessControlManager;
    
    /**
     * RedirectService.
     */
    @Autowired
    private RedirectService redirectService;

    /**
     * query entity.
     * 
     * @param handle
     *            entity handle.
     * @param request
     *            HttpServletRequest.
     * @param response
     *            HttpServletResponse
     * @return JSON formated result,with HTTP code.
     */
    @RequestMapping(value = "/entity/{handle}", method = RequestMethod.GET)
    public ResponseEntity queryEntity(@PathVariable String handle,
            HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("query entity,handle:" + handle);
        handle = StringUtils.trim(handle);
        if (!StringUtil.isValidEntityHandleOrName(handle)) {
            return RestResponseUtil.createResponse400();
        }
        Entity result = queryService.queryEntity(queryParser
                .parseQueryParam(handle));
        if (null != result) {
            if (!accessControlManager.hasPermission(result)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(result);
            return RestResponseUtil.createResponse200(result);
        }
        return RestResponseUtil.createResponse404();
    }
    
    /**
     * search entity by handle or name.
     * @param fn fn.
     * @param handle handle.
     * @param request request.
     * @return ResponseEntity.
     */
    @RequestMapping(value = "/entities", method = RequestMethod.GET)
    public ResponseEntity searchEntity(@RequestParam(required = false) String fn,
            @RequestParam(required = false) String handle,
            HttpServletRequest request){
        LOGGER.info("search entities.fn:{},handle:{}",fn,handle);
        final String fnParamName = "fn";
        final String handleParamName = "handle";
        String paramName = queryParser.getFirstParameter(request,
                new String[]{fnParamName,handleParamName});
        if (StringUtils.isBlank(paramName)) {
            return RestResponseUtil.createResponse400();
        }
        String paramValue = queryParser.getParameter(request, paramName);
        paramValue = DomainUtil.iso8859Decode(paramValue);
        paramValue = StringUtils.trim(paramValue);
        if (!StringUtil.isValidEntityHandleOrName(paramValue)) {
            return RestResponseUtil.createResponse400();
        }
        if (!StringUtil.checkIsValidSearchPattern(paramValue)) {
            return RestResponseUtil.createResponse422();
        }
        QueryParam queryParam = queryParser
                .parseEntityQueryParam(paramValue, paramName);
        LOGGER.info("generate queryParam:{}",queryParam);
        EntitySearch result = searchService.searchEntity(queryParam);
        if (null != result) {
            if (result.getHasNoAuthForAllObjects()) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(result);
            return RestResponseUtil.createResponse200(result);
        }
        return RestResponseUtil.createResponse404();
    }

    /**
     * query autnum.
     * 
     * @param autnum
     *            an AS Plain autonomous system number [RFC5396].
     * @param request
     *            HttpServletRequest.
     * @param response
     *            HttpServletResponse
     * @return JSON formated result,with HTTP code.
     */
    @RequestMapping(value = "/autnum/{autnum}", method = RequestMethod.GET)
    public ResponseEntity queryAs(@PathVariable String autnum,
            HttpServletRequest request, HttpServletResponse response) {
        LOGGER.info("query autnum:" + autnum);
        if (!AutnumValidator.isValidAutnum(autnum)) {
            return RestResponseUtil.createResponse400();
        }
        QueryParam queryParam = queryParser
                .parseQueryParam(autnum);
        Autnum result = queryService.queryAutnum(queryParam);
        if (null != result) {
            if (!accessControlManager.hasPermission(result)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(result);
            return RestResponseUtil.createResponse200(result);
        }
        LOGGER.debug("query redirect autnum :{}" , queryParam);
        RedirectResponse redirect = redirectService.queryAutnum(queryParam);
        if (redirectService.isValidRedirect(redirect)) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(autnum,
                            SERVICE_URI_AS_Q, redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        return RestResponseUtil.createResponse404();
    }

    /**
     * query domain by domain name.
     * 
     * @param domainName
     *            is a fully-qualified (relative to the root) domain name
     *            [RFC1594] in either the in-addr.arpa or ip6.arpa zones (for
     *            RIRs) or a fully-qualified domain name in a zone administered
     *            by the server operator (for DNRs).
     * @return JSON formated result,with HTTP code.
     */
    @RequestMapping(value = { "/domain/{domainName}" },
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryDomain(@PathVariable String domainName) {
        String decodeDomain = domainName;
        String punyDomainName = decodeDomain;
        try {
            decodeDomain = DomainUtil
                    .decodeAndReplaceAsciiToLowercase(domainName);
            // long lable exception
            punyDomainName = DomainUtil.geneDomainPunyName(decodeDomain);
        } catch (Exception e) {
            return RestResponseUtil.createResponse400();
        }
        if (!DomainUtil.validateDomainNameIsValidIdna(decodeDomain)) {
            return RestResponseUtil.createResponse400();
        }
        decodeDomain = StringUtil.getNormalization(decodeDomain);
        LOGGER.debug("after normalization: {}",decodeDomain);
        decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);
        decodeDomain = StringUtils.lowerCase(decodeDomain);
        QueryParam queryParam = queryParser
                .parseDomainQueryParam(decodeDomain, punyDomainName);
        if(queryService.tldInThisRegistry(queryParam)){
            return queryDomainInThisRegistry(queryParam);
        }
        return queryRedirectDomainOrNs(queryParam, domainName);
    }

    /**
     * query redirect domain or nameserver.
     * @param queryParam queryParam.
     * @return ResponseEntity.
     */
    private ResponseEntity queryRedirectDomainOrNs(QueryParam queryParam,
            String paramName) {
        LOGGER.info("   queryRedirectDomainOrNs:{}", queryParam);
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
        LOGGER.info("   redirect not found.{},return 404.", queryParam);
        return RestResponseUtil.createResponse404();
    }

    /**
     * query domain in this registry.
     * @param queryParam queryParam.
     * @return ResponseEntity.
     */
    private ResponseEntity queryDomainInThisRegistry(QueryParam queryParam) {
        LOGGER.info("   queryDomainInThisRegistry:{}" , queryParam);
        Domain domain = queryService.queryDomain(queryParam);
        if (null != domain) {
            LOGGER.info("   found domain:{}" , queryParam);
            if (!accessControlManager.hasPermission(domain)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(domain);
            return RestResponseUtil.createResponse200(domain);
        }
        LOGGER.info("   domain not found,return 404. {}" , queryParam);
        return RestResponseUtil.createResponse404();
    }

    /**
     * search domain by domain name.
     * 
     * @param domainName
     *            is a fully-qualified (relative to the root) domain name
     *            [RFC1594] in either the in-addr.arpa or ip6.arpa zones (for
     *            RIRs) or a fully-qualified domain name in a zone administered
     *            by the server operator (for DNRs).
     * @return JSON formated result,with HTTP code.
     */
    @RequestMapping(value = "/domains", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity searchDomain(
            @RequestParam(required = false) String name,
            HttpServletRequest request, HttpServletResponse response) {
        name = queryParser.getParameter(request, "name");
        String decodeDomain = name;
        try {
            decodeDomain = DomainUtil.iso8859Decode(name);
            decodeDomain = DomainUtil
                    .decodeAndReplaceAsciiToLowercase(decodeDomain);
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
        decodeDomain = StringUtil.getNormalization(decodeDomain);
        decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);
        decodeDomain = StringUtils.lowerCase(decodeDomain);
        DomainSearch domainSearch = searchService.searchDomain(queryParser
                .parseDomainQueryParam(decodeDomain, decodeDomain));
        if (null != domainSearch) {
            if (domainSearch.getHasNoAuthForAllObjects()) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(domainSearch);
            return RestResponseUtil.createResponse200(domainSearch);
        }
        return RestResponseUtil.createResponse404();
    }

    /**
     * query nameserver by nameserver name.
     * 
     * @param nameserverName
     *            represents information regarding DNS name servers used in both
     *            forward and reverse DNS. RIRs and some DNRs register or expose
     *            nameserver information as an attribute of a domain name, while
     *            other DNRs model nameservers as "first class objects".
     * @return JSON formatted result,with HTTP code.
     */
    @RequestMapping(value = { "/nameserver/{nameserverName}" },
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryNameserver(@PathVariable String nameserverName) {
        String decodeNS = nameserverName;
        String punyNSName = decodeNS;
        try {
            decodeNS = DomainUtil
                    .decodeAndReplaceAsciiToLowercase(nameserverName);
            // long lable exception
            punyNSName = DomainUtil.geneDomainPunyName(decodeNS);
        } catch (Exception e) {
            return RestResponseUtil.createResponse400();
        }
        if (!DomainUtil.validateDomainNameIsValidIdna(decodeNS)) {
            return RestResponseUtil.createResponse400();
        }
        decodeNS = StringUtil.getNormalization(decodeNS);
        LOGGER.debug("after normalization: {}",decodeNS);
        decodeNS = DomainUtil.deleteLastPoint(decodeNS);
        decodeNS = StringUtils.lowerCase(decodeNS);
        QueryParam queryParam = queryParser
                .parseNameserverQueryParam(decodeNS, punyNSName);
        if(queryService.tldInThisRegistry(queryParam)){
        	return queryNsInThisRegistry(queryParam);
        }
        return queryRedirectDomainOrNs(queryParam, nameserverName);
    }

    /**
     * query nameserver in this registry.
     * @param queryParam queryParam.
     * @return ResponseEntity.
     */
    private ResponseEntity queryNsInThisRegistry(QueryParam queryParam) {
        LOGGER.info("   queryNsInThisRegistry:{}" , queryParam);
        Nameserver ns = queryService.queryNameserver(queryParam);
        if (null != ns) {
            LOGGER.info("   found ns:{}" , queryParam);
            if (!accessControlManager.hasPermission(ns)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(ns);
            return RestResponseUtil.createResponse200(ns);
        }
        LOGGER.info("   ns not found,return 404. {}" , queryParam);
        return RestResponseUtil.createResponse404();
    }

    /**
     * search nameserver by name.
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
     */
    @RequestMapping(value = "/nameservers", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity searchNameserver(
            @RequestParam(required = false) String name,
            HttpServletRequest request, HttpServletResponse response) {
        final String strIp = "ip";
        final String strName = "name";
        NameserverQueryParam nsQueryParam = null;
        final String[] strParamOrg = { strIp, strName };
        String nameParam = queryParser.getFirstParameter(request, strParamOrg);
        if (StringUtils.isBlank(nameParam)) {
            return RestResponseUtil.createResponse400();
        }
        if (0 == nameParam.compareTo(strIp)) {
            // search by IP
            name = queryParser.getParameter(request, strIp);
            // checkIP
            if (StringUtils.isBlank(name) || !IpUtil.isIpV4StrWholeValid(name)
                    && !IpUtil.isIpV6StrValid(name)) {
                return RestResponseUtil.createResponse400();
            }
            name = StringUtils.lowerCase(name);
            nsQueryParam = (NameserverQueryParam) queryParser
                    .parseNameserverQueryParam(name, name);
            nsQueryParam.setIsSearchByIp(true);
        } else if (0 == nameParam.compareTo(strName)) {
            // search by name
            name = queryParser.getParameter(request, strName);
            String decodeNameserver = name;
            try {
                decodeNameserver = DomainUtil.iso8859Decode(name);
                decodeNameserver = DomainUtil
                        .decodeAndReplaceAsciiToLowercase(decodeNameserver);
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
            decodeNameserver = StringUtil.getNormalization(decodeNameserver);
            decodeNameserver = DomainUtil.deleteLastPoint(decodeNameserver);
            decodeNameserver = StringUtils.lowerCase(decodeNameserver);
            nsQueryParam = (NameserverQueryParam) queryParser
                    .parseNameserverQueryParam(decodeNameserver,
                            decodeNameserver);
            nsQueryParam.setIsSearchByIp(false);
        } else {
            return RestResponseUtil.createResponse400();
        }

        NameserverSearch nsSearch = searchService
                .searchNameserver(nsQueryParam);

        if (null != nsSearch) {
            if (nsSearch.getHasNoAuthForAllObjects()) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(nsSearch);
            return RestResponseUtil.createResponse200(nsSearch);
        }
        return RestResponseUtil.createResponse404();
    }

    /**
     * query ip by mask.
     * 
     * @param ipAddr
     *            the query ip
     * @param mask
     *            the ip mask
     * @return JSON formatted result,with HTTP code.
     */
    @RequestMapping(value = { "/ip/{ipAddr}/{mask}" },
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryIpWithMask(@PathVariable String ipAddr,
            @PathVariable String mask) {
        return queryIpAddress(ipAddr, mask, ipAddr + "/" + mask);
    }

    /**
     * query ip by address.
     * 
     * @param ipAddr
     *            the query ip
     * @return ResponseEntity
     */
    @RequestMapping(value = { "/ip/{ipAddr}" }, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryIp(@PathVariable String ipAddr) {
        return queryIpAddress(ipAddr, "", ipAddr);
    }

    /**
     * invoked by upper functions.
     * 
     * @param ipAddr
     *            the query ip.
     * @param ipMask
     *            the ip mask,can be 0.
     * @param originQueryParam
     *            originQueryParam.
     * @return ResponseEntity ResponseEntity.
     */
    private ResponseEntity queryIpAddress(String ipAddr, String ipMask,
            String originQueryParam) {
        String strIp = ipAddr;
        String strMask = ipMask;
        final long maskHighV6 = 128;
        final long maskLow = 0;
        final long maskHighV4 = 32;
        long numMask = 0;
        if (!StringUtils.isNumeric(ipMask)) {
            return RestResponseUtil.createResponse400();
        }
        if (StringUtils.isNotBlank(ipMask)) {
            numMask = StringUtil.parseUnsignedLong(strMask);
        }
        IpVersion ipVersion = IpVersion.V6;
        boolean isV4 = IpUtil.isIpV4StrWholeValid(strIp);
        boolean isV6 = IpUtil.isIpV6StrValid(strIp);

        if (!isV4 && !isV6) {
            return RestResponseUtil.createResponse400();
        }
        if (isV4) {
            if (numMask > maskHighV4 || numMask < maskLow) {
                return RestResponseUtil.createResponse400();
            }
            ipVersion = IpVersion.V4;
        } else if (isV6) {
            if (numMask > maskHighV6 || numMask < maskLow) {
                return RestResponseUtil.createResponse400();
            }
            ipVersion = IpVersion.V6;
        }
        StringUtils.lowerCase(strIp);
        // query ip
        QueryParam queryParam = queryParser.parseIpQueryParam(strIp,
                numMask, ipVersion);
        Network ip = queryService.queryIp(queryParam);
        if (null != ip) {
            if (!accessControlManager.hasPermission(ip)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(ip);
            return RestResponseUtil.createResponse200(ip);
        }
        LOGGER.debug("query redirect network :{}", queryParam);
        RedirectResponse redirect = redirectService.queryIp(queryParam);
        if (redirectService.isValidRedirect(redirect)) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(originQueryParam,
                            SERVICE_URI_IP_Q, redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        LOGGER.info("   redirect network not found:{}", queryParam);
        return RestResponseUtil.createResponse404();
    }

    /**
     * other invalid query uri will response 400 error.
     * 
     * @return JSON formated result,with HTTP code.
     */
    @RequestMapping(value = "/**")
    public ResponseEntity error400() {
        return RestResponseUtil.createResponse400();
    }
}
