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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.common.util.IpUtil;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * network query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class NetworkQueryDaoImpl extends AbstractQueryDao<Network> {
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

    @Override
    public List<Network> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        List<Network> networks = queryWithoutInnerObjects(outerObjectId);
        if (networks.size() > 1) {
            Network network = networks.get(0);
            networks = new ArrayList<Network>();
            networks.add(network);
        }
        queryAndSetInnerObjects(networks);
        return networks;
    }

    /**
     * query inner objects of network,and set fill them to network.Note: only
     * handle first one item.
     * 
     * @param networks
     *            inner objects will be filled
     */
    private void queryAndSetInnerObjects(List<Network> networks) {
        if (null == networks || networks.size() == 0) {
            return;
        }
        Network network = networks.get(0);
        Long networkId = network.getId();
        List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(networkId,
                ModelType.IP);
        network.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(networkId,
                ModelType.IP);
        network.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(networkId,
                ModelType.IP);
        network.setEvents(events);
    }

    /**
     * query network, without inner objects.
     * 
     * @param outerObjectId
     *            domainId.
     * @return network.
     */
    private List<Network> queryWithoutInnerObjects(final Long outerObjectId) {
        final String sql = "select * from RDAP_IP ip inner join "
                + " REL_DOMAIN_NETWORK rel on ip.IP_ID = rel.NETWORK_ID "
                + " left outer join RDAP_IP_STATUS status on ip.IP_ID = "
                + "status.IP_ID where rel.DOMAIN_ID=? ";
        List<Network> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        return ps;
                    }
                }, new NetworkResultSetExtractor());
        return result;
    }

    /**
     * network ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class NetworkResultSetExtractor implements
            ResultSetExtractor<List<Network>> {
        @Override
        public List<Network> extractData(ResultSet rs) throws SQLException {
            List<Network> result = new ArrayList<Network>();
            Map<Long, Network> networkMapById = new HashMap<Long, Network>();
            while (rs.next()) {
                Long networkId = rs.getLong("IP_ID");
                Network network = networkMapById.get(networkId);
                if (null == network) {
                    network = new Network();
                    network.setId(networkId);
                    network.setHandle(rs.getString("HANDLE"));
                    setIpVersionAndStartEndAddress(rs, network);
                    network.setName(rs.getString("NAME"));
                    network.setType(rs.getString("TYPE"));
                    network.setCountry(rs.getString("COUNTRY"));
                    network.setParentHandle(rs.getString("PARENT_HANDLE"));
                    network.setLang(rs.getString("LANG"));
                    network.setPort43(rs.getString("PORT43"));
                    result.add(network);
                    networkMapById.put(networkId, network);
                }
                network.addStatus(rs.getString("STATUS"));
            }
            return result;
        }

        /**
         * set ip version,and start/end address.
         * 
         * @param rs
         *            ResultSet.
         * @param network
         *            network.
         * @throws SQLException
         *             SQLException.
         */
        private void setIpVersionAndStartEndAddress(ResultSet rs,
                Network network) throws SQLException {
            String ipVersionStr = rs.getString("VERSION");
            String startHighAddress = rs.getString("STARTHIGHADDRESS");
            String startLowAddress = rs.getString("STARTLOWADDRESS");
            String endHighAddress = rs.getString("ENDHIGHADDRESS");
            String endLowAddress = rs.getString("ENDLOWADDRESS");
            String startAddress = "";
            String endAddress = "";
            if (IpVersion.isV6(ipVersionStr)) {
                network.setIpVersion(IpVersion.V6);
                startAddress = IpUtil.longToIpV6(
                        Long.parseLong(startHighAddress),
                        Long.parseLong(startLowAddress));
                endAddress = IpUtil.longToIpV6(
                        Long.parseLong(endHighAddress),
                        Long.parseLong(endLowAddress));
            } else if (IpVersion.isV4(ipVersionStr)) {
                network.setIpVersion(IpVersion.V4);
                startAddress = IpUtil.longToIpV4(Long
                        .parseLong(startLowAddress));
                endAddress = IpUtil.longToIpV4(Long.parseLong(endLowAddress));
            }
            network.setStartAddress(startAddress);
            network.setEndAddress(endAddress);
        }
    }
}
