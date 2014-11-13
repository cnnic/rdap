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
package org.restfulwhois.rdap.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

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
    private static final String DOMAIN_SEARCH_URI = "/domains";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    /**
     * output json.
     */
    final private String rdapJson = "application/json";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test search 422 and 400.
     * 
     * @throws Exception
     */
    @Test
    public void testSearchQStatus() throws Exception {
        RestResponseUtil.initErrorMessages();
        List<String> q422List = new ArrayList<String>();
        q422List.add("*");
        // q422List.add("*cn");
        // q422List.add("*.cn");
        q422List.add("*cn*");
        q422List.add("1**.bnnhg");
        List<String> q404List = new ArrayList<String>();
        q404List.add("*cn");
        q404List.add("*.cn");
        List<String> q400List = new ArrayList<String>();
        q400List.add("σειράτάξησυπουργείωνΣύνθεσηυπουργικούσυμβουλίουουουο*.bnnhg");
        q400List.add("%CF*.bnnhg");
        q400List.add("-*.bnnhg");
        q400List.add("%CF*.bnnhg");
        q400List.add("Σ*.bnnhg");
        q400List.add("ύύ--*.bnnhg");
        List<String> q200List = new ArrayList<String>();
        q200List.add("xn--123*.bnnhg");
        q200List.add("xn--*.bnnhg");
        q200List.add("%CF%83*.bnnhg");
        q200List.add("cn--*.bnnhg");
        q200List.add("ύ*.bnnhg");
        q200List.add("cn*");
        q200List.add("中国*");
        for (String q : q422List) {
            mockMvc.perform(
                    get(DOMAIN_SEARCH_URI + "?name=" + encodeWithIso8859(q))
                            .accept(MediaType.parseMediaType(rdapJson)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(content().contentType(rdapJson))
                    .andExpect(jsonPath("$.errorCode").value(422));
        }
        for (String q : q404List) {
            mockMvc.perform(
                    get(DOMAIN_SEARCH_URI + "?name=" + encodeWithIso8859(q))
                            .accept(MediaType.parseMediaType(rdapJson)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(rdapJson))
                    .andExpect(jsonPath("$.errorCode").value(404));
        }
        for (String q : q400List) {
            mockMvc.perform(
                    get(DOMAIN_SEARCH_URI + "?name=" + encodeWithIso8859(q))
                            .accept(MediaType.parseMediaType(rdapJson)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().contentType(rdapJson))
                    .andExpect(jsonPath("$.errorCode").value(400));
        }
        for (String q : q200List) {// no data, return 404 instead 200.
            mockMvc.perform(
                    get(DOMAIN_SEARCH_URI + "?name=" + encodeWithIso8859(q))
                            .accept(MediaType.parseMediaType(rdapJson)))
                    .andExpect(status().isNotFound())
                    .andExpect(content().contentType(rdapJson))
                    .andExpect(jsonPath("$.errorCode").value(404));
        }

    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testSearchExistDomain() throws Exception {
        super.databaseSetupWithBinaryColumns("domain-search.xml");
        String domainName = "cnnic.cn";
        /** search domain by nsLdhName */
        String nameHead = "?name=cnnic*";
        searchDomain(nameHead, domainName);

        /** search domain by nsLdhName */
        String nsLdhNameHead = "?nsLdhName=ns.cnnic*";
        searchDomain(nsLdhNameHead, domainName);

        /** search domain by nsIp */
        String ipHead = "?nsIp=";
        String nsNameIpV4 = ipHead + "218.241.111.11";
        String nsNameIpV6Full = ipHead + "ffff:ffff:ffff:ffff:0:0:0:ffff";
        String nsNameIpV6Omit = ipHead + "ffff:ffff:ffff:ffff::ffff";
        String nsIpZero1 = ipHead + "0::0";
        String nsIpZero2 = ipHead + "::";
        String nsIpV4V6 = ipHead + "::f:f:0.15.0.15";
        searchDomain(nsNameIpV4, domainName);
        searchDomain(nsNameIpV6Full, domainName);
        searchDomain(nsNameIpV6Omit, domainName);
        searchDomain(nsIpZero1, domainName);
        searchDomain(nsIpZero2, domainName);
        searchDomain(nsIpV4V6, domainName);
    }

    /** search domain */
    private void searchDomain(String strObject, String domainName)
            throws Exception {
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + strObject).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.domainSearchResults").exists())
                .andExpect(jsonPath("$.domainSearchResults").isArray())
                .andExpect(
                        jsonPath("$.domainSearchResults",
                                Matchers.hasItem(Matchers.hasKey("handle"))))
                .andExpect(
                        jsonPath("$.domainSearchResults",
                                Matchers.hasItem(Matchers.hasValue(domainName))))
                .andExpect(
                        jsonPath("$.domainSearchResults",
                                Matchers.hasItem(Matchers.hasValue(domainName))))
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
                                .value(rdapJson))
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
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testSearchTruncatedDomain() throws Exception {
        super.databaseSetupWithBinaryColumns("domain-search.xml");
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?name=" + "truncated*").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                // .andExpect(jsonPath("$.resultsTruncated").value(true))
                .andExpect(jsonPath("$.notices[0]").exists())
                .andExpect(jsonPath("$.domainSearchResults").exists())
                .andExpect(jsonPath("$.domainSearchResults").isArray())
                .andExpect(jsonPath("$.domainSearchResults", hasSize(5)))
                .andExpect(
                        jsonPath("$.domainSearchResults",
                                Matchers.hasItem(Matchers.hasKey("handle"))));
        // test by nsIp
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?nsIp=::ffff:ffff:ffff:fffe").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.domainSearchResults").exists())
                .andExpect(jsonPath("$.domainSearchResults").isArray())
                .andExpect(jsonPath("$.domainSearchResults", hasSize(2)))
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
    @DatabaseSetup(type = DatabaseOperation.REFRESH,
            value = "classpath:org/restfulwhois/rdap/dao/impl/errorMessage.xml")
    public
            void testSearchNonExistDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        // test by name
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?name=" + "nonexist*").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"))
                .andExpect(jsonPath("$.resultsTruncated").doesNotExist())
                .andExpect(jsonPath("$.domainSearchResults").doesNotExist());
        // test by nsLdhName
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?nsLdhName=" + "nonexist*").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"))
                .andExpect(jsonPath("$.resultsTruncated").doesNotExist())
                .andExpect(jsonPath("$.domainSearchResults").doesNotExist());
        // test by nsIp
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?nsIp=" + "255.255.255.0").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"))
                .andExpect(jsonPath("$.resultsTruncated").doesNotExist())
                .andExpect(jsonPath("$.nameserverSearchResults").doesNotExist());

    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/errorMessage.xml")
    public void testSearchInvalidDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?name=" + "*").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("UNPROCESSABLE ENTITY"))
                .andExpect(
                        jsonPath("$.description").value("UNPROCESSABLE ENTITY"));
        // test by nsLdhName
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?nsLdhName=*").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("UNPROCESSABLE ENTITY"))
                .andExpect(
                        jsonPath("$.description").value("UNPROCESSABLE ENTITY"));
        // test by nsIp
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + "?nsIp=*").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));

    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(
            value = { "classpath:org/restfulwhois/rdap/dao/impl/acl.xml" })
    public void testQuery403() throws Exception {
        super.databaseSetupWithBinaryColumns("domain.xml");
        String domainName = "?name=cnnic.*";
        search403(domainName);
    }

    private void search403(String domainName) throws Exception {
        mockMvc.perform(
                get(DOMAIN_SEARCH_URI + domainName).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(403))
                .andExpect(jsonPath("$.lang").value("en"));

    }

}
