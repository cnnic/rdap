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

import org.restfulwhois.rdap.common.support.PageBean;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.core.domain.dao.impl.DomainQueryDaoImpl;
import org.restfulwhois.rdap.core.domain.model.Domain;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchByDomainNameParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

/**
 * search domain by name.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class DomainSearchByNameStrategy extends AbstractDomainSearchStrategy {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DomainSearchByNameStrategy.class);

    @Override
    public boolean support(QueryParam queryParam) {
        return queryParam instanceof DomainSearchByDomainNameParam;
    }

    @Override
    public Long searchCount(QueryParam queryParam, JdbcTemplate jdbcTemplate) {
        DomainSearchParam domainSearchParam = (DomainSearchParam) queryParam;
        final String domainName = domainSearchParam.getQ();
        final String punyName = domainSearchParam.getPunyName();
        final String domainNameLikeClause = generateLikeClause(domainName);
        final String punyNameLikeClause = generateLikeClause(punyName);
        final String sql =
                "select count(1) as COUNT from RDAP_DOMAIN domain "
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
        return domainCount;
    }

    @Override
    public List<Domain>
            search(QueryParam queryParam, JdbcTemplate jdbcTemplate) {
        DomainSearchParam domainSearchParam = (DomainSearchParam) queryParam;
        final String domainName = domainSearchParam.getQ();
        final String punyName = domainSearchParam.getPunyName();
        final String domainNameLikeClause = generateLikeClause(domainName);
        final String punyNameLikeClause = generateLikeClause(punyName);
        final String sql =
                "select * from RDAP_DOMAIN domain "
                        + DomainQueryDaoImpl.SQL_LEFT_JOIN_DOMAIN_STATUS
                        + " where LDH_NAME like ? or UNICODE_NAME like ? "
                        + " order by domain.LDH_NAME limit ?,? ";
        final PageBean page = domainSearchParam.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        DomainQueryDaoImpl domainDao = new DomainQueryDaoImpl();
        List<Domain> result = null;
        result = jdbcTemplate.query(new PreparedStatementCreator() {
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
        }, domainDao.new DomainWithStatusResultSetExtractor());
        return result;
    }

}
