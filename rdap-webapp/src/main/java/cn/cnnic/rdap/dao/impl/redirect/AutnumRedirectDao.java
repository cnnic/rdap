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
package cn.cnnic.rdap.dao.impl.redirect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.RedirectResponse;
import cn.cnnic.rdap.dao.RedirectDao;

/**
 * autnum redirect DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class AutnumRedirectDao implements RedirectDao {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AutnumRedirectDao.class);       
    /**
     * JDBC template.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * redirect the autnum.
     * @param queryParam
     *          the queryParam for autnum.
     * @return RedirectResponse
     *          response of select redirect autnum.
     */
    @Override
    public RedirectResponse query(QueryParam queryParam) {
        LOGGER.info("query, queryParam:" + queryParam);
        final String autnumQ = queryParam.getQ();
        final String sql =
                "select *,end_autnum - start_autnum as asInterval "
                        + " from RDAP_AUTNUM_REDIRECT "
                        + " where START_AUTNUM <= ? and END_AUTNUM >= ?"
                        + " order by asInterval limit 1";
        List<String> result =
                jdbcTemplate.query(new PreparedStatementCreator() {
                    @Override
                    public PreparedStatement createPreparedStatement(
                            Connection conn) throws SQLException {
                        PreparedStatement ps = conn.prepareStatement(sql);
                        ps.setString(1, autnumQ);
                        ps.setString(2, autnumQ);
                        return ps;
                    }
                }, new RowMapper<String>() {
                    @Override
                    public String mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return rs.getString("REDIRECT_URL");
                    }

                });
        if (null == result || result.size() == 0) {
            LOGGER.info("query, result is null");
            return null;
        }
        LOGGER.info("query, result:" + result.get(0));
        return new RedirectResponse(result.get(0));
    }
}
