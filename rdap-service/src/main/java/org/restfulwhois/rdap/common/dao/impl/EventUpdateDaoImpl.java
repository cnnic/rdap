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
package org.restfulwhois.rdap.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.model.Event;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyq
 * 
 */
@Repository
public class EventUpdateDaoImpl extends AbstractUpdateDao<Event, BaseModel> {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(EventUpdateDaoImpl.class);    
   
    /**
     * link update dao.
     */
    @Autowired
    @Qualifier("linkUpdateDaoImpl")
    private UpdateDao<Link, BaseModel> linkUpdateDao;

	@Override
	public Event create(Event model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Event model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Event model) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * batch create Event.
	 * 
	 * @param outerObjectId
	 *        object id of outer object
	 * @param outerModelType
	 *        model type of outer object
	 * @param models 
	 *        events of outer Object
	 */
	@Override
	public void batchCreateAsInnerObjects(BaseModel outerModel, List<Event> models) {
		if (null == models || models.size() == 0){
			return;
		}
	    for (Event model: models) {
	    	Long eventId = createEvent(model); 
	    	model.setId(eventId);
	    	createRelEvent(outerModel.getId(), outerModel.getObjectType(), eventId);	    	
	    	linkUpdateDao.batchCreateAsInnerObjects(model, model.getLinks());
	    	
			
	    }
	}
	/**
	 * create Event
	 * @param model
	 *        Event object
	 * @return eventId.
	 */
	private Long createEvent(final Event model) {
        final String sql = "insert into RDAP_EVENT(EVENT_ACTION,EVENT_ACTOR,EVENT_DATE)"
	      +  " values (?,?,?)";       
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(Connection connection)
        			throws SQLException {           
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, model.getEventAction());
				ps.setString(2, model.getEventActor());
				ps.setTimestamp(3, new java.sql.Timestamp(
                   parseStringToDate(model.getEventDate()
                   , "yyyy-MM-dd HH:mm:ss").getTime()));
				return ps;
			}		
        }, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * create rel Event
	 * @param outerObjectId
	 *        object id of outer object
	 * @param outerModelType
	 *        model type of outer object
	 * @param eventId
	 *        eventId
	 */
	private void createRelEvent(final Long outerObjectId, final ModelType
			outerModelType,	final Long eventId) {
		final String sql = "insert into REL_EVENT_REGISTRATION(REL_ID,REL_OBJECT_TYPE,EVENT_ID)"
			      +  " values (?,?,?)"; 		       
		       jdbcTemplate.update(new PreparedStatementCreator() {
		           public PreparedStatement createPreparedStatement(Connection connection) 
		        			throws SQLException {           
		            PreparedStatement ps = connection.prepareStatement(
		            		sql);
		            ps.setLong(1, outerObjectId);
		            ps.setString(2, outerModelType.getName());
		            ps.setLong(3, eventId);
					return ps;
					}				
		 });		
	}
	
    /**
     * 
     * @param dateString
     *        dateString
     * @param format
     *        format
     * @return date
     */
    protected Date parseStringToDate(String dateString, String format) {
        try {           
            Date date = new SimpleDateFormat(format).parse(dateString);
            return date;
        } catch (Exception e) {
            LOGGER.error("error timestamp format,error:{}", e);
            return null;
        }
    }
	@Override
	public Long findIdByHandle(String handle) {
		// TODO Auto-generated method stub
		return null;
	}
}
