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
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.embedded.HandleDto;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
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

    @Override
    public Nameserver save(Nameserver model) {
         return null;
    }

    @Override
    public void update(Nameserver model) {
    }

    @Override
    public void delete(Nameserver model) {
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
