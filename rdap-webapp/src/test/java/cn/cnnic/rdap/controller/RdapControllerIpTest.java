/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditioip are met:
 *  
 * * Redistributioip of source code must retain the above copyright notice,
 *  this list of conditioip and the following disclaimer.
 * * Redistributioip in binary form must reproduce the above copyright notice,
 *  this list of conditioip and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR COIPEQUENTIAL DAMAGES
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
public class RdapControllerIpTest extends BaseTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    final private String urlPath = "/.well-known/rdap/ip/";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test query exist ip 200.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/ip-query.xml")
    public void testQueryExistIp() throws Exception {
        String ipV4 = "0.0.0.1";
        String ipV4Mask = "0.0.1.1/1";
        String ipV6Omit = "0:1::0";
        String ipV6Full = "1:1:1:1:1:1:1:1";
        String ipV6Mask = "1:1:1:1:1:1:1:1/1";
        String ipV6V4 = "1:1:1:1:1:1:0.1.0.1";
        String ipV6V4Mask = "1:1:1:1:1:1:0.1.0.1/1";
        String ipV6V4MaskQ = "1:1:1:1:1:1:0.1.0.1/1?abc";
        String ipLangEn = "en";
        commonQueryExistIP(ipV4, ipLangEn);
        commonQueryExistIP(ipV4Mask, ipLangEn);
        commonQueryExistIP(ipV6Omit, ipLangEn);
        commonQueryExistIP(ipV6Full, ipLangEn);
        commonQueryExistIP(ipV6Mask, ipLangEn);
        commonQueryExistIP(ipV6V4, ipLangEn);
        commonQueryExistIP(ipV6V4Mask, ipLangEn);
        commonQueryExistIP(ipV6V4MaskQ, ipLangEn);
        commonQueryExistIP("/::/2", ipLangEn);
    }

    /**
     * common query exist IP 200.
     * 
     * @param queryIPName
     *            query ip address.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryExistIP(String queryIP, String lang)
            throws Exception {
        mockMvc.perform(
                get(urlPath + queryIP).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lang").value(lang))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.port43").value("port43"));
    }

    /**
     * test query no exist ip for 404.
     * 
     * @throws Exception
     */
    @Test
    public void testQueryNonExistIP() throws Exception {
        commonQueryNonExistIP("255.255.255.255");
        commonQueryNonExistIP("255.255.255.255/2");
        commonQueryNonExistIP("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff");
        commonQueryNonExistIP("ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff/8");
        commonQueryNonExistIP("ffff:ffff:ffff:ffff:ffff:ffff:255.255.255.254");
        commonQueryNonExistIP("ffff:ffff:ffff:ffff:ffff:ffff:255.255.255.254/3");
    }

    /**
     * common query non-exist IP 404.
     * 
     * @param ipName
     *            query ip address.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryNonExistIP(String queryDomainName) throws Exception {
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
     * test query invalid ip 400.
     * 
     * @throws Exception
     */
    @Test
    public void testQueryInvalidIP() throws Exception {
        commonQueryInvalidIP("/ /");
        commonQueryInvalidIP("//");
        commonQueryInvalidIP("/ /");
        commonQueryInvalidIP("/1.1.1.1/?3");
        commonQueryInvalidIP("/1.1.1.1/33");
        commonQueryInvalidIP("c nnic.cn");
        commonQueryInvalidIP("1.1.1.1&2.2.2.2");
        commonQueryInvalidIP("/1::1/129");
    }

    /**
     * common query invalid IP 400.
     * 
     * @param ipName
     *            query ip address.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryInvalidIP(String ipName) throws Exception {
        mockMvc.perform(
                get(urlPath + ipName).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));

    }
}
