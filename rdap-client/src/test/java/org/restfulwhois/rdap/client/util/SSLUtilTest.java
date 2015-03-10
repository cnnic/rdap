package org.restfulwhois.rdap.client.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.junit.Test;
import org.restfulwhois.rdap.client.exception.RdapClientException;

public class SSLUtilTest {
    String path = "src/test/java/org/restfulwhois/rdap/client/util/test.keystore";
    
    @Test
    public void test_loadKeyStore() throws KeyStoreException{
        KeyStore keyStore;
        try {
            keyStore = SSLUtil.loadKeyStore(path, "123456");
        } catch (RdapClientException e) {
            keyStore = null;
        }
        assertEquals("test", keyStore.aliases().nextElement());
    }
    
    @Test
    public void test_getTrustManager() throws KeyStoreException{
        TrustManager[] tm;
        try {
            
            tm = SSLUtil.getTrustManager(SSLUtil.loadKeyStore(path, "123456"));
        } catch (RdapClientException e) {
            tm = null;
        }
        assertNotNull(tm);
    }
    
    @Test
    public void test_getSSLSocketFactory() throws KeyStoreException{
        SSLSocketFactory factory;
        try {
            
            factory = SSLUtil.getSSLSocketFactory(SSLUtil.getTrustManager(SSLUtil.loadKeyStore(path, "123456")));
        } catch (RdapClientException e) {
            factory = null;
        }
        assertNotNull(factory);
    }
    
}
