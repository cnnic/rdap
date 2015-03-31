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
    private String mediaType;
    private int connectTimeout;
    private int readTimeout;

    public RdapResponse execute(HttpMethodType httpMethod, URL url)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), null);
    }

    public RdapResponse execute(HttpMethodType httpMethod, URL url, String body)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), body);
    }

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

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
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