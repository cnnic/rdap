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

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.common.util.RestResponseUtil;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerDomainSearchTest extends BaseTest {
    /**
     * domain search uri.
     */
    private static final String DOMAIN_SEARCH_URI = "/.well-known/rdap/domains?name=";

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
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:cn/cnnic/rdap/dao/impl/domain-search.xml")
    public void testSearchExistDomain() throws Exception {
        String domainName = "cnnic.cn";
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "cnnic*").accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(
                        jsonPath("$.rdapConformance").value(
                                CoreMatchers.hasItem("rdap_level_0")))
                .andExpect(jsonPath("$.domainSearchResults").exists())
                .andExpect(jsonPath("$.domainSearchResults").isArray())
                .andExpect(
                        jsonPath("$.domainSearchResults",
                                Matchers.hasItem(Matchers.hasKey("handle"))))
                .andExpect(
                        jsonPath("$.domainSearchResults",
                                Matchers.hasItem(Matchers.hasValue("cnnic.cn"))))
                .andExpect(
                        jsonPath("$.domainSearchResults", Matchers
                                .hasItem(Matchers.hasValue("cnnic2.cn"))))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].lang").value("zh"))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].handle").value("1"))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].ldhName").value(
                                domainName))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].unicodeName").value(
                                domainName))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].port43").value(
                                "port43"))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].secureDNS").exists())
                .andExpect(
                        jsonPath("$.domainSearchResults[0].status").isArray())
                .andExpect(
                        jsonPath("$.domainSearchResults[0].status").value(
                                CoreMatchers.hasItems("validated",
                                        "update prohibited")))
                // remarks.
                .andExpect(
                        jsonPath("$.domainSearchResults[0].remarks").isArray())
                .andExpect(
                        jsonPath("$.domainSearchResults[0].remarks[0]")
                                .exists())
                .andExpect(
                        jsonPath("$.domainSearchResults[0].remarks[0].title")
                                .exists())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].description")
                                .exists())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].description")
                                .isArray())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].description")
                                .value(CoreMatchers.hasItems("description1",
                                        "description2")))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].remarks[0].links")
                                .exists())
                .andExpect(
                        jsonPath("$.domainSearchResults[0].remarks[0].links")
                                .isArray())
                .andExpect(
                        jsonPath("$.domainSearchResults[0].remarks[0].links[0]")
                                .exists())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].links[0].value")
                                .exists())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].links[0].rel")
                                .exists())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].links[0].href")
                                .exists())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].links[0].type")
                                .exists())
                .andExpect(
                        jsonPath(
                                "$.domainSearchResults[0].remarks[0].links[0].type")
                                .value("application/json"))
                // links.
                .andExpect(
                        jsonPath("$.domainSearchResults[0].links[0].hreflang")
                                .value(CoreMatchers.hasItems("en", "zh")))
                .andExpect(
                        jsonPath("$.domainSearchResults[0].links[0].title")
                                .exists());

    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:cn/cnnic/rdap/dao/impl/domain-search.xml")
    public void testSearchTruncatedDomain() throws Exception {
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "truncated*").accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(
                        jsonPath("$.rdapConformance").value(
                                CoreMatchers.hasItem("rdap_level_0")))
                .andExpect(jsonPath("$.resultsTruncated").value(true))
                .andExpect(jsonPath("$.domainSearchResults").exists())
                .andExpect(jsonPath("$.domainSearchResults").isArray())
                .andExpect(jsonPath("$.domainSearchResults", hasSize(5)))
                .andExpect(
                        jsonPath("$.domainSearchResults",
                                Matchers.hasItem(Matchers.hasKey("handle"))));
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:cn/cnnic/rdap/dao/impl/errorMessage.xml")
    public void testSearchNonExistDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "nonexist*").accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(
                        jsonPath("$.rdapConformance").value(
                                CoreMatchers.hasItem("rdap_level_0")))
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"))
                .andExpect(jsonPath("$.resultsTruncated").doesNotExist())
                .andExpect(jsonPath("$.domainSearchResults").doesNotExist());
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/errorMessage.xml")
    public void testSearchInvalidDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "*").accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("UNPROCESSABLE ENTITY"))
                .andExpect(jsonPath("$.description").value("UNPROCESSABLE ENTITY"));

    }
}
