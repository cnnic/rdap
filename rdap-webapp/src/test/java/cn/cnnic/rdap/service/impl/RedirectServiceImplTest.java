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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.RedirectResponse;
import cn.cnnic.rdap.common.util.DomainUtil;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.service.RedirectService;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for RedirectServiceImpl.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RedirectServiceImplTest extends BaseTest {
    /**
     * queryParser.
     */
    @Autowired
    private QueryParser queryParser;
    /**
     * RedirectService.
     */
    @Autowired
    private RedirectService redirectService;

    /**
     * test query domain.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/domain-redirect.xml")
    public void testQueryDomain() {
        String domainName = "cnnic.cn";
        String punyDomainName = DomainUtil.geneDomainPunyName(domainName);
        RedirectResponse redirect =
                redirectService.queryDomain(queryParser.parseDomainQueryParam(
                        domainName, punyDomainName));
        assertNotNull(redirect);
        assertEquals("http://cnnic.cn/rdap", redirect.getUrl());
    }

    /**
     * test query autnum.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/autnum-redirect.xml")
    public void testQueryAutnum() {
        String autnumStr = "1";
        QueryParam queryParam = queryParser.parseQueryParam(autnumStr);
        RedirectResponse redirect = redirectService.queryAutnum(queryParam);
        assertNotNull(redirect);
        assertEquals("http://cnnic.cn/rdap", redirect.getUrl());
    }

    /**
     * test query network.
     */
    @Test
//    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/network-redirect.xml")
    public void testQueryNetwork() {
        QueryParam queryParam =
                queryParser.parseIpQueryParam("1.0.0.0", 0, IpVersion.V4);
        RedirectResponse redirect = redirectService.queryIp(queryParam);
        assertNotNull(redirect);
        assertEquals("http://cnnic.cn/rdap", redirect.getUrl());
    }

}
