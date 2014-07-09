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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import cn.cnnic.rdap.bean.DomainQueryParam;
import cn.cnnic.rdap.bean.EntityQueryParam;
import cn.cnnic.rdap.bean.NameserverQueryParam;
import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.bean.NetworkQueryParam;
import cn.cnnic.rdap.bean.QueryParam;

/**
 * get request parameter from url.
 * 
 * @author jiashuo
 * 
 */
@Component
public class QueryParser {
    
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueryParser.class);
    
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
    public QueryParam
            parseNameserverQueryParam(String nsName, String punyNSName) {
        return new NameserverQueryParam(nsName, punyNSName);
    }

    /**
     * generate IpQueryParam.
     * 
     * @param ipAddr
     *            ip Address.
     * @param numMask
     *            mask for ip.
     * @param ipVersion
     *            v4 or v6.
     * @return QueryParam.
     */
    public QueryParam parseIpQueryParam(String ipAddr, long numMask,
            IpVersion ipVersion) {
        NetworkQueryParam ipQueryParam = new NetworkQueryParam(ipAddr, numMask,
                ipVersion);
        ipQueryParam.parseQueryIpMask();
        return ipQueryParam;
    }

    /**
     * generate EntityQueryParam.
     * 
     * @param q
     *            query string.
     * @param paramName
     *            param name.
     * @return QueryParam.
     */
    public QueryParam parseEntityQueryParam(String q, String paramName) {
        return new EntityQueryParam(q, paramName);
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

    /**
     * get parameter from request,get first if has more than one param.
     * 
     * @param request
     *            HttpServletRequest.
     * @param strParamOrg
     *            the final String array for url params.
     * @return first right url param.
     */
    public String getFirstParameter(HttpServletRequest request,
            final String[] strParamOrg) {
        
        String strQuery = request.getQueryString();
        LOGGER.info("getFirstParameter, query:" + strQuery 
                + ",strParamOrg:" + strParamOrg);
        
        if (StringUtils.isBlank(strQuery)) {
            return null;
        }
        
        // get first '?'
        int pos = strQuery.indexOf("?");
        if (-1 != pos) {
            try {
                strQuery = strQuery.substring(0, pos);
            } catch (Exception e) {
                LOGGER.error("getFirstParameter" + e.getMessage());
                return null;
            }
        }
        
        // get first param in strParamOrg
        final String strSplit = "&";
        String[] strParam = strQuery.split(strSplit);
        final String strEqual = "=";
        for (int k = 0; k < strParam.length; ++k) {
            String[] strParamName = strParam[k].split(strEqual);
            if (strParamName != null) {
                for (int j = 0; j < strParamOrg.length; ++j) {
                    if (strParamName[0].equals(strParamOrg[j])) {
                        LOGGER.info("getFirstParameter, param:" 
                                    + strParamOrg[j]); 
                        return strParamName[0];
                    }
                }
            }
        }
        return null;
    }
}
