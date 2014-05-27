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
package cn.cnnic.rdap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.RelDomainVariant;
import cn.cnnic.rdap.bean.Variant;
import cn.cnnic.rdap.bean.Variants;
import cn.cnnic.rdap.dao.AbstractQueryDao;

/**
 * variant query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class VariantsQueryDaoImpl extends AbstractQueryDao<Variants> {

    @Override
    public List<Variants> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        List<Variant> variantList = queryWithoutInnerObjects(outerObjectId);
        List<Variants> result = geneVariantsList(variantList);
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
     *            variant.
     * @param variantsList
     *            variantsList.
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
     * query variant.
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
        public List<Variant> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
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
     *            variantId.
     * @param variantList
     *            variantList.
     * @return variant if exist,null if not.
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
