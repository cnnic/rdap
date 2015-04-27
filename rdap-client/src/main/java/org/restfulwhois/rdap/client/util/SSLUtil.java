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
package org.restfulwhois.rdap.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.restfulwhois.rdap.client.exception.RdapClientException;

/**
 * Operations on SSL setting
 * @author M.D.
 *
 */
public final class SSLUtil {
    
    /**
     * constructor
     */
    private SSLUtil(){}

    /**
     * Load a .keystore file with file path and it's password
     * @param filePath the .keystore file path
     * @param password the .keystore file password
     * @return KeyStore
     * @throws RdapClientException if there is an I/O problem or the .keystore 
     * file can not be found or the .keystore file can not be loaded then the 
     * RdapClientException will be thrown
     */
    public static KeyStore loadKeyStore(String filePath, String password)
            throws RdapClientException {
        KeyStore ks;
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream in = new FileInputStream(filePath);
            ks.load(in, password.toCharArray());
        } catch (KeyStoreException e) {
            throw new RdapClientException(e.getMessage());
        } catch (CertificateException e) {
            throw new RdapClientException(e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new RdapClientException(e.getMessage());
        } catch (IOException e) {
            throw new RdapClientException(e.getMessage());
        }
        return ks;
    }

    /**
     * Get trust managers from ks.
     * @param ks loaded keystore
     * @return TrustManager
     * @throws RdapClientException if this operation fails
     */
    public static TrustManager[] getTrustManager(KeyStore ks)
            throws RdapClientException {

        TrustManager[] tm = null;
        try {
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            tm = tmf.getTrustManagers();
        } catch (NoSuchAlgorithmException e) {
            throw new RdapClientException(e.getMessage());
        } catch (KeyStoreException e) {
            throw new RdapClientException(e.getMessage());
        }

        return tm;
    }

    /**
     * Get SSLSocketFactory from managers
     * @param managers TrustManager
     * @return SSLSocketFactory
     * @throws RdapClientException if this operation fails
     */
    public static SSLSocketFactory getSSLSocketFactory(TrustManager[] managers)
            throws RdapClientException {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, managers, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            throw new RdapClientException(e.getMessage());
        } catch (KeyManagementException e) {
            throw new RdapClientException(e.getMessage());
        }

    }

}
