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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.JsonHelper;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.Network;
import org.restfulwhois.rdap.common.util.BeanUtil;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.common.util.JsonUtil;
import org.restfulwhois.rdap.common.validation.ServiceErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * 
 * @author zhanyq
 * 
 */
@SuppressWarnings("rawtypes")
public class NetworkUpdateControllerTest extends BaseTest {

    /**
     * ip  URI.
     */
    public static final String URI_IP_U = "/u/ip/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    final private String rdapJson = "application/rdap+json;charset=UTF-8";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public
            void test_ok_only_ip() throws Exception {
    	super.databaseSetupWithBinaryColumns("ip-delete.xml");
		IpDto network = new IpDto();
		network.setHandle("h1");
		network.setCidr("2001:0DB8:0000:0000:0000:0000:1428:0000");
		network.setEndAddress("::2001:6a8:0:1");
		network.setStartAddress("::2001:6a8:0:0");
		network.setIpVersion(IpVersion.V6.getName());
		network.setName("test-v6");
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
		String content = JsonHelper.serialize(network);
	        mockMvc.perform(
	                put(URI_IP_U + "h1").contentType(
	                MediaType.parseMediaType(rdapJson)).content(content))
	                .andExpect(status().isOk())
	                .andExpect(content().contentType(rdapJson));
		//change IpDto to NetWork        
		Network networkAssert = new Network();
		BeanUtil.copyProperties(network, networkAssert, "entities", "events",
				"remarks", "links");
		networkAssert.setIpVersion(IpVersion.getIpVersion(network.getIpVersion()));
		networkAssert.setCustomPropertiesJsonVal(JsonUtil.serializeMap(network
				.getCustomProperties()));
		int ipId = assertIp(networkAssert);
		assertStatus(networkAssert, ipId);     
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_invalid_handle_not_exist() throws Exception {
        String notExistHandle = "not-exist-handle";
        mockMvc.perform(
                put(URI_IP_U + notExistHandle).contentType(
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
    private void assertStatus(Network network,int ipId) throws Exception {
        List<Map<?, ?>> resultList1 =
                getTableDataForSql("RDAP_IP_STATUS",
                        "select * from RDAP_IP_STATUS"
                        + " where IP_ID = " + ipId);
        assertEquals(network.getStatus().size(), resultList1.size());
    }
    
    

}
