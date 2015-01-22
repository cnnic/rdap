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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restfulwhois.rdap.common.util.IdnaUtil;

/**
 * Test for IpUtil.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class IdnaUtilTest {
    
    @Test
    public void test_checkIfValidALabelDomain() {
        assertTrue(IdnaUtil.checkIfValidALabelDomain("中国"));
        assertTrue(IdnaUtil.checkIfValidALabelDomain("xn--fiqs8s"));
        assertTrue(IdnaUtil.checkIfValidALabelDomain("xn--fiqs8s.cn"));
        assertTrue(IdnaUtil.checkIfValidALabelDomain("xn--fiqs8s.中国"));
        assertTrue(IdnaUtil.checkIfValidALabelDomain("123"));
        assertTrue(IdnaUtil.checkIfValidALabelDomain("123.xn--fiqs8s"));
        assertTrue(IdnaUtil.checkIfValidALabelDomain("中国.xn--fiqs8s"));
        
        assertFalse(IdnaUtil.checkIfValidALabelDomain("xn--123"));
        assertFalse(IdnaUtil.checkIfValidALabelDomain("123.xn--123"));
        assertFalse(IdnaUtil.checkIfValidALabelDomain("xn--123.cn"));
        assertFalse(IdnaUtil.checkIfValidALabelDomain("xn--123.中国"));
        assertFalse(IdnaUtil.checkIfValidALabelDomain("xn--123*"));
        assertFalse(IdnaUtil.checkIfValidALabelDomain("55--123"));
    }
    
    /**
     * test isValidIdn.
     * 
     */
    @Test
    public void testIsValidIdn() {
        assertTrue(IdnaUtil.isValidIdn("cnnic.cn"));
        assertTrue(IdnaUtil.isValidIdn("cnnic"));
        assertTrue(IdnaUtil.isValidIdn("cnnic.com.cn"));
        assertTrue(IdnaUtil.isValidIdn("123"));
        assertTrue(IdnaUtil.isValidIdn("1cnnic.cn"));
        assertTrue(IdnaUtil
                .isValidIdn("63aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.cn"));
        assertTrue(IdnaUtil
                .isValidIdn("253aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertFalse(IdnaUtil
                .isValidIdn("254aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"));
        assertFalse(IdnaUtil
                .isValidIdn("64aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.cn"));
        assertFalse(IdnaUtil.isValidIdn(""));
        assertFalse(IdnaUtil.isValidIdn(" "));
        assertFalse(IdnaUtil.isValidIdn(null));
        assertFalse(IdnaUtil.isValidIdn("."));
        assertFalse(IdnaUtil.isValidIdn("Σ.cn"));
        assertFalse(IdnaUtil.isValidIdn("@.cn"));
        assertFalse(IdnaUtil.isValidIdn("a@.cn"));
        assertFalse(IdnaUtil.isValidIdn("@a.cn"));
        assertFalse(IdnaUtil.isValidIdn("cnnic."));
        assertFalse(IdnaUtil.isValidIdn("cnnic-.cn"));
        assertFalse(IdnaUtil.isValidIdn("-cnnic.cn"));
        assertFalse(IdnaUtil.isValidIdn("boss.-cnnic.cn"));
        assertFalse(IdnaUtil.isValidIdn("boss.cnnic-.cn"));
        assertFalse(IdnaUtil.isValidIdn("boss.cnnic.-cn"));
        assertFalse(IdnaUtil.isValidIdn("boss.cnnic.cn-"));
        assertFalse(IdnaUtil.isValidIdn("cnnic.cn."));
        assertFalse(IdnaUtil.isValidIdn("xn--55qx5d.中国"));
        assertFalse(IdnaUtil.isValidIdn("xn--55qx5d.中国.cn"));
    }
}
