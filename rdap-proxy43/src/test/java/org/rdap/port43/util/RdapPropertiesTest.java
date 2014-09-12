package org.rdap.port43.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.restfulwhois.rdap.port43.util.RdapProperties;

/**
 * RdapProperties test.
 * 
 * @author jiashuo
 * 
 */
public class RdapPropertiesTest {

    @Test
    public void testGetProp() {
        assertNotNull(RdapProperties.getMinSecondsAccessInterval());
        assertNotNull(RdapProperties.getServicePort());
        assertNotNull(RdapProperties.getManagePort());
        assertNotNull(RdapProperties.getRdapServerBaseUrl());
        assertNotNull(RdapProperties.getResponseFormater());
    }
}
