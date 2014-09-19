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
import org.restfulwhois.rdap.common.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController autnum redirect.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerAutnumRedirectTest extends BaseTest {

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
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/autnum-redirect.xml")
    public void testQueryExist() throws Exception {
        RestResponseUtil.initErrorMessages();
        String autnumStr = "1000";
        commonExist(autnumStr);
    }

    /**
     * check exist .
     * 
     * @param autnumStr
     *            autnumStr.
     * @throws Exception
     *             Exception.
     */
    private void commonExist(String autnumStr) throws Exception {
        mockMvc.perform(
                get(RdapControllerAutnumTest.URI_AS + autnumStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.country").value("zh"))
                .andExpect(jsonPath("$.lang").value("cn"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.objectClassName").value("autnum"));
    }

    /**
     * test query non exist.
     * 
     * @throws Exception
     *             Exception.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/autnum-redirect.xml")
    public void testQueryNonExist() throws Exception {
        RestResponseUtil.initErrorMessages();
        String autnumStr = "100000";
        commonNonExist(autnumStr);
    }

    /**
     * check non exist.
     * 
     * @param nonExistAutnumStr
     *            nonExistAutnumStr.
     * @throws Exception
     *             Exception.
     */
    private void commonNonExist(String nonExistAutnumStr) throws Exception {
        mockMvc.perform(
                get(RdapControllerAutnumTest.URI_AS + nonExistAutnumStr).accept(
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
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/autnum-redirect.xml")
    public void testQueryRedirect() throws Exception {
        RestResponseUtil.initErrorMessages();
        String autnumStr = "1";
        commonRedirect(autnumStr);
    }

    /**
     * check non redirect.
     * 
     * @param redirectAutnum
     *            redirectAutnum.
     * @throws Exception
     *             Exception.
     */
    private void commonRedirect(String redirectAutnum) throws Exception {
        mockMvc.perform(
                get(RdapControllerAutnumTest.URI_AS + redirectAutnum).accept(
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
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/autnum-redirect.xml")
    public void testQueryInvalid() throws Exception {
        RestResponseUtil.initErrorMessages();
        String autnumStr = "invalidNumber";
        commonInvalid(autnumStr);
    }

    /**
     * check invalid.
     * 
     * @param autnumStr
     *            autnumStr.
     * @throws Exception
     *             Exception.
     */
    private void commonInvalid(String autnumStr) throws Exception {
        mockMvc.perform(
                get(RdapControllerAutnumTest.URI_AS + autnumStr).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));
    }

}
