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
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.model.Event;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.restfulwhois.rdap.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ezvcard.util.StringUtils;

/**
 * @author zhanyq
 * 
 */
@Repository
public class EventUpdateDaoImpl extends AbstractUpdateDao<Event, EventDto> {
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
    private UpdateDao<Link, LinkDto> linkUpdateDao;

	@Override
	public Event save(Event model) {
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
	public void saveAsInnerObjects(BaseModel outerModel, List<EventDto> models) {
		if (null == models || models.size() == 0){
			return;
		}
	    for (EventDto model: models) {
	    	Long eventId = createEvent(model); 	    
	    	createRelEvent(outerModel.getId(), outerModel.getObjectType(), eventId);
	    	//create link
	    	Event eventAsOuter = new Event();
	    	eventAsOuter.setId(eventId);
	    	linkUpdateDao.saveAsInnerObjects(eventAsOuter, model.getLinks());
	    	
			
	    }
	}
	@Override
	public void deleteAsInnerObjects(BaseModel outerModel) {
		if (null == outerModel) {
			return;
		}
		List<Long> eventIds = findIdsByOuterIdAndType(outerModel);
	    if (null != eventIds) {
	    	String eventIdsStr = StringUtils.join(eventIds, ",");
	    	//delete event	    	
	    	super.delete(eventIdsStr, "RDAP_EVENT", "EVENT_ID");
	    	//delete link
	    	for(Long eventId:eventIds){
	    		Event event = new Event();
	    		event.setId(eventId);
	    		linkUpdateDao.deleteAsInnerObjects(event);
	    	}
	    	//delete relEvent
	    	super.deleteRel(outerModel,"REL_EVENT_REGISTRATION");
	    	
	    }
	
	    
	}
	
	@Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<EventDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }
	
   /**
	 * create Event.
	 * @param model
	 *        Event object
	 * @return eventId.
	 */
	private Long createEvent(final EventDto model) {
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
				ps.setString(3, DateUtil.formatUTC(DateUtil.parseUTC(model.getEventDate())));
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
	
	@Override
	public Long findIdByHandle(String handle) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Long> findIdsByOuterIdAndType(final BaseModel outerModel) {
		 return super.findIdsByOuterIdAndType(outerModel, "EVENT_ID", "REL_EVENT_REGISTRATION");
	}
	
}
