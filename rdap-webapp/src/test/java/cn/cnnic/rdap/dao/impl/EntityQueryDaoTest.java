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
package cn.cnnic.rdap.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.Assert;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntityAddress;
import cn.cnnic.rdap.bean.EntityTel;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.PublicId;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.common.RdapProperties;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.dao.QueryDao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for entity DAO.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class EntityQueryDaoTest extends BaseTest {
    /**
     * queryParser.
     */
    @Autowired
    private QueryParser queryParser;
    /**
     * domainQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;

    /**
     * test query exist domain.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("entity.xml")
    public void testQueryExistEntity() {
        String entityHandle = "h1";
        Entity entity =
                entityQueryDao.query(queryParser.parseQueryParam(entityHandle));
        assertNotNull(entity);
        assertEquals(entityHandle, entity.getHandle());
        assertEquals("individual", entity.getKind());
        assertEquals("john", entity.getFn());
        assertEquals("john@gmail.com", entity.getEmail());
        assertEquals("CEO", entity.getTitle());
        assertEquals("org", entity.getOrg());
        assertEquals("http://john.com", entity.getUrl());
        assertEquals("whois.example.net", entity.getPort43());
        // status
        List<String> statusList = entity.getStatus();
        assertThat(statusList,
                CoreMatchers.hasItems("validated", "update prohibited"));
        // roles
        List<String> roleList = entity.getRoles();
        assertThat(roleList,
                CoreMatchers.hasItems("registrant", "administrative"));
        // telephones
        List<EntityTel> telephones = entity.getTelephones();
        assertNotNull(telephones);
        assertEquals(2, telephones.size());
        EntityTel tel = telephones.get(0);
        assertNotNull(tel);
        assertEquals("home;voice", tel.getTypes());
        assertEquals("+1-555-555-1234", tel.getGlobalNumber());
        assertEquals("1234", tel.getExtNumber());
        // addresses
        List<EntityAddress> addresses = entity.getAddresses();
        assertNotNull(addresses);
        assertEquals(2, addresses.size());
        EntityAddress address = addresses.get(0);
        assertEquals("home;work", address.getTypes());
        assertEquals("post office Box", address.getPoBox());
        assertEquals("apartment addr", address.getExtendedAddress());
        assertEquals("123 Wall St.", address.getStreetAddress());
        assertEquals("New York", address.getLocality());
        assertEquals("NY", address.getRegion());
        assertEquals("12345", address.getPostalCode());
        assertEquals("USA", address.getCountry());
        // events
        List<Event> events = entity.getEvents();
        assertNotNull(events);
        assertEquals(events.size(), 1);
        Event event = events.get(0);
        assertNotNull(event);
        assertEquals(event.getEventAction(), "action1");
        assertEquals(event.getEventActor(), "jiashuo");
        assertEquals(event.getEventDate(), "2014-01-01T00:01:01Z");
        // links
        List<Link> domainLinks = entity.getLinks();
        assertNotNull(domainLinks);
        assertEquals(1, domainLinks.size());
        Link domainLink = domainLinks.get(0);
        assertNotNull(domainLink);
        assertEquals("http://domainlink", domainLink.getValue());
        assertEquals("http://domainlink", domainLink.getHref());
        // publicId
        List<PublicId> publicIds = entity.getPublicIds();
        assertNotNull(publicIds);
        assertEquals(1, publicIds.size());
        PublicId publicId = publicIds.get(0);
        assertEquals("identifier", publicId.getIdentifier());
        assertEquals("type", publicId.getType());
        // remarks
        List<Remark> remarks = entity.getRemarks();
        assertNotNull(remarks);
        assertTrue(remarks.size() > 0);
        Remark remark = remarks.get(0);
        assertNotNull(remark);
        assertEquals("Terms of Use", remark.getTitle());
        assertThat(remark.getDescription(),
                CoreMatchers.hasItems("description1", "description2"));
        List<Link> links = remark.getLinks();
        assertNotNull(links);
        assertEquals(1, links.size());
        Link link = links.get(0);
        assertNotNull(link);
        assertEquals("http://example.com/context_uri", link.getValue());
        // networks
        List<Network> networks = entity.getNetworks();
        assertNotNull(networks);
        assertTrue(networks.size() > 0);
        Network network = networks.get(0);
        assertNotNull(network);
        assertEquals("h1", network.getHandle());
        assertEquals("1.0.0.0", network.getStartAddress());
        assertEquals("1.255.255.255", network.getEndAddress());
        assertEquals("US", network.getCountry());
        // autnums
        List<Autnum> autnums = entity.getAutnums();
        Assert.notNull(autnums);
        assertTrue(autnums.size() > 0);
        Autnum autnum = autnums.get(0);
        Assert.notNull(autnum);
        assertEquals(autnum.getCountry(), "zh");
        assertEquals(autnum.getEndAutnum().longValue(), 10L);
        assertEquals(autnum.getLang(), "cn");
        assertEquals(autnum.getName(), "name1");
        List<String> autnumStatusList = autnum.getStatus();
        assertThat(autnumStatusList, CoreMatchers.hasItems("validated"));
    }

    /**
     * test query exist domain.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("entity.xml")
    public void testQueryTruncated() {
        String entityHandle = "h1";
        RdapProperties prop = new RdapProperties();
        // not truncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 5L);
        Entity entity =
                entityQueryDao.query(queryParser.parseQueryParam(entityHandle));
        List<Network> networks = entity.getNetworks();
        assertNotNull(networks);
        assertEquals(3, networks.size());
        List<Autnum> autnums = entity.getAutnums();
        Assert.notNull(autnums);
        assertEquals(4, autnums.size());
        assertNull(entity.getResultsTruncated());
        // not truncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 4L);
        entity =
                entityQueryDao.query(queryParser.parseQueryParam(entityHandle));
        assertNull(entity.getResultsTruncated());
        // truncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 3L);
        entity =
                entityQueryDao.query(queryParser.parseQueryParam(entityHandle));
        assertTrue(entity.getResultsTruncated());
        // truncated
        ReflectionTestUtils.setField(prop, "maxsizeSearch", 2L);
        entity =
                entityQueryDao.query(queryParser.parseQueryParam(entityHandle));
        assertTrue(entity.getResultsTruncated());
    }

    /**
     * test query exist domain.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("entity-as-inner.xml")
    public void testQueryAsInnerObjects() {
        // entity - entities
        List<Entity> entities =
                entityQueryDao.queryAsInnerObjects(2L, ModelType.ENTITY);
        assertEquals(3, entities.size());
        // domain - entities
        entities = entityQueryDao.queryAsInnerObjects(3L, ModelType.DOMAIN);
        assertEquals(3, entities.size());
        Entity domainEntity = entities.get(0);
        List<String> roleList = domainEntity.getRoles();
        assertEquals(2, roleList.size());
        assertThat(roleList,
                CoreMatchers.hasItems("registrant", "administrative"));
        // check roles:include all roles
        Entity entity = entityQueryDao.query(queryParser.parseQueryParam("h1"));
        assertNotNull(entity);
        roleList = entity.getRoles();
        assertEquals(3, roleList.size());
        assertThat(roleList, CoreMatchers.hasItems("registrar", "billing",
                "registrant"));
    }

    /**
     * test query non exist entity.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("entity.xml")
    public void testQueryNotExistEntity() {
        Entity entity =
                entityQueryDao.query(queryParser
                        .parseQueryParam("non-exist-entity-handle"));
        assertNull(entity);
    }

}
