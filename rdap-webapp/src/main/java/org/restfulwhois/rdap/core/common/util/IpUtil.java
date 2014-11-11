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

package org.restfulwhois.rdap.core.common.util;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.util.SubnetUtils.SubnetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonValue;
import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;
import com.googlecode.ipv6.IPv6NetworkMask;

/**
 * IP address validator and convert util.
 * <p>
 * Support IP v4 and IP v6.
 * <p>
 * This class contains methods to validate IP
 * address:isIpV4StrValid,isIpV6StrValid.
 * <p>
 * IP address is stored in number format, for fast comparing reason.This class
 * contains methods to convert IP address string to number, and number to
 * string:ipToLong, ipV6ToLong,longToIpV4,longToIpV6.
 * 
 * @author jiashuo
 * 
 */
public final class IpUtil {
    /**
     * dot char.
     */
    private static final char CHAR_DOT = '.';

    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(IpUtil.class);

    /**
     * the V4 IP address has 4 sections, like 192.0.0.in-addr.arpa .
     */
    private static final int BYTE_SIZE_IPV4 = 4;

    /**
     * Radix 16 .
     */
    public static final int RADIX_HEX = 16;

    /**
     * Radix 10 .
     */
    public static final int RADIX_DECIMAL = 10;
    /**
     * hex char size for v4.
     */
    private static final int hexCharSizeV4 = IpUtil
            .getHexCharSize(IpVersion.V4);
    /**
     * hex char size for v6.
     */
    private static final int hexCharSizeV6 = IpUtil
            .getHexCharSize(IpVersion.V6);

    /**
     * default constructor.
     */
    private IpUtil() {
        super();
    }

    /**
     * parse IP from IP str.
     * 
     * @param ip
     *            IP str.
     * @param ipVersion
     *            IP version.
     * @return IpInBytes.
     */
    public static IpInBytes parseIp(String ip, IpVersion ipVersion) {
        if (ipVersion.isNotValidIp()) {
            return null;
        }
        return new IpInBytes(ipVersion, ipToByteArray(ip, ipVersion));
    }

    /**
     * get hex char size.
     * 
     * @return hex char size.
     */
    public static int getHexCharSize(IpVersion ipVersion) {
        if (ipVersion.isV4()) {
            return IpV4.getHexCharSize();
        } else if (ipVersion.isV6()) {
            return IpV6.getHexCharSize();
        }
        return 0;
    }

    /**
     * get IP version of CIDR str.
     * 
     * @param cidr
     *            IP str.
     * @return IP version.
     */
    public static IpVersion getIpVersionOfNetwork(String cidr) {
        if (StringUtils.contains(cidr, "./")) {
            return IpVersion.INVALID;
        }
        if (IpV4.isValidIpV4(cidr)) {
            return IpVersion.V4;
        } else if (IpV6.isValidIpV6(cidr)) {
            return IpVersion.V6;
        }
        return IpVersion.INVALID;
    }

    /**
     * parse network from CIDR str.
     * 
     * @param cidr
     *            CIDR str.
     * @param ipVersion
     *            IP version.
     * @return NetworkInBytes.
     */
    public static NetworkInBytes parseNetwork(String cidr, IpVersion ipVersion) {
        if (ipVersion.isNotValidIp()) {
            return null;
        }
        if (ipVersion.isV4()) {
            return parseNetworkV4(cidr);
        }
        if (ipVersion.isV6()) {
            return parseNetworkV6(cidr);
        }
        return null;
    }

    /**
     * parse network for v6.
     * 
     * @param cidr
     *            cidr.
     * @return NetworkInBytes
     */
    public static NetworkInBytes parseNetworkV6(String cidr) {
        IPv6Network network = IPv6Network.fromString(cidr);
        NetworkInBytes result =
                new NetworkInBytes(IpVersion.V6, network.getFirst()
                        .toByteArray(), network.getLast().toByteArray());
        return result;
    }

    /**
     * parse network for v4.
     * 
     * @param cidr
     *            cidr.
     * @return NetworkInBytes
     */
    public static NetworkInBytes parseNetworkV4(String cidr) {
        SubnetUtils utils = new SubnetUtils(cidr);
        SubnetInfo info = utils.getInfo();
        NetworkInBytes result =
                new NetworkInBytes(IpVersion.V4, IpV4.toByteArray(info
                        .network()), IpV4.toByteArray(info.broadcast()));
        return result;
    }

    /**
     * parse string IP to byte array.
     * 
     * @param ipPrefix
     *            ipPrefix.
     * @return bytes.
     */
    public static byte[] ipToByteArray(String ipPrefix, IpVersion ipVersion) {
        if (null == ipVersion || ipVersion.isNotValidIp()) {
            return null;
        }
        if (ipVersion.isV4()) {
            return IpV4.toByteArray(ipPrefix);
        }
        if (ipVersion.isV6()) {
            return IpV6.toByteArray(ipPrefix);
        }
        return null;
    }

    /**
     * get IpVersion of IP.
     * 
     * @param ipPrefix
     *            ipPrefix.
     * @return IpVersion.
     */
    public static IpVersion getIpVersionOfIp(String ipPrefix) {
        if (StringUtils.isBlank(ipPrefix)) {
            return IpVersion.INVALID;
        }
        if (StringUtils.contains(ipPrefix, "/")) {
            return IpVersion.INVALID;
        }
        IpVersion ipVersion = getIpVersionOfNetwork(ipPrefix);
        return ipVersion;
    }

    /**
     * IP bytes to string.
     * 
     * @param bytes
     *            bytes.
     * @param ipVersion
     *            ipVersion.
     * @return string.
     */
    public static String toString(byte[] bytes, IpVersion ipVersion) {
        if (null == ipVersion) {
            return StringUtils.EMPTY;
        }
        if (ipVersion.isV4()) {
            return IpV4.toString(bytes);
        } else if (ipVersion.isV6()) {
            return IpV6.toString(bytes);
        }
        return StringUtils.EMPTY;
    }

    /**
     * decode a string to an Arpa.
     * 
     * @param name
     *            an arpa string.
     * @return Arpa.
     */
    public static NetworkInBytes parseArpa(String name) {
        LOGGER.debug("parseArpa:{}", name);
        if (StringUtils.isEmpty(name)) {
            return null;
        } else if (StringUtils.endsWith(name, DomainUtil.IPV4_ARPA_SUFFIX)) {
            return parseIpV4Arpa(name);
        } else if (StringUtils.endsWith(name, DomainUtil.IPV6_ARPA_SUFFIX)) {
            return parseIpV6Arpa(name);
        }

        return null;
    }

    /**
     * parse IPV4 ARPA domain to IP bytes.
     * 
     * @param name
     *            an ARPA string.
     * @return ARPA.
     */
    private static NetworkInBytes parseIpV4Arpa(String name) {
        LOGGER.debug("parseInAddrArpa, name:" + name);
        String arpa =
                StringUtils.removeEndIgnoreCase(name, CHAR_DOT
                        + DomainUtil.IPV4_ARPA_SUFFIX);
        arpa = StringUtils.reverseDelimited(arpa, CHAR_DOT);
        String[] arpaLabels = StringUtils.split(arpa, CHAR_DOT);
        int mask = arpaLabels.length * 8;
        String[] ipLabels = new String[BYTE_SIZE_IPV4];
        for (int i = 0; i < BYTE_SIZE_IPV4; i++) {
            if (i < arpaLabels.length) {
                ipLabels[i] = arpaLabels[i];
            } else {
                ipLabels[i] = "0";
            }
        }
        String networkStr = StringUtils.join(ipLabels, CHAR_DOT) + "/" + mask;
        return parseNetworkV4(networkStr);
    }

    /**
     * parse IPV6 ARPA domain to IP bytes.
     * 
     * @param name
     *            an ARPA string.
     * @return ARPA.
     */
    private static NetworkInBytes parseIpV6Arpa(String name) {
        LOGGER.debug("parseIp6Arpa, name:" + name);
        String arpa =
                StringUtils.removeEndIgnoreCase(name, CHAR_DOT
                        + DomainUtil.IPV6_ARPA_SUFFIX);
        arpa = StringUtils.remove(arpa, CHAR_DOT);
        String ip = StringUtils.reverse(arpa);
        String fullIp = StringUtils.rightPad(ip, 32, '0');
        byte[] startIpBytes = DatatypeConverter.parseHexBinary(fullIp);
        int networkMask = StringUtils.length(arpa) * 4;
        IPv6Address fromByteArray = IPv6Address.fromByteArray(startIpBytes);
        IPv6Network network =
                IPv6Network.fromAddressAndMask(fromByteArray,
                        IPv6NetworkMask.fromPrefixLength(networkMask));
        NetworkInBytes result =
                new NetworkInBytes(IpVersion.V6, network.getFirst()
                        .toByteArray(), network.getLast().toByteArray());
        return result;
    }

    /**
     * add network mask if not contains mask.
     * 
     * @param cidr
     *            cidr.
     * @return CIDR.
     */
    public static String addNetworkMaskIfNotContainsMask(String cidr) {
        if (StringUtils.contains(cidr, "/")) {
            return cidr;
        }
        IpVersion ipVersion = IpUtil.getIpVersionOfIp(cidr);
        if (ipVersion.isNotValidIp()) {
            return cidr;
        }
        if (ipVersion.isV4()) {
            cidr = cidr + "/32";
        }
        if (ipVersion.isV6()) {
            cidr = cidr + "/128";
        }
        return cidr;
    }

    /**
     * generate network range sql: v4 is 8, v6 is 32.
     * 
     * @param ipColumnName
     *            ipColumnName.
     * @param ipVersionColumnName
     *            ipVersionColumnName.
     * @return sql.
     */
    public static String generateNetworkRangeSql(String ipColumnName,
            String ipVersionColumnName) {
        String conditionTpl = "LENGTH(HEX(%s))= %s and %s='%s'";
        String conditionV4 =
                String.format(conditionTpl, ipColumnName, hexCharSizeV4,
                        ipVersionColumnName, IpVersion.V4.getName());
        String conditionV6 =
                String.format(conditionTpl, ipColumnName, hexCharSizeV6,
                        ipVersionColumnName, IpVersion.V6.getName());
        return "(" + conditionV4 + " or " + conditionV6 + ")";
    }

    /**
     * generate network version sql: v4 or v6 .
     * 
     * @param ipVersionColumnName
     *            ipVersionColumnName.
     * @return sql.
     */
    public static String generateNetworkVersionSql(String ipVersionColumnName) {
        String conditionTpl = "%s='%s'";
        String conditionV4 =
                String.format(conditionTpl, ipVersionColumnName,
                        IpVersion.V4.getName());
        String conditionV6 =
                String.format(conditionTpl, ipVersionColumnName,
                        IpVersion.V6.getName());
        return "(" + conditionV4 + " or " + conditionV6 + ")";
    }

    /**
     * IP version:v4,v6.
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
}
