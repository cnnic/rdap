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
package org.restfulwhois.rdap.core.domain.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restfulwhois.rdap.core.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.core.common.dao.QueryDao;
import org.restfulwhois.rdap.core.common.dao.SearchDao;
import org.restfulwhois.rdap.core.common.model.Event;
import org.restfulwhois.rdap.core.common.model.Link;
import org.restfulwhois.rdap.core.common.model.PublicId;
import org.restfulwhois.rdap.core.common.model.Remark;
import org.restfulwhois.rdap.core.common.model.SecureDns;
import org.restfulwhois.rdap.core.common.model.Variants;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.AutoGenerateSelfLink;
import org.restfulwhois.rdap.core.common.util.IpUtil;
import org.restfulwhois.rdap.core.common.util.NetworkInBytes;
import org.restfulwhois.rdap.core.domain.model.Domain;
import org.restfulwhois.rdap.core.domain.model.Domain.DomainType;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.core.entity.model.Entity;
import org.restfulwhois.rdap.core.ip.model.Network;
import org.restfulwhois.rdap.core.nameserver.model.Nameserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * domain query DAO query domain object from database,
 * include inner objects such as nameserver,variants etc.
 * search domain objects has the same process with query,
 * except search part include reverse domain(in-addr.arpa).
 * </pre>
 * 
 * @author jiashuo
 * 
 */
@Repository
public class DomainQueryDaoImpl extends AbstractQueryDao<Domain> {

    /**
     * left join domain status SQL.
     */
    public static String SQL_LEFT_JOIN_DOMAIN_STATUS =
            " left outer join RDAP_DOMAIN_STATUS status "
                    + " on domain.DOMAIN_ID = status.DOMAIN_ID ";
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DomainQueryDaoImpl.class);
    /**
     * variant dao.
     */
    @Autowired
    @Qualifier("variantsQueryDaoImpl")
    private QueryDao<Variants> variantsQueryDao;
    /**
     * nameserver dao.
     */
    @Autowired
    @Qualifier("nameserverQueryDaoImpl")
    private QueryDao<Nameserver> nameserverQueryDao;
    /**
     * secureDns dao.
     */
    @Autowired
    @Qualifier("secureDnsQueryDaoImpl")
    private QueryDao<SecureDns> secureDnsQueryDao;
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
     * event dao.
     */
    @Autowired
    @Qualifier("networkQueryDaoImpl")
    private QueryDao<Network> networkQueryDao;

    /**
     * entityQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;
    
    @Autowired
    @Qualifier("domainSearchDaoImpl")
    private SearchDao<Domain> searchDao;


    /**
     * query domain (RIR or DNR).
     * 
     * @param queryParam
     *            QueryParam include punyname.
     * @return domain object.
     */
    @Override
    public Domain query(QueryParam queryParam) {
        LOGGER.debug("query, queryParam:" + queryParam);
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;

        // RIR domain, like 1.0.0.in-addr.arpa
        if (domainQueryParam.isRirDomain()) {
            Domain domain = queryArpaWithoutInnerObjects(queryParam);
            queryAndSetInnerObjects(domain);
            LOGGER.debug("query, domain:" + domain);
            return domain;
        } else {
            // LDH domain for DNR
            Domain domain = queryDomainWithoutInnerObjects(queryParam);
            queryAndSetInnerObjects(domain);
            LOGGER.debug("query, domain:" + domain);
            return domain;
        }
    }
    
    @Override
    public void queryAndSetInnerObjectsForSearch(List<Domain> domains) {
        if (null == domains) {
            return;
        }
        for (Domain domain : domains) {
            queryAndSetInnerObjects(domain);
        }
    }

    /**
     * query inner objects of domain,and set them to domain object.
     * 
     * @param domain
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjects(Domain domain) {
        if (null == domain) {
            return;
        }
        Long domainId = domain.getId();
        ModelType type = ModelType.DOMAIN;
        List<Nameserver> nameServers =
                nameserverQueryDao.queryAsInnerObjects(domainId, type);
        domain.setNameservers(nameServers);
        List<SecureDns> secureDnsList =
                secureDnsQueryDao.queryAsInnerObjects(domainId, type);
        if (null != secureDnsList && secureDnsList.size() > 0) {
            domain.setSecureDns(secureDnsList.get(0));
        }
        List<PublicId> publicIds =
                publicIdQueryDao.queryAsInnerObjects(domainId, type);
        domain.setPublicIds(publicIds);
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(domainId, type);
        domain.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(domainId, type);
        links.add(AutoGenerateSelfLink.generateSelfLink(domain));
        domain.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(domainId, type);
        domain.setEvents(events);
        List<Entity> entities =
                entityQueryDao.queryAsInnerObjects(domainId, type);
        domain.setEntities(entities);
        queryAndSetVariants(domain);
        queryAndSetInnerNetwork(domain);
    }

    /**
     * query and set variants for domain object.
     * 
     * @param domain
     *            domain object which will be filled with variants.
     */
    private void queryAndSetVariants(Domain domain) {
        if (null == domain || !domain.isDnrDomain()) {
            return;
        }
        List<Variants> variants =
                variantsQueryDao.queryAsInnerObjects(domain.getId(),
                        ModelType.DOMAIN);
        domain.setVariants(variants);
    }

    /**
     * query networks for arpa ,then fill them to domain.
     * 
     * @param domain
     *            which will be filled with networks.
     */
    private void queryAndSetInnerNetwork(Domain domain) {
        if (null == domain || !domain.isArpaDomain()) {
            return;
        }
        List<Network> networks =
                networkQueryDao.queryAsInnerObjects(domain.getId(),
                        ModelType.DOMAIN);
        if (null != networks && networks.size() > 0) {
            Network network = networks.get(0);
            domain.setNetwork(network);
        }
    }

    /**
     * query domain by arpa, without inner objects.
     * <p>
     * different sql for ipv4 and ipv6.
     * 
     * @param queryParam
     *            query parameter include punyname
     * @return domain the domain object without inner objects
     */
    private Domain queryArpaWithoutInnerObjects(QueryParam queryParam) {

        final String arpaName = queryParam.getQ();
        final NetworkInBytes network = IpUtil.parseArpa(arpaName);
        List<Domain> result = null;
        final int hexCharSize = IpUtil.getHexCharSize(network.getIpVersion());
        String sql =
                "select domain.*,status.* "
                        + " from RDAP_IP ip "
                        + " inner join RDAP_DOMAIN domain "
                        + " on domain.NETWORK_ID = ip.IP_ID "
                        + SQL_LEFT_JOIN_DOMAIN_STATUS
                        + " where ip.STARTADDRESS <= ? and ip.ENDADDRESS >= ?"
                        + " and domain.TYPE = 'arpa' and ip.version = ? "
                        + " && LENGTH(HEX(STARTADDRESS))=? && LENGTH(HEX(ENDADDRESS))=? "
                        + " order by ip.STARTADDRESS desc,ip.ENDADDRESS,domain.DOMAIN_ID limit 1";
        final String finalSql = sql;
        result = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(finalSql);
                ps.setBytes(1, network.getStartAddress());
                ps.setBytes(2, network.getStartAddress());
                ps.setString(3, network.getIpVersion().getName());
                ps.setInt(4, hexCharSize);
                ps.setInt(5, hexCharSize);
                return ps;
            }
        }, new DomainWithStatusResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);

    }

    /**
     * query common domain and its status from database, without inner objects.
     * 
     * @param queryParam
     *            query parameter include punyname.
     * @return domain object without inner objects.
     */
    private Domain queryDomainWithoutInnerObjects(QueryParam queryParam) {
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        final String punyName = domainQueryParam.getPunyName();
        LOGGER.debug("query LDH_NAME with punyName:{}", punyName);
        final String sql =
                "select * from RDAP_DOMAIN domain "
                        + SQL_LEFT_JOIN_DOMAIN_STATUS + " where LDH_NAME= ?  ";
        List<Domain> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, punyName);
                        return ps;
                    }
                }, new DomainWithStatusResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    /**
     * extract domain from ResultSet.
     * 
     * @param rs
     *            ResultSet extract from.
     * @param domain
     *            domain argument to set.
     * @throws SQLException
     *             SQLException.
     */
    private void extractDomainFromRs(ResultSet rs, Domain domain)
            throws SQLException {
        domain.setId(rs.getLong("DOMAIN_ID"));
        domain.setHandle(rs.getString("HANDLE"));
        domain.setLdhName(rs.getString("LDH_NAME"));
        domain.setUnicodeName(rs.getString("UNICODE_NAME"));
        domain.setPort43(rs.getString("PORT43"));
        domain.setLang(rs.getString("LANG"));
        domain.setType(DomainType.getByTypeName(rs.getString("TYPE")));
        domain.setNetworkId(rs.getLong("NETWORK_ID"));
    }

    /**
     * domain ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    public class DomainWithStatusResultSetExtractor implements
            ResultSetExtractor<List<Domain>> {
        @Override
        public List<Domain> extractData(ResultSet rs) throws SQLException {
            List<Domain> result = new ArrayList<Domain>();
            Map<Long, Domain> domainMapById = new HashMap<Long, Domain>();
            while (rs.next()) {
                Long domainId = rs.getLong("DOMAIN_ID");
                Domain domain = domainMapById.get(domainId);
                if (null == domain) {
                    domain = new Domain();
                    extractDomainFromRs(rs, domain);
                    result.add(domain);
                    domainMapById.put(domainId, domain);
                }
                domain.addStatus(rs.getString("STATUS"));
            }
            return result;
        }
    }

    /**
     * search domain using punyname or unicodeName, without inner objects.
     * 
     * @param params
     *            query parameter include domain punyname.
     * @return domain list.
     */
    private List<Domain> searchWithoutInnerObjects(final QueryParam queryParam) {
        return searchDao.search(queryParam);
    }

}
