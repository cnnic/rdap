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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Principal;
import cn.cnnic.rdap.bean.SecureObject;
import cn.cnnic.rdap.dao.AclDao;

/**
 * AclDao implementation.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class AclDaoIml implements AclDao {
    /**
     * JDBC template simplifies the use of JDBC and helps to avoid common
     * errors.
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean hasEntry(Principal principal, SecureObject secureObject) {
        if (isPrincipalHasEntry(principal, secureObject)) {
            return true;
        }
        return !isObjectIsSecure(secureObject);
    }

    /**
     * check if object is secure - has any acl entry.
     * 
     * @param secureObject
     *            secureObject.
     * @return true if secure,false if not.
     */
    private boolean isObjectIsSecure(final SecureObject secureObject) {
        final String sql = "select count(1) as COUNT from "
                + " RDAP_IDENTITY_ACL acl "
                + " where acl.OBJECT_TYPE = ? and acl.OBJECT_ID = ? ";
        Long count = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, secureObject.getType());
                ps.setLong(2, secureObject.getId());
                return ps;
            }
        }, new CountResultSetExtractor());
        return count > 0;
    }

    /**
     * check if prinicipal has acl entry for secureObject.
     * 
     * @param principal
     *            principal.
     * @param secureObject
     *            secureObject.
     * @return true if has entry,false if not.
     */
    private boolean isPrincipalHasEntry(final Principal principal,
            final SecureObject secureObject) {
        final String sql = "select count(1) as COUNT from "
                + " RDAP_IDENTITY_USER_REL_ROLE userRole,"
                + " RDAP_IDENTITY_ACL acl "
                + " where userRole.ROLE_ID = acl.ROLE_ID "
                + " and acl.OBJECT_TYPE = ? and acl.OBJECT_ID = ? "
                + " and userRole.USER_ID= ? ";
        Long count = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, secureObject.getType());
                ps.setLong(2, secureObject.getId());
                ps.setLong(3, principal.getId());
                return ps;
            }
        }, new CountResultSetExtractor());
        return count > 0;
    }

    /**
     * count ResultSet extractor.
     * 
     * @author jiashuo
     * 
     */
    class CountResultSetExtractor implements ResultSetExtractor<Long> {
        @Override
        public Long extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            Long result = 0L;
            if (rs.next()) {
                result = rs.getLong("COUNT");
            }
            return result;
        }
    }

}
