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

import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_2048;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_255;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_HANDLE;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_LANG;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_PORT43;
import static org.restfulwhois.rdap.common.util.UpdateValidateUtil.MAX_LENGTH_STATUS;

import java.util.List;
import java.util.Map;

import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.Event;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.PublicId;
import org.restfulwhois.rdap.common.model.Remark;
import org.restfulwhois.rdap.common.model.base.BaseModel;
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
 * 
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

    @Autowired
    protected UpdateDao<MODEL, DTO> dao;
    @Autowired
    protected UpdateDao<Event, EventDto> eventDao;
    @Autowired
    protected UpdateDao<Link, LinkDto> linkDao;
    @Autowired
    protected UpdateDao<Remark, RemarkDto> remarkDao;
    @Autowired
    protected UpdateDao<PublicId, PublicIdDto> publicIdDao;
    @Autowired
    protected UpdateDao<Entity, EntityDto> entityDao;

    @Override
    public UpdateResponse execute(DTO dto) {
        LOGGER.info("begin update dto:{}", dto);
        long queryStart = System.currentTimeMillis();
        ValidationResult validationResult = validate(dto);
        if (validationResult.hasError()) {
            return handleError(dto, validationResult);
        }
        MODEL model = convertDtoToModel(dto);
        execute(model);
        UpdateResponse response =
                UpdateResponse.buildSuccessResponse(model.getHandle());
        long usedTime = System.currentTimeMillis() - queryStart;
        LOGGER.info("end update, milliseconds:{}", usedTime);
        return response;
    }

    abstract protected void execute(MODEL model);

    abstract protected MODEL convertDtoToModel(DTO dto);

    abstract protected ValidationResult validate(DTO dto);

    protected void convertCustomProperties(DTO dto, MODEL model) {
        Map<String, String> customProperties = dto.getCustomProperties();
        model.setCustomProperties(customProperties);
        model.setCustomPropertiesJsonVal(JsonUtil
                .serializeMap(customProperties));
    }

    protected void checkNotEmpty(List<String> values, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkNotEmpty(values, fieldName, validationResult);
    }

    protected void checkNotEmpty(String value, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkNotEmpty(value, fieldName, validationResult);
    }
    protected void checkNotNull(Object value, String fieldName,
            ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkNotNull(value, fieldName, validationResult);
    }

    protected void checkMaxLength(String value, int maxLength,
            String fieldName, ValidationResult validationResult) {
        if (validationResult.hasError()) {
            return;
        }
        UpdateValidateUtil.checkMaxLength(value, maxLength, fieldName,
                validationResult);
    }

    protected void checkNotEmptyAndMaxLength(String value, int maxLength,
            String fieldName, ValidationResult validationResult) {
        checkNotEmpty(value, fieldName, validationResult);
        checkMaxLength(value, maxLength, fieldName, validationResult);
    }

    protected void checkMinMaxInt(Integer value, String fieldName,
            ValidationResult validationResult) {
        UpdateValidateUtil.checkMinMaxInt(value,
                UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN, fieldName,
                validationResult);
    }
    protected void checkNotNullAndMinMaxInt(Integer value, String fieldName,
            ValidationResult validationResult) {
        checkNotNull(value, fieldName, validationResult);
        UpdateValidateUtil.checkMinMaxInt(value,
                UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN, fieldName,
                validationResult);
    }

    protected void checkMinMaxTinyInt(int value, String fieldName,
            ValidationResult validationResult) {
        UpdateValidateUtil.checkMinMaxInt(value,
                UpdateValidateUtil.MIN_VAL_FOR_TINYINT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_TINYINT_COLUMN, fieldName,
                validationResult);
    }

    protected void checkMaxLengthForStatus(String statusValue,
            ValidationResult validationResult) {
        checkMaxLength(statusValue, MAX_LENGTH_STATUS, "status",
                validationResult);
    }

    protected void checkMaxLengthForStatus(List<String> statusList,
            ValidationResult validationResult) {
        if (null == statusList) {
            return;
        }
        for (String status : statusList) {
            checkMaxLengthForStatus(status, validationResult);
        }
    }

    protected void checkMaxLengthForLang(String langValue,
            ValidationResult validationResult) {
        checkMaxLength(langValue, MAX_LENGTH_LANG, "lang", validationResult);
    }

    protected void checkMaxLengthForPort43(String port43Value,
            ValidationResult validationResult) {
        checkMaxLength(port43Value, MAX_LENGTH_PORT43, "port43",
                validationResult);
    }

    protected void checkNotEmptyAndMaxLengthForHandle(String handleValue,
            ValidationResult validationResult) {
        checkNotEmptyAndMaxLength(handleValue, MAX_LENGTH_HANDLE, "handle",
                validationResult);
    }

    protected void checkEvents(List<EventDto> events,
            ValidationResult validationResult) {
        if (null == events) {
            return;
        }
        for (EventDto event : events) {
            checkNotEmptyAndMaxLength(event.getEventAction(), MAX_LENGTH_255,
                    "event.eventAction", validationResult);
            checkMaxLength(event.getEventActor(), MAX_LENGTH_255,
                    "event.eventActor", validationResult);
            UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                    event.getEventDate(), "event.eventDate", validationResult);
        }
    }

    protected void checkPublicIds(List<PublicIdDto> publicIds,
            ValidationResult validationResult) {
        if (null == publicIds) {
            return;
        }
        for (PublicIdDto publicId : publicIds) {
            checkNotEmptyAndMaxLength(publicId.getIdentifier(), MAX_LENGTH_255,
                    "public.identifier", validationResult);
            checkNotEmptyAndMaxLength(publicId.getType(), MAX_LENGTH_255,
                    "public.type", validationResult);
        }
    }

    protected void checkRemarks(List<RemarkDto> remarks,
            ValidationResult validationResult) {
        if (null == remarks) {
            return;
        }
        for (RemarkDto remark : remarks) {
            checkMaxLength(remark.getTitle(), MAX_LENGTH_255, "remark.title",
                    validationResult);
            List<String> descriptions = remark.getDescription();
            if (null != descriptions) {
                for (String description : descriptions) {
                    checkMaxLength(description, MAX_LENGTH_2048,
                            "remark.description", validationResult);
                }
            }
        }
    }

    protected void checkLinks(List<LinkDto> links,
            ValidationResult validationResult) {
        if (null == links) {
            return;
        }
        for (LinkDto link : links) {
            checkMaxLength(link.getTitle(), MAX_LENGTH_255, "link.title",
                    validationResult);
            checkMaxLength(link.getMedia(), MAX_LENGTH_255, "link.media",
                    validationResult);
            checkMaxLength(link.getRel(), MAX_LENGTH_255, "link.rel",
                    validationResult);
            checkMaxLength(link.getType(), MAX_LENGTH_255, "link.type",
                    validationResult);
            checkMaxLength(link.getValue(), MAX_LENGTH_2048, "link.value",
                    validationResult);
            checkMaxLength(link.getHref(), MAX_LENGTH_2048, "link.href",
                    validationResult);
            List<String> hreflangs = link.getHreflang();
            if (null != hreflangs) {
                for (String hreflang : hreflangs) {
                    checkMaxLength(hreflang, MAX_LENGTH_LANG, "link.hreflang",
                            validationResult);
                }
            }
        }
    }

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

    protected void saveEvents(List<EventDto> events, MODEL model) {
        eventDao.batchCreateAsInnerObjects(model, events);
    }

    protected void saveLinks(List<LinkDto> links, MODEL model) {
        linkDao.batchCreateAsInnerObjects(model, links);
    }

    protected void saveRemarks(List<RemarkDto> remarks, MODEL model) {
        remarkDao.batchCreateAsInnerObjects(model, remarks);
    }

    protected void savePublicIds(List<PublicIdDto> publicIds, MODEL model) {
        publicIdDao.batchCreateAsInnerObjects(model, publicIds);
    }

    protected void saveEntities(MODEL model) {
        entityDao.saveRel(model);
    }

    private UpdateResponse handleError(BaseDto dto,
            ValidationResult validationResult) {
        ValidationError error = validationResult.getFirstError();
        UpdateValidationError validationError = (UpdateValidationError) error;
        return UpdateResponse.buildErrorResponse(dto.getHandle(),
                validationError.getCode(), validationError.getHttpStatusCode(),
                validationError.getMessage());
    }
}
