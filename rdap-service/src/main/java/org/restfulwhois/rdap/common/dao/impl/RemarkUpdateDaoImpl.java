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
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.Remark;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * @author zhanyq
 * 
 */
@Repository
public class RemarkUpdateDaoImpl extends AbstractUpdateDao<Remark> {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RemarkUpdateDaoImpl.class);
    
    /**
     * link update dao.
     */
    @Autowired
    @Qualifier("linkUpdateDaoImpl")
    private UpdateDao<Link> linkUpdateDao;

	@Override
	public Remark create(Remark model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Remark model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Remark model) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void batchCreateAsInnerObjects(Long outerObjectId,
            ModelType outerModelType, List<Remark> models) {
		if (null == models || models.size() == 0){
			return;
		}
	    for (Remark remark: models) {
	    	Long remarkId = createRemark(remark); 
	    	remark.setId(remarkId);
	    	createRelRemark(outerObjectId, outerModelType, remarkId);	    	
			createRemarkDescription(remark);
			//create link			
			linkUpdateDao.batchCreateAsInnerObjects(remarkId, ModelType.REMARK
				, remark.getLinks());
						
	    }
	}
	/**
	 * create remark
	 * @param remark
	 * @return remarId
	 *      
	 */
	private Long createRemark(final Remark remark) {
        final String sql = "insert into RDAP_NOTICE(TYPE,TITLE)"
	      +  " values (?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(Connection connection) 
        			throws SQLException {           
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, ModelType.REMARK.getName());
				ps.setString(2, remark.getTitle());
				
				return ps;
			}		
        }, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	/**
	 * create rel remark registration
	 * @param outerObjectId
	 *        object id of outer object
	 * @param outerModelType
	 *        model type of outer object
	 * @param remarkId
	 *        remarkId
	 */
	private void createRelRemark(final Long outerObjectId, final ModelType
			outerModelType,	final Long remarkId) {
		final String sql = "insert into REL_NOTICE_REGISTRATION(REL_ID,REL_OBJECT_TYPE,NOTICE_ID)"
			      +  " values (?,?,?)"; 		       
		       jdbcTemplate.update(new PreparedStatementCreator() {
		           public PreparedStatement createPreparedStatement(Connection connection) 
		        			throws SQLException {           
		            PreparedStatement ps = connection.prepareStatement(
		            		sql);
		            ps.setLong(1, outerObjectId);
		            ps.setString(2, outerModelType.getName());
		            ps.setLong(3, remarkId);
					return ps;
					}				
		 });		
	}
	/**
	 * create remark description
	 * @param remark
	 *        remark	 
	 */
    private void createRemarkDescription(final Remark remark) {
		final List<String> description = remark.getDescription();
		if(null == description || description.size() == 0) {
			return;
		}
		final String sql = "insert into  RDAP_NOTICE_DESCRIPTION(NOTICE_ID, DESCRIPTION)"
			      +  " values (?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		    public int getBatchSize() {
		        return description.size();
		    }
		    @Override
			public void setValues(PreparedStatement ps, int i)
				throws SQLException {
		    	ps.setLong(1, remark.getId()); 
		    	ps.setString(2, description.get(i));                              
			}				
	  });
	}

	@Override
	public Long findIdByHandle(String handle) {
		// TODO Auto-generated method stub
		return null;
	}
}
