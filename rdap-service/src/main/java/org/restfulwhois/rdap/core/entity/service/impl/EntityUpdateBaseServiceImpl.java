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
package org.restfulwhois.rdap.core.entity.service.impl;

import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.ERROR_PROP_NAME_PREFIX_ENTITY_ADDR;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.ERROR_PROP_NAME_PREFIX_ENTITY_TEL;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_255;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_PORT43;

import java.util.List;

import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityAddressDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityTelephoneDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.EntityAddress;
import org.restfulwhois.rdap.common.model.EntityTelephone;
import org.restfulwhois.rdap.common.service.AbstractUpdateService;
import org.restfulwhois.rdap.common.util.BeanUtil;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * create service implementation.
 * 
 * @author zhanyq
 * 
 */
public abstract class EntityUpdateBaseServiceImpl extends
        AbstractUpdateService<EntityDto, Entity> {
   /**
     * entityAddressDao.
     */
    @Autowired
    protected UpdateDao<EntityAddress, EntityAddressDto> entityAddressDao;
    /**
     * entityTelDao.
     */
    @Autowired
    protected UpdateDao<EntityTelephone, EntityTelephoneDto> entityTelDao;
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EntityUpdateBaseServiceImpl.class);
   /**
    * @param dto entityDto.
    * @return entity.
    */
    protected Entity convertDtoToModel(EntityDto dto) {
        Entity entity = convertDtoToEntity(dto);
        super.convertCustomProperties(dto, entity);
        return entity;
    }
    /**
     * 
     * @param entity entity.
     */
    protected void saveEntityAddresses(Entity entity) {
        LOGGER.debug("save entityAddresses ...");
        EntityDto dto = (EntityDto) entity.getDto();
        List<EntityAddressDto> entityAddressList = dto.getAddresses();
        if (null == entityAddressList || entityAddressList.isEmpty()) {
            LOGGER.debug("entityAddressList is empty.");
            return;
        }
        entityAddressDao.saveAsInnerObjects(entity, entityAddressList);
    }
    /**
     * 
     * @param entity entity.
     */
    protected void saveEntityTels(Entity entity) {
        LOGGER.debug("save entityTelephones ...");
        EntityDto dto = (EntityDto) entity.getDto();
        List<EntityTelephoneDto> entityTelList = dto.getTelephones();
        if (null == entityTelList || entityTelList.isEmpty()) {
            LOGGER.debug("entityTelList is empty.");
            return;
        }
        entityTelDao.saveAsInnerObjects(entity, entityTelList);
    }
    /**
     * 
     * @param entity entity.
     */
    protected void deleteEntityAddresses(Entity entity) {
        LOGGER.debug("delete entityAddresses ...");
        entityAddressDao.deleteAsInnerObjects(entity);
    }
    /**
     * 
     * @param entity entity.
     */
    protected void deleteEntityTels(Entity entity) {
        LOGGER.debug("delete entityTelephones ...");
        entityTelDao.deleteAsInnerObjects(entity);
    }
    /**
     * 
     * @param entity entity.
     */
    protected void updateEntityAddresses(Entity entity) {
        deleteEntityAddresses(entity);
        saveEntityAddresses(entity);
    }
    /**
     * 
     * @param entity entity.
     */
    protected void updateEntityTels(Entity entity) {
        deleteEntityTels(entity);
        saveEntityTels(entity);
    }
    /**
     * 
     * @param dto entityDto.
     * @return entity.
     */
    private Entity convertDtoToEntity(EntityDto dto) {
        Entity entity = new Entity();
        BeanUtil.copyProperties(dto, entity, "entities", "events", "remarks",
                "links", "publicIds", "addresses", "telephones");
        return entity;
    }
    /**
     * 
     * @param dto entityDto.
     * @param validationResult validationResult.
     * @return ValidationResult
     */
    protected ValidationResult validateForSaveAndUpdate(EntityDto dto,
            ValidationResult validationResult) {
        checkNotEmptyAndMaxLengthForHandle(dto.getHandle(), validationResult);
        checkNotEmptyAndMaxLength(dto.getFn(), MAX_LENGTH_255, "fn",
                validationResult);
        checkMaxLength(dto.getKind(), MAX_LENGTH_255, "kind", validationResult);
        checkMaxLength(dto.getEmail(), MAX_LENGTH_255, "email",
                validationResult);
        checkMaxLength(dto.getTitle(), MAX_LENGTH_255, "title",
                validationResult);
        checkMaxLength(dto.getOrg(), MAX_LENGTH_255, "org", validationResult);
        checkMaxLength(dto.getUrl(), MAX_LENGTH_PORT43, "url", validationResult);
        checkEntityAddress(dto, validationResult);
        checkEntityTel(dto, validationResult);
        checkPublicIds(dto.getPublicIds(), validationResult);
        validateBaseDto(dto, validationResult);
        return validationResult;
    }
    /**
     * 
     * @param dto entityDto.
     * @param validationResult validationResult.
     */
    private void checkEntityAddress(EntityDto dto,
            ValidationResult validationResult) {
        List<EntityAddressDto> entityAddressList = dto.getAddresses();
        if (null == entityAddressList || entityAddressList.isEmpty()) {
            return;
        }
        for (EntityAddressDto entityAddress : entityAddressList) {
            checkMaxLength(entityAddress.getCountry(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "country",
                    validationResult);
            checkMaxLength(entityAddress.getExtendedAddress(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "extendedAddress",
                    validationResult);
            checkMaxLength(entityAddress.getLocality(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "locality",
                    validationResult);
            checkMaxLength(entityAddress.getPostalcode(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "postalcode",
                    validationResult);
            checkMaxLength(entityAddress.getPostbox(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "postbox",
                    validationResult);
            checkMaxLength(entityAddress.getTypes(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "types",
                    validationResult);
            checkMaxLength(entityAddress.getStreetAddress(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "streetAddress",
                    validationResult);
            checkMaxLength(entityAddress.getRegion(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "region",
                    validationResult);
            checkMinMaxInt(entityAddress.getPref(),
                    ERROR_PROP_NAME_PREFIX_ENTITY_ADDR + "pref",
                    validationResult);
        }
    }
    /**
     * 
     * @param dto entityDto.
     * @param validationResult validationResult.
     */
    private void
            checkEntityTel(EntityDto dto, ValidationResult validationResult) {
        List<EntityTelephoneDto> entityTelList = dto.getTelephones();
        if (null == entityTelList || entityTelList.isEmpty()) {
            return;
        }
        for (EntityTelephoneDto entityTelephone : entityTelList) {
            checkMaxLength(entityTelephone.getExtNumber(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_TEL + "extNumber",
                    validationResult);
            checkMaxLength(entityTelephone.getNumber(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_TEL + "number",
                    validationResult);
            checkMaxLength(entityTelephone.getTypes(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_ENTITY_TEL + "types",
                    validationResult);
            checkMinMaxInt(entityTelephone.getPref(),
                    ERROR_PROP_NAME_PREFIX_ENTITY_TEL + "pref",
                    validationResult);
        }
    }
}
