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

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.util.DomainUtil;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.restfulwhois.rdap.core.exception.DecodeException;
import org.restfulwhois.rdap.core.queryparam.QueryParam;
import org.restfulwhois.rdap.search.bean.EntitySearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for entity search.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class EntitySearchController extends BaseController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EntitySearchController.class);

    /**
     * <pre>
     * search entity by handle or name.
     * URI:/entities?fn={entity name} ; /entities?handle={handle}.
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * The first appearance parameter 'fn' and 'handle' will be handled,
     * and other parameters will be ignored.
     * Parameter will be trimed.
     * 
     * </pre>
     * 
     * @param fn
     *            fn.
     * @param handle
     *            handle.
     * @param request
     *            request.
     * @return ResponseEntity.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = "/entities", method = RequestMethod.GET)
    public ResponseEntity
            searchEntity(@RequestParam(required = false) String fn,
                    @RequestParam(required = false) String handle,
                    HttpServletRequest request) throws DecodeException {
        LOGGER.debug("search entities.fn:{},handle:{}", fn, handle);
        String lastSpliInURI = queryParser.getLastSplitInURI(request);
        if (!"entities".equals(lastSpliInURI)) {
            return RestResponseUtil.createResponse400();
        }
        final String fnParamName = "fn";
        final String handleParamName = "handle";
        String paramName = queryParser.getFirstParameter(request, new String[] {
                fnParamName, handleParamName });
        if (StringUtils.isBlank(paramName)) {
            return RestResponseUtil.createResponse400();
        }
        String paramValue = queryParser.getParameter(request, paramName);
        paramValue = DomainUtil.iso8859Decode(paramValue);
        if (!StringUtil.isValidEntityHandleOrName(paramValue)) {
            return RestResponseUtil.createResponse400();
        }
        if (!StringUtil.checkIsValidSearchPattern(paramValue)) {
            return RestResponseUtil.createResponse422();
        }
        if (handleParamName.equals(paramName)) {// fold case when by handle
            paramValue = StringUtil.foldCaseAndNormalization(paramValue);
        } else {
            paramValue = StringUtil.getNormalization(paramValue);
        }
        paramValue = StringUtils.trim(paramValue);
        QueryParam queryParam =
                queryParser.parseEntityQueryParam(paramValue, paramName);
        LOGGER.debug("generate queryParam:{}", queryParam);
        EntitySearch result = searchService.searchEntity(queryParam);
        if (null != result) {
           if (result.getTruncatedInfo() != null 
                    && result.getTruncatedInfo()
                    .getHasNoAuthForAllObjects()) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(result);
            return RestResponseUtil.createResponse200(result);
        }
        return RestResponseUtil.createResponse404();
    }

}
