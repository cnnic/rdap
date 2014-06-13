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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test for StringUtil.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class DomainUtilTest {

    private static final List<String> validArpaDomain = new ArrayList<String>();
    private static final List<String> inValidArpaDomain = new ArrayList<String>();

    @BeforeClass
    public static void initValidArpaDomain() {
        validArpaDomain
                .add("b.a.9.8.7.6.5.0.4.0.0.0.3.0.0.0.2.0.0.0.1.0.0.0.0.0.0.0.1.2.3.4.ip6.arp");
        validArpaDomain
                .add("b.a.9.8.7.6.5.0.4.0.0.0.3.0.0.0.2.0.0.0.1.0.0.0.0.0.0.0.1.2.3.4.ip6.arpa.");
        validArpaDomain
                .add("B.A.9.8.7.6.5.0.4.0.0.0.3.0.0.0.2.0.0.0.1.0.0.0.0.0.0.0.1.2.3.4.ip6.arpa.");
        validArpaDomain
                .add("0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa.");
        validArpaDomain
                .add("b.a.9.8.7.6.5.0.4.0.0.0.3.0.0.0.2.0.0.0.1.0.0.0.0.0.0.0.1.2.3.4.IP6.ARPA.");
        validArpaDomain.add("0.in-addr.arpa");
        validArpaDomain.add("0.IN-ADDR.ARPA");
        validArpaDomain.add("0.in-addr.arpa.");
        validArpaDomain.add("0.1.in-addr.arpa");
        validArpaDomain.add("1.0.1.in-addr.arpa");
        validArpaDomain.add("1.1.0.1.in-addr.arpa");
        validArpaDomain.add("0.1.0.1.in-addr.arpa");
        validArpaDomain.add("0.0.0.1.in-addr.arpa");
        validArpaDomain.add("255.0.0.1.in-addr.arpa");
        validArpaDomain.add("255.255.255.255.in-addr.arpa");
    }

    @BeforeClass
    public static void initInValidArpaDomain() {
        inValidArpaDomain.add(".in-addr.arpa");
        inValidArpaDomain.add("256.255.255.255.in-addr.arpa");
        inValidArpaDomain.add("in-addr.arpa");
        inValidArpaDomain.add("-addr.arpa");
        inValidArpaDomain.add(".arpa");
        inValidArpaDomain.add("cnnic.arpa");
        inValidArpaDomain.add("cnnic.cn.arpa");
        inValidArpaDomain.add("");
        inValidArpaDomain.add("256.255.255.255.in-addr.arpa");
        inValidArpaDomain.add("1.ip6.arpa");
        inValidArpaDomain.add("0.ip6.arpa");
        inValidArpaDomain.add(".ip6.arpa");
    }

    /**
     * test isArpaTldAndLabelIsValid.
     * 
     */
    @Test
    public void testIsArpaTldAndLabelIsValid() {
        for (String domain : validArpaDomain) {
            assertTrue(DomainUtil.isArpaTldAndLabelIsValid(domain));
        }
        for (String domain : inValidArpaDomain) {
            assertFalse(DomainUtil.isArpaTldAndLabelIsValid(domain));
        }
        // not validate these domain, return true.
        assertTrue(DomainUtil.isArpaTldAndLabelIsValid("arpa."));
        assertTrue(DomainUtil.isArpaTldAndLabelIsValid("arpa"));
        assertTrue(DomainUtil.isArpaTldAndLabelIsValid("cnnic.cn"));
        assertTrue(DomainUtil.isArpaTldAndLabelIsValid(" cnnic.cn"));
        assertTrue(DomainUtil.isArpaTldAndLabelIsValid("."));
        assertTrue(DomainUtil.isArpaTldAndLabelIsValid("cnnic.cn"));
    }

    /**
     * test getLowerCaseByLabel.
     * 
     */
    @Test
    public void testGetLowerCaseByLabel() {
        assertEquals("cnnic.cn", DomainUtil.getLowerCaseByLabel("cnnic.cn"));
        assertEquals("cnnic.cn", DomainUtil.getLowerCaseByLabel("CNNIC.cn"));
        assertEquals("cnnic", DomainUtil.getLowerCaseByLabel("CNNIC"));
        assertEquals("cnnic.cn", DomainUtil.getLowerCaseByLabel("CNNIC.CN"));
        assertEquals("cnnic.cn", DomainUtil.getLowerCaseByLabel("cNNIC.CN"));
        assertEquals("cnnic.cn", DomainUtil.getLowerCaseByLabel("cNNIC.cN"));
        assertEquals("清华TSing.cn", DomainUtil.getLowerCaseByLabel("清华TSing.CN"));
    }

    /**
     * test deleteLastPoint.
     * 
     */
    @Test
    public void testDeleteLastPoint() {
        assertEquals("cnnic.cn", DomainUtil.deleteLastPoint("cnnic.cn"));
        assertEquals("cnnic.cn", DomainUtil.deleteLastPoint("cnnic.cn."));
        assertEquals("cnnic.cn.", DomainUtil.deleteLastPoint("cnnic.cn.."));
        assertEquals("", DomainUtil.deleteLastPoint(""));
        assertEquals("cnnic", DomainUtil.deleteLastPoint("cnnic"));
        assertEquals("cnnic", DomainUtil.deleteLastPoint("cnnic."));
        assertEquals("cnnic.", DomainUtil.deleteLastPoint("cnnic.."));
        assertEquals(".", DomainUtil.deleteLastPoint("."));
        assertEquals(".", DomainUtil.deleteLastPoint(".."));
    }

    /**
     * test validateDomainNameIsValidIdna.
     * 
     */
    @Test
    public void testValidateDomainNameIsValidIdna() {
        // arpa domain.
        for (String domain : validArpaDomain) {
            assertTrue(validateDomainNameIsValidIdna(domain));
        }
        for (String domain : inValidArpaDomain) {
            assertFalse(validateDomainNameIsValidIdna(domain));
        }
        // other domain.
        assertTrue(validateDomainNameIsValidIdna("cnnic.cn"));
        assertTrue(validateDomainNameIsValidIdna("cnnic.cn."));
        assertTrue(validateDomainNameIsValidIdna("cnnic.com.cn"));
        assertTrue(validateDomainNameIsValidIdna("1cnnic.cn"));
        assertTrue(validateDomainNameIsValidIdna(" cnnic.cn"));
        assertFalse(validateDomainNameIsValidIdna("我cnnic-.cn"));
        assertFalse(validateDomainNameIsValidIdna("-我cnnic.cn"));
        assertFalse(validateDomainNameIsValidIdna("cnnic-.cn"));
        assertFalse(validateDomainNameIsValidIdna("-cnnic.cn"));
        assertFalse(validateDomainNameIsValidIdna("boss.-cnnic.cn"));
        assertFalse(validateDomainNameIsValidIdna("boss.cnnic-.cn"));
        assertFalse(validateDomainNameIsValidIdna("boss.cnnic.-cn"));
        assertFalse(validateDomainNameIsValidIdna("boss.cnnic.cn-"));
        assertFalse(validateDomainNameIsValidIdna("cnnic."));
        assertFalse(validateDomainNameIsValidIdna("123"));
        assertFalse(validateDomainNameIsValidIdna("c nnic.cn"));
        assertFalse(validateDomainNameIsValidIdna("cnnic"));
        assertFalse(validateDomainNameIsValidIdna(""));
        assertFalse(validateDomainNameIsValidIdna(" "));
        assertFalse(validateDomainNameIsValidIdna(null));
        assertFalse(validateDomainNameIsValidIdna("."));
        assertFalse(validateDomainNameIsValidIdna("a."));
        assertFalse(validateDomainNameIsValidIdna(".a"));
        assertFalse(validateDomainNameIsValidIdna("Σ.cn"));
        assertFalse(validateDomainNameIsValidIdna("@.cn"));
        assertFalse(validateDomainNameIsValidIdna("a@.cn"));
        assertFalse(validateDomainNameIsValidIdna("@a.cn"));
        assertFalse(validateDomainNameIsValidIdna("xn--55qx5d.中国"));
        assertFalse(DomainUtil
                .validateDomainNameIsValidIdna("xn--55qx5d.中国.cn"));

        assertTrue(validateDomainNameIsValidIdna("中国.CN"));
        assertTrue(validateDomainNameIsValidIdna("xn--55QX5D.CN"));
        assertTrue(validateDomainNameIsValidIdna("xn--55qx5d.cn"));
        // 254,127 lable
        assertFalse(validateDomainNameIsValidIdna("a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a."));
        // 254,126 lable
        assertTrue(validateDomainNameIsValidIdna("a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.a.aaa."));
        assertTrue(validateDomainNameIsValidIdna("xn--notBase64Code.cn"));
        assertTrue(validateDomainNameIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CF%83%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("σειράτάξησυπουργείωνσύνθεσηυπουργικούσυμβουλίουουουο.bnnhg"));
        assertFalse(validateDomainNameIsValidIdna("xn--caf%C3%A9s.com"));
        assertFalse(validateDomainNameIsValidIdna("xn--cafés.com"));
        assertFalse(validateDomainNameIsValidIdna("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CF%83%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CE%BF.bnnhg"));
        assertFalse(validateDomainNameIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaaahjsgk5chhdiq0cclcgddbb8o9hoa.bnnhg"));
        assertFalse(validateDomainNameIsValidIdna("σειράτάξησυπουργείωνσύνθεσηυπουργικούσυμβουλίουουουοο.bnnhg"));
        assertFalse(validateDomainNameIsValidIdna("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CE%A3%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF.bnnhg"));
        assertFalse(validateDomainNameIsValidIdna("σειράτάξησυπουργείωνΣύνθεσηυπουργικούσυμβουλίουουουο.bnnhg"));
    }

    /**
     * validate domain.
     * 
     * @param domainName
     *            domain name.
     * @return boolean.
     */
    private boolean validateDomainNameIsValidIdna(String domainName) {
        String decodeDomain = DomainUtil
                .decodeAndTrimAndReplaceAsciiToLowercase(domainName);
        return DomainUtil.validateDomainNameIsValidIdna(decodeDomain);
    }

    /**
     * test decodeAndTrim.
     * 
     */
    @Test
    public void testDecodeAndTrim() {
        assertEquals(
                "中文.中国",
                DomainUtil
                        .decodeAndTrimAndReplaceAsciiToLowercase("%E4%B8%AD%E6%96%87.%E4%B8%AD%E5%9B%BD"));
        assertEquals("中文.cn",
                DomainUtil.decodeAndTrimAndReplaceAsciiToLowercase("中文.CN"));
        assertEquals("中文.cn",
                DomainUtil.decodeAndTrimAndReplaceAsciiToLowercase("中文.Cn"));
        assertEquals("中c文n.cn",
                DomainUtil.decodeAndTrimAndReplaceAsciiToLowercase("中C文n.Cn"));
    }

    /**
     * test decodeAndTrim.
     * 
     */
    @Test
    public void testDecodeAndTrimDecodedStr() {
        String result = DomainUtil
                .decodeAndTrimAndReplaceAsciiToLowercase("中文.中国");
        assertEquals("中文.中国", result);
    }

    /**
     * test generate puny name for unicode domain.
     * 
     */
    @Test
    public void testGeneDomainPunyNameUnicode() {
        String result = DomainUtil.geneDomainPunyName("中文.中国");
        assertEquals("xn--fiq228c.xn--fiqs8s", result);
    }

    /**
     * test generate puny name for puny domain.
     * 
     */
    @Test
    public void testGeneDomainPunyNamePunycode() {
        String result = DomainUtil.geneDomainPunyName("xn--fiq228c.xn--fiqs8s");
        assertEquals("xn--fiq228c.xn--fiqs8s", result);
    }
}
