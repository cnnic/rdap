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
package org.restfulwhois.rdap.core.entity.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.controller.BaseController;
import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.filter.QueryFilter;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.RequestUtil;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.entity.model.EntitySearchType;
import org.restfulwhois.rdap.core.entity.queryparam.EntitySearchByFnParam;
import org.restfulwhois.rdap.core.entity.queryparam.EntitySearchByHandleParam;
import org.restfulwhois.rdap.core.entity.queryparam.EntitySearchParam;
import org.restfulwhois.rdap.core.entity.service.EntitySearchService;
import org.restfulwhois.rdap.search.entity.bean.EntitySearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
     * search service.
     */
    @Autowired
    protected EntitySearchService searchService;

    @Resource(name = "commonServiceFilters")
    private List<QueryFilter> serviceFilters;

    @Override
    protected List<QueryFilter> getQueryFilters() {
        return serviceFilters;
    }

    /**
     * searchParams.
     */
    private static List<EntitySearchParam> searchParams =
            new ArrayList<EntitySearchParam>();
    /**
     * init params.
     */
    static {
        searchParams.add(new EntitySearchByHandleParam());
        searchParams.add(new EntitySearchByFnParam());
    }

    /**
     * create param.
     * 
     * @param request
     *            request.
     * @return searchParam.
     */
    private EntitySearchParam createSearchParam(HttpServletRequest request) {
        EntitySearchType searchType = parseSearchType(request);
        if (null == searchType) {
            return null;
        }
        for (EntitySearchParam domainSearchParam : searchParams) {
            if (domainSearchParam.supportSearchType(searchType)) {
                EntitySearchParam result =
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
     * @return searchType.
     */
    public EntitySearchType parseSearchType(HttpServletRequest request) {
        try {
            String lastSpliInURI = RequestUtil.getLastSplitInURI(request);
            if (!"entities".equals(lastSpliInURI)) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        final String[] allSearchType = EntitySearchType.valuesOfString();
        String paramName =
                RequestUtil.getFirstParameter(request, allSearchType);
        if (StringUtils.isBlank(paramName)) {
            return null;
        }
        EntitySearchType searchType = EntitySearchType.getByName(paramName);
        return searchType;
    }

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
        EntitySearchParam entitySearchParam = createSearchParam(request);
        return super.query(entitySearchParam);
    }

    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        LOGGER.debug("generate queryParam:{}", queryParam);
        EntitySearch result = searchService.searchEntity(queryParam);
        if (null != result) {
            return RestResponseUtil.createResponse200(result);
        }
        return RestResponseUtil.createResponse404();
    }

}
