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


/**
 * telephone number of entity.
 *
 * @author jiashuo
 *
 */
public class EntityTel extends BaseModel {

    /**
     * entity id.
     */
    private Long entityId;
    /**
     * pref.
     */
    private Integer pref;
    /**
     * tel types,splited by ';'.
     */
    private String types;
    /**
     * value.
     */
    private String value;

    /**
     * get entityId.
     *
     * @return entityId.
     */
    public Long getEntityId() {
        return entityId;
    }

    /**
     * set entityId.
     *
     * @param entityId
     *            entityId.
     */
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    /**
     * get pref.
     *
     * @return pref.
     */
    public Integer getPref() {
        return pref;
    }

    /**
     * set pref.
     *
     * @param pref
     *            pref.
     */
    public void setPref(Integer pref) {
        this.pref = pref;
    }

    /**
     * get types.
     *
     * @return types.
     */
    public String getTypes() {
        return types;
    }

    /**
     * set types.
     *
     * @param types
     *            types.
     */
    public void setTypes(String types) {
        this.types = types;
    }

    /**
     * get value.
     *
     * @return value.
     */
    public String getValue() {
        return value;
    }

    /**
     * set value.
     *
     * @param value
     *            value.
     */
    public void setValue(String value) {
        this.value = value;
    }

}
