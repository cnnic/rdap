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
package org.restfulwhois.rdap.common.service;

import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.ERROR_PROP_NAME_PREFIX_ENTITY;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.ERROR_PROP_NAME_PREFIX_EVENT;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.ERROR_PROP_NAME_PREFIX_LINK;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.ERROR_PROP_NAME_PREFIX_PUBLICID;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.ERROR_PROP_NAME_PREFIX_REMARK;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_2048;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_255;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_HANDLE;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_LANG;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_PORT43;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_STATUS;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.Event;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.PublicId;
import org.restfulwhois.rdap.common.model.Remark;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.common.util.JsonUtil;
import org.restfulwhois.rdap.common.util.UpdateValidateUtil;
import org.restfulwhois.rdap.common.validation.UpdateValidationError;
import org.restfulwhois.rdap.common.validation.ValidationError;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * abstract update service.
 * @param <DTO>
 *     object derived from BaseDto.
 * @param <MODEL>
 *     object derived from BaseModel.
 * @author jiashuo
 * 
 */
public abstract class AbstractUpdateService<DTO extends BaseDto, MODEL extends BaseModel>
        implements UpdateService<DTO, MODEL> {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractUpdateService.class);

    /**
     * update dao.
     */
    @Autowired
    private UpdateDao<MODEL, DTO> dao;
    /**
     * event dao.
     */
    @Autowired
    private UpdateDao<Event, EventDto> eventDao;
    /**
     * link dao.
     */
    @Autowired
    private UpdateDao<Link, LinkDto> linkDao;
    /**
     * remark dao.
     */
    @Autowired
    private UpdateDao<Remark, RemarkDto> remarkDao;
    /**
     * public id dao.
     */
    @Autowired
    private UpdateDao<PublicId, PublicIdDto> publicIdDao;
    /**
     * entity dao.
     */
    @Autowired
    private UpdateDao<Entity, EntityDto> entityDao;

    @Override
    public UpdateResponse execute(DTO dto) {
        LOGGER.debug("update dto:{}", dto);
        long queryStart = System.currentTimeMillis();
        ValidationResult validationResult = validate(dto);
        if (validationResult.hasError()) {
            LOGGER.info("update dto error:{}", validationResult.getFirstError()
                    .getCode());
            return handleError(dto, validationResult);
        }
        MODEL model = convertDtoToModel(dto);
        execute(model);
        UpdateResponse response =
                UpdateResponse.buildSuccessResponse(model.getHandle());
        long usedTime = System.currentTimeMillis() - queryStart;
        LOGGER.debug("end update, milliseconds:{}", usedTime);
        return response;
    }

    /**
     * 
     * @param model
     *     object derived from BaseModel.
     */
    protected abstract  void execute(MODEL model);
    
    /**
     * covert dto to model.
     * @param dto 
     *        object.
     * @return model 
     */
    protected abstract  MODEL convertDtoToModel(DTO dto);

    /**
     * validate dto.
     * @param dto
     *      dto. 
     * @return ValidationResult 
     */
    protected abstract  ValidationResult validate(DTO dto);
    
    /**
     * validate inner object include status,entities,remarks etc.
     * @param dto
     *   baseDto.
     * @param validationResult
     *           validationResult.
     */
    protected void validateBaseDto(BaseDto dto,
            ValidationResult validationResult) {
        checkMaxLengthForStatus(dto.getStatus(), validationResult);
        checkEntities(dto.getEntities(), validationResult);
        checkRemarks(dto.getRemarks(), validationResult);
        checkLinks(dto.getLinks(), validationResult);
        checkMaxLengthForPort43(dto.getPort43(), validationResult);
        checkEvents(dto.getEvents(), validationResult);
        checkMaxLengthForLang(dto.getLang(), validationResult);
    }

    /**
     * save inner object include entitesRel,remarks,links,events.
     * @param model
     *      model.
     */
    protected void saveBaseModel(MODEL model) {
        saveEntitiesRel(model);
        BaseDto dto = model.getDto();
        saveRemarks(dto.getRemarks(), model);
        saveLinks(dto.getLinks(), model);
        saveEvents(dto.getEvents(), model);
    }

    /**
     * update inner object include entitesRel,remarks,links,events.
     *
     * @param model
     *   model.
     */
    protected void updateBaseModel(MODEL model) {
        BaseDto dto = model.getDto();
        updateEntitiesRel(model);
        updateRemarks(dto.getRemarks(), model);
        updateLinks(dto.getLinks(), model);
        updateEvents(dto.getEvents(), model);
    }

    /**
     * delete inner object include entitesRel,remarks,links,events.
     * @param model
     *      model.
     */
    protected void deleteBaseModelRel(MODEL model) {
        deleteEntitiesRel(model);
        deleteRemarks(model);
        deleteLinks(model);
        deleteEvents(model);
    }

    /**
     * check ip is Valid.
     * @param ip
     *       ip.
     * @param fieldName
     *      fieldName.
     * @param validationResult
     *    validationResult.     
     */
    protected void checkIp(String ip, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        if (StringUtils.isBlank(ip)) {
            return;
        }
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(ip);
        if (ipVersion.isNotValidIp()) {
            validationResult.addError(UpdateValidationError
                    .build4008Error(fieldName));
        }
    }

    /**
     * covert customProperties.
     * @param dto
     *    dto.
     * @param model
     *     model.
     */
    protected void convertCustomProperties(DTO dto, MODEL model) {
        Map<String, String> customProperties = dto.getCustomProperties();
        model.setCustomProperties(customProperties);
        if (null == customProperties || customProperties.isEmpty()) {
            return;
        }
        model.setCustomPropertiesJsonVal(JsonUtil
                .serializeMap(customProperties));
    }

    /**
     * check string list is not empty.
     * @param values
     *      values.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *     validationResult.
     */
    protected void checkNotEmpty(List<String> values, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkNotEmpty(values, fieldName, validationResult);
    }

    /**
     * check value not empty.
     * @param value
     *      value.
     * @param fieldName
     *     fieldName.
     * @param validationResult
     *     validationResult.
     */
    protected void checkNotEmpty(String value, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkNotEmpty(value, fieldName, validationResult);
    }

    /**
     * check value not null.
     * @param value
     *     value.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *     validationResult.
     */
    protected void checkNotNull(Object value, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkNotNull(value, fieldName, validationResult);
    }

    /**
     * check maxLength.
     * @param value
     *       value.
     * @param maxLength
     *       maxLength.
     * @param fieldName
     *       fieldName.
     * @param validationResult
     *     validationResult.
     */
    protected void checkMaxLength(String value, int maxLength,
            String fieldName, ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkMaxLength(value, maxLength, fieldName,
                validationResult);
    }

    /**
     * check not empty and max length.
     * @param value 
     *      value.
     * @param maxLength
     *     maxLength.
     * @param fieldName
     *      fieldName.
     * @param validationResult
     *      validationResult.
     */
    protected void checkNotEmptyAndMaxLength(String value, int maxLength,
            String fieldName, ValidationResult validationResult) {
        checkNotEmpty(value, fieldName, validationResult);
        checkMaxLength(value, maxLength, fieldName, validationResult);
    }

    /**
     * check an  integer  between  maximum with minimum.
     * @param value
     *    value.
     * @param fieldName
     *     FieldName.
     * @param validationResult
     *     validationResult
     */
    protected void checkMinMaxInt(Integer value, String fieldName,
            ValidationResult validationResult) {
        UpdateValidateUtil.checkMinMaxInt(value,
                UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN, fieldName,
                validationResult);
    }

    /**
     * check an integer is not null and between maximum with minimum.
     * @param value
     *     value.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *    validationResult.
     */
    protected void checkNotNullAndMinMaxInt(Integer value, String fieldName,
            ValidationResult validationResult) {
        checkNotNull(value, fieldName, validationResult);
        UpdateValidateUtil.checkMinMaxInt(value,
                UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN, fieldName,
                validationResult);
    }

    /**
     * check an big integer is not null and between maximum with minimum.
     * @param value
     *   value.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *   validationResult.
     */
    protected void checkNotNullAndMinMaxBigInt(Long value, String fieldName,
            ValidationResult validationResult) {
        checkNotNull(value, fieldName, validationResult);
        UpdateValidateUtil.checkMinMaxLong(value,
                UpdateValidateUtil.MIN_VAL_FOR_BIGINT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_BIGINT_COLUMN, fieldName,
                validationResult);
    }
    
    /**
     * check an tiny integer between maximum with minimum.
     * @param value
     *     value.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *    validationResult.
     */
    protected void checkMinMaxTinyInt(int value, String fieldName,
            ValidationResult validationResult) {
        UpdateValidateUtil.checkMinMaxInt(value,
                UpdateValidateUtil.MIN_VAL_FOR_TINYINT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_TINYINT_COLUMN, fieldName,
                validationResult);
    }

    /**
     * check status max length.
     * @param statusValue
     *    statusValue.
     * @param validationResult
     *     validationResult.
     */
    protected void checkMaxLengthForStatus(String statusValue,
            ValidationResult validationResult) {
        checkMaxLength(statusValue, MAX_LENGTH_STATUS, "status",
                validationResult);
    }

    /**
     * check status max length.
     * @param statusList
     *    status list.
     * @param validationResult
     *    validationResult.
     */
    protected void checkMaxLengthForStatus(List<String> statusList,
            ValidationResult validationResult) {
        if (null == statusList) {
            return;
        }
        for (String status : statusList) {
            checkMaxLengthForStatus(status, validationResult);
        }
    }

    /**
     * check the length of lang  is less than the maximum length.
     * @param langValue
     *    langValue.
     * @param validationResult
     *    validationResult.
     */
    protected void checkMaxLengthForLang(String langValue,
            ValidationResult validationResult) {
        checkMaxLength(langValue, MAX_LENGTH_LANG, "lang", validationResult);
    }

    /**
     * check the length of port43 is less than the maximum length.
     * @param port43Value
     *    port43Value.
     * @param validationResult
     *    validationResult.
     */
    protected void checkMaxLengthForPort43(String port43Value,
            ValidationResult validationResult) {
        checkMaxLength(port43Value, MAX_LENGTH_PORT43, "port43",
                validationResult);
    }

    /**
     * check handle not empty and length is less than maximum length.
     * @param handleValue
     *   handleValue.
     * @param validationResult
     *    validationResult.
     */
    protected void checkNotEmptyAndMaxLengthForHandle(String handleValue,
            ValidationResult validationResult) {
        checkNotEmptyAndMaxLength(handleValue, MAX_LENGTH_HANDLE, "handle",
                validationResult);
    }

    /**
     * check event.
     * @param events
     *    events.
     * @param validationResult
     *     validationResult.
     */
    protected void checkEvents(List<EventDto> events,
            ValidationResult validationResult) {
        if (null == events) {
            return;
        }
        for (EventDto event : events) {
            checkNotEmptyAndMaxLength(event.getEventAction(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_EVENT + "eventAction",
                    validationResult);
            checkMaxLength(event.getEventActor(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_EVENT + "eventActor",
                    validationResult);
            UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                    event.getEventDate(), ERROR_PROP_NAME_PREFIX_EVENT
                            + "eventDate", validationResult);
        }
    }

    /**
     * check publicId.
     * @param publicIds
     *   publicIds.
     * @param validationResult
     *    validationResult.
     */
    protected void checkPublicIds(List<PublicIdDto> publicIds,
            ValidationResult validationResult) {
        if (null == publicIds) {
            return;
        }
        for (PublicIdDto publicId : publicIds) {
            checkNotEmptyAndMaxLength(publicId.getIdentifier(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_PUBLICID + "identifier",
                    validationResult);
            checkNotEmptyAndMaxLength(publicId.getType(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_PUBLICID + "type", validationResult);
        }
    }

    /**
     * check remark.
     * @param remarks
     *    remarks.
     * @param validationResult
     *    validationResult.
     */
    protected void checkRemarks(List<RemarkDto> remarks,
            ValidationResult validationResult) {
        if (null == remarks) {
            return;
        }
        for (RemarkDto remark : remarks) {
            checkMaxLength(remark.getTitle(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_REMARK + "title", validationResult);
            List<String> descriptions = remark.getDescription();
            if (null != descriptions) {
                for (String description : descriptions) {
                    checkMaxLength(description, MAX_LENGTH_2048,
                            ERROR_PROP_NAME_PREFIX_REMARK + "description",
                            validationResult);
                }
            }
        }
    }

    /**
     * check link.
     * @param links
     *    links
     * @param validationResult
     *    validationResult
     */
    protected void checkLinks(List<LinkDto> links,
            ValidationResult validationResult) {
        if (null == links) {
            return;
        }
        for (LinkDto link : links) {
            checkMaxLength(link.getTitle(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_LINK + "title", validationResult);
            checkMaxLength(link.getMedia(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_LINK + "media", validationResult);
            checkMaxLength(link.getRel(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_LINK + "rel", validationResult);
            checkMaxLength(link.getType(), MAX_LENGTH_255,
                    ERROR_PROP_NAME_PREFIX_LINK + "type", validationResult);
            checkMaxLength(link.getValue(), MAX_LENGTH_2048,
                    ERROR_PROP_NAME_PREFIX_LINK + "value", validationResult);
            checkMaxLength(link.getHref(), MAX_LENGTH_2048,
                    ERROR_PROP_NAME_PREFIX_LINK + "href", validationResult);
            List<String> hreflangs = link.getHreflang();
            if (null != hreflangs) {
                for (String hreflang : hreflangs) {
                    checkMaxLength(hreflang, MAX_LENGTH_LANG,
                            ERROR_PROP_NAME_PREFIX_LINK + "hreflang",
                            validationResult);
                }
            }
        }
    }

    /**
     * check entity.
     * @param entities
     *     entities.
     * @param validationResult
     *      validationResult.
     */
    protected void checkEntities(List<EntityHandleDto> entities,
            ValidationResult validationResult) {
        if (null == entities) {
            return;
        }
        for (EntityHandleDto entityHandle : entities) {
            List<String> roles = entityHandle.getRoles();
            for (String role : roles) {
                checkNotEmptyAndMaxLength(role, MAX_LENGTH_255,
                        ERROR_PROP_NAME_PREFIX_ENTITY + "roles",
                        validationResult);
            }
        }
    }

    /**
     * check handle not exist for create.
     * @param handle
     *    handle.
     * @param validationResult
     *   validationResult.
     */
    protected void checkHandleNotExistForCreate(String handle,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        Long id = dao.findIdByHandle(handle);
        if (null != id) {
            validationResult.addError(UpdateValidationError
                    .build4091Error(handle));
        }
    }

    /**
     * check handle exist for update.
     * @param handle
     *     handle.
     * @param validationResult
     *     validationResult.
     */
    protected void checkHandleExistForUpdate(String handle,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        Long id = dao.findIdByHandle(handle);
        if (null == id) {
            validationResult.addError(UpdateValidationError
                    .build4041Error(handle));
        }
    }

    /**
     * save events.
     * @param events
     *    event list.
     * @param model
     *    outer object associated.
     */
    protected void saveEvents(List<EventDto> events, MODEL model) {
        LOGGER.debug("save events...");
        eventDao.saveAsInnerObjects(model, events);
    }

    /**
     * delete events.
     * @param model
     *    outer object associated.
     */
    protected void deleteEvents(MODEL model) {
        LOGGER.debug("delete events...");
        eventDao.deleteAsInnerObjects(model);
    }

    /**
     * update events.
     * @param events
     *    event list.
     * @param model
     *    outer object associated.
     */
    protected void updateEvents(List<EventDto> events, MODEL model) {
        deleteEvents(model);
        saveEvents(events, model);
    }

    /**
     * save links.
     * @param links
     *    link list.
     * @param model
     *    outer object associated.
     */
    protected void saveLinks(List<LinkDto> links, MODEL model) {
        LOGGER.debug("save links...");
        linkDao.saveAsInnerObjects(model, links);
    }
    
    /**
     * delete links.     
     * @param model
     *    outer object associated.
     */
    protected void deleteLinks(MODEL model) {
        LOGGER.debug("delete links...");
        linkDao.deleteAsInnerObjects(model);
    }

    /**
     * update links.
     * @param links
     *    link list.
     * @param model
     *    outer object associated.
     */
    protected void updateLinks(List<LinkDto> links, MODEL model) {
        deleteLinks(model);
        saveLinks(links, model);
    }

    /**
     * save remarks.
     * @param remarks
     *    remark list.
     * @param model
     *    outer object associated.
     */
    protected void saveRemarks(List<RemarkDto> remarks, MODEL model) {
        LOGGER.debug("save remarks...");
        remarkDao.saveAsInnerObjects(model, remarks);
    }

    /**
     * delete remarks.
     * @param model
     *    outer object associated.
     */
    protected void deleteRemarks(MODEL model) {
        LOGGER.debug("delete remarks...");
        remarkDao.deleteAsInnerObjects(model);
    }

    /**
     * update remarks.
     * @param remarks
     *    remark list.
     * @param model
     *    outer object associated.
     */
    protected void updateRemarks(List<RemarkDto> remarks, MODEL model) {
        deleteRemarks(model);
        saveRemarks(remarks, model);
    }

    /**
     * save publicIds.
     * @param publicIds
     *     publicIds
     * @param model
     *   outer object associated.
     */
    protected void savePublicIds(List<PublicIdDto> publicIds, MODEL model) {
        LOGGER.debug("save publicIds...");
        publicIdDao.saveAsInnerObjects(model, publicIds);
    }

    /**
     * delete publicIds.
     * @param model
     *   outer object associated.
     */
    protected void deletePublicIds(MODEL model) {
        LOGGER.debug("delete publicIds...");
        publicIdDao.deleteAsInnerObjects(model);
    }

    /**
     * update publicIds.
     * @param publicIds
     *    publicIds.
     * @param model
     *     model.
     */
    protected void updatePublicIds(List<PublicIdDto> publicIds, MODEL model) {
        deletePublicIds(model);
        savePublicIds(publicIds, model);
    }

    /**
     * save entity relation.
     * @param model
     *    model.
     */
    protected void saveEntitiesRel(MODEL model) {
        LOGGER.debug("save entities rel...");
        entityDao.saveRel(model);
    }

    /**
     * delete entity relation.
     * @param model
     *    model.
     */
    protected void deleteEntitiesRel(MODEL model) {
        entityDao.deleteRel(model);
    }

    /**
     * update entity relation. 
     * @param model
     *    model.
     */
    protected void updateEntitiesRel(MODEL model) {
        deleteEntitiesRel(model);
        saveEntitiesRel(model);
    }

    /**
     * handle error.
     * @param dto
     *   dto.
     * @param validationResult
     *      validationResult.
     * @return  updateResponse.
     */
    private UpdateResponse handleError(BaseDto dto,
            ValidationResult validationResult) {
        ValidationError error = validationResult.getFirstError();
        UpdateValidationError validationError = (UpdateValidationError) error;
        return UpdateResponse.buildErrorResponse(dto.getHandle(),
                validationError.getCode(), validationError.getHttpStatusCode(),
                validationError.getMessage());
    }

    /**
     * get dao.
     * @return dao.
     */
    public UpdateDao<MODEL, DTO> getDao() {
        return dao;
    }

}
