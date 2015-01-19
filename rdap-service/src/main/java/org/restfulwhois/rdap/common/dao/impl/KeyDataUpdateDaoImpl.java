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
import org.restfulwhois.rdap.common.model.Event;
import org.restfulwhois.rdap.common.model.KeyData;
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
public class KeyDataUpdateDaoImpl extends AbstractUpdateDao<KeyData, BaseModel> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(KeyDataUpdateDaoImpl.class);
    
    
    /**
     * link update dao.
     */
    @Autowired
    @Qualifier("linkUpdateDaoImpl")
    private UpdateDao<Link, BaseModel> linkUpdateDao;
    
    /**
     * Event update dao.
     */
    @Autowired   
    @Qualifier("eventUpdateDaoImpl")
    private UpdateDao<Event, BaseModel>  eventUpdateDao;
    
    /**
     * SecureDns update dao.
     */
    @Autowired       
    private SecureDnsUpdateDaoImpl secureDnsUpdateDao;


    @Override
	public KeyData create(KeyData model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(KeyData model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(KeyData model) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * batch create KeyData.
	 * 
	 * @param outerObjectId
	 *        object id of outer object	 
	 * @param models 
	 *        KeyData of outer Object
	 */
	@Override
	public  void batchCreateAsInnerObjects(BaseModel outerModel, List<KeyData> models) {
		if (null==models || models.size() == 0) {
			return;
		}
	    for (KeyData model:models) {
	    	Long keyDataId = createKeyData(model);  
	    	model.setId(keyDataId);
	    	secureDnsUpdateDao.createRelSecureDnsDskey(outerModel.getId(), ModelType.KEYDATA, keyDataId);
	       	linkUpdateDao.batchCreateAsInnerObjects(model, model.getLinks());
	    	//create event
		    eventUpdateDao.batchCreateAsInnerObjects(model, model.getEvents());
	    }
	}
	
	/**
	 * 
	 * @param outerObjectId
	 *        object id of outer object
	 * @param modelType
	 *        model type of  object
	 * @param keyDataId
	 *        keyDataId
	 */
	/*public void createRelSecureDnsDskey(final Long outerObjectId, final ModelType
			modelType,	final Long keyDataId) {
		final String sql = "insert into REL_SECUREDNS_DSKEY(SECUREDNS_ID,REL_DSKEY_TYPE,REL_ID)"
			      +  " values (?,?,?)"; 		       
		       jdbcTemplate.update(sql, new PreparedStatementCreator() {
		           public PreparedStatement createPreparedStatement(Connection connection) 
		        			throws SQLException {           
		            PreparedStatement ps = connection.prepareStatement(
		            		sql);
		            ps.setLong(1, outerObjectId);
		            ps.setString(2, modelType.getName());
		            ps.setLong(3, keyDataId);
					return ps;
					}
				
		        });
		
	}*/
	/**
	 * @param model
	 *        KeyData
	 * @return keyDataId.
	 */
    private Long createKeyData(final KeyData model) {
        final String sql = "insert into RDAP_KEYDATA(FLAGS,"
	      +  "PROTOCOL,PUBLIC_KEY,ALGORITHM) values (?,?,?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(Connection connection) 
        			throws SQLException {           
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, model.getFlags());
				ps.setInt(2, model.getProtocol());
				ps.setString(3, model.getPublicKey());
				ps.setInt(4, model.getAlgorithm());
				return ps;
			}		
        }, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long findIdByHandle(String handle) {
		// TODO Auto-generated method stub
		return null;
	}
}
