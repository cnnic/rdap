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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.RequestUtil;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.domain.controller.BaseDnrController;
import org.restfulwhois.rdap.core.nameserver.model.NameserverSearchType;
import org.restfulwhois.rdap.core.nameserver.queryparam.NameserverSearchByIpParam;
import org.restfulwhois.rdap.core.nameserver.queryparam.NameserverSearchByNameParam;
import org.restfulwhois.rdap.core.nameserver.queryparam.NameserverSearchParam;
import org.restfulwhois.rdap.core.nameserver.service.NameserverSearchService;
import org.restfulwhois.rdap.search.nameserver.bean.NameserverSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for nameserver search.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class NameserverSearchController extends BaseDnrController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NameserverSearchController.class);
    /**
     * search service.
     */
    @Autowired
    protected NameserverSearchService searchService;
    /**
     * searchParams.
     */
    private static List<NameserverSearchParam> searchParams =
            new ArrayList<NameserverSearchParam>();
    /**
     * init params.
     */
    static {
        searchParams.add(new NameserverSearchByNameParam());
        searchParams.add(new NameserverSearchByIpParam());
    }

    /**
     * createNsSearchParam.
     * 
     * @param request
     *            request.
     * @return NameserverSearchParam.
     */
    private NameserverSearchParam
            createNsSearchParam(HttpServletRequest request) {
        NameserverSearchType searchType = parseSearchType(request);
        if (null == searchType) {
            return null;
        }
        for (NameserverSearchParam domainSearchParam : searchParams) {
            if (domainSearchParam.supportSearchType(searchType)) {
                NameserverSearchParam result =
                        BeanUtils.instantiate(domainSearchParam.getClass());
                result.setRequest(request);
                return result;
            }
        }
        return null;
    }

    /**
     * parseSearchType.
     * 
     * @param request
     *            request.
     * @return NameserverSearchType.
     */
    public NameserverSearchType parseSearchType(HttpServletRequest request) {
        try {
            String lastSpliInURI = RequestUtil.getLastSplitInURI(request);
            if (!"nameservers".equals(lastSpliInURI)) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        final String[] allSearchType = NameserverSearchType.valuesOfString();
        String paramName =
                RequestUtil.getFirstParameter(request, allSearchType);
        if (StringUtils.isBlank(paramName)) {
            return null;
        }
        NameserverSearchType searchType =
                NameserverSearchType.getByName(paramName);
        return searchType;
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
        NameserverSearchParam nsSearchParam = createNsSearchParam(request);
        if (null == nsSearchParam) {
            return RestResponseUtil.createResponse400();
        }
        return super.query(nsSearchParam);

    }

    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        NameserverSearch nsSearch = searchService.searchNameserver(queryParam);
        if (null != nsSearch) {
            return RestResponseUtil.createResponse200(nsSearch);
        }
        return RestResponseUtil.createResponse404();
    }

}
