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
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.PublicId;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author zhanyq
 * 
 */
public class PublicIdUpdateDaoTest extends BaseTest {

	 private static final String TABLE_RDAP_PUBLICID = "RDAP_PUBLICID";
	 private static final String TABLE_REL_PUBLICID_REGISTRATION = "REL_PUBLICID_REGISTRATION";


	    @Autowired
	    private UpdateDao<PublicId, PublicIdDto> updateDao;

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml") 
	    @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/publicId-create.xml")
	    public void testcreatePublicId() throws Exception {
	    	Domain domain = new Domain();
	    	domain.setId(1L);
	    	List<PublicIdDto> publicIdList = createPublicIdList();
	        updateDao.saveAsInnerObjects(domain, publicIdList);
	        //assertCreate();
	    }

	    public static void assertCreate() throws Exception {
            assertTablesForUpdate("publicId-create.xml", TABLE_RDAP_PUBLICID,
	        		TABLE_REL_PUBLICID_REGISTRATION);
        }

        public static List<PublicIdDto> createPublicIdList() {
            List<PublicIdDto> publicIdList = new ArrayList<PublicIdDto>();
	    	PublicIdDto publicId = new PublicIdDto();
	    	publicId.setIdentifier("cnnic-1");
	    	publicId.setType("cnnic");
	    	publicIdList.add(publicId);
            return publicIdList;
        }
        @Test
        @DatabaseSetup("publicId-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/publicId-empty.xml")
        public void testDeletePublicId() throws Exception {
            Domain domain = new Domain();
            domain.setId(1L);
            updateDao.deleteAsInnerObjects(domain);
        }
        
        @Test
        @DatabaseSetup("publicId-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/publicId-update.xml")
        public void testUpdatePublicId() throws Exception {
        	Domain domain = new Domain();
	    	domain.setId(1L);
	    	List<PublicIdDto> publicIdList = createPublicIdList();
            updateDao.updateAsInnerObjects(domain, publicIdList);
            
        }
}
