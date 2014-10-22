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
package org.restfulwhois.rdap.core.model;

import java.util.List;

import org.restfulwhois.rdap.core.model.base.BaseCustomModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * base class of all model. Model is designed according to <a
 * href="http://tools.ietf.org/id/draft-ietf-weirds-json-response-07.txt"
 * >draft-ietf-weirds-json-response</a>
 * 
 * @author jiashuo
 * 
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(value = {
        "id", "objectType" })
public class BaseModel {
    /**
     * specifications used in the construction of the response.
     */
    private List<String> rdapConformance;
    /**
     * identity of object.
     */
    private Long id;
    /**
     * DNRs and RIRs have registry-unique identifiers that may be used to
     * specifically reference an object instance.
     */
    private String handle;

    /**
     * language identifier, as described by [RFC5646].
     */
    private String lang;

    /**
     * notice.
     */
    private List<Notice> notices;

    /**
     * customModel.
     * 
     * <pre>
     *      Position for Properties in customModel is at last.
     * </pre>
     */
    @JsonUnwrapped
    private BaseCustomModel customModel;

    /**
     * find object from list by id.
     * 
     * @param baseModelObjects
     *            ModelObject list.
     * @param id
     *            id.
     * @return model object if find, null if not.
     */
    public static BaseModel findObjectFromListById(
            List<? extends BaseModel> baseModelObjects, Long id) {
        if (null == id || null == baseModelObjects) {
            return null;
        }
        for (BaseModel modelObject : baseModelObjects) {
            if (id.equals(modelObject.getId())) {
                return modelObject;
            }
        }
        return null;
    }

    /**
     * get model type, value is simple name of class.
     * 
     * @return simple name of class
     */
    public ModelType getObjectType() {
        return ModelType.BASE;
    }

    /**
     * get the uri of object.
     * 
     * @return simple name of class
     */
    @JsonIgnore
    public QueryUri getUri() {        
        throw new UnsupportedOperationException(
                "must be inmplemented in sub class if I'am called.");      
    }
    
    /**
     * get the value of href in the link.
     * 
     * @return string
     */
    public String generateLinkHref() {
        throw new UnsupportedOperationException(
            "must be inmplemented in sub class if I'am called.");
    }

    /**
     * get identity of object.
     * 
     * @return identity
     */
    public Long getId() {
        return id;
    }

    /**
     * setId.
     * 
     * @param id
     *            for a long id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * getRdapConformance.
     * 
     * @return a list string of rdapConformance.
     */
    public List<String> getRdapConformance() {
        return rdapConformance;
    }

    /**
     * setRdapConformance.
     * 
     * @param rdapConformance
     *            for a list string of rdapConformance to set.
     */
    public void setRdapConformance(List<String> rdapConformance) {
        this.rdapConformance = rdapConformance;
    }

    /**
     * getLang.
     * 
     * @return a string of language to get.
     */
    public String getLang() {
        return lang;
    }

    /**
     * setLang.
     * 
     * @param lang
     *            a string of language to set.
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * getNotices.
     * 
     * @return list of notice object to get.
     */
    public List<Notice> getNotices() {
        return notices;
    }

    /**
     * setNotices.
     * 
     * @param notices
     *            a list of notice to set.
     */
    public void setNotices(List<Notice> notices) {
        this.notices = notices;
    }

    /**
     * get handle.
     * 
     * @return handle.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * set handle.
     * 
     * @param handle
     *            handle.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * get customModel.
     * 
     * @return customModel.
     */
    public BaseCustomModel getCustomModel() {
        return customModel;
    }

    /**
     * set customModel.
     * 
     * @param customModel
     *            customModel.
     */
    public void setCustomModel(BaseCustomModel customModel) {
        this.customModel = customModel;
    }

}
