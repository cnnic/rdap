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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.DsData;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.SecureDns.SecureDNSRType;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * dsData query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class DsDataQueryDaoImpl extends AbstractQueryDao<DsData> {
    /**
     * event dao.
     */
    @Autowired
    @Qualifier("eventQueryDaoImpl")
    private QueryDao<Event> eventQueryDao;
    /**
     * link dao.
     */
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;

    @Override
    public List<DsData> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        List<DsData> dsDataList = queryWithoutInnerObjects(outerObjectId);
        queryAndSetInnerObjects(dsDataList);
        return dsDataList;
    }

    /**
     * query inner objects, and set them to dsData.
     * 
     * @param dsDataList
     *            dsData list.
     */
    private void queryAndSetInnerObjects(List<DsData> dsDataList) {
        if (null == dsDataList || dsDataList.size() == 0) {
            return;
        }
        for (DsData dsData : dsDataList) {
            queryAndSetInnerObjects(dsData);
        }
    }

    /**
     * query inner objects, and set them to dsData.
     * 
     * @param dsData
     *            dsData.
     */
    private void queryAndSetInnerObjects(DsData dsData) {
        if (null == dsData) {
            return;
        }
        List<Event> events = eventQueryDao.queryAsInnerObjects(dsData.getId(),
                ModelType.DSDATA);
        dsData.setEvents(events);
        List<Link> links = linkQueryDao.queryAsInnerObjects(dsData.getId(),
                ModelType.DSDATA);
        dsData.setLinks(links);
    }

    /**
     * query dsData, without inner objects.
     * 
     * @param outerObjectId outerObjectId.
     * @return dsData list.
     */
    private List<DsData> queryWithoutInnerObjects(final Long outerObjectId) {
        final String sql = "select * from REL_SECUREDNS_DSKEY rel,"
                + "RDAP_DSDATA ds " + " where rel.REL_ID = ds.DSDATA_ID "
                + " and rel.SECUREDNS_ID=? and rel.REL_DSKEY_TYPE=? ";
        List<DsData> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, SecureDNSRType.DsData.getName());
                        return ps;
                    }
                }, new DsDataResultSetExtractor());
        return result;
    }

    /**
     * dsData ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class DsDataResultSetExtractor implements ResultSetExtractor<List<DsData>> {
        @Override
        public List<DsData> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<DsData> result = new ArrayList<DsData>();
            while (rs.next()) {
                DsData dsData = new DsData();
                dsData.setId(rs.getLong("DSDATA_ID"));
                dsData.setAlgorithm(getIntegerFromRs(rs, "ALGORITHM"));
                dsData.setDigest(rs.getString("DIGEST"));
                dsData.setDigestType(getIntegerFromRs(rs, "DIGEST_TYPE"));
                dsData.setKeyTag(getIntegerFromRs(rs, "KEY_TAG"));
                result.add(dsData);
            }
            return result;
        }
    }
}
