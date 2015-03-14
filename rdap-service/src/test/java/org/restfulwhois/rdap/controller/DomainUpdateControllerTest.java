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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.JsonHelper;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.model.Domain.DomainType;
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
public class DomainUpdateControllerTest extends BaseTest {

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
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-update.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public
            void test_ok_only_domain() throws Exception {
        String updateLdhName = "update.cn";
        String updateLang = "us";
        String originalHandle = "h1";
        String updatePort43 = "update-port43";
        String updateHandle = "new-handle";
        String updateStatusRenewProbibited = "renew prohibited";
        String updateStatusTransferProbibited = "transfer prohibited";
        String updateStatusDeleteProbibited = "delete prohibited";
        DomainDto domain = new DomainDto();
        domain.setHandle(updateHandle);
        domain.setLdhName(updateLdhName);
        domain.setUnicodeName(updateLdhName);
        domain.setPort43(updatePort43);
        domain.setLang(updateLang);
        domain.setType(DomainType.ARPA.getName());
        List<String> expectedStatus = new ArrayList<String>();
        expectedStatus.add(updateStatusRenewProbibited);
        expectedStatus.add(updateStatusTransferProbibited);
        expectedStatus.add(updateStatusDeleteProbibited);
        domain.setStatus(expectedStatus);
        Map<String, String> customProperties = new HashMap<String, String>();
        customProperties.put("customKey3", "customValue3");
        domain.setCustomProperties(customProperties);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                put(URI_DOMAIN_U + originalHandle).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson));
        assertDomain(updateLdhName, updateLang, originalHandle, updatePort43);
        assertStatus();
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_not_exist() throws Exception {
        String notExistHandle = "not-exist-handle";
        mockMvc.perform(
                put(URI_DOMAIN_U + notExistHandle).contentType(
                        MediaType.parseMediaType(rdapJson)).content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(404))
                .andExpect(jsonPath("$.subErrorCode").value(4041))
                .andExpect(
                        jsonPath("$.description")
                                .value(CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4041
                                                .getMessage(), notExistHandle))));
    }

    private void assertStatus() throws Exception {
        List<Map<?, ?>> resultList1 =
                getTableDataForSql("RDAP_DOMAIN_STATUS",
                        "select * from RDAP_DOMAIN_STATUS where DOMAIN_ID=1");
        assertEquals(3, resultList1.size());
    }

    private void assertDomain(String updateLdhName, String updateLang,
            String originalHandle, String updatePort43) throws Exception {
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_DOMAIN",
                        "select * from RDAP_DOMAIN where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        Map<?, ?> actualDomain = resultList.get(0);
        assertEquals(originalHandle, actualDomain.get("HANDLE"));
        assertEquals(DomainType.DNR.getName(), actualDomain.get("TYPE"));
        assertEquals(updateLdhName, actualDomain.get("LDH_NAME"));
        assertEquals(updateLdhName, actualDomain.get("UNICODE_NAME"));
        assertEquals(updateLang, actualDomain.get("LANG"));
        assertEquals(updatePort43, actualDomain.get("PORT43"));
        assertEquals("{\"customKey3\":\"customValue3\"}",
                actualDomain.get("CUSTOM_PROPERTIES"));
    }

}
