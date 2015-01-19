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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.JsonHelper;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.model.Domain.DomainType;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
import org.restfulwhois.rdap.dao.impl.DomainUpdateDaoTest;
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
    public void test_ok() throws Exception {
        DomainDto domain = generateDomainDto();
        String content = JsonHelper.serialize(domain);
        mockMvc.perform(
                post(URI_DOMAIN_U).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.handle").value(domain.getHandle()));
        super.assertTablesForUpdate("domain-update.xml",
                DomainUpdateDaoTest.TABLE_RDAP_DOMAIN);
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_valid_ldhName_maxlength() throws Exception {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LDHNAME);
        domain.setLdhName(stringMaxLength);
        assertTrue(domain.getLdhName().length() == UpdateValidateUtil.MAX_LENGTH_LDHNAME);
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
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
    public void test_invalid_ldhName_exceed_maxlength() throws Exception {
        DomainDto domain = new DomainDto();
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LDHNAME + 1);
        domain.setLdhName(stringExceedOneMoreChar);
        assertTrue(domain.getLdhName().length() > UpdateValidateUtil.MAX_LENGTH_LDHNAME);
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
        domain.setType(DomainType.DNR.getName());
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
        DomainDto domain = new DomainDto();
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE);
        domain.setHandle(stringMaxLength);
        assertTrue(domain.getHandle().length() == UpdateValidateUtil.MAX_LENGTH_HANDLE);
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
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
    public void test_invalid_handle_empty() throws Exception {
        DomainDto domain = new DomainDto();
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
        domain.setType(DomainType.DNR.getName());
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
        DomainDto domain = new DomainDto();
        domain.setLdhName("cnnic.cn");
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_HANDLE + 1);
        domain.setHandle(stringExceedOneMoreChar);
        assertTrue(domain.getHandle().length() > UpdateValidateUtil.MAX_LENGTH_HANDLE);
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
        domain.setType(DomainType.DNR.getName());
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
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
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
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
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
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LANG);
        domain.setLang(stringMaxLength);
        assertTrue(domain.getLang().length() == UpdateValidateUtil.MAX_LENGTH_LANG);
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
    public void test_invalid_lang_exceed_maxlength() throws Exception {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_LANG + 1);
        domain.setLang(stringExceedOneMoreChar);
        assertTrue(domain.getLang().length() > UpdateValidateUtil.MAX_LENGTH_LANG);
        domain.setType(DomainType.DNR.getName());
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
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setLang("zh");
        String stringMaxLength =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_PORT43);
        domain.setPort43(stringMaxLength);
        assertTrue(domain.getPort43().length() == UpdateValidateUtil.MAX_LENGTH_PORT43);
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
    public void test_invalid_port43_exceed_maxlength() throws Exception {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        String stringExceedOneMoreChar =
                createStringWithLength(UpdateValidateUtil.MAX_LENGTH_PORT43 + 1);
        domain.setPort43(stringExceedOneMoreChar);
        assertTrue(domain.getPort43().length() > UpdateValidateUtil.MAX_LENGTH_PORT43);
        domain.setUnicodeName("cnnic.cn");
        domain.setLang("zh");
        domain.setType(DomainType.DNR.getName());
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
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(rdapJson))
                .andExpect(jsonPath("$.errorCode").value(400))
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

    private DomainDto generateDomainDto() {
        DomainDto domain = new DomainDto();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
        domain.setType(DomainType.DNR.getName());
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
