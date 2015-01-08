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
package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import javax.xml.bind.DatatypeConverter;

import org.junit.Test;
import org.junit.internal.ArrayComparisonFailure;
import org.restfulwhois.rdap.common.model.IpVersion;

/**
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class ArpaUtilTest {

    @Test
    public void test_generate_ARPA() {
        // NetworkInBytes networkInBytes = IpUtil.parseArpa("f.f.ip6.arpa");
        NetworkInBytes networkInBytes =
                ArpaUtil.parseArpa("1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa");
        String hex1 =
                DatatypeConverter.printHexBinary(networkInBytes
                        .getStartAddress());
        // System.err.println("0x" + hex1);
        String hex2 =
                DatatypeConverter
                        .printHexBinary(networkInBytes.getEndAddress());
        // System.err.println("0x" + hex2);
    }

    @Test
    public void test_ARPA_V4() {
        NetworkInBytes networkInBytes =
                ArpaUtil.parseArpa("2.0.0.in-addr.arpa");
        assertEquals(IpVersion.V4, networkInBytes.getIpVersion());
        assertEquals("0.0.2.0", networkInBytes.getStartAddressAsString());
        assertArrayEquals("", IpV4.toByteArray("0.0.2.0"),
                networkInBytes.getStartAddress());
        assertEquals("0.0.2.255", networkInBytes.getEndAddressAsString());
        assertArrayEquals("", IpV4.toByteArray("0.0.2.255"),
                networkInBytes.getEndAddress());
    }

    @Test
    public void test_ARPA_V6() {
        doTestArpaV6("F.0.0.0.1.ip6.arpa", "1000:f000::",
                "1000:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        doTestArpaV6("F.0.0.0.ip6.arpa", "f::",
                "f:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        doTestArpaV6("F.0.0.ip6.arpa", "f0::",
                "ff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        doTestArpaV6("F.ip6.arpa", "f000::",
                "ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        doTestArpaV6(
                "F.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa",
                "::f0", "::ff");
        doTestArpaV6(
                "F.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.8.ip6.arpa",
                "8000::f0", "8000::ff");
    }

    private void doTestArpaV6(String arpa, String expectedStartAddress,
            String expectedEndAddress) throws ArrayComparisonFailure {
        NetworkInBytes networkInBytes = ArpaUtil.parseArpa(arpa);
        assertEquals(IpVersion.V6, networkInBytes.getIpVersion());
        assertEquals(expectedStartAddress,
                networkInBytes.getStartAddressAsString());
        assertArrayEquals("", IpV6.toByteArray(expectedStartAddress),
                networkInBytes.getStartAddress());
        assertEquals(expectedEndAddress, networkInBytes.getEndAddressAsString());
        assertArrayEquals("", IpV6.toByteArray(expectedEndAddress),
                networkInBytes.getEndAddress());
    }
}