package org.restfulwhois.rdap.client.service.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapResponse;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.common.dto.SimpleHttpStatusCode;

public class HttpTemplate extends RdapRestTemplate {

    @Override
    public HttpURLConnection prepareExecute(URL url, HttpMethodType httpMethod)
            throws RdapClientException {
        try {
            HttpURLConnection httpURLConn = (HttpURLConnection) url
                    .openConnection();
            httpURLConn.setConnectTimeout(connectTimeout);
            httpURLConn.setReadTimeout(readTimeout);
            httpURLConn.setUseCaches(false);
            httpURLConn.setRequestMethod(httpMethod.name());
            httpURLConn.setDoInput(true);
            if (httpMethod.equals(HttpMethodType.GET)
                    || httpMethod.equals(HttpMethodType.DELETE)) {
                httpURLConn.setDoOutput(false);
                httpURLConn.setInstanceFollowRedirects(false);
            } else {
                httpURLConn.setDoOutput(true);
                httpURLConn.setInstanceFollowRedirects(true);
                httpURLConn.setRequestProperty("content-type", MEDIA_TYPE);
            }
            return httpURLConn;
        } catch (IOException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    @Override
    protected RdapResponse doExecute(URLConnection urlConnection, String body)
            throws RdapClientException {
        HttpURLConnection httpURLConn = (HttpURLConnection) urlConnection;
        RdapResponse response = new RdapResponse();
        try {
            httpURLConn.connect();
            if (httpURLConn.getDoOutput() && body != null) {
                OutputStream out = httpURLConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(out, "utf-8"));
                writer.write(body);
                writer.flush();
                writer.close();
                out.close();
            }
            int code = httpURLConn.getResponseCode();
            response.setResponseCode(code);
            response.setResponseMessage(httpURLConn.getResponseMessage());
            try {
                SimpleHttpStatusCode.valueOf(code);
                if (code == 200) {
                    response.setIn(httpURLConn.getInputStream());
                } else {
                    response.setIn(httpURLConn.getErrorStream());
                }
            } catch (IllegalArgumentException iae) {
            }
        } catch (IOException io) {
            throw new RdapClientException(io.getMessage());
        }

        return response;
    }

}