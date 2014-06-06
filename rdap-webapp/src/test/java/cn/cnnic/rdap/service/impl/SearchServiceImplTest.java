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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.DomainSearch;
import cn.cnnic.rdap.bean.NameserverSearch;
import cn.cnnic.rdap.common.RdapProperties;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.service.SearchService;

import com.github.springtestdbunit.annotation.DatabaseOperation;
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
     @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH,
            value = "classpath:cn/cnnic/rdap/dao/impl/domain-search-page.xml")
    public void testQueryDomain() {
        String domainName = "truncated*.cn";
        RdapProperties prop = new RdapProperties();
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        DomainSearch domainSearch = searchService.searchDomain(queryParser
                .parseDomainQueryParam(domainName, domainName));
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 5L);
        domainSearch = searchService.searchDomain(queryParser
                .parseDomainQueryParam(domainName, domainName));
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 6L);
        domainSearch = searchService.searchDomain(queryParser
                .parseDomainQueryParam(domainName, domainName));
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(5L, domainSearch.getDomainSearchResults().size());
        assertTrue(domainSearch.getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 6L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        domainSearch = searchService.searchDomain(queryParser
                .parseDomainQueryParam(domainName, domainName));
        assertNotNull(domainSearch);
        assertNotNull(domainSearch.getDomainSearchResults());
        assertEquals(6L, domainSearch.getDomainSearchResults().size());
        assertNull(domainSearch.getResultsTruncated());
    }
    
    /**
     * test search nameserver.
     */
    @Test
     @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH,
            value = "classpath:cn/cnnic/rdap/dao/impl/nameserver-search-page.xml")
    public void testSearchNameserver() {
        String nsName = "ns.truncated*.cn";
        RdapProperties prop = new RdapProperties();
        // resultsTruncated = true, batch<max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        NameserverSearch nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(5L, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getResultsTruncated());
        // resultsTruncated = true, batch=max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 5L);
        nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(5L, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getResultsTruncated());
        // resultsTruncated = true, batch>max
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 6L);
        nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(5L, nsSearch.getNameserverSearchResults().size());
        assertTrue(nsSearch.getResultsTruncated());
        // no resultsTruncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 6L);
        ReflectionTestUtils.setField(prop, "batchsizeSearch", 3L);
        nsSearch = searchService.searchNameserver(queryParser
                .parseNameserverQueryParam(nsName, nsName));
        assertNotNull(nsSearch);
        assertNotNull(nsSearch.getNameserverSearchResults());
        assertEquals(6L, nsSearch.getNameserverSearchResults().size());
        assertNull(nsSearch.getResultsTruncated());
    }
    
}
