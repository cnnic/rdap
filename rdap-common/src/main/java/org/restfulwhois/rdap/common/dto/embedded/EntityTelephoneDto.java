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
package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * EntityTelephoneDto.
 * 
 * @author jiashuo.
 * 
 */
public class EntityTelephoneDto extends BaseDto {
    /**
     * pref.
     */
    private int pref;
    /**
     * types.
     */
    private String types;
    /**
     * number.
     */
    private String number;
    /**
     * extNumber.
     */
    private String extNumber;

    /**
     * get pref.
     * 
     * @return pref.
     */
    public int getPref() {
        return pref;
    }

    /**
     * set pref.
     * 
     * @param pref
     *            pref.
     */
    public void setPref(int pref) {
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
     * get number.
     * 
     * @return number.
     */
    public String getNumber() {
        return number;
    }

    /**
     * set number.
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
}
