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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.IPAddress;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelStatus;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.NameserverQueryParam;
import cn.cnnic.rdap.bean.Notice;
import cn.cnnic.rdap.bean.PageBean;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.common.util.IpUtil;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.NoticeDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * nameserver query DAO.
 * 
 * @author weijunkai
 * 
 */
@Repository
public class NameserverQueryDaoImpl extends AbstractQueryDao<Nameserver> {
    /**
     * notice dao.
     */
    @Autowired
    @Qualifier("noticeDaoImpl")
    private NoticeDao noticeDao;
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

    /**
     * query inner objects of nameserver,and set object value to them.
     * 
     * @param outerObjectId
     *            id from domain.
     * 
     * @param outerModelType
     *            type of the object
     * 
     * @return result for List<Nameserver>
     */
    private List<Nameserver> queryNameserverWithDomainID(
            final Long outerObjectId, final ModelType outerModelType) {
        final String sql = "select * from RDAP_NAMESERVER ns inner join "
                + "REL_DOMAIN_NAMESERVER rel on (ns.NAMESERVER_ID = "
                + "rel.NAMESERVER_ID and rel.DOMAIN_ID = ? and rel.DOMAIN_TYPE =? ) left outer "
                + "join RDAP_NAMESERVER_STATUS status on ns.NAMESERVER_ID "
                + "=status.NAMESERVER_ID";

        List<Nameserver> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, outerModelType.getName());
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
        List<Nameserver> listNameserver = queryNameserverWithDomainID(
                outerObjectId, outerModelType);
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
        queryAndSetInnerObjectsWithoutNotice(nameserver);
        queryAndSetInnerNotice(nameserver);
        return nameserver;
    }

    /**
     * query notice,and set to nameserver.
     * 
     * @param nameserver
     *            nameserver object.
     */
    private void queryAndSetInnerNotice(Nameserver nameserver) {
        if (null == nameserver) {
            return;
        }
        List<Notice> notices = noticeDao.getAllNotices();
        nameserver.setNotices(notices);
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
        List<IPAddress> listIPAddress = ipAddressQueryDao.queryAsInnerObjects(
                nsID, ModelType.NAMESERVER);
        if (listIPAddress.size() > 0) {
            IPAddress objIPAddress = listIPAddress.get(0);
            ns.setIpAddresses(objIPAddress);
        }
        List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(nsID,
                ModelType.NAMESERVER);
        ns.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(nsID,
                ModelType.NAMESERVER);
        ns.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(nsID,
                ModelType.NAMESERVER);
        ns.setEvents(events);
        List<Entity> entities =
                entityQueryDao.queryAsInnerObjects(nsID, ModelType.NAMESERVER);
        ns.setEntities(entities);
    }

    /**
     * query nameserver, without inner objects.
     * 
     * @param queryParam
     *            query parameter
     * @return nameserver
     */
    private Nameserver queryWithoutInnerObjects(QueryParam queryParam) {
        NameserverQueryParam nsQueryParam = (NameserverQueryParam) queryParam;
        final String punyName = nsQueryParam.getPunyName();
        final String sql = "select * from RDAP_NAMESERVER ns "
                + " left outer join RDAP_NAMESERVER_STATUS status "
                + " on ns.NAMESERVER_ID = status.NAMESERVER_ID "
                + " where LDH_NAME= ?";
        List<Nameserver> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
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
        public List<Nameserver> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
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
    public List<Nameserver> search(QueryParam queryParam) {
        List<Nameserver> listNS = searchWithoutInnerObjects(queryParam);
        queryAndSetNameserverStatus(listNS);
        queryAndSetInnerObjectsWithoutNotice(listNS);
        return listNS;
    }

    /**
     * @param queryParam
     *            for QueryParam
     * @return BigDecimal[]
     * 
     * @author weijunkai
     */
    public BigDecimal[] getBigDecimalIp(QueryParam queryParam) {
        NameserverQueryParam nsQueryParam = (NameserverQueryParam) queryParam;
        final String strIp = nsQueryParam.getQ();
        if (!IpUtil.isIpV4StrWholeValid(strIp) && !IpUtil.isIpV6StrValid(strIp)) {
            return null;
        }
        BigDecimal[] arrayIp = IpUtil.ipToBigDecimal(strIp);
        return arrayIp;
    }

    @Override
    public Long searchCount(QueryParam queryParam) {
        NameserverQueryParam nsQueryParam = (NameserverQueryParam) queryParam;
        if (nsQueryParam == null) {
            return 0L;
        }
        boolean isSearchByIp = nsQueryParam.getIsSearchByIp();
        Long recordsCount = 0L;
        if (isSearchByIp) {
            BigDecimal[] arrayIp = getBigDecimalIp(nsQueryParam);
            if (arrayIp == null) {
                return 0L;
            }
            BigDecimal ipTmp = new BigDecimal(0L);
            if (arrayIp.length > 1) {
                ipTmp = arrayIp[1];
            }
            final BigDecimal ipHigh = arrayIp[0];
            final BigDecimal ipLow = ipTmp;
            final String strHead = "select count(1) as COUNT from RDAP_NAMESERVER_IP "
                    + " where IP_LOW = ? && ";
            String tmpSql = "IP_HIGH = ?";
            if (ipHigh.doubleValue() == 0.0) {
                tmpSql = "(IP_HIGH = ? or IP_HIGH is NULL)";
            }
            final String sql = strHead + tmpSql;
            recordsCount = jdbcTemplate.query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(
                        Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setBigDecimal(2, ipHigh);
                    ps.setBigDecimal(1, ipLow);
                    return ps;
                }
            }, new CountResultSetExtractor());
        } else {
            final String nameserver = nsQueryParam.getQ();
            final String punyName = nsQueryParam.getPunyName();
            final String nameserverLikeClause = super
                    .generateLikeClause(nameserver);
            final String punyNameLikeClause = super
                    .generateLikeClause(punyName);
            final String sql = "select count(1) as COUNT from RDAP_NAMESERVER "
                    + " where LDH_NAME like ? or UNICODE_NAME like ? ";
            recordsCount = jdbcTemplate.query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(
                        Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, punyNameLikeClause);
                    ps.setString(2, nameserverLikeClause);
                    return ps;
                }
            }, new CountResultSetExtractor());
        }
        return recordsCount;
    }

    /**
     * search nameserver, without inner objects.
     * 
     * @param queryParam
     *            query parameter.
     * @return nameserver list.
     */
    private List<Nameserver> searchWithoutInnerObjects(
            final QueryParam queryParam) {
        NameserverQueryParam nsQueryParam = (NameserverQueryParam) queryParam;
        boolean isSearchByIp = nsQueryParam.getIsSearchByIp();
        List<Nameserver> result = null;
        final PageBean page = queryParam.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        if (isSearchByIp) {
            BigDecimal[] arrayIp = getBigDecimalIp(nsQueryParam);
            BigDecimal ipTmp = new BigDecimal(0L);
            if (arrayIp.length > 1) {
                ipTmp = arrayIp[1];
            }
            final BigDecimal ipHigh = arrayIp[0];
            final BigDecimal ipLow = ipTmp;
            final String strHead = "select * from RDAP_NAMESERVER ns,RDAP_NAMESERVER_IP ip"
                    + " where ns.NAMESERVER_ID=ip.NAMESERVER_ID and ";
            String tmpSql = "IP_HIGH = ?";
            if (ipHigh.doubleValue() == 0.0) {
                tmpSql = "(IP_HIGH = ? or IP_HIGH is NULL) ";
            }
            final String strEnd = "and IP_LOW = ? order by ns.LDH_NAME limit ?,? ";
            final String sql = strHead + tmpSql + strEnd;
            result = jdbcTemplate.query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(
                        Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setBigDecimal(1, ipHigh);
                    ps.setBigDecimal(2, ipLow);
                    ps.setLong(3, startRow);
                    ps.setLong(4, page.getMaxRecords());
                    return ps;
                }
            }, new NameserverResultSetExtractor());
        } else {
            final String nsName = nsQueryParam.getQ();
            final String punyName = nsQueryParam.getPunyName();
            final String nsNameLikeClause = super.generateLikeClause(nsName);
            final String punyNameLikeClause = super
                    .generateLikeClause(punyName);
            final String sql = "select * from RDAP_NAMESERVER ns "
                    + " where LDH_NAME like ? or UNICODE_NAME like ?"
                    + " order by ns.LDH_NAME limit ?,? ";
            result = jdbcTemplate.query(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(
                        Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(sql);
                    ps.setString(1, punyNameLikeClause);
                    ps.setString(2, nsNameLikeClause);
                    ps.setLong(3, startRow);
                    ps.setLong(4, page.getMaxRecords());
                    return ps;
                }
            }, new NameserverResultSetExtractor());
        }
        return result;
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
        public List<Nameserver> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
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
     *            ResultSet.
     * @param nameserver
     *            nameserver object.
     * @throws SQLException
     *             SQLException.
     */
    private void extractNameserverFromRs(ResultSet rs, Nameserver nameserver)
            throws SQLException {
        nameserver.setId(rs.getLong("NAMESERVER_ID"));
        nameserver.setHandle(rs.getString("HANDLE"));
        nameserver.setLdhName(rs.getString("LDH_NAME"));
        nameserver.setUnicodeName(rs.getString("UNICODE_NAME"));
        nameserver.setPort43(rs.getString("PORT43"));
        nameserver.setLang(rs.getString("LANG"));
    }

    /**
     * 
     * @author weijunkai
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
     * query inner objects of nameserver,and set fill them to nameserver.
     * 
     * @param nameservers
     *            nameservers list.
     */
    private void queryAndSetInnerObjectsWithoutNotice(
            List<Nameserver> nameservers) {
        if (null == nameservers) {
            return;
        }
        for (Nameserver nameserver : nameservers) {
            queryAndSetInnerObjectsWithoutNotice(nameserver);
        }
    }

    /**
     * query inner objects of nameserver,and set fill them to nameserver.
     * 
     * @param nameserver
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjectsWithoutNotice(Nameserver nameserver) {
        if (null == nameserver) {
            return;
        }
        Long nsId = nameserver.getId();

        List<IPAddress> listIPAddress = ipAddressQueryDao.queryAsInnerObjects(
                nsId, ModelType.NAMESERVER);
        if (listIPAddress.size() > 0) {
            IPAddress objIPAddress = listIPAddress.get(0);
            nameserver.setIpAddresses(objIPAddress);
        }
        List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(nsId,
                ModelType.NAMESERVER);
        nameserver.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(nsId,
                ModelType.NAMESERVER);
        nameserver.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(nsId,
                ModelType.NAMESERVER);
        nameserver.setEvents(events);
    }

    /**
     * query and set nameserver status.
     * 
     * @param listNameserver
     *            nameserver list.
     */
    private void queryAndSetNameserverStatus(List<Nameserver> listNameserver) {
        List<Long> nameserverIds = getModelIds(listNameserver);
        List<ModelStatus> nameserverStatusList = queryNameserverStatus(nameserverIds);
        for (ModelStatus status : nameserverStatusList) {
            BaseModel obj = BaseModel.findObjectFromListById(listNameserver,
                    status.getId());
            if (null == obj) {
                continue;
            }
            Nameserver nameserver = (Nameserver) obj;
            nameserver.addStatus(status.getStatus());
        }
    }

    /**
     * query nameserver status.
     * 
     * @param nameserverIds
     *            nameserver id list.
     * @return nameserver status list.
     */
    private List<ModelStatus> queryNameserverStatus(List<Long> nameserverIds) {
        if (null == nameserverIds || nameserverIds.size() == 0) {
            return new ArrayList<ModelStatus>();
        }
        final String nameserverIdsJoinedByComma = StringUtils.join(
                nameserverIds, ",");
        final String sqlTpl = "select * from RDAP_NAMESERVER_STATUS"
                + " where NAMESERVER_ID in (%s)";
        final String sql = String.format(sqlTpl, nameserverIdsJoinedByComma);
        List<ModelStatus> result = jdbcTemplate.query(sql,
                new RowMapper<ModelStatus>() {
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
