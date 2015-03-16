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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.model.Autnum;
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
public class AutnumUpdateDaoTest extends BaseTest {


	    @Autowired
	    private UpdateDao<Autnum, AutnumDto> updateDao;

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")
	    @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
          value = "classpath:/org/restfulwhois/rdap/dao/impl/autnum-create.xml")
	    public void test_save_autnum_and_status() throws Exception {
	    	Autnum autnum = new Autnum();
	    	autnum.setHandle("h1");
	    	autnum.setStartAutnum(1L);
	    	autnum.setEndAutnum(1L);
	    	autnum.setName("as-200-2:data:1~1");
	    	autnum.setPort43("cnnic.cn");
	        autnum.setCountry("CN");	 
	        autnum.setLang("zh");
	        autnum.setType("alocated");
	        List<String> status = new ArrayList<String>();
	        status.add("validated");
	        status.add("update prohibited");
	        autnum.setStatus(status);
	        Map<String, String> customProperties = new LinkedHashMap<String, String>();
	        customProperties.put("customKey1", "customValue1");
	        customProperties.put("customKey2", "customValue2");
	        autnum.setCustomProperties(customProperties);
	        autnum.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        updateDao.save(autnum);
	        updateDao.saveStatus(autnum);
	    }
	    
	    @Test
	    @DatabaseSetup("autnum-delete.xml")
	    @DatabaseTearDown("teardown.xml")
	    public void test_delete_autnum_and_status() throws Exception {
	    	Autnum autnum = new Autnum();
	    	autnum.setId(1L);
	    	updateDao.delete(autnum);
	    	updateDao.deleteStatus(autnum);
	        assertTablesForUpdate("teardown.xml", "RDAP_AUTNUM",
	                "RDAP_AUTNUM_STATUS");
	    }

	    @Test
	    @DatabaseSetup("classpath:/org/restfulwhois/rdap/dao/impl/autnum-delete.xml")
	    @DatabaseTearDown("teardown.xml")
	    public void test_update_autnum_and_status() throws Exception {
	        List<Map<?, ?>> resultList =
	                getTableDataForSql("RDAP_AUTNUM",
	                        "select * from RDAP_AUTNUM where HANDLE='h1'");
	        assertTrue(resultList.size() > 0);
	        Map<?, ?> existNameserver = resultList.get(0);
	        Integer autnumId = (Integer) existNameserver.get("AS_ID");
	        assertNotNull(autnumId);
	        Long updateStartAutnum = 2L;
	        Long updateEndAutnum = 3L;
	        String updateLang = "us";
	        String originalHandle = "h1";
	        String updatePort43 = "port43";
	        String updateHandle = "new-handle";
	        String updateName = "as-200-3:4269852";
	        String updateCountry = "FR";
	        String updateType = "DIRECT ALLOCATION";
	        String updateStatusRenewProbibited = "renew prohibited";
	        String updateStatusTransferProbibited = "transfer prohibited";
	        String updateStatusDeleteProbibited = "delete prohibited";
	        Autnum autnum = new Autnum();
	        autnum.setId(Long.valueOf(autnumId));
	        autnum.setHandle(updateHandle);
	    	autnum.setStartAutnum(updateStartAutnum);
	    	autnum.setEndAutnum(updateEndAutnum);
	    	autnum.setName(updateName);
	    	autnum.setPort43(updatePort43);
	        autnum.setCountry(updateCountry);	 
	        autnum.setLang(updateLang);
	        autnum.setType(updateType);    
	        List<String> expectedStatus = new ArrayList<String>();
	        expectedStatus.add(updateStatusRenewProbibited);
	        expectedStatus.add(updateStatusTransferProbibited);
	        expectedStatus.add(updateStatusDeleteProbibited);
	        autnum.setStatus(expectedStatus);
	        Map<String, String> customProperties = new LinkedHashMap<String, String>();
	        customProperties.put("customKey3", "customValue3");
	        autnum.setCustomProperties(customProperties);
	        autnum.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        updateDao.update(autnum);
	        updateDao.updateStatus(autnum);
	        assertAutnum(updateName, updateLang, originalHandle, 
                    updatePort43, updateStartAutnum, updateEndAutnum,
	        		updateCountry, updateType);
	        assertStatus();
	    }

	    private void assertStatus() throws Exception {
	        List<Map<?, ?>> resultList1 =
	                getTableDataForSql("RDAP_AUTNUM_STATUS",
	                        "select * from RDAP_AUTNUM_STATUS"
	                        + " where AS_ID=1");
	        assertEquals(3, resultList1.size());
	    }

       private void assertAutnum(String updateName, String updateLang,
	            String originalHandle, String updatePort43,
	            Long updateStartAutnum, Long updateEndAutnum, String updateCountry,
	            String updateType) throws Exception {
	        List<Map<?, ?>> resultList =
	                getTableDataForSql("RDAP_AUTNUM",
	                        "select * from RDAP_AUTNUM where HANDLE='h1'");
	        assertTrue(resultList.size() > 0);
	        Map<?, ?> actualNameserver = resultList.get(0);
	        assertEquals(originalHandle, actualNameserver.get("HANDLE"));	    
	        assertEquals(updateName, actualNameserver.get("NAME"));
	        assertEquals(updateStartAutnum, 
	        		(Long)((BigInteger)actualNameserver.get("START_AUTNUM")).longValue());
	        assertEquals(updateLang, actualNameserver.get("LANG"));
	        assertEquals(updatePort43, actualNameserver.get("PORT43"));
	        assertEquals(updateType, actualNameserver.get("TYPE"));
	        assertEquals(updateEndAutnum, 
	        		(Long)((BigInteger)actualNameserver.get("END_AUTNUM")).longValue());
	        assertEquals(updateCountry, actualNameserver.get("COUNTRY"));
	        assertEquals("{\"customKey3\":\"customValue3\"}",
	        		actualNameserver.get("CUSTOM_PROPERTIES"));
	    }


}
