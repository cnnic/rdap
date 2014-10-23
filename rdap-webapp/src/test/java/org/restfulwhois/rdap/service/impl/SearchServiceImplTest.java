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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.core.common.RdapProperties;
import org.restfulwhois.rdap.core.controller.support.QueryParser;
import org.restfulwhois.rdap.core.queryparam.DomainSearchParam;
import org.restfulwhois.rdap.core.service.SearchService;
import org.restfulwhois.rdap.search.bean.DomainSearch;
import org.restfulwhois.rdap.search.bean.EntitySearch;
import org.restfulwhois.rdap.search.bean.NameserverSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for SearchService.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class SearchServiceImplTest extends BaseTest {
    @Autowired
    private QueryParser queryParser;
    @Autowired
    private SearchService searchService;

    /**
     * test search domain.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/entity-search.xml")
    public void testSearchEntity() {
        String entityHandle = "truncated*";
        RdapProperties prop = new RdapProperties();
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 4L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        EntitySearch entitySearch = searchService.searchEntity(queryParser
                .parseEntityQueryParam(entityHandle, "handle"));
        assertNotNull(entitySearch);
        assertNotNull(entitySearch.getEntitySearchResults());
        assertEquals(4L, entitySearch.getEntitySearchResults().size());
        assertTrue(entitySearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 4L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 5L);
        entitySearch = searchService.searchEntity(queryParser
                .parseEntityQueryParam(entityHandle, "handle"));
        assertNotNull(entitySearch);
        assertNotNull(entitySearch.getEntitySearchResults());
        assertEquals(4L, entitySearch.getEntitySearchResults().size());
        assertTrue(entitySearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 4L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 6L);
        entitySearch = searchService.searchEntity(queryParser
                .parseEntityQueryParam(entityHandle, "handle"));
        assertNotNull(entitySearch);
        assertNotNull(entitySearch.getEntitySearchResults());
        assertEquals(4L, entitySearch.getEntitySearchResults().size());
        assertTrue(entitySearch.getTruncatedInfo().getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 6L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        entitySearch = searchService.searchEntity(queryParser
                .parseEntityQueryParam(entityHandle, "handle"));
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
    public void testSearchDomain() {
        String domainName = "truncated*.cn";
        RdapProperties prop = new RdapProperties();
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        DomainSearchParam  domainSearchParam = 
        		(DomainSearchParam)queryParser.parseDomainSearchParam(domainName, domainName);
        domainSearchParam.setSearchByParam("name");
        DomainSearch domainSearch = searchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 5L);
        domainSearch = searchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 6L);
        domainSearch = searchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getTruncatedInfo().getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 6L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        domainSearch = searchService.searchDomain(domainSearchParam);
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(6L, domainSearch.getDomainSearchResults().size());
        assertEquals("authorization",domainSearch.getTruncatedInfo().getReasonTypeShortName());        
    }
    
    /**
     * test search nameserver.
     */
    @Test
     @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/nameserver-search-page.xml")
    public void testSearchNameserver() {
        String nsName = "ns.truncated*.cn";
        RdapProperties prop = new RdapProperties();

        long sizeHigh = 3L;
        long sizeLow = 2L;
        long sizeLimit = 6L;
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeHigh);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeLow);
        NameserverSearch nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeHigh, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeLow);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeLow);
        nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeLow, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getTruncatedInfo().getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeLow);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeHigh);
        nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeLow, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getTruncatedInfo().getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", sizeLimit);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", sizeLow);
        nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(sizeLimit, nsSearch.getNameserverSearchResults().size());
        assertEquals("authorization",nsSearch.getTruncatedInfo().getReasonTypeShortName());
    }
    
}
