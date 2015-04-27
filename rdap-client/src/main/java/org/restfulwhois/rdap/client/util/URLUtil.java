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
package org.restfulwhois.rdap.client.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.restfulwhois.rdap.client.exception.RdapClientException;

/**
 * This class is mainly used to make URL with uri or parameter
 * @author M.D.
 *
 */
public final class URLUtil {
    /**
     * "/" character
     */
    private static final String SLASH = "/";
    /**
     * "&" character
     */
    private static final String SPLIT = "&";
    /**
     * "?" character
     */
    private static final String SEPARATE = "?";
    /**
     * "=" character
     */
    private static final String ASSIGN = "=";

    /**
     * Default constructor
     */
    private URLUtil(){}
    
    /**
     * Creates a URL object from the url string.
     * @param url the String to parse as a URL
     * @return URL 
     * @throws RdapClientException  if no protocol is specified, 
     * or an unknown protocol is found, or url is null.
     */
    public static URL makeURL(String url) throws RdapClientException {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    /**
     * Creates a URL object from the url string and path string.
     * @param url the String to parse as a URL
     * @param path the String marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPath(String url, String... path)
            throws RdapClientException {

        if (path != null && path.length != 0) {
            return makeURLWithPath(url, Arrays.asList(path));
        } else {
            return makeURL(url);
        }
    }

    /**
     * Creates a URL object from the url string and path list.
     * @param url the String to parse as a URL
     * @param path the List marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPath(String url, List<String> path)
            throws RdapClientException {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (path != null && path.size() != 0) {
            for (String p : path) {
                if (!StringUtil.isEmpty(p)){
                    urlBuilder.append(SLASH).append(p);
                }
            }
        }
        try {
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    /**
     * Creates a URL object from the url string and parameter map.
     * @param url the String to parse as a URL
     * @param param parameter map
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithParam(String url, Map<String, String> param)
            throws RdapClientException {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (param != null) {
            Set<String> keys = param.keySet();
            if (keys.size() != 0) {
                StringBuilder paramBuilder = new StringBuilder();
                for (String key : keys) {
                    paramBuilder.append(key).append(ASSIGN)
                            .append(param.get(key)).append(SPLIT);
                }
                urlBuilder.append(SEPARATE).append(
                        paramBuilder.substring(0, paramBuilder.length() - 1));
            }
        }
        try {
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    /**
     * Creates a URL object from the url string ,path string and parameter map.
     * @param url the String to parse as a URL
     * @param param parameter map
     * @param path the String marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPathAndParam(String url,
            Map<String, String> param, String... path)
            throws RdapClientException {

        if (path != null && path.length != 0){
            return makeURLWithPathAndParam(url, param, Arrays.asList(path));
        }else{
            return makeURLWithParam(url, param);
        }
    }

    /**
     * Creates a URL object from the url string ,path string and parameter map.
     * @param url the String to parse as a URL
     * @param param parameter map
     * @param path the List marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPathAndParam(String url,
            Map<String, String> param, List<String> path)
            throws RdapClientException {

        StringBuilder urlBuilder = new StringBuilder(url);
        if (path != null && path.size() != 0) {
            for (String p : path) {
                if (!StringUtil.isEmpty(p)){
                    urlBuilder.append(SLASH).append(p);
                }
            }
        }
        return makeURLWithParam(urlBuilder.toString(), param);
    }

    /**
     * It will return true if it used https protocol.
     * @param url url
     * @return boolean
     */
    public static boolean isHttps(URL url) {
        String protocol = url.getProtocol();
        if (protocol.equalsIgnoreCase(ProtocolType.HTTPS.name())) {
            return true;
        }
        return false;
    }

    /**
     * This enumeration lists two protocol types are http and https.
     * @author M.D.
     *
     */
    public enum ProtocolType {
        /**
         * http
         */
        HTTP, 
        /**
         * https
         */
        HTTPS;
    }
}
