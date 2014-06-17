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
package cn.cnnic.rdap.common.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntityAddress;
import cn.cnnic.rdap.bean.EntityTel;
import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Address;
import ezvcard.property.Kind;
import ezvcard.property.Telephone;
import ezvcard.util.TelUri;
import ezvcard.util.TelUri.Builder;

/**
 * Jcard util, see draft-ietf-jcardcal-jcard.
 * 
 * @author jiashuo
 * 
 */
public final class JcardUtil {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JcardUtil.class);

    /**
     * default constructor.
     */
    private JcardUtil() {
        super();
    }

    /**
     * parse entity to jcard string.
     * 
     * @param entity
     *            entity.
     * @return jcard string.
     */
    public static String toJcardString(Entity entity) {
        VCard vcard = convertToVcard(entity);
        return Ezvcard.writeJson(vcard).prodId(false).go();
    }

    /**
     * convert entity to vcard.
     * 
     * @param entity
     *            entity.
     * @return vcard.
     */
    private static VCard convertToVcard(Entity entity) {
        VCard vcard = new VCard();
        if (null == entity) {
            return vcard;
        }
        if (StringUtils.isNotBlank(entity.getKind())) {
            vcard.setKind(new Kind(entity.getKind()));
        }
        if (StringUtils.isNotBlank(entity.getFn())) {
            vcard.setFormattedName(entity.getFn());
        }
        addAddressToVcard(vcard, entity);
        addTelsToVcard(vcard, entity);
        if (StringUtils.isNotBlank(entity.getEmail())) {
            vcard.addEmail(entity.getEmail());
        }
        if (StringUtils.isNotBlank(entity.getTitle())) {
            vcard.addTitle(entity.getTitle());
        }
        if (StringUtils.isNotBlank(entity.getOrg())) {
            vcard.setOrganization(entity.getOrg());
        }
        if (StringUtils.isNotBlank(entity.getUrl())) {
            vcard.addUrl(entity.getUrl());
        }
        return vcard;
    }

    /**
     * add telephone to vcard.
     * 
     * @param vcard
     *            vcard.
     * @param entity
     *            entity.
     */
    private static void addTelsToVcard(VCard vcard, Entity entity) {
        List<EntityTel> telList = entity.getTelephones();
        if (null == telList) {
            return;
        }
        for (EntityTel tel : telList) {
            if (StringUtils.isBlank(tel.getGlobalNumber())) {
                continue;
            }
            TelUri uri = safeBuildTelUri(tel);
            if (null == uri) {
                continue;
            }
            Telephone telephone = new Telephone(uri);
            addTelephoneTypes(tel.getTypes(), telephone);
            addPref(tel, telephone);
            vcard.addTelephoneNumber(telephone);
        }
    }

    /**
     * add pref.
     * 
     * @param tel
     *            tel.
     * @param telephone
     *            telephone.
     */
    private static void addPref(EntityTel tel, Telephone telephone) {
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
     * build telephone uri.
     * 
     * @param tel
     *            tel.
     * @return TelUri if tel number is valid, return null if not.
     */
    private static TelUri safeBuildTelUri(EntityTel tel) {
        try {
            Builder telBuilder = new TelUri.Builder(tel.getGlobalNumber());
            if (StringUtils.isNotBlank(tel.getExtNumber())) {
                telBuilder.extension(tel.getExtNumber());
            }
            return telBuilder.build();
        } catch (Exception e) {
            LOGGER.error("buildTelUri error:{} for tel:{}", e.getMessage(), tel);
        }
        return null;
    }

    /**
     * add telephone types to telephone.
     * 
     * @param types
     *            types.
     * @param telephone
     *            telephone.
     */
    private static void addTelephoneTypes(String types, Telephone telephone) {
        if (StringUtils.isBlank(types)) {
            return;
        }
        String[] typeStrArray = StringUtils.split(types, ";");
        if (null == typeStrArray) {
            return;
        }
        for (String typeStr : typeStrArray) {
            TelephoneType telType = TelephoneType.get(typeStr);
            telephone.addType(telType);
        }
    }

    /**
     * add addresses to vcard.
     * 
     * @param vcard
     *            vcard.
     * @param entity
     *            entity.
     */
    private static void addAddressToVcard(VCard vcard, Entity entity) {
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
            vcard.addAddress(address);
        }
    }

    /**
     * add addressTypes to address.
     * 
     * @param addressesStr
     *            addressesStr,splited by ';'.
     * @param address
     *            address.
     */
    private static void addAddressTypes(String addressesStr, Address address) {
        if (StringUtils.isBlank(addressesStr)) {
            return;
        }
        String[] addressStrArray = StringUtils.split(addressesStr, ";");
        if (null == addressStrArray) {
            return;
        }
        for (String addressStr : addressStrArray) {
            AddressType addressType = AddressType.get(addressStr);
            address.addType(addressType);
        }
    }

}
