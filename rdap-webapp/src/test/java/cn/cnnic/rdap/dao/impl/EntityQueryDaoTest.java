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

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.PublicId;
import cn.cnnic.rdap.bean.Remark;
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
        // status
        List<String> roleList = entity.getRoles();
        assertThat(roleList,
                CoreMatchers.hasItems("registrant", "administrative"));
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
        assertEquals("h1",network.getHandle());
        assertEquals(16777216,network.getStartAddress());
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
