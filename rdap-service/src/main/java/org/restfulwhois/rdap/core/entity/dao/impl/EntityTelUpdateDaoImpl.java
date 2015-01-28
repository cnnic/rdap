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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.EntityTelephoneDto;
import org.restfulwhois.rdap.common.model.EntityTelephone;
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
public class EntityTelUpdateDaoImpl extends 
              AbstractUpdateDao<EntityTelephone, EntityTelephoneDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(EntityTelUpdateDaoImpl.class);    
 

    @Override
    public EntityTelephone save(EntityTelephone model) {
        return null;
    }

    @Override
    public void update(EntityTelephone model) {

    }

    @Override
    public void delete(EntityTelephone model) {

    }
    /**
     * batch create link.
     * 
     * @param outerModel
     *         outer object
     * @param models
     *        links of outer Object
     */
    @Override
    public void saveAsInnerObjects(final BaseModel outerModel,
            final List<EntityTelephoneDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }  
        final List<EntityTelephoneDto> notEmptyModels 
                        = getNotEmptyList(models);
        final String sql = "insert into RDAP_VCARD_TEL"
                + "(TYPE,GLOBAL_NUMBER,EXT_NUMBER,ENTITY_ID,PREF) "
                + "values (?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return notEmptyModels.size();
            }
            @Override
            public void setValues(PreparedStatement ps, int i)
              throws SQLException {
                EntityTelephoneDto model = notEmptyModels.get(i);
                ps.setString(1, model.getTypes());
                ps.setString(2, model.getNumber());
                ps.setString(3, model.getExtNumber());
                ps.setLong(4, outerModel.getId());
                ps.setLong(5, model.getPref());
                                              
             }
              
         });
    }
    
    @Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<EntityTelephoneDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }

    @Override
    public void deleteAsInnerObjects(BaseModel outerModel) {
      if (null == outerModel) {
             return;
       }
       delete(String.valueOf(outerModel.getId()), 
                       "RDAP_VCARD_TEL", "ENTITY_ID");
    } 

    @Override
    public Long findIdByHandle(String handle) {
        return null;
    }
    /**
     * 
     * @param entityTelList
     * entityTelList
     * @return notEmptyList
     */
    public static List<EntityTelephoneDto> getNotEmptyList(
           List<EntityTelephoneDto> entityTelList) {
        List<EntityTelephoneDto> notEmptyList = 
                   new ArrayList<EntityTelephoneDto>(); 
        if (null == entityTelList) {
            return notEmptyList;
        }
        for (EntityTelephoneDto entityTel:entityTelList) {
            if (StringUtils.isNotBlank(entityTel.getNumber())) {
                notEmptyList.add(entityTel);
            }
        }
        return notEmptyList;
    }
}
