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
package org.restfulwhois.rdap.core.entity.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;



/**
 * @author zhanyq
 * 
 */
@Repository
public class EntityUpdateDaoImpl extends AbstractUpdateDao<Entity, EntityDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(EntityUpdateDaoImpl.class);

    @Override
	public Entity create(Entity model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Entity model) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Entity model) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 
	 * @param outerObjectId
	 *        object id of outer object
	 * @param outerModelType
	 *        model type of outer object
	 * @param linkId
	 *        linkId
	 */
	public void createRel(BaseModel outerModel) {
		if (null == outerModel || null == outerModel.getDto()) {
			return;
		}
		List<EntityHandleDto> entityHandles= outerModel.getDto().getEntities();
		if(null == entityHandles  || entityHandles.size() ==0){
			return;
		}
		for (EntityHandleDto entityHandleDto:entityHandles){
			Long entityId = this.findIdByHandle(entityHandleDto.getHandle());
			if(null != entityId){
				createRelEntity(outerModel,entityHandleDto, entityId);
			}
		}	
	}
	
    /**
     * 
     * @param outerModel
     * @param entityHandleDto
     * @param entityId
     */
    private void createRelEntity(final BaseModel outerModel, 
    		final EntityHandleDto entityHandleDto, final Long entityId) {	
    	final List<String> roles = entityHandleDto.getRoles();
    	if(roles == null || roles.size() == 0){
    		return;
    	}
    	final String sql = "insert into REL_ENTITY_REGISTRATION (REL_ID,REL_OBJECT_TYPE,ENTITY_ID,ENTITY_ROLE)"
			      +  " values (?,?,?,?)"; 	   
    	jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		    public int getBatchSize() {
		        return roles.size();
		    }
		    @Override
			public void setValues(PreparedStatement ps, int i)
				throws SQLException {
		    	ps.setLong(1, outerModel.getId()); 
		    	ps.setString(2, outerModel.getObjectType().getName()); 
		    	ps.setLong(3, entityId);
		    	ps.setString(4, roles.get(i));
			}				
	  });
	}

    @Override
    public Long findIdByHandle(String handle) {
        return super.findIdByHandle(handle, "ENTITY_ID", "RDAP_ENTITY");
    }
}
