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

import org.apache.commons.lang.StringUtils;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;

/**
 * ip v6 util.
 * 
 * @author jiashuo
 * 
 */
public class IpV6 {
    /**
     * max lengh
     */
    private static final int MAX_LENGTH_PER_SPLIT = 4;
    /**
     * hex char size is 32, that is 16 bytes.
     */
    private static final int HEX_CHAR_SIZE_V6 = 32;

    /**
     * get hex char size.
     * 
     * @return hex char size.
     */
    public static int getHexCharSize() {
        return HEX_CHAR_SIZE_V6;
    }

    /**
     * check if IPv6 is valid.
     * 
     * @param cidr
     *            IP.
     * @return true if valid, false if not.
     */
    public static boolean isValidIpV6(String cidr) {
        if (StringUtils.isBlank(cidr)) {
            return false;
        }
        if (!StringUtils.contains(cidr, "/")) {
            cidr = cidr + "/128";
        }
        String ipPrefix = StringUtils.substringBefore(cidr, "/");
        if (!ipSplitsLengthIsValid(ipPrefix)) {
            return false;
        }
        if (StringUtils.endsWith(ipPrefix, ":")
                && !StringUtils.endsWith(ipPrefix, "::")) {
            return false;
        }
        try {
            IPv6Network.fromString(cidr);
        } catch (Exception e) {
            return false;
        }
        boolean validExpends = isValidIpV6WithExpends(cidr, ipPrefix);
        if (!validExpends) {
            return false;
        }
        return true;
    }

    /**
     * check if IP V6 splits length is valid.
     * 
     * <pre>
     *      checkSplitsLength("1234::0"):true
     *      checkSplitsLength("12345::0"):false
     * </pre>
     * 
     * @param ipPrefix
     *            ipPrefix.
     * @return true if valid, false if not.
     */
    private static boolean ipSplitsLengthIsValid(String ipPrefix) {
        String[] splits = StringUtils.split(ipPrefix, ":");
        for (String split : splits) {
            if (StringUtils.contains(split, ".")) {
                break;
            }
            if (StringUtils.length(split) > MAX_LENGTH_PER_SPLIT) {
                return false;
            }
        }
        return true;
    }

    /**
     * check if is valid after expends ::.
     * 
     * @param cidr
     *            cidr.
     * @param ipPrefix
     *            ipPrefix.
     * @return true if valid, false if not.
     */
    private static boolean isValidIpV6WithExpends(String cidr, String ipPrefix) {
        String mask = StringUtils.substringAfter(cidr, "/");
        if (StringUtils.endsWith(ipPrefix, "::")) {
            String ipWithExpands = StringUtils.replace(ipPrefix, "::", "::0");
            try {
                IPv6Network.fromString(ipWithExpands + "/" + mask);
            } catch (Exception e) {
                return false;
            }
        }
        if (StringUtils.startsWith(ipPrefix, "::")) {
            String ipWithExpands = StringUtils.replace(ipPrefix, "::", "0::");
            try {
                IPv6Network.fromString(ipWithExpands + "/" + mask);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    /**
     * IP v6 bytes to string.
     * 
     * @param v6Bytes
     *            bytes.
     * @return string.
     */
    public static String toString(byte[] v6Bytes) {
        try {
            IPv6Address iPv6Address = IPv6Address.fromByteArray(v6Bytes);
            return iPv6Address.toString();
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * parse string IP to byte array.
     * 
     * @param ipPrefix
     *            ipPrefix.
     * @return bytes.
     */
    public static byte[] toByteArray(String ipPrefix) {
        IPv6Address iPv6Address = IPv6Address.fromString(ipPrefix);
        return iPv6Address.toByteArray();
    }

    public static void main(String[] args) {
        String[] splits = StringUtils.split("1:1::12345", ":");
        for (String split : splits) {
            System.err.println(StringUtils.length(split));
        }
    }
}
