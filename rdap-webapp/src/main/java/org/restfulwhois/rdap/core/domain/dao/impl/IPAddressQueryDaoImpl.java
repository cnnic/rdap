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
import java.util.List;

import org.restfulwhois.rdap.core.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.core.common.util.IpUtil;
import org.restfulwhois.rdap.core.common.util.IpUtil.IpVersion;
import org.restfulwhois.rdap.core.ip.model.IPAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * IPAddress query DAO select IPAddress from RDAP_NAMESERVER_IP.
 * the selected ip is for nameserver.
 * </pre>
 * 
 * @author weijunkai
 * 
 */
@Repository
public class IPAddressQueryDaoImpl extends AbstractQueryDao<IPAddress> {

    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(IPAddressQueryDaoImpl.class);

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
     * query IPAddress from RDAP_NAMESERVER_IP for nameserver, without inner
     * objects.
     * 
     * @param outerObjectId
     *            nameserver id which as the relation with nameserver.
     * @return IPAddress will be set to nameserver.
     */
    private IPAddress queryWithoutInnerObjects(final Long outerObjectId) {
        final String sql =
                "select * from RDAP_NAMESERVER_IP nsIP "
                        + " where nsIP.NAMESERVER_ID = ?" + " and "
                        + IpUtil.generateNetworkRangeSql("IP", "VERSION");
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
         * set ip version,and high/low address. change them to unsigned long.
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
            IpVersion ipVersion = IpVersion.getIpVersion(ipVersionStr);
            byte[] ipBytes = rs.getBytes("IP");
            String realAddress = IpUtil.toString(ipBytes, ipVersion);
            if (StringUtils.isEmpty(realAddress)) {
                return;
            }
            if (ipVersion.isV4()) {
                ipV4.add(realAddress);
            } else if (ipVersion.isV6()) {
                ipV6.add(realAddress);
            }
        }
    }
}
