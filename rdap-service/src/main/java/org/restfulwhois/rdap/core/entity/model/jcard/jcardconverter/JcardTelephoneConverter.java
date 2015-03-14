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
package org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.EntityTelephone;
import org.restfulwhois.rdap.core.entity.model.jcard.JcardPropertyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Telephone;
import ezvcard.util.TelUri;
import ezvcard.util.TelUri.Builder;

/**
 * JcardTelephoneConverter.
 * 
 * @author jiashuo
 * 
 */
public class JcardTelephoneConverter implements JcardPropertyConverter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JcardTelephoneConverter.class);

    @Override
    public void convertAndSetProperty(VCard vcard, Entity entity) {
        this.addTelsToVcard(vcard, entity);
    }

    /**
     * add telephone to VCARD.
     * 
     * @param vcard
     *            VCARD.
     * @param entity
     *            entity.
     */
    private void addTelsToVcard(VCard vcard, Entity entity) {
        List<EntityTelephone> telList = entity.getTelephones();
        if (null == telList) {
            return;
        }
        for (EntityTelephone tel : telList) {
            if (tel.isEmpty()) {
                continue;
            }
            addTelephoneToVcard(tel, vcard);
        }
    }

    /**
     * add PREF.
     * 
     * @param tel
     *            tel.
     * @param telephone
     *            telephone.
     */
    private void addPref(EntityTelephone tel, Telephone telephone) {
        if (null == tel.getPref()) {
            return;
        }
        try {
            telephone.setPref(tel.getPref());
        } catch (Exception e) {
            LOGGER.error("addPref error:{}. Not set pref:{}.", e.getMessage(),
                    tel.getPref());
        }
    }

    /**
     * build telephone URI.
     * 
     * @param tel
     *            tel.
     * @param vcard vcard.
     * @return TelUri list if tel number is valid, return null if not.
     */
    private void addTelephoneToVcard(EntityTelephone tel, VCard vcard) {
        if (tel.isEmpty()) {
            return;
        }
        try {
            Builder telBuilder = new TelUri.Builder(tel.getNumber());
            if (StringUtils.isNotBlank(tel.getExtNumber())) {
                telBuilder.extension(tel.getExtNumber());
            }
            TelUri telUri = telBuilder.build();
            Telephone telephone = new Telephone(telUri);
            List<TelephoneType> types = parseTelephoneTypes(tel.getTypes());
            for (TelephoneType type : types) {
                telephone.addType(type);
            }
            addPref(tel, telephone);
            vcard.addTelephoneNumber(telephone);
        } catch (Exception e) {
            LOGGER.error("addFaxTelToVcard error:{} tel:{}", e.getMessage(),
                    tel);
        }
    }

    /**
     * parseTelephoneTypes.
     * 
     * @param typesStr
     *            types String, split by ';'.
     * @return TelephoneType list.
     */
    private List<TelephoneType> parseTelephoneTypes(String typesStr) {
        List<TelephoneType> types = new ArrayList<TelephoneType>();
        String[] typeSplitStrArray = StringUtils.split(typesStr, ";");
        if(null == typeSplitStrArray){
            return types;
        }
        for (String typeSplit : typeSplitStrArray) {
            TelephoneType type = TelephoneType.find(typeSplit);
            if (null != type) {
                types.add(type);
            }
        }
        return types;
    }

}
