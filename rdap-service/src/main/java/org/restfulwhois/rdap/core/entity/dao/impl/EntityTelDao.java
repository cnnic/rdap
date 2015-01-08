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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.EntityTelephone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * entity telephone query DAO select entity telephone from RDAP_VCARD_TEL. it is
 * an object belonged to entity,and use query method as interface.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class EntityTelDao extends AbstractQueryDao<EntityTelephone> {

    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(EntityTelDao.class);
    /**
     * jdbcTemplate.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * query entity telephone from RDAP_VCARD_TEL using entity id.
     * 
     * @param entity
     *            entity object.
     * @return EntityTel EntityTel list.
     */
    public List<EntityTelephone> query(final Entity entity) {
        if (null == entity || null == entity.getId()) {
            return null;
        }
        final String sql = "select * from RDAP_VCARD_TEL where ENTITY_ID = ?";
        List<EntityTelephone> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setLong(1, entity.getId());
                        return ps;
                    }
                }, new EntityTelephoneResultSetExtractor());
        return result;
    }

    /**
     * entity address ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class EntityTelephoneResultSetExtractor implements
            ResultSetExtractor<List<EntityTelephone>> {
        @Override
        public List<EntityTelephone> extractData(ResultSet rs)
                throws SQLException {
            List<EntityTelephone> result = new ArrayList<EntityTelephone>();
            while (rs.next()) {
                EntityTelephone tel = extractEntityTelFromRs(rs);
                result.add(tel);
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
     * @return EntityTelephone EntityTelephone.
     * @throws SQLException
     *             SQLException.
     */
    private EntityTelephone extractEntityTelFromRs(ResultSet rs)
            throws SQLException {
        EntityTelephone telephone =
                EntityTelephone.buildTextTel(rs.getString("GLOBAL_NUMBER"),
                        rs.getString("EXT_NUMBER"));
        telephone.setTypes(rs.getString("TYPE"));
        telephone.setPref(getIntegerFromRs(rs, "PREF"));
        return telephone;
    }
    
}
