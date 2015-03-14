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
package org.restfulwhois.rdap.core.nameserver.service.impl;

import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_LDHNAME;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_UNICODENAME;

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;
import org.restfulwhois.rdap.common.model.IPAddress;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.service.AbstractUpdateService;
import org.restfulwhois.rdap.common.util.BeanUtil;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * create service implementation.
 * 
 * @author jiashuo
 * 
 */
public abstract class NameserverUpdateBaseServiceImpl extends
        AbstractUpdateService<NameserverDto, Nameserver> {
   /**
     * ipAddressDto.
     */
    @Autowired
    protected UpdateDao<IPAddress, IpAddressDto> ipAddressDao;
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NameserverUpdateBaseServiceImpl.class);
    /**
     * @param dto nameserverDto.
     * @return nameserver.
     */
    protected Nameserver convertDtoToModel(NameserverDto dto) {
        Nameserver nameserver = convertDtoToNameserver(dto);
        super.convertCustomProperties(dto, nameserver);
        return nameserver;
    }
    /**
     * 
     * @param nameserver nameserver.
     */
    protected void saveIpAddresses(Nameserver nameserver) {
        LOGGER.debug("save ipAddresses ...");
        NameserverDto dto = (NameserverDto) nameserver.getDto();
        IpAddressDto ipAddressDto = dto.getIpAddresses();
        if (null == ipAddressDto) {
            LOGGER.debug("ipAddresses is empty.");
            return;
        }
        List<IpAddressDto> ipAddressList = new ArrayList<IpAddressDto>();
        ipAddressList.add(ipAddressDto);
        ipAddressDao.saveAsInnerObjects(nameserver, ipAddressList);
    }
    /**
     * 
     * @param nameserver nameserver.
     */
    protected void deleteIpAddresses(Nameserver nameserver) {
        LOGGER.debug("delete ipAddresses ...");
        ipAddressDao.deleteAsInnerObjects(nameserver);
    }
    /**
     * 
     * @param nameserver nameserver.
     */
    protected void updateIpAddresses(Nameserver nameserver) {
        deleteIpAddresses(nameserver);
        saveIpAddresses(nameserver);
    }
    /**
     * 
     * @param dto  nameserverDto.
     * @return nameserver.
     */
    private Nameserver convertDtoToNameserver(NameserverDto dto) {
        Nameserver nameserver = new Nameserver();
        BeanUtil.copyProperties(dto, nameserver, "entities", "events",
                "remarks", "links");
        return nameserver;
    }
    /**
     * 
     * @param dto namerserverDto.
     * @param validationResult validationResult.
     * @return validationResult.
     */
    protected ValidationResult validateForSaveAndUpdate(NameserverDto dto,
            ValidationResult validationResult) {
        checkNotEmptyAndMaxLength(dto.getLdhName(), MAX_LENGTH_LDHNAME,
                "ldhName", validationResult);
        checkNotEmptyAndMaxLengthForHandle(dto.getHandle(), validationResult);
        checkMaxLength(dto.getUnicodeName(), MAX_LENGTH_UNICODENAME,
                "unicodeName", validationResult);
        checkIpAddress(dto, validationResult);
        validateBaseDto(dto, validationResult);
        return validationResult;
    }
    /**
     * 
     * @param dto nameserverDto.
     * @param validationResult validationResult.
     */
    private void checkIpAddress(NameserverDto dto,
            ValidationResult validationResult) {
        IpAddressDto ipAddressDto = dto.getIpAddresses();
        if (null == ipAddressDto) {
            return;
        }
        checkIpList(ipAddressDto.getIpList(), "ipAddresses", validationResult);
    }
    /**
     * 
     * @param ipList ipList.
     * @param fieldName fieldName.
     * @param validationResult validationResult.
     */
    private void checkIpList(List<String> ipList, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        if (null != ipList) {
            for (String ip : ipList) {
                checkIp(ip, fieldName, validationResult);
            }
        }
    }

}
