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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.bean.DomainQueryParam;
import org.restfulwhois.rdap.bean.QueryParam;
import org.restfulwhois.rdap.bean.RedirectResponse;
import org.restfulwhois.rdap.bootstrap.bean.DomainRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.restfulwhois.rdap.dao.RedirectDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * <pre>
 * domain redirect DAO mainly select domain object from database.
 * query method overwrite the counterpart in RedirectDao.
 * </pre>
 * 
 * @author jiashuo
 * 
 */
@Repository
public class DomainRedirectDao implements RedirectDao {

    /**
     * save.
     */
    private static final String SAVE_DOMAIN_REDIRECT =
            "insert into RDAP_DOMAIN_REDIRECT(REDIRECT_TLD,REDIRECT_URL) values (?,?)";

    /**
     * delete by tld.
     */
    private static final String DELETE_DOMAIN_REDIRECT_BY_TLD =
            "delete from RDAP_DOMAIN_REDIRECT where REDIRECT_TLD = ?";
    /**
     * select max id.
     */
    private static final String SELECT_MAX_ID =
            "select max(RDAP_DOMAIN_REDIRECT_ID) from RDAP_DOMAIN_REDIRECT";

    /**
     * delete rows <= id.
     */
    private static final String DELETE_SMALLER_THAN_ID =
            "delete from RDAP_DOMAIN_REDIRECT where RDAP_DOMAIN_REDIRECT_ID<=?";
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DomainRedirectDao.class);

    /**
     * JDBC template.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * redirect the domain by select object from RDAP_DOMAIN_REDIRECT.
     * 
     * @param queryParam
     *            parameter for domain query.
     * @return RedirectResponse response to select redirect domain.
     */
    @Override
    public RedirectResponse query(QueryParam queryParam) {
        LOGGER.debug("query, queryParam:" + queryParam);
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        String fullPunyTld = domainQueryParam.getFullPunyTld();
        fullPunyTld = StringUtils.replace(fullPunyTld, "'", "''");
        final String joinedTldQCond = getJoinedTldQCondition(fullPunyTld);
        final String sql =
                "select * from RDAP_DOMAIN_REDIRECT "
                        + " where REDIRECT_TLD in (" + joinedTldQCond + ") "
                        + " order by char_length(REDIRECT_TLD) desc limit 1";
        List<String> result = jdbcTemplate.query(sql, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
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

    /**
     * get joined tld query string condition: 'cn','com.cn'.
     * 
     * @param fullTld
     *            fullTld.
     * @return joined query condition.
     */
    private String getJoinedTldQCondition(String fullTld) {
        LOGGER.debug("getJoinedTldQCondition, fullTld:" + fullTld);
        if (StringUtils.isBlank(fullTld)) {
            return null;
        }
        List<String> tldList = new ArrayList<String>();
        String currentTld = fullTld;
        tldList.add(StringUtil.addQuotas(currentTld));
        while (StringUtils.isNotBlank(currentTld)) {
            String subTld =
                    StringUtils.substringAfter(currentTld,
                            StringUtil.TLD_SPLITOR);
            if (StringUtils.isNotBlank(subTld)) {
                tldList.add(StringUtil.addQuotas(subTld));
            }
            currentTld = subTld;
        }
        return StringUtils.join(tldList, ",");
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
            DomainRedirect domainRedirect = (DomainRedirect) bootstrap;
            batchSaveParams.add(new String[] { domainRedirect.getTld(),
                    domainRedirect.getUrls().get(0) });
        }
        if (batchSaveParams.size() > 0) {
            jdbcTemplate.batchUpdate(SAVE_DOMAIN_REDIRECT, batchSaveParams);
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
