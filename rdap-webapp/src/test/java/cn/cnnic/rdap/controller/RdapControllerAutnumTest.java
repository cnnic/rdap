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

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * Test for RdapController
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerAutnumTest extends BaseTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

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
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:cn/cnnic/rdap/dao/impl/autnum.xml")
    public void testQueryExistAutnumWithRdapAndJson() throws Exception {
        String autnumStr = "1";
        mockMvc.perform(
                get("/.well-known/rdap/autnum/" + autnumStr).accept(
                        MediaType.parseMediaType("application/rdap+json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/rdap+json"))
                .andExpect(jsonPath("$.country").value("zh"))
                .andExpect(jsonPath("$.lang").value("cn"))
                .andExpect(jsonPath("$.name").value("name1"));
    }

    /**
     * test query exist autnum,with json type
     * 
     * @throws Exception
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "classpath:cn/cnnic/rdap/dao/impl/autnum.xml")
    public void testQueryExistAutnum() throws Exception {
        String autnumStr = "1";
        mockMvc.perform(
                get("/.well-known/rdap/autnum/" + autnumStr).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.country").value("zh"))
                .andExpect(jsonPath("$.lang").value("cn"))
                .andExpect(jsonPath("$.name").value("name1"));
    }

    /**
     * test query non exist autnum
     * 
     * @throws Exception
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    public void testQueryNonExistAutnum() throws Exception {
        String nonExistAutnumStr = "1000";
        mockMvc.perform(
                get("/.well-known/rdap/autnum/" + nonExistAutnumStr).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(404));
    }

    /**
     * test query autnum with invalid query parameter
     * 
     * @throws Exception
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    public void testQueryAutnumWithInvalidQ() throws Exception {
        String invalidAutnumStr = "invalidQ";
        mockMvc.perform(
                get("/.well-known/rdap/autnum/" + invalidAutnumStr).accept(
                        MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.errorCode").value(400));
    }

}
