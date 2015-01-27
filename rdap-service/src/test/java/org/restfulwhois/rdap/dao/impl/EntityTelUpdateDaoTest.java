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
import org.restfulwhois.rdap.common.dto.embedded.EntityTelephoneDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.EntityTelephone;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author zhanyq
 * 
 */
public class EntityTelUpdateDaoTest extends BaseTest {


       @Autowired
       private UpdateDao<EntityTelephone, EntityTelephoneDto> updateDao;       

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")
	    @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/entityTel-create.xml")
	    public void testCreateEntityTel() throws Exception {
	    	Entity entity = new Entity();
	    	entity.setId(1L);	    	
	    	List<EntityTelephoneDto> entityTelList = 
	    			createEntityTelList();
	        updateDao.saveAsInnerObjects(entity, entityTelList);
	    }

	    public static List<EntityTelephoneDto> createEntityTelList() {
            List<EntityTelephoneDto> entityTelList = 
            		new ArrayList<EntityTelephoneDto>();
            EntityTelephoneDto entityTelDto = new EntityTelephoneDto();
            entityTelDto.setExtNumber("3179");
            entityTelDto.setNumber("+58813179");
            entityTelDto.setPref(1);           
	    	entityTelDto.setTypes("home;voice");
	    	entityTelList.add(entityTelDto);
	    	entityTelDto = new EntityTelephoneDto();
	    	entityTelDto.setExtNumber("1234");
            entityTelDto.setNumber("+1-555-555-1235");
            entityTelDto.setPref(2);           
	    	entityTelDto.setTypes("work;voice");	    	
	    	entityTelList.add(entityTelDto);
	    	entityTelDto = new EntityTelephoneDto();
	    	entityTelDto.setExtNumber("4567");
            entityTelDto.setPref(3);
	    	entityTelDto.setTypes("work");	    	
	    	entityTelList.add(entityTelDto);
            return entityTelList;
        }       
        
        @Test
        @DatabaseSetup("entityTel-delete.xml")
        @DatabaseTearDown("teardown.xml")       
        public void testDeleteEntityTel() throws Exception {        	
        	Entity entity = new Entity();
        	entity.setId(1L);	    	
            updateDao.deleteAsInnerObjects(entity);
            super.assertTablesForUpdate("teardown.xml", "RDAP_VCARD_TEL");
            
        }
        
        @Test
        @DatabaseSetup("entityAddress-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
          value = "classpath:/org/restfulwhois/rdap/dao/impl/entityTel-create.xml")
        public void testUpdateEntityTel() throws Exception {        	
        	Entity entity = new Entity();
        	entity.setId(1L);	    	
            List<EntityTelephoneDto> entityAddressList = createEntityTelList();
            updateDao.updateAsInnerObjects(entity, entityAddressList);
        }        
       
}
