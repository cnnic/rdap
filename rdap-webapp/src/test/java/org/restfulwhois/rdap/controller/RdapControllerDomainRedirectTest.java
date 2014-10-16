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
import org.restfulwhois.rdap.common.RdapProperties;
import org.restfulwhois.rdap.common.util.RestResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RdapController domain redirect.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RdapControllerDomainRedirectTest extends BaseTest {

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
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-redirect.xml")
    public void testQueryExistDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        // set both 'in' and 'not in' tld.
        RdapProperties.clearTldsInMemory();
        String domainName = "cnnic.cn";
        RdapProperties prop = new RdapProperties();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "edu.cn");
        commonExist(domainName);
        // only set 'in' tld.
        RdapProperties.clearTldsInMemory();
        domainName = "cnnic.cn";
        prop = new RdapProperties();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        commonExist(domainName);
    }

    /**
     * check exist domain.
     * 
     * @param domainName
     *            domainName.
     * @throws Exception
     *             Exception.
     */
    private void commonExist(String domainName) throws Exception {
        mockMvc.perform(
                get(RdapControllerDomainTest.URI_DOMAIN_Q + domainName).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value("1"))
                .andExpect(jsonPath("$.ldhName").value(domainName))
                .andExpect(jsonPath("$.objectClassName").value("domain"));
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-redirect.xml")
    public void testQueryNonExistDomain() throws Exception {
        RestResponseUtil.initErrorMessages();
        // set both 'in' and 'not in' tld.
        RdapProperties.clearTldsInMemory();
        String domainName = "nonexist.cn";
        RdapProperties prop = new RdapProperties();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "edu.cn");
        commconNonExist(domainName);
        // only set 'in' tld.
        RdapProperties.clearTldsInMemory();
        domainName = "nonexist.cn";
        prop = new RdapProperties();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        commconNonExist(domainName);
    }

    /**
     * check non exist domain.
     * 
     * @param domainName
     *            domainName.
     * @throws Exception
     *             Exception.
     */
    private void commconNonExist(String domainName) throws Exception {
        mockMvc.perform(
                get(RdapControllerDomainTest.URI_DOMAIN_Q + domainName).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("NOT FOUND"))
                .andExpect(jsonPath("$.description").value("NOT FOUND"));
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-redirect.xml")
    public void testQueryRedirect() throws Exception {
        RestResponseUtil.initErrorMessages();
        // set both 'in' and 'not in' tld.
        String domainName = "cnnic.cn";
        RdapProperties prop = new RdapProperties();
        RdapProperties.clearTldsInMemory();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "cn");
        commconRedirect(domainName);
        // set both 'in' and 'not in' tld, multi tlds.
        domainName = "nonexist.cn";
        prop = new RdapProperties();
        RdapProperties.clearTldsInMemory();
        ReflectionTestUtils.setField(prop, "inTlds", "com;cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "cn");
        commconRedirect(domainName);
        domainName = "nonexist.cn";
        prop = new RdapProperties();
        RdapProperties.clearTldsInMemory();
        ReflectionTestUtils.setField(prop, "inTlds", "com;cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "org;cn");
        commconRedirect(domainName);
        // only set 'not in' tld.
        domainName = "nonexist.cn";
        prop = new RdapProperties();
        RdapProperties.clearTldsInMemory();
        ReflectionTestUtils.setField(prop, "notInTlds", "cn");
        commconRedirect(domainName);
        // contain 'cn',but not contain 'edu.cn'
        domainName = "cnnic.edu.cn";
        prop = new RdapProperties();
        RdapProperties.clearTldsInMemory();
        ReflectionTestUtils.setField(prop, "inTlds", "com;cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "edu.cn");
        commconRedirect(domainName);

    }

    /**
     * check non exist domain.
     * 
     * @param domainName
     *            domainName.
     * @throws Exception
     *             Exception.
     */
    private void commconRedirect(String domainName) throws Exception {
        mockMvc.perform(
                get(RdapControllerDomainTest.URI_DOMAIN_Q + domainName).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isMovedPermanently())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(301))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("MOVED PERMANENTLY"))
                .andExpect(jsonPath("$.description").value("MOVED PERMANENTLY"));
    }

    /**
     * test query exist domain.
     * 
     * @throws Exception
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-redirect.xml")
    public void testQueryInvalid() throws Exception {
        RestResponseUtil.initErrorMessages();
        // set both 'in' and 'not in' tld.
        String domainName = "***invalid.cn";
        RdapProperties prop = new RdapProperties();
        RdapProperties.clearTldsInMemory();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "edu.cn");
        commonInvalid(domainName);
        // only set 'in' tld.
        domainName = "***invalid.cn";
        prop = new RdapProperties();
        RdapProperties.clearTldsInMemory();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        commonInvalid(domainName);
    }

    /**
     * check invalid domain.
     * 
     * @param domainName
     *            domain name.
     * @throws Exception
     *             Exception.
     */
    private void commonInvalid(String domainName) throws Exception {
        mockMvc.perform(
                get(RdapControllerDomainTest.URI_DOMAIN_Q + domainName).accept(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.lang").value("en"))
                .andExpect(jsonPath("$.title").value("BAD REQUEST"))
                .andExpect(jsonPath("$.description").value("BAD REQUEST"));

    }
}
