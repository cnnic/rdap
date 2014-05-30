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
package cn.cnnic.rdap.dao;

import java.util.List;

import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.QueryParam;

/**
 * query dao interface. Each method return BaseObject, which can be converted to
 * model class by caller.
 * 
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
     * * query model list, as nested models of other Model.
     * 
     * @param outerModelId
     *            id of outer object
     * @param outerModelType
     *            model type of outer object
     * @return object list.
     */
    List<T> queryAsInnerObjects(Long outerObjectId, ModelType outerModelType);

    /**
     * search model list.
     * 
     * @param queryParam
     *            queryParam.
     * @return object list.
     */
    List<T> search(QueryParam queryParam);

    /**
     * get search count.
     * 
     * @param queryParam
     *            queryParam.
     * @return queryParam.
     */
    Long searchCount(QueryParam queryParam);

}
