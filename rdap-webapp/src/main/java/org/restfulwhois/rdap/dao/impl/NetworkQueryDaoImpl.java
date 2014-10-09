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

import org.restfulwhois.rdap.bean.Entity;
import org.restfulwhois.rdap.bean.Event;
import org.restfulwhois.rdap.bean.Link;
import org.restfulwhois.rdap.bean.ModelType;
import org.restfulwhois.rdap.bean.Network;
import org.restfulwhois.rdap.bean.Network.IpVersion;
import org.restfulwhois.rdap.bean.NetworkQueryParam;
import org.restfulwhois.rdap.bean.QueryParam;
import org.restfulwhois.rdap.bean.Remark;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.restfulwhois.rdap.dao.AbstractQueryDao;
import org.restfulwhois.rdap.dao.NoticeDao;
import org.restfulwhois.rdap.dao.QueryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * network query DAO select ip from RDAP_IP.
 * need to query notice remark link event entity as inner objects.
 * for ipAddress,support ipv4 and ipv6.
 * </pre>
 * 
 * @author weijunkai
 * 
 */
@Repository
public class NetworkQueryDaoImpl extends AbstractQueryDao<Network> {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NetworkQueryDaoImpl.class);
    /**
     * notice dao.
     */
    @Autowired
    @Qualifier("noticeDaoImpl")
    private NoticeDao noticeDao;
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
     * domainQueryDao.
     */
    @Autowired
    private DomainQueryDaoImpl domainQueryDao;

    /**
     * entityQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;

    /**
     * query network object.
     * 
     * @param queryParam
     *            query parameter.
     * @return network ip address.
     */
    @Override
    public Network query(QueryParam queryParam) {
        LOGGER.debug("query, queryParam:{}", queryParam);
        Network network = queryWithoutInnerObjects(queryParam);
        queryAndSetInnerObjectsWithoutEntities(network);
        queryAndSetEntities(network);
        LOGGER.debug("query, network:{}", network);
        return network;
    }

    /**
     * query and set entities to network.
     * 
     * @param network
     *            which will be set with entities.
     */
    private void queryAndSetEntities(Network network) {
        if (null == network) {
            return;
        }
        List<Entity> entities =
                entityQueryDao.queryAsInnerObjects(network.getId(),
                        ModelType.IP);
        network.setEntities(entities);
    }

    /**
     * query network for arpa and entity.
     * 
     * @param outerObjectId
     *            outer object related to network
     * @param outerModelType
     *            outer object type related to network
     * @return network list.
     */
    @Override
    public List<Network> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        LOGGER.debug("queryAsInnerObjects,outerObjId:{},outerModel:{}",
                outerObjectId, outerModelType);
        if (ModelType.ENTITY.equals(outerModelType)) {
            List<Network> networks =
                    queryWithoutInnerObjectsForEntity(outerObjectId);
            queryAndSetInnerObjects(networks);
            LOGGER.debug("for entities result size:{}", networks.size());
            return networks;
        }
        if (ModelType.DOMAIN.equals(outerModelType)) {
            List<Network> networks =
                    findByArpaDomainId(outerObjectId);
            queryAndSetInnerObjects(networks);
            LOGGER.debug("for arpa result size:{}", networks.size());
            return networks;
        }
        throw new UnsupportedOperationException(
                "only support ENTITY/ARPA model.");
    }

    /**
     * find by ARPA domain id.
     * 
     * @param arpaDomainId
     *            arpaDomainId.
     * @return network list.
     */
    private List<Network> findByArpaDomainId(final Long arpaDomainId) {
        final String sql = "select * from RDAP_IP ip"
                + " inner join RDAP_DOMAIN d "
                + " on ip.IP_ID=d.NETWORK_ID "
                + " left outer join RDAP_IP_STATUS status"
                + " on ip.IP_ID = status.IP_ID "
                + " where d.DOMAIN_ID=?";
        List<Network> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, arpaDomainId);
                        return ps;
                    }
                }, new NetworkWithStatusResultSetExtractor());
        return result;
    }

    /**
     * query and set inner objects for network.
     * 
     * @param networks
     *            networks which will be set.
     */
    private void queryAndSetInnerObjects(List<Network> networks) {
        if (null == networks) {
            return;
        }
        for (Network network : networks) {
            queryAndSetInnerObjectsWithoutEntities(network);
        }
    }

    /**
     * query network from RDAP_IP, without inner objects.Only support ENTITY.
     * 
     * @param outerObjectId
     *            entity id.
     * @return network list
     */
    private List<Network> queryWithoutInnerObjectsForEntity(
            final Long outerObjectId) {
        final String sql =
                "select * from RDAP_IP ip inner join "
                        + " REL_ENTITY_REGISTRATION rel "
                        + " on ip.IP_ID = rel.REL_ID "
                        + " left outer join RDAP_IP_STATUS status on ip.IP_ID = "
                        + " status.IP_ID where rel.ENTITY_ID = ? "
                        + " and REL_OBJECT_TYPE=? " + " order by ip.HANDLE ";
        List<Network> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, ModelType.IP.getName());
                        return ps;
                    }
                }, new NetworkWithStatusResultSetExtractor());
        return result;
    }

    /**
     * network with status resultSet extractor.
     * 
     * @author jiashuo
     * 
     */
    class NetworkWithStatusResultSetExtractor implements
            ResultSetExtractor<List<Network>> {
        @Override
        public List<Network> extractData(ResultSet rs) throws SQLException {
            List<Network> result = new ArrayList<Network>();
            Map<Long, Network> networkMapById = new HashMap<Long, Network>();
            while (rs.next()) {
                Long networkId = rs.getLong("IP_ID");
                Network network = networkMapById.get(networkId);
                if (null == network) {
                    network = extractNetworkFromRs(rs, networkId);
                    result.add(network);
                    networkMapById.put(networkId, network);
                }
                network.addStatus(rs.getString("STATUS"));
            }
            return result;
        }
    }

    /**
     * query inner objects of ip,and fill them to ip.
     * 
     * @param objIp
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjectsWithoutEntities(Network objIp) {
        if (null == objIp) {
            return;
        }
        Long ipId = objIp.getId();
        List<String> status = queryNetworkStatus(ipId);
        objIp.setStatus(status);
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(ipId, ModelType.IP);
        objIp.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(ipId, ModelType.IP);
        objIp.setLinks(links);
        List<Event> events =
                eventQueryDao.queryAsInnerObjects(ipId, ModelType.IP);
        objIp.setEvents(events);
    }

    /**
     * query ip status from RDAP_IP_STATUS.
     * 
     * @param ipId
     *            find ip Status by ipId
     * @return list of status
     */
    private List<String> queryNetworkStatus(final Long ipId) {
        final String sql =
                "select * from RDAP_IP_STATUS ip where " + " IP_ID = ?";
        List<String> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, ipId);
                        return ps;
                    }
                }, new StatusResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result;
    }

    /**
     * query ip using queryParam, without inner objects.
     * 
     * @param queryParam
     *            query parameter of network
     * @return Ip object
     */
    private Network queryWithoutInnerObjects(QueryParam queryParam) {
        PreparedStatementCreator pstatCreator =
                generatePStatCreator(queryParam, "RDAP_IP");
        List<Network> result =
                jdbcTemplate.query(pstatCreator,
                        new NetworkResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    /**
     * <pre>
     * generate PreparedStatementCreator by executing sql.
     * select network ip by high address and low address and match the 
     * most accurate one.
     * </pre>
     * 
     * @param queryParam
     *            queryParam of network.
     * @param ipTableName
     *            the database ip table name.
     * @return PreparedStatementCreator.
     */
    public static PreparedStatementCreator generatePStatCreator(
            QueryParam queryParam, String ipTableName) {
        NetworkQueryParam ipQueryParam = (NetworkQueryParam) queryParam;
        final BigDecimal ipQueryStartLow = ipQueryParam.getIpQueryStartLow();
        final BigDecimal ipQueryEndLow = ipQueryParam.getIpQueryEndLow();
        final IpVersion ipVersion = ipQueryParam.getQueryIpVersion();
        final String sqlV6 =
                "select *, (ENDHIGHADDRESS-STARTHIGHADDRESS) as high,"
                        + " (ENDLOWADDRESS-STARTLOWADDRESS) as low from "
                        + ipTableName
                        + " ip where (STARTHIGHADDRESS<? or STARTHIGHADDRESS=?"
                        + " and STARTLOWADDRESS<=?) && (ENDHIGHADDRESS>?"
                        + " or ENDHIGHADDRESS=? and ENDLOWADDRESS>=?)"
                        + " && VERSION=? order by high,low limit 1";
        final BigDecimal ipQueryStartHigh = ipQueryParam.getIpQueryStartHigh();
        final BigDecimal ipQueryEndHigh = ipQueryParam.getIpQueryEndHigh();
        final String sqlV4 =
                "select *,(ENDLOWADDRESS-STARTLOWADDRESS) as low"
                        + " from "
                        + ipTableName
                        + " where STARTLOWADDRESS<=? && ENDLOWADDRESS>=?"
                        + " && STARTLOWADDRESS<POW(2,32) && ENDLOWADDRESS<POW(2,32) &&"
                        + " VERSION = ? order by low limit 1";
        PreparedStatementCreator pstatCreator = new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = null;
                if (ipVersion == IpVersion.V6) {
                    ps = connection.prepareStatement(sqlV6);
                    ps.setBigDecimal(1, ipQueryStartHigh);
                    ps.setBigDecimal(2, ipQueryStartHigh);
                    ps.setBigDecimal(3, ipQueryStartLow);
                    ps.setBigDecimal(4, ipQueryEndHigh);
                    ps.setBigDecimal(5, ipQueryEndHigh);
                    ps.setBigDecimal(6, ipQueryEndLow);
                    ps.setString(7, ipVersion.getName());
                } else if (ipVersion == IpVersion.V4) {
                    ps = connection.prepareStatement(sqlV4);
                    ps.setBigDecimal(1, ipQueryStartLow);
                    ps.setBigDecimal(2, ipQueryEndLow);
                    ps.setString(3, ipVersion.getName());
                }
                return ps;
            }
        };
        return pstatCreator;
    }

    /**
     * Status ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class StatusResultSetExtractor implements ResultSetExtractor<List<String>> {
        @Override
        public List<String> extractData(ResultSet rs) throws SQLException {
            List<String> result = new ArrayList<String>();

            while (rs.next()) {
                String status = rs.getString("STATUS");
                if (!result.contains(status)) {
                    result.add(status);
                }
            }
            return result;
        }
    }

    /**
     * extract network from ResultSet.
     * 
     * @param rs
     *            ResultSet.
     * @param networkId
     *            network Id.
     * @return Network.
     * @throws SQLException
     *             SQLException.
     */
    private Network extractNetworkFromRs(ResultSet rs, Long networkId)
            throws SQLException {
        Network network = new Network();
        network.setId(networkId);
        network.setHandle(rs.getString("HANDLE"));
        setIpVersionAndStartEndAddress(rs, network);
        network.setName(rs.getString("NAME"));
        network.setType(rs.getString("TYPE"));
        network.setCountry(rs.getString("COUNTRY"));
        network.setParentHandle(rs.getString("PARENT_HANDLE"));
        network.setLang(rs.getString("LANG"));
        network.setPort43(rs.getString("PORT43"));
        return network;
    }

    /**
     * set ip version,and start/end address.
     * 
     * @param rs
     *            ResultSet.
     * @param objIp
     *            Ip will be set to.
     * @throws SQLException
     *             SQLException.
     */
    private void setIpVersionAndStartEndAddress(ResultSet rs, Network objIp)
            throws SQLException {
        String ipVersionStr = rs.getString("VERSION");
        String startHighAddress = rs.getString("STARTHIGHADDRESS");
        String startLowAddress = rs.getString("STARTLOWADDRESS");
        String endHighAddress = rs.getString("ENDHIGHADDRESS");
        String endLowAddress = rs.getString("ENDLOWADDRESS");
        String startAddress = "";
        String endAddress = "";
        if (IpVersion.isV6(ipVersionStr)) {
            objIp.setIpVersion(IpVersion.V6);
            long longHighStart = StringUtil.parseUnsignedLong(startHighAddress);
            long longLowStart = StringUtil.parseUnsignedLong(startLowAddress);
            startAddress = IpUtil.longToIpV6(longHighStart, longLowStart);
            long longHighEnd = StringUtil.parseUnsignedLong(endHighAddress);
            long longLowEnd = StringUtil.parseUnsignedLong(endLowAddress);
            endAddress = IpUtil.longToIpV6(longHighEnd, longLowEnd);
        } else if (IpVersion.isV4(ipVersionStr)) {
            objIp.setIpVersion(IpVersion.V4);
            startAddress =
                    IpUtil.longToIpV4(StringUtil
                            .parseUnsignedLong(startLowAddress));
            endAddress =
                    IpUtil.longToIpV4(StringUtil
                            .parseUnsignedLong(endLowAddress));
        }
        objIp.setStartAddress(startAddress);
        objIp.setEndAddress(endAddress);
    }

    /**
     * IP ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class NetworkResultSetExtractor implements
            ResultSetExtractor<List<Network>> {
        @Override
        public List<Network> extractData(ResultSet rs) throws SQLException {
            List<Network> result = new ArrayList<Network>();
            while (rs.next()) {
                Long networkId = rs.getLong("IP_ID");
                Network network = extractNetworkFromRs(rs, networkId);
                result.add(network);
            }
            return result;
        }
    }
}
