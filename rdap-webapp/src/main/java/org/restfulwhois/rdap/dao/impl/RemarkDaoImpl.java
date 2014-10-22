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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.model.BaseNotice.NoticeType;
import org.restfulwhois.rdap.core.model.Remark;
import org.restfulwhois.rdap.dao.RemarkDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;


/**
 * remark DAO implementation get  remark from database.
 * while the system is starting..
 * 
 * @author zhanyq
 * 
 */
@Repository
public class RemarkDaoImpl implements RemarkDao {
    
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(RemarkDaoImpl.class);
    
    /**
     * jdbcTemplate.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Remark> loadRemarksByTypes(List<String> types) {
         LOGGER.debug("loadRemarksByTypes, types:{},");
         final String typesJoinedByComma = StringUtils.join(types, ",");
         final String sql = "select notice.*, description.description"
              + " from RDAP_NOTICE notice "
              + " left outer join RDAP_NOTICE_DESCRIPTION description "
              + " on notice.NOTICE_ID = description.NOTICE_ID "
              + " where notice.TYPE=? and notice.REASON_TYPE_SHORT_NAME in ( "
              + typesJoinedByComma + ")";
         List<Remark> result = jdbcTemplate.query(
                 new PreparedStatementCreator() {
                     public PreparedStatement createPreparedStatement(
                             Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, NoticeType.REMARK.getName());
                        return ps;
                    }
                 }, new RemarkResultSetExtractor());
         LOGGER.debug("queryAsInnerObjects, result:{}", result);
         return result;       
                  
    }
    /**
     * remark ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class RemarkResultSetExtractor implements ResultSetExtractor<List<Remark>> {
        @Override
        public List<Remark> extractData(ResultSet rs) throws SQLException {
            List<Remark> result = new ArrayList<Remark>();
            Map<Long, Remark> remarkMapById = new HashMap<Long, Remark>();
            while (rs.next()) {
                Long remarkId = rs.getLong("NOTICE_ID");
                Remark remark = remarkMapById.get(remarkId);
                if (null == remark) {
                    remark = new Remark();
                    remark.setId(remarkId);
                    remark.setTitle(rs.getString("TITLE"));
                    remark.setReasonType(rs.getString("REASON_TYPE"));
                    remark.setReasonTypeShortName(
                           rs.getString("REASON_TYPE_SHORT_NAME"));
                    remarkMapById.put(remarkId, remark);
                    result.add(remark);
                }
                remark.addDescription(rs.getString("DESCRIPTION"));
            }
            return result;
        }
    }

}