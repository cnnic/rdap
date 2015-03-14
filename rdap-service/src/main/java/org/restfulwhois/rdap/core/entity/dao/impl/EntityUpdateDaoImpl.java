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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
    /**
     * SQL_SAVE_ENTITY.
     */
    private static final String SQL_SAVE_ENTITY =
            "INSERT INTO RDAP_ENTITY"
                    + " (HANDLE,KIND,FN,EMAIL,TITLE,ORG,URL,PORT43,LANG,CUSTOM_PROPERTIES)"
                    + " values(?,?,?,?,?,?,?,?,?,?)";
    /**
     * SQL_UPDATE_ENTITY.
     */
    private static final String SQL_UPDATE_ENTITY = "UPDATE RDAP_ENTITY"
            + " SET KIND=?,FN=?,EMAIL=?,TITLE=?,ORG=?,URL=?,PORT43=?,LANG=?"
            + " ,CUSTOM_PROPERTIES=? WHERE ENTITY_ID=?";
    /**
     * SQL_DELETE_ENTITY.
     */
    private static final String SQL_DELETE_ENTITY =
            "DELETE FROM RDAP_ENTITY where ENTITY_ID=?";

    @Override
    public Entity save(final Entity model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(SQL_SAVE_ENTITY,
                                Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getHandle());
                ps.setString(2, model.getKind());
                ps.setString(3, model.getFn());
                ps.setString(4, model.getEmail());
                ps.setString(5, model.getTitle());
                ps.setString(6, model.getOrg());
                ps.setString(7, model.getUrl());
                ps.setString(8, model.getPort43());
                ps.setString(9, model.getLang());
                ps.setString(10, model.getCustomPropertiesJsonVal());
                return ps;
            }
        }, keyHolder);
        model.setId(keyHolder.getKey().longValue());
        return model;
    }

    @Override
    public void saveStatus(Entity model) {
        saveStatus(model, model.getStatus(), "RDAP_ENTITY_STATUS", "ENTITY_ID");
    }

    @Override
    public void update(final Entity model) {
        jdbcTemplate.update(SQL_UPDATE_ENTITY, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, model.getKind());
                ps.setString(2, model.getFn());
                ps.setString(3, model.getEmail());
                ps.setString(4, model.getTitle());
                ps.setString(5, model.getOrg());
                ps.setString(6, model.getUrl());
                ps.setString(7, model.getPort43());
                ps.setString(8, model.getLang());
                ps.setString(9, model.getCustomPropertiesJsonVal());
                ps.setLong(10, model.getId());
            }
        });
    }

    @Override
    public void updateStatus(Entity entity) {
        deleteStatus(entity);
        saveStatus(entity);
    }

    @Override
    public void delete(final Entity model) {
        jdbcTemplate.update(SQL_DELETE_ENTITY, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, model.getId());
            }
        });
    }

    @Override
    public void deleteStatus(Entity model) {
        deleteStatus(model, "RDAP_ENTITY_STATUS", "ENTITY_ID");
    }

    /**
     * 
     * @param outerModel
     *            outer object
     */
    public void saveRel(BaseModel outerModel) {
        if (null == outerModel || null == outerModel.getDto()) {
            return;
        }
        List<EntityHandleDto> entityHandles = outerModel.getDto().getEntities();
        if (null == entityHandles || entityHandles.size() == 0) {
            return;
        }
        for (EntityHandleDto entityHandleDto : entityHandles) {
            Long entityId = this.findIdByHandle(entityHandleDto.getHandle());
            if (null != entityId) {
                createRelEntity(outerModel, entityHandleDto, entityId);
            }
        }
    }

    @Override
    public void deleteRel(BaseModel outerModel) {
        if (null == outerModel || null == outerModel.getId()) {
            return;
        }
        super.deleteRel(outerModel, "REL_ENTITY_REGISTRATION");
    }

    @Override
    public void updateRel(BaseModel outerModel) {
        if (null == outerModel || null == outerModel.getId()
                || null == outerModel.getDto()) {
            return;
        }
        deleteRel(outerModel);
        saveRel(outerModel);
    }

    /**
     * 
     * @param outerModel
     *            outerModel.
     * @param entityHandleDto
     *            entityHandleDto.
     * @param entityId
     *            entityId.
     */
    private void createRelEntity(final BaseModel outerModel,
            final EntityHandleDto entityHandleDto, final Long entityId) {
        final List<String> roles = entityHandleDto.getRoles();
        if (roles == null || roles.size() == 0) {
            return;
        }
        final String sql =
                "insert into REL_ENTITY_REGISTRATION (REL_ID,"
                        + "REL_OBJECT_TYPE,ENTITY_ID,ENTITY_ROLE)"
                        + " values (?,?,?,?)";
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
