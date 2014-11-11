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
package org.restfulwhois.rdap.core.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.core.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.core.common.dao.QueryDao;
import org.restfulwhois.rdap.core.common.model.DsData;
import org.restfulwhois.rdap.core.common.model.Event;
import org.restfulwhois.rdap.core.common.model.Link;
import org.restfulwhois.rdap.core.common.model.SecureDns.SecureDNSRType;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * dsData query DAO select DsData from RDAP_DSDATA.
 * dsData only be queried as inner objects for domain.
 * </pre>
 * 
 * @author jiashuo
 * 
 */
@Repository
public class DsDataQueryDaoImpl extends AbstractQueryDao<DsData> {
    
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DsDataQueryDaoImpl.class);
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
    
    /**
     * query DsDatas associated to an outer object.
     * 
     * @param outerObjectId
     *            associated object id.
     * @param outerModelType
     *            associated object type.
     * @return List<DsData>
     *            DsData list.
     */
    @Override
    public List<DsData> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        LOGGER.debug("queryAsInnerObjects,outerObjId:{},outerModel:{}",
                outerObjectId, outerModelType);
        List<DsData> dsDataList = queryWithoutInnerObjects(outerObjectId);
        queryAndSetInnerObjects(dsDataList);
        LOGGER.debug("queryAsInnerObjects,dsDataList:{}",
                dsDataList);
        return dsDataList;
    }

    @Override
    public void queryAndSetInnerObjects(List<DsData> dsDataList) {
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
     *            dsData which will be filled with inner objects.
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
     * query dsData from RDAP_DSDATA table, without inner objects.
     * 
     * @param outerObjectId secure dns id which is used as key while querying.
     * @return dsData list object without inner objects.
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
        public List<DsData> extractData(ResultSet rs) throws SQLException {
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
