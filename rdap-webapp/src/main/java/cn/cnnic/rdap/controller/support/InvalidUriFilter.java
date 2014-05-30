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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.WebUtils;

import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.common.util.StringUtil;

/**
 * filter invalid uri which spring can't catch
 * 
 * @author nic
 * 
 */
public class InvalidUriFilter implements Filter {
    /**
     * rdap url prefix.
     */
    private static final String RDAP_URL_PREFIX = ".well-known/rdap";

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        decodeServletPathForSpringUrlMapping(request);
        String path = request.getRequestURI();
        if (StringUtils.isBlank(path)) {
            writeError400Response(response);
            return;
        }
        String decodeUri = StringUtils.EMPTY;
        String uri = path.substring(request.getContextPath().length());
        if (StringUtils.isBlank(uri)) {
            writeError400Response(response);
            return;
        }
        try {
            decodeUri = urlDecode(uri);
            if (decodeUri.contains(" ")) {
                writeError400Response(response);
                return;
            }
        } catch (Exception e) {
            writeError400Response(response);
            return;
        }
        if (decodeUri.contains("//") || decodeUri.contains("ip/::/")) {
            writeError400Response(response);
            return;
        }
        if (!"/".equals(decodeUri)) {// if not /,then must begin with rdapUrl
            String uriWithoutPrefixSlash = decodeUri.substring(1,
                    decodeUri.length());
            if (!uriWithoutPrefixSlash.startsWith(RDAP_URL_PREFIX)) {
                writeError400Response(response);
                return;
            } else if (!uriWithoutPrefixSlash.equals(RDAP_URL_PREFIX + "/")
                    && decodeUri.endsWith("/")) {
                writeError400Response(response);
                return;
            } else if (uriWithoutPrefixSlash.endsWith("/.")) {
                writeError400Response(response);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    /**
     * decode servletPath with UTF-8 for spring url mapping.
     * 
     * @param request
     *            HttpServletRequest.
     */
    private void decodeServletPathForSpringUrlMapping(HttpServletRequest request)
            throws UnsupportedEncodingException {
        request.setCharacterEncoding(StringUtil.CHAR_SET_UTF8);
        String servletPath = request.getServletPath();
        if (StringUtils.isNotBlank(servletPath)) {
            String decodedPath = new String(
                    servletPath.getBytes(StringUtil.CHAR_SET_ISO8859),
                    StringUtil.CHAR_SET_UTF8);
            request.setAttribute(WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE,
                    decodedPath);
        }
    }

    private void writeError400Response(HttpServletResponse response)
            throws IOException {
        ResponseEntity<ErrorMessage> responseEntity = RestResponseUtil
                .createResponse400();
        FilterHelper.writeResponse(responseEntity, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {

    }

    /**
     * decode url with UTF-8.
     * 
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    private String urlDecode(String str) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return URLDecoder.decode(str, "UTF-8");
    }
}
