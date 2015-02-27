package org.restfulwhois.rdap.client.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.impl.HttpsTemplate.TrustType;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.SSLUtil;
import org.restfulwhois.rdap.common.dto.SimpleHttpStatusCode;

public class RdapRestTemplate {
    private String MEDIA_TYPE;
    private int connectTimeout;
    private int readTimeout;
    private TrustType trustType;
    private String filePath;
    private String password;

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

    private URLConnection prepareExecute(URL url,
            HttpMethodType httpMethod) throws RdapClientException{
        try {
            URLConnection urlConn = url.openConnection();
            urlConn.setConnectTimeout(connectTimeout);
            urlConn.setReadTimeout(readTimeout);
            urlConn.setUseCaches(false);
            
            urlConn.setDoInput(true);
            if (httpMethod.equals(HttpMethodType.GET)
                    || httpMethod.equals(HttpMethodType.DELETE)) {
                urlConn.setDoOutput(false);
            } else {
                urlConn.setDoOutput(true);
                urlConn.setRequestProperty("content-type", MEDIA_TYPE);
            }
            
            urlConn.setRequestMethod(httpMethod.name());
            urlConn.setInstanceFollowRedirects(true);
            if (!trustType.equals(TrustType.DEFAULT)) {
                SSLContext sslContext = SSLContext.getInstance("TLS");
                KeyStore ks = SSLUtil.loadKeyStore(filePath, password);
                TrustManager[] managers = SSLUtil.getTrustManager(ks);
                sslContext.init(null, managers, null);
                httpsURLConn.setSSLSocketFactory(sslContext.getSocketFactory());
            }
            return httpsURLConn;
        } catch (IOException e) {
            throw new RdapClientException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RdapClientException(e.getMessage());
        } catch (KeyManagementException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    private RdapResponse doExecute(URLConnection urlConnection,
            String body) throws RdapClientException{
        HttpsURLConnection httpsURLConn = (HttpsURLConnection) urlConnection;
        RdapResponse response = new RdapResponse();
        try {
            httpsURLConn.connect();
            if (httpsURLConn.getDoOutput() && body != null) {
                OutputStream out = httpsURLConn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(out, "utf-8"));
                writer.write(body);
                writer.flush();
                writer.close();
                out.close();
            }
            int code = httpsURLConn.getResponseCode();
            response.setResponseCode(code);
            response.setResponseMessage(httpsURLConn.getResponseMessage());
            try {
                SimpleHttpStatusCode.valueOf(code);
                if (code == 200) {
                    response.setIn(httpsURLConn.getInputStream());
                } else {
                    response.setIn(httpsURLConn.getErrorStream());
                }
            } catch (IllegalArgumentException iae) {
            }
        } catch (IOException io) {
            throw new RdapClientException(io.getMessage());
        }

        return response;
    }

    public enum TrustType{
        DEFAULT, KEYSTORE;
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