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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.WebUtils;

import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.common.util.StringUtil;

/**
 * filter invalid uri which spring can't catch.
 * 
 * @author jiashuo
 * 
 */
public class InvalidUriFilter implements RdapFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthenticationFilter.class);

    /**
     * rdap url prefix.
     */
    private static final String RDAP_URL_PREFIX = ".well-known/rdap";

    /**
     * constructor.
     */
    public InvalidUriFilter() {
        super();
        LOGGER.info("init RDAP filter:{}", this.getName());
    }

    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        decodeServletPathForSpringUrlMapping(request);
        String path = request.getRequestURI();
        if (StringUtils.isBlank(path)) {
            writeError400Response(response);
            return false;
        }
        String decodeUri = StringUtils.EMPTY;
        String uri = path.substring(request.getContextPath().length());
        if (StringUtils.isBlank(uri)) {
            writeError400Response(response);
            return false;
        }
        try {
            decodeUri = urlDecode(uri);
        } catch (Exception e) {
            writeError400Response(response);
            return false;
        }
        if (decodeUri.contains("\\")) {
            writeError400Response(response);
            return false;
        }
        if (decodeUri.contains("//")) { // || decodeUri.contains("ip/::/")) {
            writeError400Response(response);
            return false;
        }
        if (containInvalidSpace(decodeUri)) {
            writeError400Response(response);
            return false;
        }
        if (!"/".equals(decodeUri)) { // if not /,then must begin with rdapUrl
            String uriWithoutPrefixSlash =
                    decodeUri.substring(1, decodeUri.length());
            if (!uriWithoutPrefixSlash.startsWith(RDAP_URL_PREFIX)) {
                writeError400Response(response);
                return false;
            } else if (!uriWithoutPrefixSlash.equals(RDAP_URL_PREFIX + "/")
                    && decodeUri.endsWith("/")) {
                writeError400Response(response);
                return false;
            } else if (uriWithoutPrefixSlash.endsWith("/.")) {
                writeError400Response(response);
                return false;
            }
        }
        return true;
    }

    /**
     * check if decodeUri contain invalid space.
     * 
     * @param decodeUri
     *            decodeUri.
     * @return true if contain, false if not.
     */
    private boolean containInvalidSpace(String decodeUri) {
        if (StringUtils.isBlank(decodeUri)) {
            return false;
        }
        if (StringUtils.startsWith(decodeUri, "/" + RDAP_URL_PREFIX
                + "/entity/")) {
            return false;
        }
        if (StringUtils.contains(decodeUri, "/ ")
                || StringUtils.endsWith(decodeUri, StringUtil.SPACE)) {
            return true;
        }
        return false;
    }

    /**
     * decode servletPath with UTF-8 for spring url mapping.
     * 
     * @param request
     *            HttpServletRequest.
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException.
     */
    private void
            decodeServletPathForSpringUrlMapping(HttpServletRequest request)
                    throws UnsupportedEncodingException {
        request.setCharacterEncoding(StringUtil.CHAR_SET_UTF8);
        String servletPath = request.getServletPath();
        if (StringUtils.isNotBlank(servletPath)) {
            String decodedPath =
                    new String(
                            servletPath.getBytes(StringUtil.CHAR_SET_ISO8859),
                            StringUtil.CHAR_SET_UTF8);
            request.setAttribute(WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE,
                    decodedPath);
        }
    }

    /**
     * write error 400 response.
     * 
     * @param response
     *            response.
     * @throws IOException
     *             IOException.
     */
    private void writeError400Response(HttpServletResponse response)
            throws IOException {
        ResponseEntity<ErrorMessage> responseEntity =
                RestResponseUtil.createResponse400();
        FilterHelper.writeResponse(responseEntity, response);
    }

    /**
     * decode url with UTF-8.
     * 
     * @param str
     *            URL.
     * @return decoded URL.
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException.
     */
    private String urlDecode(String str) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return URLDecoder.decode(str, StringUtil.CHAR_SET_UTF8);
    }

    @Override
    public boolean postProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return true;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

}
