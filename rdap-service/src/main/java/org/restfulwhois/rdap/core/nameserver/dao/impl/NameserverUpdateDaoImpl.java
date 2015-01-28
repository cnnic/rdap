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
package org.restfulwhois.rdap.core.nameserver.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.embedded.HandleDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;



/**
 * @author zhanyq
 * 
 */
@Repository
public class NameserverUpdateDaoImpl extends 
               AbstractUpdateDao<Nameserver, NameserverDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(NameserverUpdateDaoImpl.class);
    private static final String SQL_SAVE_NAMESERVER =
            "INSERT INTO RDAP_NAMESERVER"
                    + " (HANDLE,LDH_NAME,UNICODE_NAME,PORT43,LANG,CUSTOM_PROPERTIES)"
                    + " values(?,?,?,?,?,?)";
    private static final String SQL_UPDATE_NAMESERVER = "UPDATE RDAP_NAMESERVER"
            + " SET LDH_NAME=?,UNICODE_NAME=?,PORT43=?,LANG=?"
            + " ,CUSTOM_PROPERTIES=? where NAMESERVER_ID=?";
    private static final String SQL_DELETE_NAMESERVER =
            "DELETE FROM RDAP_NAMESERVER where NAMESERVER_ID=?";

    @Override
    public Nameserver save(final Nameserver model) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps =
                        connection.prepareStatement(SQL_SAVE_NAMESERVER,
                                Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, model.getHandle());
                ps.setString(2, model.getLdhName());
                ps.setString(3, model.getUnicodeName());
                ps.setString(4, model.getPort43());
                ps.setString(5, model.getLang());               
                ps.setString(6, model.getCustomPropertiesJsonVal());
                return ps;
            }
        }, keyHolder);
        model.setId(keyHolder.getKey().longValue());
        return model;
    
    }
    
    @Override
    public void saveStatus(Nameserver model) {
        saveStatus(model, model.getStatus(), "RDAP_NAMESERVER_STATUS", 
                   "NAMESERVER_ID");
    }
    @Override
    public void update(final Nameserver model) {
        jdbcTemplate.update(SQL_UPDATE_NAMESERVER, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setString(1, model.getLdhName());
                ps.setString(2, model.getUnicodeName());
                ps.setString(3, model.getPort43());
                ps.setString(4, model.getLang());               
                ps.setString(5, model.getCustomPropertiesJsonVal());
                ps.setLong(6, model.getId());
            }
        });
    }
    
    @Override
    public void updateStatus(Nameserver nameserver) {
        deleteStatus(nameserver);
        saveStatus(nameserver);
    }
    @Override
    public void delete(final Nameserver model) {
        jdbcTemplate.update(SQL_DELETE_NAMESERVER, new PreparedStatementSetter() {
            public void setValues(PreparedStatement ps) throws SQLException {
                ps.setLong(1, model.getId());
            }
        });
    }
    
    @Override
    public void deleteStatus(Nameserver model) {
        deleteStatus(model, "RDAP_NAMESERVER_STATUS", "NAMESERVER_ID");
    }
    @Override
    public void saveRel(BaseModel outerModel) {
        if (null == outerModel || null == outerModel.getDto()) {
               return;
        }
        BaseDto dto = outerModel.getDto();
        if (!(dto instanceof DomainDto)) {
               return;
        }
        DomainDto domainDto = (DomainDto) dto;
        List<HandleDto> handles = domainDto.getNameservers();
        if (null == handles  || handles.size() == 0) {
             return;
        }
        for (HandleDto handleDto:handles) {
            Long nsId = this.findIdByHandle(handleDto.getHandle());
            if (null != nsId) {
                createRelDomainNameserver(outerModel.getId(), nsId);
            }
        }       
    }
       
    @Override
    public void deleteRel(BaseModel outerModel) {
         if (null == outerModel || null == outerModel.getId()) {
             return;
         }              
         super.delete(String.valueOf(outerModel.getId()),
                            "REL_DOMAIN_NAMESERVER", "DOMAIN_ID");
    }
       
    @Override
    public void updateRel(BaseModel outerModel) {
        if (null == outerModel || null == outerModel.getId()
                    || null == outerModel.getDto()) {
               return;
        }
        deleteRel(outerModel);
        saveRel(outerModel);
    }
    /**
     * 
     * @param outerModelId 
     *       object id of outer object  
     * @param nsId
     *        ns id
     */
    private void createRelDomainNameserver(final Long outerModelId,
                  final Long nsId) {                  
           final String sql = "insert into REL_DOMAIN_NAMESERVER (DOMAIN_ID,"
                           +  "NAMESERVER_ID) values (?,?)";           
           jdbcTemplate.update(new PreparedStatementCreator() {
                  public PreparedStatement createPreparedStatement(
                                Connection connection) throws SQLException {
                   PreparedStatement ps = connection.prepareStatement(
                                 sql);
                   ps.setLong(1, outerModelId);                  
                   ps.setLong(2, nsId);
                            return ps;
                  }                            
          });              
     }   

    @Override
    public Long findIdByHandle(String handle) {
        return super.findIdByHandle(handle, "NAMESERVER_ID", "RDAP_NAMESERVER");
    }
}
