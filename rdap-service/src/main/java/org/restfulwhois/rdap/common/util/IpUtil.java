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
import org.restfulwhois.rdap.common.util.SubnetUtils.SubnetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.ipv6.IPv6Network;

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
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(IpUtil.class);

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
     * @param ipVersion
     *            ipVersion.
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
     * @param ipVersion
     *            ipVersion.
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
     * ipToByteArray.
     * @param ipPrefix IP.
     * @return bytes.
     */
    public static byte[] ipToByteArray(String ipPrefix) {
        IpVersion ipVersion = getIpVersionOfIp(ipPrefix);
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

}
