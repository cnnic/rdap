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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.BaseNotice.NoticeType;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * remark query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class RemarkQueryDaoImpl extends AbstractQueryDao<Remark> {
    /**
     * link dao.
     */
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;

    @Override
    public List<Remark> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        List<Remark> remarks = queryWithoutInnerObjects(outerObjectId,
                outerModelType);
        queryAndSetInnerObjects(remarks);
        return remarks;
    }

    /**
     * query inner objects, and set them to remarks.
     * 
     * @param remarks
     *            remark list.
     */
    private void queryAndSetInnerObjects(List<Remark> remarks) {
        if (null == remarks || remarks.size() == 0) {
            return;
        }
        for (Remark remark : remarks) {
            queryAndSetInnerObjects(remark);
        }
    }

    /**
     * query inner objects, and set them to remark.
     * 
     * @param remark
     *            remark after set inner objects.
     */
    private void queryAndSetInnerObjects(Remark remark) {
        if (null == remark) {
            return;
        }
        List<Link> links = linkQueryDao.queryAsInnerObjects(remark.getId(),
                ModelType.REMARK);
        remark.setLinks(links);
    }

    /**
     * query remark, without inner objects.
     * 
     * @param outerObjectId
     *            object id of outer object.
     * @param outerModelType
     *            model type of outer object.
     * @return remark list.
     */
    private List<Remark> queryWithoutInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        final String sql = "select notice.*, description.description "
                + " from RDAP_NOTICE notice"
                + " inner join REL_NOTICE_REGISTRATION rel "
                + " on (rel.NOTICE_ID = notice.NOTICE_ID and rel.REL_ID = ? "
                + " and rel.REL_OBJECT_TYPE = ? and notice.TYPE=?) "
                + " left outer join RDAP_NOTICE_DESCRIPTION description "
                + " on notice.NOTICE_ID = description.NOTICE_ID";
        List<Remark> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, outerModelType.getName());
                        ps.setString(3, NoticeType.REMARK.getName());
                        return ps;
                    }
                }, new RemarkResultSetExtractor());
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
        public List<Remark> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Remark> result = new ArrayList<Remark>();
            Map<Long, Remark> remarkMapById = new HashMap<Long, Remark>();
            while (rs.next()) {
                Long remarkId = rs.getLong("NOTICE_ID");
                Remark remark = remarkMapById.get(remarkId);
                if (null == remark) {
                    remark = new Remark();
                    remark.setId(remarkId);
                    remark.setTitle(rs.getString("TITLE"));
                    remarkMapById.put(remarkId, remark);
                    result.add(remark);
                }
                remark.addDescription(rs.getString("DESCRIPTION"));
            }
            return result;
        }
    }
}
