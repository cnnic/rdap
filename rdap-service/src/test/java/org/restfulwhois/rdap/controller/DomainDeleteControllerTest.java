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

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class DomainDeleteControllerTest extends BaseTest {

    /**
     * domain query URI.
     */
    public static final String URI_DOMAIN_U = "/u/domain/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    final private String rdapJson = "application/rdap+json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-delete.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public
            void test_ok_only_domain() throws Exception {
        mockMvc.perform(
                delete(URI_DOMAIN_U + "h1").contentType(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson));
        super.assertTablesForUpdate("teardown.xml", "RDAP_DOMAIN",
                "RDAP_DOMAIN_STATUS");
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-delete-for-controller.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public
            void test_ok_with_fat_domain_with_all_inner_objects()
                    throws Exception {
        mockMvc.perform(
                delete(URI_DOMAIN_U + "h1").contentType(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson));
        super.assertTablesForUpdate("teardown.xml", "RDAP_DOMAIN",
                "RDAP_DOMAIN_STATUS", "RDAP_EVENT", "REL_EVENT_REGISTRATION",
                "RDAP_LINK", "REL_LINK_OBJECT", "RDAP_LINK_HREFLANG",
                "RDAP_VARIANT", "REL_DOMAIN_VARIANT",
                "REL_ENTITY_REGISTRATION", "REL_DOMAIN_NAMESERVER",
                "REL_PUBLICID_REGISTRATION", "RDAP_NOTICE",
                "REL_NOTICE_REGISTRATION", "RDAP_NOTICE_DESCRIPTION",
                "RDAP_SECUREDNS", "RDAP_DSDATA", "REL_SECUREDNS_DSKEY",
                "RDAP_KEYDATA");

        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_PUBLICID",
                        "select * from RDAP_PUBLICID where PUBLIC_ID=1");
        assertTrue(resultList.size() > 0);
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-delete.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public
            void test_invalid_handle_not_exist() throws Exception {
        String notExistHandle = "not-exist-handle";
        mockMvc.perform(
                delete(URI_DOMAIN_U + notExistHandle).contentType(
                        MediaType.parseMediaType(rdapJson)))
                .andExpect(status().isNotFound())
                .andExpect(
                        content().contentType(
                                "application/rdap+json;charset=UTF-8"))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.subErrorCode").value(4041))
                .andExpect(
                        jsonPath("$.description")
                                .value(CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4041
                                                .getMessage(), notExistHandle))));
    }

}
