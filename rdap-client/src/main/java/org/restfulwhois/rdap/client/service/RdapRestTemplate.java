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

public class RdapRestTemplate {
    private String MEDIA_TYPE;
    private int connectTimeout;
    private int readTimeout;

    public RdapRestTemplate() {
        this.MEDIA_TYPE = "application/json;charset=UTF-8";
    }

    public RdapResponse execute(HttpMethodType httpMethod, URL url)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), null);
    }

    public RdapResponse execute(HttpMethodType httpMethod, URL url, String body)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), body);
    }

    private HttpURLConnection prepareExecute(URL url, HttpMethodType httpMethod)
            throws RdapClientException {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url
                    .openConnection();
            httpURLConnection.setConnectTimeout(connectTimeout);
            httpURLConnection.setReadTimeout(readTimeout);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod(httpMethod.name());
            httpURLConnection.setDoInput(true);
            if (httpMethod.equals(HttpMethodType.GET)
                    || httpMethod.equals(HttpMethodType.DELETE)) {
                httpURLConnection.setDoOutput(false);
                httpURLConnection.setInstanceFollowRedirects(false);
            } else {
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setInstanceFollowRedirects(true);
                httpURLConnection
                        .setRequestProperty("content-type", MEDIA_TYPE);
            }
            return httpURLConnection;
        } catch (IOException e) {
            throw new RdapClientException(e.getMessage());
        }

    }

    private RdapResponse doExecute(HttpURLConnection httpURLConnection,
            String body) throws RdapClientException {
        RdapResponse response = new RdapResponse();
        try {
            httpURLConnection.connect();
            if (httpURLConnection.getDoOutput() && body != null) {
                OutputStream out = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(out, "utf-8"));
                writer.write(body);
                writer.flush();
                writer.close();
                out.close();
            }
            int code = httpURLConnection.getResponseCode();
            response.setResponseCode(code);
            response.setResponseMessage(httpURLConnection.getResponseMessage());
            try {
                SimpleHttpStatusCode.valueOf(code);
                if (code == 200) {
                    response.setIn(httpURLConnection.getInputStream());
                } else {
                    response.setIn(httpURLConnection.getErrorStream());
                }
            } catch (IllegalArgumentException iae) {
            }
        } catch (IOException io) {
            throw new RdapClientException(io.getMessage());
        }

        return response;
    }

    public String getMEDIA_TYPE() {
        return MEDIA_TYPE;
    }

    public void setMEDIA_TYPE(String mEDIA_TYPE) {
        MEDIA_TYPE = mEDIA_TYPE;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

}