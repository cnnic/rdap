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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.JsonHelper;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
import org.restfulwhois.rdap.dao.impl.EntityAddressUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.EntityTelUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.EntityUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.LinkUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.PublicIdUpdateDaoTest;
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
public class EntityCreateControllerTest extends BaseTest {

    /**
     * entity query URI.
     */
    public static final String URI_ENTITY_U = "/u/entity";

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
    public void test_ok_with_only_entity() throws Exception {
        EntityDto entity = generateEntityDto();
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
        super.assertTablesForUpdate("entity-create.xml", "RDAP_ENTITY");
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_thin_entity() throws Exception {
        EntityDto entity = new EntityDto();
        entity.setHandle("h1");
        entity.setFn("00miss");
        entity.setEmail("100_1@cnnic.cn");
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_not_empty_custom_properties() throws Exception {
        EntityDto entity = generateEntityDto();
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_ENTITY",
                        "select CUSTOM_PROPERTIES from RDAP_ENTITY where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        assertEquals(
                "{\"customKey1\":\"customValue1\",\"customKey2\":\"customValue2\"}",
                resultList.get(0).get("CUSTOM_PROPERTIES"));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_empty_custom_properties() throws Exception {
        EntityDto entity = new EntityDto();
        entity.setHandle("h1");
        entity.setFn("00miss");
        entity.setEmail("100_1@cnnic.cn");
        entity.setCustomProperties(new HashMap<String, String>());
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_ENTITY",
                        "select CUSTOM_PROPERTIES from RDAP_ENTITY where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        assertNull(resultList.get(0).get("CUSTOM_PROPERTIES"));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_null_custom_properties() throws Exception {
    	EntityDto entity = new EntityDto();
        entity.setHandle("h1");
        entity.setFn("00miss");
        entity.setEmail("100_1@cnnic.cn");
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_ENTITY",
                        "select CUSTOM_PROPERTIES from RDAP_ENTITY where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        assertNull(resultList.get(0).get("CUSTOM_PROPERTIES"));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_ignore_unrecognized_properties() throws Exception {
        String content =
                "{\"handle\":\"1\",\"fn\":\"00miss\",\"org\":\"dnr\""
                        + ",\"\":1,\"unknownP\":\"x\"}";
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value("1"));
    } 
    
    @Test
    public void test_invalid_propertyType_should_be_int_but_is_string()
            throws Exception {
        String content =
                "{\"handle\":\"1\",\"fn\":\"00miss\",\"org\":\"dnr\""
                        + ",\"addresses\":[{\"pref\":\"i-am-not-int-value\"}]}";
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
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
    public
            void
            test_ok_propertyType_should_be_int_but_is_int_with_dot_will_truncated_to_int()
                    throws Exception {
    	String content =
                "{\"handle\":\"1\",\"fn\":\"00miss\",\"org\":\"dnr\""
                        + ",\"addresses\":[{\"pref\":1.8}]}";
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value("1"));
    }
    @Test
    public void
            test_invalid_propertyType_int_but_exceed_max_value_as_minus_int()
                    throws Exception {
    	String content =
                "{\"handle\":\"1\",\"fn\":\"00miss\",\"org\":\"dnr\""
                     + ",\"addresses\":[{\"pref\":"
                    + (UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN + 1) + "}]}";
                       
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4010));
    }
    
    @Test
    public void test_invalid_propertyType_int_but_exceed_max_value()
            throws Exception {
    	String content =
                "{\"handle\":\"1\",\"fn\":\"00miss\",\"org\":\"dnr\""
                        + ",\"addresses\":[{\"pref\":4294967296}]}";
                        
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4001));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_fat_entity_with_all_inner_objects()
            throws Exception {
        EntityDto entity = generateEntityDto();
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("update prohibited");
        entity.setStatus(status);
        createAndSetEvents(entity);
        entity.setRemarks(RemarkUpdateDaoTest.createRemarkList());
        entity.setLinks(LinkUpdateDaoTest.createLinkList());
        entity.setPublicIds(PublicIdUpdateDaoTest.createPublicIdList());
        entity.setAddresses(EntityAddressUpdateDaoTest.
        		createEntityAddressList());
        entity.setTelephones(EntityTelUpdateDaoTest.createEntityTelList());
        entity.setEntities(EntityUpdateDaoTest.createEntityHandleList());
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
        // RemarkUpdateDaoTest.assertCreate();/link duplicated.
        // LinkUpdateDaoTest.assertCreate();
        super.assertTablesForUpdate("publicId-create-for-entity.xml", 
        		"RDAP_PUBLICID", "REL_PUBLICID_REGISTRATION");
        super.assertTablesForUpdate("entityAddress-create.xml", 
        		"RDAP_VCARD_ADR");
        super.assertTablesForUpdate("entityTel-create.xml", "RDAP_VCARD_TEL");
        List<Map<?, ?>> actualEntityIds =
                getTableDataForSql("RDAP_ENTITY",
                        "SELECT ENTITY_ID from RDAP_ENTITY WHERE HANDLE='h1'");
        assertNotNull(actualEntityIds);
        assertTrue(actualEntityIds.size() > 0);
        Integer entityId = (Integer) actualEntityIds.get(0).get("ENTITY_ID");
        List<Map<?, ?>> actualRelEntityList =
                getTableDataForSql("REL_ENTITY_REGISTRATION",
                        "select * from REL_ENTITY_REGISTRATION where REL_ID="
                                + entityId + " and REL_OBJECT_TYPE ='entity' ");
        assertNotNull(actualRelEntityList);
        assertEquals(2,actualRelEntityList.size());
        
    }

    private void createAndSetEvents(EntityDto domain) {
        List<EventDto> events = new ArrayList<EventDto>();
        EventDto event = new EventDto();
        events.add(event);
        event.setEventAction("registration");
        event.setEventActor("zhanyq");
        event.setEventDate("2015-01-15T17:15:12Z");
        domain.setEvents(events);
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_fn_maxlength() throws Exception {
        EntityDto entity = generateEntityDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_255);        
        entity.setFn(stringMaxLength);
        assertTrue(entity.getFn().length() == UpdateValidateUtil.MAX_LENGTH_255);
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
    }

    
    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_handle_maxlength() throws Exception {
        EntityDto entity = generateEntityDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE);
        entity.setHandle(stringMaxLength);
        assertTrue(entity.getHandle().length() == UpdateValidateUtil.MAX_LENGTH_HANDLE);
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_empty() throws Exception {
        EntityDto entity = generateEntityDto();
        entity.setHandle(null);
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
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
        EntityDto entity = generateEntityDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE + 1);
        entity.setHandle(stringExceedOneMoreChar);
        assertTrue(entity.getHandle().length() > UpdateValidateUtil.MAX_LENGTH_HANDLE);
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
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
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(
                        invalidContent))
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
        EntityDto entity = generateEntityDto();
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(entity.getHandle()));
        mockMvc.perform(
                post(URI_ENTITY_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.subErrorCode").value(4091))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4091
                                                .getMessage(), entity
                                                .getHandle()))));
    }      
    private EntityDto generateEntityDto() {
        EntityDto entity = new EntityDto();
        entity.setHandle("h1");
    	entity.setEmail("100_1@cnnic.cn");
    	entity.setFn("00miss");
    	entity.setKind("individual");
    	entity.setOrg("中国互联网络信息中心");
    	entity.setPort43("whois.example.net");
    	entity.setLang("zh");
    	entity.setTitle("team leader");
    	entity.setUrl("00miss.cnnic.cn");
        Map<String, String> customProperties = new LinkedHashMap<String, String>();
        customProperties.put("customKey1", "customValue1");
        customProperties.put("customKey2", "customValue2");
        entity.setCustomProperties(customProperties);
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("active");
        entity.setStatus(status);
        return entity;
    }

    private String createStringWithLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("1");
        }
        return sb.toString();
    }
}
