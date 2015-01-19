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
import org.restfulwhois.rdap.common.model.RelDomainVariant;
import org.restfulwhois.rdap.common.model.Variant;
import org.restfulwhois.rdap.common.model.Variants;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.restfulwhois.rdap.common.util.DomainUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class VariantsUpdateDaoImpl extends AbstractUpdateDao<Variants> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(VariantsUpdateDaoImpl.class);

    @Override
	public Variants create(Variants model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Variants model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Variants model) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * batch create Variants.
	 * 
	 * @param outerObjectId
	 *        object id of outer object	
	 * @param models 
	 *        Variants of outer Object
	 */
	@Override
	public  void batchCreateAsInnerObjects(Long outerObjectId,
			ModelType outerModelType, List<Variants> models) {
		if (null == models || models.size() == 0){
			return;
		}
	    for (Variants model: models) {
	    	  createVariants(outerObjectId, model);	    	  
	    }
	}

	/**
	 * @param outerObjectId
	 *        object id of outer object	
	 * @param model 
	 *        Variants object
	 *         
	 */
	private void createVariants(Long outerObjectId, Variants model) {
		List<Variant> variantList = model.getVariantNames();
		if (null == variantList || variantList.size() == 0) {
			return;
		}
		for (Variant variant:variantList) {
			Long variantId = createVariant(variant);  
			variant.setId(variantId);
	    	createRelDomainVariant(outerObjectId, variant);
		}
	}
	/**
	 * create rel domain variant
	 * @param outerObjectId
	 *        object id of outer object	 
	 * @param model
	 *        Variant object
	 */
	private void createRelDomainVariant(final Long outerObjectId, 
			        final Variant model) {
	    final List<RelDomainVariant> relationList = model.getRelations();
	    if(null == relationList || relationList .size() == 0 ){
	    	return;
	    }
		final String sql = "insert into REL_DOMAIN_VARIANT("
               + "DOMAIN_ID,VARIANT_TYPE,VARIANT_ID) values (?,?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		    public int getBatchSize() {
		        return relationList.size();
		    }
		    @Override
			public void setValues(PreparedStatement ps, int i)
				throws SQLException {
		    	ps.setLong(1, outerObjectId); 
		    	ps.setString(2, relationList.get(i).getVariantType());
		    	ps.setLong(3, model.getId());
			}				
	  });
		
	}
	/**
	 * create Variant
	 * @param model
	 *        Variant object
	 * @return variantId.
	 */
    private Long createVariant(final Variant model) {
        final String sql = "insert into RDAP_VARIANT(LDH_NAME,"
	      +  " UNICODE_NAME,IDNTABLE) values (?,?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(Connection connection)
        			throws SQLException {           
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, DomainUtil
				  .geneDomainPunyName(model.getUnicodeName()));
				ps.setString(2, model.getUnicodeName());
				ps.setString(3, model.getIdnTable());
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
