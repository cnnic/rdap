package org.restfulwhois.rdap.client.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.restfulwhois.rdap.client.exception.RdapClientException;

public class SSLUtil {

    public static KeyStore loadKeyStoreWithKSFile(String filePath,
            String password) throws RdapClientException {
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

    public static TrustManager[] createManagersWithKeyStroe(KeyStore ks)
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
}