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
package org.restfulwhois.rdap.common.dao;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.model.base.BaseModel;

/**
 * update DAO interface.
 * 
 * @param <T>
 *            object derived from BaseModel.
 * @param <DTO>
 *      object derived from BaseDto.
 * @author jiashuo
 * 
 */
public interface UpdateDao<T extends BaseModel, DTO extends BaseDto> {

    /**
     * 
     * @param handle
     *    handle.
     * @return long
     */
    Long findIdByHandle(String handle);
    
    /**
     * save method.
     * @param model
     *    model.
     * @return object.
     */
    T save(T model);

    /**
     * update method.
     * @param model
     *   model
     */
    void update(T model);

    /**
     * delete method.
     * @param model
     *     model.
     */
    void delete(T model);

    /**
     * save status.
     * @param model
     *     model.
     */
    void saveStatus(T model);
    
    /**
     * update status.
     * @param model
     *    model.
     */
    void updateStatus(T model);
    
    /**
     * delete status.
     * @param model
     *    model.
     */
    void deleteStatus(T model);
    
    /**
     * save the relation between entity and other object.
     * @param outerModel
     *    outer object.
     */
    void saveRel(BaseModel outerModel);
    
    /**
     * update the relation between entity and other object.
     * @param outerModel
     *    outer object.
     */
    void updateRel(BaseModel outerModel);
    
    /**
     * delete the relation between entity and other object
     * @param outerModel
     *    outer object.
     */
    void deleteRel(BaseModel outerModel);
    
    /**
     * save model list, as nested models of other Model.
     * @param outerModel
     *    outer object.
     * @param models
     *     object list.
     */
    void saveAsInnerObjects(BaseModel outerModel, List<DTO> models);

    /**
     * update model list, as nested models of other Model.
     * @param outerModel
     *     outer object.
     * @param models
     *    object list.
     */
    void updateAsInnerObjects(BaseModel outerModel, List<DTO> models);
    
    /**
     * delete model list, as nested models of other Model.
     * @param outerModel
     *   outer object.
     */
    void deleteAsInnerObjects(BaseModel outerModel);
    
}
