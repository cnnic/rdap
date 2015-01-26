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
package org.restfulwhois.rdap.search.domain.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.support.PageBean;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.core.domain.dao.impl.DomainQueryDaoImpl;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchByNsIpParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

/**
 * search domain by NS IP.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class DomainSearchByNsIpStrategy extends AbstractDomainSearchStrategy {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DomainSearchByNsIpStrategy.class);

    @Override
    public boolean support(QueryParam queryParam) {
        return queryParam instanceof DomainSearchByNsIpParam;
    }

    @Override
    public Long searchCount(QueryParam queryParam, JdbcTemplate jdbcTemplate) {
        LOGGER.debug("searchCount, queryParam:" + queryParam);
        final DomainSearchByNsIpParam searchByNsIpParam =
                (DomainSearchByNsIpParam) queryParam;
        final String sql =
                "select COUNT(*) as COUNT" + " from RDAP_DOMAIN t1 "
                        + " inner join REL_DOMAIN_NAMESERVER t2"
                        + " on t1.DOMAIN_ID = t2.DOMAIN_ID "
                        + " inner join RDAP_NAMESERVER_IP t3 on "
                        + " t2.NAMESERVER_ID = t3.NAMESERVER_ID"
                        + " where t3.IP = ?";
        Long recordsCount = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setBytes(1, searchByNsIpParam.getIpInBytes().getBytes());
                return ps;
            }
        }, new CountResultSetExtractor());
        return recordsCount;
    }

    @Override
    public List<Domain>
            search(QueryParam queryParam, JdbcTemplate jdbcTemplate) {
        DomainSearchParam domainQueryParam = (DomainSearchParam) queryParam;
        List<Domain> result = null;
        final PageBean page = domainQueryParam.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        String ipPrefix = domainQueryParam.getQ();
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(ipPrefix);
        final byte[] ipBytes = IpUtil.ipToByteArray(ipPrefix, ipVersion);
        DomainQueryDaoImpl domainDao = new DomainQueryDaoImpl();
        final String sql =
                "SELECT distinct domain.* FROM  RDAP_DOMAIN domain "
                        + " INNER JOIN REL_DOMAIN_NAMESERVER rel "
                        + " ON domain.DOMAIN_ID = rel.DOMAIN_ID "
                        + " INNER JOIN RDAP_NAMESERVER_IP nsip "
                        + " ON rel.NAMESERVER_ID = nsip.NAMESERVER_ID "
                        + " where nsip.IP = ? order by domain.LDH_NAME limit ?,?";
        result = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setBytes(1, ipBytes);
                ps.setLong(2, startRow);
                ps.setLong(3, page.getMaxRecords());
                return ps;
            }
        }, domainDao.new DomainWithStatusResultSetExtractor());
        for(Domain domain : result){
        	domainDao.queryDomainStatus(domain, jdbcTemplate);
        }
        return result;
    }

}
