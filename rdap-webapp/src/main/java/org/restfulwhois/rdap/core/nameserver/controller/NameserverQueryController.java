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
package org.restfulwhois.rdap.core.nameserver.controller;

import javax.servlet.http.HttpServletRequest;

import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.domain.controller.BaseDnrController;
import org.restfulwhois.rdap.core.nameserver.model.Nameserver;
import org.restfulwhois.rdap.core.nameserver.queryparam.NameserverQueryParam;
import org.restfulwhois.rdap.core.nameserver.service.NameserverQueryService;
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
 * Controller for nameserver query.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class NameserverQueryController extends BaseDnrController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NameserverQueryController.class);
    /**
     * query service.
     */
    @Autowired
    protected NameserverQueryService queryService;

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
        QueryParam queryParam = new NameserverQueryParam(request);
        return super.query(queryParam);
    }

    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        if (queryService.tldInThisRegistry(queryParam)) {
            return queryNsInThisRegistry(queryParam);
        }
        return queryRedirectDomainOrNs(queryParam, queryParam.getOriginalQ());
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
            return RestResponseUtil.createResponse200(ns);
        }
        LOGGER.debug("   ns not found,return 404. {}", queryParam);
        return RestResponseUtil.createResponse404();
    }

}
