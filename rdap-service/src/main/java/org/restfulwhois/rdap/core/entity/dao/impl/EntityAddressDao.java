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
import org.restfulwhois.rdap.common.model.EntityAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * entity address query DAO select entity address from database for entity.
 * <p>
 * entity address use query method as an outer object interface.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class EntityAddressDao extends AbstractQueryDao<EntityAddress> {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(EntityAddressDao.class);       
    /**
     * jdbcTemplate.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * query entity address using entity id from RDAP_VCARD_ADR.
     * 
     * @param entity
     *            entity which will be filled with entity address.
     * @return EntityAddress list
     *            which will be set to entity.
     */
    public List<EntityAddress> query(final Entity entity) {
        LOGGER.debug("query, entity:{}", entity);
        if (null == entity || null == entity.getId()) {
            return null;
        }
        final String sql = "select * from RDAP_VCARD_ADR where ENTITY_ID = ?";
        List<EntityAddress> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setLong(1, entity.getId());
                        return ps;
                    }
                }, new EntityAddressResultSetExtractor());
        LOGGER.debug("query, result:{}", result);
        return result;
    }

    /**
     * entity address ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class EntityAddressResultSetExtractor implements
            ResultSetExtractor<List<EntityAddress>> {
        @Override
        public List<EntityAddress> extractData(ResultSet rs)
                throws SQLException {
            List<EntityAddress> result = new ArrayList<EntityAddress>();
            while (rs.next()) {
                EntityAddress entityAddr = new EntityAddress();
                extractEntityAddressFromRs(rs, entityAddr);
                result.add(entityAddr);
            }
            return result;
        }
    }

    /**
     * extract address from ResultSet.
     * 
     * @param rs
     *            ResultSet is the result set select from database.
     * @param address
     *            Entity address which will be filled with data.
     * @throws SQLException
     *             SQLException.
     */
    private void
            extractEntityAddressFromRs(ResultSet rs, EntityAddress address)
                    throws SQLException {
        address.setId(rs.getLong("ADR_ID"));
        address.setEntityId(rs.getLong("ENTITY_ID"));
        address.setPoBox(rs.getString("POST_BOX"));
        address.setExtendedAddress(rs.getString("EXT_ADR"));
        address.setStreetAddress(rs.getString("STREET"));
        address.setLocality(rs.getString("CITY"));
        address.setRegion(rs.getString("SP"));
        address.setPostalCode(rs.getString("POSTAL_CODE"));
        address.setCountry(rs.getString("COUNTRY"));
        address.setTypes(rs.getString("TYPE"));
        address.setPref(getIntegerFromRs(rs, "PREF"));
    }

}
