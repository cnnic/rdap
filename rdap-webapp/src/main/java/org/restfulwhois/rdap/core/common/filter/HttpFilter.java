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
package org.restfulwhois.rdap.core.common.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RDAP filter interface.
 * <p>
 * A RDAP filter is an object that performs filtering tasks on either the
 * request to a resource, or on the response from a resource, or both.
 * <p>
 * RDAP filter is called in {@link javax.servlet.Filter#doFilter()}.
 * 
 * <p>
 * Filters perform pre filtering in the <code>preProcess</code> method, and
 * perform post filterting in the <code>postProcess</code> method. Every Filter
 * has reference to HttpServletRequest and HttpServletResponse.Each method may
 * throw Exception.
 * 
 * <p>
 * Filters are configured in {@link FilterChainProxy}.
 * 
 * @author jiashuo
 * 
 */
public interface HttpFilter {
    /**
     * do pre process.
     * 
     * @param req
     *            request.
     * @param res
     *            response.
     * @throws Exception
     *             Exception.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    boolean preProcess(HttpServletRequest req, HttpServletResponse res)
            throws Exception;

    /**
     * do post process.
     * 
     * @param req
     *            request.
     * @param res
     *            response.
     * @throws Exception
     *             Exception.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    boolean postProcess(HttpServletRequest req, HttpServletResponse res)
            throws Exception;

    /**
     * get filter name.
     * 
     * @return filter name.
     */
    String getName();

}
