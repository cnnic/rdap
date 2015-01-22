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
package org.restfulwhois.rdap.core.autnum.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restfulwhois.rdap.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.common.dao.QueryDao;
import org.restfulwhois.rdap.common.dao.impl.SelfLinkGenerator;
import org.restfulwhois.rdap.common.model.Event;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.Remark;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.core.autnum.model.Autnum;
import org.restfulwhois.rdap.core.entity.model.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * autnum query DAO mainly select autnum object from RDAP_AUTNUM.
 * Meantime select related status from RDAP_AUTNUM_STATUS.
 * And select remark, event, link object as inner objects。
 * 
 * query, queryAsInnerObjects methods overrite the counterpart 
 * in abstractQueryDao.
 * ResultSetExtractor, createPreparedStatement implement from jdbc.
 * </pre>
 * 
 * @author jiashuo
 * 
 */
@Repository
public class AutnumQueryDaoImpl extends AbstractQueryDao<Autnum> {
    
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AutnumQueryDaoImpl.class);    
    
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
    
    /**
     * query an autnum.
     * 
     * @param queryParam
     *            param for an autnum.
     * @return Autnum
     *            autnum.
     */
    @Override
    public Autnum query(QueryParam queryParam) {
        LOGGER.debug("query, queryParam:" + queryParam);
        Autnum autnum = queryWithoutInnerObjects(queryParam);
        queryAndSetInnerObjects(autnum);
        queryAndSetEntities(autnum);
        LOGGER.debug("query, autnum:" + autnum);
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
    
    /**
     * query autnums associated to an outer object.
     * 
     * @param outerObjectId
     *            associated object id.
     * @param outerModelType
     *            associated object type.
     * @return List<Autnum>
     *            autnums.
     */
    @Override
    public List<Autnum> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        LOGGER.debug("queryAsInnerObjects, outerObjectId:" + outerObjectId
                + ", outerModelType:" + outerModelType);
        if (!ModelType.ENTITY.equals(outerModelType)) {
            throw new UnsupportedOperationException(
                    "only support ENTITY modelType.");
        }
        List<Autnum> autnums = queryWithoutInnerObjects(outerObjectId);
        queryAndSetInnerObjects(autnums);
        LOGGER.debug("queryAsInnerObjects, autnums:" + autnums);
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
     * <pre>
     * query autnum without inner objects.Only in ENTITY.
     * select data from RDAP_AUTNUM RDAP_AUTNUM_STATUS table.
     * </pre>
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
     *            autnum object.
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
        links.add(SelfLinkGenerator.generateSelfLink(autnum));
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
