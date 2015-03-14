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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.Network;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * @author zhanyq
 * 
 */
public class NetworkUpdateDaoTest extends BaseTest {


	    @Autowired
	    private UpdateDao<Network, IpDto> updateDao;

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml")
	   /* @ExpectedDatabase(
	            assertionMode = DatabaseAssertionMode.NON_STRICT,
          value = "classpath:/org/restfulwhois/rdap/dao/impl/ip-create.xml")*/
	    public void test_save_ip_and_status() throws Exception {
	    	Network network = new Network();
	    	network.setHandle("h1");
	    	network.setCidr("FFF1:1::");
	    	network.setEndAddress("218.241.111.12");
	    	network.setStartAddress("218.241.111.11");
	    	network.setIpVersion(IpVersion.V4);
	    	network.setParentHandle("ip-200-1");
	    	network.setName("cnnic-1");
	    	network.setPort43("cnnic.cn");
	    	network.setCountry("cn");	 
	    	network.setLang("zh");
	        network.setType("alocated");	  
	        List<String> status = new ArrayList<String>();
	        status.add("validated");
	        status.add("update prohibited");
	        network.setStatus(status);
	        Map<String, String> customProperties = new HashMap<String, String>();
	        customProperties.put("customKey1", "customValue1");
	        customProperties.put("customKey2", "customValue2");
	        network.setCustomProperties(customProperties);
	        network.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        updateDao.save(network);
	        int ipId = assertIp(network);
	        updateDao.saveStatus(network);
	        assertStatus(network,ipId);
	    }
	    
	    private int assertIp(Network network) throws Exception {
	    	List<Map<?, ?>> resultList =
	                getTableDataForSql("RDAP_IP",
	                        "select * from RDAP_IP where HANDLE='h1'");
	    	assertTrue(resultList.size() > 0);
	    	Map<?, ?> existIp = resultList.get(0);
	    	Integer ipId = (Integer) existIp.get("IP_ID");
	    	assertEquals(network.getHandle(), existIp.get("HANDLE"));
	        assertEquals(network.getCidr(), existIp.get("CIDR"));
	        assertEquals(network.getLang(), existIp.get("LANG"));
	        assertEquals(network.getPort43(), existIp.get("PORT43"));
	        assertEquals(network.getType(), existIp.get("TYPE"));
	        assertEquals(network.getName(), existIp.get("NAME"));
	        assertEquals(network.getParentHandle(), 
	        		existIp.get("PARENT_HANDLE"));
	        assertEquals(network.getIpVersion().getName(), 
	        		existIp.get("VERSION"));
	        assertEquals(network.getCountry(), existIp.get("COUNTRY"));
	        assertEquals(network.getStartAddress(), IpUtil.toString(
	            (byte[]) existIp.get("STARTADDRESS"), 
	            network.getIpVersion()));
	        assertEquals(network.getEndAddress(), IpUtil.toString(
	  	       (byte[]) existIp.get("ENDADDRESS"),
	  	       network.getIpVersion()));
	        assertEquals(network.getCustomPropertiesJsonVal(),
	        		existIp.get("CUSTOM_PROPERTIES"));
	        return ipId;
		}

		@Test
	    //@DatabaseSetup("ip-delete.xml")
	    @DatabaseTearDown("teardown.xml")
	    public void test_delete_ip_and_status() throws Exception {
			super.databaseSetupWithBinaryColumns("ip-delete.xml");
	    	Network network = new Network();
	    	network.setId(1L);
	    	updateDao.delete(network);
	    	updateDao.deleteStatus(network);
	        assertTablesForUpdate("teardown.xml", "RDAP_IP",
	                "RDAP_IP_STATUS");
	    }

	    @Test
	    @DatabaseTearDown("teardown.xml")
	    public void test_update_ip_and_status() throws Exception {
	    	super.databaseSetupWithBinaryColumns("ip-delete.xml");
	        List<Map<?, ?>> resultList =
	                getTableDataForSql("RDAP_IP",
	                        "select * from RDAP_IP where HANDLE='h1'");
	        assertTrue(resultList.size() > 0);
	        Map<?, ?> existIp = resultList.get(0);
	        Integer ipId = (Integer) existIp.get("IP_ID");
	        assertNotNull(ipId);
	        Network network = new Network();
	        network.setId(Long.valueOf(ipId));
	    	network.setHandle("h1");
	    	network.setCidr("2001:0DB8:0000:0000:0000:0000:1428:0000");
	    	network.setEndAddress("::2001:6a8:0:1");
	    	network.setStartAddress("::2001:6a8:0:0");
	    	network.setIpVersion(IpVersion.V6);
	    	//network.setParentHandle("ip-200-1");
	    	network.setName("test-v4");
	    	network.setPort43("port43");
	    	network.setCountry("FR");	 
	    	network.setLang("en");
	        network.setType("located");	  
	        List<String> status = new ArrayList<String>();
	        status.add("renew prohibited");
	        status.add("transfer prohibited");
	        status.add("delete prohibited");
	        network.setStatus(status);
	        Map<String, String> customProperties = new HashMap<String, String>();
	        customProperties.put("customKey3", "customValue3");
	        network.setCustomProperties(customProperties);
	        network.setCustomPropertiesJsonVal(JsonUtil
	                .serializeMap(customProperties));
	        updateDao.update(network);
	        updateDao.updateStatus(network);
	         ipId = assertIp(network);
	        assertStatus(network, ipId);
	    }

	    private void assertStatus(Network network,int ipId) throws Exception {
	        List<Map<?, ?>> resultList1 =
	                getTableDataForSql("RDAP_IP_STATUS",
	                        "select * from RDAP_IP_STATUS"
	                        + " where IP_ID = " + ipId);
	        assertEquals(network.getStatus().size(), resultList1.size());
	    }
}
