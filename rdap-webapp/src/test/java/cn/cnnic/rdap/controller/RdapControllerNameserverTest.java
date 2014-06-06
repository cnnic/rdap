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
package cn.cnnic.rdap.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.common.util.StringUtil;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController
 * 
 * @author weijunkai
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerNameserverTest extends BaseTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    final private String urlPath = "/.well-known/rdap/nameserver/";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test query exist ns.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardownNSTest.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/nameserverTest.xml")
    public void testQueryExistNameserver() throws Exception {
        String nsName = "ns.abc";
        String nsNameWithPrefixBlank = " ns.abc";
        String nsChineseLDH = "ns.清华大学.中国";
        String nsChineseUnicode = "ns.xn--xkry9kk1bz66a.xn--fiqs8s";
        String nsLangEn = "en";
        String nsLangZh = "zh";
        commonQueryExistNS(nsName, nsName, nsName, nsLangEn);
        commonQueryExistNS(nsNameWithPrefixBlank, nsName, nsName, nsLangEn);
        String nsChineseOrg = nsChineseLDH;
        nsChineseLDH = StringUtil.urlEncode(nsChineseLDH);
        commonQueryExistNS(nsChineseLDH, nsChineseOrg, nsChineseUnicode,
                nsLangZh);
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    public void testQueryNonExistNS() throws Exception {
        commonQueryNonExistNS("1cnnic.cn");
        commonQueryNonExistNS("cnnic.com.cn");
        commonQueryNonExistNS("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg");
        commonQueryNonExistNS("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CF%83%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF.bnnhg");
        commonQueryNonExistNS("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg");
    }

    /**
     * test query exist nameserver.
     * 
     * @throws Exception
     */
    @Test
    public void testQueryInvalidNS() throws Exception {
        commonQueryInvalidNS("123");
        commonQueryInvalidNS("c nnic.cn");
    }

    /**
     * common query exist NS.
     * 
     * @param queryNSName
     *            nameserver name.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryExistNS(String queryNSName, String expectedLdhName,
            String expectedUnicodeName, String lang) throws Exception {
        System.err.println(queryNSName + expectedLdhName + expectedUnicodeName
                + lang);
        mockMvc.perform(
                get(urlPath + queryNSName).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lang").value(lang))
                .andExpect(jsonPath("$.ldhName").exists())
                .andExpect(jsonPath("$.unicodeName").exists())
                .andExpect(jsonPath("$.port43").value("port43"));
    }

    /**
     * common query non-exist NS.
     * 
     * @param nsName
     *            nameserver name.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryNonExistNS(String queryDomainName) throws Exception {
        mockMvc.perform(
                get(urlPath + queryDomainName).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"));
    }

    /**
     * common query invalid NS.
     * 
     * @param nsName
     *            nameserver name.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryInvalidNS(String nsName) throws Exception {
        mockMvc.perform(
                get(urlPath + nsName).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));

    }
}
