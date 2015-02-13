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
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.embedded.HandleDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Nameserver;
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
public class NameserverUpdateDaoTest extends BaseTest {
	
	 private static final String TABLE_REL_DOMAIN_NAMESERVER = "REL_DOMAIN_NAMESERVER";

	    @Autowired
	    private UpdateDao<Nameserver, NameserverDto> nsUpdateDao;

	    @Test
	   // @DatabaseSetup("teardown.xml")
	    @DatabaseSetup("rel-domain-nameserver-init.xml")
	    @DatabaseTearDown("teardown.xml")   
	    public void testcreateRel() throws Exception {
	    	Domain domain = new Domain();
	    	domain.setId(1L);
	    	DomainDto domainDto = new DomainDto();
	    	List<HandleDto> handleList = new ArrayList<HandleDto>();
	    	HandleDto handle = new HandleDto();
	    	handle.setHandle("h1");
	    	handleList.add(handle);
	    	handle = new HandleDto();
	    	handle.setHandle("h2");
	    	handleList.add(handle);
           domainDto.setNameservers(handleList);
           domain.setDto(domainDto);
           nsUpdateDao.saveRel(domain);
	       super.assertTablesForUpdate("rel-domain-nameserver.xml", 
	        		TABLE_REL_DOMAIN_NAMESERVER);
	    }
	    
	    
	    @Test
	    @DatabaseSetup("rel-domain-nameserver-delete.xml")
	    @DatabaseTearDown("teardown.xml")	    
	    public void testDeleteRel() throws Exception {
	       Domain domain = new Domain();
	       domain.setId(1L);
	       nsUpdateDao.deleteRel(domain);
           super.assertTablesForUpdate("teardown.xml", "REL_DOMAIN_NAMESERVER");
	    }
	    
	    @Test
	    @DatabaseSetup("rel-domain-nameserver-update-init.xml")
	    @DatabaseTearDown("teardown.xml")	    
	    public void testUpdateRel() throws Exception {
	        Domain domain = new Domain();
	        domain.setId(1L);
	        
	    	DomainDto domainDto = new DomainDto();
	    	List<HandleDto> handleList = new ArrayList<HandleDto>();
	    	HandleDto handle = new HandleDto();
	    	handle.setHandle("h1");
	    	handleList.add(handle);
	    	handle = new HandleDto();
	    	handle.setHandle("h2");
	    	handleList.add(handle);
           domainDto.setNameservers(handleList);
           domain.setDto(domainDto);
           nsUpdateDao.updateRel(domain);
	        super.assertTablesForUpdate("rel-domain-nameserver.xml", 
	        		TABLE_REL_DOMAIN_NAMESERVER);
	    }
	    
	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")
	    @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
          value = "classpath:/org/restfulwhois/rdap/dao/impl/nameserver-create.xml")
	    public void test_save_nameserver_and_status() throws Exception {
	        Nameserver nameserver = new Nameserver();
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
	        nameserver.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        nsUpdateDao.save(nameserver);
	        nsUpdateDao.saveStatus(nameserver);
	    }
	    
	    @Test
	    @DatabaseSetup("nameserver-delete.xml")
	    @DatabaseTearDown("teardown.xml")
	    public void test_delete_nameserver_and_status() throws Exception {
	    	Nameserver nameserver = new Nameserver();
	    	nameserver.setId(1L);
	        nsUpdateDao.delete(nameserver);
	        nsUpdateDao.deleteStatus(nameserver);
	        assertTablesForUpdate("teardown.xml", "RDAP_NAMESERVER",
	                "RDAP_NAMESERVER_STATUS");
	    }

	    @Test
	    @DatabaseSetup("classpath:/org/restfulwhois/rdap/dao/impl/nameserver-update.xml")
	    @DatabaseTearDown("teardown.xml")
	    public void test_update_nameserver_and_status() throws Exception {
	        List<Map<?, ?>> resultList =
	                getTableDataForSql("RDAP_NAMESERVER",
	                        "select * from RDAP_NAMESERVER where HANDLE='h1'");
	        assertTrue(resultList.size() > 0);
	        Map<?, ?> existNameserver = resultList.get(0);
	        Integer nameserverId = (Integer) existNameserver.get("NAMESERVER_ID");
	        assertNotNull(nameserverId);
	        String updateLdhName = "update.cn";
	        String updateLang = "us";
	        String originalHandle = "h1";
	        String updatePort43 = "update-port43";
	        String updateHandle = "new-handle";
	        String updateStatusRenewProbibited = "renew prohibited";
	        String updateStatusTransferProbibited = "transfer prohibited";
	        String updateStatusDeleteProbibited = "delete prohibited";
	        Nameserver nameserver = new Nameserver();
	        nameserver.setId(Long.valueOf(nameserverId));
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
	        Map<String, String> customProperties = new LinkedHashMap<String, String>();
	        customProperties.put("customKey3", "customValue3");
	        nameserver.setCustomProperties(customProperties);
	        nameserver.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        nsUpdateDao.update(nameserver);
	        nsUpdateDao.updateStatus(nameserver);
	        assertNameserver(updateLdhName, updateLang, originalHandle, updatePort43);
	        assertStatus();
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
