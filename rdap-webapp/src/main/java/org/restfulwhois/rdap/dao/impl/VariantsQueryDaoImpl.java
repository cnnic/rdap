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
package org.restfulwhois.rdap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.core.common.model.RelDomainVariant;
import org.restfulwhois.rdap.core.common.model.Variant;
import org.restfulwhois.rdap.core.common.model.Variants;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.dao.AbstractQueryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

/**
 * variant query DAO mainly select variants from RDAP_VARIANT
 * according to the related domain id in REL_DOMAIN_VARIANT.
 * <p>
 * queryAsInnerObjects method overrite the counterpart in abstractQueryDao.
 * ResultSetExtractor, createPreparedStatement implement from jdbc.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class VariantsQueryDaoImpl extends AbstractQueryDao<Variants> {

    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(VariantsQueryDaoImpl.class);
    /**
     * query results of Variants list to an associated object.
     *   ie. domain to variants,
     *       use queryAsInnerObjects(domainId) to query variants
     * @param outerObjectId
     *            associated object id.
     * @param outerModelType
     *            associated object type.            
     * @return List<Variants>
     *            variants associated to the domain.
     *   
     */
    @Override
    public List<Variants> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        LOGGER.debug("queryAsInnerObjects, outerObjectId:{}, outerModelType:{}",
                outerObjectId, outerModelType);
        if (!ModelType.DOMAIN.equals(outerModelType)) {
            LOGGER.debug("queryAsInnerObjects, type is not DOMAIN.");
            return null;
        }
        List<Variant> variantList = queryWithoutInnerObjects(outerObjectId);
        List<Variants> result = geneVariantsList(variantList);
        LOGGER.debug("queryAsInnerObjects, result:{}", result);
        return result;
    }

    /**
     * get Variants list from Variant list,by grouping relation and idntable.
     * 
     * @param variantList
     *            variant List
     * @return variants List
     */
    private List<Variants> geneVariantsList(List<Variant> variantList) {
        List<Variants> result = new ArrayList<Variants>();
        for (Variant variant : variantList) {
            addVariantToVariantsList(variant, result);
        }
        return result;
    }

    /**
     * add variant to variants list.
     * 
     * @param variant
     *            variant will be added.
     * @param variantsList
     *            variants list.
     */
    private void addVariantToVariantsList(Variant variant,
            List<Variants> variantsList) {
        if (null == variant) {
            return;
        }
        boolean added = false;
        for (Variants variants : variantsList) {
            if (variants.equalsForRelationAndIdnTable(variant)) {
                added = true;
                variants.addVariant(variant);
                break;
            }
        }
        if (!added) {
            Variants variants = new Variants();
            variants.setIdnTable(variant.getIdnTable());
            variants.setRelation(variant.getSortedRelationStringList());
            variants.addVariant(variant);
            variantsList.add(variants);
        }
    }

    /**
     * query variant from RDAP_VARIANT.
     * 
     * @param outerObjectId
     *            object id of outer object.
     * @return variant list.
     */
    private List<Variant> queryWithoutInnerObjects(final Long outerObjectId) {
        final String sql = "select * from REL_DOMAIN_VARIANT rel, RDAP_VARIANT "
                + " variant where rel.DOMAIN_ID=? and rel.VARIANT_ID"
                + "=variant.VARIANT_ID ";
        List<Variant> result = jdbcTemplate.query(
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
     * variant ResultSetExtractor, extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class VariantsResultSetExtractor implements
            ResultSetExtractor<List<Variant>> {
        @Override
        public List<Variant> extractData(ResultSet rs) throws SQLException {
            List<Variant> result = new ArrayList<Variant>();
            while (rs.next()) {
                Long variantId = rs.getLong("VARIANT_ID");
                Variant variant = findVariantFromList(variantId, result);
                if (null == variant) {
                    variant = new Variant();
                    result.add(variant);
                    variant.setId(variantId);
                    variant.setLdhName(rs.getString("LDH_NAME"));
                    variant.setUnicodeName(rs.getString("UNICODE_NAME"));
                    variant.setIdnTable(rs.getString("IDNTABLE"));
                }
                RelDomainVariant relation = new RelDomainVariant();
                relation.setVariantType(rs.getString("VARIANT_TYPE"));
                variant.addRelation(relation);
            }
            return result;
        }
    }

    /**
     * find variant from list.
     * 
     * @param variantId
     *            variant id.
     * @param variantList
     *            variant list.
     * @return variant if exist,return null if not.
     */
    private Variant findVariantFromList(Long variantId,
            List<Variant> variantList) {
        if (null == variantId) {
            return null;
        }
        for (Variant variant : variantList) {
            if (variantId.equals(variant.getId())) {
                return variant;
            }
        }
        return null;
    }
}
