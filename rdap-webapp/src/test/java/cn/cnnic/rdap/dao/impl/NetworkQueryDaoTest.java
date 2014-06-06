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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.dao.QueryDao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for network DAO.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class NetworkQueryDaoTest extends BaseTest {
    /**
     * queryParser.
     */
    @Autowired
    private QueryParser queryParser;
    /**
     * networkQueryDao.
     */
    @Autowired
    private QueryDao<Network> networkQueryDao;

    /**
     * test query exist v4 network.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "networkV4.xml")
    public void testQueryExistV4Network() {
        Long domainId = 1L;
        List<Network> networks =
                networkQueryDao.queryAsInnerObjects(domainId, ModelType.DOMAIN);
        assertNotNull(networks);
        assertEquals(1, networks.size());
        Network network = networks.get(0);
        assertEquals("h1", network.getHandle());
        assertEquals("1.0.0.0", network.getStartAddress());
        assertEquals("1.255.255.255", network.getEndAddress());
        assertEquals(IpVersion.V4, network.getIpVersion());
        assertEquals("APNIC-1", network.getName());
        assertEquals("Allocated to APNIC", network.getType());
        assertEquals("US", network.getCountry());
        assertEquals("h2", network.getParentHandle());
        assertEquals("port43", network.getPort43());
        assertEquals("zh", network.getLang());
        // status
        List<String> statusList = network.getStatus();
        assertThat(statusList,
                CoreMatchers.hasItems("validated", "update prohibited"));
        // events
        List<Event> events = network.getEvents();
        assertNotNull(events);
        assertEquals(events.size(), 1);
        Event event = events.get(0);
        assertNotNull(event);
        assertEquals(event.getEventAction(), "action1");
        assertEquals(event.getEventActor(), "jiashuo");
        assertEquals(event.getEventDate(), "2014-01-01T00:01:01Z");
        // links
        List<Link> networkLinks = network.getLinks();
        assertNotNull(networkLinks);
        assertEquals(1, networkLinks.size());
        Link networkLink = networkLinks.get(0);
        assertNotNull(networkLink);
        assertEquals("http://networklink", networkLink.getValue());
        assertEquals("http://networklink", networkLink.getHref());
        // remarks
        List<Remark> remarks = network.getRemarks();
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
    }

    /**
     * test query exist v6 network.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "networkV6.xml")
    public void testQueryExistV6Network() {
        Long domainId = 1L;
        List<Network> networks =
                networkQueryDao.queryAsInnerObjects(domainId, ModelType.DOMAIN);
        assertNotNull(networks);
        assertEquals(1, networks.size());
        Network network = networks.get(0);
        assertEquals("h1", network.getHandle());
        assertEquals("0:0:0:0:2001:6a8:0:1", network.getStartAddress());
        assertEquals("2001:db8:85a3:0:2001:6a8:0:2", network.getEndAddress());
        assertEquals(IpVersion.V6, network.getIpVersion());
        assertEquals("APNIC-1", network.getName());
        assertEquals("Allocated to APNIC", network.getType());
        assertEquals("US", network.getCountry());
        assertEquals("h2", network.getParentHandle());
        assertEquals("port43", network.getPort43());
        assertEquals("zh", network.getLang());
        // status
        List<String> statusList = network.getStatus();
        assertThat(statusList,
                CoreMatchers.hasItems("validated", "update prohibited"));
        // events
        List<Event> events = network.getEvents();
        assertNotNull(events);
        assertEquals(events.size(), 1);
        Event event = events.get(0);
        assertNotNull(event);
        assertEquals(event.getEventAction(), "action1");
        assertEquals(event.getEventActor(), "jiashuo");
        assertEquals(event.getEventDate(), "2014-01-01T00:01:01Z");
        // links
        List<Link> networkLinks = network.getLinks();
        assertNotNull(networkLinks);
        assertEquals(1, networkLinks.size());
        Link networkLink = networkLinks.get(0);
        assertNotNull(networkLink);
        assertEquals("http://networklink", networkLink.getValue());
        assertEquals("http://networklink", networkLink.getHref());
        // remarks
        List<Remark> remarks = network.getRemarks();
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
    }

    /**
     * test query non exist network.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "networkV6.xml")
    public void testQueryNotExistNetwork() {
        final Long nonExistDomainId = 1000L;
        List<Network> networks =
                networkQueryDao.queryAsInnerObjects(nonExistDomainId,
                        ModelType.DOMAIN);
        assertNotNull(networks);
        assertTrue(networks.size() == 0);
    }
}
