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
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.KeyDataDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;
import org.restfulwhois.rdap.common.model.DsData;
import org.restfulwhois.rdap.common.model.KeyData;
import org.restfulwhois.rdap.common.model.SecureDns;
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
public class SecureDnsUpdateDaoImpl extends AbstractUpdateDao<SecureDns, SecureDnsDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(SecureDnsUpdateDaoImpl.class);
    
    
    /**
     * KeyData update dao.
     */
    @Autowired 
    @Qualifier("keyDataUpdateDaoImpl")
    private UpdateDao<KeyData, KeyDataDto>  keyDataUpdateDao;
    
    /**
     * DsData update dao.
     */    
    @Autowired 
    @Qualifier("dsDataUpdateDaoImpl")
    private UpdateDao<DsData, DsDataDto>  dsDataUpdateDao;

    @Override
	public SecureDns create(SecureDns model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(SecureDns model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(SecureDns model) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * batch create SecureDns.
	 * 
	 * @param outerObjectId
	 *        object id of outer object
	 * @param outerModelType
	 *        model type of outer object
	 * @param models 
	 *        SecureDns of outer Object
	 */
	@Override
	public  void batchCreateAsInnerObjects(BaseModel outerModel, List<SecureDnsDto> models) {
		if(null == models || models.size() == 0){
			return;
		}
	    for (SecureDnsDto model: models) {
	    	Long secureDnsId = createSecureDns(model,outerModel.getId()); 
	    	SecureDns secureDnsAsOuter = new SecureDns();
	    	secureDnsAsOuter.setId(secureDnsId);
	    	//create keyData	    	
	    	keyDataUpdateDao.batchCreateAsInnerObjects(secureDnsAsOuter, model.getKeyData());	    	
	    	//create DsData            
            dsDataUpdateDao.batchCreateAsInnerObjects(secureDnsAsOuter, model.getDsData());	    	
	    }
	}
	
	
	/**
	 * @param model
	 *        SecureDns object
	 * @return secureDnsId.
	 */
    private Long createSecureDns(final SecureDnsDto model, final Long outerObjectId) {		
        final String sql = "insert into RDAP_SECUREDNS(ZONE_SIGNED,DELEGATION_SIGNED,MAX_SIGLIFE,DOMAIN_ID)"
	      +  " values (?,?,?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(Connection connection) 
        			throws SQLException {           
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setBoolean(1, model.isZoneSigned());
				ps.setBoolean(2, model.isDelegationSigned());
				ps.setInt(3, model.getMaxSigLife());
				ps.setLong(4, outerObjectId);				
				return ps;
			}		
        }, keyHolder);
		return keyHolder.getKey().longValue();
	}
    
    /**
	 * 
	 * @param outerObjectId
	 *        object id of outer object
	 * @param modelType
	 *        model type of  object
	 * @param dsKeyId
	 *        keyDataId or DsDataId
	 */
	public void createRelSecureDnsDskey(final Long outerObjectId, final ModelType
			modelType,	final Long dsKeyId) {
		final String sql = "insert into REL_SECUREDNS_DSKEY(SECUREDNS_ID,REL_DSKEY_TYPE,REL_ID)"
			      +  " values (?,?,?)"; 		       
		       jdbcTemplate.update(new PreparedStatementCreator() {
		           public PreparedStatement createPreparedStatement(Connection connection) 
		        			throws SQLException {           
		            PreparedStatement ps = connection.prepareStatement(
		            		sql);
		            ps.setLong(1, outerObjectId);
		            ps.setString(2, modelType.getName());
		            ps.setLong(3, dsKeyId);
					return ps;
					}
				
		        });
		
	}

	@Override
	public Long findIdByHandle(String handle) {
		// TODO Auto-generated method stub
		return null;
	}
}
