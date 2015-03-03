package org.restfulwhois.rdap.client.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.SSLUtil;
import org.restfulwhois.rdap.client.util.StringUtil;
import org.restfulwhois.rdap.client.util.URLUtil;
import org.restfulwhois.rdap.common.dto.SimpleHttpStatusCode;

public class RdapRestTemplate {
    private String mediaType;
    private int connectTimeout;
    private int readTimeout;
    private String filePath;
    private String password;

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
            HttpURLConnection httpUrlConn = (HttpURLConnection)url.openConnection();
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

            if (URLUtil.isHttps(url)) {
                this.setSSLSocketFactory((HttpsURLConnection) httpUrlConn);
            }
            httpUrlConn.setRequestProperty("cookie", "_c_sso_tgc=TGT-13-De7nEEK7PpXQvHnfi4xEHbPbPuwvB1A2PYrblk7OskkOgVeD6W-cas; JSESSIONID=0BD318392E4000F49B8E8EBCD4F70C6E-n1.miaodi");
            return httpUrlConn;
        } catch (IOException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    private RdapResponse doExecute(HttpURLConnection urlConnection, String body)
            throws RdapClientException {
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

    private void setSSLSocketFactory(HttpsURLConnection httpsUrlConn)
            throws RdapClientException {
        if (!StringUtil.isEmpty(filePath)) {
            KeyStore ks = SSLUtil.loadKeyStore(filePath, password);
            TrustManager[] managers = SSLUtil.getTrustManager(ks);
            httpsUrlConn.setSSLSocketFactory(SSLUtil
                    .getSSLSocketFactory(managers));
        }
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}