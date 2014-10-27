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

import org.restfulwhois.rdap.core.bean.TruncatedInfo;
import org.restfulwhois.rdap.core.common.util.CustomizeNoticeandRemark;
import org.restfulwhois.rdap.core.dao.QueryDao;
import org.restfulwhois.rdap.core.model.BaseNotice.NoticeType;
import org.restfulwhois.rdap.core.model.Link;
import org.restfulwhois.rdap.core.model.ModelType;
import org.restfulwhois.rdap.core.model.Notice;
import org.restfulwhois.rdap.dao.NoticeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.apache.commons.lang.StringUtils;
/**
 * notice query DAO select notice object from RDAP_NOTICE.
 * <p>
 * usually write notice object in front of domain,nameserver,entity,
 * network object etc.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class NoticeDaoImpl implements NoticeDao {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(NoticeDaoImpl.class);     
    /**
     * JDBC template simplifies the use of JDBC and helps to avoid common
     * errors.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * link query database api.
     */
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;
    /**
     * get notice list.
     * 
     * @return list of notice.
     */
    @Override
    public List<Notice> getNoticesNoTruncated() {
        LOGGER.debug("getAllNotices.");
        List<Notice> notices = queryWithoutInnerObjects(NoticeType.Notice);
        queryAndSetInnerObjects(notices, NoticeType.Notice);
        return notices;
    }
    /**
     * load notice list.
     * 
     * @return list of notice.
     */
    @Override
    public List<Notice> loadNoticesForTruncated() {
        LOGGER.debug("loadAllNotices.");
        final String typesJoinedByComma = StringUtils.join(
                             TruncatedInfo.TYPES, ",");
        final String sql = "select notice.*, description.description"
                + " from RDAP_NOTICE notice "
                + " left outer join RDAP_NOTICE_DESCRIPTION description "
                + " on notice.NOTICE_ID = description.NOTICE_ID "
                + " where notice.TYPE=? and notice.REASON_TYPE_SHORT_NAME in ( "
              + typesJoinedByComma + ")";
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
     * query help list.
     * 
     * @return list of help.
     */
    @Override
    public List<Notice> getHelp() {
        LOGGER.debug("getHelp.");
        List<Notice> notices = queryWithoutInnerObjects(NoticeType.HELP);
        queryAndSetInnerObjects(notices, NoticeType.HELP);
        return notices;
    }    
    
    /**
     * query inner objects, and set them to notices.
     * 
     * @param notices
     *            notice list
     * @param type
     *            the notice type.
     */
    private void queryAndSetInnerObjects(List<Notice> notices,
            NoticeType type) {
        if (null == notices || notices.size() == 0) {
            return;
        }
        for (Notice notice : notices) {
            queryAndSetInnerObjects(notice, type);
        }
    }

    /**
     * query inner objects, and set them to notice.
     * 
     * @param notice
     *            notice after set inner objects
     * @param type
     *            the notice type.
     */
    private void queryAndSetInnerObjects(Notice notice, NoticeType type) {
        if (null == notice || null == type) {
            return;
        }
        List<Link> links = null;
        if (NoticeType.Notice == type) {
            links = linkQueryDao.queryAsInnerObjects(notice.getId(),
                ModelType.NOTICE);
        } else if (NoticeType.HELP == type) {
            links = linkQueryDao.queryAsInnerObjects(notice.getId(),
                ModelType.HELP);
        }
        if (null != links) {
            notice.setLinks(links);
        }
    }

    /**
     * query notice from RDAP_NOTICE, without inner objects.
     * @param type notice type.
     * @return notice list
     */
    private List<Notice> queryWithoutInnerObjects(final NoticeType type) {
        final String typesJoinedByComma = StringUtils.join(
                TruncatedInfo.TYPES, ",");
        final String sql = "select notice.*, description.description"
                + " from RDAP_NOTICE notice "
                + " left outer join RDAP_NOTICE_DESCRIPTION description "
                + " on notice.NOTICE_ID = description.NOTICE_ID  where "
                + " notice.TYPE=? and ( notice.REASON_TYPE_SHORT_NAME not in ("
                +  typesJoinedByComma 
                +  " ) or notice.REASON_TYPE_SHORT_NAME is null)";
        List<Notice> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, type.getName());
                        return ps;
                    }
                }, new NoticeResultSetExtractor());
        return result;
    }

    /**
     * notice ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class NoticeResultSetExtractor implements ResultSetExtractor<List<Notice>> {
        @Override
        public List<Notice> extractData(ResultSet rs) throws SQLException {
            List<Notice> result = new ArrayList<Notice>();
            Map<Long, Notice> noticeMapById = new HashMap<Long, Notice>();
            while (rs.next()) {
                Long noticeId = rs.getLong("NOTICE_ID");
                Notice notice = noticeMapById.get(noticeId);
                if (null == notice) {
                    notice = new Notice();
                    notice.setId(noticeId);
                    notice.setTitle(rs.getString("TITLE"));
                    notice.setReasonType(rs.getString("REASON_TYPE"));
                    notice.setReasonTypeShortName(
                       rs.getString("REASON_TYPE_SHORT_NAME"));
                    noticeMapById.put(noticeId, notice);
                    result.add(notice);
                }
                notice.addDescription(rs.getString("DESCRIPTION"));
            }
            return result;
        }
    }    
}
