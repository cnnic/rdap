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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.exception.DecodeException;
import org.restfulwhois.rdap.common.model.base.QueryUri;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * request util.
 * 
 * @author jiashuo
 * 
 */
public class RequestUtil {
    /**
     * constructor.
     */
    private RequestUtil() {
        super();
    }

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RequestUtil.class);

    /**
     * validate last split URI is expectedQueryUri.
     * 
     * @param request
     *            request
     * @param expectedQueryUri
     *            expectedQueryUri.
     * @return true if is expected URI, false if not.
     */
    public static boolean validateLastSplitUri(HttpServletRequest request,
            QueryUri expectedQueryUri) {
        try {
            String lastSpliInURI = getLastSplitInURI(request);
            if (!expectedQueryUri.getNameWithoutUriSplitter().equals(
                    lastSpliInURI)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
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
    public static String getFirstParameter(HttpServletRequest request,
            final String[] strParamOrg) {

        String strQuery = request.getQueryString();
        LOGGER.debug("getFirstParameter, query:" + strQuery + ",strParamOrg:"
                + strParamOrg);

        if (StringUtils.isBlank(strQuery)) {
            return null;
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
                        return strParamName[0];
                    }
                }
            }
        }
        return null;
    }

    /**
     * get last split in URI.
     * <p>
     * For spring will ignore ending params, eg: /cnnic.cn%1a.
     * <p>
     * eg: for URI '/domain/cnnic.cn' return 'cnnic.cn'.
     * 
     * @param request
     *            request.
     * @return param value.
     * @throws DecodeException
     *             DecodeException.
     */
    public static String getLastSplitInURI(HttpServletRequest request)
            throws DecodeException {
        String path = request.getRequestURI();
        String result = StringUtils.substringAfterLast(path, "/");
        result = DomainUtil.urlDecode(result);
        LOGGER.debug("last split in URI: {}", result);
        return result;
    }

    /**
     * get last second split in URI.
     * <p>
     * For spring will ignore ending params, eg: /1.1.1.1%1a/32.
     * <p>
     * eg: for URI '/1.1.1.1%1a/32' return '1.1.1.1%1a'.
     * 
     * @param request
     *            request.
     * @return param value.
     */
    public static String getLastSecondSplitInURI(HttpServletRequest request) {
        String path = request.getRequestURI();
        String substringBeforeLast = StringUtils.substringBeforeLast(path, "/");
        String result =
                StringUtils.substringAfterLast(substringBeforeLast, "/");
        LOGGER.debug("last second split in URI: {}", result);
        return result;
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
    public static String getParameter(HttpServletRequest request, String name) {
        String[] values = request.getParameterValues(name);
        if (null == values || values.length < 1) {
            return null;
        }
        return values[0];
    }
}
