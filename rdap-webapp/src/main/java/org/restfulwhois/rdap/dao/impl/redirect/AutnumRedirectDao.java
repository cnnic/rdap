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
package org.restfulwhois.rdap.dao.impl.redirect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.bootstrap.bean.AutnumRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.core.model.RedirectResponse;
import org.restfulwhois.rdap.core.queryparam.QueryParam;
import org.restfulwhois.rdap.redirect.dao.RedirectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * autnum redirect DAO maily select autnum object from RDAP_AUTNUM_REDIRECT.
 * overrite the query method in RedirectDao and return the RedirectResponse.
 * </pre>
 * 
 * @author jiashuo
 * 
 */
@Repository
public class AutnumRedirectDao implements RedirectDao {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AutnumRedirectDao.class);
    /**
     * save.
     */
    private static final String SAVE_REDIRECT =
            "insert into RDAP_AUTNUM_REDIRECT"
                    + "(START_AUTNUM,END_AUTNUM,REDIRECT_URL) values (?,?,?)";

    /**
     * select max id.
     */
    private static final String SELECT_MAX_ID =
            "select max(AS_REDIRECT_ID) from RDAP_AUTNUM_REDIRECT";

    /**
     * delete rows <= id.
     */
    private static final String DELETE_SMALLER_THAN_ID =
            "delete from RDAP_AUTNUM_REDIRECT where AS_REDIRECT_ID<=?";
    /**
     * JDBC template.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * <pre>
     * redirect the autnum.
     * select data from RDAP_AUTNUM_REDIRECT table.
     * </pre>
     * 
     * @param queryParam
     *            the queryParam for autnum.
     * @return RedirectResponse response of select redirect autnum.
     */
    @Override
    public RedirectResponse query(QueryParam queryParam) {
        LOGGER.debug("query, queryParam:" + queryParam);
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
            LOGGER.debug("query, result is null");
            return null;
        }
        LOGGER.debug("query, result:" + result.get(0));
        return new RedirectResponse(result.get(0));
    }

    @Override
    public void save(List<Redirect> bootstraps) {
        if (null == bootstraps || bootstraps.size() == 0) {
            LOGGER.info("bootstraps is empty, not do sync.");
            return;
        }
        Long maxOldId = getMaxId();
        LOGGER.info("get tobe delete maxOldId:{}", maxOldId);
        LOGGER.info("save new bootstraps...");
        saveNew(bootstraps);
        LOGGER.info("delete old bootstraps...");
        deleteOld(maxOldId);
    }

    /**
     * delete old redirects.
     * 
     * @param maxOldId
     *            maxOldId.
     */
    private void deleteOld(Long maxOldId) {
        if (null == maxOldId) {
            LOGGER.info("maxOldId is null, not delete.");
            return;
        }
        jdbcTemplate.update(DELETE_SMALLER_THAN_ID, maxOldId);
    }

    /**
     * save new bootstraps.
     * 
     * @param bootstraps
     *            bootstraps.
     */
    private void saveNew(List<Redirect> bootstraps) {
        List<Object[]> batchSaveParams = new ArrayList<Object[]>();
        for (Redirect bootstrap : bootstraps) {
            AutnumRedirect autnumRedirect = (AutnumRedirect) bootstrap;
            batchSaveParams.add(new Object[] { autnumRedirect.getStartAutnum(),
                    autnumRedirect.getEndAutnum(),
                    autnumRedirect.getUrls().get(0) });
        }
        if (batchSaveParams.size() > 0) {
            jdbcTemplate.batchUpdate(SAVE_REDIRECT, batchSaveParams);
        }
    }

    /**
     * get max id.
     * 
     * @return max id.
     */
    private Long getMaxId() {
        return jdbcTemplate.queryForObject(SELECT_MAX_ID, Long.class);
    }

}
