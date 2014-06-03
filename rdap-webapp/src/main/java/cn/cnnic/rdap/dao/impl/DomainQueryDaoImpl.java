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
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.DomainQueryParam;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelStatus;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.Notice;
import cn.cnnic.rdap.bean.PageBean;
import cn.cnnic.rdap.bean.PublicId;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.bean.SecureDns;
import cn.cnnic.rdap.bean.Variants;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.NoticeDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * domain query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class DomainQueryDaoImpl extends AbstractQueryDao<Domain> {
    /**
     * notice dao.
     */
    @Autowired
    @Qualifier("noticeDaoImpl")
    private NoticeDao noticeDao;

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
     * network dao.
     */
    @Autowired
    private QueryDao<Network> networkQueryDao;

    @Override
    public Domain query(QueryParam queryParam) {
        Domain domain = queryWithoutInnerObjects(queryParam);
        queryAndSetInnerObjectsWithoutNotice(domain);
        queryAndSetInnerNotice(domain);
        return domain;
    }

    @Override
    public List<Domain> search(QueryParam queryParam) {
        List<Domain> domains = searchWithoutInnerObjects(queryParam);
        queryAndSetDomainStatus(domains);
        queryAndSetInnerObjectsWithoutNotice(domains);
        return domains;
    }

    @Override
    public Long searchCount(QueryParam queryParam) {
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        final String domainName = domainQueryParam.getQ();
        final String punyName = domainQueryParam.getPunyName();
        final String domainNameLikeClause = super
                .generateLikeClause(domainName);
        final String punyNameLikeClause = super.generateLikeClause(punyName);
        final String sql = "select count(1) as COUNT from RDAP_DOMAIN domain "
                + " where LDH_NAME like ? or UNICODE_NAME like ? ";
        Long domainCount = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, punyNameLikeClause);
                ps.setString(2, domainNameLikeClause);
                return ps;
            }
        }, new CountResultSetExtractor());
        return domainCount;
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
     * query domain status.
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
        final String sqlTpl = "select * from RDAP_DOMAIN_STATUS where DOMAIN_ID in (%s)";
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
     * query inner objects of domain,and set fill them to domain.
     * 
     * @param domains
     *            domain list.
     */
    private void queryAndSetInnerObjectsWithoutNotice(List<Domain> domains) {
        if (null == domains) {
            return;
        }
        for (Domain domain : domains) {
            queryAndSetInnerObjectsWithoutNotice(domain);
        }
    }

    /**
     * query inner objects of domain,and set fill them to domain.
     * 
     * @param domain
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjectsWithoutNotice(Domain domain) {
        if (null == domain) {
            return;
        }
        Long domainId = domain.getId();
        List<Variants> varients = variantsQueryDao.queryAsInnerObjects(
                domainId, ModelType.DOMAIN);
        domain.setVarients(varients);
        List<Nameserver> nameServers = nameserverQueryDao.queryAsInnerObjects(
                domainId, ModelType.DOMAIN);
        domain.setNameServers(nameServers);
        List<SecureDns> secureDnsList = secureDnsQueryDao.queryAsInnerObjects(
                domainId, ModelType.DOMAIN);
        if (null != secureDnsList && secureDnsList.size() > 0) {
            domain.setSecureDns(secureDnsList.get(0));
        }
        List<PublicId> publicIds = publicIdQueryDao.queryAsInnerObjects(
                domainId, ModelType.DOMAIN);
        domain.setPublicIds(publicIds);
        List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(domainId,
                ModelType.DOMAIN);
        domain.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(domainId,
                ModelType.DOMAIN);
        domain.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(domainId,
                ModelType.DOMAIN);
        domain.setEvents(events);
        List<Network> networks = networkQueryDao.queryAsInnerObjects(domainId,
                ModelType.DOMAIN);
        domain.setNetwork(networks);
    }

    /**
     * query notice,and set to domain.
     * 
     * @param domain
     *            domain.
     */
    private void queryAndSetInnerNotice(Domain domain) {
        if (null == domain) {
            return;
        }
        List<Notice> notices = noticeDao.getAllNotices();
        domain.setNotices(notices);
    }

    /**
     * query domain, without inner objects.
     * 
     * @param queryParam
     *            query parameter
     * @return autnum
     */
    private Domain queryWithoutInnerObjects(QueryParam queryParam) {
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        final String punyName = domainQueryParam.getPunyName();
        final String sql = "select * from RDAP_DOMAIN domain "
                + " left outer join RDAP_DOMAIN_STATUS status "
                + " on domain.DOMAIN_ID = status.DOMAIN_ID "
                + " where LDH_NAME= ?  ";
        List<Domain> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
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
        public List<Domain> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Domain> result = new ArrayList<Domain>();
            while (rs.next()) {
                Domain domain = new Domain();
                ExtractorDomainFromRs(rs, domain);
                result.add(domain);
            }
            return result;
        }
    }

    /**
     * extract domain from ResultSet.
     * 
     * @param rs
     *            ResultSet.
     * @param domain
     *            domain.
     * @throws SQLException
     *             SQLException.
     */
    private void ExtractorDomainFromRs(ResultSet rs, Domain domain)
            throws SQLException {
        domain.setId(rs.getLong("DOMAIN_ID"));
        domain.setHandle(rs.getString("HANDLE"));
        domain.setLdhName(rs.getString("LDH_NAME"));
        domain.setUnicodeName(rs.getString("UNICODE_NAME"));
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
        public List<Domain> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Domain> result = new ArrayList<Domain>();
            Map<Long, Domain> domainMapById = new HashMap<Long, Domain>();
            while (rs.next()) {
                Long domainId = rs.getLong("DOMAIN_ID");
                Domain domain = domainMapById.get(domainId);
                if (null == domain) {
                    domain = new Domain();
                    ExtractorDomainFromRs(rs, domain);
                    result.add(domain);
                    domainMapById.put(domainId, domain);
                }
                domain.addStatus(rs.getString("STATUS"));
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
        public Long extractData(ResultSet rs) throws SQLException,
                DataAccessException {
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
     * @param queryParam
     *            query parameter.
     * @return domain list.
     */
    private List<Domain> searchWithoutInnerObjects(final QueryParam queryParam) {
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        final String domainName = domainQueryParam.getQ();
        final String punyName = domainQueryParam.getPunyName();
        final String domainNameLikeClause = super
                .generateLikeClause(domainName);
        final String punyNameLikeClause = super.generateLikeClause(punyName);
        final String sql = "select * from RDAP_DOMAIN domain "
                + " where LDH_NAME like ? or UNICODE_NAME like ? order by domain.LDH_NAME limit ?,? ";
        final PageBean page = queryParam.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        List<Domain> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
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
}
