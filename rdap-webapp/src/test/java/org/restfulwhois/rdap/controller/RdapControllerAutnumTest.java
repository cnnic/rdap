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
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerAutnumTest extends BaseTest {

    /**
     * as query URI.
     */
    public static final String URI_AS = "/autnum/";

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
     * test query exist autnum,with rdap+json type
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(
            value = "classpath:org/restfulwhois/rdap/dao/impl/autnum.xml")
    public void testQueryExistAutnumWithRdapAndJson() throws Exception {
        RestResponseUtil.initErrorMessages();
        String autnumStr = "1";
        mockMvc.perform(
                get(URI_AS + autnumStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.country").value("zh"))
                .andExpect(jsonPath("$.lang").value("cn"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.objectClassName").value("autnum"));
    }

    /**
     * test query exist autnum,with json type
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(
            value = "classpath:org/restfulwhois/rdap/dao/impl/autnum.xml")
    public void testQueryExistAutnum() throws Exception {
        RestResponseUtil.initErrorMessages();
        String autnumStr = "1";
        mockMvc.perform(
                get(URI_AS + autnumStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.country").value("zh"))
                .andExpect(jsonPath("$.lang").value("cn"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.objectClassName").value("autnum"));
    }

    /**
     * test query non exist autnum
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryNonExistAutnum() throws Exception {
        RestResponseUtil.initErrorMessages();
        String nonExistAutnumStr = "1000";
        mockMvc.perform(
                get(URI_AS + nonExistAutnumStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404));
    }

    /**
     * test query autnum with invalid query parameter
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryAutnumWithInvalidQ() throws Exception {
        RestResponseUtil.initErrorMessages();
        String invalidAutnumStr = "invalidQ";
        mockMvc.perform(
                get(URI_AS + invalidAutnumStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400));
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(value = {
            "classpath:org/restfulwhois/rdap/dao/impl/autnum.xml",
            "classpath:org/restfulwhois/rdap/dao/impl/acl.xml" })
    public void testQuery403() throws Exception {
        RestResponseUtil.initErrorMessages();
        String autnumStr = "1";
        query403(autnumStr);
    }

    private void query403(String q) throws Exception {
        mockMvc.perform(
                get(URI_AS + q).accept(MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(403))
                .andExpect(jsonPath("$.lang").value("en"));

    }

}
