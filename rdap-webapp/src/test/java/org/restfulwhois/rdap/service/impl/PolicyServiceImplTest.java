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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.core.common.model.DsData;
import org.restfulwhois.rdap.core.common.model.Event;
import org.restfulwhois.rdap.core.common.model.Link;
import org.restfulwhois.rdap.core.common.model.Notice;
import org.restfulwhois.rdap.core.common.model.Remark;
import org.restfulwhois.rdap.core.common.model.SecureDns;
import org.restfulwhois.rdap.core.common.model.Variant;
import org.restfulwhois.rdap.core.common.model.Variants;
import org.restfulwhois.rdap.core.domain.model.Domain;
import org.restfulwhois.rdap.core.entity.model.Entity;
import org.restfulwhois.rdap.core.ip.model.IPAddress;
import org.restfulwhois.rdap.core.nameserver.model.Nameserver;
import org.restfulwhois.rdap.filters.queryFilter.service.CustomColumnPolicyService;
import org.springframework.beans.factory.annotation.Autowired;

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
    private CustomColumnPolicyService customColumnPolicyService;

    /**
     * test policy.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/policy.xml")
    public void testSetPolicyOneWrap() {
        customColumnPolicyService.init();

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

        Event event = new Event();
        event.setId(id);
        event.setHandle(handle);
        event.setLang(lang);
        List<Event> events = new ArrayList<Event>();
        events.add(event);
        ns.setEvents(events);

        IPAddress ipAddr = new IPAddress();
        ipAddr.setId(id);
        ipAddr.setHandle(handle);
        ipAddr.setLang(lang);
        ns.setIpAddresses(ipAddr);

        Link link = new Link();
        List<Link> links = new ArrayList<Link>();
        link.setId(id);
        link.setHandle(handle);
        link.setLang(lang);
        links.add(link);
        ns.setLinks(links);

        Notice notice = new Notice();
        List<Notice> notices = new ArrayList<Notice>();
        notice.setId(id);
        notice.setHandle(handle);
        notice.setLang(lang);
        notices.add(notice);
        ns.setNotices(notices);

        Remark remark = new Remark();
        List<Remark> remarks = new ArrayList<Remark>();
        remark.setId(id);
        remark.setHandle(handle);
        remark.setLang(lang);
        remarks.add(remark);
        ns.setRemarks(remarks);

        final String port43 = "port43";
        ns.setPort43(port43);

        customColumnPolicyService.applyPolicy(ns);

        assertNotNull(ns);
        assertEquals(ns.getLdhName(), null);
        assertEquals(ns.getId(), null);
        assertEquals(ns.getHandle(), null);
        assertEquals(ns.getUnicodeName(), null);
        assertEquals(ns.getEntities(), null);
        assertEquals(ns.getPort43(), null);
        assertEquals(ns.getEvents(), null);
        assertEquals(ns.getIpAddresses(), null);
        assertEquals(ns.getNotices(), null);
        assertEquals(ns.getLinks(), null);
        assertEquals(ns.getRemarks(), null);

        Domain domain = setDomainValue(ns);
        applyPolicySingleWrapDomain(domain);

        Entity entityWrap = setEntityValue(entities);
        applyPolicySingleWrapEntity(entityWrap);

        clearPolicy();
    }

    private void clearPolicy() {
        customColumnPolicyService.clearPolicy();
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:org/restfulwhois/rdap/dao/impl/policyTwoWrap.xml")
    public
            void testSetPolicyMultiWrap() {
        customColumnPolicyService.init();

        Domain domain = setDomainValue(null);
        applyPolicyMultiWrapDomain(domain);

        List<Entity> entities = setEntities();
        Entity entityWrap = setEntityValue(entities);
        applyPolicyMutliWrapEntity(entityWrap);

        clearPolicy();
    }

    /**
     * apply policy to domain with single Wrap.
     * 
     * @param domain
     *            object to apply policy.
     */
    private void applyPolicySingleWrapDomain(Domain domain) {
        customColumnPolicyService.applyPolicy(domain);
        assertEquals(domain.getNameservers(), null);
        assertEquals(domain.getVariants(), null);
        assertEquals(domain.getSecureDns(), null);
        assertEquals(domain.getPublicIds(), null);
    }

    /**
     * apply policy to entity with single Wrap.
     * 
     * @param entity
     *            object to apply policy.
     */
    private void applyPolicySingleWrapEntity(Entity entity) {
        customColumnPolicyService.applyPolicy(entity);
        assertNotNull(entity);
        assertEquals(entity.getAsEventActor(), null);
        assertEquals(entity.getEntities(), null);
    }

    /**
     * apply policy to entity with multi Wrap.
     * 
     * @param entity
     *            object to apply policy.
     */
    private void applyPolicyMutliWrapEntity(Entity entity) {
        customColumnPolicyService.applyPolicy(entity);
        List<Event> asEventActor = entity.getAsEventActor();
        assertNotNull(entity);
        for (Event event : asEventActor) {
            assertEquals(event.getEventAction(), null);
        }
    }

    /**
     * set domain value.
     * 
     * @param ns
     *            add ns for domain.
     * @return domain to set policy.
     */
    private Domain setDomainValue(Nameserver ns) {
        // domain
        Domain domain = new Domain();
        final Long id = 1L;
        domain.setId(id);
        final String handle = "h1";
        domain.setHandle(handle);
        final String lang = "en";
        domain.setLang(lang);
        final String ldhName = "ldhName";
        domain.setLdhName(ldhName);
        final String unicode = "unicodeName";
        domain.setUnicodeName(unicode);
        final String port43 = "port43";
        domain.setPort43(port43);
        // ns
        List<Nameserver> nameServers = new ArrayList<Nameserver>();
        nameServers.add(ns);
        domain.setNameservers(nameServers);
        // variant
        List<Variants> variants = new ArrayList<Variants>();
        Variants variantsOne = new Variants();
        variantsOne.setId(id);
        variantsOne.setHandle(handle);
        Variant variant = new Variant();
        String idnTable = "idnTable";
        variant.setIdnTable(idnTable);
        List<Variant> variantNames = new ArrayList<Variant>();
        variantNames.add(variant);
        variantsOne.setVariantNames(variantNames);
        variants.add(variantsOne);
        domain.setVariants(variants);
        // secDns
        SecureDns secureDns = new SecureDns();
        secureDns.setDelegationSigned(false);
        secureDns.setMaxSigLife(1);
        secureDns.setZoneSigned(true);
        // dsData
        DsData dsData = new DsData();
        Integer algorithm = new Integer(123456);
        dsData.setAlgorithm(algorithm);
        List<DsData> dsDatas = new ArrayList<DsData>();
        dsDatas.add(dsData);
        secureDns.setDsData(dsDatas);
        domain.setSecureDns(secureDns);

        List<Entity> entities = setEntities();
        domain.setEntities(entities);

        return domain;
    }

    private List<Entity> setEntities() {
        // entity
        final Long id = 1L;
        final String handle = "h1";
        final String lang = "en";
        final String ldhName = "ldhName";
        final String unicode = "unicodeName";
        final String port43 = "port43";

        Entity entity = new Entity();
        entity.setId(id);
        entity.setHandle(handle);
        entity.setLang(lang);
        entity.setPort43(port43);
        List<Entity> entities = new ArrayList<Entity>();
        entities.add(entity);
        return entities;
    }

    private Entity setEntityValue(List<Entity> entities) {
        // entities
        Entity objEntity = new Entity();
        final Long id = 1L;
        objEntity.setId(id);
        List<Event> asEventActor = new ArrayList<Event>();
        Event event = new Event();
        event.setEventAction("validate");
        asEventActor.add(event);
        objEntity.setAsEventActor(asEventActor);

        objEntity.setEntities(entities);
        return objEntity;
    }

    /**
     * apply policy to domain with multi Wrap.
     * 
     * @param domain
     *            object to apply policy.
     */
    private void applyPolicyMultiWrapDomain(Domain domain) {

        customColumnPolicyService.applyPolicy(domain);
        assertNotNull(domain);

        SecureDns secureDns = domain.getSecureDns();
        assertNotNull(secureDns);
        List<DsData> dsDatas = secureDns.getDsData();
        assertNotNull(dsDatas);
        for (DsData dsData : dsDatas) {
            assertNotNull(dsData);
            assertEquals(dsData.getAlgorithm(), null);
        }

        List<Entity> entities = domain.getEntities();
        for (Entity entity : entities) {
            assertNotNull(entity);
            assertEquals(entity.getId(), null);
            assertEquals(entity.getHandle(), null);
            assertEquals(entity.getLang(), null);
            assertEquals(entity.getPort43(), null);
        }
    }
}
