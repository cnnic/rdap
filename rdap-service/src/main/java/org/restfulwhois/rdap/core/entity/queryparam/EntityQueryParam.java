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
package org.restfulwhois.rdap.core.entity.queryparam;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.restfulwhois.rdap.common.model.base.QueryUri;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.restfulwhois.rdap.core.entity.validator.EntityQueryValidator;

/**
 * entity query parameter bean.
 * 
 * @author jiashuo
 * 
 */
public class EntityQueryParam extends QueryParam {
    /**
     * constructor.
     * 
     * @param request
     *            request.
     */
    public EntityQueryParam(HttpServletRequest request) {
        super(request);
    }

    /**
     * default constructor.
     * 
     * @param q
     *            query string.
     */
    public EntityQueryParam(String q) {
        super(q);
    }

    @Override
    protected void initValidators() {
        addValidator(new EntityQueryValidator());
    }

    @Override
    public void fillParam() throws Exception {
        String handle = getLastSplitInURI(getRequest());
        handle = StringUtils.trim(handle);
        super.setOriginalQ(handle);
        super.setQ(handle);
    }

    @Override
    public void convertParam() throws Exception {
        String handle = StringUtil.foldCaseAndNormalization(getQ());
        super.setQ(handle);
    }

    /**
     * constructor.
     * 
     * @param q
     *            query string.
     * @param searchParamName
     *            searchParamName.
     */
    public EntityQueryParam(String q, String searchParamName) {
        super(q);
        this.searchParamName = searchParamName;
    }

    /**
     * searchParamName.
     */
    private String searchParamName;

    /**
     * get searchParamName.
     * 
     * @return searchParamName.
     */
    public String getSearchParamName() {
        return searchParamName;
    }

    /**
     * set searchParamName.
     * 
     * @param searchParamName
     *            searchParamName.
     */
    public void setSearchParamName(String searchParamName) {
        this.searchParamName = searchParamName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(searchParamName).append(getQ())
                .toString();
    }

    @Override
    public QueryUri getQueryUri() {
        return QueryUri.ENTITY;
    }

}
