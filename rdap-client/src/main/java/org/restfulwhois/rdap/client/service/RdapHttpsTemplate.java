package org.restfulwhois.rdap.client.service;

import java.net.URL;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManager;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.SSLUtil;
import org.restfulwhois.rdap.client.util.StringUtil;

/**
 * 
 * @author M.D.
 *
 */
public class RdapHttpsTemplate extends RdapRestTemplate {
    /**
     * .keystore file path
     */
    private String filePath;
    /**
     * .keystore file password
     */
    private String password;

    /**
     * Execute http request with no request body<P>
     * for GET and DELETE
     * @param url the url of aim
     * @param httpMethod the request method: GET or DELETE
     * @return RdapResponse
     * @throws RdapClientException <p>It may be throw RdapClientException 
     * during SSL setting
     */
    public RdapResponse execute(HttpMethodType httpMethod, URL url)
            throws RdapClientException {
        HttpsURLConnection httpsUrlConn = (HttpsURLConnection) prepareExecute(
                url, httpMethod);
        setSSLSocketFactory(httpsUrlConn);
        return doExecute(httpsUrlConn, null);
    }

    /**
     * Execute http request with request body
     * @param url the url of aim
     * @param body request body
     * @param httpMethod request method: POST or PULL
     * @return RdapResponse
     * @throws RdapClientException <p>It may be throw RdapClientException 
     * during SSL setting
     */
    public RdapResponse execute(HttpMethodType httpMethod, URL url, String body)
            throws RdapClientException {
        HttpsURLConnection httpsUrlConn = (HttpsURLConnection) prepareExecute(
                url, httpMethod);
        setSSLSocketFactory(httpsUrlConn);
        return doExecute(this.prepareExecute(url, httpMethod), body);
    }

    /**
     * Get SSLSocketFactory from TrustManager and set it into httpsUrlConn
     * @param httpsUrlConn HttpsURLConnection
     * @throws RdapClientException <p>It may be throw RdapClientException 
     * during SSL setting
     */
    private void setSSLSocketFactory(HttpsURLConnection httpsUrlConn)
            throws RdapClientException {
        if (!StringUtil.isEmpty(filePath)) {
            KeyStore ks = SSLUtil.loadKeyStore(filePath, password);
            TrustManager[] managers = SSLUtil.getTrustManager(ks);
            httpsUrlConn.setSSLSocketFactory(SSLUtil
                    .getSSLSocketFactory(managers));
        }
    }

    /**
     * filePath getter
     * @return filePath
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * filePath setter
     * @param filePath the string for .keystore file path
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * password getter
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * password setter
     * @param password the password for .keystore file
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
