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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.QueryParamHelper;
import org.restfulwhois.rdap.common.model.Autnum;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.support.RdapProperties;
import org.restfulwhois.rdap.core.autnum.service.AutnumService;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.core.domain.service.DomainQueryService;
import org.restfulwhois.rdap.core.entity.service.EntityQueryService;
import org.restfulwhois.rdap.core.nameserver.service.NameserverQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for NameserverServiceImpl.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class QueryServiceImplTest extends BaseTest {
    @Autowired
    private AutnumService asService;
    @Autowired
    private DomainQueryService domainService;
    @Autowired
    private NameserverQueryService nameserverService;
    @Autowired
    private EntityQueryService entityService;

    /**
     * test query exist entity.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/entity.xml")
    public void testTldInThisRegistry() {
        String domainName = "cnnic.cn";
        RdapProperties prop = new RdapProperties();
        ReflectionTestUtils.setField(prop, "inTlds", "cn");
        ReflectionTestUtils.setField(prop, "notInTlds", "edu.cn");
        assertTrue(domainService.tldInThisRegistry(DomainQueryParam
                .generateQueryParam(domainName, domainName)));
        domainName = "cnnic.edu.cn";
        assertFalse(domainService.tldInThisRegistry(DomainQueryParam
                .generateQueryParam(domainName, domainName)));
        domainName = "cnnic.com";
        assertFalse(domainService.tldInThisRegistry(DomainQueryParam
                .generateQueryParam(domainName, domainName)));
        // set multi tlds
        ReflectionTestUtils.setField(prop, "inTlds", "cn;org");
        ReflectionTestUtils.setField(prop, "notInTlds", "com;edu.cn");
        domainName = "cnnic.cn";
        assertTrue(domainService.tldInThisRegistry(DomainQueryParam
                .generateQueryParam(domainName, domainName)));
        domainName = "cnnic.edu.cn";
        assertFalse(domainService.tldInThisRegistry(DomainQueryParam
                .generateQueryParam(domainName, domainName)));
        domainName = "cnnic.com";
        assertFalse(domainService.tldInThisRegistry(DomainQueryParam
                .generateQueryParam(domainName, domainName)));
    }

    /**
     * test query exist entity.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/entity.xml")
    public void testQueryEntity() {
        String handle = "h1";
        Entity entity =
                entityService.queryEntity(QueryParamHelper
                        .buildQueryParam(handle));
        Assert.notNull(entity);
        assertEquals(handle, entity.getHandle());
        assertEquals("individual", entity.getKind());
        assertEquals("john", entity.getFn());
        assertEquals("john@gmail.com", entity.getEmail());
        assertEquals("CEO", entity.getTitle());
        assertEquals("org", entity.getOrg());
        assertEquals("http://john.com", entity.getUrl());
        assertEquals("whois.example.net", entity.getPort43());
    }

    /**
     * test query exist autnum
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/autnum.xml")
    public void testQueryAutnum() {
        String autnumStr = "1";
        Autnum autnum =
                asService.queryAutnum(QueryParamHelper
                        .buildQueryParam(autnumStr));
        Assert.notNull(autnum);
        assertEquals(autnum.getId(), Long.valueOf(autnumStr));
        assertEquals(autnum.getCountry(), "zh");
        assertEquals(autnum.getEndAutnum().longValue(), 10L);
        assertEquals(autnum.getLang(), "cn");
        assertEquals(autnum.getName(), "name1");
        List<String> statusList = autnum.getStatus();
        assertThat(statusList, CoreMatchers.hasItems("validated"));
    }

    /**
     * test query domain.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testQueryDomain() {
        super.databaseSetupWithBinaryColumns("domain.xml");
        String domainName = "cnnic.cn";
        Domain domain =
                domainService.queryDomain(DomainQueryParam.generateQueryParam(
                        domainName, domainName));
        assertNotNull(domain);
    }

}
