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
package org.restfulwhois.rdap.filters.queryFilter;

import org.restfulwhois.rdap.core.common.filter.QueryFilter;
import org.restfulwhois.rdap.core.common.filter.QueryFilterResult;
import org.restfulwhois.rdap.core.common.model.ErrorMessage;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.redirect.service.RedirectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * AbstractRedirectQueryFilter.
 * 
 * @author jiashuo
 * 
 */
public abstract class AbstractRedirectQueryFilter implements QueryFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractRedirectQueryFilter.class);
    /**
     * redirect service.
     */
    @Autowired
    protected RedirectService redirectService;

    @Override
    public QueryFilterResult postQuery(QueryParam queryParam,
            ResponseEntity responseEntity) {
        LOGGER.debug("check if responseBody is 404...");
        Object responseBody = responseEntity.getBody();
        if (null == responseBody) {
            LOGGER.debug("responseBody is null,return and not query redirect.");
            return null;
        }
        if (responseIs404(responseBody)) {
            LOGGER.debug("responseBody is 404, query redirect...");
            QueryFilterResult result = queryRedirect(queryParam);
            LOGGER.debug("query redirect result is :{}", result);
            return result;
        }
        return null;
    }

    /**
     * check if response is 404.
     * 
     * @param responseBody
     *            responseBody.
     * @return true if is 404, false if not.
     */
    private boolean responseIs404(Object responseBody) {
        if (responseBody instanceof ErrorMessage) {
            ErrorMessage errorMessage = (ErrorMessage) responseBody;
            if (errorMessage.equalsByCode(HttpStatus.NOT_FOUND.value())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param queryParam
     * @return
     */
    abstract protected QueryFilterResult queryRedirect(QueryParam queryParam);

    @Override
    public QueryFilterResult preParamValidate(QueryParam queryParam) {
        return null;
    }

    @Override
    public QueryFilterResult postParamValidate(QueryParam queryParam) {
        return null;
    }

}
