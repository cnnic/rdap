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
package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.QueryParamHelper;
import org.restfulwhois.rdap.common.exception.DecodeException;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.RequestUtil;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Test for QueryParser
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class QueryParserTest extends BaseTest {

    /**
     * test valid autnum of one number
     * @throws DecodeException 
     */
    @Test
    public void testGetRealParamInPath() throws DecodeException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("", "");
        request.setRequestURI("/domain/cnnic.cn%1a");
        String paramV = RequestUtil.getLastSplitInURI(request);
        assertNotNull(paramV);
        assertEquals("cnnic.cn", paramV);
    }

    /**
     * test valid autnum of one number
     */
    @Test
    public void testParseQ() {
        String q = "3";
        QueryParam queryParam = QueryParamHelper.buildQueryParam(q);
        assertNotNull(queryParam);
        assertEquals(q, queryParam.getQ());
    }

    /**
     * test valid autnum of one number
     */
    @Test
    public void testGetParameter() {
        String paramName = "name";
        String paramValue1 = "v1";
        String paramValue2 = "v2";
        String paramValue3 = "v3";
        /**
         * one param
         */
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter(paramName, paramValue1);
        assertEquals(paramValue1, RequestUtil.getParameter(request, paramName));
        /**
         * two params
         */
        request = new MockHttpServletRequest();
        request.addParameter(paramName, paramValue1);
        request.addParameter(paramName, paramValue2);
        assertEquals(paramValue1, RequestUtil.getParameter(request, paramName));
        /**
         * three params
         */
        request = new MockHttpServletRequest();
        request.addParameter(paramName, paramValue1);
        request.addParameter(paramName, paramValue2);
        request.addParameter(paramName, paramValue3);
        assertEquals(paramValue1, RequestUtil.getParameter(request, paramName));
        /**
         * none param
         */
        request = new MockHttpServletRequest();
        assertNull(RequestUtil.getParameter(request, paramName));
    }

}
