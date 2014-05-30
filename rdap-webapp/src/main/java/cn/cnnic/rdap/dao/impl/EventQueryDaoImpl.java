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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * remark query DAO
 * 
 * @author jiashuo
 * 
 */
@Repository
public class EventQueryDaoImpl extends AbstractQueryDao<Event> {
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;

    /**
     * query event as inner objects of other object.
     */
    @Override
    public List<Event> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        List<Event> events = queryWithoutInnerObjects(outerObjectId,
                outerModelType);
        queryAndSetInnerObjects(events);
        return events;
    }

    /**
     * query inner objects, and set it to event.
     * 
     * @param events
     */
    private void queryAndSetInnerObjects(List<Event> events) {
        if (null == events || events.size() == 0) {
            return;
        }
        for (Event event : events) {
            queryAndSetInnerObjects(event);
        }
    }

    /**
     * query inner objects, and set them to event
     * 
     * @param event
     *            event after set inner objects
     */
    private void queryAndSetInnerObjects(Event event) {
        if (null == event) {
            return;
        }
        List<Link> links = linkQueryDao.queryAsInnerObjects(event.getId(),
                ModelType.EVENT);
        event.setLinks(links);
    }

    /**
     * query event,without inner objects
     * 
     * @param outerObjectId
     *            outer object id
     * @param outerModelType
     *            outer model type
     * @return events list
     */
    private List<Event> queryWithoutInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        final String sql = "select event.*  from RDAP_EVENT event"
                + " inner join REL_EVENT_REGISTRATION rel "
                + " on (rel.EVENT_ID = event.EVENT_ID and rel.REL_ID = ? and rel.REL_OBJECT_TYPE = ?) ";
        List<Event> result = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, outerObjectId);
                ps.setString(2, outerModelType.getName());
                return ps;
            }
        }, new EventResultSetExtractor());
        return result;
    }

    /**
     * event ResultSetExtractor, extract data from ResultSet
     * 
     * @author jiashuo
     * 
     */
    class EventResultSetExtractor implements ResultSetExtractor<List<Event>> {
        @Override
        public List<Event> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Event> result = new ArrayList<Event>();
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getLong("EVENT_ID"));
                event.setEventAction(rs.getString("EVENT_ACTION"));
                event.setEventActor(rs.getString("EVENT_ACTOR"));
                event.setEventDate(extractTimestampFromRs(rs, "EVENT_DATE"));
                result.add(event);
            }
            return result;
        }
    }
}
