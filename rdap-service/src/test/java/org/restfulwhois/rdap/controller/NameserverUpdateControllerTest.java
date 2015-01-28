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
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
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
public class NameserverUpdateControllerTest extends BaseTest {
    private static final String URI_NS_U = "/u/nameserver/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    final private String rdapJson = "application/rdap+json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/nameserver-update-for-controller.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public
            void test_ok() throws Exception {
        String updateLdhName = "update.cn";
        String updateLang = "us";
        String originalHandle = "h1";
        String updatePort43 = "update-port43";
        String updateHandle = "new-handle";
        String updateStatusRenewProbibited = "renew prohibited";
        String updateStatusTransferProbibited = "transfer prohibited";
        String updateStatusDeleteProbibited = "delete prohibited";
        NameserverDto nameserver = new NameserverDto();
        nameserver.setHandle(updateHandle);
        nameserver.setLdhName(updateLdhName);
        nameserver.setUnicodeName(updateLdhName);
        nameserver.setPort43(updatePort43);
        nameserver.setLang(updateLang);
        List<String> expectedStatus = new ArrayList<String>();
        expectedStatus.add(updateStatusRenewProbibited);
        expectedStatus.add(updateStatusTransferProbibited);
        expectedStatus.add(updateStatusDeleteProbibited);
        nameserver.setStatus(expectedStatus);
        Map<String, String> customProperties = new HashMap<String, String>();
        customProperties.put("customKey3", "customValue3");
        nameserver.setCustomProperties(customProperties);
        IpAddressDto ipAddresses = new IpAddressDto();
        List<String> ipList = new ArrayList<String>();
        ipList.add("218.1.1.1");
        ipList.add("2001:12::");
        ipList.add(" ");
        ipAddresses.setIpList(ipList);
        nameserver.setIpAddresses(ipAddresses);
        String content = JsonHelper.serialize(nameserver);
        mockMvc.perform(
                put(URI_NS_U + originalHandle).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson));
        assertNameserver(updateLdhName, updateLang, originalHandle,
                updatePort43);
        assertNsIp();
        assertStatus();
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_not_exist() throws Exception {
        String notExistHandle = "not-exist-handle";
        mockMvc.perform(
                put(URI_NS_U + notExistHandle).contentType(
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

    private void assertNsIp() throws Exception {
        List<Map<?, ?>> actualIpList =
                getTableDataForSql("RDAP_NAMESERVER_IP",
                        "select * from RDAP_NAMESERVER_IP where NAMESERVER_ID=1");
        assertEquals(2, actualIpList.size());
    }

    private void assertStatus() throws Exception {
        List<Map<?, ?>> resultList1 =
                getTableDataForSql("RDAP_NAMESERVER_STATUS",
                        "select * from RDAP_NAMESERVER_STATUS where NAMESERVER_ID=1");
        assertEquals(3, resultList1.size());
    }

    private void assertNameserver(String updateLdhName, String updateLang,
            String originalHandle, String updatePort43) throws Exception {
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_NAMESERVER",
                        "select * from RDAP_NAMESERVER where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        Map<?, ?> actualNameserver = resultList.get(0);
        assertEquals(originalHandle, actualNameserver.get("HANDLE"));
        assertEquals(updateLdhName, actualNameserver.get("LDH_NAME"));
        assertEquals(updateLdhName, actualNameserver.get("UNICODE_NAME"));
        assertEquals(updateLang, actualNameserver.get("LANG"));
        assertEquals(updatePort43, actualNameserver.get("PORT43"));
        assertEquals("{\"customKey3\":\"customValue3\"}",
                actualNameserver.get("CUSTOM_PROPERTIES"));
    }

}
