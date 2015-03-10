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

public class SSLUtil {

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