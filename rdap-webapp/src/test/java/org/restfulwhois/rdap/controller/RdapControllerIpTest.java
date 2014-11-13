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
package org.restfulwhois.rdap.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController.
 * 
 * @author weijunkai
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerIpTest extends BaseTest {

    /**
     * a object of wac.
     */
    @Autowired
    private WebApplicationContext wac;

    /**
     * object mockMvc.
     */
    private MockMvc mockMvc;

    /**
     * ip query URI.
     */
    final private String URI_IP = "/ip/";

    /**
     * output json.
     */
    final private String rdapJson = "application/rdap+json";

    /**
     * set up mockMvc.
     */
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test query exist ip 200.
     * 
     * @throws Exception
     *             throw a exception.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryExistIp() throws Exception {
        super.databaseSetupWithBinaryColumns("ip-query.xml");
        String ipV4 = "1.0.0.0";
        String ipV4Mask = "1.0.0.0/8";
        String ipV6Omit = "0:0:0:0:2001:6a8::";
        String ipV6Full = "0:0:0:0:2001:6a8:0:0";
        String ipV6Mask = "0:0:0:0:2001:6a8::/96";
        String ipV6V4 = "0:0:0:0:2001:6a8::1.0.0.0";
        String ipV6V4Mask = "0:0:0:0:2001:6a8::1.0.0.0/96";
        String ipV6V4MaskQ = "0:0:0:0:2001:6a8::1.0.0.0/96?abc";
        String ipLangEn = "en";
        commonQueryExistIP(ipV4, ipLangEn);
        commonQueryExistIP(ipV4Mask, ipLangEn);
        commonQueryExistIP(ipV6Omit, ipLangEn);
        commonQueryExistIP(ipV6Full, ipLangEn);
        commonQueryExistIP(ipV6Mask, ipLangEn);
        commonQueryExistIP(ipV6V4, ipLangEn);
        commonQueryExistIP(ipV6V4Mask, ipLangEn);
        commonQueryExistIP(ipV6V4MaskQ, ipLangEn);
    }

    /**
     * common query exist IP 200.
     * 
     * @param queryIP
     *            query ip address.
     * @param lang
     *            a string of language
     * @throws Exception
     *             Exception.
     */
    private void commonQueryExistIP(String queryIP, String lang)
            throws Exception {
        mockMvc.perform(
                get(URI_IP + queryIP)
                        .accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.lang").value(lang))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.port43").value("port43"))
                .andExpect(jsonPath("$.objectClassName").value("ip network"));
    }

    /**
     * test query no exist ip for 404.
     * 
     * @throws Exception
     *             throw a exception
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
     * @param ipAddr
     *            query ip address.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryNonExistIP(String ipAddr) throws Exception {
        final int numNotFound = 404;
        mockMvc.perform(
                get(URI_IP + ipAddr).accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(numNotFound))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"));
    }

    /**
     * test query invalid ip 400.
     * 
     * @throws Exception
     *             throw a exception.
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
        final int numInvalid = 400;
        mockMvc.perform(
                get(URI_IP + ipName).accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(numInvalid))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(
            value = { "classpath:org/restfulwhois/rdap/dao/impl/acl.xml" })
    public void testQuery403() throws Exception {
        RestResponseUtil.initErrorMessages();
        super.databaseSetupWithBinaryColumns("ip-query.xml");
        String ip = "0:0:0:0:2001:6a8::";
        query403(ip);
    }

    private void query403(String q) throws Exception {
        mockMvc.perform(
                get(URI_IP + q).accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(403))
                .andExpect(jsonPath("$.lang").value("en"));

    }

}
