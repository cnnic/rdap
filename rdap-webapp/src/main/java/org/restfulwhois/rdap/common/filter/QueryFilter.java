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
package org.restfulwhois.rdap.common.filter;

import org.restfulwhois.rdap.common.support.QueryParam;
import org.springframework.http.ResponseEntity;

/**
 * filter in query process.
 * 
 * <pre>
 *     All filter method return an object of type QueryFilterResult:
 *     1.if the return value is null, the query process will continue.
 *     2.if not null, the query process will intercepted and the ResponseEntity
 *       in the returned QueryFilterResult object will be returned to caller.
 * </pre>
 * 
 * @author jiashuo
 * 
 */
public interface QueryFilter {
    /**
     * do before parameter validate.
     * 
     * @param queryParam
     *            queryParam.
     * @return QueryFilterResult.
     */
    QueryFilterResult preParamValidate(QueryParam queryParam);

    /**
     * do after parameter validate.
     * 
     * @param queryParam
     *            queryParam.
     * @return QueryFilterResult.
     */
    QueryFilterResult postParamValidate(QueryParam queryParam);

    /**
     * do after query.
     * 
     * @param queryParam
     *            queryParam.
     * @param responseEntity
     *            responseEntity including the query result.
     * @return QueryFilterResult.
     */
    QueryFilterResult postQuery(QueryParam queryParam,
            ResponseEntity responseEntity);
}
