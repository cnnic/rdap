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
package org.restfulwhois.rdap.core.autnum.service.impl;

import static org.restfulwhois.rdap.common.util
               .UpdateValidateUtil.MAX_LENGTH_255;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.model.Autnum;
import org.restfulwhois.rdap.common.service.AbstractUpdateService;
import org.restfulwhois.rdap.common.util.BeanUtil;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * update service implementation.
 * 
 * @author zhanyq
 * 
 */
public abstract class AutnumUpdateBaseServiceImpl extends
        AbstractUpdateService<AutnumDto, Autnum> {   
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AutnumUpdateBaseServiceImpl.class);

    /**
     * @param dto 
     *      autnumdto.
     * @return Autnum.
     */
    protected Autnum convertDtoToModel(AutnumDto dto) {
        Autnum autnum = convertDtoToAutnum(dto);
        super.convertCustomProperties(dto, autnum);
        return autnum;
    }   
   
    /**
     * 
     * @param dto dto.
     * @return Autnum.
     */
    private Autnum convertDtoToAutnum(AutnumDto dto) {
        Autnum autnum = new Autnum();
        BeanUtil.copyProperties(dto, autnum, "entities", "events",
                "remarks", "links");
        return autnum;
    }

    /**
     * 
     * @param dto dto.
     * @param validationResult validationResult.
     * @return ValidationResult.
     */
    protected ValidationResult validateForSaveAndUpdate(AutnumDto dto,
            ValidationResult validationResult) {
        LOGGER.debug("validate for save and update ...");
        checkNotNullAndMinMaxBigInt(dto.getStartAutnum(), 
                 "startAutnum", validationResult);
        checkNotNullAndMinMaxBigInt(dto.getEndAutnum(),
                "endAutnum", validationResult);
        checkMaxLength(dto.getName(), MAX_LENGTH_255,
                "name", validationResult);
        checkMaxLength(dto.getType(), MAX_LENGTH_255,
                "type", validationResult);
        checkMaxLength(dto.getCountry(), MAX_LENGTH_255,
                "country", validationResult);
        checkNotEmptyAndMaxLengthForHandle(dto.getHandle(), validationResult);
        validateBaseDto(dto, validationResult);
        return validationResult;
    }
}
