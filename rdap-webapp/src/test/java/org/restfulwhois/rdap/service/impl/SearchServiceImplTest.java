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
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.support.RdapProperties;
import org.restfulwhois.rdap.core.domain.model.DomainSearch;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchByDomainNameParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchParam;
import org.restfulwhois.rdap.core.domain.service.DomainSearchService;
import org.restfulwhois.rdap.core.entity.queryparam.EntitySearchByHandleParam;
import org.restfulwhois.rdap.core.entity.service.EntitySearchService;
import org.restfulwhois.rdap.core.nameserver.queryparam.NameserverSearchByNameParam;
import org.restfulwhois.rdap.core.nameserver.service.NameserverSearchService;
import org.restfulwhois.rdap.search.entity.bean.EntitySearch;
import org.restfulwhois.rdap.search.nameserver.bean.NameserverSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for NameserverSearchService.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class SearchServiceImplTest extends BaseTest {
    @Autowired
    private DomainSearchService domainSearchService;
    @Autowired
    private NameserverSearchService nameserverSearchService;
    @Autowired
    private EntitySearchService entitySearchService;

    /**
     * test search domain.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/entity-search.xml")
    public
            void testSearchEntity() {
        String entityHandle = "truncated*";
        EntitySearchByHandleParam queryParam = new EntitySearchByHandleParam();
        queryParam.setQ(entityHandle);

        RdapProperties prop = new RdapProperties();
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 4L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        EntitySearch entitySearch =
                entitySearchService.searchEntity(queryParam);
        assertNotNull(entitySearch);
        assertNotNull(entitySearch.getEntitySearchResults());
        assertEquals(4L, entitySearch.getEntitySearchResults().size());
        assertTrue(entitySearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 4L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 5L);
        entitySearch = entitySearchService.searchEntity(queryParam);
        assertNotNull(entitySearch);
        assertNotNull(entitySearch.getEntitySearchResults());
        assertEquals(4L, entitySearch.getEntitySearchResults().size());
        assertTrue(entitySearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 4L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 6L);
        entitySearch = entitySearchService.searchEntity(queryParam);
        assertNotNull(entitySearch);
        assertNotNull(entitySearch.getEntitySearchResults());
        assertEquals(4L, entitySearch.getEntitySearchResults().size());
        assertTrue(entitySearch.getTruncatedInfo().getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 6L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        entitySearch = entitySearchService.searchEntity(queryParam);
        assertNotNull(entitySearch);
        assertNotNull(entitySearch.getEntitySearchResults());
        assertEquals(5L, entitySearch.getEntitySearchResults().size());
        assertTrue(!entitySearch.getTruncatedInfo().getResultsTruncated());
    }

    /**
     * test search domain.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-search-page.xml")
    public
            void testSearchDomain() {
        String domainName = "truncated*.cn";
        RdapProperties prop = new RdapProperties();
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        DomainSearchParam domainSearchParam =
                new DomainSearchByDomainNameParam();
        domainSearchParam.setPunyName(domainName);
        DomainSearch domainSearch =
                domainSearchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 5L);
        domainSearch = domainSearchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 6L);
        domainSearch = domainSearchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getTruncatedInfo().getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 6L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        domainSearch = domainSearchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(6L, domainSearch.getDomainSearchResults().size());
    }

    /**
     * test search nameserver.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/nameserver-search-page.xml")
    public
            void testSearchNameserver() {
        String nsName = "ns.truncated*.cn";
        RdapProperties prop = new RdapProperties();
        NameserverSearchByNameParam queryParam =
                new NameserverSearchByNameParam();
        queryParam.setPunyName(nsName);

        long sizeHigh = 3L;
        long sizeLow = 2L;
        long sizeLimit = 6L;
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeHigh);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeLow);
        NameserverSearch nsSearch =
                nameserverSearchService.searchNameserver(queryParam);
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeHigh, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeLow);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeLow);
        nsSearch = nameserverSearchService.searchNameserver(queryParam);
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeLow, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeLow);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeHigh);
        nsSearch = nameserverSearchService.searchNameserver(queryParam);
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeLow, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getTruncatedInfo().getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeLimit);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeLow);
        nsSearch = nameserverSearchService.searchNameserver(queryParam);
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeLimit, nsSearch.getNameserverSearchResults().size());
    }

}
