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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.bean.Autnum;
import org.restfulwhois.rdap.bean.Domain;
import org.restfulwhois.rdap.bean.Entity;
import org.restfulwhois.rdap.bean.Nameserver;
import org.restfulwhois.rdap.bean.Network;
import org.restfulwhois.rdap.common.RdapProperties;
import org.restfulwhois.rdap.controller.support.QueryParser;
import org.restfulwhois.rdap.service.AccessControlManager;
import org.restfulwhois.rdap.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for AccessControlManagerImpl.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class AccessControlManagerImplTest extends BaseTest {
    @Autowired
    private AccessControlManager accessControlManager;
    @Autowired
    private QueryService queryService;
    @Autowired
    private QueryParser queryParser;
    /**
     * test for exist entry.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/acl.xml")
    public void testHasEntry() {
        Domain domain = new Domain();
        domain.setId(1L);
        /**
         * anonymous user
         */
        assertFalse(accessControlManager.hasPermission(domain));
        /**
         * userId with 1
         */
        super.setUserIdToPrincipal(1L);
        assertTrue(accessControlManager.hasPermission(domain));
        /**
         * userId with 10000000
         */
        super.setUserIdToPrincipal(10000000L);
        assertFalse(accessControlManager.hasPermission(domain));
    }

    /**
     * test for hasEntry for non-exist entry.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/acl.xml")
    public void testHasEntryForNonExistEntry() {
        Domain domain = new Domain();
        /**
         * domain with id 2000000 has non acl entry.
         */
        domain.setId(2000000L);
        assertTrue(accessControlManager.hasPermission(domain));
    }
    
    /**
     * test for .
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/domain-search-acl.xml")
    public void testDomainInnerObjectHasPermission() {
        RdapProperties prop = new RdapProperties();
      //set needCheckAccess true
        ReflectionTestUtils.setField(prop, 
              "accessControlForInnerObjectForSearch", false);
         String domainName = 
     "0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa";
          Domain domain =
                  queryService.queryDomain(queryParser.parseDomainQueryParam(
                          domainName, domainName)); 
          assertNotNull(domain.getNetwork());
          assertNotNull(domain.getnameServers()); 
          Nameserver ns = domain.getnameServers().get(0);
          assertEquals(2L, domain.getnameServers().size());
          assertNotNull(domain.getEntities()); 
          assertEquals(1L, domain.getEntities().size()); 
          Entity entity = domain.getEntities().get(0);
          accessControlManager.innerObjectHasPermission(domain);
          assertNotNull(domain.getNetwork());
          assertNotNull(domain.getnameServers()); 
          assertEquals(2L, domain.getnameServers().size());
          assertNotNull(domain.getEntities()); 
          assertEquals(1L, domain.getEntities().size());
          //set needCheckAccess true
          ReflectionTestUtils.setField(prop, 
               "accessControlForInnerObjectForSearch", true);
          accessControlManager.innerObjectHasPermission(domain);
          assertNull(domain.getNetwork());    
          assertEquals(1L, domain.getnameServers().size()); 
          assertFalse(domain.getnameServers().contains(ns));
          assertEquals(0L, domain.getEntities().size());  
          assertFalse(domain.getEntities().contains(entity));
          assertNull(domain.getNetwork());  
    }
    /**
     * test for .
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/nameserver-search-acl.xml")
    public void testNameServerInnerObjectHasPermission() {
         String nsName =  "ns.cnnic.cn";
          Nameserver ns =
          queryService.queryNameserver(queryParser.parseNameserverQueryParam(
                  nsName, nsName));
          
          assertNotNull(ns.getEntities()); 
          assertEquals(1L, ns.getEntities().size()); 
          Entity entity = ns.getEntities().get(0);
          accessControlManager.innerObjectHasPermission(ns);
          assertFalse(ns.getEntities().contains(entity));
          assertEquals(0L, ns.getEntities().size());  
         
    }
    /**
     * test for .
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/entity-search-acl.xml")
    
    public void testEntityInnerObjectHasPermission() {
        String handle = "h1";
        Entity entity =
                queryService.queryEntity(queryParser.parseQueryParam(handle));
          assertNotNull(entity.getAutnums());
          assertNotNull(entity.getNetworks()); 
          assertEquals(4L, entity.getAutnums().size());
          assertEquals(3L, entity.getNetworks().size());
          assertEquals(1L, entity.getEntities().size()); 
          Autnum autnum = entity.getAutnums().get(0);
          Network network = entity.getNetworks().get(0);
          Entity entitySub = entity.getEntities().get(0);
          accessControlManager.innerObjectHasPermission(entity);           
          assertEquals(3L, entity.getAutnums().size()); 
          assertEquals(2L, entity.getNetworks().size()); 
          assertEquals(0L, entity.getEntities().size());
          
          assertFalse(entity.getAutnums().contains(autnum)); 
          assertFalse(entity.getNetworks().contains(network));
          assertFalse(entity.getEntities().contains(entitySub));
           
    }
}
