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

import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * delete service implementation.
 * 
 * @author jiashuo
 * 
 */
@Service("entityDeleteServiceImpl")
public class EntityDeleteServiceImpl extends EntityUpdateBaseServiceImpl {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(EntityDeleteServiceImpl.class);

    @Override
    protected void execute(Entity entity) {
        LOGGER.debug("delete entitys...");
        getDao().delete(entity);
        LOGGER.debug("delete status...");
        getDao().deleteStatus(entity);
        deleteEntityAddresses(entity);
        deleteEntityTels(entity);
        deletePublicIds(entity);
        deleteBaseModelRel(entity);
    }

    @Override
    protected Entity convertDtoToModel(EntityDto dto) {
        Entity entity = new Entity();
        Long id = getDao().findIdByHandle(dto.getHandle());
        entity.setId(id);
        entity.setHandle(dto.getHandle());
        return entity;
    }

    @Override
    protected ValidationResult validate(EntityDto entityDto) {
        ValidationResult validationResult = new ValidationResult();
        checkNotEmpty(entityDto.getHandle(), "handle", validationResult);
        checkHandleExistForUpdate(entityDto.getHandle(), validationResult);
        return validationResult;
    }

}
