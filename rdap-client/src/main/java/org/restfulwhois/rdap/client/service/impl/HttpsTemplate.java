package org.restfulwhois.rdap.client.service.impl;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapResponse;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.SSLUtil;
import org.restfulwhois.rdap.common.dto.SimpleHttpStatusCode;

public class HttpsTemplate extends RdapRestTemplate {

    @Override
    protected URLConnection prepareExecute(URL url, HttpMethodType httpMethod)
            throws RdapClientException {
        try {
            HttpsURLConnection httpsURLConn = (HttpsURLConnection) url
                    .openConnection();
            httpsURLConn.setConnectTimeout(connectTimeout);
            httpsURLConn.setReadTimeout(readTimeout);
            httpsURLConn.setUseCaches(false);
            httpsURLConn.setRequestMethod(httpMethod.name());
            httpsURLConn.setDoInput(true);
            if (httpMethod.equals(HttpMethodType.GET)
                    || httpMethod.equals(HttpMethodType.DELETE)) {
                httpsURLConn.setDoOutput(false);
                httpsURLConn.setInstanceFollowRedirects(false);
            } else {
                httpsURLConn.setDoOutput(true);
                httpsURLConn.setInstanceFollowRedirects(true);
                httpsURLConn.setRequestProperty("content-type", MEDIA_TYPE);
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, SSLUtil.createManagersWithTrustAll(), null);
            httpsURLConn.setSSLSocketFactory(sslContext.getSocketFactory());
            return httpsURLConn;
        } catch (IOException | NoSuchAlgorithmException
                | KeyManagementException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    @Override
    protected RdapResponse doExecute(URLConnection urlConnection, String body)
            throws RdapClientException {
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

}