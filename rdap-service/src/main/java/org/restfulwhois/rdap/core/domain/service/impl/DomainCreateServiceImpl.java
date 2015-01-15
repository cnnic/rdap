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
package org.restfulwhois.rdap.core.domain.service.impl;

import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_HANDLE;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_LANG;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_LDHNAME;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_PORT43;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_UNICODENAME;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Domain.DomainType;
import org.restfulwhois.rdap.common.service.AbstractUpdateService;
import org.restfulwhois.rdap.common.validation.UpdateValidationError;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * create service implementation.
 * 
 * @author jiashuo
 * 
 */
@Service("domainCreateServiceImpl")
public class DomainCreateServiceImpl extends
        AbstractUpdateService<DomainDto, Domain> {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainCreateServiceImpl.class);

    @Override
    protected void execute(Domain domain) {
        dao.create(domain);
    }

    @Override
    protected Domain convertDtoToModel(DomainDto dto) {
        Domain domain = new Domain();
        domain.setType(DomainType.getByTypeName(dto.getType()));
        domain.setHandle(dto.getHandle());
        domain.setLdhName(dto.getLdhName());
        domain.setUnicodeName(dto.getUnicodeName());
        domain.setStatus(dto.getStatus());
        domain.setPort43(dto.getPort43());
        domain.setLang(dto.getLang());
        setNetworkHandle(dto, domain);
        super.convertCustomProperties(dto, domain);
        return domain;
    }

    /**
     * set network handle to domain.
     * 
     * @param dto
     *            dto.
     * @param domain
     *            domain.
     */
    private void setNetworkHandle(DomainDto dto, Domain domain) {
        if (StringUtils.isBlank(dto.getNetworkHandle())) {
            return;
        }
        Long networkId = dao.findIdByHandle(dto.getNetworkHandle());
        if (null != networkId) {
            domain.setNetworkId(networkId);
        }
    }

    @Override
    protected ValidationResult validate(DomainDto domainDto) {
        ValidationResult validationResult = new ValidationResult();
        checkNotEmpty(domainDto.getType(), "type", validationResult);
        checkDomainTypeValid(domainDto.getType(), "type", validationResult);
        checkNotEmptyAndMaxLength(domainDto.getLdhName(), MAX_LENGTH_LDHNAME,
                "ldhName", validationResult);
        checkNotEmptyAndMaxLength(domainDto.getHandle(), MAX_LENGTH_HANDLE,
                "handle", validationResult);
        checkMaxLength(domainDto.getUnicodeName(), MAX_LENGTH_UNICODENAME,
                "unicodeName", validationResult);
        checkMaxLength(domainDto.getPort43(), MAX_LENGTH_PORT43, "port43",
                validationResult);
        checkMaxLength(domainDto.getLang(), MAX_LENGTH_LANG, "lang",
                validationResult);
        checkHandleExistForCreate(domainDto.getHandle(), validationResult);
        return validationResult;
    }

    /**
     * 
     * @param typeStr
     * @param fieldName
     * @param validationResult
     */
    private void checkDomainTypeValid(String typeStr, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        DomainType domainType = null;
        DomainType[] types = DomainType.values();
        for (DomainType type : types) {
            if (type.getName().equals(typeStr)) {
                domainType = type;
            }
        }
        if (null == domainType) {
            validationResult.addError(UpdateValidationError
                    .build4008Error(fieldName));
        }
    }

}
