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
package org.restfulwhois.rdap.core.ip.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.model.Network;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

/**
 * @author jiashuo
 * 
 */
@Repository
public class NetworkUpdateDaoImpl extends AbstractUpdateDao<Network, IpDto> {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(NetworkUpdateDaoImpl.class);
    
    /**
     * save sql.
     */
    private static final String SQL_SAVE_IP =
            "INSERT INTO RDAP_IP"
                  + " (HANDLE,ENDADDRESS,STARTADDRESS,VERSION,NAME,TYPE,"
                  + " COUNTRY,PARENT_HANDLE,PORT43,LANG,CIDR,CUSTOM_PROPERTIES)"
                  + " values(?,?,?,?,?,?,?,?,?,?,?,?)";
    /**
     * update sql.
     */
    private static final String SQL_UPDATE_IP = "UPDATE RDAP_IP"
            + " SET ENDADDRESS=?,STARTADDRESS=?,VERSION=?,NAME=?,TYPE=?,"
            + " COUNTRY=?,PARENT_HANDLE=?,PORT43=?,LANG=?,"
            + " CIDR=?,CUSTOM_PROPERTIES=? where IP_ID=?";
   /**
    * delete sql.
    */
    private static final String SQL_DELETE_IP =
            "DELETE FROM RDAP_IP where IP_ID=?";

    @Override
    public Network save(final Network model) {
         KeyHolder keyHolder = new GeneratedKeyHolder();
         jdbcTemplate.update(new PreparedStatementCreator() {
             public PreparedStatement createPreparedStatement(
                     Connection connection) throws SQLException {
                 PreparedStatement ps =
                         connection.prepareStatement(SQL_SAVE_IP,
                                 Statement.RETURN_GENERATED_KEYS);
                 ps.setString(1, model.getHandle());
                 ps.setBytes(2, IpUtil.ipToByteArray(model.getEndAddress()));
                 ps.setBytes(3, IpUtil.ipToByteArray(model.getStartAddress()));
                 ps.setString(4, model.getIpVersion().getName());
                 ps.setString(5, model.getName());
                 ps.setString(6, model.getType());
                 ps.setString(7, model.getCountry());
                 ps.setString(8, model.getParentHandle());
                 ps.setString(9, model.getPort43());
                 ps.setString(10, model.getLang());
                 ps.setString(11, model.getCidr());
                 ps.setString(12, model.getCustomPropertiesJsonVal());
                 return ps;
             }
         }, keyHolder);
         model.setId(keyHolder.getKey().longValue());
         return model;
     
    }
    
    @Override
    public void saveStatus(Network model) {
        saveStatus(model, model.getStatus(), "RDAP_IP_STATUS", 
                   "IP_ID");
    }
    
    @Override
    public void update(final Network model) {
        jdbcTemplate.update(SQL_UPDATE_IP, new PreparedStatementSetter() {
           public void setValues(PreparedStatement ps) throws SQLException {
                ps.setBytes(1, IpUtil.ipToByteArray(model.getEndAddress()));
                ps.setBytes(2, IpUtil.ipToByteArray(model.getStartAddress()));
                ps.setString(3, model.getIpVersion().getName());
                ps.setString(4, model.getName());
                ps.setString(5, model.getType());
                ps.setString(6, model.getCountry());
                ps.setString(7, model.getParentHandle());
                ps.setString(8, model.getPort43());
                ps.setString(9, model.getLang());
                ps.setString(10, model.getCidr());
                ps.setString(11, model.getCustomPropertiesJsonVal());
                ps.setLong(12, model.getId());
            }
        });

    }
    @Override
    public void updateStatus(Network model) {
        deleteStatus(model);
        saveStatus(model);
    }
    @Override
    public void delete(final Network model) {
       jdbcTemplate.update(SQL_DELETE_IP, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, model.getId());
            }
        });

    }
    @Override
    public void deleteStatus(Network model) {
        deleteStatus(model, "RDAP_IP_STATUS", "IP_ID");
    }
    @Override
    public Long findIdByHandle(String handle) {
        return super.findIdByHandle(handle, "IP_ID", "RDAP_IP");
    }

}
