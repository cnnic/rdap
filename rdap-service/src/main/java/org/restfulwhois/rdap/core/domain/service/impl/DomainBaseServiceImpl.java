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

import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_255;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_32;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_HANDLE;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_LDHNAME;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_UNICODENAME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.dto.embedded.KeyDataDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantNameDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.KeyData;
import org.restfulwhois.rdap.common.model.SecureDns;
import org.restfulwhois.rdap.common.model.Variant;
import org.restfulwhois.rdap.common.model.Variants;
import org.restfulwhois.rdap.common.service.AbstractUpdateService;
import org.restfulwhois.rdap.common.util.BeanUtil;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create service implementation.
 * 
 * @author jiashuo
 * 
 */
public abstract class DomainBaseServiceImpl extends
        AbstractUpdateService<DomainDto, Domain> {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainBaseServiceImpl.class);

    protected Domain convertDtoToModelWithoutType(DomainDto dto) {
        Domain domain = convertDtoToDomain(dto);
        super.convertCustomProperties(dto, domain);
        convertNetworkIdIfExist(dto, domain);
        return domain;
    }

    private void convertSecureDns(DomainDto dto, Domain domain) {
        SecureDnsDto secureDnsDto = dto.getSecureDNS();
        if (null == secureDnsDto) {
            return;
        }
        SecureDns secureDns = new SecureDns();
        BeanUtil.copyProperties(secureDnsDto, secureDns, "keyData", "dsData");
        List<KeyDataDto> keyDataDtoList = secureDnsDto.getKeyData();
        if (null == keyDataDtoList) {
            return;
        }
        for (KeyDataDto keyDataDto : keyDataDtoList) {
            KeyData keyData = new KeyData();
            BeanUtil.copyProperties(keyDataDto, keyData, "events");
        }
    }

    private void convertVariants(DomainDto dto, Domain domain) {
        List<VariantDto> variantDtos = dto.getVariants();
        if (null == variantDtos) {
            return;
        }
        List<Variants> variantsList = new ArrayList<Variants>();
        domain.setVariants(variantsList);
        for (VariantDto variantDto : variantDtos) {
            Variants variants = new Variants();
            variantsList.add(variants);
            variants.setRelation(variantDto.getRelation());
            variants.setIdnTable(variantDto.getIdnTable());
            List<VariantNameDto> variantNames = variantDto.getVariantNames();
            List<Variant> variantList = new ArrayList<Variant>();
            variants.setVariantNames(variantList);
            for (VariantNameDto variantNameDto : variantNames) {
                Variant variant = new Variant();
                variantList.add(variant);
                variant.setLdhName(variantNameDto.getLdhName());
                variant.setUnicodeName(variantNameDto.getUnicodeName());
            }
        }
    }

    private Domain convertDtoToDomain(DomainDto dto) {
        Domain domain = new Domain();
        BeanUtil.copyProperties(dto, domain, "variants", "nameservers",
                "entities", "publicIds", "remarks", "links");
        return domain;
    }

    /**
     * set network id to domain.
     * 
     * @param dto
     *            DTO.
     * @param domain
     *            domain.
     */
    private void convertNetworkIdIfExist(DomainDto dto, Domain domain) {
        if (StringUtils.isBlank(dto.getNetworkHandle())) {
            return;
        }
        Long networkId = dao.findIdByHandle(dto.getNetworkHandle());
        if (null != networkId) {
            domain.setNetworkId(networkId);
        }
    }

    protected ValidationResult validateWithoutType(DomainDto domainDto) {
        ValidationResult validationResult = new ValidationResult();
        checkNotEmptyAndMaxLength(domainDto.getLdhName(), MAX_LENGTH_LDHNAME,
                "ldhName", validationResult);
        checkNotEmptyAndMaxLengthForHandle(domainDto.getHandle(),
                validationResult);
        checkMaxLength(domainDto.getUnicodeName(), MAX_LENGTH_UNICODENAME,
                "unicodeName", validationResult);
        checkMaxLengthForPort43(domainDto.getPort43(), validationResult);
        checkMaxLengthForLang(domainDto.getLang(), validationResult);
        List<String> statusList = domainDto.getStatus();
        for (String status : statusList) {
            checkMaxLengthForStatus(status, validationResult);
        }
        checkVariants(domainDto, validationResult);
        checkSecureDns(domainDto, validationResult);
        checkEntities(domainDto.getEntities(), validationResult);
        checkPublicIds(domainDto.getPublicIds(), validationResult);
        checkRemarks(domainDto.getRemarks(), validationResult);
        checkLinks(domainDto.getLinks(), validationResult);
        checkEvents(domainDto.getEvents(), validationResult);
        return validationResult;
    }

    private void checkEntities(List<EntityHandleDto> entities,
            ValidationResult validationResult) {
        if (null == entities) {
            return;
        }
        for (EntityHandleDto entityHandle : entities) {
            List<String> roles = entityHandle.getRoles();
            for (String role : roles) {
                checkNotEmptyAndMaxLength(role, MAX_LENGTH_32, "entity.role",
                        validationResult);
            }
        }
    }

    private void checkSecureDns(DomainDto domainDto,
            ValidationResult validationResult) {
        SecureDnsDto secureDns = domainDto.getSecureDNS();
        if (null == secureDns) {
            return;
        }
        checkMinMaxInt(secureDns.getMaxSigLife(),
                UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN,
                "secureDns.maxSigLife", validationResult);
        checkDsData(secureDns, validationResult);
        checkKeyData(secureDns, validationResult);
    }

    private void checkKeyData(SecureDnsDto secureDns,
            ValidationResult validationResult) {
        List<KeyDataDto> keyDatas = secureDns.getKeyData();
        if (null == keyDatas) {
            return;
        }
        for (KeyDataDto keyData : keyDatas) {
            checkMinMaxInt(keyData.getFlags(),
                    UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                    UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN, "keyData.flags",
                    validationResult);
            checkMinMaxInt(keyData.getProtocol(),
                    UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                    UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN,
                    "keyData.protocol", validationResult);
            checkNotEmptyAndMaxLength(keyData.getPublicKey(),
                    UpdateValidateUtil.MAX_LENGTH_2048, "keyData.publicKey",
                    validationResult);
            checkMinMaxInt(keyData.getAlgorithm(),
                    UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                    UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN,
                    "keyData.algorithm", validationResult);
            checkEvents(keyData.getEvents(), validationResult);
        }
    }

    private void checkDsData(SecureDnsDto secureDns,
            ValidationResult validationResult) {
        List<DsDataDto> dsDatas = secureDns.getDsData();
        if (null == dsDatas) {
            return;
        }
        for (DsDataDto dsData : dsDatas) {
            checkMinMaxInt(dsData.getKeyTag(),
                    UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                    UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN,
                    "secureDns.maxSigLife", validationResult);
            checkEvents(dsData.getEvents(), validationResult);

        }
    }

    private void checkVariants(DomainDto domainDto,
            ValidationResult validationResult) {
        List<VariantDto> variants = domainDto.getVariants();
        if (null == variants) {
            return;
        }
        for (VariantDto variant : variants) {
            checkMaxLength(variant.getIdnTable(), MAX_LENGTH_255,
                    "variant.idnTable", validationResult);
            List<String> relations = variant.getRelation();
            for (String relation : relations) {
                checkMaxLength(relation, MAX_LENGTH_32, "variant.relation",
                        validationResult);
            }
            List<VariantNameDto> variantNames = variant.getVariantNames();
            for (VariantNameDto variantName : variantNames) {
                checkNotEmptyAndMaxLength(variantName.getLdhName(),
                        MAX_LENGTH_HANDLE, "variant.ldhName", validationResult);
                checkNotEmptyAndMaxLength(variantName.getUnicodeName(),
                        MAX_LENGTH_UNICODENAME, "variant.unicodeName",
                        validationResult);
            }
        }
    }
}
