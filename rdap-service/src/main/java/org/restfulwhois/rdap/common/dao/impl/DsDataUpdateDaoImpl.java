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

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.model.DsData;
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
public class DsDataUpdateDaoImpl extends AbstractUpdateDao<DsData, DsDataDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DsDataUpdateDaoImpl.class); 
  

    /**
     * link update dao.
     */
    @Autowired
    @Qualifier("linkUpdateDaoImpl")
    private UpdateDao<Link, LinkDto> linkUpdateDao;
    
    /**
     * Event update dao.
     */
    @Autowired   
    @Qualifier("eventUpdateDaoImpl")
    private UpdateDao<Event, EventDto>  eventUpdateDao;
    /**
     * SecureDns update dao.
     */
    @Autowired       
    private SecureDnsUpdateDaoImpl secureDnsUpdateDao;


    @Override
	public DsData save(DsData model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(DsData model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(DsData model) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * batch create DsData.
	 * 
	 * @param outerModel
	 *         outer object	 
	 * @param models 
	 *        DsData of outer Object
	 */
	@Override
	public void saveAsInnerObjects(BaseModel outerModel,
			List<DsDataDto> models) {
		if (null == models || models.size() == 0){
			return;
		}
		for (DsDataDto model: models) {
	    	Long dsDataId = createDsData(model); 	    	
	    	secureDnsUpdateDao.createRelSecureDnsDskey(outerModel.getId(),
	    			ModelType.DSDATA, dsDataId);
	    	DsData dsDataAsOuter = new DsData();
	    	dsDataAsOuter.setId(dsDataId);	    	
	    	//create link	    		    		
		    linkUpdateDao.saveAsInnerObjects(dsDataAsOuter,
		    		model.getLinks());	    		    	
	    	//create event
	    	eventUpdateDao.saveAsInnerObjects(dsDataAsOuter, 
	    			model.getEvents());
	    	    	
	    }
	}
	
	@Override
	public void deleteAsInnerObjects(BaseModel outerModel) {
		if (null == outerModel) {
			return;
		}
		List<Long> dsDataIds = secureDnsUpdateDao.findIdsByOuterIdAndType(
				outerModel.getId(), ModelType.DSDATA);
	    if (null != dsDataIds) {
	    	String dsDataIdStr = StringUtils.join(dsDataIds, ",");
	    	//delete KeyData	    	
	    	super.delete(dsDataIdStr, "RDAP_DSDATA", "DSDATA_ID");
	    	secureDnsUpdateDao.deleteRelSecureDnsDskey(
	    			outerModel.getId(), ModelType.DSDATA);
	    	for (Long dsDataId:dsDataIds) {
	    		DsData dsData = new DsData();
	    		dsData.setId(dsDataId);
		    	linkUpdateDao.deleteAsInnerObjects(dsData);
		    	eventUpdateDao.deleteAsInnerObjects(dsData);
	    	}
	    }
    }	
	
    @Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<DsDataDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }
	/**
	 * @param model
	 *        DsData
	 * @return dsDataId.
	 */
    private Long createDsData(final DsDataDto model) {
        final String sql = "insert into RDAP_DSDATA(KEY_TAG,"
	      +  "ALGORITHM,DIGEST,DIGEST_TYPE) values (?,?,?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(
        		 Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, model.getKeyTag());
				ps.setInt(2, model.getAlgorithm());
				ps.setString(3, model.getDigest());
				ps.setInt(4, model.getDigestType());
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
