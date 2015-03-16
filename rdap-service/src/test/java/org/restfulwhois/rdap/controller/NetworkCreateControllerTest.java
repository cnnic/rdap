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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.JsonHelper;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.Network;
import org.restfulwhois.rdap.common.util.BeanUtil;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.common.util.JsonUtil;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
import org.restfulwhois.rdap.dao.impl.EntityUpdateDaoTest;
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
 * @author zhanyq
 * 
 */
@SuppressWarnings("rawtypes")
public class NetworkCreateControllerTest extends BaseTest {

    /**
     * autnum query URI.
     */
    public static final String URI_IP_U = "/u/ip";

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
    public void test_ok_with_only_ip() throws Exception {
        IpDto ipDto = generateIpDto();
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ipDto.getHandle()));
        Network network = new Network();
        BeanUtil.copyProperties(ipDto, network, "entities", "events",
                "remarks", "links");
        network.setIpVersion(IpVersion.getIpVersion(ipDto.getIpVersion()));
        network.setCustomPropertiesJsonVal(JsonUtil.serializeMap(ipDto
                .getCustomProperties()));
        int ipId = assertIp(network);
        assertStatus(network, ipId);
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_thin_ip() throws Exception {
        IpDto ipDto = new IpDto();
        ipDto.setHandle("h1");
        ipDto.setEndAddress("218.241.111.12");
        ipDto.setStartAddress("218.241.111.11");
        ipDto.setIpVersion("v4");
        ipDto.setCidr("218.241.111.0/8");

        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ipDto.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_fat_entity_with_all_inner_objects()
            throws Exception {
        IpDto ipDto = generateIpDto();
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("update prohibited");
        ipDto.setStatus(status);
        createAndSetEvents(ipDto);
        ipDto.setRemarks(RemarkUpdateDaoTest.createRemarkList());
        ipDto.setLinks(LinkUpdateDaoTest.createLinkList());
        ipDto.setEntities(EntityUpdateDaoTest.createEntityHandleList());
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ipDto.getHandle()));
        // RemarkUpdateDaoTest.assertCreate();/link duplicated.
        // LinkUpdateDaoTest.assertCreate();
        List<Map<?, ?>> actualAutnumIds =
                getTableDataForSql("RDAP_IP",
                        "SELECT IP_ID from RDAP_IP WHERE HANDLE='h1'");
        assertNotNull(actualAutnumIds);
        assertTrue(actualAutnumIds.size() > 0);
        Integer ipId = (Integer) actualAutnumIds.get(0).get("IP_ID");
        List<Map<?, ?>> actualRelAutnumList =
                getTableDataForSql("RDAP_IP_STATUS",
                        "select * from RDAP_IP_STATUS where IP_ID=" + ipId);
        assertNotNull(actualRelAutnumList);
        assertEquals(2, actualRelAutnumList.size());

    }

    private void createAndSetEvents(IpDto ipDto) {
        List<EventDto> events = new ArrayList<EventDto>();
        EventDto event = new EventDto();
        events.add(event);
        event.setEventAction("registration");
        event.setEventActor("zhanyq");
        event.setEventDate("2015-01-15T17:15:12Z");
        ipDto.setEvents(events);
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_name_maxlength() throws Exception {
        IpDto ipDto = generateIpDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255);
        ipDto.setName(stringMaxLength);
        assertTrue(ipDto.getName().length() == UpdateValidateUtil.MAX_LENGTH_255);
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ipDto.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_handle_maxlength() throws Exception {
        IpDto ipDto = generateIpDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE);
        ipDto.setHandle(stringMaxLength);
        assertTrue(ipDto.getHandle().length() == UpdateValidateUtil.MAX_LENGTH_HANDLE);
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ipDto.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_empty_cidr() throws Exception {
        IpDto ipDto = new IpDto();
        ipDto.setHandle("h1");
        ipDto.setEndAddress("218.241.111.12");
        ipDto.setStartAddress("218.241.111.11");
        ipDto.setIpVersion("v4");

        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4002));
    }
    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_startAddress_empty() throws Exception {
        IpDto ipDto = generateIpDto();
        ipDto.setStartAddress("");      
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4002))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4002
                                                .getMessage(), "startAddress"))));
    }
    
    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_endAddress_empty() throws Exception {
        IpDto ipDto = generateIpDto();
        ipDto.setEndAddress("");      
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4002))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4002
                                                .getMessage(), "endAddress"))));
    }
    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_empty() throws Exception {
        IpDto ipDto = generateIpDto();
        ipDto.setHandle(null);
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4002))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4002
                                                .getMessage(), "handle"))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_exceed_maxlength() throws Exception {
        IpDto ipDto = generateIpDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE + 1);
        ipDto.setHandle(stringExceedOneMoreChar);
        assertTrue(ipDto.getHandle().length() > UpdateValidateUtil.MAX_LENGTH_HANDLE);
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4003))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4003
                                                .getMessage(), "handle",
                                        UpdateValidateUtil.MAX_LENGTH_HANDLE
                                                + ""))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_JSON() throws Exception {
        String invalidContent = "{";
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(invalidContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/rdap+json"))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4001))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers
                                        .hasItems(ServiceErrorCode.ERROR_4001
                                                .getMessage())));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_already_exist() throws Exception {
        IpDto ipDto = generateIpDto();
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content)).andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(ipDto.getHandle()));
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.subErrorCode").value(4091))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4091
                                                .getMessage(), ipDto
                                                .getHandle()))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_IpVersion_empty() throws Exception {
        IpDto ipDto = generateIpDto();
        ipDto.setIpVersion(null);
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4002))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4002
                                                .getMessage(), "ipVersion"))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_IpVersion() throws Exception {
        IpDto ipDto = generateIpDto();
        ipDto.setIpVersion("invalidIpVersion");
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4008))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4008
                                                .getMessage(), "ipVersion"))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_startAddress_empty() throws Exception {
        IpDto ipDto = generateIpDto();
        ipDto.setStartAddress(null);
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4002))
                .andExpect(
                        jsonPath("$.description")
                                .value(CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4002
                                                .getMessage(), "startAddress"))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_startAddress() throws Exception {
        IpDto ipDto = generateIpDto();
        ipDto.setStartAddress("::123.dd.ff.gg/23");
        String content = JsonHelper.serialize(ipDto);
        mockMvc.perform(
                post(URI_IP_U).contentType(MediaType.parseMediaType(rdapJson))
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4008))
                .andExpect(
                        jsonPath("$.description")
                                .value(CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4008
                                                .getMessage(), "startAddress"))));
    }

    private IpDto generateIpDto() {
        IpDto network = new IpDto();
        network.setHandle("h1");
        network.setCidr("FFF1:1::");
        network.setEndAddress("218.241.111.12");
        network.setStartAddress("218.241.111.11");
        network.setIpVersion(IpVersion.V4.getName());
        network.setParentHandle("ip-200-1");
        network.setName("cnnic-1");
        network.setPort43("cnnic.cn");
        network.setCountry("cn");
        network.setLang("zh");
        network.setType("alocated");
        Map<String, String> customProperties = new HashMap<String, String>();
        customProperties.put("customKey1", "customValue1");
        customProperties.put("customKey2", "customValue2");
        network.setCustomProperties(customProperties);
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("active");
        network.setStatus(status);
        return network;
    }

    private int assertIp(Network network) throws Exception {
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_IP",
                        "select * from RDAP_IP where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        Map<?, ?> existIp = resultList.get(0);
        Integer ipId = (Integer) existIp.get("IP_ID");
        assertEquals(network.getHandle(), existIp.get("HANDLE"));
        assertEquals(network.getCidr(), existIp.get("CIDR"));
        assertEquals(network.getLang(), existIp.get("LANG"));
        assertEquals(network.getPort43(), existIp.get("PORT43"));
        assertEquals(network.getType(), existIp.get("TYPE"));
        assertEquals(network.getName(), existIp.get("NAME"));
        assertEquals(network.getParentHandle(), existIp.get("PARENT_HANDLE"));
        assertEquals(network.getIpVersion().getName(), existIp.get("VERSION"));
        assertEquals(network.getCountry(), existIp.get("COUNTRY"));
        assertEquals(
                network.getStartAddress(),
                IpUtil.toString((byte[]) existIp.get("STARTADDRESS"),
                        network.getIpVersion()));
        assertEquals(
                network.getEndAddress(),
                IpUtil.toString((byte[]) existIp.get("ENDADDRESS"),
                        network.getIpVersion()));
        assertEquals(network.getCustomPropertiesJsonVal(),
                existIp.get("CUSTOM_PROPERTIES"));
        return ipId;
    }

    private void assertStatus(Network network, int ipId) throws Exception {
        List<Map<?, ?>> resultList1 =
                getTableDataForSql("RDAP_IP_STATUS",
                        "select * from RDAP_IP_STATUS" + " where IP_ID = "
                                + ipId);
        assertEquals(network.getStatus().size(), resultList1.size());
    }

    private String createStringWithLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("1");
        }
        return sb.toString();
    }
}
