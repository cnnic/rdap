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
package org.restfulwhois.rdap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.dao.ConformanceDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * conformance query DAO mainly select conformance information 
 * from RDAP_CONFORMANCE.
 * <p>
 * these information will be valid after restart the system.
 * 
 * @author weijunkai
 * 
 */
@Repository
public class ConformanceDaoImpl implements ConformanceDao {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ConformanceDaoImpl.class);    
    
    /**
     * jdbcTemplate.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * query conformances from database.
     * 
     * @return List<String>
     *            return the queried records to conformance list.
     */
    @Override
    public List<String> queryConformance() {
        LOGGER.debug("query, conformances.");
        final String sql =
                "select * from RDAP_CONFORMANCE order by conformance_id";
        List<String> listConformance = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        return ps;
                    }
                }, new ConformanceResultSetExtractor());
        
        LOGGER.debug("query, Conformances:" + listConformance);
        return listConformance;
    }

    /**
     * object Columns ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class ConformanceResultSetExtractor implements
            ResultSetExtractor<List<String>> {
        @Override
        public List<String> extractData(ResultSet rs) throws SQLException {
            List<String> result = new ArrayList<String>();
            while (rs.next()) {
                extractConformanceFromRs(rs, result);
            }
            return result;
        }
    }

    /**
     * extract conformance string from ResultSet.
     * 
     * @param rs
     *            ResultSet.
     * @param result
     *            conformance list.
     * @throws SQLException
     *             SQLException.
     */
    private void extractConformanceFromRs(ResultSet rs, List<String> result)
            throws SQLException {
        String strConformance = rs.getString("RDAP_CONFORMANCE");
        if (result.contains(strConformance)) {
            return;
        }
        result.add(strConformance);
    }
}
