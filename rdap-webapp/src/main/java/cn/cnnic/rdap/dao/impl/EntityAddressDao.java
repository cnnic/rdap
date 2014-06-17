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
import cn.cnnic.rdap.bean.EntityAddress;
import cn.cnnic.rdap.dao.AbstractQueryDao;

/**
 * entity address query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class EntityAddressDao extends AbstractQueryDao<EntityAddress> {
    /**
     * jdbcTemplate.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * query entity address.
     * 
     * @param entity
     *            entity.
     * @return EntityAddress list.
     */
    public List<EntityAddress> query(final Entity entity) {
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
     *            ResultSet.
     * @param address
     *            address.
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
