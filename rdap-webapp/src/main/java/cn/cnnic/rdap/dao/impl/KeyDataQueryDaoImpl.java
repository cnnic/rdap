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

import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.KeyData;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.SecureDns.SecureDNSRType;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * keyData query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class KeyDataQueryDaoImpl extends AbstractQueryDao<KeyData> {
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
    public List<KeyData> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        List<KeyData> keyDataList = queryWithoutInnerObjects(outerObjectId);
        queryAndSetInnerObjects(keyDataList);
        return keyDataList;
    }

    /**
     * query inner objects, and set them to keyData.
     * 
     * @param keyDataList
     *            keyData list.
     */
    private void queryAndSetInnerObjects(List<KeyData> keyDataList) {
        if (null == keyDataList || keyDataList.size() == 0) {
            return;
        }
        for (KeyData keyData : keyDataList) {
            queryAndSetInnerObjects(keyData);
        }
    }

    /**
     * query inner objects, and set them to keyData.
     * 
     * @param keyData
     *            keyData.
     */
    private void queryAndSetInnerObjects(KeyData keyData) {
        if (null == keyData) {
            return;
        }
        List<Event> events = eventQueryDao.queryAsInnerObjects(keyData.getId(),
                ModelType.KEYDATA);
        keyData.setEvents(events);
        List<Link> links = linkQueryDao.queryAsInnerObjects(keyData.getId(),
                ModelType.KEYDATA);
        keyData.setLinks(links);
    }

    /**
     * query keyData, without inner objects.
     * @param outerObjectId outerObjectId.
     * @return keyData list.
     */
    private List<KeyData> queryWithoutInnerObjects(final Long outerObjectId) {
        final String sql = "select * from REL_SECUREDNS_DSKEY rel,RDAP_KEYDATA"
                + " keyData where rel.REL_ID = keyData.KEYDATA_ID "
                + " and rel.SECUREDNS_ID=? and rel.REL_DSKEY_TYPE=? ";
        List<KeyData> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, SecureDNSRType.KeyData.getName());
                        return ps;
                    }
                }, new KeyDataResultSetExtractor());
        return result;
    }

    /**
     * keyData ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class KeyDataResultSetExtractor implements
            ResultSetExtractor<List<KeyData>> {
        @Override
        public List<KeyData> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<KeyData> result = new ArrayList<KeyData>();
            while (rs.next()) {
                KeyData keyData = new KeyData();
                keyData.setId(rs.getLong("KEYDATA_ID"));
                keyData.setAlgorithm(getIntegerFromRs(rs, "ALGORITHM"));
                keyData.setPublicKey(rs.getString("PUBLIC_KEY"));
                keyData.setFlags(getIntegerFromRs(rs, "FLAGS"));
                keyData.setProtocol(getIntegerFromRs(rs, "PROTOCOL"));
                result.add(keyData);
            }
            return result;
        }
    }
}
