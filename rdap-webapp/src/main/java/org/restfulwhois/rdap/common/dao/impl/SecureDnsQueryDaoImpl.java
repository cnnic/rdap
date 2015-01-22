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
package org.restfulwhois.rdap.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.common.dao.QueryDao;
import org.restfulwhois.rdap.common.model.DsData;
import org.restfulwhois.rdap.common.model.KeyData;
import org.restfulwhois.rdap.common.model.SecureDns;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * secureDns query DAO select secureDns from database for domain.
 * query keyData and dsData as inner objects by using autowired dao。
 * 
 * @author jiashuo
 * 
 */
@Repository
public class SecureDnsQueryDaoImpl extends AbstractQueryDao<SecureDns> {
    
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(SecureDnsQueryDaoImpl.class);   
    
    /**
     * keyDataQueryDao.
     */
    @Autowired
    @Qualifier("keyDataQueryDaoImpl")
    private QueryDao<KeyData> keyDataQueryDao;
    /**
     * dsDataQueryDao.
     */
    @Autowired
    @Qualifier("dsDataQueryDaoImpl")
    private QueryDao<DsData> dsDataQueryDao;
    
    /**
     * query results of SecureDns list to an associated object.
     *   ie. domain to SecureDns,
     *       use queryAsInnerObjects(domainId) to query variants
     * @param outerObjectId
     *            associated object id.
     * @param outerModelType
     *            associated object type.            
     * @return List<SecureDns>
     *            SecureDns associated to the object.
     *   
     */
    @Override
    public List<SecureDns> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        LOGGER.debug("queryAsInnerObjects, outerObjectId:{},"
                + " outerModelType:{}", outerObjectId, outerModelType);
        List<SecureDns> result =
                queryWithoutInnerObjects(outerObjectId, outerModelType);
        queryAndSetInnerObjects(result);
        LOGGER.debug("queryAsInnerObjects, result:{}", result);
        return result;
    }

    /**
     * query and set inner objects for SecureDNS list.
     * 
     * @param secureDnsList
     *            secureDnsList.
     */
    private void queryAndSetInnerObjects(List<SecureDns> secureDnsList) {
        if (null == secureDnsList || secureDnsList.size() == 0) {
            return;
        }
        for (SecureDns secureDns : secureDnsList) {
            queryAndSetInnerObjects(secureDns);
        }
    }

    /**
     * query and set inner objects for SecureDNS.
     * 
     * @param secureDns
     *            secureDns.
     */
    private void queryAndSetInnerObjects(SecureDns secureDns) {
        if (null == secureDns) {
            return;
        }
        Long secureDnsId = secureDns.getId();
        List<DsData> dsDataList = dsDataQueryDao.queryAsInnerObjects(
                secureDnsId, ModelType.SECUREDNS);
        secureDns.setDsData(dsDataList);
        List<KeyData> keyDataList = keyDataQueryDao.queryAsInnerObjects(
                secureDnsId, ModelType.SECUREDNS);
        secureDns.setKeyData(keyDataList);
    }

    /**
     * <pre>
     * query SecureDNS from RDAP_SECUREDNS domain.
     * every domain has 0 or 1 secureDns, so query first one.
     * </pre>
     * 
     * @param outerObjectId
     *            object id of outer object.
     * @param type
     *            object model type of outer object.
     * @return SecureDNS list
     */
    private List<SecureDns> queryWithoutInnerObjects(
            final Long outerObjectId, final ModelType type) {
        final String sql = "select * from RDAP_SECUREDNS where "
                + " DOMAIN_ID=? ";
        List<SecureDns> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        return ps;
                    }
                }, new VariantsResultSetExtractor());
        return result;
    }

    /**
     * SecureDNS ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class VariantsResultSetExtractor implements
            ResultSetExtractor<List<SecureDns>> {
        @Override
        public List<SecureDns> extractData(ResultSet rs) throws SQLException {
            List<SecureDns> result = new ArrayList<SecureDns>();
            while (rs.next()) {
                SecureDns secureDns = new SecureDns();
                secureDns.setId(rs.getLong("SECUREDNS_ID"));
                secureDns.setZoneSigned(rs.getBoolean("ZONE_SIGNED"));
                secureDns.setDelegationSigned(rs
                        .getBoolean("DELEGATION_SIGNED"));
                Integer maxSigLifeIntVale = getIntegerFromRs(rs, "MAX_SIGLIFE");
                secureDns.setMaxSigLife(maxSigLifeIntVale);
                result.add(secureDns);
            }
            return result;
        }

    }
}
