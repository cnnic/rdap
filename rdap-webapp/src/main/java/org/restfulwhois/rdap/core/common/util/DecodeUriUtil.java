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
package org.restfulwhois.rdap.core.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.WebUtils;

/**
 * This class is used to decode uri.
 * @author zhanyq
 *
 */
public final class DecodeUriUtil {
   /**
     * default constructor.
     */
    private DecodeUriUtil() {
        super();
    }
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DecodeUriUtil.class);    
    /**
    * Decode uri .
    * 
    * @param request
    *            request.
    * @return decodeUri.
    */
   public static String decodeUri(HttpServletRequest request) {
        String decodeUri = StringUtils.EMPTY;
        try {
             decodeServletPathForSpringUrlMapping(request);
             String path = request.getRequestURI();
             LOGGER.info("request URI: {} ", path);
             if (StringUtils.isBlank(path) || "/".equals(path)) {           
                  return null;
              }        
             String uri = path.substring(request.getContextPath().length());
             if (StringUtils.isBlank(uri)) {           
                return null;
             }        
             decodeUri = urlDecode(uri);       
        } catch (Exception e) {
             LOGGER.error(e.getMessage());
             return null;
        }
        return decodeUri;

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
}
