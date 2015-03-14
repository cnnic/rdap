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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Filter helper to write response and HTTP STATUS.
 * <p>
 * FilterHelper.writeResponse() is typically used when error occured, writing
 * error response.
 * 
 * @author jiashuo
 * 
 */
public final class FilterHelper {
    /**
     * constructor.
     */
    private FilterHelper() {

    }

    /**
     * check if is update URI.
     * 
     * @param uri
     *            URI.
     * @return true if is update URI, false if not.
     */
    public static boolean isUpdateUri(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if(StringUtils.equals(contextPath, "/")){
            contextPath = StringUtils.EMPTY;
        }
        return StringUtils.startsWith(uri, contextPath + "/u/");
    }

    /**
     * write response for responseEntity.
     * 
     * @param responseEntity
     *            response entity for error message.
     * @param response
     *            HttpServletResponse.
     * @throws IOException
     *             IOException.
     */
    public static void writeResponse(ResponseEntity responseEntity,
            HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "application/rdap+json");
        HttpHeaders headers = responseEntity.getHeaders();
        Set<String> headerKeys = headers.keySet();
        for (String headerKey : headerKeys) {
            List<String> headerValues = headers.get(headerKey);
            for (String headerValue : headerValues) {
                response.setHeader(headerKey, headerValue);
            }
        }
        response.setCharacterEncoding(StringUtil.CHAR_SET_UTF8);
        PrintWriter writer = response.getWriter();
        response.setStatus(responseEntity.getStatusCode().value());
        String jsonStr = beanToJSON(responseEntity.getBody());
        writer.print(jsonStr);
    }

    /**
     * convert bean to JSON format String.
     * 
     * @param object
     *            bean object.
     * @return string of json.
     * @throws IOException
     *             exception of io.
     */
    private static String beanToJSON(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
