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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.JsonHelper;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
import org.restfulwhois.rdap.dao.impl.LinkUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.RemarkUpdateDaoTest;
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
public class NameserverCreateControllerTest extends BaseTest {
    private static final String URI_NS_U = "/u/nameserver";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    final private String rdapJson = "application/rdap+json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_only_ns() throws Exception {
        NameserverDto ns = generateNsDto();
        String content = JsonHelper.serialize(ns);
        mockMvc.perform(
                post(URI_NS_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ns.getHandle()));
        super.assertTablesForUpdate("nameserver-create.xml", "RDAP_NAMESERVER");
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_thin_ns() throws Exception {
        NameserverDto ns = new NameserverDto();
        ns.setHandle("h1");
        ns.setLdhName("cnnic.cn");
        String content = JsonHelper.serialize(ns);
        mockMvc.perform(
                post(URI_NS_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ns.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_fat_domain_with_many_inner_objects()
            throws Exception {
        NameserverDto ns = generateNsDto();
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("update prohibited");
        ns.setStatus(status);
        IpAddressDto ipAddresses = new IpAddressDto();
        List<String> ipList = new ArrayList<String>();
        ipList.add("218.1.1.1");
        ipList.add("2001:12::");
        ipList.add(" ");
        ipAddresses.setIpList(ipList);
        ns.setIpAddresses(ipAddresses);
        createAndSetEvents(ns);
        ns.setRemarks(RemarkUpdateDaoTest.createRemarkList());
        ns.setLinks(LinkUpdateDaoTest.createLinkList());
        String content = JsonHelper.serialize(ns);
        mockMvc.perform(
                post(URI_NS_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ns.getHandle()));
        List<Map<?, ?>> actualNsIds =
                getTableDataForSql("RDAP_NAMESERVER",
                        "SELECT NAMESERVER_ID from RDAP_NAMESERVER WHERE HANDLE='h1'");
        assertNotNull(actualNsIds);
        assertTrue(actualNsIds.size() > 0);
        Integer nsId = (Integer) actualNsIds.get(0).get("NAMESERVER_ID");
        assertNotNull(nsId);
        List<Map<?, ?>> actualStatusList =
                getTableDataForSql("RDAP_NAMESERVER_STATUS",
                        "select * from RDAP_NAMESERVER_STATUS where NAMESERVER_ID="
                                + nsId);
        assertEquals(2, actualStatusList.size());
        List<Map<?, ?>> actualIpList =
                getTableDataForSql("RDAP_NAMESERVER_IP",
                        "select * from RDAP_NAMESERVER_IP where NAMESERVER_ID="
                                + nsId);
        assertEquals(2, actualIpList.size());
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_already_exist() throws Exception {
        NameserverDto ns = generateNsDto();
        String content = JsonHelper.serialize(ns);
        mockMvc.perform(
                post(URI_NS_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ns.getHandle()));
        mockMvc.perform(
                post(URI_NS_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.subErrorCode").value(4091))
                .andExpect(
                        jsonPath("$.description")
                                .value(CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4091
                                                .getMessage(), ns.getHandle()))));
    }

    private void createAndSetEvents(NameserverDto ns) {
        List<EventDto> events = new ArrayList<EventDto>();
        EventDto event = new EventDto();
        events.add(event);
        event.setEventAction("registration");
        event.setEventActor("zhanyq");
        event.setEventDate("2015-01-15T17:15:12Z");
        ns.setEvents(events);
    }

    private NameserverDto generateNsDto() {
        NameserverDto nameserver = new NameserverDto();
        nameserver.setHandle("h1");
        nameserver.setLdhName("cnnic.cn");
        nameserver.setUnicodeName("cnnic.cn");
        nameserver.setPort43("port43");
        nameserver.setLang("zh");
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("update prohibited");
        nameserver.setStatus(status);
        Map<String, String> customProperties = new LinkedHashMap<String, String>();
        customProperties.put("customKey1", "customValue1");
        customProperties.put("customKey2", "customValue2");
        nameserver.setCustomProperties(customProperties);
        return nameserver;
    }
}
