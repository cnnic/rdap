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
package org.restfulwhois.rdap.search.nameserver.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.support.PageBean;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.core.nameserver.queryparam.NameserverSearchByIpParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

/**
 * search nameserver by IP.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class NameserverSearchByIpStrategy extends
        AbstractNameserverSearchStrategy {

    @Override
    public boolean support(QueryParam queryParam) {
        return queryParam instanceof NameserverSearchByIpParam;
    }

    @Override
    public Long searchCount(QueryParam queryParam, JdbcTemplate jdbcTemplate) {
        final NameserverSearchByIpParam searchByIpParam =
                (NameserverSearchByIpParam) queryParam;
        final String sql =
                "select count(1) as COUNT from RDAP_NAMESERVER_IP "
                        + " where IP = ? ";
        Long recordsCount = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setBytes(1, searchByIpParam.getIpInBytes().getBytes());
                return ps;
            }
        }, new CountResultSetExtractor());
        return recordsCount;
    }

    @Override
    public List<Nameserver> search(QueryParam queryParam,
            JdbcTemplate jdbcTemplate) {
        List<Nameserver> result = null;
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(queryParam.getQ());
        if (ipVersion.isNotValidIp()) {
            return result;
        }
        final PageBean page = queryParam.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        final byte[] ipInBytes =
                IpUtil.ipToByteArray(queryParam.getQ(), ipVersion);
        final String sql =
                "select * from RDAP_NAMESERVER ns,RDAP_NAMESERVER_IP ip"
                        + " where ns.NAMESERVER_ID=ip.NAMESERVER_ID and "
                        + " IP = ? order by ns.LDH_NAME limit ?,? ";
        result = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setBytes(1, ipInBytes);
                ps.setLong(2, startRow);
                ps.setLong(3, page.getMaxRecords());
                return ps;
            }
        }, new NameserverResultSetExtractor());
        return result;
    }

}
