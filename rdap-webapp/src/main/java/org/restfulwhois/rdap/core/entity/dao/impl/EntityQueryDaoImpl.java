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
package org.restfulwhois.rdap.core.entity.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.autnum.model.Autnum;
import org.restfulwhois.rdap.core.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.core.common.dao.QueryDao;
import org.restfulwhois.rdap.core.common.dao.SearchDao;
import org.restfulwhois.rdap.core.common.model.Event;
import org.restfulwhois.rdap.core.common.model.Link;
import org.restfulwhois.rdap.core.common.model.PublicId;
import org.restfulwhois.rdap.core.common.model.Remark;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.ModelStatus;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo.TruncateReason;
import org.restfulwhois.rdap.core.common.util.AutoGenerateSelfLink;
import org.restfulwhois.rdap.core.common.util.CustomizeNoticeandRemark;
import org.restfulwhois.rdap.core.common.util.RdapProperties;
import org.restfulwhois.rdap.core.entity.model.Entity;
import org.restfulwhois.rdap.core.entity.model.EntityAddress;
import org.restfulwhois.rdap.core.entity.model.EntityRole;
import org.restfulwhois.rdap.core.entity.model.EntityTel;
import org.restfulwhois.rdap.core.entity.model.jcard.Jcard;
import org.restfulwhois.rdap.core.ip.model.Network;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * entity query DAO select entity object from RDAP_ENTITY.
 * overwrite query method to select entity without inner object.
 * overwrite queryAsInnerObject method to select inner objects within entity.
 * overwrite search method to finish batch query.
 * there also can be entities in entity object.
 * </pre>
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

    @Autowired
    @Qualifier("entitySearchDaoImpl")
    private SearchDao<Entity> searchDao;

    @Override
    public Entity query(QueryParam queryParam) {
        Entity entity = queryWithoutInnerObjects(queryParam);
        if (null == entity) {
            return entity;
        }
        queryAndSetNetworksAndAs(entity);
        queryAndSetInnerObjectsWithoutEntities(entity);
        queryAndSetInnerEntities(entity);
        //queryAndSetNetworksAndAs(entity);
        return entity;
    }

    @Override
    public List<Entity> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        LOGGER.debug("queryAsInnerObjects,outerObjectId:{},outerModelType:{}",
                outerObjectId, outerModelType.getName());
        List<Entity> entities =
                queryWithoutInnerObjects(outerObjectId, outerModelType);
        LOGGER.debug("queryAsInnerObjects,results:{}", entities);
        if (null == entities) {
            return entities;
        }
        queryAndSetInnerObjectsWithoutEntities(entities);
        return entities;
    }
    
    @Override
    public void queryAndSetInnerObjectsForSearch(List<Entity> entities) {
        queryAndSetNetworksAndAs(entities);
        queryAndSetInnerObjectsWithoutEntities(entities);
        queryAndSetRoles(entities);        
        queryAndSetInnerEntities(entities);
    }

    /**
     * query and set inner entities.
     * @param entities entities which will be set to entity.
     */
    private void queryAndSetInnerEntities(List<Entity> entities) {
        if (null == entities) {
            return;
        }
        for (Entity entity : entities) {
            queryAndSetInnerEntities(entity);
        }
    }

    /**
     * query and set network and autnum.
     * 
     * @param entity
     *            entity which will be filled with network and autnum.
     */
    private void queryAndSetNetworksAndAs(Entity entity) {
        List<Network> networks =
                networkQueryDao.queryAsInnerObjects(entity.getId(),
                        ModelType.ENTITY);
        entity.setNetworks(networks);
        List<Autnum> autnums =
                autnumQueryDao.queryAsInnerObjects(entity.getId(),
                        ModelType.ENTITY);
        entity.setAutnums(autnums);
        setTruncatedIfTooMuchResult(entity);
    }
    
    /**
     * query and set network and autnum.
     * @param entities entity list.
     */
    private void queryAndSetNetworksAndAs(List<Entity> entities) {
        if (null == entities) {
            return;
        }
        for (Entity entity : entities) {
            queryAndSetNetworksAndAs(entity);
        }
    }

    /**
     * query inner objects of entity,and fill them to entity.
     * 
     * @param entities
     *            entity list.
     */
    private void queryAndSetInnerObjectsWithoutEntities(List<Entity> entities) {
        if (null == entities) {
            return;
        }
        for (Entity entity : entities) {
            queryAndSetInnerObjectsWithoutEntities(entity);
        }
    }

    /**
     * convert entity to vcard and set to entity.
     * 
     * @param entity
     *            entity object.
     */
    private void convertAndSetVcardArray(Entity entity) {
        List<EntityTel> telephones = entityTelDao.query(entity);
        entity.setTelephones(telephones);
        List<EntityAddress> addresses = entityAddressDao.query(entity);
        entity.setAddresses(addresses);
        entity.setVcardArray(Jcard.build(entity).toJSON());
    }

    /**
     * query inner objects of entity,and set fill them to entity.
     * 
     * @param entity
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjectsWithoutEntities(Entity entity) {
        if (null == entity) {
            return;
        }
        LOGGER.debug("queryAndSetInnerObjectsWithoutEntities,entityHandle:{}",
                entity.getHandle());
        convertAndSetVcardArray(entity);
        queryAndSetStatus(entity);
        Long entityId = entity.getId();
        List<PublicId> publicIds =
                publicIdQueryDao
                        .queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setPublicIds(publicIds);
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        if (entity.getTruncatedInfo() != null 
                      && entity.getTruncatedInfo().getResultsTruncated()) {
             List <TruncateReason> truncateReasons = entity
                    .getTruncatedInfo().getTruncateReasons();
            for (TruncateReason truncateReason : truncateReasons) {
                  remarks.add(CustomizeNoticeandRemark
                          .getRemarkByReasonType(truncateReason.getName()));
            }  
        }
        entity.setRemarks(remarks);
        List<Link> links =
                linkQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        links.add(AutoGenerateSelfLink.generateSelfLink(entity));
        entity.setLinks(links);
        queryAndSetEvents(entity, entityId);
    }

    /**
     * query and set events and asEventActor.
     * 
     * @param entity
     *            entity.
     * @param entityId
     *            entityId.
     */
    private void queryAndSetEvents(Entity entity, Long entityId) {
        List<Event> events =
                eventQueryDao.queryAsInnerObjects(entityId, ModelType.ENTITY);
        entity.setEvents(events);
        List<Event> eventsAsActor = new ArrayList<Event>();
        List<Event> eventsNotAsActor = new ArrayList<Event>();
        for (Event event : events) {
            if (entity.getHandle().equals(event.getEventActor())) {
                LOGGER.debug("asEventActor,entityId:{},eventId:{}", entityId,
                        event.getId());
                event.setEventActor(null);
                eventsAsActor.add(event);
            } else {
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
    private void queryAndSetInnerEntities(Entity entity) {
        LOGGER.debug("queryAndSetInnerEntities,entityHandle:{}",
                entity.getHandle());
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
        TruncatedInfo truncatedInfo = new TruncatedInfo();
        if (null != networks && networks.size() > maxInnerObjSize) {
            LOGGER.debug(
                    "networks exceed max size:{},truncated.max size is {}",
                    networks.size(), maxInnerObjSize);
            List<Network> truncatedNetworks =
                    networks.subList(0, maxInnerObjSize);
            entity.setNetworks(truncatedNetworks);
            truncatedInfo.addTruncate(TruncateReason.TRUNCATEREASON_EXLOAD);
            entity.setTruncatedInfo(truncatedInfo);
        }
        if (null != autnums && autnums.size() > maxInnerObjSize) {
            LOGGER.debug("autnums exceed max size:{},truncated.max size is {}",
                    autnums.size(), maxInnerObjSize);
            List<Autnum> truncatedAutnums = autnums.subList(0, maxInnerObjSize);
            entity.setAutnums(truncatedAutnums);
            truncatedInfo.addTruncate(TruncateReason.TRUNCATEREASON_EXLOAD);
            entity.setTruncatedInfo(truncatedInfo);
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
     * extract entity ResultSet.
     * 
     * @param rs
     *            ResultSet will be set to entity.
     * @param entity
     *            entity to be set.
     * @throws SQLException
     *             SQLException.
     */
    public static void extractEntityFromRs(ResultSet rs, Entity entity)
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
        entity.setLang(rs.getString("LANG"));
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
     * query and set status to entity.
     * 
     * @param entity
     *            entity which will be set.
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
     * query and set status to entity list.
     * 
     * @param models
     *            entity list which will be filled with status.
     */
    private void queryAndSetStatus(List<Entity> models) {
        List<Long> entityIds = getModelIds(models);
        List<ModelStatus> statusList = queryStatus(entityIds);
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
     * query and set roles to entity list.
     * 
     * @param models
     *            entity list which will be filled with roles.
     */
    private void queryAndSetRoles(List<Entity> models) {
        List<Long> entityIds = getModelIds(models);
        List<EntityRole> roleList = queryRoles(entityIds);
        for (EntityRole role : roleList) {
            BaseModel obj =
                    BaseModel.findObjectFromListById(models, role.getId());
            if (null == obj) {
                continue;
            }
            Entity entity = (Entity) obj;
            entity.addRole(role.getRole());
        }
    }

    /**
     * query status from RDAP_ENTITY_STATUS.
     * 
     * @param modelIds
     *            model id list as the query parameter.
     * @return model status list.
     */
    private List<ModelStatus> queryStatus(List<Long> modelIds) {
        if (null == modelIds || modelIds.size() == 0) {
            return new ArrayList<ModelStatus>();
        }
        final String idsJoinedByComma = StringUtils.join(modelIds, ",");
        final String sqlTpl =
                "select * from RDAP_ENTITY_STATUS where ENTITY_ID in (%s)";
        final String sql = String.format(sqlTpl, idsJoinedByComma);
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
     * query roles from REL_ENTITY_REGISTRATION by entity id.
     * @param entityIds entity id list to check.
     * @return entity role list queried from database.
     */
    private List<EntityRole> queryRoles(List<Long> entityIds) {
        if (null == entityIds || entityIds.size() == 0) {
            return new ArrayList<EntityRole>();
        }
        final String idsJoinedByComma = StringUtils.join(entityIds, ",");
        final String sqlTpl =
                "select * from REL_ENTITY_REGISTRATION where ENTITY_ID in (%s)";
        final String sql = String.format(sqlTpl, idsJoinedByComma);
        List<EntityRole> result =
                jdbcTemplate.query(sql, new RowMapper<EntityRole>() {
                    @Override
                    public EntityRole mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new EntityRole(rs.getLong("ENTITY_ID"), rs
                                .getString("ENTITY_ROLE"));
                    }
                });
        return result;
    }

    /**
     * search entity from RDAP_ENTITY, without inner objects.
     * 
     * @param params
     *            query parameter include entity's name.
     * @return entity list which will be filled with data.
     */
    private List<Entity> searchWithoutInnerObjects(final QueryParam queryParam) {
        return searchDao.search(queryParam);
    }
}
