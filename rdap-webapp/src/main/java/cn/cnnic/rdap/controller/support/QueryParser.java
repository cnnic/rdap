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
package cn.cnnic.rdap.controller.support;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import cn.cnnic.rdap.bean.DomainQueryParam;
import cn.cnnic.rdap.bean.NameserverQueryParam;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.common.util.DomainUtil;

/**
 * 
 * @author jiashuo
 * 
 */
@Component
public class QueryParser {
    /**
     * generate QueryParam.
     * 
     * @param q
     *            query string.
     * @return QueryParam.
     */
    public QueryParam parseQueryParam(String q) {
        return new QueryParam(q);
    }

    /**
     * generate DomainQueryParam.
     * 
     * @param domainName
     *            domain name.
     * @param punyDomainName
     *            domain puny name.
     * @return QueryParam.
     */
    public QueryParam parseDomainQueryParam(String domainName,
            String punyDomainName) {
        return new DomainQueryParam(domainName, punyDomainName);
    }

    /**
     * generate NameserverQueryParam.
     * 
     * @param nsName
     *            nameserver name.
     * @param punyNSName
     *            nameserver puny name.
     * @return QueryParam.
     */
    public QueryParam parseNameserverQueryParam(String nsName, String punyNSName) {
        return new NameserverQueryParam(nsName, punyNSName);
    }

    /**
     * get parameter from request,get first if has more than one value.
     * 
     * @param request
     *            request.
     * @param name
     *            parameter name.
     * @return parameter value.
     */
    public String getParameter(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);
        if (null == values || values.length < 1) {
            return null;
        }
        return values[0];
    }
}
