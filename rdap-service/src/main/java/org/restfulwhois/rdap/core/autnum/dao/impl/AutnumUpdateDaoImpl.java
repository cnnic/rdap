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
package org.restfulwhois.rdap.core.autnum.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.model.Autnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class AutnumUpdateDaoImpl extends AbstractUpdateDao<Autnum, AutnumDto> {
    /**
     * SQL_CREATE_AUTNUM.
     */
    private static final String SQL_CREATE_AUTNUM =
            "INSERT INTO RDAP_AUTNUM"
             + " (HANDLE,START_AUTNUM,END_AUTNUM,NAME,TYPE,COUNTRY,LANG,PORT43,"
             + " CUSTOM_PROPERTIES) values(?,?,?,?,?,?,?,?,?)";
    /**
     * SQL_UPDATE_AUTNUM.
     */
    private static final String SQL_UPDATE_AUTNUM = "UPDATE RDAP_AUTNUM"
            + " SET START_AUTNUM=?,END_AUTNUM=?,NAME=?,TYPE=?,COUNTRY=?,LANG=?,PORT43=?,"
            + " CUSTOM_PROPERTIES=? WHERE AS_ID=?";
    /**
     * SQL_DELETE_AUTNUM.
     */
    private static final String SQL_DELETE_AUTNUM =
            "DELETE FROM RDAP_AUTNUM WHERE AS_ID=?";
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AutnumUpdateDaoImpl.class);

    @Override
    public Autnum save(final Autnum model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(SQL_CREATE_AUTNUM,
                                Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getHandle());
                ps.setLong(2, model.getStartAutnum());
                ps.setLong(3, model.getEndAutnum());
                ps.setString(4, model.getName());
                ps.setString(5, model.getType());
                ps.setString(6, model.getCountry());
                ps.setString(7, model.getLang());
                ps.setString(8, model.getPort43());               
                ps.setString(9, model.getCustomPropertiesJsonVal());
                return ps;
            }
        }, keyHolder);
        model.setId(keyHolder.getKey().longValue());
        return model;
    }

    @Override
    public void saveStatus(Autnum model) {
        saveStatus(model, model.getStatus(), "RDAP_AUTNUM_STATUS", "AS_ID");
    }

    @Override
    public void update(final Autnum model) {
        jdbcTemplate.update(SQL_UPDATE_AUTNUM, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, model.getStartAutnum());
                ps.setLong(2, model.getEndAutnum());
                ps.setString(3, model.getName());
                ps.setString(4, model.getType());
                ps.setString(5, model.getCountry());
                ps.setString(6, model.getLang());
                ps.setString(7, model.getPort43());               
                ps.setString(8, model.getCustomPropertiesJsonVal());
                ps.setLong(9, model.getId());
            }
        });
    }

    @Override
    public void delete(final Autnum model) {
        jdbcTemplate.update(SQL_DELETE_AUTNUM, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, model.getId());
            }
        });
    }

    @Override
    public void deleteStatus(Autnum model) {
        deleteStatus(model, "RDAP_AUTNUM_STATUS", "AS_ID");
    }

    @Override
    public void updateStatus(Autnum domain) {
        deleteStatus(domain);
        saveStatus(domain);
    }

    @Override
    public Long findIdByHandle(String handle) {
        return super.findIdByHandle(handle, "AS_ID", "RDAP_AUTNUM");
    }

}
