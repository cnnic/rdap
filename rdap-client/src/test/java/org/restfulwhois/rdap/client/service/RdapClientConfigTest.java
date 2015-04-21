package org.restfulwhois.rdap.client.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RdapClientConfigTest {
    
    @Test
    public void test_isHttps_false(){
        RdapClientConfig config = new RdapClientConfig("http://example.com");
        assertEquals(false, config.isHttps());
    }
    
    @Test
    public void test_isHttps_true(){
        RdapClientConfig config = new RdapClientConfig("https://example.com");
        assertEquals(true, config.isHttps());
    }
}
