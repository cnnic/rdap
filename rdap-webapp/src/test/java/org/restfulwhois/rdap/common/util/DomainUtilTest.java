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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.util.DomainUtil;

/**
 * Test for StringUtil.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class DomainUtilTest {

    private static final List<String> validArpaDomain = new ArrayList<String>();
    private static final List<String> inValidArpaDomain =
            new ArrayList<String>();

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

        validArpaDomain.add("1.1.1.d.a.c.a.0.ip6.arpa");
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
        inValidArpaDomain.add("z.ip6.arpa");
        inValidArpaDomain.add("cn.ip6.arpa");
        inValidArpaDomain.add(".ip6.arpa");
    }
    
    

    /**
     * testUrlDecodeAndReplaceAsciiToLowercase.
     * @throws DecodeException 
     * 
     */
    @Test
    public void testUrlDecodeAndReplaceAsciiToLowercase() throws DecodeException {
        assertEquals("cnnic.cn", DomainUtil.urlDecodeAndReplaceAsciiToLowercase("Cnnic.cn"));
        assertEquals("cnnic.cn", DomainUtil.replaceAsciiToLowercase("CNNIC.cn"));
        assertEquals("cnnic.cn", DomainUtil.replaceAsciiToLowercase("CNNIC.CN"));
        assertEquals("中国.cn", DomainUtil.replaceAsciiToLowercase("中国.CN"));
    }
        
    /**
     * testReplaceAsciiToLowercase.
     * 
     */
    @Test
    public void testReplaceAsciiToLowercase() {
        assertEquals("cnnic.cn", DomainUtil.replaceAsciiToLowercase("Cnnic.cn"));
        assertEquals("cnnic.cn", DomainUtil.replaceAsciiToLowercase("CNNIC.cn"));
        assertEquals("cnnic.cn", DomainUtil.replaceAsciiToLowercase("CNNIC.CN"));
        assertEquals("中国.cn", DomainUtil.replaceAsciiToLowercase("中国.CN"));
    }
    
        
        /**
         * test isArpaTldAndLabelIsValid.
         * 
         */
        @Test
        public void testIsArpaTldAndLabelIsValid() {

        assertTrue(DomainUtil
                .isArpaTldAndLabelIsValid("1.1.1.d.a.c.a.0.ip6.arpa"));

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
        assertTrue(validateDomainNameIsValidIdna("ａ.cn"));
        assertTrue(validateDomainNameIsValidIdna("cnnic.cn"));
        assertTrue(validateDomainNameIsValidIdna("cnnic.cn."));
        assertTrue(validateDomainNameIsValidIdna("cnnic.com.cn"));
        assertTrue(validateDomainNameIsValidIdna("1cnnic.cn"));
        assertFalse(validateDomainNameIsValidIdna(" cnnic.cn"));
        assertFalse(validateDomainNameIsValidIdna(" cnnic.cn "));
        assertFalse(validateDomainNameIsValidIdna("cnnic.cn "));
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
                .validateDomainNameIsValidIdna("xn--55qx5d.中国.cn",false));

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

        assertFalse(validateDomainNameIsValidIdna("xn--.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("xn--1.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("σειράτάξησυπουργείωνσύνθεσηυπουργικούσυμβουλίουουουο.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg"));

        assertFalse(validateDomainNameIsValidIdna("xnxnhopefullynonexisting<*"));
        assertTrue(validateDomainNameIsValidIdna("xn--4xA.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("xn--7wA.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("σ.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("ς.bnnhg"));
        assertTrue(validateDomainNameIsValidIdna("ß.bnnhg"));
        assertFalse(validateDomainNameIsValidIdna("Σ.bnnhg"));
        assertFalse(validateDomainNameIsValidIdna("√.com"));
        assertFalse(validateDomainNameIsValidIdna("ÖBB.at"));
        assertFalse(validateDomainNameIsValidIdna("Ⱥbby.com"));

        assertTrue(validateDomainNameIsValidIdna("\u0205.com"));

        assertFalse(validateDomainNameIsValidIdna("cnnic%25.cn"));

        assertFalse(validateDomainNameIsValidIdna("123〳.中国"));
        assertFalse(validateDomainNameIsValidIdna("123Ā.中国"));
        assertFalse(validateDomainNameIsValidIdna("123Ƽ.中国"));
        assertFalse(validateDomainNameIsValidIdna("123஝஝஝஝.中国")); // \u0b9d஝
        assertFalse(validateDomainNameIsValidIdna("123઱஝஝.中国")); // \u0ab1
        assertFalse(validateDomainNameIsValidIdna("123�஝஝.中国")); // \ufff8

        assertFalse(validateDomainNameIsValidIdna("cnnic。.cn")); // \u3002
        assertFalse(validateDomainNameIsValidIdna("测试。．中国.cn")); // \u3002

    }

    @Test
    public void testValidateDomainNameIsValidIdnaException() {
        assertFalse(validateDomainNameIsValidIdna("cnnic%.cn"));
        // assertFalse(validateDomainNameIsValidIdna("测试・中国.cn")); // \u30fb
    }

    /**
     * validate domain.
     * 
     * @param domainName
     *            domain name.
     * @return boolean.
     */
    private boolean validateDomainNameIsValidIdna(String domainName) {
        String decodeDomain = domainName;
        try {
            decodeDomain =
                    DomainUtil.urlDecodeAndReplaceAsciiToLowercase(domainName);
        } catch (DecodeException e) {
            e.printStackTrace();
        }
        return DomainUtil.validateDomainNameIsValidIdna(decodeDomain,false);
    }

    /**
     * validate search domain.
     * 
     * @param domainName
     *            domain name.
     * @return boolean.
     */
    private boolean validateSearchStringIsValidIdna(String domainName) {
        String decodeDomain = domainName;
        try {
            decodeDomain =
                    DomainUtil.urlDecodeAndReplaceAsciiToLowercase(domainName);
        } catch (DecodeException e) {
            e.printStackTrace();
        }
        return DomainUtil.validateSearchStringIsValidIdna(decodeDomain);
    }

    /**
     * test validateSearchStringIsValidIdna.
     * 
     */
    @Test
    public void testValidateSearchStringIsValidIdna() {
        assertTrue(validateSearchStringIsValidIdna("cn*"));
        assertTrue(validateSearchStringIsValidIdna("中国*"));
        assertTrue(validateSearchStringIsValidIdna("cn.中国*"));
        assertTrue(validateSearchStringIsValidIdna("cn中国*"));
        assertTrue(validateSearchStringIsValidIdna("*.cn"));// this is checked
                                                            // for error 422
        assertTrue(validateSearchStringIsValidIdna("*cn"));// this is checked
                                                           // for error 422
        assertTrue(validateSearchStringIsValidIdna("*"));// this is checked for
                                                         // error 422
        assertFalse(validateSearchStringIsValidIdna("σειράτάξησυπουργείωνΣύνθεσηυπουργικούσυμβουλίουουουο*.bnnhg"));
        assertFalse(validateSearchStringIsValidIdna("%CF*.bnnhg"));
        assertFalse(validateSearchStringIsValidIdna("1**.bnnhg"));
        assertFalse(validateSearchStringIsValidIdna("-*.bnnhg"));
        assertFalse(validateSearchStringIsValidIdna("%CF*.bnnhg"));
        assertFalse(validateSearchStringIsValidIdna("Σ*.bnnhg"));
        assertFalse(validateSearchStringIsValidIdna("ύύ--*.bnnhg"));

        assertTrue(validateSearchStringIsValidIdna("xn--123*.bnnhg"));
        assertTrue(validateSearchStringIsValidIdna("xn--*.bnnhg"));
        assertTrue(validateSearchStringIsValidIdna("xn--*"));
        assertTrue(validateSearchStringIsValidIdna("%CF%83*.bnnhg"));
        assertTrue(validateSearchStringIsValidIdna("cn--*.bnnhg"));
        assertTrue(validateSearchStringIsValidIdna("ύ*.bnnhg"));

        assertTrue(validateSearchStringIsValidIdna("cnnic*.cn"));
        assertTrue(validateSearchStringIsValidIdna("xn--fiqa61au8b7zsevnm8ak20mc4a87e*.cn"));
        assertTrue(validateSearchStringIsValidIdna("xn--elaaaa*.com"));
        assertTrue(validateSearchStringIsValidIdna("xn--123123*.cn"));
        assertTrue(validateSearchStringIsValidIdna("中国互联*.cn"));
        assertTrue(validateSearchStringIsValidIdna("\u0205*.cn"));

        assertTrue(validateSearchStringIsValidIdna("mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo*.cn"));
        assertTrue(validateSearchStringIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa*.bnnhg"));
        assertTrue(validateSearchStringIsValidIdna("mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo*.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabal1.cn"));
        assertTrue(validateSearchStringIsValidIdna("mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo*.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabal1.cn."));
        assertTrue(validateSearchStringIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa*.xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.jaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg"));
        assertTrue(validateSearchStringIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa*.xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.jaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg."));

        assertTrue(validateSearchStringIsValidIdna("1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.com*"));
        assertTrue(validateSearchStringIsValidIdna("1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.com*."));

        assertTrue(validateSearchStringIsValidIdna("cnnic*.cn"));
        assertTrue(validateSearchStringIsValidIdna("xn--fiqa61au8b7zsevnm8ak20mc4a87e.cn*."));
        assertTrue(validateSearchStringIsValidIdna("xn--elaaaa.com.*"));
        assertTrue(validateSearchStringIsValidIdna("xn--ELAAAA.COM*."));

        assertTrue(validateSearchStringIsValidIdna("%C8%85%C8%85%C8%85%C8%85*.com"));
        assertTrue(validateSearchStringIsValidIdna("%E4%B8%AD%E5%9B%BD%E4%BA%92%E8%81%94%E7%BD%91%E7%BB%9C%E4%BF%A1%E6%81%AF%E4%B8%AD%E5%BF%83.cn*"));

        assertTrue(validateSearchStringIsValidIdna("256.in-addr.arpa*"));
        assertTrue(validateSearchStringIsValidIdna("1.2.3.4.5.in-addr.arpa*."));

        assertFalse(validateSearchStringIsValidIdna(".1.in-addr.arpa*"));
        assertFalse(validateSearchStringIsValidIdna("1..5.in-addr.arpa*"));
        assertTrue(validateSearchStringIsValidIdna("1.2-5.in-addr.arpa*"));
        assertFalse(validateSearchStringIsValidIdna("1.-25.in-addr.arpa*"));
        assertFalse(validateSearchStringIsValidIdna("1.-25.in-addr.arpa*.."));
        assertFalse(validateSearchStringIsValidIdna("1.-25-.in-addr.arpa*"));

        assertFalse(validateSearchStringIsValidIdna("abcd123456789012345678901234567890123456789012345678901234567890.cn*"));
        assertFalse(validateSearchStringIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa1.bnnhg*"));

        assertFalse(validateSearchStringIsValidIdna("1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.n.*"));
        assertFalse(validateSearchStringIsValidIdna("1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.1.2.3.4.5.6.7.8.9.0.a.b.c.d.e.f.g.h.i.j.k.n*"));
        assertFalse(validateSearchStringIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.jaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa1.bnnhg*"));
        assertFalse(validateSearchStringIsValidIdna("mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouoo.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabalookouo.mamalookhahababalookhehewhowholookhouhoumamalookaiaibabal1.cn"));

        assertFalse(validateSearchStringIsValidIdna("cnnic。cn*"));
        assertFalse(validateSearchStringIsValidIdna(".cnnic.cn*"));
        assertFalse(validateSearchStringIsValidIdna("ww..he.cn*"));
        assertFalse(validateSearchStringIsValidIdna("-cnnic-.cn*"));

        assertFalse(validateSearchStringIsValidIdna("cnnic-.cn*"));
        assertFalse(validateSearchStringIsValidIdna("ab.-.cn*"));
        assertFalse(validateSearchStringIsValidIdna("cnnic_cn*"));
        assertFalse(validateSearchStringIsValidIdna("cnnic%2C.cn*"));

        assertFalse(validateSearchStringIsValidIdna("cnnic%25.cn*"));
        assertFalse(validateSearchStringIsValidIdna("cnnic*.cn*"));
        assertFalse(validateSearchStringIsValidIdna("cnni+c.cn*"));
        assertFalse(validateSearchStringIsValidIdna("cn<n>ic.cn*"));

        assertFalse(validateSearchStringIsValidIdna("cnnic. .cn*"));
        assertFalse(validateSearchStringIsValidIdna("^cnnic.cn*"));
        assertFalse(validateSearchStringIsValidIdna("cnnic,.cn*"));
        assertFalse(validateSearchStringIsValidIdna("cnnic:cn*"));

        assertTrue(validateSearchStringIsValidIdna("cnnic*.cn"));
        assertFalse(validateSearchStringIsValidIdna("cnni+c.cn*"));
        assertFalse(validateSearchStringIsValidIdna("cn<n>ic.cn*"));
        assertFalse(validateSearchStringIsValidIdna("xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaaa4423b5n2b2s5b340b*.cn"));
        assertFalse(validateSearchStringIsValidIdna("xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.edu.cn*"));

        assertFalse(validateSearchStringIsValidIdna("xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.xn--6iq8zxlq4ga518lea949ds8ax1at67b8mc412a4uaw57iy98ao71bi59a.cn*"));
        assertFalse(validateSearchStringIsValidIdna("12--11中国*.中国"));
        assertFalse(validateSearchStringIsValidIdna("xn--5us中国*.中国"));
        assertFalse(validateSearchStringIsValidIdna("xn--5us.%E4%B8%AD%E5%9B%BD*"));

        assertFalse(validateSearchStringIsValidIdna("xn--5us.%E4%B8%AD%E5%9B%BD*"));
        assertFalse(validateSearchStringIsValidIdna("xn--5us.中国*"));
        assertFalse(validateSearchStringIsValidIdna("xn--5us*.qqéa.cn"));

        assertFalse(validateSearchStringIsValidIdna(".好.中国*"));
        assertFalse(validateSearchStringIsValidIdna("12..好.中国*"));
        assertFalse(validateSearchStringIsValidIdna("-好.中国*"));
        assertFalse(validateSearchStringIsValidIdna("好-.中国*"));

        assertFalse(validateSearchStringIsValidIdna("好-.中国*"));
        assertFalse(validateSearchStringIsValidIdna("-好-.中国*"));
        assertFalse(validateSearchStringIsValidIdna("好.-.中国*"));

        // DISALLOWED & UNASSIGNED
        assertFalse(validateSearchStringIsValidIdna("123〳*.中国"));
        assertFalse(validateSearchStringIsValidIdna("123Ā*.中国"));
        assertFalse(validateSearchStringIsValidIdna("123Ƽ*.中国"));
        assertFalse(validateSearchStringIsValidIdna("123஝஝஝஝*.中国")); // \u0b9d஝
        assertFalse(validateSearchStringIsValidIdna("123઱஝஝*.中国")); // \u0ab1
        assertFalse(validateSearchStringIsValidIdna("123�஝஝*.中国")); // \ufff8

        // wrong utf-8
        assertFalse(validateSearchStringIsValidIdna("%C81%851%C81%815%C81%851%C81%851.com*"));
        // assertFalse(validateSearchStringIsValidIdna("%E%B8%AD%E5%9B%BD%E4%BA%92%E8%81%94%E7%BD%91%E7%BB%9C%E4%BF%A1%E6%81%AF%E4%B8%AD%E5%BF%83.cn*"));
        assertFalse(validateSearchStringIsValidIdna("好.-.中国*"));
        assertFalse(validateSearchStringIsValidIdna("好.-.中国*"));
        assertFalse(validateSearchStringIsValidIdna("好.-.中国*"));
        assertFalse(validateSearchStringIsValidIdna("好.-.中国*"));

    }

    @Test
    public void testValidateSearchStringIsValidIdnaException() {
        // SHOULD BE FALSE
        assertFalse(validateSearchStringIsValidIdna("cnnic%.cn*"));

        assertFalse(validateSearchStringIsValidIdna("%E%B8%AD%E5%9B%BD%E4%BA%92%E8%81%94%E7%BD%91%E7%BB%9C%E4%BF%A1%E6%81%AF%E4%B8%AD%E5%BF%83.cn*"));
    }

    /**
     * test decodeAndTrim.
     * 
     * @throws DecodeException
     * 
     */
    @Test
    public void testDecodeAndTrim() throws DecodeException {
        assertEquals("中文.中国",
                DomainUtil.urlDecode("%E4%B8%AD%E6%96%87.%E4%B8%AD%E5%9B%BD"));
        assertEquals("中文.cn",
                DomainUtil.urlDecodeAndReplaceAsciiToLowercase("中文.Cn"));
        assertEquals("中c文n.cn",
                DomainUtil.urlDecodeAndReplaceAsciiToLowercase("中C文n.Cn"));
    }

    /**
     * test decodeAndTrim.
     * 
     * @throws DecodeException
     * 
     */
    @Test
    public void testDecodeAndTrimDecodedStr() throws DecodeException {
        String result = DomainUtil.urlDecode("中文.中国");
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
    
    /**
     * test generate puny name for unicode domain.
     * 
     */
    @Test
    public void testIsLdh() {
        boolean result = DomainUtil.isLdh("ns.ddomain.cn");
        assertEquals(true, result);
    }
}
