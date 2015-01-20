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
package org.restfulwhois.rdap.common.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

/**
 * 
 * @param <T>
 *            object derived from BaseModel.
 * @author jiashuo
 * 
 */
public abstract class AbstractUpdateDao<T extends BaseModel, DTO extends BaseDto>
        implements UpdateDao<T, DTO> {
    private static final String TPL_COUNT_BY_HANDLE =
            "SELECT %s as ID from %s where HANDLE = ?";
    private static final String TPL_CREATE_STATUS =
            "INSERT INTO %s(%s,STATUS) values(?,?)";
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractUpdateDao.class);
    /**
     * JDBC template simplifies the use of JDBC and helps to avoid common
     * errors.
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Override
    public void
            batchCreateAsInnerObjects(BaseModel outerModel, List<DTO> models) {
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }

    protected Long findIdByHandle(final String handle, String idColumnName,
            String tableName) {
        final String sql =
                String.format(TPL_COUNT_BY_HANDLE, idColumnName, tableName);
        LOGGER.debug("check handle exist,sql:{}", sql);
        List<Long> ids = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, handle);
                return ps;
            }
        }, new RowMapper<Long>() {
            public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getLong("ID");
            }
        });
        if (ids.size() > 0) {
            return ids.get(0);
        }
        return null;
    }

    
    @Override
    public void saveStatus(T model) {
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }

    protected void saveStatus(final T model, final List<String> statusList,
            String tableName, String outerModelIdColumnName) {
        if(null == statusList||statusList.isEmpty()){
            LOGGER.debug("status is empty, not save.");
            return;
        }
        String sql =
                String.format(TPL_CREATE_STATUS, tableName,
                        outerModelIdColumnName);
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            public int getBatchSize() {
                return statusList.size();
            }

            @Override
            public void setValues(PreparedStatement ps, int i)
                    throws SQLException {
                ps.setLong(1, model.getId());
                ps.setString(2, statusList.get(i));
            }
        });
    }
}
