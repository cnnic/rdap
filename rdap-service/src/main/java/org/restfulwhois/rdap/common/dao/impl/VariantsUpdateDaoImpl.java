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

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantNameDto;
import org.restfulwhois.rdap.common.model.Variants;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;



/**
 * @author zhanyq
 * 
 */
@Repository
public class VariantsUpdateDaoImpl extends 
                    AbstractUpdateDao<Variants, VariantDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(VariantsUpdateDaoImpl.class);  

    @Override
	public Variants save(Variants model) {
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
	 * @param outerModel
	 *         outer object	
	 * @param models 
	 *        Variants of outer Object
	 */
	@Override
	public void saveAsInnerObjects(BaseModel outerModel,
			List<VariantDto> models) {
		if (null == models || models.size() == 0) {
			return;
		}
	    for (VariantDto model: models) {
	    	  createVariants(outerModel.getId(), model);	    	  
	    }
	}
	
	@Override
	public void deleteAsInnerObjects(BaseModel outerModel) {
		if (null == outerModel) {
			return;
		}
		List<Long> variantIds = findIdsByOuterIdAndType(outerModel);
		
		if (null != variantIds) {
	    	String variantIdStr = StringUtils.join(variantIds, ",");
	    	super.delete(variantIdStr, "RDAP_VARIANT", "VARIANT_ID");
	    	super.delete(String.valueOf(outerModel.getId()),
	    			"REL_DOMAIN_VARIANT", "DOMAIN_ID");
		}
	}

    @Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<VariantDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }
	/**
	 * @param domainId
	 *        object id of outer object	
	 * @param model 
	 *        Variants object
	 *         
	 */
	private void createVariants(Long domainId, VariantDto model) {
	    List<String> relations = model.getRelation();
		List<VariantNameDto> variantList = model.getVariantNames();
		if (null == variantList || variantList.size() == 0) {
			return;
		}
		for (VariantNameDto variant:variantList) {
            Long variantId = createVariant(variant, model.getIdnTable());
			createRelDomainVariant(domainId, variantId, relations);
		}
	}
	/**
	 * 
	 * @param domainId
	 *        domainId
	 * @param variantId
	 *       variantId
	 * @param relations
	 *         relations
	 */
	private void createRelDomainVariant(final Long domainId, 
			        final Long variantId, List<String> relations) {
	    final List<String> notEmptyRelations = StringUtil.
	    		getNotEmptyStringList(relations);
	    if (notEmptyRelations.isEmpty()) {
            return;
        }
		final String sql = "insert into REL_DOMAIN_VARIANT("
               + "DOMAIN_ID,VARIANT_TYPE,VARIANT_ID) values (?,?,?)";
		jdbcTemplate.batchUpdate(sql, 
				new BatchPreparedStatementSetter() {
		    public int getBatchSize() {
		        return notEmptyRelations.size();
		    }
		    @Override
			public void setValues(PreparedStatement ps, int i)
				throws SQLException {
		    	ps.setLong(1, domainId); 
		    	ps.setString(2, notEmptyRelations.get(i));
		    	ps.setLong(3, variantId);
			}				
	  });
		
	}

	/**
	 * 
	 * @param variant
	 *        VariantNameDto
	 * @param idnTable
	 *        idnTable
	 * @return id
	 */
    private Long createVariant(final VariantNameDto variant,
    		final String idnTable) {
        final String sql = "insert into RDAP_VARIANT(LDH_NAME,"
	      +  " UNICODE_NAME,IDNTABLE) values (?,?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(
        			Connection connection) throws SQLException {
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, variant.getLdhName());
				ps.setString(2, variant.getUnicodeName());
				ps.setString(3, idnTable);
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
	/**
	 * 
	 * @param outerModel
	 *        outer object
	 * @return ids
	 */
	public List<Long> findIdsByOuterIdAndType(final BaseModel outerModel) {
		final String sql = "SELECT VARIANT_ID as ID from "
				+ "REL_DOMAIN_VARIANT where DOMAIN_ID = ?";
        LOGGER.debug("find VARIANT_ID exist,sql:{}", sql);
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
}
