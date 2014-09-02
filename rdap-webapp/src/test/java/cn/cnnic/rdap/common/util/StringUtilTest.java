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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.http.MediaType;

import cn.cnnic.rdap.BaseTest;

/**
 * Test for StringUtil
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class StringUtilTest extends BaseTest {

    /**
     * test parseMediaTypes.
     */
    @Test
    public void testParseMediaTypes() {
        // test one type.
        List<MediaType> mediaTypes =
                StringUtil.parseMediaTypes("application/rdap+json");
        assertNotNull(mediaTypes);
        assertEquals(1, mediaTypes.size());
        assertNotNull(mediaTypes.get(0));
        assertEquals("application", mediaTypes.get(0).getType());
        assertEquals("rdap+json", mediaTypes.get(0).getSubtype());
        // test with charset.
        mediaTypes =
                StringUtil
                        .parseMediaTypes("application/rdap+json;charset=UTF-8");
        assertNotNull(mediaTypes);
        assertEquals(1, mediaTypes.size());
        assertNotNull(mediaTypes.get(0));
        assertEquals("application", mediaTypes.get(0).getType());
        assertEquals("rdap+json", mediaTypes.get(0).getSubtype());
        // test multi types.
        mediaTypes =
                StringUtil
                        .parseMediaTypes("application/json,application/rdap+json;charset=UTF-8");
        assertNotNull(mediaTypes);
        assertEquals(2, mediaTypes.size());
        assertNotNull(mediaTypes.get(0));
        assertEquals("application", mediaTypes.get(0).getType());
        assertEquals("json", mediaTypes.get(0).getSubtype());
        assertNotNull(mediaTypes.get(1));
        assertEquals("application", mediaTypes.get(1).getType());
        assertEquals("rdap+json", mediaTypes.get(1).getSubtype());
        // test with space
        mediaTypes =
                StringUtil
                        .parseMediaTypes(" application/json , application/rdap+json ; charset=UTF-8");
        assertNotNull(mediaTypes);
        assertEquals(2, mediaTypes.size());
        assertNotNull(mediaTypes.get(0));
        assertEquals("application", mediaTypes.get(0).getType());
        assertEquals("json", mediaTypes.get(0).getSubtype());
        assertNotNull(mediaTypes.get(1));
        assertEquals("application", mediaTypes.get(1).getType());
        assertEquals("rdap+json", mediaTypes.get(1).getSubtype());
        // upper case
        mediaTypes =
                StringUtil
                        .parseMediaTypes(" APPLICATION/JSON, APPLICATION/RDAP+JSON ; charset=UTF-8");
        assertNotNull(mediaTypes);
        assertEquals(2, mediaTypes.size());
        assertNotNull(mediaTypes.get(0));
        assertEquals("application", mediaTypes.get(0).getType());
        assertEquals("json", mediaTypes.get(0).getSubtype());
        assertNotNull(mediaTypes.get(1));
        assertEquals("application", mediaTypes.get(1).getType());
        assertEquals("rdap+json", mediaTypes.get(1).getSubtype());
        // invalid charset.
        mediaTypes =
                StringUtil
                        .parseMediaTypes("application/rdap+json;charset=UNKNOWN");
        assertNull(mediaTypes);
        // invalid string.
        mediaTypes =
                StringUtil
                        .parseMediaTypes(" someUnKnonwn!@#/!@#, APPLICATION/RDAP+JSON ; charset=UTF-8");
        assertNull(mediaTypes);
    }

    /**
     * test generateEncodedRedirectURL.
     */
    @Test
    public void testContainInvalidChar() {
        assertTrue(StringUtil.containNonAsciiPrintableChars("http://我们"));
        assertTrue(StringUtil.containNonAsciiPrintableChars("http://"));
        assertFalse(StringUtil
                .containNonAsciiPrintableChars("http://localhost:8080/.well-known/rdap%1a/ip/1.1.1.1/32"));
        assertFalse(StringUtil
                .containNonAsciiPrintableChars("https://localhost:8080/.well-known/rdap%1a/ip/1.1.1.1/32"));
    }

    /**
     * test generateEncodedRedirectURL.
     */
    @Test
    public void testGenerateEncodedRedirectURL() {
        String result = "http://rdap.cn/domain/%E4%B8%AD%E5%9B%BD.cn";
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "/domain/", "http://rdap.cn"));
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "domain", "http://rdap.cn"));
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "/domain", "http://rdap.cn"));
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "domain/", "http://rdap.cn"));
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "/domain/", "http://rdap.cn/"));
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "domain", "http://rdap.cn/"));
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "/domain", "http://rdap.cn/"));
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "domain/", "http://rdap.cn/"));
        // base uri with unicode
        result =
                "http://rdap.cn/%E4%B8%AD%E5%9B%BD/domain/%E4%B8%AD%E5%9B%BD.cn";
        assertEquals(result, StringUtil.generateEncodedRedirectURL("中国.cn",
                "/domain/", "http://rdap.cn/中国/"));
    }

    /**
     * test addQuotas.
     */
    @Test
    public void testParseTldsToListIfTldListIsNull() {
        assertThat(StringUtil.parseSeparatedStringToListIfListIsNull("cn", null),
                CoreMatchers.hasItems("cn"));
        assertThat(
                StringUtil.parseSeparatedStringToListIfListIsNull("edu.cn;cn", null),
                CoreMatchers.hasItems("cn", "edu.cn"));
        assertThat(
                StringUtil.parseSeparatedStringToListIfListIsNull("cn;edu.cn", null),
                CoreMatchers.hasItems("cn", "edu.cn"));
        assertThat(
                StringUtil.parseSeparatedStringToListIfListIsNull("cn;edu.cn;", null),
                CoreMatchers.hasItems("cn", "edu.cn"));
        assertThat(
                StringUtil.parseSeparatedStringToListIfListIsNull(";cn;edu.cn;", null),
                CoreMatchers.hasItems("cn", "edu.cn"));
        assertThat(
                StringUtil.parseSeparatedStringToListIfListIsNull(";cn;edu.cn", null),
                CoreMatchers.hasItems("cn", "edu.cn"));
        List<String> tldList = new ArrayList<String>();
        tldList.add("cn");
        tldList.add("edu.cn");
        assertThat(
                StringUtil.parseSeparatedStringToListIfListIsNull("com.cn", tldList),
                CoreMatchers.hasItems("cn", "edu.cn"));
    }

    /**
     * test addQuotas.
     */
    @Test
    public void testAddQuotas() {
        assertEquals("'cn'", StringUtil.addQuotas("cn"));
        assertEquals(null, StringUtil.addQuotas(""));
        assertEquals(null, StringUtil.addQuotas(null));
    }

    /**
     * test containsMoreThanOnce.
     * 
     */
    @Test
    public void testContainsMoreThanOnce() throws UnsupportedEncodingException {
        assertTrue(StringUtil.containsAtMostOnce("bobabc", "o"));
        assertTrue(StringUtil.containsAtMostOnce("bobabc", "a"));
        assertTrue(StringUtil.containsAtMostOnce("tobabc", "t"));
        assertTrue(StringUtil.containsAtMostOnce("bob*", "*"));
        assertTrue(StringUtil.containsAtMostOnce("*bob", "*"));
        assertTrue(StringUtil.containsAtMostOnce("b*ob", "*"));
        assertTrue(StringUtil.containsAtMostOnce("*", "*"));
        assertTrue(StringUtil.containsAtMostOnce(" * ", "*"));
        assertFalse(StringUtil.containsAtMostOnce("*b*ob", "*"));
        assertFalse(StringUtil.containsAtMostOnce("b**ob", "*"));
        assertFalse(StringUtil.containsAtMostOnce("**", "*"));
        assertFalse(StringUtil.containsAtMostOnce("**abc", "*"));

    }

    /**
     * test checkIsValidSearchPattern.
     * 
     */
    @Test
    public void testCheckIsValidSearchPattern()
            throws UnsupportedEncodingException {
        assertTrue(StringUtil.checkIsValidSearchPattern("bob"));
        assertTrue(StringUtil.checkIsValidSearchPattern(" bob "));
        assertFalse(StringUtil.checkIsValidSearchPattern("*"));
        assertFalse(StringUtil.checkIsValidSearchPattern("*abc"));
        assertFalse(StringUtil.checkIsValidSearchPattern("*abc*"));
        assertFalse(StringUtil.checkIsValidSearchPattern("a*b*"));
        assertFalse(StringUtil.checkIsValidSearchPattern("a*b*c"));
        assertFalse(StringUtil.checkIsValidSearchPattern("a*b*c*"));
        assertFalse(StringUtil.checkIsValidSearchPattern("**"));
        assertFalse(StringUtil.checkIsValidSearchPattern("a**"));
        assertFalse(StringUtil.checkIsValidSearchPattern("**c"));
    }

    /**
     * test getNormalization.
     * 
     */
    @Test
    public void testGetNormalization() throws UnsupportedEncodingException {
        assertTrue(!"a".equals("ａ"));
        assertEquals(StringUtil.getNormalization("a"),
                StringUtil.getNormalization("ａ"));
    }

    /**
     * test encoded URL.
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testEncodedURL() throws UnsupportedEncodingException {
        String qEncode = URLEncoder.encode("中文。中国", "UTF-8");
        String expectURL =
                "http://cwhois.cnnic.cn/whois?inputfield=value&entity=domain&value="
                        + qEncode;
        String encodedURL = expectURL;
        String result = StringUtil.urlEncode(encodedURL);
        assertEquals(expectURL, result);
    }

    /**
     * test decoded URL.
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testDecodedURL() throws UnsupportedEncodingException {
        String qEncode = URLEncoder.encode("中文。中国", "UTF-8");
        String expectURL =
                "http://cwhois.cnnic.cn/whois?inputfield=value&entity=domain&value="
                        + qEncode;
        String decodedURL =
                "http://cwhois.cnnic.cn/whois?inputfield=value&entity=domain&value=中文。中国";
        String result = StringUtil.urlEncode(decodedURL);
        assertEquals(expectURL, result);
    }

    /**
     * test decoded URL.
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testDecodedURLForIDN() throws UnsupportedEncodingException {
        String decodedURL = "http://网络中心.中国/链接1\\a?a=b#c=d";
        String encodeURL = URLEncoder.encode(decodedURL, "UTF-8");
        String expectURL =
                "http://%E7%BD%91%E7%BB%9C%E4%B8%AD%E5%BF%83.%E4%B8%AD%E5%9B%BD/%E9%93%BE%E6%8E%A51%5Ca?a=b#c=d";
        String result = StringUtil.urlEncode(decodedURL);
        assertEquals(expectURL, result);
        result = StringUtil.urlEncode(encodeURL);
        assertEquals(expectURL, result);
    }

    /**
     * test parseUnsignedLong.
     */
    @Test
    public void testParseUnsignedLong() {
        assertEquals(123L, StringUtil.parseUnsignedLong("123"));
        assertEquals(1844674407370955161L,
                StringUtil.parseUnsignedLong("1844674407370955161"));
        assertEquals(-1L, StringUtil.parseUnsignedLong("18446744073709551615"));
        assertEquals(-4L, StringUtil.parseUnsignedLong("18446744073709551612"));
    }
}