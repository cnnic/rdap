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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.model.Domain.DomainType;
import org.restfulwhois.rdap.common.support.RdapProperties;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
import org.restfulwhois.rdap.dao.impl.DomainUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.LinkUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.PublicIdUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.RemarkUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.SecureDnsUpdateDaoTest;
import org.restfulwhois.rdap.dao.impl.VariantsUpdateDaoTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
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
public class DomainCreateControllerTest extends BaseTest {

    /**
     * domain query URI.
     */
    public static final String URI_DOMAIN_U = "/u/domain";

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
    public void test_ok_with_only_domain() throws Exception {
        DomainDto domain = generateDomainDto();
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
        super.assertTablesForUpdate("domain-create.xml",
                DomainUpdateDaoTest.TABLE_RDAP_DOMAIN);
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_thin_domain() throws Exception {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setType(DomainType.DNR.getName());
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_not_empty_custom_properties() throws Exception {
        DomainDto domain = generateDomainDto();
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_DOMAIN",
                        "select CUSTOM_PROPERTIES from RDAP_DOMAIN where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        assertEquals(
                "{\"customKey1\":\"customValue1\",\"customKey2\":\"customValue2\"}",
                resultList.get(0).get("CUSTOM_PROPERTIES"));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_empty_custom_properties() throws Exception {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setType(DomainType.DNR.getName());
        domain.setCustomProperties(new HashMap<String, String>());
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_DOMAIN",
                        "select CUSTOM_PROPERTIES from RDAP_DOMAIN where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        assertNull(resultList.get(0).get("CUSTOM_PROPERTIES"));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_null_custom_properties() throws Exception {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setType(DomainType.DNR.getName());
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_DOMAIN",
                        "select CUSTOM_PROPERTIES from RDAP_DOMAIN where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        assertNull(resultList.get(0).get("CUSTOM_PROPERTIES"));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_ignore_unrecognized_properties() throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"\":1,\"unknownP\":\"x\"}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value("1"));
    }

    @Test
    public void test_invalid_propertyType_should_be_object_but_is_array()
            throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":[{\"maxSigLife\":1}]}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
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
    public void test_invalid_propertyType_should_be_int_but_is_string()
            throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"maxSigLife\":\"i-am-not-int-value\"}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
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
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"maxSigLife\":1.8}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
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
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"maxSigLife\":"
                        + (UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN + 1)
                        + "}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4010));
    }

    @Test
    public void test_invalid_propertyType_int_but_exceed_max_value()
            throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"maxSigLife\":4294967296}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4001));
    }

    @Test
    public void test_invalid_propertyType_should_be_tinyint_but_is_string()
            throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"zoneSigned\":\"i-am-not-tinyint-value\"}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
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
            test_ok_propertyType_should_be_boolean_but_is_int_128_result_is_true()
                    throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"zoneSigned\":128}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value("1"));
    }

    @Test
    public
            void
            test_ok_propertyType_should_be_boolean_but_is_int_0_result_is_false()
                    throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"zoneSigned\":0}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value("1"));
    }

    @Test
    public
            void
            test_ok_propertyType_should_be_boolean_but_is_int_minus_number_result_is_true()
                    throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"zoneSigned\":-1}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value("1"));
    }

    @Test
    public void
            test_invalid_propertyType_should_be_boolean_but_is_int_with_dot()
                    throws Exception {
        String content =
                "{\"handle\":\"1\",\"ldhName\":\"cnnic.cn\",\"type\":\"dnr\""
                        + ",\"secureDNS\":{\"zoneSigned\":1.1}}";
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
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
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_ok_with_fat_domain_with_all_inner_objects()
            throws Exception {
        DomainDto domain = generateDomainDto();
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("update prohibited");
        domain.setStatus(status);
        createAndSetEvents(domain);
        domain.setRemarks(RemarkUpdateDaoTest.createRemarkList());
        domain.setLinks(LinkUpdateDaoTest.createLinkList());
        domain.setPublicIds(PublicIdUpdateDaoTest.createPublicIdList());
        domain.setSecureDNS(SecureDnsUpdateDaoTest.createSecureDns().get(0));
        domain.setVariants(VariantsUpdateDaoTest.createVariants());
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
        // RemarkUpdateDaoTest.assertCreate();/link duplicated.
        // LinkUpdateDaoTest.assertCreate();
        PublicIdUpdateDaoTest.assertCreate();
        SecureDnsUpdateDaoTest.assertTable();
        VariantsUpdateDaoTest.assertTable();
    }

    private void createAndSetEvents(DomainDto domain) {
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
    public void test_valid_ldhName_maxlength() throws Exception {
        DomainDto domain = generateDomainDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LDHNAME);
        domain.setLdhName(stringMaxLength);
        assertTrue(domain.getLdhName().length() == UpdateValidateUtil.MAX_LENGTH_LDHNAME);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_ldhName_exceed_maxlength() throws Exception {
        DomainDto domain = generateDomainDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LDHNAME + 1);
        domain.setLdhName(stringExceedOneMoreChar);
        assertTrue(domain.getLdhName().length() > UpdateValidateUtil.MAX_LENGTH_LDHNAME);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4003))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4003
                                                .getMessage(), "ldhName",
                                        UpdateValidateUtil.MAX_LENGTH_LDHNAME
                                                + ""))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_handle_maxlength() throws Exception {
        DomainDto domain = generateDomainDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE);
        domain.setHandle(stringMaxLength);
        assertTrue(domain.getHandle().length() == UpdateValidateUtil.MAX_LENGTH_HANDLE);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_empty() throws Exception {
        DomainDto domain = generateDomainDto();
        domain.setHandle(null);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
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
        DomainDto domain = generateDomainDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE + 1);
        domain.setHandle(stringExceedOneMoreChar);
        assertTrue(domain.getHandle().length() > UpdateValidateUtil.MAX_LENGTH_HANDLE);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
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
                post(URI_DOMAIN_U).contentType(
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
    public void test_invalid_type_empty() throws Exception {
        DomainDto domain = generateDomainDto();
        domain.setType(null);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4002))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4002
                                                .getMessage(), "type"))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_type() throws Exception {
        DomainDto domain = generateDomainDto();
        domain.setType("invalidType");
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4008))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4008
                                                .getMessage(), "type"))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_lang_maxlength() throws Exception {
        DomainDto domain = generateDomainDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LANG);
        domain.setLang(stringMaxLength);
        assertTrue(domain.getLang().length() == UpdateValidateUtil.MAX_LENGTH_LANG);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_lang_exceed_maxlength() throws Exception {
        DomainDto domain = generateDomainDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LANG + 1);
        domain.setLang(stringExceedOneMoreChar);
        assertTrue(domain.getLang().length() > UpdateValidateUtil.MAX_LENGTH_LANG);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4003))
                .andExpect(
                        jsonPath("$.description")
                                .value(CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4003
                                                .getMessage(), "lang",
                                        UpdateValidateUtil.MAX_LENGTH_LANG + ""))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_port43_maxlength() throws Exception {
        DomainDto domain = generateDomainDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_PORT43);
        domain.setPort43(stringMaxLength);
        assertTrue(domain.getPort43().length() == UpdateValidateUtil.MAX_LENGTH_PORT43);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_port43_exceed_maxlength() throws Exception {
        DomainDto domain = generateDomainDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_PORT43 + 1);
        domain.setPort43(stringExceedOneMoreChar);
        assertTrue(domain.getPort43().length() > UpdateValidateUtil.MAX_LENGTH_PORT43);
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4003))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4003
                                                .getMessage(), "port43",
                                        UpdateValidateUtil.MAX_LENGTH_PORT43
                                                + ""))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_already_exist() throws Exception {
        DomainDto domain = generateDomainDto();
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isConflict())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(409))
                .andExpect(jsonPath("$.subErrorCode").value(4091))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4091
                                                .getMessage(), domain
                                                .getHandle()))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_event_date() throws Exception {
        DomainDto domain = generateDomainDto();
        List<EventDto> events = new ArrayList<EventDto>();
        domain.setEvents(events);
        EventDto event = new EventDto();
        events.add(event);
        event.setEventAction("registration");
        event.setEventActor("zhanyq");
        event.setEventDate("2015-01-15 17:15:12Z");
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
                .andExpect(jsonPath("$.subErrorCode").value(4007))
                .andExpect(
                        jsonPath("$.description").value(
                                CoreMatchers.hasItems(String.format(
                                        ServiceErrorCode.ERROR_4007
                                                .getMessage(),
                                        "events.eventDate"))));
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_method_not_allowed() throws Exception {
        DomainDto domain = generateDomainDto();
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                get(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType("application/rdap+json"))
                .andExpect(jsonPath("$.errorCode").value(405))
                .andExpect(jsonPath("$.subErrorCode").doesNotExist());
    }

    @Test
    public void test_not_filter_ratelimit() throws Exception {
        RdapProperties prop = new RdapProperties();
        Long oneMinuteInMilliseconds = 60000L;
        ReflectionTestUtils.setField(prop, "minSecondsAccessIntervalAnonymous",
                oneMinuteInMilliseconds);
    }

    private DomainDto generateDomainDto() {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
        domain.setType(DomainType.DNR.getName());
        Map<String, String> customProperties = new LinkedHashMap<String, String>();
        customProperties.put("customKey1", "customValue1");
        customProperties.put("customKey2", "customValue2");
        domain.setCustomProperties(customProperties);
        return domain;
    }

    private String createStringWithLength(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append("1");
        }
        return sb.toString();
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_can_not_update_domain_type() throws Exception {

    }
}
