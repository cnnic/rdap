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

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.model.IpVersion;

/**
 * 
 * @author jiashuo
 * 
 */
public class NetworkInBytes {

    /**
     * version of IP.
     */
    private IpVersion ipVersion;
    /**
     * startAddress byte value.
     */
    private byte[] startAddress;

    /**
     * endAddress byte value.
     */
    private byte[] endAddress;

    /**
     * constructor.
     * 
     * @param ipVersion
     *            ipVersion.
     * @param startAddress
     *            startAddress.
     * @param endAddress
     *            endAddress.
     */
    public NetworkInBytes(IpVersion ipVersion, byte[] startAddress,
            byte[] endAddress) {
        super();
        this.ipVersion = ipVersion;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
    }

    /**
     * get startAddress as string.
     * 
     * @return startAddress string.
     */
    public String getStartAddressAsString() {
        if (ipVersion.isV4()) {
            return IpV4.toString(this.getStartAddress());
        }
        if (ipVersion.isV6()) {
            return IpV6.toString(this.getStartAddress());
        }
        return StringUtils.EMPTY;
    }

    /**
     * get endAddress as string.
     * 
     * @return endAddress string.
     */
    public String getEndAddressAsString() {
        if (ipVersion.isV4()) {
            return IpV4.toString(this.getEndAddress());
        }
        if (ipVersion.isV6()) {
            return IpV6.toString(this.getEndAddress());
        }
        return StringUtils.EMPTY;
    }

    /**
     * get ipVersion.
     * 
     * @return ipVersion.
     */
    public IpVersion getIpVersion() {
        return ipVersion;
    }

    /**
     * set ipVersion.
     * 
     * @param ipVersion
     *            ipVersion.
     */
    public void setIpVersion(IpVersion ipVersion) {
        this.ipVersion = ipVersion;
    }

    /**
     * get startAddress.
     * 
     * @return startAddress.
     */
    public byte[] getStartAddress() {
        return startAddress;
    }

    /**
     * set startAddress.
     * 
     * @param startAddress
     *            startAddress.
     */
    public void setStartAddress(byte[] startAddress) {
        this.startAddress = startAddress;
    }

    /**
     * get endAddress.
     * 
     * @return endAddress.
     */
    public byte[] getEndAddress() {
        return endAddress;
    }

    /**
     * set endAddress.
     * 
     * @param endAddress
     *            endAddress.
     */
    public void setEndAddress(byte[] endAddress) {
        this.endAddress = endAddress;
    }

}
