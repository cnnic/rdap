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

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6Network;
import com.googlecode.ipv6.IPv6NetworkMask;

/**
 * arpa domain util.
 * 
 * @author jiashuo
 * 
 */
public final class ArpaUtil {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(ArpaUtil.class);
    /**
     * dot char.
     */
    private static final char CHAR_DOT = '.';

    /**
     * the V4 IP address has 4 sections, like 192.0.0.in-addr.arpa .
     */
    private static final int BYTE_SIZE_IPV4 = 4;

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
        return IpUtil.parseNetworkV4(networkStr);
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
}
