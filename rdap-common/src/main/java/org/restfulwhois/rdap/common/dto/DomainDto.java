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
 * DomainDto.
 * 
 * @author jiashuo.
 * 
 */
public class DomainDto extends BaseDto {
    /**
     * ldhName.
     */
    private String ldhName;
    /**
     * unicodeName.
     */
    private String unicodeName;
    /**
     * type.
     */
    private String type;
    /**
     * networkHandle.
     */
    private String networkHandle;
    /**
     * nameservers.
     */
    private List<HandleDto> nameservers;
    /**
     * secureDNS.
     */
    private SecureDnsDto secureDNS;
    /**
     * publicIds.
     */
    private List<PublicIdDto> publicIds;
    /**
     * variants.
     */
    private List<VariantDto> variants = new ArrayList<VariantDto>();

    /**
     * get ldhName.
     * 
     * @return ldhName.
     */
    public String getLdhName() {
        return ldhName;
    }

    /**
     * set ldhName.
     * 
     * @param ldhName
     *            ldhName.
     */
    public void setLdhName(String ldhName) {
        this.ldhName = ldhName;
    }

    /**
     * get unicodeName.
     * 
     * @return unicodeName.
     */
    public String getUnicodeName() {
        return unicodeName;
    }

    /**
     * set unicodeName.
     * 
     * @param unicodeName
     *            unicodeName.
     */
    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
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
     * get networkHandle.
     * 
     * @return networkHandle.
     */
    public String getNetworkHandle() {
        return networkHandle;
    }

    /**
     * set networkHandle.
     * 
     * @param networkHandle
     *            networkHandle.
     */
    public void setNetworkHandle(String networkHandle) {
        this.networkHandle = networkHandle;
    }

    /**
     * get variants.
     * 
     * @return variants.
     */
    public List<VariantDto> getVariants() {
        return variants;
    }

    /**
     * set variants.
     * 
     * @param variants
     *            variants.
     */
    public void setVariants(List<VariantDto> variants) {
        this.variants = variants;
    }

    /**
     * get nameservers.
     * 
     * @return nameservers.
     */
    public List<HandleDto> getNameservers() {
        return nameservers;
    }

    /**
     * set nameservers.
     * 
     * @param nameservers
     *            nameservers.
     */
    public void setNameservers(List<HandleDto> nameservers) {
        this.nameservers = nameservers;
    }

    /**
     * get secureDNS.
     * 
     * @return secureDNS.
     */
    public SecureDnsDto getSecureDNS() {
        return secureDNS;
    }

    /**
     * set secureDNS.
     * 
     * @param secureDNS
     *            secureDNS.
     */
    public void setSecureDNS(SecureDnsDto secureDNS) {
        this.secureDNS = secureDNS;
    }

    /**
     * get publicIds.
     * 
     * @return publicIds.
     */
    public List<PublicIdDto> getPublicIds() {
        return publicIds;
    }

    /**
     * set publicIds.
     * 
     * @param publicIds
     *            publicIds.
     */
    public void setPublicIds(List<PublicIdDto> publicIds) {
        this.publicIds = publicIds;
    }

    @Override
    public String toString() {
        return super.toString();
    }

}
