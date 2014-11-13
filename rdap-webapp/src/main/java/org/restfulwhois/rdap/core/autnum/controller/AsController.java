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
package org.restfulwhois.rdap.core.autnum.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restfulwhois.rdap.core.autnum.model.Autnum;
import org.restfulwhois.rdap.core.autnum.queryparam.AsQueryParam;
import org.restfulwhois.rdap.core.autnum.service.AutnumService;
import org.restfulwhois.rdap.core.common.controller.BaseController;
import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.filter.QueryFilter;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.support.QueryUri;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller for as number query.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class AsController extends BaseController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AsController.class);
    /**
     * query service.
     */
    @Autowired
    protected AutnumService queryService;

    @Resource(name = "commonServiceFilters")
    private List<QueryFilter> serviceFilters;

    @Override
    protected List<QueryFilter> getQueryFilters() {
        return serviceFilters;
    }
    
    /**
     * <pre>
     * query autnum.
     * URI:/autnum/{autnum}
     * 
     * First query autnum in local registry, if not exist, then query 
     * for redirect.
     *   
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param autnum
     *            an AS Plain autonomous system number [RFC5396].
     * @param request
     *            HttpServletRequest.
     * @param response
     *            HttpServletResponse
     * @return JSON formated result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = "/autnum/{autnum}", method = RequestMethod.GET)
    public ResponseEntity queryAs(@PathVariable String autnum,
            HttpServletRequest request, HttpServletResponse response)
            throws DecodeException {
        LOGGER.debug("query autnum:" + autnum);
        QueryParam queryParam = new AsQueryParam(request);
        return super.query(queryParam);
    }

    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        Autnum result = queryService.queryAutnum(queryParam);
        if (null != result) {
            return RestResponseUtil.createResponse200(result);
        }
        LOGGER.debug("query redirect autnum :{}", queryParam);
        RedirectResponse redirect = redirectService.queryAutnum(queryParam);
        if (redirectService.isValidRedirect(redirect)) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(
                            queryParam.getOriginalQ(),
                            QueryUri.AUTNUM.getName(), redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        return RestResponseUtil.createResponse404();
    }

}
