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
package org.restfulwhois.rdap.core.entity.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;

import ezvcard.parameter.TelephoneType;

/**
 * telephone number of entity.
 * 
 * @author jiashuo
 * 
 */
public class EntityTelephone extends BaseModel {
    /**
     * pref.
     */
    private Integer pref;
    /**
     * tel type.
     */
    private List<TelephoneType> types = new ArrayList<TelephoneType>();
    /**
     * globalNumber,eg:+1-555-555-1234.
     */
    private String number;
    /**
     * ext number,eg:102.
     */
    private String extNumber;

    /**
     * addTelephoneTypes.
     * 
     * @param typesStr
     *            types Str, split by ';'.
     */
    public EntityTelephone addTelephoneTypes(String typesStr) {
        if (StringUtils.isBlank(typesStr)) {
            return this;
        }
        String[] typeSplitStrArray = StringUtils.split(typesStr, ";");
        for (String typeSplit : typeSplitStrArray) {
            addTelephoneType(typeSplit);
        }
        return this;
    }

    /**
     * addTelephoneType.
     * 
     * @param typeStr
     *            typeStr.
     */
    public EntityTelephone addTelephoneType(String typeStr) {
        if (StringUtils.isBlank(typeStr)) {
            return this;
        }
        TelephoneType type = TelephoneType.find(typeStr);
        if (null == type) {
            return this;
        }
        getTypes().add(type);
        return this;
    }

    /**
     * build text tel.
     * 
     * @param number
     * @param extNumber
     * @return
     */
    public static EntityTelephone buildTextTel(String number, String extNumber) {
        EntityTelephone tel = build(number, extNumber);
        tel.getTypes().add(TelephoneType.TEXT);
        return tel;
    }

    /**
     * build fax tel.
     * 
     * @param number
     * @param extNumber
     * @return
     */
    public static EntityTelephone buildFaxTel(String number, String extNumber) {
        EntityTelephone tel = build(number, extNumber);
        tel.getTypes().add(TelephoneType.FAX);
        return tel;
    }

    /**
     * build tel.
     * 
     * @param number
     * @param extNumber
     * @return
     */
    public static EntityTelephone build(String number, String extNumber) {
        EntityTelephone tel = new EntityTelephone();
        tel.number = number;
        tel.extNumber = extNumber;
        return tel;
    }

    /**
     * check if is empty.
     * 
     * @return true if is empty, false if not.
     */
    public boolean isEmpty() {
        return StringUtils.isBlank(number);
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
     * getTypes.
     * 
     * @return types.
     */
    public List<TelephoneType> getTypes() {
        return types;
    }

    /**
     * set types.
     * 
     * @param types
     *            types.
     */
    public void setTypes(List<TelephoneType> types) {
        this.types = types;
    }

    /**
     * get number.
     * 
     * @return number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * set number..
     * 
     * @param number
     *            number.
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * get extNumber.
     * 
     * @return extNumber.
     */
    public String getExtNumber() {
        return extNumber;
    }

    /**
     * set extNumber.
     * 
     * @param extNumber
     *            extNumber.
     */
    public void setExtNumber(String extNumber) {
        this.extNumber = extNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(number).append(extNumber)
                .append(pref).toString();
    }

}
