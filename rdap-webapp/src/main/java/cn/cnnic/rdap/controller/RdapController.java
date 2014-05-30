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
import cn.cnnic.rdap.common.util.AutnumValidator;
import cn.cnnic.rdap.common.util.DomainUtil;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.common.util.StringUtil;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.service.QueryService;
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
        Autnum result = queryService.queryAutnum(queryParser
                .parseQueryParam(autnum));
        if (null != result) {
            responseDecorator.decorateResponse(result);
            return RestResponseUtil.createResponse200(result);
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
    @RequestMapping(value = { "/domain/{domainName}" }, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryDomain(@PathVariable String domainName) {
        String decodeDomain = DomainUtil
                .decodeAndTrimAndReplaceAsciiToLowercase(domainName);
        String punyDomainName = decodeDomain;
        try {
            // long lable exception
            punyDomainName = DomainUtil.geneDomainPunyName(decodeDomain);
        } catch (Exception e) {
            return RestResponseUtil.createResponse400();
        }
        if (!DomainUtil.validateDomainNameIsValidIdna(decodeDomain)) {
            return RestResponseUtil.createResponse400();
        }
        decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);
        decodeDomain = StringUtils.lowerCase(decodeDomain);
        Domain domain = queryService.queryDomain(queryParser
                .parseDomainQueryParam(decodeDomain, punyDomainName));
        if (null != domain) {
            responseDecorator.decorateResponse(domain);
            return RestResponseUtil.createResponse200(domain);
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
    @RequestMapping(value = "/domains", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity searchDomain(
            @RequestParam(required = false) String name,
            HttpServletRequest request, HttpServletResponse response) {
        String decodeDomain = DomainUtil
                .decodeAndTrimAndReplaceAsciiToLowercase(name);
        if(StringUtils.isBlank(decodeDomain)){
            return RestResponseUtil.createResponse400();
        }
        name = StringUtil.getNormalization(name);
        if (StringUtil.ASTERISK.equals(name)
                || name.startsWith(StringUtil.ASTERISK)) {
            return RestResponseUtil.createResponse422();
        }
        String punyDomainName = decodeDomain;
        try {
            // long lable exception
            punyDomainName = DomainUtil.geneDomainPunyName(decodeDomain);
        } catch (Exception e) {
            return RestResponseUtil.createResponse400();
        }
        decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);
        decodeDomain = StringUtils.lowerCase(decodeDomain);
        DomainSearch domainSearch = searchService.searchDomain(queryParser
                .parseDomainQueryParam(decodeDomain, punyDomainName));
        if (null != domainSearch) {
            responseDecorator.decorateResponse(domainSearch);
            return RestResponseUtil.createResponse200(domainSearch);
        }
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