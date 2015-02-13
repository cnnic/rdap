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
package org.restfulwhois.rdap.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author zhanyq
 * 
 */
public class EntityUpdateDaoTest extends BaseTest {
	 private static final String TABLE_REL_ENTITY_REGISTRATION = "REL_ENTITY_REGISTRATION";

	 @Autowired
	    private UpdateDao<Entity, EntityDto> updateDao;

	    @Test
	   // @DatabaseSetup("teardown.xml")
	    @DatabaseSetup("rel-entity-create-init.xml")
	    @DatabaseTearDown("teardown.xml")   
	    public void testcreateRel() throws Exception {
	    	Domain domain = new Domain();
	    	domain.setId(1L);
	    	DomainDto domainDto = new DomainDto();
	    	List<EntityHandleDto> entityHandleList = 
	    			createEntityHandleList();
            domainDto.setEntities(entityHandleList);
            domain.setDto(domainDto);
            updateDao.saveRel(domain);
	        super.assertTablesForUpdate("rel-entity-update.xml", 
	        		TABLE_REL_ENTITY_REGISTRATION);
	    }
	    
	    public static List<EntityHandleDto> createEntityHandleList() {
	    	List<EntityHandleDto> entityHandleList = 
	    			new ArrayList<EntityHandleDto>();
	    	EntityHandleDto entityHandle = new EntityHandleDto();
	    	entityHandle.setHandle("h1");
	    	List<String> roles = new ArrayList<String>();
	    	roles.add("registrant");
	    	roles.add("administrative");
	    	
	    	entityHandle.setRoles(roles);
	    	entityHandleList.add(entityHandle);
	    	return entityHandleList;
	    }
	    
	    @Test
	    @DatabaseSetup("rel-entity-delete.xml")
	    @DatabaseTearDown("teardown.xml")	    
	    public void testDeleteRel() throws Exception {
	      Domain domain = new Domain();
	      domain.setId(1L);
	      updateDao.deleteRel(domain);
          super.assertTablesForUpdate("teardown.xml","REL_ENTITY_REGISTRATION");
	    }
	    
	    @Test
	    @DatabaseSetup("rel-entity-update-init.xml")
	    @DatabaseTearDown("teardown.xml")	    
	    public void testUpdateRel() throws Exception {	       
	        Domain domain = new Domain();
	    	domain.setId(1L);
	    	DomainDto domainDto = new DomainDto();
	    	List<EntityHandleDto> entityHandleList = 
	    			new ArrayList<EntityHandleDto>();
	    	EntityHandleDto entityHandle = new EntityHandleDto();
	    	entityHandle.setHandle("h1");
	    	List<String> roles = new ArrayList<String>();
	    	roles.add("registrant");
	    	roles.add("administrative");
	    	
	    	entityHandle.setRoles(roles);
	    	entityHandleList.add(entityHandle);
            domainDto.setEntities(entityHandleList);
            domain.setDto(domainDto);
            updateDao.updateRel(domain);
	        super.assertTablesForUpdate("rel-entity-update.xml", 
	        		TABLE_REL_ENTITY_REGISTRATION);
	    }
	    
	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")
	    @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
          value = "classpath:/org/restfulwhois/rdap/dao/impl/entity-create.xml")
	    public void test_save_entity_and_status() throws Exception {
	        Entity entity = new Entity();
	        entity.setEmail("100_1@cnnic.cn"); 
	        entity.setFn("00miss");
	        entity.setHandle("h1");
	        entity.setKind("individual");
	        entity.setLang("en");
	        entity.setOrg("中国互联网络信息中心");
	        entity.setPort43("whois.example.net");
	        entity.setUrl("00miss.cnnic.cn");
	        entity.setTitle("team leader");
	        entity.setLang("zh");
	        List<String> status = new ArrayList<String>();
	        status.add("validated");
	        status.add("active");
	        entity.setStatus(status);
	        Map<String, String> customProperties = new LinkedHashMap<String, String>();
	        customProperties.put("customKey1", "customValue1");
	        customProperties.put("customKey2", "customValue2");
	        entity.setCustomProperties(customProperties);
	        entity.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        updateDao.save(entity);
	        updateDao.saveStatus(entity);
	    }
	    
	    @Test
	    @DatabaseSetup("entity-delete.xml")
	    @DatabaseTearDown("teardown.xml")
	    public void test_delete_entity_and_status() throws Exception {
	    	Entity entity = new Entity();
	    	entity.setId(1L);
	    	updateDao.delete(entity);
	    	updateDao.deleteStatus(entity);
	        assertTablesForUpdate("teardown.xml", "RDAP_ENTITY",
	                "RDAP_ENTITY_STATUS");
	    }
	    @Test
        @DatabaseSetup("classpath:/org/restfulwhois/rdap/dao/impl/entity-update.xml")
	    @DatabaseTearDown("teardown.xml")
	    public void test_update_entity_and_status() throws Exception {
	        List<Map<?, ?>> resultList =
	                getTableDataForSql("RDAP_ENTITY",
	                        "select * from RDAP_ENTITY where HANDLE='h1'");
	        assertTrue(resultList.size() > 0);
	        Map<?, ?> existEntity = resultList.get(0);
	        Integer entityId = (Integer) existEntity.get("ENTITY_ID");
	        assertNotNull(entityId);
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
	        Entity entity = new Entity();
	        entity.setId(Long.valueOf(entityId));
	        entity.setHandle(updateHandle);	       
	        entity.setPort43(updatePort43);
	        entity.setLang(updateLang);
	        entity.setEmail(updateEmail);
	        entity.setFn(updateFn);
	        entity.setKind(updateKind);
	        entity.setOrg(updateOrg);
	        entity.setUrl(updateUrl);
	        entity.setTitle(updateTitle);
	        List<String> expectedStatus = new ArrayList<String>();
	        expectedStatus.add(updateStatusRenewProbibited);
	        expectedStatus.add(updateStatusTransferProbibited);
	        expectedStatus.add(updateStatusDeleteProbibited);
	        entity.setStatus(expectedStatus);
	        Map<String, String> customProperties = new LinkedHashMap<String, String>();
	        customProperties.put("customKey3", "customValue3");
	        entity.setCustomProperties(customProperties);
	        entity.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        updateDao.update(entity);
	        updateDao.updateStatus(entity);
	        assertEntity(updateEmail, updateFn, updateKind, updateOrg, updateLang, 
	        		updateUrl, originalHandle, updatePort43);
	        assertStatus();
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
