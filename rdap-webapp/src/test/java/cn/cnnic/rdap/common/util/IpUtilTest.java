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

import java.math.BigDecimal;

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
    	assertEquals("0:0:0:1:0:0:0:1", IpUtil.longToIpV6(1, 1));
    	assertEquals("0:0:0:ffff:0:0:0:0", IpUtil.longToIpV6(0xFFFF, 0));
    	assertEquals("1:1:1:ffff:1:1:1:0", IpUtil.longToIpV6(0x000100010001FFFFL, 0x0001000100010000L));
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

    @Test
    public void testIsIpV4StrValid() {
    	assertEquals(true, IpUtil.isIpV4StrValid("13.23.45.67"));
    	assertEquals(true, IpUtil.isIpV4StrValid("0.0.0.0"));
    	assertEquals(true, IpUtil.isIpV4StrValid("255.255.255.255"));
    	assertEquals(true, IpUtil.isIpV4StrValid("*.2.255.255"));
    	assertEquals(false, IpUtil.isIpV4StrValid("1:255.255.255"));
    	assertEquals(false, IpUtil.isIpV4StrValid("::"));
    }
    
    @Test
    public void testIsIpV4StrWholeValid() {
    	assertEquals(true, IpUtil.isIpV4StrWholeValid("13.23.45.67"));
    	assertEquals(true, IpUtil.isIpV4StrWholeValid("0.0.0.0"));
    	assertEquals(true, IpUtil.isIpV4StrWholeValid("255.255.255.255"));
    	assertEquals(false, IpUtil.isIpV4StrWholeValid("1:255.255.255"));
    	assertEquals(false, IpUtil.isIpV4StrWholeValid("::"));
    	assertEquals(false, IpUtil.isIpV4StrWholeValid("*.2.255.255"));
    }
    
    @Test
    public void testCountOccurrences() {
    	assertEquals(3, IpUtil.countOccurrences("13.23.45.67", '.'));
    	assertEquals(3, IpUtil.countOccurrences("13:23:45:67", ':'));
    	assertEquals(4, IpUtil.countOccurrences("13::23:45:67", ':'));
    }
    
    @Test
    public void testGenerateZeroes() {
    	assertEquals("", IpUtil.generateZeroes(0));
    	assertEquals("0:", IpUtil.generateZeroes(1));
    	assertEquals("0:0:", IpUtil.generateZeroes(2));
    	assertEquals("0:0:0:0:0:0:0:", IpUtil.generateZeroes(7));
    }
    
    @Test
    public void testExpandShortNotation() {
    	assertEquals("0:0:0:0:0:0:0:0:", IpUtil.expandShortNotation("::"));
    	assertEquals("0:0:0:0:0:0:0:1", IpUtil.expandShortNotation("0::1"));
    	assertEquals("1:0:0:0:0:2:3.4.5.6", IpUtil.expandShortNotation("1::2:3.4.5.6"));
    }
    
	@Test
    public void testIpv6ToBigDecimal() {
    	BigDecimal v6[] = new BigDecimal[2];
    	v6[0]= BigDecimal.valueOf(0);
    	v6[1]= BigDecimal.valueOf(0);
    	BigDecimal v6Test[] = IpUtil.ipV6ToBigDecimal("::");
    	assertEquals(v6[0].longValue(), v6Test[0].longValue());
    	assertEquals(v6[1].longValue(), v6Test[1].longValue());
    	v6[0] = BigDecimal.valueOf(0);
    	v6[1] = BigDecimal.valueOf(1);
    	v6Test = IpUtil.ipV6ToBigDecimal("0::1");
    	assertEquals(v6[0].longValue(), v6Test[0].longValue());
    	assertEquals(v6[1].longValue(), v6Test[1].longValue());
    	v6[0] = BigDecimal.valueOf(0x0001000000000000L);
    	v6[1] = BigDecimal.valueOf(0x0000000200000000L+(3*256+4)*65536+5*256+6);
    	v6Test = IpUtil.ipV6ToBigDecimal("1::2:3.4.5.6");
    	assertEquals(v6[0].byteValue(), v6Test[0].byteValue());
    	assertEquals(v6[1].byteValue(), v6Test[1].byteValue());
    }
	
	@Test
	public void testIpV6ToLong() {
		long v6[] = IpUtil.ipV6ToLong("::");
		assertEquals(0, v6[0]);
		assertEquals(0, v6[1]);
		v6 = IpUtil.ipV6ToLong("1::1");
		assertEquals(0x0001000000000000L, v6[0]);
		assertEquals(0x0000000000000001L, v6[1]);
		v6 = IpUtil.ipV6ToLong("1::2:3:4");
		assertEquals(0x0001000000000000L, v6[0]);
		assertEquals(0x0000000200030004L, v6[1]);
	}
	
	@Test
	public void testIpv4ToDecimal() {
		BigDecimal v4[] = new BigDecimal[2];
		v4[0] = BigDecimal.valueOf(0);
		v4[1] = BigDecimal.valueOf(0);
		BigDecimal v4Test[] = IpUtil.ipV4ToDecimal("0.0.0.0");
		assertEquals(v4Test[0], v4[0]);
		assertEquals(v4Test[1], v4[1]);
		v4[0] = BigDecimal.valueOf(0);
		v4[1] = BigDecimal.valueOf(256*256*256+2*256*256+3*256+4);
		v4Test = IpUtil.ipV4ToDecimal("1.2.3.4");
		assertEquals(v4Test[0], v4[0]);
		assertEquals(v4Test[1], v4[1]);
	}
	
	@Test
	public void testIpToBigDecimal() {
		BigDecimal ipAddr[] = new BigDecimal[2];
		long longIp = 0xffffffffL;
		ipAddr[0] = BigDecimal.valueOf(0);
		ipAddr[1] = BigDecimal.valueOf(longIp);
		BigDecimal ipTest[] = IpUtil.ipToBigDecimal("255.255.255.255");
		assertEquals(ipAddr[0], ipTest[0]);
		assertEquals(ipAddr[1], ipTest[1]);
		longIp = 0x7fffffffffffffffL;
		BigDecimal bigDec = BigDecimal.valueOf(longIp);
		ipAddr[0] = bigDec.add(bigDec).add(BigDecimal.valueOf(1));
		ipAddr[1] = ipAddr[0];
		ipTest = IpUtil.ipToBigDecimal("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
		assertEquals(ipAddr[0].byteValue(), ipTest[0].byteValue());
		assertEquals(ipAddr[1].byteValue(), ipTest[1].byteValue());
	}
	
	@Test
	public void testIpToLong() {
		long ipTest[] = IpUtil.ipToLong("127.0.0.1");
		long ip[] = new long[2];
		ip[0] = 0;
		ip[1] = 127*256*256*256+1;
		assertEquals(ipTest[0],ip[0]);
		assertEquals(ipTest[1],ip[1]);
		ipTest = IpUtil.ipToLong("ff:ff:ff::ff");
		ip[0] = 0x00ff00ff00ff0000L;
		ip[1] = 0x00000000000000ffL;
		assertEquals(ipTest[0],ip[0]);
		assertEquals(ipTest[1],ip[1]);
	}
	
	@Test
	public void testIsIpLongValid() {
		assertEquals(true,IpUtil.isIpLongValid("12344", true));
		assertEquals(false,IpUtil.isIpLongValid("4294967296", true));
		assertEquals(true,IpUtil.isIpLongValid("4294967296", false));
		assertEquals(false,IpUtil.isIpLongValid("18446744073709551616", false));
	}
}