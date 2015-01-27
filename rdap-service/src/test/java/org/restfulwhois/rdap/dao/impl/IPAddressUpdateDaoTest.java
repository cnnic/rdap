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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.QueryDao;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;
import org.restfulwhois.rdap.common.model.IPAddress;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author zhanyq
 * 
 */
public class IPAddressUpdateDaoTest extends BaseTest {


       @Autowired
       private UpdateDao<IPAddress, IpAddressDto> updateDao;
       @Autowired
       private QueryDao<IPAddress> ipAddressQueryDao;

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")  	  
	    public void testCreateIPAddress() throws Exception {
	    	Nameserver nameserver = new Nameserver();
	    	nameserver.setId(1L);	    	
	    	List<IpAddressDto> ipAddressList = createIpAddressList();
	        updateDao.saveAsInnerObjects(nameserver, ipAddressList);
	        assertIpAddress();
	    }

	    public static List<IpAddressDto> createIpAddressList() {
            List<IpAddressDto> ipAddressList = new ArrayList<IpAddressDto>();
	    	List<String> ipList = new ArrayList<String>();
	    	ipList.add("::2001:6a8:0:1");
	    	ipList.add("218.241.111.11");
	    	IpAddressDto ipAddressDto = new IpAddressDto();
	    	ipAddressDto.setIpList(ipList);	
	    	ipAddressList.add(ipAddressDto);
            return ipAddressList;
        }       
        
        @Test
        //@DatabaseSetup("ipAddress-delete.xml")
        @DatabaseTearDown("teardown.xml")       
        public void testDeleteIpAddress() throws Exception {
        	super.databaseSetupWithBinaryColumns("ipAddress-delete.xml");
        	Nameserver nameserver = new Nameserver();
	    	nameserver.setId(1L);	    	
            updateDao.deleteAsInnerObjects(nameserver);
            super.assertTablesForUpdate("teardown.xml", "RDAP_NAMESERVER_IP");
            
        }
        
        @Test      
        @DatabaseTearDown("teardown.xml")        
        public void testUpdateIpAddress() throws Exception {
        	super.databaseSetupWithBinaryColumns("ipAddress-delete.xml");
        	Nameserver nameserver = new Nameserver();
	    	nameserver.setId(1L);	    	
            List<IpAddressDto> ipAddressList = createIpAddressList();
            updateDao.updateAsInnerObjects(nameserver, ipAddressList);
            assertIpAddress();
        }
        
        
        private void assertIpAddress() throws Exception {
        	List<IPAddress> resultList = ipAddressQueryDao
                    .queryAsInnerObjects(1L, ModelType.NAMESERVER);
            assertTrue(resultList.size() > 0);            
            IPAddress ipAddress = resultList.get(0);             
            assertEquals("::2001:6a8:0:1", ipAddress.getAddressV6().get(0));
            assertEquals("218.241.111.11", ipAddress.getAddressV4().get(0)); 
        }     
}
