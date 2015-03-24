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
 * IpDto.
 * 
 * @author jiashuo.
 * 
 */
public class IpDto extends BaseDto {
    /**
     * startAddress.
     */
    private String startAddress;
    /**
     * endAddress as described by [//].
     */
    private String endAddress;
    /**
     * represents the IPVersion.
     */
    private String ipVersion;
    /**
     * represents the name.
     */
    private String name;
    /**
     * represents the type.
     */
    private String type;
    /**
     * represents the country.
     */
    private String country;
    /**
     * represents the parentHandle.
     */
    private String parentHandle;

    /**
     * network name cidr.
     */
    private String cidr;

    /**
     * get startAddress.
     * 
     * @return startAddress.
     */
    public String getStartAddress() {
        return startAddress;
    }

    /**
     * set startAddress.
     * 
     * @param startAddress
     *            startAddress.
     */
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    /**
     * get endAddress.
     * 
     * @return endAddress.
     */
    public String getEndAddress() {
        return endAddress;
    }

    /**
     * set endAddress.
     * 
     * @param endAddress
     *            endAddress.
     */
    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    /**
     * get ipVersion.
     * 
     * @return ipVersion.
     */
    public String getIpVersion() {
        return ipVersion;
    }

    /**
     * set ipVersion.
     * 
     * @param ipVersion
     *            ipVersion.
     */
    public void setIpVersion(String ipVersion) {
        this.ipVersion = ipVersion;
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

    /**
     * get parentHandle.
     * 
     * @return parentHandle.
     */
    public String getParentHandle() {
        return parentHandle;
    }

    /**
     * set parentHandle.
     * 
     * @param parentHandle
     *            parentHandle.
     */
    public void setParentHandle(String parentHandle) {
        this.parentHandle = parentHandle;
    }

    /**
     * get cidr.
     * 
     * @return cidr.
     */
    public String getCidr() {
        return cidr;
    }

    /**
     * set cidr.
     * 
     * @param cidr
     *            cidr.
     */
    public void setCidr(String cidr) {
        this.cidr = cidr;
    }
}
