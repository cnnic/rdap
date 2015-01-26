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
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;
import org.restfulwhois.rdap.common.model.PublicId;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class PublicIdUpdateDaoImpl extends 
               AbstractUpdateDao<PublicId, PublicIdDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(PublicIdUpdateDaoImpl.class);

    @Override
    public PublicId save(PublicId model) {
       return null;
    }

    @Override
    public void update(PublicId model) {
    }

    @Override
    public void delete(PublicId model) {
    }
    
    /**
     * batch create publicId.
     * @param outerModel
     *         outer object
     * @param models 
     *        PublicId of outer Object
     */
    @Override
    public void saveAsInnerObjects(BaseModel outerModel,
                   List<PublicIdDto> models) {
       if (null == models || models.size() == 0) {
              return;
       }
       for (PublicIdDto model: models) {
            Long publicId = queryByIndentifierAndType(model);
            if (null == publicId) {
                 publicId = createPublicId(model);
            }
            createRelPublicId(outerModel, publicId);
       }
    }

    @Override
    public void deleteAsInnerObjects(BaseModel outerModel) {
        if (null == outerModel) {
            return;
        }
        super.deleteRel(outerModel, "REL_PUBLICID_REGISTRATION");
    }
    
    @Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<PublicIdDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }
    /**
     * 
     * @param outerModel
     *        outer object
     * @param publicId
     *        publicId
     */
     private void createRelPublicId(final BaseModel outerModel, 
             final Long publicId) {
        final String sql = "insert into REL_PUBLICID_REGISTRATION(REL_ID,"
               +  "REL_OBJECT_TYPE,PUBLIC_ID) values (?,?,?)";
        jdbcTemplate.update(new PreparedStatementCreator() {
             public PreparedStatement createPreparedStatement(
                   Connection connection) throws SQLException {
                  PreparedStatement ps = connection.prepareStatement(
                      sql);
                  ps.setLong(1, outerModel.getId());
                  ps.setString(2, outerModel.getObjectType().getName());
                  ps.setLong(3, publicId);
                  return ps;
             }
         });
    }

   /**
	 * @param model
	 *        pubuliId object
	 * @return publicId.
	 */
    private Long createPublicId(final PublicIdDto model) {
        final String sql = "insert into RDAP_PUBLICID(IDENTIFIER,TYPE)"
	      +  " values (?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(
        		Connection connection) throws SQLException {
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, model.getIdentifier());
				ps.setString(2, model.getType());
				return ps;
			}		
        }, keyHolder);
		return keyHolder.getKey().longValue();
	}
    
    
    /**
     * query publicId from RDAP_PUBLICID by IDENTIFIER and TYPE.
     * 
     *  @param model
     *            PublicId object.
     * @return public id
     */
    private Long queryByIndentifierAndType(final PublicIdDto model) {
        final String sql = "select PUBLIC_ID  from RDAP_PUBLICID publicId"
                + " where IDENTIFIER=? and TYPE=? ";
        List<Long> publicIds = 
        		jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, model.getIdentifier());
                ps.setString(2, model.getType());
                return ps;
            }
        }, new RowMapper<Long>() {
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("PUBLIC_ID");
            }
        });
        if (publicIds.size() > 0) {
            return publicIds.get(0);
        }
        return null;       
    }

	@Override
	public Long findIdByHandle(String handle) {
		// TODO Auto-generated method stub
		return null;
	}	
}
