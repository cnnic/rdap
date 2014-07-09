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

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.common.util.RestResponseUtil;

/**
 * filter HTTP request.
 * 
 * @author jiashuo
 * 
 */
public class HttpRequestFilter implements RdapFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthenticationFilter.class);

    /**
     * rdap content type.
     */
    private static final String VALID_CONTENT_TYPE = "application/rdap+json";
    /**
     * allow methods.
     */
    private static final List<String> ALLOW_METHODS = new ArrayList<String>();
    /**
     * init allow methods.
     */
    static {
        ALLOW_METHODS.add("GET");
    }

    /**
     * constructor.
     */
    public HttpRequestFilter() {
        super();
        LOGGER.info("init RDAP filter:{}", this.getName());
    }
    /**
     * do pre process request method.
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
        boolean httpMethodIsValid = httpMethodIsValid(request);
        if (!httpMethodIsValid) {
            ResponseEntity<ErrorMessage> responseEntity =
                    RestResponseUtil.createResponse405();
            FilterHelper.writeResponse(responseEntity, response);
            return false;
        }
        boolean contentTypeIsValid = contentTypeIsValid(request);
        if (!contentTypeIsValid) {
            ResponseEntity<ErrorMessage> responseEntity =
                    RestResponseUtil.createResponse415();
            FilterHelper.writeResponse(responseEntity, response);
            return false;
        }
        return true;
    }

    /**
     * check if content type is valid.
     * 
     * @param request
     *            HttpServletRequest
     * @return true if valid, false if not
     */
    private boolean contentTypeIsValid(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        if (StringUtils.isBlank(acceptHeader)) {
            return true;
        }
        if (VALID_CONTENT_TYPE.equals(acceptHeader)
                || acceptHeader.startsWith(VALID_CONTENT_TYPE + ";")) {
            return true;
        }
        return false;
    }

    /**
     * check if HTTP METHOD is valid.
     * 
     * @param request
     *            HttpServletRequest
     * @return true if valid, false if not
     */
    private boolean httpMethodIsValid(HttpServletRequest request) {
        String method = request.getMethod();
        boolean httpMethodIsValid = ALLOW_METHODS.contains(method);
        return httpMethodIsValid;
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
