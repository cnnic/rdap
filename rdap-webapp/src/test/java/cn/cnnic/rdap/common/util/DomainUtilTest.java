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

import org.junit.Test;

/**
 * Test for StringUtil.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class DomainUtilTest {

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
        assertTrue(DomainUtil.validateDomainNameIsValidIdna("cnnic.cn"));
        assertTrue(DomainUtil
                .validateDomainNameIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg"));
        assertTrue(DomainUtil
                .validateDomainNameIsValidIdna(DomainUtil
                        .decodeAndTrim("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CF%83%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF.bnnhg")));
        assertTrue(DomainUtil
                .validateDomainNameIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg"));
        assertTrue(DomainUtil
                .validateDomainNameIsValidIdna("σειράτάξησυπουργείωνσύνθεσηυπουργικούσυμβουλίουουουο.bnnhg"));
        assertFalse(DomainUtil.validateDomainNameIsValidIdna(DomainUtil
                .decodeAndTrim("xn--caf%C3%A9s.com")));
        assertFalse(DomainUtil.validateDomainNameIsValidIdna("xn--cafés.com"));
        assertFalse(DomainUtil
                .validateDomainNameIsValidIdna(DomainUtil
                        .decodeAndTrim("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CF%83%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CE%BF.bnnhg")));
        assertFalse(DomainUtil
                .validateDomainNameIsValidIdna("xn--hxaajaoebldbselhkqsqmapxidccaaaahjsgk5chhdiq0cclcgddbb8o9hoa.bnnhg"));
        assertFalse(DomainUtil
                .validateDomainNameIsValidIdna("σειράτάξησυπουργείωνσύνθεσηυπουργικούσυμβουλίουουουοο.bnnhg"));
        assertFalse(DomainUtil
                .validateDomainNameIsValidIdna(DomainUtil
                        .decodeAndTrim("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CE%A3%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF.bnnhg")));
        assertFalse(DomainUtil
                .validateDomainNameIsValidIdna("σειράτάξησυπουργείωνΣύνθεσηυπουργικούσυμβουλίουουουο.bnnhg"));
        assertFalse(DomainUtil.validateDomainNameIsValidIdna(""));
    }

    /**
     * test decodeAndTrim.
     * 
     */
    @Test
    public void testDecodeAndTrim() {
        String result = DomainUtil
                .decodeAndTrim("%E4%B8%AD%E6%96%87.%E4%B8%AD%E5%9B%BD");
        assertEquals("中文.中国", result);
    }

    /**
     * test decodeAndTrim.
     * 
     */
    @Test
    public void testDecodeAndTrimDecodedStr() {
        String result = DomainUtil.decodeAndTrim("中文.中国");
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
