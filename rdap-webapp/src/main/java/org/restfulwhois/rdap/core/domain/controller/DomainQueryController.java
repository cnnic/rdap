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
package org.restfulwhois.rdap.core.domain.controller;

import javax.servlet.http.HttpServletRequest;

import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.domain.model.Domain;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.core.domain.service.DomainQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for domain query.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class DomainQueryController extends BaseDnrController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainQueryController.class);
    /**
     * query service.
     */
    @Autowired
    protected DomainQueryService queryService;

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
        QueryParam queryParam = new DomainQueryParam(request);
        return super.query(queryParam);
    }

    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        if (queryService.tldInThisRegistry(queryParam)) {
            return queryDomainInThisRegistry(queryParam);
        }
        return queryRedirectDomainOrNs(queryParam, queryParam.getOriginalQ());
    }

    /**
     * query domain in this registry.
     * 
     * @param queryParam
     *            queryParam.
     * @return ResponseEntity.
     */
    protected ResponseEntity queryDomainInThisRegistry(QueryParam queryParam) {
        LOGGER.debug("   queryDomainInThisRegistry:{}", queryParam);
        Domain domain = queryService.queryDomain(queryParam);
        if (null != domain) {
            LOGGER.debug("   found domain:{}", queryParam);
            return RestResponseUtil.createResponse200(domain);
        }
        LOGGER.debug("   domain not found,return 404. {}", queryParam);
        return RestResponseUtil.createResponse404();
    }

}
