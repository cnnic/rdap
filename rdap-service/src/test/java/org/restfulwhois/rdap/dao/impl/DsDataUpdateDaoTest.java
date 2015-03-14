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
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.DsData;
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
public class DsDataUpdateDaoTest extends BaseTest {

	    @Autowired
	    private UpdateDao<DsData, DsDataDto> updateDao;

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml") 
	    @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/dsData-update.xml")
	    public void testcreateDsData() throws Exception {	    	
	    	SecureDns secureDns = new SecureDns();
	        secureDns.setId(1L);
	    	List<DsDataDto> dsDataList = new ArrayList<DsDataDto>();
	    	DsDataDto dsData = new DsDataDto();
	    	dsData.setAlgorithm(1);
	    	dsData.setDigest("1-ds-dt-200-50");
	    	dsData.setDigestType(1);
	    	dsData.setKeyTag(1);	    	
	    	//link
	    	List<LinkDto> linkList = createLinkList();	    	
	    	dsData.setLinks(linkList);
	    
	    	dsDataList.add(dsData);
	        updateDao.saveAsInnerObjects(secureDns, dsDataList);
	    }
	    public static List<LinkDto> createLinkList() {
	    	List<LinkDto> linkList = new ArrayList<LinkDto>();
	    	List<String> hreflang = new ArrayList<String>();
	    	hreflang.add("en");
	    	hreflang.add("zh");
	    	LinkDto link = new LinkDto();
	    	link.setHref("http://sina.com.cn");
	    	link.setMedia("screen");
	    	link.setRel("up");
	    	link.setTitle("little title");
	    	link.setType("application/rdap+json");
	    	link.setValue("http://sina.com.cn");
	    	link.setHreflang(hreflang);
	    	linkList.add(link);
	    	return linkList;
	    }
	    
	    @Test
        @DatabaseSetup("dsData-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/dsData-empty.xml")
        public void testDeleteDsData() throws Exception {
	    	 SecureDns secureDns = new SecureDns();
	         secureDns.setId(1L);
            updateDao.deleteAsInnerObjects(secureDns);
            
        }
	    
	    @Test
        @DatabaseSetup("dsData-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
             assertionMode = DatabaseAssertionMode.NON_STRICT,
             value = "classpath:/org/restfulwhois/rdap/dao/impl/dsData-update.xml")
        public void testUpdateDsData() throws Exception {
	    	SecureDns secureDns = new SecureDns();
	        secureDns.setId(1L);
	    	List<DsDataDto> dsDataList = new ArrayList<DsDataDto>();
	    	DsDataDto dsData = new DsDataDto();
	    	dsData.setAlgorithm(1);
	    	dsData.setDigest("1-ds-dt-200-50");
	    	dsData.setDigestType(1);
	    	dsData.setKeyTag(1);	    	
	    	//link
	    	List<LinkDto> linkList = createLinkList();	    	
	    	dsData.setLinks(linkList);
	    	dsDataList.add(dsData);
            updateDao.updateAsInnerObjects(secureDns, dsDataList);
            
        }
}
