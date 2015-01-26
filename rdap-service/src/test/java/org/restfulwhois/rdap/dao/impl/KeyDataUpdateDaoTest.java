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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.KeyDataDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.KeyData;
import org.restfulwhois.rdap.common.model.SecureDns;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author zhanyq
 * 
 */
public class KeyDataUpdateDaoTest extends BaseTest {	 

	    @Autowired
	    private UpdateDao<KeyData, KeyDataDto> updateDao;

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")   
	    @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/keyData-update.xml")
	    public void testcreateKeyData() throws Exception {
	    	SecureDns secureDns = new SecureDns();
            secureDns.setId(1L);
	    	List<KeyDataDto> keyDataList = new ArrayList<KeyDataDto>();
	    	KeyDataDto keyData = new KeyDataDto();
	    	keyData.setAlgorithm(1);	    	
	    	keyData.setProtocol(1);
	    	keyData.setFlags(1);
	    	keyData.setPublicKey("1-kd-dt-200-50");	    	
	    	//link
	    	List<LinkDto> linkList = LinkUpdateDaoTest.createLinkList();
	    	keyData.setLinks(linkList);
	        
	    	keyDataList.add(keyData);
	        updateDao.saveAsInnerObjects(secureDns, keyDataList);
	    }
	    
	    @Test
        @DatabaseSetup("keyData-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/keyData-empty.xml")
        public  void testDeleteKeyData() throws Exception {
            SecureDns secureDns = new SecureDns();
            secureDns.setId(1L);
            updateDao.deleteAsInnerObjects(secureDns);
            
        }
	    
	    @Test
        @DatabaseSetup("keyData-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/keyData-update.xml")
        public  void testUpdateKeyData() throws Exception {
	    	SecureDns secureDns = new SecureDns();
            secureDns.setId(1L);
	    	List<KeyDataDto> keyDataList = new ArrayList<KeyDataDto>();
	    	KeyDataDto keyData = new KeyDataDto();
	    	keyData.setAlgorithm(1);	    	
	    	keyData.setProtocol(1);
	    	keyData.setFlags(1);
	    	keyData.setPublicKey("1-kd-dt-200-50");	    	
	    	//link
	    	List<LinkDto> linkList = LinkUpdateDaoTest.createLinkList();
	    	keyData.setLinks(linkList);
	        
	    	keyDataList.add(keyData);
            updateDao.updateAsInnerObjects(secureDns, keyDataList);
            
        }
}
