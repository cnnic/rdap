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

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;
import org.restfulwhois.rdap.common.model.IPAddress;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;



/**
 * @author zhanyq
 * 
 */
@Repository
public class IPAddressUpdateDaoImpl extends 
               AbstractUpdateDao<IPAddress, IpAddressDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(IPAddressUpdateDaoImpl.class);   

    @Override
    public IPAddress save(IPAddress model) {
        return null;
    }

    @Override
    public void update(IPAddress model) {
    }

    @Override
    public void delete(IPAddress model) {
    }  
    
    
    @Override
    public void saveAsInnerObjects(BaseModel outerModel, 
             List<IpAddressDto> models) {
       if (null == models || models.size() == 0) {
            return;
       }
       for (IpAddressDto ipAdressDto: models) {
            createNameserverIp(ipAdressDto, outerModel.getId());
       }
    }
    
    @Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<IpAddressDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }
    
    @Override
    public void deleteAsInnerObjects(BaseModel outerModel) {
        if (null == outerModel) {
              return;
        }           
       //delete Nameserver Ip
        super.delete(String.valueOf(outerModel.getId()), 
                        "RDAP_NAMESERVER_IP", "NAMESERVER_ID");
         
        
    }
    /**
     * 
     * @param ipAdressDto
     *        ipAdressDto
     * @param outerModelId
     *        object id  of outer model
     */
    private void createNameserverIp(final IpAddressDto ipAdressDto,
             final Long outerModelId) {
       List<String> ips = ipAdressDto.getIpList();    
       final List<String> notEmptyIps = 
               StringUtil.getNotEmptyStringList(ips);
       if (notEmptyIps.isEmpty()) {
             return;
        }
       final String sql = "insert into RDAP_NAMESERVER_IP (NAMESERVER_ID,"
                           +  "IP, VERSION) values (?,?,?)";    
       jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
           public int getBatchSize() {
               return notEmptyIps.size();
           }
           @Override
           public void setValues(PreparedStatement ps, int i)
              throws SQLException {
              IpVersion ipVersion = IpUtil.getIpVersionOfIp(notEmptyIps.get(i));
              byte[] bytes = IpUtil.ipToByteArray(notEmptyIps.get(i), ipVersion);
                ps.setLong(1, outerModelId); 
                ps.setBytes(2, bytes);
                ps.setString(3, ipVersion.getName());
           }
         });
    }


    @Override
    public Long findIdByHandle(String handle) {
        return super.findIdByHandle(handle, "NAMESERVER_ID", "RDAP_NAMESERVER");
    }
}
