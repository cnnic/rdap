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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.dao.QueryDao;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for event DAO
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class EventQueryDaoTest extends BaseTest {
    @Autowired
    private QueryParser queryParser;
    @Autowired
    private QueryDao<Event> eventQueryDao;

    /**
     * test query exist event
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("event.xml")
    public void testQueryExistEvent() {
        Long autnumId = 1L;
        List<Event> events = eventQueryDao.queryAsInnerObjects(autnumId,
                ModelType.AUTNUM);
        Assert.notNull(events);
        assertEquals(events.size(), 1);
        Event event = events.get(0);
        Assert.notNull(event);
        assertEquals(event.getEventAction(), "action1");
        assertEquals(event.getEventActor(), "jiashuo");
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        assertEquals(event.getEventDate(), "2014-01-01T00:01:01Z");
    }

    /**
     * test query non exist event
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("event.xml")
    public void testQueryNonExistEvent() {
        Long nonExistAutnumId = 10000L;
        List<Event> events = eventQueryDao.queryAsInnerObjects(
                nonExistAutnumId, ModelType.AUTNUM);
        Assert.notNull(events);
        assertEquals(events.size(), 0);
    }
}
