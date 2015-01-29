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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.QueryDao;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.EntityAddressDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.EntityAddress;
import org.restfulwhois.rdap.common.model.IPAddress;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author zhanyq
 * 
 */
public class EntityAddressUpdateDaoTest extends BaseTest {


       @Autowired
       private UpdateDao<EntityAddress, EntityAddressDto> updateDao;   

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")
	    @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
          value = "classpath:/org/restfulwhois/rdap/dao/impl/entityAddress-create.xml")
	    public void testCreateEntityAddress() throws Exception {
	    	Entity entity = new Entity();
	    	entity.setId(1L);	    	
	    	List<EntityAddressDto> entityAddressList = 
	    			createEntityAddressList();
	        updateDao.saveAsInnerObjects(entity, entityAddressList);
	    }

	    public static List<EntityAddressDto> createEntityAddressList() {
            List<EntityAddressDto> entityAddressList = 
            		new ArrayList<EntityAddressDto>();
	    	EntityAddressDto entityAddressDto = new EntityAddressDto();
	    	entityAddressDto.setCountry("中国");
	    	entityAddressDto.setExtendedAddress("2");
	    	entityAddressDto.setLocality("北京");
	    	entityAddressDto.setPostalcode("100190");
	    	entityAddressDto.setPostbox("6");
	    	entityAddressDto.setPref(1);
	    	entityAddressDto.setRegion("北京");
	    	entityAddressDto.setStreetAddress("南四街四号");
	    	entityAddressDto.setTypes("work");
	    	entityAddressList.add(entityAddressDto);
	    	entityAddressDto = new EntityAddressDto();
	    	entityAddressDto.setCountry("中国");
	    	entityAddressDto.setExtendedAddress("3");
	    	entityAddressDto.setLocality("北京");
	    	entityAddressDto.setPostalcode("100120");
	    	entityAddressDto.setPostbox("1");
	    	entityAddressDto.setPref(2);	    	
	    	entityAddressDto.setStreetAddress("中关村南四街四号");
	    	entityAddressDto.setTypes("home");
	    	entityAddressList.add(entityAddressDto);
	    	entityAddressDto = new EntityAddressDto();
	    	entityAddressList.add(entityAddressDto);
            return entityAddressList;
        }       
        
        @Test
        @DatabaseSetup("entityAddress-delete.xml")
        @DatabaseTearDown("teardown.xml")       
        public void testDeleteEntityAddress() throws Exception {        	
        	Entity entity = new Entity();
        	entity.setId(1L);	    	
            updateDao.deleteAsInnerObjects(entity);
            super.assertTablesForUpdate("teardown.xml", "RDAP_VCARD_ADR");
            
        }
        
        @Test
        @DatabaseSetup("entityAddress-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
          value = "classpath:/org/restfulwhois/rdap/dao/impl/entityAddress-create.xml")
        public void testUpdateEntityAddress() throws Exception {        	
        	Entity entity = new Entity();
        	entity.setId(1L);	    	
            List<EntityAddressDto> entityAddressList = createEntityAddressList();
            updateDao.updateAsInnerObjects(entity, entityAddressList);
        }        
       
}
