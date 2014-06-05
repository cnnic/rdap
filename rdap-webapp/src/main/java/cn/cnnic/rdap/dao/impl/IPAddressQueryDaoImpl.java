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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.IPAddress;
import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.common.util.IpUtil;
import cn.cnnic.rdap.common.util.StringUtil;
import cn.cnnic.rdap.dao.AbstractQueryDao;

/**
 * IPAddress query DAO.
 * 
 * @author weijunkai
 * 
 */
@Repository
public class IPAddressQueryDaoImpl extends AbstractQueryDao<IPAddress> {

    @Override
    public List<IPAddress> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        List<IPAddress> listIPAddress = new ArrayList<IPAddress>();
        IPAddress objIPAddress = queryWithoutInnerObjects(outerObjectId);
        if (objIPAddress.getIpExisted()) {
            listIPAddress.add(objIPAddress);
        }
        return listIPAddress;
    }

    /**
     * query IPAddress, without inner objects.
     * 
     * @param outerObjectId
     *            nsId.
     * @return IPAddress.
     */
    private IPAddress queryWithoutInnerObjects(final Long outerObjectId) {
        final String sql = "select * from RDAP_NAMESERVER_IP nsIP "
                + " where nsIP.NAMESERVER_ID = ?";
        IPAddress result = jdbcTemplate.query(new PreparedStatementCreator() {
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
     * IPAddress ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class NetworkResultSetExtractor implements ResultSetExtractor<IPAddress> {
        @Override
        public IPAddress extractData(ResultSet rs) throws SQLException {
            IPAddress result = new IPAddress();

            List<String> ipV4 = new ArrayList<String>();
            List<String> ipV6 = new ArrayList<String>();
            while (rs.next()) {
                Long ipId = rs.getLong("NAMESERVER_IP_ID");
                result.setId(ipId);
                setIpVersionAndAddress(rs, ipV4, ipV6);
            }
            if (ipV4.size() > 0) {
                result.setAddressV4(ipV4);
                result.setIpExisted(true);
            }
            if (ipV6.size() > 0) {
                result.setAddressV6(ipV6);
                result.setIpExisted(true);
            }
            if (ipV4.size() <= 0 && ipV6.size() <= 0) {
                result.setIpExisted(false);
            }
            return result;
        }

        /**
         * set ip version,and high/low address.
         * 
         * @param rs
         *            ResultSet.
         * @param ipV4
         *            List<String>.
         * @param ipV6
         *            List<String>.
         * @throws SQLException
         *             SQLException.
         */
        private void setIpVersionAndAddress(ResultSet rs, List<String> ipV4,
                List<String> ipV6) throws SQLException {
            String ipVersionStr = rs.getString("VERSION");
            String highAddress = rs.getString("IP_HIGH");
            String lowAddress = rs.getString("IP_LOW");
            String realAddress = "";

            if (IpVersion.isV6(ipVersionStr)) {
                if (!StringUtils.isBlank(lowAddress)
                        && IpUtil.isIpValid(lowAddress, false)) {
                    if (StringUtils.isBlank(highAddress)) {
                        highAddress = "0";
                    }
                    if (IpUtil.isIpValid(highAddress, false)) {
                        realAddress = IpUtil.longToIpV6(
                                StringUtil.parseUnsignedLong(highAddress),
                                StringUtil.parseUnsignedLong(lowAddress));
                    }
                    if (!realAddress.isEmpty()) {
                        ipV6.add(realAddress);
                    }
                }
            } else if (IpVersion.isV4(ipVersionStr)) {
                if (lowAddress != null) {
                    realAddress = IpUtil.longToIpV4(StringUtil
                            .parseUnsignedLong(lowAddress));
                    if (!StringUtils.isBlank(realAddress)) {
                        ipV4.add(realAddress);
                    }
                }
            }
        }
    }
}
