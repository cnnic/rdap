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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.BaseNotice.NoticeType;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Notice;
import cn.cnnic.rdap.dao.NoticeDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * notice query DAO
 * 
 * @author jiashuo
 * 
 */
@Repository
public class NoticeDaoImpl implements NoticeDao {
    /**
     * JDBC template simplifies the use of JDBC and helps to avoid common
     * errors.
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;

    @Override
    public List<Notice> getAllNotices() {
        List<Notice> notices = queryWithoutInnerObjects();
        queryAndSetInnerObjects(notices);
        return notices;
    }

    /**
     * query inner objects, and set them to notices
     * 
     * @param notices
     *            notice list
     */
    private void queryAndSetInnerObjects(List<Notice> notices) {
        if (null == notices || notices.size() == 0) {
            return;
        }
        for (Notice notice : notices) {
            queryAndSetInnerObjects(notice);
        }
    }

    /**
     * query inner objects, and set them to notice
     * 
     * @param notice
     *            notice after set inner objects
     */
    private void queryAndSetInnerObjects(Notice notice) {
        if (null == notice) {
            return;
        }
        List<Link> links = linkQueryDao.queryAsInnerObjects(notice.getId(),
                ModelType.NOTICE);
        notice.setLinks(links);
    }

    /**
     * query notice, without inner objects
     * 
     * @return notice list
     */
    private List<Notice> queryWithoutInnerObjects() {
        final String sql = "select notice.*, description.description  from RDAP_NOTICE notice "
                + " left outer join RDAP_NOTICE_DESCRIPTION description "
                + " on notice.NOTICE_ID = description.NOTICE_ID "
                + " where notice.TYPE=?";
        List<Notice> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, NoticeType.Notice.getName());
                        return ps;
                    }
                }, new NoticeResultSetExtractor());
        return result;
    }

    /**
     * notice ResultSetExtractor, extract data from ResultSet
     * 
     * @author jiashuo
     * 
     */
    class NoticeResultSetExtractor implements ResultSetExtractor<List<Notice>> {
        @Override
        public List<Notice> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Notice> result = new ArrayList<Notice>();
            Map<Long, Notice> noticeMapById = new HashMap<Long, Notice>();
            while (rs.next()) {
                Long noticeId = rs.getLong("NOTICE_ID");
                Notice notice = noticeMapById.get(noticeId);
                if (null == notice) {
                    notice = new Notice();
                    notice.setId(noticeId);
                    notice.setTitle(rs.getString("TITLE"));
                    noticeMapById.put(noticeId, notice);
                    result.add(notice);
                }
                notice.addDescription(rs.getString("DESCRIPTION"));
            }
            return result;
        }
    }
}