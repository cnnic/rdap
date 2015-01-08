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
package org.restfulwhois.rdap.common.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * IpVersion.
 * 
 * @author jiashuo
 * 
 */
public enum IpVersion {
    /**
     * The representation of IPv4 addresses in this document uses the
     * dotted-decimal notation described in [RFC1166]. The representation of
     * IPv6 addresses in this document follow the forms outlined in
     * [RFC5952].
     */
    INVALID("invalid"), V4("v4"), V6("v6");
    /**
     * a string signifying the IP protocol version of the network: "v4"
     * signifying an IPv4 network, "v6" signifying an IPv6 network.
     */
    private String name;

    /**
     * check if is ipv6.
     * 
     * @return true if is, false if not.
     */
    public boolean isV4() {
        return V4.equals(this);
    }

    /**
     * check if is ipv6.
     * 
     * @return true if is, false if not.
     */
    public boolean isV6() {
        return V6.equals(this);
    }

    /**
     * check if is invalid.
     * 
     * @return true if is invalid, false if not.
     */
    public boolean isNotValidIp() {
        return INVALID.equals(this);
    }

    /**
     * default constructor.
     * 
     * @param name
     *            ip version name.
     */
    private IpVersion(String name) {
        this.name = name;
    }

    /**
     * get name.
     * 
     * @return name.
     */
    @JsonValue
    public String getName() {
        return name;
    }

    /**
     * get IpVersion by name.
     * 
     * @param name
     *            name.
     * @return IpVersion if name is valid, null if not
     */
    public static IpVersion getIpVersion(String name) {
        IpVersion[] ipVersions = IpVersion.values();
        for (IpVersion ipVersion : ipVersions) {
            if (ipVersion.getName().equalsIgnoreCase(name)) {
                return ipVersion;
            }
        }
        return null;
    }

}
