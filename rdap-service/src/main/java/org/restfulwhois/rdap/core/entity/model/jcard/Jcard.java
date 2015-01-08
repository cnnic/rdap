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
package org.restfulwhois.rdap.core.entity.model.jcard;

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardAddressConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardEmailConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardFnConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardKindConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardLangConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardOrgConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardTelephoneConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardTitleConverter;
import org.restfulwhois.rdap.core.entity.model.jcard.jcardconverter.JcardUrlConverter;

import ezvcard.Ezvcard;
import ezvcard.VCard;

/**
 * This class is used to convert <a href='http://tools.ietf.org/html/rfc6350'>
 * VCARD</a> to JSON string, see <a
 * href='http://tools.ietf.org/html/draft-ietf-jcardcal-jcard-07'>
 * draft-ietf-jcardcal-jcard</a>.
 * <p>
 * This class use <a href='https://code.google.com/p/ez-vcard'>ezvcard</a> to
 * convert VCARD to JSON.
 * 
 * <p>
 * property will not be write to JSON if exception occurred.
 * 
 * @author jiashuo
 * 
 */
public final class Jcard {
    /**
     * entity, datasource of VCARD.
     */
    private Entity entity;
    /**
     * VCARD property converters.
     */
    private List<JcardPropertyConverter> converters;

    /**
     * factory method.
     * 
     * @param entity
     *            entity.
     * @return Jcard.
     */
    public static Jcard build(Entity entity) {
        return new Jcard(entity);
    }

    /**
     * constructor.
     * 
     * @param entity
     *            entity.
     */
    private Jcard(Entity entity) {
        super();
        this.entity = entity;
        this.initPropertyConverters();
    }

    /**
     * initialize converters.
     * 
     * <pre>
     * WARN: the order of the converters IS the order of the JSON outputs, 
     * so it SHOULD not be changed.
     * </pre>
     */
    private void initPropertyConverters() {
        converters = new ArrayList<JcardPropertyConverter>();
        converters.add(new JcardKindConverter());
        converters.add(new JcardFnConverter());
        converters.add(new JcardAddressConverter());
        converters.add(new JcardTelephoneConverter());
        converters.add(new JcardEmailConverter());
        converters.add(new JcardTitleConverter());
        converters.add(new JcardOrgConverter());
        converters.add(new JcardUrlConverter());
        converters.add(new JcardLangConverter());
    }

    /**
     * convert VCARD to JSON.
     * 
     * @return JSON string.
     */
    public String toJSON() {
        VCard vcard = new VCard();
        if (null == entity) {
            return this.writeJSON(vcard);
        }
        for (JcardPropertyConverter converter : converters) {
            converter.convertAndSetProperty(vcard, entity);
        }
        if (!vcard.iterator().hasNext()) {
            return null;
        }
        return this.writeJSON(vcard);
    }

    /**
     * write JSON.
     * 
     * @param vcard
     *            VCARD.
     * @return JSON string.
     */
    private String writeJSON(VCard vcard) {
        return Ezvcard.writeJson(vcard).prodId(false).go();
    }

}
