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

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.common.util.RestResponseUtil;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerDomainTest extends BaseTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/domain.xml")
    public void testQueryExistDomain() throws Exception {
        String domainName = "cnnic.cn";
        String domainNameWithLastDot = "cnnic.cn.";
        String domainNameWithPrefixBlank = " cnnic.cn";
        commonQueryExistDomain(domainName, domainName, domainName);
        commonQueryExistDomain(domainNameWithLastDot, domainName, domainName);
        commonQueryExistDomain(domainNameWithPrefixBlank, domainName,
                domainName);
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/errorMessage.xml")
    public void testQueryNonExistDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        commonQueryNonExistDomain("1cnnic.cn");
        commonQueryNonExistDomain("cnnic.com.cn");
        commonQueryNonExistDomain("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg");
        commonQueryNonExistDomain("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CF%83%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF.bnnhg");
        commonQueryNonExistDomain("xn--hxaajaoebldbselhkqsqmapxidccaaahjrgk3chhdip9bclcgddbb4ooioa.bnnhg");
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/errorMessage.xml")
    public void testQueryInvalidDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        commonQueryInvalidDomain("123");
        commonQueryInvalidDomain("c nnic.cn");
        commonQueryInvalidDomain("cnnic");
        commonQueryInvalidDomain("cnnic.");
        commonQueryInvalidDomain("");
        commonQueryInvalidDomain(" ");
        commonQueryInvalidDomain(".");
        commonQueryInvalidDomain("a.");
        commonQueryInvalidDomain(".a");
        commonQueryInvalidDomain("Σ.cn");
        commonQueryInvalidDomain("@.cn");
        commonQueryInvalidDomain("a@.cn");
        commonQueryInvalidDomain("@a.cn");
        commonQueryInvalidDomain("xn--55qx5d.中国");
        commonQueryInvalidDomain("xn--55qx5d.中国.cn");
        commonQueryInvalidDomain("xn--caf%C3%A9s.com");
        commonQueryInvalidDomain("xn--cafés.com");
        commonQueryInvalidDomain("σειράτάξησυπουργείωνσύνθεσηυπουργικούσυμβουλίουουουο.bnnhg");
        commonQueryInvalidDomain("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CF%83%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CE%BF.bnnhg");
        commonQueryInvalidDomain("xn--hxaajaoebldbselhkqsqmapxidccaaaahjsgk5chhdiq0cclcgddbb8o9hoa.bnnhg");
        commonQueryInvalidDomain("σειράτάξησυπουργείωνσύνθεσηυπουργικούσυμβουλίουουουοο.bnnhg");
        commonQueryInvalidDomain("%CF%83%CE%B5%CE%B9%CF%81%CE%AC%CF%84%CE%AC%CE%BE%CE%B7%CF%83%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B5%CE%AF%CF%89%CE%BD%CE%A3%CF%8D%CE%BD%CE%B8%CE%B5%CF%83%CE%B7%CF%85%CF%80%CE%BF%CF%85%CF%81%CE%B3%CE%B9%CE%BA%CE%BF%CF%8D%CF%83%CF%85%CE%BC%CE%B2%CE%BF%CF%85%CE%BB%CE%AF%CE%BF%CF%85%CE%BF%CF%85%CE%BF%CF%85%CE%BF.bnnhg");
    }

    /**
     * common query exist domain.
     * 
     * @param domainName
     *            domain name.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryExistDomain(String queryDomainName,
            String expectedLdhName, String expectedUnicodeName)
            throws Exception {
        ResultActions r = mockMvc.perform(get(
                "/.well-known/rdap/domain/" + queryDomainName).accept(
                MediaType.parseMediaType("application/json")));
        mockMvc.perform(
                get("/.well-known/rdap/domain/" + queryDomainName).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.lang").value("zh"))
                .andExpect(jsonPath("$.handle").value("1"))
                .andExpect(jsonPath("$.ldhName").value(expectedLdhName))
                .andExpect(jsonPath("$.unicodeName").value(expectedUnicodeName))
                .andExpect(jsonPath("$.port43").value("port43"))
                .andExpect(jsonPath("$.secureDNS").exists())
                .andExpect(jsonPath("$.status").isArray())
                .andExpect(
                        jsonPath("$.status").value(
                                CoreMatchers.hasItems("validated",
                                        "update prohibited")))
                // remarks.
                .andExpect(jsonPath("$.remarks").isArray())
                .andExpect(jsonPath("$.remarks[0]").exists())
                .andExpect(jsonPath("$.remarks[0].title").exists())
                .andExpect(jsonPath("$.remarks[0].description").exists())
                .andExpect(jsonPath("$.remarks[0].description").isArray())
                .andExpect(
                        jsonPath("$.remarks[0].description").value(
                                CoreMatchers.hasItems("description1",
                                        "description2")))
                .andExpect(jsonPath("$.remarks[0].links").exists())
                .andExpect(jsonPath("$.remarks[0].links").isArray())
                .andExpect(jsonPath("$.remarks[0].links[0]").exists())
                .andExpect(jsonPath("$.remarks[0].links[0].value").exists())
                .andExpect(jsonPath("$.remarks[0].links[0].rel").exists())
                .andExpect(jsonPath("$.remarks[0].links[0].href").exists())
                .andExpect(jsonPath("$.remarks[0].links[0].type").exists())
                .andExpect(
                        jsonPath("$.remarks[0].links[0].type").value(
                                "application/json"))
                // links.
                .andExpect(
                        jsonPath("$.links[0].hreflang").value(
                                CoreMatchers.hasItems("en", "zh")))
                .andExpect(jsonPath("$.links[0].title").exists())

        ;
    }

    /**
     * common query non-exist domain.
     * 
     * @param domainName
     *            domain name.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryNonExistDomain(String queryDomainName)
            throws Exception {
        mockMvc.perform(
                get("/.well-known/rdap/domain/" + queryDomainName).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"));
    }

    /**
     * common query invalid domain.
     * 
     * @param domainName
     *            domain name.
     * @throws Exception
     *             Exception.
     */
    private void commonQueryInvalidDomain(String domainName) throws Exception {
        mockMvc.perform(
                get("/.well-known/rdap/domain/" + domainName).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));

    }
}
