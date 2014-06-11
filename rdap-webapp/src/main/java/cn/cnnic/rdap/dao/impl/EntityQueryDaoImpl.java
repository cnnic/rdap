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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.bean.DomainQueryParam;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelStatus;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.PageBean;
import cn.cnnic.rdap.bean.PublicId;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.common.util.JcardUtil;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * entity query DAO.
 *
 * @author jiashuo
 *
 */
@Repository
public class EntityQueryDaoImpl extends AbstractQueryDao<Entity> {

    /**
     * publicId dao.
     */
    @Autowired
    private QueryDao<PublicId> publicIdQueryDao;
    /**
     * remark dao.
     */
    @Autowired
    @Qualifier("remarkQueryDaoImpl")
    private QueryDao<Remark> remarkQueryDao;
    /**
     * link dao.
     */
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;
    /**
     * event dao.
     */
    @Autowired
    @Qualifier("eventQueryDaoImpl")
    private QueryDao<Event> eventQueryDao;
    /**
     * autnum dao.
     */
    @Autowired
    @Qualifier("autnumQueryDaoImpl")
    private QueryDao<Autnum> autnumQueryDao;
    /**
     * network dao.
     */
    @Autowired
    private QueryDao<Network> networkQueryDao;

    @Override
    public Entity query(QueryParam queryParam) {
        Entity entity = queryWithoutInnerObjects(queryParam);
        if (null == entity) {
            return entity;
        }
        entity.setVcardArray(JcardUtil.toJcardString(entity));
        queryAndSetInnerObjects(entity);
        return entity;
    }

    @Override
    public List<Entity> search(QueryParam queryParam) {
        List<Entity> entities = searchWithoutInnerObjects(queryParam);
        queryAndSetEntityStatus(entities);
        queryAndSetInnerObjectsWithoutNotice(entities);
        return entities;
    }

    @Override
    public Long searchCount(QueryParam queryParam) {
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        final String domainName = domainQueryParam.getQ();
        final String punyName = domainQueryParam.getPunyName();
        final String domainNameLikeClause =
                super.generateLikeClause(domainName);
        final String punyNameLikeClause = super.generateLikeClause(punyName);
        final String sql =
                "select count(1) as COUNT from RDAP_DOMAIN domain "
                        + " where LDH_NAME like ? or UNICODE_NAME like ? ";
        Long entityCount = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, punyNameLikeClause);
                ps.setString(2, domainNameLikeClause);
                return ps;
            }
        }, new CountResultSetExtractor());
        return entityCount;
    }

    /**
     * query and set entity status.
     *
     * @param entities
     *            entity list.
     */
    private void queryAndSetEntityStatus(List<Entity> entities) {
        List<Long> entityIds = getModelIds(entities);
        List<ModelStatus> entityStatusList = queryEntityStatus(entityIds);
        for (ModelStatus status : entityStatusList) {
            BaseModel obj =
                    BaseModel.findObjectFromListById(entities, status.getId());
            if (null == obj) {
                continue;
            }
            Entity entity = (Entity) obj;
            entity.addStatus(status.getStatus());
        }
    }

    /**
     * query entity status.
     *
     * @param entityIds
     *            entity id list.
     * @return entity status list.
     */
    private List<ModelStatus> queryEntityStatus(List<Long> entityIds) {
        if (null == entityIds || entityIds.size() == 0) {
            return new ArrayList<ModelStatus>();
        }
        final String domainsIdsJoinedByComma = StringUtils.join(entityIds, ",");
        final String sqlTpl =
                "select * from RDAP_DOMAIN_STATUS where DOMAIN_ID in (%s)";
        final String sql = String.format(sqlTpl, domainsIdsJoinedByComma);
        List<ModelStatus> result =
                jdbcTemplate.query(sql, new RowMapper<ModelStatus>() {
                    @Override
                    public ModelStatus mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new ModelStatus(rs.getLong("DOMAIN_ID"), rs
                                .getString("STATUS"));
                    }

                });
        return result;
    }

    /**
     * query inner objects of entity,and set fill them to entity.
     *
     * @param entities
     *            domain list.
     */
    private void queryAndSetInnerObjectsWithoutNotice(List<Entity> entities) {
        if (null == entities) {
            return;
        }
        for (Entity entity : entities) {
            queryAndSetInnerObjects(entity);
        }
    }

    /**
     * query inner objects of entity,and set fill them to entity.
     *
     * @param entity
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjects(Entity entity) {
        if (null == entity) {
            return;
        }
        Long entityId = entity.getId();
        List<PublicId> publicIds =
                publicIdQueryDao
                        .queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setPublicIds(publicIds);
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setRemarks(remarks);
        List<Link> links =
                linkQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setLinks(links);
        List<Event> events =
                eventQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setEvents(events);
        List<Network> networks =
                networkQueryDao.queryAsInnerObjects(entityId, ModelType.IP);
        entity.setNetworks(networks);
        List<Autnum> autnums =
                autnumQueryDao.queryAsInnerObjects(entityId, ModelType.AUTNUM);
        entity.setAutnums(autnums);
    }

    /**
     * query entity, without inner objects.
     *
     * @param queryParam
     *            query parameter.
     * @return entity.
     */
    private Entity queryWithoutInnerObjects(final QueryParam queryParam) {
        if (StringUtils.isBlank(queryParam.getQ())) {
            return null;
        }
        final String sql =
                "select * from RDAP_ENTITY entity "
                        + " left outer join RDAP_ENTITY_STATUS status "
                        + " on entity.ENTITY_ID = status.ENTITY_ID "
                        + " left outer join RDAP_ENTITY_ROLE role "
                        + " on entity.ENTITY_ID = role.ENTITY_ID "
                        + " where entity.HANDLE= ? ";
        List<Entity> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, queryParam.getQ());
                        return ps;
                    }
                }, new EntityWithStatusResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    /**
     * entity ResultSetExtractor, extract data from ResultSet.
     *
     * @author jiashuo
     *
     */
    class EntityResultSetExtractor implements ResultSetExtractor<List<Entity>> {
        @Override
        public List<Entity> extractData(ResultSet rs) throws SQLException {
            List<Entity> result = new ArrayList<Entity>();
            while (rs.next()) {
                Entity entity = new Entity();
                extractEntityFromRs(rs, entity);
                result.add(entity);
            }
            return result;
        }
    }

    /**
     * extract entity from ResultSet.
     *
     * @param rs
     *            ResultSet.
     * @param entity
     *            entity.
     * @throws SQLException
     *             SQLException.
     */
    private void extractEntityFromRs(ResultSet rs, Entity entity)
            throws SQLException {
        entity.setId(rs.getLong("ENTITY_ID"));
        entity.setHandle(rs.getString("HANDLE"));
        entity.setKind(rs.getString("KIND"));
        entity.setFn(rs.getString("FN"));
        entity.setEmail(rs.getString("EMAIL"));
        entity.setTitle(rs.getString("TITLE"));
        entity.setOrg(rs.getString("ORG"));
        entity.setUrl(rs.getString("URL"));
        entity.setPort43(rs.getString("PORT43"));
    }

    /**
     * entity ResultSetExtractor, extract data from ResultSet.
     *
     * @author jiashuo
     *
     */
    class EntityWithStatusResultSetExtractor implements
            ResultSetExtractor<List<Entity>> {
        @Override
        public List<Entity> extractData(ResultSet rs) throws SQLException {
            List<Entity> result = new ArrayList<Entity>();
            Map<Long, Entity> entityMapById = new HashMap<Long, Entity>();
            while (rs.next()) {
                Long entityId = rs.getLong("ENTITY_ID");
                Entity entity = entityMapById.get(entityId);
                if (null == entity) {
                    entity = new Entity();
                    extractEntityFromRs(rs, entity);
                    result.add(entity);
                    entityMapById.put(entityId, entity);
                }
                entity.addStatus(rs.getString("STATUS"));
                entity.addRole(rs.getString("ROLE"));
            }
            return result;
        }
    }

    /**
     *
     * @author jiashuo
     *
     */
    class CountResultSetExtractor implements ResultSetExtractor<Long> {
        @Override
        public Long extractData(ResultSet rs) throws SQLException {
            Long result = 0L;
            if (rs.next()) {
                result = rs.getLong("COUNT");
            }
            return result;
        }
    }

    /**
     * search domain, without inner objects.
     *
     * @param params
     *            query parameter.
     * @return domain list.
     */
    private List<Entity> searchWithoutInnerObjects(final QueryParam params) {
        DomainQueryParam domainQueryParam = (DomainQueryParam) params;
        final String domainName = domainQueryParam.getQ();
        final String punyName = domainQueryParam.getPunyName();
        final String domainNameLikeClause =
                super.generateLikeClause(domainName);
        final String punyNameLikeClause = super.generateLikeClause(punyName);
        final String sql =
                "select * from RDAP_DOMAIN domain "
                        + " where LDH_NAME like ? or UNICODE_NAME like ? "
                        + " order by domain.LDH_NAME limit ?,? ";
        final PageBean page = params.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        List<Entity> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, punyNameLikeClause);
                        ps.setString(2, domainNameLikeClause);
                        ps.setLong(3, startRow);
                        ps.setLong(4, page.getMaxRecords());
                        return ps;
                    }
                }, new EntityResultSetExtractor());
        return result;
    }
}
