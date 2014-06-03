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
package cn.cnnic.rdap.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * IPAddress registrations found in RIRs and is the expected response for the
 * "/nameserver/" query as defined by [I-D.ietf-weirds-rdap-query].
 * 
 * @author weijunkai
 * 
 */
public class IPAddress extends BaseModel {

    /**
     * the V4 IP address.
     */
    private List<String> ipAddressV4;

    /**
     * the V6 IP address.
     */
    private List<String> ipAddressV6;

    /**
     * an flag identify if the ipAddress existed.
     */
    @JsonIgnore
    private boolean ipExisted;

    /**
     * get ip Address.
     * 
     * @return ipAddress.
     */
    public List<String> getAddressV4() {
        return ipAddressV4;
    }

    /**
     * set ip Address V4.
     * 
     * @param ipAddressV4
     *            set the ipAddress V4.
     */
    @JsonProperty("v4")
    public void setAddressV4(List<String> ipAddressV4) {
        this.ipAddressV4 = ipAddressV4;
    }

    /**
     * get lowAddress.
     * 
     * @return lowAddress.
     */
    public List<String> getAddressV6() {
        return ipAddressV6;
    }

    /**
     * set ipAddressV6.
     * 
     * @param ipAddressV6
     *            ipAddressV6.
     */
    @JsonProperty("v6")
    public void setAddressV6(List<String> ipAddressV6) {
        this.ipAddressV6 = ipAddressV6;
    }

    /**
     * get ipExisted.
     * 
     * @return ipExisted.
     */
    public boolean getIpExisted() {
        return ipExisted;
    }

    /**
     * set ipExisted.
     * 
     * @param ipExisted
     *            for nameserver.
     */
    public void setIpExisted(boolean ipExisted) {
        this.ipExisted = ipExisted;
    }
}
