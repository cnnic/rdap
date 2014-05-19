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
package cn.cnnic.rdap.service.impl;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.service.QueryService;

import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * Test for QueryServiceImpl
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class QueryServiceImplTest extends BaseTest {
	@Autowired
	private QueryParser queryParser;
	@Autowired
	private QueryService queryService;

	/**
	 * test query exist autnum
	 */
	@Test
	// @DatabaseTearDown("teardown.xml")
	@DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/autnum.xml")
	public void testQueryAutnum() {
		String autnumStr = "1";
		Autnum autnum = queryService.queryAutnum(queryParser
				.parseQueryParam(autnumStr));
		Assert.notNull(autnum);
		assertEquals(autnum.getId(), Long.valueOf(autnumStr));
		assertEquals(autnum.getCountry(), "zh");
		assertEquals(autnum.getEndAutnum().longValue(), 10L);
		assertEquals(autnum.getLang(), "cn");
		assertEquals(autnum.getName(), "name1");
		List<String> statusList = autnum.getStatus();
		assertThat(statusList, CoreMatchers.hasItems("validated"));
	}

	@Test
	public void testQueryDomain() {
		// TODO:add validation
		String domainName = "cnnic.cn";
		Domain domain = queryService.queryDomain(queryParser
				.parseQueryParam(domainName));
		assertNull(domain);
	}
}
