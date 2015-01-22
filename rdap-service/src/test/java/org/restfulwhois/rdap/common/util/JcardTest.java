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
package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.EntityAddress;
import org.restfulwhois.rdap.common.model.EntityTelephone;
import org.restfulwhois.rdap.core.entity.model.jcard.Jcard;

import ezvcard.Ezvcard;
import ezvcard.Ezvcard.ParserChainJsonString;
import ezvcard.VCard;
import ezvcard.property.Kind;

/**
 * Test for Jcard util.
 * 
 * @author jiashuo
 * 
 */
public class JcardTest {
    /**
     * test toJcardString.
     */
    @Test
    public void testToJcardString() {
        Entity entity = null;
        entity = new Entity();
        entity.setFn("Jonathan Doe");
        entity.setKind(Kind.INDIVIDUAL);
        List<EntityAddress> addresses = new ArrayList<EntityAddress>();
        EntityAddress address = new EntityAddress();
        addresses.add(address);
        address.setPref(1);
        address.setTypes("home");
        address.setPoBox("post office Box");
        address.setExtendedAddress("apartment addr");
        address.setStreetAddress("123 Wall St.");
        address.setLocality("New York");
        address.setRegion("NY");
        address.setPostalCode("12345");
        address.setCountry("USA");
        entity.setAddresses(addresses);
        List<EntityTelephone> telephones = new ArrayList<EntityTelephone>();
        EntityTelephone entityTel = new EntityTelephone();
        telephones.add(entityTel);
        entityTel.setPref(1);
        telephones.add(EntityTelephone.buildTextTel("+9981-().", "998-()"));
        entity.setTelephones(telephones);
        entity.setEmail("johndoe@hotmail.com");
        entity.setTitle("CEO");
        entity.setOrg("org");
        entity.setUrl("http://www.acme-co.com");
        entity.setLang("zh_CN");
        String jcardString = Jcard.build(entity).toJSON();
        ParserChainJsonString e = Ezvcard.parseJson(jcardString);
        List<VCard> list = e.all();
        System.err.println(Ezvcard.write(list.get(0)).prodId(false).go());
        System.err.println(jcardString);
        assertNotNull(jcardString);
        assertThat(jcardString, new StringContains("zh_CN"));
        // invalid lang also can work
        entity.setLang("invalid_lang_value");
        jcardString = Jcard.build(entity).toJSON();
        assertNotNull(jcardString);
        assertThat(jcardString, new StringContains("invalid_lang_value"));
    }

    @Test
    public void testTel() {
        Entity entity = new Entity();
        List<EntityTelephone> telephones = new ArrayList<EntityTelephone>();
        EntityTelephone entityTel = new EntityTelephone();
        // valid tel
        telephones.add(entityTel);
        entityTel.setPref(1);
        telephones.add(EntityTelephone.buildTextTel("+9981-().", "998-()"));
        telephones.add(EntityTelephone.buildTextTel("+0981+-().", "998-()"));
        telephones.add(EntityTelephone.buildTextTel("+9981+-().", "+998-()"));
        telephones.add(EntityTelephone.buildTextTel("a9981-().", "998-()"));
        telephones.add(EntityTelephone.buildTextTel("@#-().", "998-()"));
        telephones.add(EntityTelephone.buildTextTel(" +9981-().", "998-()"));
        String telTypeStrs = "home;text";
        for(EntityTelephone tel:telephones){
            tel.setTypes(telTypeStrs);
        }
        entity.setTelephones(telephones);
        String jcardString = Jcard.build(entity).toJSON();
        assertNotNull(jcardString);
        assertThat(jcardString, new StringContains("+9981-()."));
        assertThat(jcardString, new StringContains("998-()"));
        assertThat(jcardString, new IsNot(new StringContains("+9981+-().")));
        assertThat(jcardString, new IsNot(new StringContains("+0981+-().")));
        assertThat(jcardString, new IsNot(new StringContains("+998-()")));
        assertThat(jcardString, new IsNot(new StringContains("a9981-().")));
        assertThat(jcardString, new IsNot(new StringContains("@#-().")));
        assertThat(jcardString, new IsNot(new StringContains(" +9981-().")));
    }
}
