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
package org.restfulwhois.rdap.core.help.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restfulwhois.rdap.common.controller.BaseController;
import org.restfulwhois.rdap.common.exception.DecodeException;
import org.restfulwhois.rdap.common.filter.QueryFilter;
import org.restfulwhois.rdap.common.model.Help;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.support.RestResponse;
import org.restfulwhois.rdap.core.help.queryparam.HelpQueryParam;
import org.restfulwhois.rdap.core.help.service.HelpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for help query.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class HelpQueryController extends BaseController {
    /**
     * query service.
     */
    @Autowired
    protected HelpService queryService;

    /**
     * queryFilters.
     */
    @Resource(name = "helpQueryQueryFilters")
    private List<QueryFilter> queryFilters;

    @Override
    protected List<QueryFilter> getQueryFilters() {
        return queryFilters;
    }

    /**
     * <pre>
     * Query help.
     * URI:/help.
     * This service is not under permission control.
     * This service is not under policy control.
     * 
     * </pre>
     * 
     * @param request
     *            HttpServletRequest.
     * @param response
     *            HttpServletResponse
     * @return JSON formated result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @SuppressWarnings("rawtypes")
    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public ResponseEntity queryHelp(HttpServletRequest request,
            HttpServletResponse response) throws DecodeException {
        return super.query(new HelpQueryParam(request));
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        Help result = queryService.queryHelp(queryParam);
        if (null != result) {
            return RestResponse.createResponse200(result);
        }
        return RestResponse.createResponse404();
    }

}
