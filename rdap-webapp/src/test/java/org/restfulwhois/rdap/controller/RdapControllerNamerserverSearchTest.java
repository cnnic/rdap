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

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.core.common.util.RdapProperties;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController.
 * 
 * @author weijunkai
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerNamerserverSearchTest extends BaseTest {
    /**
     * ns search URI.
     */
    private static final String URI_NS_SEARCH = "/nameservers?";
    /**
     * output json.
     */
    private final String rdapJson = "application/rdap+json";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test search exist nameserver.
     * 
     * @throws Exception
     *             throw a exception.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testSearchExistNameserver() throws Exception {
        super.databaseSetupWithBinaryColumns("nameserver-search.xml");
        RestResponseUtil.initErrorMessages();
        String nsNameCn = "name=ns.cnnic*";
        String ipParamNamePrefix = "ip=";
        String nsNameIpV4 = ipParamNamePrefix + "218.241.111.11";
        String nsNameIpV6Full =
                ipParamNamePrefix + "ffff:ffff:ffff:ffff:0:0:0:ffff";
        String nsNameIpV6Omit = ipParamNamePrefix + "ffff:ffff:ffff:ffff::ffff";
        String nsIpV4 = ipParamNamePrefix + "0::0";
        String nsIpZero = ipParamNamePrefix + "::";
        String nsIpV4V6 = ipParamNamePrefix + "::f:f:0.15.0.15";
        String nsBoth1 = nsNameCn + "&" + ipParamNamePrefix + "::f:f:0.15.0.15";
        String nsBoth2 = ipParamNamePrefix + "::f:f:0.15.0.15" + "&" + nsNameCn;
        String nsComplex = "&name=ns.cnni*.cn&ip=::?name=0";
        searchByObject(nsNameCn);
        searchByObject(nsNameIpV4);
        searchByObject(nsNameIpV6Full);
        searchByObject(nsNameIpV6Omit);
        searchByObject(nsIpV4);
        searchByObject(nsIpZero);
        searchByObject(nsIpV4V6);
        searchByObject(nsBoth1);
        searchByObject(nsBoth2);
        searchByObject(nsComplex);
    }

    /**
     * 
     * @param strObject
     *            object as a parameter
     * @throws Exception
     *             throw a exception.
     */
    private void searchByObject(String strObject) throws Exception {
        RdapProperties prop = new RdapProperties();
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 2L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 2L);
        mockMvc.perform(
                get(URI_NS_SEARCH + strObject).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameserverSearchResults").exists())
                .andExpect(jsonPath("$.nameserverSearchResults").isArray())
                .andExpect(
                        jsonPath("$.nameserverSearchResults",
                                Matchers.hasItem(Matchers.hasKey("handle"))))
                .andExpect(
                        jsonPath("$.nameserverSearchResults",
                                Matchers.hasItem(Matchers.notNullValue())))
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].lang").value(
                                "en"))
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].handle")
                                .exists())
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].ldhName")
                                .exists())
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].unicodeName")
                                .exists())
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].port43").value(
                                "port43"))
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].status")
                                .isArray())
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].status").value(
                                CoreMatchers.hasItems("validated",
                                        "update prohibited")))
                // remarks.
                .andExpect(jsonPath("$.notices").isArray())
                .andExpect(jsonPath("$.notices[0]").exists())
                .andExpect(jsonPath("$.notices[0].title").exists())
                .andExpect(jsonPath("$.notices[0].description").exists())
                .andExpect(jsonPath("$.notices[0].description").isArray())
                .andExpect(
                        jsonPath("$.notices[0].description").value(
                                CoreMatchers.hasItems("description1",
                                        "description2")))
                .andExpect(jsonPath("$.notices[0].links").exists())
                .andExpect(jsonPath("$.notices[0].links").isArray())
                .andExpect(jsonPath("$.notices[0].links[0]").exists())
                .andExpect(jsonPath("$.notices[0].links[0].value").exists())
                .andExpect(jsonPath("$.notices[0].links[0].rel").exists())
                .andExpect(jsonPath("$.notices[0].links[0].href").exists())
                .andExpect(jsonPath("$.notices[0].links[0].type").exists())
                .andExpect(
                        jsonPath("$.notices[0].links[0].type").value(
                                "application/json"))
                // links.
                .andExpect(
                        jsonPath(
                                "$.nameserverSearchResults[0].links[0].hreflang")
                                .value(CoreMatchers.hasItems("en", "zh")))
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].links[0].title")
                                .exists())
                .andExpect(
                        jsonPath("$.nameserverSearchResults[0].objectClassName")
                                .value("nameserver"));
    }

    /**
     * test search exist truncated nameserver.
     * 
     * @throws Exception
     *             throw a exception.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testSearchTruncatedNameserver() throws Exception {
        super.databaseSetupWithBinaryColumns("nameserver-search.xml");
        RestResponseUtil.initErrorMessages();
        String nsHead = "name=";
        String ipHead = "ip=";
        String nsName = nsHead + "ns.truncated*";
        String nsIpV6 = ipHead + "::ffff:ffff:ffff:fffe";
        String ipHighCase = ipHead + "::FFFF:ffff:FFFF:FFFE";
        // searchTruncatedNS(nsName);
        searchTruncatedNS(nsIpV6);
        // searchTruncatedNS(ipHighCase);
    }

    /**
     * search truncated nameservers.
     * 
     * @param strObj
     *            string of object
     * @throws Exception
     *             throw a exception.
     */
    private void searchTruncatedNS(String strObj) throws Exception {
        RdapProperties prop = new RdapProperties();
        long finalSize = 2L;
        ReflectionTestUtils.setField(prop, "maxsizeSearch", finalSize);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", finalSize);
        mockMvc.perform(
                get(URI_NS_SEARCH + strObj).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                // .andExpect(jsonPath("$.resultsTruncated").value(true))
                .andExpect(jsonPath("$.nameserverSearchResults").exists())
                .andExpect(jsonPath("$.nameserverSearchResults").isArray())
                .andExpect(
                        jsonPath("$.nameserverSearchResults",
                                hasSize((int) finalSize)))
                .andExpect(
                        jsonPath("$.nameserverSearchResults",
                                Matchers.hasItem(Matchers.hasKey("handle"))));
    }

    /**
     * test search not existed nameserver 404.
     * 
     * @throws Exception
     *             throw a exception.
     */
    @Test
    @DatabaseSetup(type = DatabaseOperation.REFRESH,
            value = "classpath:org/restfulwhois/rdap/dao/impl/errorMessage.xml")
    public
            void testSearchNonExistNameserver() throws Exception {
        RestResponseUtil.initErrorMessages();
        String nsHead = "name=";
        String ipHead = "ip=";
        String nsName = nsHead + "nonexist*";
        String nsIpV4 = ipHead + "255.255.255.0";
        String nsIpV6 = ipHead + "ffff::ffff";
        searchNonExistNS(nsName);
        searchNonExistNS(nsIpV4);
        searchNonExistNS(nsIpV6);
    }

    /**
     * search no existed nameservers.
     * 
     * @param strObject
     *            string of object.
     * @throws Exception
     *             throw a exception.
     */
    private void searchNonExistNS(String strObject) throws Exception {
        mockMvc.perform(
                get(URI_NS_SEARCH + strObject).accept(
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
     * test search nameserver 422.
     * 
     * @throws Exception
     *             throw a exception.
     */
    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/errorMessage.xml")
    public void testSearchInvalidNameserver() throws Exception {
        RestResponseUtil.initErrorMessages();
        mockMvc.perform(
                get(URI_NS_SEARCH + "name=*").accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(422))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("UNPROCESSABLE ENTITY"))
                .andExpect(
                        jsonPath("$.description").value("UNPROCESSABLE ENTITY"));
    }

    /**
     * test search nameserver 400.
     * 
     * @throws Exception
     *             throw a exception.
     */
    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/errorMessage.xml")
    public void testSearchIllegalNameserver() throws Exception {
        RestResponseUtil.initErrorMessages();
        String nsHead = "name=";
        String nsNameCn = nsHead + "ns.cnnic.cn";
        String ipHead = "ip=";
        String nsIpIllegal = ipHead + "1:?0";
        String nsIpWildcardEnd = ipHead + "1.*";
        String nsIpWildcardHead = ipHead + "*.*";
        String nsNameIllegal = nsHead + "";
        String nsComplex = "&name=&ip=::?name=0";
        String nsOnlyOne1 = ipHead + "::f:f:0.15.0.15" + "?" + nsNameCn;
        String nsOnlyOne2 = nsNameCn + "?" + ipHead + "::f:f:0.15.0.15";
        seachIllegalNS(nsOnlyOne1);
        seachIllegalNS(nsOnlyOne2);
        seachIllegalNS(nsIpIllegal);
        seachIllegalNS(nsNameIllegal);
        seachIllegalNS(nsIpWildcardHead);
        seachIllegalNS(nsIpWildcardEnd);
        seachIllegalNS(nsComplex);
    }

    /**
     * search illegal nameservers.
     * 
     * @param strObj
     *            string of object.
     * @throws Exception
     *             throw a exception.
     */
    private void seachIllegalNS(String strObj) throws Exception {
        mockMvc.perform(
                get(URI_NS_SEARCH + strObj).accept(
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
        super.databaseSetupWithBinaryColumns("nameserver-search.xml");
        String nameserverQ = "name=ns.cnnic1.cn*";
        search403(nameserverQ);
    }

    private void search403(String q) throws Exception {
        mockMvc.perform(
                get(URI_NS_SEARCH + q).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(403))
                .andExpect(jsonPath("$.lang").value("en"));

    }

}
