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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.support.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController network redirect.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerNetworkRedirectTest extends BaseTest {

    /**
     * ip query URI.
     */
    private static final String URI_IP_Q = "/ip/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;
    /**
     * output json.
     */
    final private String rdapJson = "application/rdap+json";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    /**
     * test query exist.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryExist() throws Exception {
        RestResponse.initErrorMessages();
        // v4
        String networkStr = "218.241.0.0/16";
        commonExist(networkStr);
        // v6
        networkStr = "2014:2014:2014::/80";
        commonExist(networkStr);
    }

    /**
     * check exist .
     * 
     * @param networkStr
     *            autnumStr.
     * @throws Exception
     *             Exception.
     */
    private void commonExist(String networkStr) throws Exception {
        mockMvc.perform(
                get(URI_IP_Q + networkStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.port43").value("port43"))
                .andExpect(jsonPath("$.objectClassName").value("ip network"));
    }

    /**
     * test query non exist.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryNonExist() throws Exception {
        RestResponse.initErrorMessages();
        // v4
        String networkStr = "219.241.0.0";
        commonNonExist(networkStr);
        // v6
        networkStr = "2015:2014:2014::";
        commonNonExist(networkStr);
    }

    /**
     * check non exist.
     * 
     * @param networkStr
     *            networkStr.
     * @throws Exception
     *             Exception.
     */
    private void commonNonExist(String networkStr) throws Exception {
        mockMvc.perform(
                get(URI_IP_Q + networkStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404));
    }

    /**
     * test query redirect.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryRedirect() throws Exception {
        RestResponse.initErrorMessages();
        // v4
        String networkStr = "1.0.0.0";
        commonRedirect(networkStr);
        // v6
        networkStr = "0:0:0:0:2001:6a8::/96";
        commonRedirect(networkStr);
    }

    /**
     * check redirect.
     * 
     * @param networkStr
     *            networkStr.
     * @throws Exception
     *             Exception.
     */
    private void commonRedirect(String networkStr) throws Exception {
        mockMvc.perform(
                get(URI_IP_Q + networkStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isMovedPermanently())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(301));
    }

    /**
     * test query invalid.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryInvalid() throws Exception {
        RestResponse.initErrorMessages();
        String invalidIpStr = "invalidNumber";
        commonInvalid(invalidIpStr);
    }

    /**
     * check invalid.
     * 
     * @param networkStr
     *            networkStr.
     * @throws Exception
     *             Exception.
     */
    private void commonInvalid(String networkStr) throws Exception {
        mockMvc.perform(
                get(URI_IP_Q + networkStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));
    }

    @Override
    public void before() throws Exception {
        super.databaseSetupWithBinaryColumns("network-redirect.xml");
    }

}
