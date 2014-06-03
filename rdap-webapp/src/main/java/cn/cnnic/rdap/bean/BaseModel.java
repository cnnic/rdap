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
package cn.cnnic.rdap.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * base class of all model. Model is designed according to <a
 * href="http://tools.ietf.org/id/draft-ietf-weirds-json-response-07.txt"
 * >draft-ietf-weirds-json-response</a>
 * 
 * @author jiashuo
 * 
 */
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(value = { "id", "objectType" })
public class BaseModel {
    /**
     * specifications used in the construction of the response
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
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }

    /**
     * get identity of object.
     * 
     * @return identity
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRdapConformance() {
        return rdapConformance;
    }

    public void setRdapConformance(List<String> rdapConformance) {
        this.rdapConformance = rdapConformance;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<Notice> getNotices() {
        return notices;
    }

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
}
