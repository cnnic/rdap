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
import org.springframework.web.util.WebUtils;

/**
 * This filter is used to decode URI exception.
 * <p>
 * Some URI can be seized by spring URL mapping, these URI is validated by this
 * filter pre spring mapping.
 * decode uri exception and  URI can not be Empty.
 * <p> 
 * 
 * @author zhanyq
 * 
 */
public class DecodeUriForSpringFilter implements HttpFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DecodeUriForSpringFilter.class);

    /**
     * constructor.
     */
    public DecodeUriForSpringFilter() {
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
        decodeServletPathForSpringUrlMapping(request);
        String path = request.getRequestURI();
        LOGGER.info("request URI: {} ", path);
        if (StringUtils.isBlank(path) || "/".equals(path)) {
            writeError400Response(response);
            return false;
        }        
        String uri = path.substring(request.getContextPath().length());
        if (StringUtils.isBlank(uri)) {
            writeError400Response(response);
            return false;
        }       
        return true;
    }

    /**
     * decode servletPath with UTF-8 for spring url mapping.
     * 
     * @param request
     *            HttpServletRequest.
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException.
     */
    private static void
            decodeServletPathForSpringUrlMapping(HttpServletRequest request)
                    throws UnsupportedEncodingException {
        request.setCharacterEncoding(StringUtil.CHAR_SET_ISO8859);
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
     * @return this class name.
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

}
