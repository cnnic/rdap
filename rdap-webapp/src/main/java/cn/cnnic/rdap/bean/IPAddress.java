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
     * @see IpVersion.
     */
    private IpVersion ipVersion;
    /**
     * an identifier assigned to the ipAddress.
     */
    private String ipID;

    /**
     * ip version:v4,v6.
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
        V4("v4"), V6("v6");
        /**
         * a string signifying the IP protocol version of the network: "v4"
         * signifying an IPv4 network, "v6" signifying an IPv6 network.
         */
        private String name;

        /**
         * check ip version string is ipv4.
         * 
         * @param ipVersionStr
         *            ip version string.
         * @return true if is, false if not.
         */
        public static boolean isV4(String ipVersionStr) {
            if (V4.getName().equals(ipVersionStr)) {
                return true;
            }
            return false;
        }

        /**
         * check ip version string is ipv6.
         * 
         * @param ipVersionStr
         *            ip version string.
         * @return true if is, false if not.
         */
        public static boolean isV6(String ipVersionStr) {
            if (V6.getName().equals(ipVersionStr)) {
                return true;
            }
            return false;
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
        public String getName() {
            return name;
        }
    }

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
     * get ID.
     * 
     * @return ID.
     */
    public String getID() {
        return ipID;
    }

    /**
     * set id.
     * 
     * @param id
     *            for nameserver.
     */
    public void setID(String id) {
        this.ipID = id;
    }
}
