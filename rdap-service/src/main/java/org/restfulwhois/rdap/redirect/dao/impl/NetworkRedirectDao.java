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
package org.restfulwhois.rdap.redirect.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.bootstrap.bean.NetworkRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.NetworkInBytes;
import org.restfulwhois.rdap.core.ip.dao.impl.NetworkQueryDaoImpl;
import org.restfulwhois.rdap.core.ip.queryparam.NetworkQueryParam;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
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
 * network redirect DAO mainly select network object from RDAP_IP_REDIRECT.
 * query method overwrite the counterpart in RedirectDao.
 * </pre>
 * 
 * @author jiashuo
 * 
 */
@Repository
public class NetworkRedirectDao implements RedirectDao {

    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(NetworkRedirectDao.class);
    /**
     * save network redirect.
     */
    private static final String SAVE_NETWORK_REDIRECT =
            "insert into RDAP_IP_REDIRECT(STARTADDRESS,ENDADDRESS"
                    + ",REDIRECT_URL,VERSION) values(?,?,?,?)";
    /**
     * select max id.
     */
    private static final String SELECT_MAX_ID =
            "select max(RDAP_IP_REDIRECT_ID) from RDAP_IP_REDIRECT";

    /**
     * delete rows <= id.
     */
    private static final String DELETE_SMALLER_THAN_ID =
            "delete from RDAP_IP_REDIRECT where RDAP_IP_REDIRECT_ID<=?"
                    + " and VERSION=?";
    /**
     * JDBC template.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * redirect the network by select object from RDAP_IP_REDIRECT.
     * 
     * @param queryParam
     *            the queryParam for network.
     * @return RedirectResponse response to select redirect network.
     */
    @Override
    public RedirectResponse query(QueryParam queryParam) {
        LOGGER.debug("query, queryParam:" + queryParam);
        PreparedStatementCreator pstatCreator =
                NetworkQueryDaoImpl.generatePStatCreator(queryParam,
                        "RDAP_IP_REDIRECT");
        List<String> result =
                jdbcTemplate.query(pstatCreator, new RowMapper<String>() {
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
        deleteOld(maxOldId, bootstraps);
    }

    /**
     * delete old redirects.
     * <p>
     * NOTICE not to delete another IP type rows which just inserted.
     * </p>
     * 
     * @param maxOldId
     *            maxOldId.
     * @param bootstraps
     *            bootstraps.
     */
    private void deleteOld(Long maxOldId, List<Redirect> bootstraps) {
        if (null == maxOldId) {
            LOGGER.info("maxOldId is null, not delete old.");
            return;
        }
        if (null == bootstraps || bootstraps.size() == 0) {
            LOGGER.info("bootstraps is empty, not delete old.");
            return;
        }
        NetworkRedirect redirect = (NetworkRedirect) bootstraps.get(0);
        IpVersion ipVersion =
                redirect.getNetworkQueryParam().getNetworkInBytes()
                        .getIpVersion();
        jdbcTemplate.update(DELETE_SMALLER_THAN_ID, maxOldId,
                ipVersion.getName());
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
            NetworkRedirect redirect = (NetworkRedirect) bootstrap;
            NetworkQueryParam q = redirect.getNetworkQueryParam();
            NetworkInBytes networkInBytes = q.getNetworkInBytes();
            batchSaveParams.add(new Object[] {
                    networkInBytes.getStartAddress(),
                    networkInBytes.getEndAddress(), bootstrap.getUrls().get(0),
                    networkInBytes.getIpVersion().getName() });
        }
        if (batchSaveParams.size() > 0) {
            jdbcTemplate.batchUpdate(SAVE_NETWORK_REDIRECT, batchSaveParams);
        }
    }

    /**
     * get max id .
     * 
     * @return max id.
     */
    private Long getMaxId() {
        return jdbcTemplate.queryForObject(SELECT_MAX_ID, Long.class);
    }
}
