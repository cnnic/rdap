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
import java.sql.ResultSet;
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
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ezvcard.util.StringUtils;



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
	public SecureDns save(SecureDns model) {
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
	 * @param outerModel
	 *         outer object	 
	 * @param models 
	 *        SecureDns of outer Object
	 */
	@Override
	public  void saveAsInnerObjects(BaseModel outerModel,
			List<SecureDnsDto> models) {
		if (null == models || models.size() == 0) {
			return;
		}
	    for (SecureDnsDto model: models) {
	    	Long secureDnsId = createSecureDns(model, outerModel.getId()); 
	    	SecureDns secureDnsAsOuter = new SecureDns();
	    	secureDnsAsOuter.setId(secureDnsId);
	    	//create KeyData	    	
	    	keyDataUpdateDao.saveAsInnerObjects(secureDnsAsOuter,
	    			model.getKeyData());	    	
	    	//create DsData            
            dsDataUpdateDao.saveAsInnerObjects(secureDnsAsOuter, 
            		model.getDsData());	    	
	    }
	}
	
	@Override
	public void deleteAsInnerObjects(BaseModel outerModel) {
		if (null == outerModel) {
			return;
		}
		List<Long> secureDnsIds = findIdsByOuterIdAndType(outerModel);
	    if (null != secureDnsIds) {
	    	String secureDnsIdStr = StringUtils.join(secureDnsIds, ",");
	    	//delete SecureDns	    		
	    	super.delete(secureDnsIdStr, "RDAP_SECUREDNS", "SECUREDNS_ID");
	    	for (Long secureDnsId : secureDnsIds) {
	    		SecureDns secureDns = new SecureDns();
	    		secureDns.setId(secureDnsId);
	    		//delete KeyData
		    	keyDataUpdateDao.deleteAsInnerObjects(secureDns);
		    	//delete DsData
		    	dsDataUpdateDao.deleteAsInnerObjects(secureDns); 	
	    	}
	    }
	}	
	
	@Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<SecureDnsDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }
	/**
	 * @param model
	 *        SecureDns object
	 *  @param outerObjectId
	 *         object id of outer object
	 * @return secureDnsId.
	 */
    private Long createSecureDns(final SecureDnsDto model,
    		final Long outerObjectId) {		
        final String sql = "insert into RDAP_SECUREDNS(ZONE_SIGNED,"
	      +  "DELEGATION_SIGNED,MAX_SIGLIFE,DOMAIN_ID) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(
        			Connection connection) throws SQLException {
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
	public void createRelSecureDnsDskey(final Long outerObjectId, 
			final ModelType modelType,	final Long dsKeyId) {
		final String sql = "insert into REL_SECUREDNS_DSKEY"
			+ "(SECUREDNS_ID,REL_DSKEY_TYPE,REL_ID) values (?,?,?)";
		jdbcTemplate.update(new PreparedStatementCreator() {
		    public PreparedStatement createPreparedStatement(
		         Connection connection)	throws SQLException {
		      PreparedStatement ps = connection.prepareStatement(sql);
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
	/**
	 * 
	 * @param outerModel
	 *        outer object
	 * @return ids
	 */
	
	public List<Long> findIdsByOuterIdAndType(final BaseModel outerModel) {
		final String sql = "SELECT SECUREDNS_ID as ID from "
				+ " RDAP_SECUREDNS where DOMAIN_ID = ?";
        LOGGER.debug("check SECUREDNS_ID exist,sql:{}", sql);
        List<Long> ids = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, outerModel.getId());                
                return ps;
            }
        }, new RowMapper<Long>() {
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("ID");
            }
        });
        if (ids.size() > 0) {
            return ids;
        }
        return null;
    }	
	
	/**
	 * 
	 * @param secureDnsId
	 *       secureDnsId
	 * @param modelType
	 *    object type
	 */
	public void deleteRelSecureDnsDskey(final Long secureDnsId,
			final ModelType modelType) {
		final String sql = "delete from REL_SECUREDNS_DSKEY where"
				+ " SECUREDNS_ID = ? and REL_DSKEY_TYPE= ?";
		jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, secureDnsId);
                ps.setString(2, modelType.getName());
                return ps;
            }       
		});
		
	}
	/**
	 * 
	 * @param outerModelId
	 *        outerModelId
	 * @param modelType
	 *        object type
	 * @return ids
	 */
	public List<Long> findIdsByOuterIdAndType(final Long outerModelId,
			final ModelType modelType) {
		final String sql = "SELECT REL_ID from REL_SECUREDNS_DSKEY"
		     + " where SECUREDNS_ID = ? and REL_DSKEY_TYPE =?";
        LOGGER.debug("check SECUREDNS_ID exist,sql:{}", sql);
        List<Long> ids = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, outerModelId);   
                ps.setString(2, modelType.getName()); 
                return ps;
            }
        }, new RowMapper<Long>() {
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("REL_ID");
            }
        });
        if (ids.size() > 0) {
            return ids;
        }
        return null;
    }
}
