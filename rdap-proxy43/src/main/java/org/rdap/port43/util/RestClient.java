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
package org.rdap.port43.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.rdap.port43.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * rest client. TODO:refactor.
 * 
 * @author weijunkai,jiashuo
 * 
 */
public final class RestClient {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JsonUtil.class);

    /**
     * instance.
     */
    private static RestClient restClient = new RestClient();

    /**
     * get singleton instance.
     * 
     * @return
     */
    public static RestClient getInstance() {
        return restClient;
    }

    /**
     * 
     * @param url
     * @return
     */
    public RestResponse execute(String url) throws ServiceException {
        CloseableHttpClient client = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setRedirectsEnabled(false).build();
        HttpGet request = new HttpGet(url);
        request.setConfig(requestConfig);
        request.setHeader("Accept", "application/rdap+json");
        HttpResponse response = null;
        String responseBody = StringUtils.EMPTY;
        int statusCode = HttpStatus.SC_OK;
        String locationHeader = StringUtils.EMPTY;
        try {
            response = client.execute(request);
            HttpEntity entity = response.getEntity();
            statusCode = response.getStatusLine().getStatusCode();
            Header[] headers = response.getHeaders("location");
            if (null != headers && headers.length > 0) {
                locationHeader = headers[0].getValue();
            }
            if (entity != null) {
                InputStream inStream = entity.getContent();
                responseBody = convertStreamToString(inStream);
                inStream.close();
            }
        } catch (Exception ex) {
            LOGGER.error("request RDAP server error:{}", ex);
            throw new ServiceException("request RDAP server error");
        }
        RestResponse result = new RestResponse();
        result.setBody(responseBody);
        result.setStatusCode(statusCode);
        result.setLocationHeader(locationHeader);
        return result;
    }

    /**
     * convert the inputStream to string.
     * 
     * @param inStream
     *            input stream.
     * @return string.
     */
    private static String convertStreamToString(InputStream inStream) {
        BufferedReader reader = null;
        try {
            reader =
                    new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        StringBuilder strBuilder = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                strBuilder.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strBuilder.toString();
    }

}
