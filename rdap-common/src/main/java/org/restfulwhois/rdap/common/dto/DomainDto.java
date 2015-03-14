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

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.common.dto.embedded.HandleDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantDto;

/**
 * 
 * @author jiashuo
 * 
 */
public class DomainDto extends BaseDto {
    private String ldhName;
    private String unicodeName;
    private String type;
    private String networkHandle;
    private List<HandleDto> nameservers;
    private SecureDnsDto secureDNS;
    private List<PublicIdDto> publicIds;
    /**
     * variants.
     */
    private List<VariantDto> variants = new ArrayList<VariantDto>();

    public String getLdhName() {
        return ldhName;
    }

    public void setLdhName(String ldhName) {
        this.ldhName = ldhName;
    }

    public String getUnicodeName() {
        return unicodeName;
    }

    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNetworkHandle() {
        return networkHandle;
    }

    public void setNetworkHandle(String networkHandle) {
        this.networkHandle = networkHandle;
    }

    public List<VariantDto> getVariants() {
        return variants;
    }

    public void setVariants(List<VariantDto> variants) {
        this.variants = variants;
    }

    public List<HandleDto> getNameservers() {
        return nameservers;
    }

    public void setNameservers(List<HandleDto> nameservers) {
        this.nameservers = nameservers;
    }

    public SecureDnsDto getSecureDNS() {
        return secureDNS;
    }

    public void setSecureDNS(SecureDnsDto secureDNS) {
        this.secureDNS = secureDNS;
    }

    public List<PublicIdDto> getPublicIds() {
        return publicIds;
    }

    public void setPublicIds(List<PublicIdDto> publicIds) {
        this.publicIds = publicIds;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
