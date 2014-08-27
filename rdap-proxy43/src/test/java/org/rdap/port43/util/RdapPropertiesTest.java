package org.rdap.port43.util;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

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
        assertNotNull(RdapProperties.getPort());
        assertNotNull(RdapProperties.getRdapServerBaseUrl());
    }

}
