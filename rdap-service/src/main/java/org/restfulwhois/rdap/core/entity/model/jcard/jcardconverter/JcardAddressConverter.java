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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.EntityAddress;
import org.restfulwhois.rdap.core.entity.model.jcard.JcardPropertyConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.property.Address;

/**
 * JcardAddressConverter.
 * 
 * @author jiashuo
 * 
 */
public class JcardAddressConverter implements JcardPropertyConverter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JcardAddressConverter.class);

    @Override
    public void convertAndSetProperty(VCard vcard, Entity entity) {
        this.addAddressToVcard(vcard, entity);
    }

    /**
     * add addresses to VCARD.
     * 
     * @param vcard
     *            VCARD.
     * @param entity
     *            entity.
     */
    private void addAddressToVcard(VCard vcard, Entity entity) {
        List<EntityAddress> addressList = entity.getAddresses();
        if (null == addressList) {
            return;
        }
        for (EntityAddress entityAddress : addressList) {
            Address address = new Address();
            address.setPoBox(entityAddress.getPoBox());
            address.setExtendedAddress(entityAddress.getExtendedAddress());
            address.setStreetAddress(entityAddress.getStreetAddress());
            address.setLocality(entityAddress.getLocality());
            address.setRegion(entityAddress.getRegion());
            address.setPostalCode(entityAddress.getPostalCode());
            address.setCountry(entityAddress.getCountry());
            addAddressTypes(entityAddress.getTypes(), address);
            setAddressPref(entityAddress, address);
            vcard.addAddress(address);
        }
    }

    /**
     * set address PREF.
     * 
     * @param entityAddress
     *            entityAddress.
     * @param address
     *            address.
     */
    private void setAddressPref(EntityAddress entityAddress, Address address) {
        if (null == entityAddress.getPref()) {
            return;
        }
        try {
            address.setPref(entityAddress.getPref());
        } catch (Exception e) {
            LOGGER.error("setAddressPref error:{}. Not set pref:{}.",
                    e.getMessage(), entityAddress.getPref());
        }
    }

    /**
     * add addressTypes to address.
     * 
     * @param addressTypesStr
     *            addressesStr,splitted by ';'.
     * @param address
     *            address.
     */
    private void addAddressTypes(String addressTypesStr, Address address) {
        if (StringUtils.isBlank(addressTypesStr)) {
            return;
        }
        String[] addressTypeStrArray = StringUtils.split(addressTypesStr, ";");
        if (null == addressTypeStrArray) {
            return;
        }
        for (String addressStr : addressTypeStrArray) {
            AddressType addressType = AddressType.get(addressStr);
            address.addType(addressType);
        }
    }

}
