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
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_LDHNAME;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_UNICODENAME;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.KeyDataDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantNameDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Domain.DomainType;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.model.Network;
import org.restfulwhois.rdap.common.model.SecureDns;
import org.restfulwhois.rdap.common.model.Variants;
import org.restfulwhois.rdap.common.service.AbstractUpdateService;
import org.restfulwhois.rdap.common.util.BeanUtil;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.restfulwhois.rdap.common.validation.UpdateValidationError;
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
public abstract class DomainUpdateBaseServiceImpl extends
        AbstractUpdateService<DomainDto, Domain> {
    /**
     * 
     */
    @Autowired
    protected UpdateDao<Nameserver, NameserverDto> nameserverDao;
    /**
     * 
     */
    @Autowired
    private UpdateDao<SecureDns, SecureDnsDto> secureDnsUpdateDao;
    /**
     * 
     */
    @Autowired
    private UpdateDao<Variants, VariantDto> variantUpdateDao;
    /**
     * 
     */
    @Autowired
    protected UpdateDao<Network, IpDto> networkDao;
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainUpdateBaseServiceImpl.class);

    /**
     * 
     * @param dto
     *            dto.
     * @return Domain.
     */
    protected Domain convertDtoToModelWithoutType(DomainDto dto) {
        Domain domain = convertDtoToDomain(dto);
        super.convertCustomProperties(dto, domain);
        convertNetworkIdIfExist(dto, domain);
        return domain;
    }

    /**
     * 
     * @param domain domain.
     */
    protected void saveNameservers(Domain domain) {
        LOGGER.debug("save ns rel ...");
        nameserverDao.saveRel(domain);
    }

    /**
     * 
     * @param domain domain.
     */
    protected void deleteNameserversRel(Domain domain) {
        LOGGER.debug("delete ns rel ...");
        getNameserverDao().deleteRel(domain);
    }

    /**
     * 
     * @param domain domain.
     */
    protected void updateNameserversRel(Domain domain) {
        deleteNameserversRel(domain);
        saveNameservers(domain);
    }

    /**
     * 
     * @param dto dto.
     * @param domain domain.
     */
    protected void saveSecureDns(DomainDto dto, Domain domain) {
        LOGGER.debug("save secureDns...");
        SecureDnsDto secureDnsDto = dto.getSecureDNS();
        if (null == secureDnsDto) {
            LOGGER.debug("secureDns is empty.");
            return;
        }
        List<SecureDnsDto> secureDnsDtos = new ArrayList<SecureDnsDto>();
        secureDnsDtos.add(secureDnsDto);
        secureDnsUpdateDao.saveAsInnerObjects(domain, secureDnsDtos);
    }

    /**
     * 
     * @param domain domain.       
     */
    protected void deleteSecureDns(Domain domain) {
        secureDnsUpdateDao.deleteAsInnerObjects(domain);
    }
    /**
     * 
     * @param dto domainDto.
     * @param domain  domain.
     */

    protected void updateSecureDns(DomainDto dto, Domain domain) {
        deleteSecureDns(domain);
        saveSecureDns(dto, domain);
    }
    /**
     * 
     * @param dto domainDto.
     * @param domain domain.
     */
    protected void saveVariants(DomainDto dto, Domain domain) {
        LOGGER.debug("save variants...");
        List<VariantDto> variantDtos = dto.getVariants();
        if (null == variantDtos || variantDtos.isEmpty()) {
            LOGGER.debug("variants is empty.");
            return;
        }
        variantUpdateDao.saveAsInnerObjects(domain, variantDtos);
    }
    /**
     * 
     * @param domain domain.
     */
    protected void deleteVariants(Domain domain) {
        variantUpdateDao.deleteAsInnerObjects(domain);
    }
    /**
     * 
     * @param dto dto.
     * @param domain domain.
     */
    protected void updateVariants(DomainDto dto, Domain domain) {
        deleteVariants(domain);
        saveVariants(dto, domain);
    }
    /**
     * 
     * @param dto domainDto.
     * @return domain 
     */
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
        Long networkId = networkDao.findIdByHandle(dto.getNetworkHandle());
        if (null != networkId) {
            domain.setNetworkId(networkId);
        }
    }
   /**
    * 
    * @param domainDto domainDto.
    * @param validationResult validationResult.
    * @return ValidationResult
    */
    protected ValidationResult validateWithoutType(DomainDto domainDto,
            ValidationResult validationResult) {
        checkNotEmptyAndMaxLength(domainDto.getLdhName(), MAX_LENGTH_LDHNAME,
                "ldhName", validationResult);
        checkNotEmptyAndMaxLengthForHandle(domainDto.getHandle(),
                validationResult);
        checkMaxLength(domainDto.getUnicodeName(), MAX_LENGTH_UNICODENAME,
                "unicodeName", validationResult);
        checkVariants(domainDto, validationResult);
        checkSecureDns(domainDto, validationResult);
        checkPublicIds(domainDto.getPublicIds(), validationResult);
        validateBaseDto(domainDto, validationResult);
        return validationResult;
    }
    /**
     * 
     * @param domainDto domainDto.
     * @param validationResult validationResult.
     */
    private void checkSecureDns(DomainDto domainDto,
            ValidationResult validationResult) {
        SecureDnsDto secureDns = domainDto.getSecureDNS();
        if (null == secureDns) {
            return;
        }
        checkMinMaxInt(secureDns.getMaxSigLife(), "secureDns.maxSigLife",
                validationResult);
        checkDsData(secureDns, validationResult);
        checkKeyData(secureDns, validationResult);
    }
    /**
     * 
     * @param secureDns secureDns.
     * @param validationResult validationResult.
     */
    private void checkKeyData(SecureDnsDto secureDns,
            ValidationResult validationResult) {
        List<KeyDataDto> keyDatas = secureDns.getKeyData();
        if (null == keyDatas) {
            return;
        }
        for (KeyDataDto keyData : keyDatas) {
            checkNotNullAndMinMaxInt(keyData.getFlags(), "keyData.flags",
                    validationResult);
            checkNotNullAndMinMaxInt(keyData.getProtocol(), "keyData.protocol",
                    validationResult);
            checkMaxLength(keyData.getPublicKey(),
                    UpdateValidateUtil.MAX_LENGTH_2048, "keyData.publicKey",
                    validationResult);
            checkNotNullAndMinMaxInt(keyData.getAlgorithm(),
                    "keyData.algorithm", validationResult);
            checkEvents(keyData.getEvents(), validationResult);
            checkLinks(keyData.getLinks(), validationResult);
        }
    }
    /**
     * 
     * @param secureDns secureDns.
     * @param validationResult validationResult.
     */
    private void checkDsData(SecureDnsDto secureDns,
            ValidationResult validationResult) {
        List<DsDataDto> dsDatas = secureDns.getDsData();
        if (null == dsDatas) {
            return;
        }
        for (DsDataDto dsData : dsDatas) {
            checkNotNullAndMinMaxInt(dsData.getKeyTag(), "dsData.keyTag",
                    validationResult);
            checkNotNullAndMinMaxInt(dsData.getAlgorithm(), "dsData.algorithm",
                    validationResult);
            checkNotNullAndMinMaxInt(dsData.getDigestType(),
                    "dsData.digestType", validationResult);
            checkNotEmptyAndMaxLength(dsData.getDigest(),
                    UpdateValidateUtil.MAX_LENGTH_2048, "dsData.digest",
                    validationResult);
            checkEvents(dsData.getEvents(), validationResult);
            checkLinks(dsData.getLinks(), validationResult);
        }
    }
    /**
     * 
     * @param domainDto domainDto.
     * @param validationResult validationResult.
     */
    private void checkVariants(DomainDto domainDto,
            ValidationResult validationResult) {
        List<VariantDto> variants = domainDto.getVariants();
        if (null == variants) {
            return;
        }
        for (VariantDto variant : variants) {
            checkMaxLength(variant.getIdnTable(), MAX_LENGTH_255,
                    "variants.idnTable", validationResult);
            List<String> relations = variant.getRelation();
            if (null != relations) {
                for (String relation : relations) {
                    checkMaxLength(relation, MAX_LENGTH_255,
                            "variants.relation", validationResult);
                }
            }
            List<VariantNameDto> variantNames = variant.getVariantNames();
            if (null != variantNames) {
                for (VariantNameDto variantName : variantNames) {
                    checkNotEmptyAndMaxLength(variantName.getLdhName(),
                            MAX_LENGTH_LDHNAME, "variants.ldhName",
                            validationResult);
                    checkNotEmptyAndMaxLength(variantName.getUnicodeName(),
                            MAX_LENGTH_UNICODENAME, "variants.unicodeName",
                            validationResult);
                }
            }
        }
    }

    /**
     * 
     * @param typeStr
     *            typeStr.
     * @param fieldName
     *            fieldName.
     * @param validationResult
     *            validationResult.
     */
    protected void checkDomainTypeNotEmptyAndValid(String typeStr,
            ValidationResult validationResult) {
        String fieldName = "type";
        checkNotEmpty(typeStr, fieldName, validationResult);
        checkDomainTypeValid(typeStr, fieldName, validationResult);
    }

    /**
     * 
     * @param typeStr
     *            typeStr.
     * @param fieldName
     *            fieldName.
     * @param validationResult
     *            validationResult.
     */
    protected void checkDomainTypeValid(String typeStr, String fieldName,
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
    /**
     * 
     * @return nameserverDao.
     */
    public UpdateDao<Nameserver, NameserverDto> getNameserverDao() {
        return nameserverDao;
    }
    /**
     * 
     * @return secureDnsUpdateDao.
     */
    public UpdateDao<SecureDns, SecureDnsDto> getSecureDnsUpdateDao() {
        return secureDnsUpdateDao;
    }
    /**
     * 
     * @return variantUpdateDao.
     */
    public UpdateDao<Variants, VariantDto> getVariantUpdateDao() {
        return variantUpdateDao;
    }
    /**
     * 
     * @return networkDao.
     */
    public UpdateDao<Network, IpDto> getNetworkDao() {
        return networkDao;
    }

}
