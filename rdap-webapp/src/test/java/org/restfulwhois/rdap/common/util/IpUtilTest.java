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
import org.restfulwhois.rdap.core.common.util.IpUtil;
import org.restfulwhois.rdap.core.common.util.IpUtil.IpVersion;
import org.restfulwhois.rdap.core.common.util.IpV4;
import org.restfulwhois.rdap.core.common.util.IpV6;
import org.restfulwhois.rdap.core.common.util.NetworkInBytes;

import com.googlecode.ipv6.IPv6Address;

/**
 * Test for IpUtil.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class IpUtilTest {
    
    @Test
    public void test_validate_IPV4() {
        IpVersion ipVersion = IpUtil.getIpVersionOfIp("1.0.0.0");
        assertEquals(IpVersion.V4, ipVersion);
        ipVersion = IpUtil.getIpVersionOfIp("01.0.0.0");
        assertEquals(IpVersion.INVALID, ipVersion);
        ipVersion = IpUtil.getIpVersionOfIp("001.0.0.0");
        assertEquals(IpVersion.INVALID, ipVersion);
        ipVersion = IpUtil.getIpVersionOfIp("00.0.0.0");
        assertEquals(IpVersion.INVALID, ipVersion);
        ipVersion = IpUtil.getIpVersionOfIp("0.01.0.0");
        assertEquals(IpVersion.INVALID, ipVersion);
        ipVersion = IpUtil.getIpVersionOfIp("0.0.0.01");
        assertEquals(IpVersion.INVALID, ipVersion);
    }
    
    @Test
    public void test_toByteArray_IPV4() {
        String ip = "218.241.111.11";
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(ip);
        String hex =
                DatatypeConverter.printHexBinary(IpUtil.ipToByteArray(ip,
                        ipVersion));
//        System.err.println("0x" + hex);
    }

    @Test
    public void test_toByteArray_IPV6() {
        String ip = "::f:f:0.15.0.15";
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(ip);
        String hex =
                DatatypeConverter.printHexBinary(IpUtil.ipToByteArray(ip,
                        ipVersion));
//        System.err.println("0x" + hex);
    }

    @Test
    public void test_generate_ARPA() {
        // NetworkInBytes networkInBytes = IpUtil.parseArpa("f.f.ip6.arpa");
        NetworkInBytes networkInBytes =
                IpUtil.parseArpa("1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa");
        String hex1 =
                DatatypeConverter.printHexBinary(networkInBytes
                        .getStartAddress());
//        System.err.println("0x" + hex1);
        String hex2 =
                DatatypeConverter
                        .printHexBinary(networkInBytes.getEndAddress());
//        System.err.println("0x" + hex2);
    }

    @Test
    public void test_ARPA_V4() {
        NetworkInBytes networkInBytes = IpUtil.parseArpa("2.0.0.in-addr.arpa");
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
        NetworkInBytes networkInBytes = IpUtil.parseArpa(arpa);
        assertEquals(IpVersion.V6, networkInBytes.getIpVersion());
        assertEquals(expectedStartAddress,
                networkInBytes.getStartAddressAsString());
        assertArrayEquals("", IpV6.toByteArray(expectedStartAddress),
                networkInBytes.getStartAddress());
        assertEquals(expectedEndAddress, networkInBytes.getEndAddressAsString());
        assertArrayEquals("", IpV6.toByteArray(expectedEndAddress),
                networkInBytes.getEndAddress());
    }

    @Test
    public void testIpV6ToString() {
        String ipPrefix = "0:0:0:0:2001:6a8:0:1";
        // "2001:db8:85a3:0:2001:6a8:0:2"
        // "1:1:1:ffff:1:1:1:0"
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(ipPrefix);
        assertEquals(IpVersion.V6, ipVersion);
        byte[] bytes = IpUtil.ipToByteArray(ipPrefix, ipVersion);
        String ipString = IpUtil.toString(bytes, ipVersion);
        assertEquals("::2001:6a8:0:1", ipString);
    }

    @Test
    public void test_parse_IPV4() {
        // assertEquals("1.255.255.255", IpUtil.longToIpV4(33554431));
        // assertEquals("255.255.255.255", IpUtil.longToIpV4(num));
        String ipPrefix = "1.0.0.0";
        // ipPrefix = "1.255.255.255";
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(ipPrefix);
        assertEquals(IpVersion.V4, ipVersion);
        byte[] bytes = IpUtil.ipToByteArray(ipPrefix, ipVersion);
        String ipString = IpUtil.toString(bytes, ipVersion);
        // assertEquals("1.255.255.255", ipString);
        String hex1 = DatatypeConverter.printHexBinary(bytes);
//        System.err.println("0x" + hex1);
    }

    @Test
    public void test_parse_network_V4() {
        String network = "218.241.0.0/16";
        IpVersion ipVersion = IpUtil.getIpVersionOfNetwork(network);
        assertEquals(IpVersion.V4, ipVersion);
        NetworkInBytes networkInBytes = IpUtil.parseNetwork(network, ipVersion);
        String hex1 =
                DatatypeConverter.printHexBinary(networkInBytes
                        .getStartAddress());
//        System.err.println("0x" + hex1);
        String hex2 =
                DatatypeConverter
                        .printHexBinary(networkInBytes.getEndAddress());
        // System.err.println("0x" + hex2);
        assertEquals(IpVersion.V4, networkInBytes.getIpVersion());
    }

    public static void main(String[] args) {
        IPv6Address ip = IPv6Address.fromString("1:1:1:1:1:1:1.0.0.0");
        System.err.println(ip.isIPv4Mapped());
    }

    @Test
    public void test_parse_network_V6() {
        String network = "2014:2014:2014::/80";
        IpVersion ipVersion = IpUtil.getIpVersionOfNetwork(network);
        assertEquals(IpVersion.V6, ipVersion);
        NetworkInBytes networkInBytes = IpUtil.parseNetwork(network, ipVersion);
        String hex1 =
                DatatypeConverter.printHexBinary(networkInBytes
                        .getStartAddress());
        System.err.println("0x" + hex1);
        String hex2 =
                DatatypeConverter
                        .printHexBinary(networkInBytes.getEndAddress());
        System.err.println("0x" + hex2);
        assertEquals(IpVersion.V6, networkInBytes.getIpVersion());
    }

    @Test
    public void testgetIpVersionOfIpTrue() {
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("2001:db8:85a3:0:2001:6a8:0:2"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::1"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("fe80::1"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::192.168.1.1"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("0:0:0:0:0:0:0:0"));
        assertEquals(
                IpVersion.V6,
                IpUtil.getIpVersionOfIp("ffff:FFFF:fffF:Ffff:fFFF:FFff:ffFF:FFFf"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1080::8:800:2C:4A"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("FEC0:0:0:0:0:0:0:1"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("::FFFF:192.168.1.1"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("abcd:ef:111:f123::1"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("1138:0:0:0:8:80:800:417A"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("fecc:face::b00c:f001:fedc:fedd"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("CaFe:BaBe:dEAd:BeeF:12:345:6789:abcd"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::1:a:B:C:d:e:f"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1::a:B:C:d:e:f"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1:a::B:C:d:e:f"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1:a:B::C:d:e:f"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1:a:B:C::d:e:f"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1:a:B:C:d::e:f"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1:a:B:C:d:e::f"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("1:a:B:C:d:e:f::"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::2:f:E:D:6:7"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("2::f:E:D:6:7"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("2:f::E:D:6:7"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("2:f:E::D:6:7"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("2:f:E:D::6:7"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("2:f:E:D:6::7"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("2:f:E:D:6:7::"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::3:A:b:8:0"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("3::A:b:8:0"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("3:A::b:8:0"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("3:A:b::8:0"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("3:A:b:8::0"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("3:A:b:8:0::"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::4:5:C:d"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("4::5:C:d"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("4:5::C:d"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("4:5:C::d"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("4:5:C:d::"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::5:e:F"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("5::e:F"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("5:e::F"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("5:e:F::"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::6:A"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("6::A"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("6:A::"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::7"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("7::"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::7"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("1:a:B:C:d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("::a:B:C:d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("a::B:C:d:2:0.0.0.0"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("a:B::C:d:2:255.255.255.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("a:B:C::d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("a:B:C:d::2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("a:B:C:d:2::255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("::B:C:d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("B::C:d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("B:C::d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("B:C:d::2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("B:C:d:2::255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("::C:d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("C::d:2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("C:d::2:255.0.0.255"));
        assertEquals(IpVersion.V6,
                IpUtil.getIpVersionOfIp("C:d:2::255.0.0.255"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::d:2:255.0.0.255"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("d::2:255.0.0.255"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("d:2::255.0.0.255"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::2:255.0.0.255"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("2::255.0.0.255"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::255.0.0.255"));
    }

    @Test
    public void testgetIpVersionOfIpFalse() {
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("::256.0.0.255"));
        assertEquals(IpVersion.INVALID, IpUtil.getIpVersionOfIp("::256.0.0.2f"));
        assertEquals(IpVersion.INVALID, IpUtil.getIpVersionOfIp(":::255.0.0.2"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("f:E:d:c:2:1:0000:ffff:"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("f:E:d:c:2:1:0000:ffff::"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("::f:E:d:c:2:1:0000:ffff"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("f:E:d:c:2:1:0000:ggg"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("f:E:d:c:-2:1:0000:ffff:"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("f:E:d:c:2:1:0000:00ffff"));
        assertEquals(IpVersion.INVALID, IpUtil.getIpVersionOfIp("::00000"));
    }

    @Test
    public void testIsIpV4StrValid() {
        assertEquals(IpVersion.V4, IpUtil.getIpVersionOfIp("13.23.45.67"));
        assertEquals(IpVersion.V4, IpUtil.getIpVersionOfIp("0.0.0.0"));
        assertEquals(IpVersion.V4, IpUtil.getIpVersionOfIp("255.255.255.255"));
        assertEquals(IpVersion.INVALID, IpUtil.getIpVersionOfIp("*.2.255.255"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("1:255.255.255"));
    }

    @Test
    public void testgetIpVersionOfIp() {
        assertEquals(IpVersion.V4, IpUtil.getIpVersionOfIp("13.23.45.67"));
        assertEquals(IpVersion.V4, IpUtil.getIpVersionOfIp("0.0.0.0"));
        assertEquals(IpVersion.V4, IpUtil.getIpVersionOfIp("255.255.255.255"));
        assertEquals(IpVersion.INVALID,
                IpUtil.getIpVersionOfIp("1:255.255.255"));
        assertEquals(IpVersion.V6, IpUtil.getIpVersionOfIp("::"));
        assertEquals(IpVersion.INVALID, IpUtil.getIpVersionOfIp("*.2.255.255"));
    }

}