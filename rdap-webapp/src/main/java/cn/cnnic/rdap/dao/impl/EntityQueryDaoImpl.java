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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import cn.cnnic.rdap.bean.EntityAddress;
import cn.cnnic.rdap.bean.EntityTel;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelStatus;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.PageBean;
import cn.cnnic.rdap.bean.PublicId;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.common.RdapProperties;
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
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EntityQueryDaoImpl.class);

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
    /**
     * entityTelDao.
     */
    @Autowired
    private EntityTelDao entityTelDao;
    /**
     * entityAddressDao.
     */
    @Autowired
    private EntityAddressDao entityAddressDao;

    @Override
    public Entity query(QueryParam queryParam) {
        Entity entity = queryWithoutInnerObjects(queryParam);
        if (null == entity) {
            return entity;
        }
        queryAndSetInnerObjects(entity);
        return entity;
    }

    @Override
    public List<Entity> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        List<Entity> entities =
                queryWithoutInnerObjects(outerObjectId, outerModelType);
        if (null == entities) {
            return entities;
        }
        queryAndSetInnerObjects(entities);
        return entities;
    }

    @Override
    public List<Entity> search(QueryParam queryParam) {
        List<Entity> entities = searchWithoutInnerObjects(queryParam);
        queryAndSetEntityStatus(entities);
        queryAndSetInnerObjects(entities);
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
     * query inner objects of entity,and set fill them to entity.
     * 
     * @param entities
     *            entities.
     */
    private void queryAndSetInnerObjects(List<Entity> entities) {
        if (null == entities) {
            return;
        }
        for (Entity entity : entities) {
            queryAndSetInnerObjects(entity);
        }
    }

    /**
     * convert entity to vcard and set to entity.
     * 
     * @param entity
     *            entity.
     */
    private void convertAndSetVcardArray(Entity entity) {
        List<EntityTel> telephones = entityTelDao.query(entity);
        entity.setTelephones(telephones);
        List<EntityAddress> addresses = entityAddressDao.query(entity);
        entity.setAddresses(addresses);
        entity.setVcardArray(JcardUtil.toJcardString(entity));
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
     * @param entity
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjects(Entity entity) {
        if (null == entity) {
            return;
        }
        convertAndSetVcardArray(entity);
        queryAndSetStatus(entity);
//        queryAndSetEntities(entity);
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
        queryAndSetEvents(entity, entityId);
        List<Network> networks =
                networkQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setNetworks(networks);
        List<Autnum> autnums =
                autnumQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setAutnums(autnums);
        setTruncatedIfTooMuchResult(entity);
    }

    /**
     * query and set events and asEventActor.
     * @param entity entity.
     * @param entityId entityId.
     */
    private void queryAndSetEvents(Entity entity, Long entityId) {
        List<Event> events =
                eventQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setEvents(events);
        List<Event> eventsAsActor = new ArrayList<Event>();
        List<Event> eventsNotAsActor = new ArrayList<Event>();
        for(Event event:events){
            if(entity.getHandle().equals(event.getEventActor())){
                LOGGER.debug("asEventActor,entityId:{},eventId:{}",entityId,
                        event.getId());
                event.setEventActor(null);
                eventsAsActor.add(event);
            }else{
                eventsNotAsActor.add(event);
            }
        }
        entity.setEvents(eventsNotAsActor);
        entity.setAsEventActor(eventsAsActor);
    }

    /**
     * query and set entities.
     * 
     * @param entity
     *            entity.
     */
    private void queryAndSetEntities(Entity entity) {
        List<Entity> entities =
                queryAsInnerObjects(entity.getId(), ModelType.ENTITY);
        entity.setEntities(entities);
    }

    /**
     * set truncated as true if inner networks or autnums is exceed max size.
     * 
     * @param entity
     *            entity.
     */
    private void setTruncatedIfTooMuchResult(Entity entity) {
        List<Network> networks = entity.getNetworks();
        List<Autnum> autnums = entity.getAutnums();
        int maxInnerObjSize = RdapProperties.getMaxsizeSearch().intValue();
        if (null != networks && networks.size() > maxInnerObjSize) {
            LOGGER.debug(
                    "networks exceed max size:{},truncated.max size is {}",
                    networks.size(), maxInnerObjSize);
            List<Network> truncatedNetworks =
                    networks.subList(0, maxInnerObjSize);
            entity.setNetworks(truncatedNetworks);
            entity.setResultsTruncated(true);
        }
        if (null != autnums && autnums.size() > maxInnerObjSize) {
            LOGGER.debug("autnums exceed max size:{},truncated.max size is {}",
                    autnums.size(), maxInnerObjSize);
            List<Autnum> truncatedAutnums = autnums.subList(0, maxInnerObjSize);
            entity.setAutnums(truncatedAutnums);
            entity.setResultsTruncated(true);
        }
    }

    /**
     * query entity, without inner objects.Role from REL_ENTITY_REGISTRATION,
     * contain all role related is entity.
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
                        + " left outer join REL_ENTITY_REGISTRATION rel "
                        + " on entity.ENTITY_ID = rel.ENTITY_ID "
                        + " where entity.HANDLE= ? ";
        List<Entity> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, queryParam.getQ());
                        return ps;
                    }
                }, new EntityWithRoleResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    /**
     * query entity, without inner objects,for query as inner object.
     * 
     * @param outerObjectId
     *            outerObjectId.
     * @param outerModelType
     *            outerModelType.
     * @return entity.
     */
    private List<Entity> queryWithoutInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        final String sql = generateSqlForQueryRelEntity(outerModelType);
        List<Entity> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, outerModelType.getName());
                        return ps;
                    }
                }, new EntityWithRoleResultSetExtractor());
        return result;
    }

    /**
     * generate sql for query REL_ENTITY_REGISTRATION by REL_ID. Recode for
     * rel.ENTITY_ID = rel.REL_ID and rel.REL_OBJECT_TYPE= 'entity' represents
     * entity self relation,for store entity role such as 'registrar'.
     * 
     * @param outerModelType
     *            outerModelType.
     * @return sql.
     */
    private String generateSqlForQueryRelEntity(ModelType outerModelType) {
        final String sqlForNotEntityRel =
                "select * from RDAP_ENTITY entity "
                        + " inner join REL_ENTITY_REGISTRATION rel "
                        + " on entity.ENTITY_ID = rel.ENTITY_ID "
                        + " where rel.REL_ID= ? "
                        + " and rel.ENTITY_ID != rel.REL_ID "
                        + " and rel.REL_OBJECT_TYPE= ? ";
        final String sqlForEntityRel =
                "select * from RDAP_ENTITY entity "
                        + " inner join REL_ENTITY_REGISTRATION rel "
                        + " on entity.ENTITY_ID = rel.ENTITY_ID "
                        + " where rel.REL_ID= ? "
                        + " and rel.ENTITY_ID != rel.REL_ID "
                        + " and rel.REL_OBJECT_TYPE= ? ";
        if (ModelType.ENTITY.equals(outerModelType)) {
            return sqlForEntityRel;
        }
        return sqlForNotEntityRel;
    }

    /**
     * entity ResultSetExtractor, extract data ResultSet.
     * 
     * 
     * or jiashuo
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
     * extract entity ResultSet.
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
    class EntityWithRoleResultSetExtractor implements
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
                entity.addRole(rs.getString("ENTITY_ROLE"));
            }
            return result;
        }
    }

    /**
     * query and set status.
     * 
     * @param entity
     *            entity.
     */
    private void queryAndSetStatus(Entity entity) {
        List<Entity> models = new ArrayList<Entity>();
        if (null == entity) {
            return;
        }
        models.add(entity);
        queryAndSetStatus(models);
    }

    /**
     * query and set status.
     * 
     * @param models
     *            model list.
     */
    private void queryAndSetStatus(List<Entity> models) {
        List<Long> domainIds = getModelIds(models);
        List<ModelStatus> statusList = queryStatus(domainIds);
        for (ModelStatus status : statusList) {
            BaseModel obj =
                    BaseModel.findObjectFromListById(models, status.getId());
            if (null == obj) {
                continue;
            }
            Entity entity = (Entity) obj;
            entity.addStatus(status.getStatus());
        }
    }

    /**
     * query status.
     * 
     * @param modelIds
     *            model id list.
     * @return model status list.
     */
    private List<ModelStatus> queryStatus(List<Long> modelIds) {
        if (null == modelIds || modelIds.size() == 0) {
            return new ArrayList<ModelStatus>();
        }
        final String modelIdsJoinedByComma = StringUtils.join(modelIds, ",");
        final String sqlTpl =
                "select * from RDAP_ENTITY_STATUS where ENTITY_ID in (%s)";
        final String sql = String.format(sqlTpl, modelIdsJoinedByComma);
        List<ModelStatus> result =
                jdbcTemplate.query(sql, new RowMapper<ModelStatus>() {
                    @Override
                    public ModelStatus mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new ModelStatus(rs.getLong("ENTITY_ID"), rs
                                .getString("STATUS"));
                    }

                });
        return result;
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
