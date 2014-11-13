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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.RequestUtil;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.domain.model.DomainSearch;
import org.restfulwhois.rdap.core.domain.model.DomainSearchType;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchByDomainNameParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchByNsIpParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchByNsLdhNameParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchParam;
import org.restfulwhois.rdap.core.domain.service.DomainSearchService;
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
     * search service.
     */
    @Autowired
    protected DomainSearchService searchService;

    /**
     * all possible search params.
     * <p>
     * A specific param in this list will be initialized for each query.
     * </p>
     */
    private static List<DomainSearchParam> searchParams =
            new ArrayList<DomainSearchParam>();
    /**
     * initialize param list.
     */
    static {
        searchParams.add(new DomainSearchByDomainNameParam());
        searchParams.add(new DomainSearchByNsLdhNameParam());
        searchParams.add(new DomainSearchByNsIpParam());
    }

    /**
     * createDomainSearchParam.
     * 
     * @param request
     *            request.
     * @return DomainSearchParam.
     */
    private DomainSearchParam
            createDomainSearchParam(HttpServletRequest request) {
        DomainSearchType searchType = parseSearchType(request);
        if (null == searchType) {
            return null;
        }
        for (DomainSearchParam domainSearchParam : searchParams) {
            if (domainSearchParam.supportSearchType(searchType)) {
                DomainSearchParam result =
                        BeanUtils.instantiate(domainSearchParam.getClass());
                result.setRequest(request);
                return result;
            }
        }
        return null;
    }

    public DomainSearchType parseSearchType(HttpServletRequest request) {
        try {
            String lastSpliInURI = RequestUtil.getLastSplitInURI(request);
            if (!"domains".equals(lastSpliInURI)) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        final String[] allSearchType = DomainSearchType.valuesOfString();
        String paramName =
                RequestUtil.getFirstParameter(request, allSearchType);
        if (StringUtils.isBlank(paramName)) {
            return null;
        }
        DomainSearchType searchType = DomainSearchType.getByName(paramName);
        return searchType;
    }

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
        DomainSearchParam domainSearchParam = createDomainSearchParam(request);
        if (null == domainSearchParam) {
            return RestResponseUtil.createResponse400();
        }
        return super.query(domainSearchParam);
    }

    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        DomainSearch domainSearch = searchService.searchDomain(queryParam);
        if (null != domainSearch) {
            return RestResponseUtil.createResponse200(domainSearch);
        }

        return RestResponseUtil.createResponse404();
    }

}
