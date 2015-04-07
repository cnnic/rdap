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