package org.restfulwhois.rdap.client.service;

import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.SSLUtil;
import org.restfulwhois.rdap.client.util.StringUtil;

public class RdapHttpsTemplate extends RdapRestTemplate {
    private String filePath;
    private String password;

    public RdapResponse execute(HttpMethodType httpMethod, URL url)
            throws RdapClientException {
        HttpsURLConnection httpsUrlConn = (HttpsURLConnection) prepareExecute(
                url, httpMethod);
        setSSLSocketFactory(httpsUrlConn);
        return doExecute(httpsUrlConn, null);
    }

    public RdapResponse execute(HttpMethodType httpMethod, URL url, String body)
            throws RdapClientException {
        HttpsURLConnection httpsUrlConn = (HttpsURLConnection) prepareExecute(
                url, httpMethod);
        setSSLSocketFactory(httpsUrlConn);
        return doExecute(this.prepareExecute(url, httpMethod), body);
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
