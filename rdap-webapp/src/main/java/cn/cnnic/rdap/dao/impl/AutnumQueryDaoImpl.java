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
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * autnum query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class AutnumQueryDaoImpl extends AbstractQueryDao<Autnum> {
    /**
     * remarkQueryDao.
     */
    @Autowired
    @Qualifier("remarkQueryDaoImpl")
    private QueryDao<Remark> remarkQueryDao;
    /**
     * linkQueryDao.
     */
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;
    /**
     * eventQueryDao.
     */
    @Autowired
    @Qualifier("eventQueryDaoImpl")
    private QueryDao<Event> eventQueryDao;

    /**
     * entityQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;

    @SuppressWarnings("unchecked")
    @Override
    public Autnum query(QueryParam queryParam) {
        Autnum autnum = queryWithoutInnerObjects(queryParam);
        queryAndSetInnerObjects(autnum);
        queryAndSetEntities(autnum);
        return autnum;
    }

    /**
     * query and set entities.
     * @param autnum autnum.
     */
    private void queryAndSetEntities(Autnum autnum) {
        if (null == autnum) {
            return;
        }
        List<Entity> entities =
                entityQueryDao.queryAsInnerObjects(autnum.getId(),
                        ModelType.AUTNUM);
        autnum.setEntities(entities);
    }

    @Override
    public List<Autnum> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        if (!ModelType.ENTITY.equals(outerModelType)) {
            throw new UnsupportedOperationException(
                    "only support ENTITY modelType.");
        }
        List<Autnum> autnums = queryWithoutInnerObjects(outerObjectId);
        queryAndSetInnerObjects(autnums);
        return autnums;
    }

    /**
     * query inner objects of autnums,and set them to autnum.
     * 
     * @param autnums
     *            autnums.
     */
    private void queryAndSetInnerObjects(List<Autnum> autnums) {
        if (null == autnums) {
            return;
        }
        for (Autnum autnum : autnums) {
            queryAndSetInnerObjects(autnum);
        }
    }

    /**
     * query autnum without inner objects.Only in ENTITY!
     * 
     * @param outerObjectId
     *            entity id.
     * @return autnum list.
     */
    private List<Autnum> queryWithoutInnerObjects(final Long outerObjectId) {
        final String sql =
                "select * from RDAP_AUTNUM autnum inner join "
                        + " REL_ENTITY_REGISTRATION rel "
                        + " on autnum.AS_ID = rel.REL_ID "
                        + " left outer join RDAP_AUTNUM_STATUS status "
                        + " on autnum.AS_ID = status.AS_ID "
                        + " where rel.ENTITY_ID=? and REL_OBJECT_TYPE=?"
                        + " order by autnum.HANDLE ";
        List<Autnum> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, ModelType.AUTNUM.getName());
                        return ps;
                    }
                }, new AutnumResultSetExtractor());
        return result;
    }

    /**
     * query inner objects of autnum,and set them to autnum.
     * 
     * @param autnum
     *            autnum.
     */
    private void queryAndSetInnerObjects(Autnum autnum) {
        if (null == autnum) {
            return;
        }
        Long autnumId = autnum.getId();
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(autnumId, ModelType.AUTNUM);
        autnum.setRemarks(remarks);
        List<Link> links =
                linkQueryDao.queryAsInnerObjects(autnumId, ModelType.AUTNUM);
        autnum.setLinks(links);
        List<Event> events =
                eventQueryDao.queryAsInnerObjects(autnumId, ModelType.AUTNUM);
        autnum.setEvents(events);
    }

    /**
     * query autnum, without inner objects.
     * 
     * @param queryParam
     *            query parameter
     * @return autnum
     */
    private Autnum queryWithoutInnerObjects(QueryParam queryParam) {
        final String autnumQ = queryParam.getQ();
        final String sql =
                "select *,end_autnum - start_autnum as asInterval "
                        + " from RDAP_AUTNUM autnum "
                        + " left outer join RDAP_AUTNUM_STATUS status "
                        + " on autnum.as_id = status.as_id "
                        + " where autnum.start_autnum <= ? and end_autnum >= ?"
                        + " order by asInterval ";
        List<Autnum> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, autnumQ);
                        ps.setString(2, autnumQ);
                        return ps;
                    }
                }, new AutnumResultSetExtractor());
        Autnum autnum = null;
        if (null != result && result.size() > 0) {
            autnum = result.get(0);
        }
        return autnum;
    }

    /**
     * autnum ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class AutnumResultSetExtractor implements ResultSetExtractor<List<Autnum>> {
        @Override
        public List<Autnum> extractData(ResultSet rs) throws SQLException {
            List<Autnum> result = new ArrayList<Autnum>();
            Map<Long, Autnum> autnumMapById = new HashMap<Long, Autnum>();
            while (rs.next()) {
                Long autnumId = rs.getLong("as_id");
                Autnum autnum = autnumMapById.get(autnumId);
                if (null == autnum) {
                    autnum = new Autnum();
                    autnum.setId(autnumId);
                    autnum.setHandle(rs.getString("HANDLE"));
                    autnum.setStartAutnum(rs.getLong("START_AUTNUM"));
                    autnum.setEndAutnum(rs.getLong("END_AUTNUM"));
                    autnum.setName(rs.getString("NAME"));
                    autnum.setType(rs.getString("TYPE"));
                    autnum.setCountry(rs.getString("COUNTRY"));
                    autnum.setLang(rs.getString("LANG"));
                    autnum.setPort43(rs.getString("PORT43"));
                    result.add(autnum);
                    autnumMapById.put(autnumId, autnum);
                }
                autnum.addStatus(rs.getString("STATUS"));
            }
            return result;
        }
    }
}
