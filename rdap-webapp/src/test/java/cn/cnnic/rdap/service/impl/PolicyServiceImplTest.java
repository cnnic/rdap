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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.service.PolicyControlService;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for PolicyServiceImpl.
 * 
 * @author weijunkai
 * 
 */
@SuppressWarnings("rawtypes")
public class PolicyServiceImplTest extends BaseTest {
    @Autowired
    private PolicyControlService policyService;

    /**
     * test policy.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/policy.xml")
    public void testSetPolicyOneWrap() {
        policyService.loadAllPolicyByList();
        Nameserver ns = new Nameserver();

        final Long id = 1L;
        ns.setId(id);
        final String handle = "h1";
        ns.setHandle(handle);
        final String lang = "en";
        ns.setLang(lang);
        final String ldhName = "ldhName";
        ns.setLdhName(ldhName);
        final String unicode = "unicodeName";
        ns.setUnicodeName(unicode);
        
        Entity entity = new Entity();
        entity.setId(id);
        entity.setHandle(handle);
        entity.setLang(lang);
        
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(entity);
        
        ns.setEntities(entities);
//        ns.setEvents(events);
//        ns.setIpAddresses(ipAddresses);
//        ns.setLinks(links);
//        ns.setNotices(notices);
        final String port43 = "port43";
        ns.setPort43(port43);
        
        final String strObj = "nameServer";
        policyService.applyPolicy(ns);
        
        assertNotNull(ns);
        assertEquals(ns.getLdhName(), null);
        assertEquals(ns.getId(), null);
        assertEquals(ns.getHandle(), null);
        assertEquals(ns.getEntities(), null);
        assertEquals(ns.getPort43(),null);
    }
    
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/policyTwoWrap.xml")
    public void testSetPolicyTwoWrap() {
        policyService.loadAllPolicyByList();
        Nameserver ns = new Nameserver();

        final Long id = 1L;
        ns.setId(id);
        final String handle = "h1";
        ns.setHandle(handle);
        final String lang = "en";
        ns.setLang(lang);
        final String ldhName = "ldhName";
        ns.setLdhName(ldhName);
        final String unicode = "unicodeName";
        ns.setUnicodeName(unicode);
        
        Entity entity = new Entity();
        entity.setId(id);
        entity.setHandle(handle);
        entity.setLang(lang);
        
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(entity);
        
        ns.setEntities(entities);
        final String port43 = "port43";
        entity.setPort43(port43);
        
        final String strObj = "entity";
        policyService.applyPolicy(entity);
        
        assertNotNull(ns);
        assertNotNull(entity);
        assertEquals(entity.getId(), null);
        assertEquals(entity.getHandle(), null);
        assertEquals(entity.getLang(), null);
//        assertEquals(entity.getEntities(), null);
        assertEquals(entity.getPort43(),null);
    }
}
