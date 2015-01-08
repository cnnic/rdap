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
package org.restfulwhois.rdap.search.entity.dao.strategy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.support.PageBean;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.core.entity.dao.impl.EntityQueryDaoImpl;
import org.restfulwhois.rdap.search.common.dao.SearchStrategy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * abstract entity search strategy.
 * 
 * @author jiashuo
 * 
 */
public abstract class AbstractEntitySearchStrategy implements
        SearchStrategy<Entity> {

    /**
     * searchCount.
     * 
     * @param queryParam
     *            queryParam.
     * @param jdbcTemplate
     *            jdbcTemplate.
     * @param searchColumnName
     *            searchColumnName.
     * @return count.
     */
    protected Long searchCount(QueryParam queryParam,
            JdbcTemplate jdbcTemplate, final String searchColumnName) {
        final String q = queryParam.getQ();
        final String qLikeClause = generateLikeClause(q);
        final String sql =
                "select count(1) as COUNT from RDAP_ENTITY " + " where "
                        + searchColumnName + " like ? ";
        Long entityCount = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, qLikeClause);
                return ps;
            }
        }, new CountResultSetExtractor());
        return entityCount;
    }

    /**
     * search entities.
     * 
     * @param queryParam
     *            queryParam.
     * @param jdbcTemplate
     *            jdbcTemplate.
     * @param searchColumnName
     *            searchColumnName.
     * @return entity list.
     */
    protected List<Entity> search(QueryParam queryParam,
            JdbcTemplate jdbcTemplate, String searchColumnName) {
        final String q = queryParam.getQ();
        final String qLikeClause = generateLikeClause(q);
        final String sql =
                "select * from RDAP_ENTITY entity where " + searchColumnName
                        + " like ? " + " order by entity.HANDLE limit ?,? ";
        final PageBean page = queryParam.getPageBean();
        int startPage = page.getCurrentPage() - 1;
        startPage = startPage >= 0 ? startPage : 0;
        final long startRow = startPage * page.getMaxRecords();
        List<Entity> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, qLikeClause);
                        ps.setLong(2, startRow);
                        ps.setLong(3, page.getMaxRecords());
                        return ps;
                    }
                }, new EntityResultSetExtractor());
        return result;
    }

    /**
     * EntityResultSetExtractor extract entity from result set.
     * 
     * @author jiashuo
     * 
     */
    class EntityResultSetExtractor implements ResultSetExtractor<List<Entity>> {
        @Override
        public List<Entity> extractData(ResultSet rs) throws SQLException {
            List<Entity> result = new ArrayList<Entity>();
            while (rs.next()) {
                Entity entity = new Entity();
                result.add(entity);
                EntityQueryDaoImpl.extractEntityFromRs(rs, entity);
            }
            return result;
        }
    }

    /**
     * count the number of resultSet.
     * 
     */
    class CountResultSetExtractor implements ResultSetExtractor<Long> {
        @Override
        public Long extractData(ResultSet rs) throws SQLException {
            Long result = 0L;
            if (rs.next()) {
                result = rs.getLong("COUNT");
            }
            return result;
        }
    }

    /**
     * searchColumnName.
     * 
     * @param q
     *            q.
     * @return string.
     */
    private String generateLikeClause(String q) {
        return AbstractQueryDao.generateLikeClause(q);
    }

}
