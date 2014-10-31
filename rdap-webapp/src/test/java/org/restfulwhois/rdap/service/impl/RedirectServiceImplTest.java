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
package org.restfulwhois.rdap.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.QueryParamHelper;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.DomainUtil;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
import org.restfulwhois.rdap.redirect.service.RedirectService;
import org.springframework.beans.factory.annotation.Autowired;

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
     * RedirectService.
     */
    @Autowired
    private RedirectService redirectService;

    /**
     * test query domain.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-redirect.xml")
    public
            void testQueryDomain() {
        String domainName = "cnnic.cn";
        String punyDomainName = DomainUtil.geneDomainPunyName(domainName);
        RedirectResponse redirect =
                redirectService.queryDomain(DomainQueryParam
                        .generateQueryParam(domainName, punyDomainName));
        assertNotNull(redirect);
        assertEquals("http://cnnic.cn/rdap", redirect.getUrl());
    }

    /**
     * test query autnum.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/autnum-redirect.xml")
    public
            void testQueryAutnum() {
        String autnumStr = "1";
        QueryParam queryParam = QueryParamHelper.buildQueryParam(autnumStr);
        RedirectResponse redirect = redirectService.queryAutnum(queryParam);
        assertNotNull(redirect);
        assertEquals("http://cnnic.cn/rdap", redirect.getUrl());
    }

    /**
     * test query network.
     */
    @Test
    // @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
            public
            void testQueryNetwork() {
        super.databaseSetupWithBinaryColumns("network-redirect.xml");
        QueryParam queryParam =
                QueryParamHelper.generateNetworkParam("1.0.0.0/8");
        RedirectResponse redirect = redirectService.queryIp(queryParam);
        assertNotNull(redirect);
        assertEquals("http://cnnic.cn/rdap", redirect.getUrl());
        // sub net
        queryParam = QueryParamHelper.generateNetworkParam("1.0.0.0/10");
        redirect = redirectService.queryIp(queryParam);
        assertNotNull(redirect);
    }

}
