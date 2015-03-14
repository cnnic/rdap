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
package org.restfulwhois.rdap.filters.queryFilter.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

import org.restfulwhois.rdap.filters.queryFilter.dao.CustomColumnPolicyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;


/**
 * policy query DAO select the hidden column from RDAP_POLICY.
 * load all hidden columns while system is loading.
 * 
 * @author weijunkai
 * 
 */
@Repository
public class CustomColumnPolicyDaoImpl implements CustomColumnPolicyDao {
    
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(CustomColumnPolicyDaoImpl.class);
    
    /**
     * jdbcTemplate.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * <pre>
     * query policy to map from RDAP_POLICY.
     * first string is object,second one is set of fields.
     * </pre>
     * 
     * @return map of policy.
     */
    public Map<String, Set<String>> loadAllPolicyMap() {
        LOGGER.debug("loadAllPolicyMap.");
        final String sql = "select * from RDAP_POLICY";
        Map<String, Set<String>> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        return ps;
                    }
                }, new ObjColumnsResultSetExtractor());
        return result;
    }

    /**
     * object Columns ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class ObjColumnsResultSetExtractor implements
            ResultSetExtractor<Map<String, Set<String>>> {
        @Override
        public Map<String, Set<String>> extractData(ResultSet rs)
                throws SQLException {
            Map<String, Set<String>> map = new HashMap<String, Set<String>>();
            while (rs.next()) {
                extractObjColumnsFromRs(rs, map);
            }
            return map;
        }
    }

    /**
     * extract object columns name from ResultSet.
     * 
     * @param rs
     *            ResultSet.
     * @param mapObjCols
     *            policy map.
     * @throws SQLException
     *             SQLException.
     */
    private void extractObjColumnsFromRs(ResultSet rs,
            Map<String, Set<String>> mapObjCols) throws SQLException {
        String strObj = rs.getString("OBJECT_TYPE");
        String strField = rs.getString("HIDE_COLUMN");

        if (mapObjCols.containsKey(strObj)) {
            Set<String> setColumns = mapObjCols.get(strObj);
            setColumns.add(strField);
            mapObjCols.put(strObj, setColumns);
        } else {
            Set<String> setColumns = new HashSet<String>();
            setColumns.add(strField);

            mapObjCols.put(strObj, setColumns);
        }
    }
}
