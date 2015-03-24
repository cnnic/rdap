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
package org.restfulwhois.rdap.common.dto;

/**
 * AutnumDto.
 * 
 * @author jiashuo.
 * 
 */
public class AutnumDto extends BaseDto {
    /**
     * startAutnum.
     */
    private Long startAutnum;
    /**
     * endAutnum.
     */
    private Long endAutnum;
    /**
     * name.
     */
    private String name;
    /**
     * type.
     */
    private String type;
    /**
     * country.
     */
    private String country;

    /**
     * get startAutnum.
     * 
     * @return startAutnum.
     */
    public Long getStartAutnum() {
        return startAutnum;
    }

    /**
     * set startAutnum.
     * 
     * @param startAutnum
     *            startAutnum.
     */
    public void setStartAutnum(Long startAutnum) {
        this.startAutnum = startAutnum;
    }

    /**
     * get endAutnum.
     * 
     * @return endAutnum.
     */
    public Long getEndAutnum() {
        return endAutnum;
    }

    /**
     * set endAutnum.
     * 
     * @param endAutnum
     *            endAutnum.
     */
    public void setEndAutnum(Long endAutnum) {
        this.endAutnum = endAutnum;
    }

    /**
     * get name.
     * 
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * set name.
     * 
     * @param name
     *            name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get type.
     * 
     * @return type.
     */
    public String getType() {
        return type;
    }

    /**
     * set type.
     * 
     * @param type
     *            type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get country.
     * 
     * @return country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * set country.
     * 
     * @param country
     *            country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

}
