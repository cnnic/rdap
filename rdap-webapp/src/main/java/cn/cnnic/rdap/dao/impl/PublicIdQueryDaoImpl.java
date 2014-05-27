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
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.PublicId;
import cn.cnnic.rdap.dao.AbstractQueryDao;

/**
 * publicId query DAO.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class PublicIdQueryDaoImpl extends AbstractQueryDao<PublicId> {

    @Override
    public List<PublicId> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        List<PublicId> publicIds = queryWithoutInnerObjects(outerObjectId,
                outerModelType);
        return publicIds;
    }

    /**
     * query variant.
     * 
     * @param outerObjectId
     *            object id of outer object.
     * @param outerModelType
     *            model type.
     * @return variant list
     */
    private List<PublicId> queryWithoutInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        final String sql = "select * from REL_PUBLICID_REGISTRATION rel,"
                + " RDAP_PUBLICID publicId "
                + " where rel.PUBLIC_ID=publicId.PUBLIC_ID "
                + " and rel.REL_ID=? and rel.REL_OBJECT_TYPE=? ";
        List<PublicId> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        ps.setString(2, outerModelType.getName());
                        return ps;
                    }
                }, new RowMapper<PublicId>() {
                    @Override
                    public PublicId mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        PublicId publicId = new PublicId();
                        publicId.setIdentifier(rs.getString("IDENTIFIER"));
                        publicId.setType(rs.getString("TYPE"));
                        return publicId;
                    }
                });
        return result;
    }
}
