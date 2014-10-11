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

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.bean.Arpa;
import org.restfulwhois.rdap.bean.BaseModel;
import org.restfulwhois.rdap.bean.Domain;
import org.restfulwhois.rdap.bean.DomainQueryParam;
import org.restfulwhois.rdap.bean.DomainSearchParam;
import org.restfulwhois.rdap.bean.DomainSearchType;
import org.restfulwhois.rdap.bean.Entity;
import org.restfulwhois.rdap.bean.Event;
import org.restfulwhois.rdap.bean.Link;
import org.restfulwhois.rdap.bean.ModelStatus;
import org.restfulwhois.rdap.bean.ModelType;
import org.restfulwhois.rdap.bean.Nameserver;
import org.restfulwhois.rdap.bean.Network;
import org.restfulwhois.rdap.bean.Network.IpVersion;
import org.restfulwhois.rdap.bean.PageBean;
import org.restfulwhois.rdap.bean.PublicId;
import org.restfulwhois.rdap.bean.QueryParam;
import org.restfulwhois.rdap.bean.Remark;
import org.restfulwhois.rdap.bean.SecureDns;
import org.restfulwhois.rdap.bean.Variants;
import org.restfulwhois.rdap.common.util.AutoGenerateSelfLink;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.dao.AbstractQueryDao;
import org.restfulwhois.rdap.dao.QueryDao;
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
            queryAndSetInnerNetwork(domain);
            LOGGER.debug("query, domain:" + domain);
            return domain;
        } else {
            // LDH domain for DNR
            Domain domain = queryDomainWithoutInnerObjects(queryParam);
            queryAndSetInnerObjects(domain);
            queryAndSetVariants(domain);
            LOGGER.debug("query, domain:" + domain);
            return domain;
        }
    }

    /**
     * search DNR domain.
     * 
     * @param queryParam
     *            QueryParam.
     * @return domain list .
     */
    @Override
    public List<Domain> search(QueryParam queryParam) {
        LOGGER.debug("search, queryParam:" + queryParam);
        List<Domain> domains = searchWithoutInnerObjects(queryParam);
        queryAndSetDomainStatus(domains);
        queryAndSetInnerObjectsWithoutNotice(domains);
        LOGGER.debug("search, domains:" + domains);
        return domains;
    }

    /**
     * search domain count.
     * <p>
     * select the counter number of domain from database.
     * 
     * @param queryParam
     *            QueryParam.
     * @return domain count.
     */
    @Override
    public Long searchCount(QueryParam queryParam) {
        LOGGER.debug("searchCount, queryParam:" + queryParam);
        DomainSearchParam domainSearchParam = (DomainSearchParam) queryParam;
        if (DomainSearchType.NAME.value().equals(
             domainSearchParam.getSearchByParam())) {
            //search domain by name
            return searchCountByName(queryParam);
        } else if (DomainSearchType.NSLDHNAME.value().equals(
             domainSearchParam.getSearchByParam())) {
             //search domain by nsLdhName
           return searchCountByNsLdhName(queryParam);
        } else if (DomainSearchType.NSIP.value().equals(
             domainSearchParam.getSearchByParam())) {
            //search domain by nsIp
           return searchCountByNsIp(queryParam);
        } else {
           return 0L;
        }
    }
    
    /**
     * query and set domain status.
     * 
     * @param domains
     *            domain list.
     */
    private void queryAndSetDomainStatus(List<Domain> domains) {
        List<Long> domainIds = getModelIds(domains);
        List<ModelStatus> domainStatusList = queryDomainStatus(domainIds);
        for (ModelStatus status : domainStatusList) {
            BaseModel obj = BaseModel.findObjectFromListById(domains,
                    status.getId());
            if (null == obj) {
                continue;
            }
            Domain domain = (Domain) obj;
            domain.addStatus(status.getStatus());
        }
    }

    /**
     * query domain status from RDAP_DOMAIN_STATUS with domain id.
     * 
     * @param domainIds
     *            domain id list.
     * @return domain status list.
     */
    private List<ModelStatus> queryDomainStatus(List<Long> domainIds) {
        if (null == domainIds || domainIds.size() == 0) {
            return new ArrayList<ModelStatus>();
        }
        final String domainsIdsJoinedByComma = StringUtils.join(domainIds, ",");
        final String sqlTpl = 
                "select * from RDAP_DOMAIN_STATUS where DOMAIN_ID in (%s)";
        final String sql = String.format(sqlTpl, domainsIdsJoinedByComma);
        List<ModelStatus> result = jdbcTemplate.query(sql,
                new RowMapper<ModelStatus>() {
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
     * query and set arpa status.
     * 
     * @param domains
     *            domain list.
     */
    private void queryAndSetArpaStatus(List<Domain> domains) {
        List<Long> domainIds = getModelIds(domains);
        List<ModelStatus> domainStatusList = queryArpaStatus(domainIds);
        for (ModelStatus status : domainStatusList) {
            BaseModel obj = BaseModel.findObjectFromListById(domains,
                    status.getId());
            if (null == obj) {
                continue;
            }
            Domain domain = (Domain) obj;
            domain.addStatus(status.getStatus());
        }
    }

    /**
     * query domain status from RDAP_ARPA_STATUS using domain id.
     * 
     * @param domainIds
     *            domain id list.
     * @return domain status list.
     */
    private List<ModelStatus> queryArpaStatus(List<Long> domainIds) {
        if (null == domainIds || domainIds.size() == 0) {
            return new ArrayList<ModelStatus>();
        }
        final String domainsIdsJoinedByComma = StringUtils.join(domainIds, ",");
        final String sqlTpl = 
                "select * from RDAP_ARPA_STATUS where ARPA_ID in (%s)";
        final String sql = String.format(sqlTpl, domainsIdsJoinedByComma);
        List<ModelStatus> result = jdbcTemplate.query(sql,
                new RowMapper<ModelStatus>() {
                    @Override
                    public ModelStatus mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new ModelStatus(rs.getLong("ARPA_ID"), rs
                                .getString("STATUS"));
                    }

                });
        return result;
    }

    /**
     * query inner objects of domain,and set them to domain object.
     * 
     * @param domains
     *            domain list.
     */
    private void queryAndSetInnerObjectsWithoutNotice(List<Domain> domains) {
        if (null == domains) {
            return;
        }
        for (Domain domain : domains) {
            queryAndSetInnerObjects(domain);
            queryAndSetVariants(domain);
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
        ModelType type = domain.getDomainType();
        List<Nameserver> nameServers = nameserverQueryDao.queryAsInnerObjects(
                domainId, type);
        domain.setNameservers(nameServers);
        List<SecureDns> secureDnsList = secureDnsQueryDao.queryAsInnerObjects(
                domainId, type);
        if (null != secureDnsList && secureDnsList.size() > 0) {
            domain.setSecureDns(secureDnsList.get(0));
        }
        List<PublicId> publicIds = publicIdQueryDao.queryAsInnerObjects(
                domainId, type);
        domain.setPublicIds(publicIds);
        List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(domainId,
                type);
        domain.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(domainId, type);
        links.add(AutoGenerateSelfLink.generateSelfLink(domain));
        domain.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(domainId, type);
        domain.setEvents(events);
        List<Entity> entities = entityQueryDao.queryAsInnerObjects(domainId,
                type);
        domain.setEntities(entities);
    }

    /**
     * query and set variants for domain object.
     * 
     * @param domain
     *            domain object which will be filled with variants.
     */
    private void queryAndSetVariants(Domain domain) {
        if (null == domain) {
            return;
        }
        List<Variants> variants = variantsQueryDao.
                queryAsInnerObjects(domain.getId(), domain.getDomainType());
        domain.setVariants(variants);
    }

    /**
     * query networks for arpa ,then fill them to domain.
     * 
     * @param domain
     *            which will be filled with networks.
     */
    private void queryAndSetInnerNetwork(Domain domain) {
        if (null == domain) {
            return;
        }

        ModelType type = domain.getDomainType();
        if (ModelType.ARPA == type
                && StringUtils.isNotEmpty(domain.getLdhName())) {
            List<Network> networks = networkQueryDao.queryAsInnerObjects(
                    domain.getId(), ModelType.ARPA);
            if (null != networks && networks.size() > 0) {
                Network network = networks.get(0);
                domain.setNetwork(network);
            }
        }
    }

    /**
     * query domain by arpa, without inner objects.
     * <p>
     * different sql for ipv4 and ipv6.
     * 
     * @param queryParam
     *            query parameter include punyname
     * @return domain
     *            the domain object without inner objects
     */
    private Domain queryArpaWithoutInnerObjects(QueryParam queryParam) {

        final String arpaName = queryParam.getQ();
        final Arpa arpa = Arpa.decodeArpa(arpaName);
        if (null == arpa) {
            return null;
        }
        List<Domain> result = null;

        if (IpVersion.V4 == arpa.getIpVersion()) {

            // Arpa for IPv4, ignore high address
            final String sql = 
                    "select *, (ENDLOWADDRESS - STARTLOWADDRESS) as low "
                    + "from RDAP_ARPA"
                    + " where STARTLOWADDRESS <= ? and ENDLOWADDRESS >= ? "
                    + " and (STARTHIGHADDRESS is null "
                    + " or STARTHIGHADDRESS = '0')"
                    + " and (ENDHIGHADDRESS is null or ENDHIGHADDRESS = '0')"
                    + " and version = 'v4' " + " order by low  limit 1  ";
            result = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(
                        Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(
                            1,
                            arpa.getStartLowAddress().toString(
                                    Arpa.RADIX_DECIMAL));
                    ps.setString(2,
                            arpa.getEndLowAddress()
                                    .toString(Arpa.RADIX_DECIMAL));
                    return ps;
                }
            }, new ArpaResultSetExtractor());

        } else if (IpVersion.V6 == arpa.getIpVersion()) {

            // Arpa for IPv6, include high address and low address
            final String sql = 
                    "select *, (ENDLOWADDRESS - STARTLOWADDRESS) as low, "
                    + "(ENDHIGHADDRESS - STARTHIGHADDRESS) as high "
                    + "from RDAP_ARPA "
                    + "where (STARTHIGHADDRESS < ? "
                    + "     or (STARTHIGHADDRESS=? and STARTLOWADDRESS<=?)) "
                    + " and (ENDHIGHADDRESS > ? "
                    + "     or (ENDHIGHADDRESS = ? and ENDLOWADDRESS >= ?)) "
                    + " and version = 'v6' " + " order by high ,low  limit 1  ";
            result = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(
                        Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(
                            1,
                            arpa.getStartHighAddress().toString(
                                    Arpa.RADIX_DECIMAL));
                    ps.setString(
                            2,
                            arpa.getStartHighAddress().toString(
                                    Arpa.RADIX_DECIMAL));
                    ps.setString(
                            3,
                            arpa.getStartLowAddress().toString(
                                    Arpa.RADIX_DECIMAL));

                    ps.setString(
                            4,
                            arpa.getEndHighAddress().toString(
                                    Arpa.RADIX_DECIMAL));
                    ps.setString(
                            5,
                            arpa.getEndHighAddress().toString(
                                    Arpa.RADIX_DECIMAL));
                    ps.setString(6,
                            arpa.getEndLowAddress()
                                    .toString(Arpa.RADIX_DECIMAL));

                    return ps;
                }
            }, new ArpaResultSetExtractor());
        }
        if (null == result || result.size() == 0) {
            return null;
        }

        queryAndSetArpaStatus(result);
        return result.get(0);

    }

    /**
     * query common domain and its status from database, without inner objects.
     * 
     * @param queryParam
     *            query parameter include punyname.
     * @return domain
     *            object without inner objects.
     */
    private Domain queryDomainWithoutInnerObjects(QueryParam queryParam) {
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        final String punyName = domainQueryParam.getPunyName();
        LOGGER.debug("query LDH_NAME with punyName:{}", punyName);
        final String sql = "select * from RDAP_DOMAIN domain "
                + " left outer join RDAP_DOMAIN_STATUS status "
                + " on domain.DOMAIN_ID = status.DOMAIN_ID "
                + " where LDH_NAME= ?  ";
        List<Domain> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
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
     * domain ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class DomainResultSetExtractor implements ResultSetExtractor<List<Domain>> {
        @Override
        public List<Domain> extractData(ResultSet rs) throws SQLException {
            List<Domain> result = new ArrayList<Domain>();
            while (rs.next()) {
                Domain domain = new Domain();
                extractDomainFromRs(rs, domain);
                result.add(domain);
            }
            return result;
        }
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
    }

    /**
     * extract arpa from ResultSet.
     * 
     * @param rs
     *            ResultSet which extract arpa from.
     * @param domain
     *            domain argument to set.
     * @throws SQLException
     *             SQLException.
     */
    private void extractArpaFromRs(ResultSet rs, Domain domain)
            throws SQLException {
        domain.setId(rs.getLong("ARPA_ID"));
        domain.setHandle(rs.getString("HANDLE"));
        domain.setLdhName(rs.getString("ARPA_NAME"));
        domain.setPort43(rs.getString("PORT43"));
        domain.setLang(rs.getString("LANG"));
    }

    /**
     * domain ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class DomainWithStatusResultSetExtractor implements
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
     * arpa ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class ArpaResultSetExtractor implements ResultSetExtractor<List<Domain>> {
        @Override
        public List<Domain> extractData(ResultSet rs) throws SQLException {
            List<Domain> result = new ArrayList<Domain>();
            while (rs.next()) {
                Domain domain = new Domain();
                extractArpaFromRs(rs, domain);
                result.add(domain);
            }
            return result;
        }
    }

    /**
     * load arpa domain by id.
     * 
     * @param domainId
     *            domainId argument.
     * @return Domain if exist in database, return null if not.
     */
    public Domain loadArpaDomain(final Long domainId) {
        if (null == domainId) {
            return null;
        }
        List<Domain> result = null;
        final String sql = "select * from RDAP_ARPA where ARPA_ID= ? ";
        result = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, domainId);
                return ps;
            }
        }, new ArpaResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    /**
     * count the number of resutlset.
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
     * search domain using punyname or unicodeName, without inner objects.
     * 
     * @param params
     *            query parameter include domain punyname.
     * @return domain list.
     */
    private List<Domain> searchWithoutInnerObjects(final QueryParam params) {
        DomainSearchParam domainSearchParam = (DomainSearchParam) params;
        if (DomainSearchType.NAME.value().equals(
                domainSearchParam.getSearchByParam())) {
            //search domain by name
            return searchWithoutInnerObjectsByName(params);
        } else if (DomainSearchType.NSLDHNAME.value().equals(
                domainSearchParam.getSearchByParam())) {
            //search domain by nsLdhName
            return searchWithoutInnerObjectsByNsLdhName(params);
        } else if (DomainSearchType.NSIP.value().equals(
                domainSearchParam.getSearchByParam())) {
            //search domain by nsIp
            return searchWithoutInnerObjectsByNsIp(params);
        } else {
            return null;
        }
       
    }
    
    
    /**
     * search domain count by name
     * <p>
     * select the counter number of domain from database.
     * 
     * @param queryParam
     *            QueryParam.
     * @return domain count.
     */
    public Long searchCountByName(QueryParam queryParam) {
       LOGGER.debug("searchCount, queryParam:" + queryParam);
       DomainSearchParam domainQueryParam = (DomainSearchParam) queryParam;
       final String domainName = domainQueryParam.getQ();
       final String punyName = domainQueryParam.getPunyName();
       final String domainNameLikeClause = super.generateLikeClause(domainName);
       final String punyNameLikeClause = super.generateLikeClause(punyName);
       final String sql = "select count(1) as COUNT from RDAP_DOMAIN domain "
                    + " where LDH_NAME like ? or UNICODE_NAME like ? ";
       Long domainCount = jdbcTemplate.query(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(
                        Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, punyNameLikeClause);
                    ps.setString(2, domainNameLikeClause);
                    return ps;
                }
            }, new CountResultSetExtractor());
            LOGGER.debug("searchCount, domainCount:" + domainCount);
            return domainCount;
    }
    
    /**
     * search domain count by nsLdhName 
     * <p>
     * select the counter number of domain from database.
     * 
     * @param queryParam
     *            QueryParam.
     * @return domain count.
     */
    private Long searchCountByNsLdhName(QueryParam queryParam) {
       LOGGER.debug("searchCount, queryParam:" + queryParam);
       DomainSearchParam domainQueryParam = (DomainSearchParam) queryParam;
       final String domainName = domainQueryParam.getQ();
       final String punyName = domainQueryParam.getPunyName();
       final String domainNameLikeClause = super.generateLikeClause(domainName);
       final String punyNameLikeClause = super.generateLikeClause(punyName);
       final String sql = "select count(0) as COUNT from  (SELECT distinct t1.* from " 
        + "RDAP_DOMAIN t1 inner join REL_DOMAIN_NAMESERVER t2 " 
        + "on t1.DOMAIN_ID = t2.DOMAIN_ID inner join RDAP_NAMESERVER "
        + "t3 on t2.NAMESERVER_ID = t3.NAMESERVER_ID "
        + "where t2.DOMAIN_TYPE='domain' and t3.LDH_NAME like ? " 
        + "union all "
        + "select distinct t1.ARPA_ID as DOMAIN_ID, t1.HANDLE, t1.ARPA_NAME as LDH_NAME,"
        + " '' AS UNICODE_NAME, t1.PORT43, " 
        + "t1.LANG from RDAP_ARPA t1 inner join REL_DOMAIN_NAMESERVER t2 on "
        + "t1.ARPA_ID = t2.DOMAIN_ID inner join RDAP_NAMESERVER t3 "
        + "on t2.NAMESERVER_ID = t3.NAMESERVER_ID where t2.DOMAIN_TYPE='arpa' "
        + "and t3.LDH_NAME like ? ) T";
        Long domainCount = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, punyNameLikeClause);
                ps.setString(2, punyNameLikeClause);
                return ps;
            }
        }, new CountResultSetExtractor());
        LOGGER.debug("searchCount, domainCount:" + domainCount);
        return domainCount;
    }
    
    
    /**
     * search domain count by nsIp
     * <p>
     * select the counter number of domain from database.
     * 
     * @param queryParam
     *            QueryParam.
     * @return domain count.
     */
    private Long searchCountByNsIp(QueryParam queryParam) {
        LOGGER.debug("searchCount, queryParam:" + queryParam);
        BigDecimal[] arrayIp = getBigDecimalIp(queryParam);
        if (arrayIp == null) {
            return 0L;
        }
        BigDecimal ipLowTmp = new BigDecimal(0L);
        if (arrayIp.length > 1) {
           ipLowTmp = arrayIp[1];
        }
        final BigDecimal ipHigh = arrayIp[0];
        final BigDecimal ipLow = ipLowTmp;
        final String strHead = "select count(0) as COUNT from (select "
         + "distinct t1.* from  RDAP_DOMAIN t1 "
         + "inner join REL_DOMAIN_NAMESERVER t2 on t1.DOMAIN_ID = t2.DOMAIN_ID "
         + "inner join RDAP_NAMESERVER_IP t3 on "
         + "t2.NAMESERVER_ID = t3.NAMESERVER_ID where t3.IP_LOW = ? && ";
        String arpaSql = "UNION ALL SELECT distinct t1.ARPA_ID AS DOMAIN_ID,  "
         + "t1.HANDLE,t1.ARPA_NAME AS LDH_NAME, '' AS UNICODE_NAME, t1.PORT43, "
         + "t1.LANG FROM RDAP_ARPA t1 INNER JOIN "
         + "REL_DOMAIN_NAMESERVER t2 ON t1.ARPA_ID = t2.DOMAIN_ID "
         + "INNER JOIN RDAP_NAMESERVER_IP t3 "
         + "ON t2.NAMESERVER_ID = t3.NAMESERVER_ID where t3.IP_LOW = ? && ";
        String tmpSql = "t3.IP_HIGH = ?";
        if (ipHigh.doubleValue() == 0.0) {
            tmpSql = "(t3.IP_HIGH = ? or t3.IP_HIGH is null)";
        }
        final String sql = strHead + tmpSql + arpaSql + tmpSql + ") T";
        Long recordsCount = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setBigDecimal(1, ipLow);
                ps.setBigDecimal(2, ipHigh);
                ps.setBigDecimal(3, ipLow);
                ps.setBigDecimal(4, ipHigh);               
                return ps;
            }
        }, new CountResultSetExtractor());
        return recordsCount;
    }
    
    /**
     * search domain using name.
     * 
     * @param params
     *            query parameter include domain punyname.
     * @return domain list.
     */
    private List<Domain> searchWithoutInnerObjectsByName(
          final QueryParam params) {
       DomainSearchParam domainQueryParam = (DomainSearchParam) params;
       final String domainName = domainQueryParam.getQ();
       final String punyName = domainQueryParam.getPunyName();
       final String domainNameLikeClause = super.generateLikeClause(domainName);
       final String punyNameLikeClause = super.generateLikeClause(punyName);
       final String sql = "select * from RDAP_DOMAIN domain "
          + " where LDH_NAME like ? or UNICODE_NAME like ? "
          + " order by domain.LDH_NAME limit ?,? ";
       final PageBean page = params.getPageBean();
       int startPage = page.getCurrentPage() - 1;
       startPage = startPage >= 0 ? startPage : 0;
       final long startRow = startPage * page.getMaxRecords();
       List<Domain> result = jdbcTemplate.query(
           new PreparedStatementCreator() {
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
           }, new DomainResultSetExtractor());
       return result;
    }
    
    
    /**
     * search domain using nsLdhName.
     * 
     * @param params
     *            query parameter include domain punyname.
     * @return domain list.
     */
    private List<Domain> searchWithoutInnerObjectsByNsLdhName(
            final QueryParam params) {
      DomainSearchParam domainQueryParam = (DomainSearchParam) params;
      final String nsName = domainQueryParam.getQ();
      final String punyName = domainQueryParam.getPunyName();
      final String nsNameLikeClause = super.generateLikeClause(nsName);
      final String punyNameLikeClause = super.generateLikeClause(punyName);
      final String sql = "select distinct t1.* from  RDAP_DOMAIN t1 inner join "
            + "REL_DOMAIN_NAMESERVER t2 on t1.DOMAIN_ID = t2.DOMAIN_ID "
            + "inner join RDAP_NAMESERVER t3 on  "
            + "t2.NAMESERVER_ID = t3.NAMESERVER_ID "
            + "where t2.DOMAIN_TYPE='domain' and t3.LDH_NAME LIKE ? "
            + "union all "
            + "select distinct t1.ARPA_ID as DOMAIN_ID, t1.HANDLE, "
            + "t1.ARPA_NAME as LDH_NAME, '' AS UNICODE_NAME, t1.PORT43, "
            + "t1.LANG from  RDAP_ARPA t1 inner join REL_DOMAIN_NAMESERVER t2 "
            + "on t1.ARPA_ID = t2.DOMAIN_ID inner join RDAP_NAMESERVER t3 "
            + "on t2.NAMESERVER_ID = t3.NAMESERVER_ID where "
            + "t2.DOMAIN_TYPE='arpa' and t3.LDH_NAME "
            + "LIKE ? order by LDH_NAME limit ?,? ";
        final PageBean page = params.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        List<Domain> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, punyNameLikeClause);
                        ps.setString(2, punyNameLikeClause);
                        ps.setLong(3, startRow);
                        ps.setLong(4, page.getMaxRecords());
                        return ps;
                    }
                }, new DomainResultSetExtractor());
        return result;
    }
    
    
    /**
     * search domain using nsIp.
     * 
     * @param params
     *            query parameter include domain punyname.
     * @return domain list.
     */
    private List<Domain> searchWithoutInnerObjectsByNsIp(
         final QueryParam params) {
        DomainSearchParam domainQueryParam = (DomainSearchParam) params;
        List<Domain> result = null;
        final PageBean page = domainQueryParam.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        BigDecimal[] arrayIp = getBigDecimalIp(domainQueryParam);
        BigDecimal ipLowTmp = new BigDecimal(0L);
        if (arrayIp.length > 1) {
          ipLowTmp = arrayIp[1];
        }
        final BigDecimal ipHigh = arrayIp[0];
        final BigDecimal ipLow = ipLowTmp;
        final String strHead = "SELECT distinct t1.* FROM  RDAP_DOMAIN t1 "
               + "INNER JOIN REL_DOMAIN_NAMESERVER t2 "
               + "ON t1.DOMAIN_ID = t2.DOMAIN_ID INNER JOIN "
               + "RDAP_NAMESERVER_IP t3 ON t2.NAMESERVER_ID = t3.NAMESERVER_ID "
               + "where ";
        String arpaSql = "UNION ALL SELECT distinct t1.ARPA_ID AS DOMAIN_ID, "
                + "t1.HANDLE, t1.ARPA_NAME AS LDH_NAME, '' AS UNICODE_NAME, "
                + "t1.PORT43, t1.LANG FROM RDAP_ARPA t1 INNER JOIN "
                + "REL_DOMAIN_NAMESERVER t2 ON t1.ARPA_ID = t2.DOMAIN_ID "
                + "INNER JOIN RDAP_NAMESERVER_IP t3 "
                + "ON t2.NAMESERVER_ID = t3.NAMESERVER_ID where ";
        String tmpSql = "t3.IP_HIGH = ? ";
        if (ipHigh.doubleValue() == 0.0) {
            tmpSql = "(t3.IP_HIGH = ? or t3.IP_HIGH is NULL) ";
        }
        final String strEnd = "and t3.IP_LOW = ? ";
        final String strLast = "order by LDH_NAME limit ?,?";
        final String sql = strHead + tmpSql + strEnd + arpaSql + tmpSql
                + strEnd + strLast;
        result = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setBigDecimal(1, ipHigh);
                ps.setBigDecimal(2, ipLow);
                ps.setBigDecimal(3, ipHigh);
                ps.setBigDecimal(4, ipLow);
                ps.setLong(5, startRow);
                ps.setLong(6, page.getMaxRecords());
                return ps;
            }
        }, new DomainResultSetExtractor());
        return result;
    }
    
    /**
     * get bigDecimal of ip from nameserver query parameter.
     * @param queryParam
     *            query parameter of nameserver
     * @return BigDecimal[] for ip
     * 
     * @author weijunkai
     */
    private BigDecimal[] getBigDecimalIp(QueryParam queryParam) {
        DomainSearchParam domainQueryParam = (DomainSearchParam) queryParam;
        final String strIp = domainQueryParam.getQ();
        if (!IpUtil.isIpV4StrWholeValid(strIp)
                && !IpUtil.isIpV6StrValid(strIp)) {
            return null;
        }
        BigDecimal[] arrayIp = IpUtil.ipToBigDecimal(strIp);
        return arrayIp;
    }
    
    
}
