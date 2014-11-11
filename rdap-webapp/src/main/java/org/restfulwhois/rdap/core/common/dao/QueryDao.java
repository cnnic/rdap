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
package org.restfulwhois.rdap.core.common.dao;

import java.util.List;

import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.core.common.support.QueryParam;

/**
 * query dao interface.
 * <p>
 * each method return object of BaseModel, which can be converted to concrete
 * model by caller.
 * </p>
 * 
 * @param <T>
 *            object driving from BaseModel.
 * @author jiashuo
 * 
 */
public interface QueryDao<T extends BaseModel> {
    /**
     * query model object.
     * 
     * @param queryParam
     *            query parameter.
     * @return object, using base class BaseObject.
     */
    T query(QueryParam queryParam);

    /**
     * query model list, as nested models of other Model.
     * 
     * @param outerObjectId
     *            id of outer object.
     * @param outerModelType
     *            model type of outer object.
     * @return object list.
     */
    List<T> queryAsInnerObjects(Long outerObjectId, ModelType outerModelType);

    /**
     * query and set inner objects for search result.
     * 
     * @param result
     *            query result.
     */
    void queryAndSetInnerObjectsForSearch(List<T> result);

}
