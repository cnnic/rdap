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
package org.restfulwhois.rdap.core.nameserver.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.core.common.dao.QueryDao;
import org.restfulwhois.rdap.core.common.dao.SearchDao;
import org.restfulwhois.rdap.core.common.dao.impl.LinkQueryDaoImpl;
import org.restfulwhois.rdap.core.common.model.Event;
import org.restfulwhois.rdap.core.common.model.Link;
import org.restfulwhois.rdap.core.common.model.Remark;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.ModelStatus;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.AutoGenerateSelfLink;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.core.entity.model.Entity;
import org.restfulwhois.rdap.core.ip.model.IPAddress;
import org.restfulwhois.rdap.core.nameserver.model.Nameserver;
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
 * nameserver query DAO select nameserver object from RDAP_NAMESERVER.
 * overwrite query queryAsInnerObject methods from AbstractQueryDao.
 * search batch nameservers by overwriting search method.
 * there are remark link event entity and ipAddress as inner objects.
 * </pre>
 * 
 * @author weijunkai
 * 
 */
@Repository
public class NameserverQueryDaoImpl extends AbstractQueryDao<Nameserver> {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(LinkQueryDaoImpl.class);
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
     * IPAddress dao.
     */
    @Autowired
    private QueryDao<IPAddress> ipAddressQueryDao;

    /**
     * entityQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;

    @Autowired
    @Qualifier("nameserverSearchDaoImpl")
    private SearchDao<Nameserver> searchDao;

    /**
     * query nameserver object as inner objects,and get the list of nameserver.
     * 
     * @param outerObjectId
     *            id from domain.
     * @return result for List<Nameserver>
     */
    private List<Nameserver> queryNameserverWithDomainID(
            final Long outerObjectId) {
        final String sql =
                "select * from RDAP_NAMESERVER ns inner join "
                        + "REL_DOMAIN_NAMESERVER rel on (ns.NAMESERVER_ID = "
                        + "rel.NAMESERVER_ID and rel.DOMAIN_ID = ?) "
                        + " left outer join RDAP_NAMESERVER_STATUS status"
                        + " on ns.NAMESERVER_ID = status.NAMESERVER_ID";

        List<Nameserver> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        return ps;
                    }
                }, new NSResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }

        return result;
    }

    @Override
    public List<Nameserver> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        List<Nameserver> listNameserver =
                queryNameserverWithDomainID(outerObjectId);
        if (listNameserver == null) {
            return null;
        }
        final int sizeNameserver = listNameserver.size();
        for (int i = 0; i < sizeNameserver; ++i) {
            queryAndSetInnerObjects(listNameserver.get(i));
        }
        return listNameserver;
    }

    @Override
    public Nameserver query(QueryParam queryParam) {
        Nameserver nameserver = queryWithoutInnerObjects(queryParam);
        queryAndSetInnerObjects(nameserver);
        queryAndSetEntities(nameserver);
        return nameserver;
    }

    /**
     * query inner objects of nameserver,and set object value to them.
     * 
     * @param ns
     *            nameserver which inner objects value will be filled.
     */
    private void queryAndSetInnerObjects(Nameserver ns) {
        if (null == ns) {
            return;
        }
        Long nsID = ns.getId();
        List<IPAddress> listIPAddress =
                ipAddressQueryDao.queryAsInnerObjects(nsID,
                        ModelType.NAMESERVER);
        if (listIPAddress.size() > 0) {
            IPAddress objIPAddress = listIPAddress.get(0);
            ns.setIpAddresses(objIPAddress);
        }
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(nsID, ModelType.NAMESERVER);
        ns.setRemarks(remarks);
        List<Link> links =
                linkQueryDao.queryAsInnerObjects(nsID, ModelType.NAMESERVER);
        links.add(AutoGenerateSelfLink.generateSelfLink(ns));
        ns.setLinks(links);
        List<Event> events =
                eventQueryDao.queryAsInnerObjects(nsID, ModelType.NAMESERVER);
        ns.setEvents(events);
    }

    /**
     * query entities from database and set them to nameserver.
     * 
     * @param ns
     *            nameserver which will be set.
     */
    private void queryAndSetEntities(Nameserver ns) {
        if (ns == null) {
            return;
        }
        List<Entity> entities =
                entityQueryDao.queryAsInnerObjects(ns.getId(),
                        ModelType.NAMESERVER);
        ns.setEntities(entities);
    }

    /**
     * query nameserver from RDAP_NAMESERVER using punyname, without inner
     * objects.
     * 
     * @param queryParam
     *            query parameter of Nameserver include punyname
     * @return nameserver object
     */
    private Nameserver queryWithoutInnerObjects(QueryParam queryParam) {
        DomainQueryParam nsQueryParam = (DomainQueryParam) queryParam;
        final String punyName = nsQueryParam.getPunyName();
        final String sql =
                "select * from RDAP_NAMESERVER ns "
                        + " left outer join RDAP_NAMESERVER_STATUS status "
                        + " on ns.NAMESERVER_ID = status.NAMESERVER_ID "
                        + " where LDH_NAME= ?";
        List<Nameserver> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, punyName);
                        return ps;
                    }
                }, new NSResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    /**
     * nameserver NSResultInnerExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class NSResultInnerExtractor implements
            ResultSetExtractor<List<Nameserver>> {
        @Override
        public List<Nameserver> extractData(ResultSet rs) throws SQLException {
            List<Nameserver> result = new ArrayList<Nameserver>();
            Map<Long, Nameserver> nsMapById = new HashMap<Long, Nameserver>();
            while (rs.next()) {
                Long nsId = rs.getLong("NAMESERVER_ID");
                Nameserver ns = nsMapById.get(nsId);
                if (null == ns) {
                    ns = new Nameserver();
                    ns.setId(nsId);
                    ns.setHandle(rs.getString("HANDLE"));
                    ns.setLdhName(rs.getString("LDH_NAME"));
                    ns.setUnicodeName(rs.getString("UNICODE_NAME"));
                    ns.setPort43(rs.getString("PORT43"));
                    ns.setLang(rs.getString("LANG"));
                    result.add(ns);
                    nsMapById.put(nsId, ns);
                }
            }
            return result;
        }
    }

    /**
     * nameserver ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class NSResultSetExtractor implements ResultSetExtractor<List<Nameserver>> {
        @Override
        public List<Nameserver> extractData(ResultSet rs) throws SQLException {
            List<Nameserver> result = new ArrayList<Nameserver>();
            Map<Long, Nameserver> nsMapById = new HashMap<Long, Nameserver>();
            while (rs.next()) {
                Long nsId = rs.getLong("NAMESERVER_ID");
                Nameserver ns = nsMapById.get(nsId);
                if (null == ns) {
                    ns = new Nameserver();
                    ns.setId(nsId);
                    extractNameserverFromRs(rs, ns);
                    result.add(ns);
                    nsMapById.put(nsId, ns);
                }
                ns.addStatus(rs.getString("STATUS"));
            }
            return result;
        }
    }

    @Override
    public void queryAndSetInnerObjectsForSearch(List<Nameserver> nameservers) {
        queryAndSetNameserverStatus(nameservers);
        queryAndSetInnerObjects(nameservers);
        queryAndSetEntities(nameservers);
    }

    /**
     * query and set entities to nameserver.
     * 
     * @param nameservers
     *            nameserver list which will be set with entities.
     */
    private void queryAndSetEntities(List<Nameserver> nameservers) {
        if (null == nameservers) {
            return;
        }
        for (Nameserver nameserver : nameservers) {
            queryAndSetEntities(nameserver);
        }
    }

    /**
     * <pre>
     * search nameservers from RDAP_NAMESERVER, without inner objects.
     * it can be indexed by ip,or by ldhName.
     * </pre>
     * 
     * @param queryParam
     *            query parameter of nameserver include index.
     * @return nameserver list.
     */
    private List<Nameserver> searchWithoutInnerObjects(
            final QueryParam queryParam) {
        return searchDao.search(queryParam);
    }

    /**
     * nameserver ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class NameserverResultSetExtractor implements
            ResultSetExtractor<List<Nameserver>> {
        @Override
        public List<Nameserver> extractData(ResultSet rs) throws SQLException {
            List<Nameserver> result = new ArrayList<Nameserver>();
            while (rs.next()) {
                Nameserver ns = new Nameserver();
                extractNameserverFromRs(rs, ns);
                result.add(ns);
            }
            return result;
        }
    }

    /**
     * extract nameserver from ResultSet.
     * 
     * @param rs
     *            ResultSet of sql.
     * @param nameserver
     *            nameserver object which will be extracted from.
     * @throws SQLException
     *             SQLException.
     */
    public static void extractNameserverFromRs(ResultSet rs,
            Nameserver nameserver) throws SQLException {
        nameserver.setId(rs.getLong("NAMESERVER_ID"));
        nameserver.setHandle(rs.getString("HANDLE"));
        nameserver.setLdhName(rs.getString("LDH_NAME"));
        nameserver.setUnicodeName(rs.getString("UNICODE_NAME"));
        nameserver.setPort43(rs.getString("PORT43"));
        nameserver.setLang(rs.getString("LANG"));
    }

    /**
     * query inner objects of nameserver,and set fill them to nameserver.
     * 
     * @param nameservers
     *            nameservers list.
     */
    private void queryAndSetInnerObjects(List<Nameserver> nameservers) {
        if (null == nameservers) {
            return;
        }
        for (Nameserver nameserver : nameservers) {
            queryAndSetInnerObjects(nameserver);
        }
    }

    /**
     * query and set nameserver status.
     * 
     * @param listNameserver
     *            nameserver list.
     */
    private void queryAndSetNameserverStatus(List<Nameserver> listNameserver) {
        List<Long> nameserverIds = getModelIds(listNameserver);
        List<ModelStatus> nameserverStatusList =
                queryNameserverStatus(nameserverIds);
        for (ModelStatus status : nameserverStatusList) {
            BaseModel obj =
                    BaseModel.findObjectFromListById(listNameserver,
                            status.getId());
            if (null == obj) {
                continue;
            }
            Nameserver nameserver = (Nameserver) obj;
            nameserver.addStatus(status.getStatus());
        }
    }

    /**
     * query nameserver status from RDAP_NAMESERVER_STATUS by nameserver id.
     * 
     * @param nameserverIds
     *            nameserver id list.
     * @return nameserver status list.
     */
    private List<ModelStatus> queryNameserverStatus(List<Long> nameserverIds) {
        if (null == nameserverIds || nameserverIds.size() == 0) {
            return new ArrayList<ModelStatus>();
        }
        final String nameserverIdsJoinedByComma =
                StringUtils.join(nameserverIds, ",");
        final String sqlTpl =
                "select * from RDAP_NAMESERVER_STATUS"
                        + " where NAMESERVER_ID in (%s)";
        final String sql = String.format(sqlTpl, nameserverIdsJoinedByComma);
        List<ModelStatus> result =
                jdbcTemplate.query(sql, new RowMapper<ModelStatus>() {
                    @Override
                    public ModelStatus mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new ModelStatus(rs.getLong("NAMESERVER_ID"), rs
                                .getString("STATUS"));
                    }
                });
        return result;
    }

}
