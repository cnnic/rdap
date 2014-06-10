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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntityAddress;
import ezvcard.VCard;
import ezvcard.parameter.TelephoneType;
import ezvcard.property.Kind;
import ezvcard.property.Telephone;
import ezvcard.util.TelUri;

/**
 * Test for Jcard util.
 *
 * @author jiashuo
 *
 */
public class JcardUtilTest {
    /**
     * test toJcardString.
     */
    @Test
    public void testToJcardString() {
        Entity entity = null;
        JcardUtil.toJcardString(entity);
        entity = new Entity();
        entity.setFn("Jonathan Doe");
        entity.setKind(Kind.INDIVIDUAL);
        List<EntityAddress> addresses = new ArrayList<EntityAddress>();
        EntityAddress address = new EntityAddress();
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
        System.err.println(JcardUtil.toJcardString(entity));
    }

    private static VCard createVCard() throws IOException {
        VCard vcard = new VCard();
        TelUri uri =
                new TelUri.Builder("+1-800-555-9876").extension("111").build();
        Telephone tel = new Telephone(uri);
        tel.addType(TelephoneType.WORK);
        TelephoneType t = TelephoneType.get("aaa");
        tel.setPref(1); // the most preferred
        vcard.addTelephoneNumber(tel);
        vcard.addEmail("johndoe@hotmail.com");
        vcard.addTitle("CEO");
        vcard.setOrganization("org").setType("work");
        vcard.addUrl("http://www.acme-co.com");
        return vcard;
    }
}
