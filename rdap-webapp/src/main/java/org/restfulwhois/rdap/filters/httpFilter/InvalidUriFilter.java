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
package org.restfulwhois.rdap.filters.httpFilter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.filter.FilterHelper;
import org.restfulwhois.rdap.core.common.filter.HttpFilter;
import org.restfulwhois.rdap.core.common.model.ErrorMessage;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 * This filter is used to invalid URI.
 * <p>
 * Some URI can be seized by spring URL mapping, these URI is validated by this
 * filter pre spring mapping.
 * <p>
 * Now it validates:
 * 
 * <pre>
 *      URI can not be Empty;
 *      URI can not contain '\';
 *      URI can not contain '//';
 *      URI can not contain invalid space;
 *      If media type in 'Accept' header is 'application/rdap+json';
 * </pre>
 * 
 * @author jiashuo
 * 
 */
public class InvalidUriFilter implements HttpFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(InvalidUriFilter.class);

    /**
     * constructor.
     */
    public InvalidUriFilter() {
        super();
        LOGGER.debug("init RDAP filter:{}", this.getName());
    }

    /**
     * do pre process url.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @throws Exception
     *             Exception.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {  
        String path = request.getRequestURI();
        LOGGER.info("request URI: {} ", path);
        String decodeUri = StringUtils.EMPTY;
        String uri = path.substring(request.getContextPath().length());
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
        if (pathContainInvalidChar(decodeUri)) {
            writeError400Response(response);
            return false;
        }
        if (!"/".equals(decodeUri)) {
            String uriWithoutPrefixSlash = StringUtils.substring(decodeUri, 1);
            if (uriWithoutPrefixSlash.endsWith("/.")) {
                writeError400Response(response);
                return false;
            }
        }
        return true;
    }

    /**
     * check if decodeUri path section contain invalid space.
     * 
     * @param decodeUri
     *            decodeUri.
     * @return true if contain, false if not.
     */
    private boolean pathContainInvalidChar(String decodeUri) {
        String substringBeforeLast =
                StringUtils.substringBeforeLast(decodeUri, "/");
        return StringUtil.containNonAsciiPrintableChars(substringBeforeLast);
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
        if (StringUtils.startsWith(decodeUri, "/entity/")) {
            return false;
        }
        if (StringUtils.contains(decodeUri, "/ ")
                || StringUtils.endsWith(decodeUri, StringUtil.SPACE)) {
            return true;
        }
        return false;
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
     * do post process.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @throws Exception
     *             Exception.
     * @return true .
     */
    @Override
    public boolean postProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return true;
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
    private static String urlDecode(String str) 
                   throws UnsupportedEncodingException {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return URLDecoder.decode(str, StringUtil.CHAR_SET_UTF8);
    }
    /**
     * @return this class name.
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

}
