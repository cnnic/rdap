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
