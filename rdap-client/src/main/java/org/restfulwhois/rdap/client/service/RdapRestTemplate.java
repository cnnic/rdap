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
package org.restfulwhois.rdap.client.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.common.dto.SimpleHttpStatusCode;

/**
 * Send http request.
 * @author M.D.
 *
 */
public class RdapRestTemplate {
    /**
     * content-type and accept
     */
    private String mediaType;
    /**
     * connection timeout
     */
    private int connectTimeout;
    /**
     * read timeout
     */
    private int readTimeout;

    /**
     * Execute http request
     * @param httpMethod GET or DELETE
     * @param url url
     * @return RdapResponse
     * @throws RdapClientException if fail to execute http request or fail to 
     * convert json to object
     */
    public RdapResponse execute(HttpMethodType httpMethod, URL url)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), null);
    }

    /**
     * Execute http request
     * @param httpMethod POST or PUT
     * @param url url
     * @param body request body
     * @return RdapResponse
     * @throws RdapClientException if fail to execute http request or fail to 
     * convert json to object
     */
    public RdapResponse execute(HttpMethodType httpMethod, URL url, String body)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), body);
    }

    /**
     * Create HttpURLConnection instance
     * @param url url
     * @param httpMethod GET,PUT,POST or DELETE
     * @return HttpURLConnection
     * @throws RdapClientException if fail to create
     */
    protected HttpURLConnection prepareExecute(URL url,
            HttpMethodType httpMethod) throws RdapClientException {
        try {
            HttpURLConnection httpUrlConn = (HttpURLConnection) url
                    .openConnection();
            httpUrlConn.setConnectTimeout(connectTimeout);
            httpUrlConn.setReadTimeout(readTimeout);
            httpUrlConn.setUseCaches(false);
            httpUrlConn.setInstanceFollowRedirects(true);
            httpUrlConn.setRequestMethod(httpMethod.name());
            httpUrlConn.setDoInput(true);
            if (httpMethod.equals(HttpMethodType.GET)
                    || httpMethod.equals(HttpMethodType.DELETE)) {
                httpUrlConn.setDoOutput(false);
            } else {
                httpUrlConn.setDoOutput(true);
                httpUrlConn.setRequestProperty("content-type", mediaType);
            }
            httpUrlConn.setRequestProperty("accept", mediaType);
            return httpUrlConn;
        } catch (IOException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    /**
     * Send request
     * @param urlConnection HttpURLConnection
     * @param body request body
     * @return RdapResponse
     * @throws RdapClientException if fail to send
     */
    protected RdapResponse doExecute(HttpURLConnection urlConnection,
            String body) throws RdapClientException {
        RdapResponse response = new RdapResponse();
        try {
            urlConnection.connect();
            if (urlConnection.getDoOutput() && body != null) {
                OutputStream out = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(out, "utf-8"));
                writer.write(body);
                writer.flush();
                writer.close();
                out.close();
            }
            int code = urlConnection.getResponseCode();
            response.setResponseCode(code);
            response.setResponseMessage(urlConnection.getResponseMessage());
            response.setMethodType(HttpMethodType.valueOf(urlConnection.getRequestMethod()));
            try {
                SimpleHttpStatusCode.valueOf(code);
                if (code == 200) {
                    response.setIn(urlConnection.getInputStream());
                } else {
                    response.setIn(urlConnection.getErrorStream());
                }
            } catch (IllegalArgumentException iae) {
            }
        } catch (IOException io) {
            throw new RdapClientException(io.getMessage());
        }

        return response;
    }

    /**
     * mediaType getter
     * @return mediaType
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * mediaType setter
     * @param mediaType String
     */
    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    /**
     * connectTimeout getter
     * @return connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * connectTimeout setter
     * @param connectTimeout milliseconds
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * readTimeout getter
     * @return readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * readTimeout setter
     * @param readTimeout milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}
