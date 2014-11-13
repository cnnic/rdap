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

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.controller.BaseController;
import org.restfulwhois.rdap.core.common.filter.QueryFilter;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * Base Controller for DNR object, such as domain,nameserver.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class BaseDnrController extends BaseController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BaseDnrController.class);

    @Resource(name = "commonServiceFilters")
    private List<QueryFilter> serviceFilters;

    @Override
    protected List<QueryFilter> getQueryFilters() {
        return serviceFilters;
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
    protected ResponseEntity queryRedirectDomainOrNs(QueryParam queryParam,
            String paramName) {
        LOGGER.debug("   queryRedirectDomainOrNs:{}", queryParam);
        RedirectResponse redirect = redirectService.queryDomain(queryParam);
        if (null != redirect && StringUtils.isNotBlank(redirect.getUrl())) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(paramName, queryParam
                            .getQueryUri().getName(), redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        LOGGER.debug("   redirect not found.{},return 404.", queryParam);
        return RestResponseUtil.createResponse404();
    }

}
