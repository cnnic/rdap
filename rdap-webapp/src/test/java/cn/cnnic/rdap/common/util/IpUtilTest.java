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
package cn.cnnic.rdap.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test for IpUtil.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class IpUtilTest {
    /**
     * test longToIpV6.
     * 
     */
    @Test
    public void testIpV6ToString() {
        assertEquals("0:0:0:0:2001:6a8:0:1",
                IpUtil.longToIpV6(0, 2306131802814676993L));
        assertEquals("2001:db8:85a3:0:2001:6a8:0:2",
                IpUtil.longToIpV6(2306139570357600256L, 2306131802814676994L));
    }

    /**
     * test longToIpV4.
     * 
     */
    @Test
    public void testLongToIpV4() {
        assertEquals("1.0.0.0", IpUtil.longToIpV4(16777216));
        assertEquals("1.255.255.255", IpUtil.longToIpV4(33554431));
        long num = (0x100000000L);
        assertEquals("", IpUtil.longToIpV4(num));
        num = (0xffffffffL);
        assertEquals("255.255.255.255", IpUtil.longToIpV4(num));
    }

    @Test
    public void testIsIpV6StrValidTrue() {
        assertEquals(true, IpUtil.isIpV6StrValid("::"));
        assertEquals(true,
                IpUtil.isIpV6StrValid("2001:db8:85a3:0:2001:6a8:0:2"));
        assertEquals(true, IpUtil.isIpV6StrValid("::1"));
        assertEquals(true, IpUtil.isIpV6StrValid("fe80::1"));
        assertEquals(true, IpUtil.isIpV6StrValid("::192.168.1.1"));
        assertEquals(true, IpUtil.isIpV6StrValid("0:0:0:0:0:0:0:0"));
        assertEquals(
                true,
                IpUtil.isIpV6StrValid("ffff:FFFF:fffF:Ffff:fFFF:FFff:ffFF:FFFf"));
        assertEquals(true, IpUtil.isIpV6StrValid("1080::8:800:2C:4A"));
        assertEquals(true, IpUtil.isIpV6StrValid("FEC0:0:0:0:0:0:0:1"));
        assertEquals(true, IpUtil.isIpV6StrValid("::FFFF:192.168.1.1"));
        assertEquals(true, IpUtil.isIpV6StrValid("abcd:ef:111:f123::1"));
        assertEquals(true, IpUtil.isIpV6StrValid("1138:0:0:0:8:80:800:417A"));
        assertEquals(true,
                IpUtil.isIpV6StrValid("fecc:face::b00c:f001:fedc:fedd"));
        assertEquals(true,
                IpUtil.isIpV6StrValid("CaFe:BaBe:dEAd:BeeF:12:345:6789:abcd"));
        assertEquals(true, IpUtil.isIpV6StrValid("::1:a:B:C:d:e:f"));
        assertEquals(true, IpUtil.isIpV6StrValid("1::a:B:C:d:e:f"));
        assertEquals(true, IpUtil.isIpV6StrValid("1:a::B:C:d:e:f"));
        assertEquals(true, IpUtil.isIpV6StrValid("1:a:B::C:d:e:f"));
        assertEquals(true, IpUtil.isIpV6StrValid("1:a:B:C::d:e:f"));
        assertEquals(true, IpUtil.isIpV6StrValid("1:a:B:C:d::e:f"));
        assertEquals(true, IpUtil.isIpV6StrValid("1:a:B:C:d:e::f"));
        assertEquals(true, IpUtil.isIpV6StrValid("1:a:B:C:d:e:f::"));
        assertEquals(true, IpUtil.isIpV6StrValid("::2:f:E:D:6:7"));
        assertEquals(true, IpUtil.isIpV6StrValid("2::f:E:D:6:7"));
        assertEquals(true, IpUtil.isIpV6StrValid("2:f::E:D:6:7"));
        assertEquals(true, IpUtil.isIpV6StrValid("2:f:E::D:6:7"));
        assertEquals(true, IpUtil.isIpV6StrValid("2:f:E:D::6:7"));
        assertEquals(true, IpUtil.isIpV6StrValid("2:f:E:D:6::7"));
        assertEquals(true, IpUtil.isIpV6StrValid("2:f:E:D:6:7::"));
        assertEquals(true, IpUtil.isIpV6StrValid("::3:A:b:8:0"));
        assertEquals(true, IpUtil.isIpV6StrValid("3::A:b:8:0"));
        assertEquals(true, IpUtil.isIpV6StrValid("3:A::b:8:0"));
        assertEquals(true, IpUtil.isIpV6StrValid("3:A:b::8:0"));
        assertEquals(true, IpUtil.isIpV6StrValid("3:A:b:8::0"));
        assertEquals(true, IpUtil.isIpV6StrValid("3:A:b:8:0::"));
        assertEquals(true, IpUtil.isIpV6StrValid("::4:5:C:d"));
        assertEquals(true, IpUtil.isIpV6StrValid("4::5:C:d"));
        assertEquals(true, IpUtil.isIpV6StrValid("4:5::C:d"));
        assertEquals(true, IpUtil.isIpV6StrValid("4:5:C::d"));
        assertEquals(true, IpUtil.isIpV6StrValid("4:5:C:d::"));
        assertEquals(true, IpUtil.isIpV6StrValid("::5:e:F"));
        assertEquals(true, IpUtil.isIpV6StrValid("5::e:F"));
        assertEquals(true, IpUtil.isIpV6StrValid("5:e::F"));
        assertEquals(true, IpUtil.isIpV6StrValid("5:e:F::"));
        assertEquals(true, IpUtil.isIpV6StrValid("::6:A"));
        assertEquals(true, IpUtil.isIpV6StrValid("6::A"));
        assertEquals(true, IpUtil.isIpV6StrValid("6:A::"));
        assertEquals(true, IpUtil.isIpV6StrValid("::7"));
        assertEquals(true, IpUtil.isIpV6StrValid("7::"));
        assertEquals(true, IpUtil.isIpV6StrValid("::7"));
        assertEquals(true, IpUtil.isIpV6StrValid("1:a:B:C:d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("::a:B:C:d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("a::B:C:d:2:0.0.0.0"));
        assertEquals(true, IpUtil.isIpV6StrValid("a:B::C:d:2:255.255.255.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("a:B:C::d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("a:B:C:d::2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("a:B:C:d:2::255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("::B:C:d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("B::C:d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("B:C::d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("B:C:d::2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("B:C:d:2::255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("::C:d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("C::d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("C:d::2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("C:d:2::255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("::d:2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("d::2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("d:2::255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("::2:255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("2::255.0.0.255"));
        assertEquals(true, IpUtil.isIpV6StrValid("::255.0.0.255"));
    }

    @Test
    public void testIsIpV6StrValidFalse() {
        assertEquals(false, IpUtil.isIpV6StrValid("::256.0.0.255"));
        assertEquals(false, IpUtil.isIpV6StrValid("::256.0.0.2f"));
        assertEquals(false, IpUtil.isIpV6StrValid(":::255.0.0.2"));
        assertEquals(false, IpUtil.isIpV6StrValid("f:E:d:c:2:1:0000:ffff:"));
        assertEquals(false, IpUtil.isIpV6StrValid("f:E:d:c:2:1:0000:ggg"));
        assertEquals(false, IpUtil.isIpV6StrValid("f:E:d:c:-2:1:0000:ffff:"));
        assertEquals(false, IpUtil.isIpV6StrValid("::00000"));
    }
}