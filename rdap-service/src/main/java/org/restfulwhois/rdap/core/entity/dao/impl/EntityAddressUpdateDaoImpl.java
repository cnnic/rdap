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
import org.restfulwhois.rdap.common.dto.embedded.EntityAddressDto;
import org.restfulwhois.rdap.common.model.EntityAddress;
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
public class EntityAddressUpdateDaoImpl extends 
              AbstractUpdateDao<EntityAddress, EntityAddressDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(EntityAddressUpdateDaoImpl.class);    
 

    @Override
    public EntityAddress save(EntityAddress model) {
        return null;
    }

    @Override
    public void update(EntityAddress model) {

    }

    @Override
    public void delete(EntityAddress model) {

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
            final List<EntityAddressDto> models) {
        if (null == models || models.size() == 0) {
             return;
        } 
        final List<EntityAddressDto> notEmptyModels 
                   = getNotEmptyList(models);
        final String sql = "insert into RDAP_VCARD_ADR"
                + "(POST_BOX,EXT_ADR,STREET,CITY,"
                +  "SP,POSTAL_CODE,COUNTRY,ENTITY_ID,TYPE,PREF) "
                + "values (?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return notEmptyModels.size();
            }
            @Override
            public void setValues(PreparedStatement ps, int i)
              throws SQLException {
                EntityAddressDto model = notEmptyModels.get(i);
                ps.setString(1, model.getPostbox());
                ps.setString(2, model.getExtendedAddress());
                ps.setString(3, model.getStreetAddress());
                ps.setString(4, model.getLocality());
                ps.setString(5, model.getRegion());
                ps.setString(6, model.getPostalcode());
                ps.setString(7, model.getCountry());
                ps.setLong(8, outerModel.getId());
                ps.setString(9, model.getTypes());
                ps.setLong(10, model.getPref());
                                
             }
              
         });
    }
    
    @Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<EntityAddressDto> models) {
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
                       "RDAP_VCARD_ADR", "ENTITY_ID");
    } 

    @Override
    public Long findIdByHandle(String handle) {
        return null;
    }
    
    /**
     * 
     * @param entityAddressList
     * entityAddressList
     * @return notEmptyList
     */
    private  List<EntityAddressDto> getNotEmptyList(
           List<EntityAddressDto> entityAddressList) {
        List<EntityAddressDto> notEmptyList = 
                   new ArrayList<EntityAddressDto>(); 
        if (null == entityAddressList) {
            return notEmptyList;
        }
        for (EntityAddressDto entityAddress:entityAddressList) {
            if (StringUtils.isNotBlank(entityAddress.getExtendedAddress()) 
                || StringUtils.isNotBlank(entityAddress.getCountry())
                || StringUtils.isNotBlank(entityAddress.getLocality())
                || StringUtils.isNotBlank(entityAddress.getPostalcode())
                || StringUtils.isNotBlank(entityAddress.getPostbox())
                || StringUtils.isNotBlank(entityAddress.getRegion())
                || StringUtils.isNotBlank(entityAddress.getStreetAddress())) {
                notEmptyList.add(entityAddress);
            }
        }
        return notEmptyList;
    }
}
