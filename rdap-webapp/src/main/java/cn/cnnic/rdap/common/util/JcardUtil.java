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

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.parameter.AddressType;
import ezvcard.property.Address;

/**
 * 
 * @author jiashuo
 * 
 */
public class JcardUtil {
	public static void main(String[] args) throws Throwable {
		VCard vcard = createVCard();
		System.out.println(Ezvcard.writeJson(vcard).go());
	}

	private static VCard createVCard() throws IOException {
		VCard vcard = new VCard();
		// vcard.setKind(Kind.individual());
		// vcard.setGender(Gender.male());
		// vcard.addLanguage("en-US");
		// StructuredName n = new StructuredName();
		// n.setFamily("Doe");
		// n.setGiven("Jonathan");
		// n.addPrefix("Mr");
		// vcard.setStructuredName(n);
		// vcard.setFormattedName("Jonathan Doe");
		// vcard.setNickname("John", "Jonny");
		// vcard.addTitle("Widget Engineer");
		// vcard.setOrganization("Acme Co. Ltd.", "Widget Department");
		Address adr = new Address();
		adr.setStreetAddress("123 Wall St.");
		adr.setLocality("New York");
		adr.setRegion("NY");
		adr.setPostalCode("12345");
		adr.setCountry("USA");
		adr.setLabel("123 Wall St.\nNew York, NY 12345\nUSA");
		adr.addType(AddressType.WORK);
		vcard.addAddress(adr);
		//
		// adr = new Address();
		// adr.setStreetAddress("123 Main St.");
		// adr.setLocality("Albany");
		// adr.setRegion("NY");
		// adr.setPostalCode("54321");
		// adr.setCountry("USA");
		// adr.setLabel("123 Main St.\nAlbany, NY 54321\nUSA");
		// adr.addType(AddressType.HOME);
		// vcard.addAddress(adr);
		//
		// vcard.addTelephoneNumber("1-555-555-1234", TelephoneType.WORK);
		// vcard.addTelephoneNumber("1-555-555-5678", TelephoneType.WORK,
		// TelephoneType.CELL);
		//
		// vcard.addEmail("johndoe@hotmail.com", EmailType.HOME);
		// vcard.addEmail("doe.john@acme.com", EmailType.WORK);
		// vcard.addUrl("http://www.acme-co.com");
		// vcard.setCategories("widgetphile", "biker", "vCard expert");
		// vcard.setGeo(37.6, -95.67);
		// java.util.TimeZone tz = java.util.TimeZone
		// .getTimeZone("America/New_York");
		// vcard.setTimezone(new Timezone(tz));
		//
		// vcard.setUid(Uid.random());
		// vcard.setRevision(Revision.now());
		return vcard;
	}
}
