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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Ip;
import cn.cnnic.rdap.bean.IpQueryParam;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.common.util.IpUtil;
import cn.cnnic.rdap.common.util.StringUtil;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.NoticeDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * ip query DAO.
 * 
 * @author weijunkai
 * 
 */
@Repository
public class IpQueryDaoImpl extends AbstractQueryDao<Ip> {
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
     * entityQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;

    @Override
    public Ip query(QueryParam queryParam) {
        Ip ip = queryWithoutInnerObjects(queryParam);
        queryAndSetInnerObjects(ip);
        return ip;
    }

    /**
     * query inner objects of ip,and fill them to ip.
     * 
     * @param ip
     *            inner objects will be filled.
     */
    private void queryAndSetInnerObjects(Ip objIp) {
        if (null == objIp) {
            return;
        }
        Long ipId = objIp.getId();
        List<String> status = queryIpStatus(ipId);
        objIp.setStatus(status);
        List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(ipId,
                ModelType.IP);
        objIp.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(ipId, ModelType.IP);
        objIp.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(ipId,
                ModelType.IP);
        objIp.setEvents(events);
        List<Entity> entities =
                entityQueryDao.queryAsInnerObjects(ipId, ModelType.IP);
        objIp.setEntities(entities);
    }

    /**
     * queryIpStatus.
     * 
     * @param ipId
     *            find ip Status by ipId
     * @return list<String>
     */
    private List<String> queryIpStatus(final Long ipId) {
        final String sql = "select * from RDAP_IP_STATUS ip where "
                + " IP_ID = ?";
        List<String> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
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
     * query ip, without inner objects.
     * 
     * @param queryParam
     *            query parameter
     * @return Ip
     */
    private Ip queryWithoutInnerObjects(QueryParam queryParam) {
        IpQueryParam ipQueryParam = (IpQueryParam) queryParam;
        final BigDecimal ipQueryStartLow = ipQueryParam.getIpQueryStartLow();
        final BigDecimal ipQueryEndLow = ipQueryParam.getIpQueryEndLow();
        final String ipQueryVersion = ipQueryParam.getQueryIpVersion();
        final String sqlV6 = "select *, (ENDHIGHADDRESS-STARTHIGHADDRESS) as high,"
                + " (ENDLOWADDRESS-STARTLOWADDRESS) as low from RDAP_IP ip where "
                + " (STARTHIGHADDRESS<? or STARTHIGHADDRESS=? and STARTLOWADDRESS<=?)"
                + " && (ENDHIGHADDRESS>? or ENDHIGHADDRESS=? and ENDLOWADDRESS>=?)"
                + " && VERSION=? order by high,low limit 1";
        final BigDecimal ipQueryStartHigh = ipQueryParam.getIpQueryStartHigh();
        final BigDecimal ipQueryEndHigh = ipQueryParam.getIpQueryEndHigh();
        final String sqlV4 = "select *,(ENDLOWADDRESS-STARTLOWADDRESS) as low"
                + " from RDAP_IP where STARTLOWADDRESS<=? && ENDLOWADDRESS>=?"
                + " && STARTLOWADDRESS<POW(2,32) && ENDLOWADDRESS<POW(2,32) &&"
                + " VERSION = ? order by low limit 1";
        List<Ip> result = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = null;
                if (ipQueryVersion == "v6") {
                    ps = connection.prepareStatement(sqlV6);
                    ps.setBigDecimal(1, ipQueryStartHigh);
                    ps.setBigDecimal(2, ipQueryStartHigh);
                    ps.setBigDecimal(3, ipQueryStartLow);
                    ps.setBigDecimal(4, ipQueryEndHigh);
                    ps.setBigDecimal(5, ipQueryEndHigh);
                    ps.setBigDecimal(6, ipQueryEndLow);
                    ps.setString(7, ipQueryVersion);
                } else if (ipQueryVersion == "v4") {
                    ps = connection.prepareStatement(sqlV4);
                    ps.setBigDecimal(1, ipQueryStartLow);
                    ps.setBigDecimal(2, ipQueryEndLow);
                    ps.setString(3, ipQueryVersion);
                }
                return ps;
            }
        }, new IpResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
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
            Map<Long, String> statusMapById = new HashMap<Long, String>();
            while (rs.next()) {
                Long statusId = rs.getLong("IP_STATUS_ID");
                String status = statusMapById.get(statusId);
                if (null == status) {
                    status = rs.getString("STATUS");
                    if (!result.contains(status)) {
                        statusMapById.put(statusId, status);
                        result.add(status);
                    }
                }
            }
            return result;
        }
    }

    /**
     * IP ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class IpResultSetExtractor implements ResultSetExtractor<List<Ip>> {
        @Override
        public List<Ip> extractData(ResultSet rs) throws SQLException {
            List<Ip> result = new ArrayList<Ip>();
            Map<Long, Ip> ipMapById = new HashMap<Long, Ip>();
            while (rs.next()) {
                Long ipId = rs.getLong("IP_ID");
                Ip objIp = ipMapById.get(ipId);
                if (null == objIp) {
                    objIp = new Ip();
                    objIp.setId(ipId);
                    objIp.setHandle(rs.getString("HANDLE"));
                    setIpVersionAndStartEndAddress(rs, objIp);
                    objIp.setName(rs.getString("NAME"));
                    objIp.setType(rs.getString("TYPE"));
                    objIp.setCountry(rs.getString("COUNTRY"));
                    objIp.setParentHandle(rs.getString("PARENT_HANDLE"));
                    objIp.setLang(rs.getString("LANG"));
                    objIp.setPort43(rs.getString("PORT43"));
                    result.add(objIp);
                    ipMapById.put(ipId, objIp);
                }
            }
            return result;
        }

        /**
         * set ip version,and start/end address.
         * 
         * @param rs
         *            ResultSet.
         * @param objIp
         *            Ip.
         * @throws SQLException
         *             SQLException.
         */
        private void setIpVersionAndStartEndAddress(ResultSet rs, Ip objIp)
                throws SQLException {
            String ipVersionStr = rs.getString("VERSION");
            String startHighAddress = rs.getString("STARTHIGHADDRESS");
            String startLowAddress = rs.getString("STARTLOWADDRESS");
            String endHighAddress = rs.getString("ENDHIGHADDRESS");
            String endLowAddress = rs.getString("ENDLOWADDRESS");
            String startAddress = "";
            String endAddress = "";
            if (IpVersion.isV6(ipVersionStr)) {
                objIp.setIpVersion("v6");
                startAddress = IpUtil.longToIpV6(
                        StringUtil.parseUnsignedLong(startHighAddress),
                        StringUtil.parseUnsignedLong(startLowAddress));
                endAddress = IpUtil.longToIpV6(
                        StringUtil.parseUnsignedLong(endHighAddress),
                        StringUtil.parseUnsignedLong(endLowAddress));
            } else if (IpVersion.isV4(ipVersionStr)) {
                objIp.setIpVersion("v4");
                startAddress = IpUtil.longToIpV4(StringUtil
                        .parseUnsignedLong(startLowAddress));
                endAddress = IpUtil.longToIpV4(StringUtil
                        .parseUnsignedLong(endLowAddress));
            }
            objIp.setStartAddress(startAddress);
            objIp.setEndAddress(endAddress);
        }
    }
}
