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
package cn.cnnic.rdap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntityTel;
import cn.cnnic.rdap.dao.AbstractQueryDao;

/**
 * entity telephone query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class EntityTelDao extends AbstractQueryDao<EntityTel> {
    /**
     * jdbcTemplate.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * query entity telephone.
     * 
     * @param entity
     *            entity.
     * @return EntityTel EntityTel.
     */
    public List<EntityTel> query(final Entity entity) {
        if (null == entity || null == entity.getId()) {
            return null;
        }
        final String sql = "select * from RDAP_VCARD_TEL where ENTITY_ID = ?";
        List<EntityTel> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setLong(1, entity.getId());
                        return ps;
                    }
                }, new EntityTelResultSetExtractor());
        return result;
    }

    /**
     * entity address ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class EntityTelResultSetExtractor implements
            ResultSetExtractor<List<EntityTel>> {
        @Override
        public List<EntityTel> extractData(ResultSet rs) throws SQLException {
            List<EntityTel> result = new ArrayList<EntityTel>();
            while (rs.next()) {
                EntityTel entityTel = new EntityTel();
                extractEntityTelFromRs(rs, entityTel);
                result.add(entityTel);
            }
            return result;
        }
    }

    /**
     * extract telephone from ResultSet.
     * 
     * @param rs
     *            ResultSet.
     * @param entityTel
     *            entityTel.
     * @throws SQLException
     *             SQLException.
     */
    private void extractEntityTelFromRs(ResultSet rs, EntityTel entityTel)
            throws SQLException {
        entityTel.setId(rs.getLong("TEL_ID"));
        entityTel.setEntityId(rs.getLong("ENTITY_ID"));
        entityTel.setTypes(rs.getString("TYPE"));
        entityTel.setGlobalNumber(rs.getString("GLOBAL_NUMBER"));
        entityTel.setExtNumber(rs.getString("EXT_NUMBER"));
        entityTel.setPref(getIntegerFromRs(rs, "PREF"));
    }

}
