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
import org.restfulwhois.rdap.core.model.DomainSearchType;
import org.restfulwhois.rdap.core.queryparam.DomainSearchParam;
import org.restfulwhois.rdap.search.bean.DomainSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for domain search.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class DomainSearchController extends BaseDnrController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainSearchController.class);

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
            if (domainSearch.getHasNoAuthForAllObjects()) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(domainSearch);
            return RestResponseUtil.createResponse200(domainSearch);
        }

        return RestResponseUtil.createResponse404();
    }

}
