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
import org.restfulwhois.rdap.common.dto.EntityDto;
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
 * @author zhanyq
 * 
 */
@SuppressWarnings("rawtypes")
public class EntityUpdateControllerTest extends BaseTest {

    /**
     * entity query URI.
     */
    public static final String URI_ENTITY_U = "/u/entity/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    final private String rdapJson = "application/rdap+json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/entity-update.xml")
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public
            void test_ok_only_entity() throws Exception {
    	String updateEmail = "john@gmail.com";
        String updateFn = "john";
        String updateKind = "update-org";
        String updateLang = "us";
        String updateOrg = "org";
        String updateUrl = "http://john.com";
        String updateTitle = "CEO";
        String originalHandle = "h1";
        String updatePort43 = "update-port43";
        String updateHandle = "new-handle";
        String updateStatusRenewProbibited = "renew prohibited";
        String updateStatusTransferProbibited = "transfer prohibited";
        String updateStatusDeleteProbibited = "delete prohibited";
        EntityDto entity = new EntityDto();
        entity.setHandle(updateHandle);
        entity.setEmail(updateEmail);
        entity.setKind(updateKind);
        entity.setFn(updateFn);
        entity.setLang(updateLang);
        entity.setOrg(updateOrg);
        entity.setPort43(updatePort43);
        entity.setUrl(updateUrl);
        entity.setTitle(updateTitle);        
        List<String> expectedStatus = new ArrayList<String>();
        expectedStatus.add(updateStatusRenewProbibited);
        expectedStatus.add(updateStatusTransferProbibited);
        expectedStatus.add(updateStatusDeleteProbibited);
        entity.setStatus(expectedStatus);
        Map<String, String> customProperties = new HashMap<String, String>();
        customProperties.put("customKey3", "customValue3");
        entity.setCustomProperties(customProperties);
        String content = JsonHelper.serialize(entity);
        mockMvc.perform(
                put(URI_ENTITY_U + originalHandle).contentType(
                        MediaType.parseMediaType(rdapJson)).content(content))
                .andExpect(status().isOk())
                .andExpect(content().contentType(rdapJson));
        assertEntity(updateEmail, updateFn, updateKind, updateOrg, updateLang, 
        		updateUrl, originalHandle, updatePort43);
        assertStatus();
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_not_exist() throws Exception {
        String notExistHandle = "not-exist-handle";
        mockMvc.perform(
                put(URI_ENTITY_U + notExistHandle).contentType(
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
                getTableDataForSql("RDAP_ENTITY_STATUS",
                        "select * from RDAP_ENTITY_STATUS where ENTITY_ID=1");
        assertEquals(3, resultList1.size());
    }

    private void assertEntity(String updateEmail, String updateFn, String updateKind,
    		String updateOrg, String updateLang, String updateUrl,
            String originalHandle, String updatePort43) throws Exception {
        List<Map<?, ?>> resultList =
                getTableDataForSql("RDAP_ENTITY",
                        "select * from RDAP_ENTITY where HANDLE='h1'");
        assertTrue(resultList.size() > 0);
        Map<?, ?> actualEntity = resultList.get(0);
        assertEquals(originalHandle, actualEntity.get("HANDLE"));
        assertEquals(updateFn, actualEntity.get("FN"));
        assertEquals(updateKind, actualEntity.get("KIND"));
        assertEquals(updateOrg, actualEntity.get("ORG"));
        assertEquals(updateLang, actualEntity.get("LANG"));
        assertEquals(updateUrl, actualEntity.get("URL"));
        assertEquals(updatePort43, actualEntity.get("PORT43"));
        assertEquals("{\"customKey3\":\"customValue3\"}",
        		actualEntity.get("CUSTOM_PROPERTIES"));
    }

}
