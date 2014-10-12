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

import java.nio.ByteBuffer;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.util.SubnetUtils.SubnetInfo;

/**
 * ip v4 util.
 * 
 * @author jiashuo
 * 
 */
public class IpV4 {

    /**
     * dot separator.
     */
    private static final String DOT_SEPARATOR = ".";
    /**
     * v4 bytes size.
     */
    private static final int N_BYTES_V4 = 4;

    /**
     * check if IPv4 is valid.
     * 
     * @param cidr
     *            IP.
     * @return true if valid, false if not.
     */
    public static boolean isValidIpV4(String cidr) {
        if (StringUtils.isBlank(cidr)) {
            return false;
        }
        cidr = addDefaultMaskIfNotExist(cidr);
        try {
            new SubnetUtils(cidr);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private static String addDefaultMaskIfNotExist(String cidr) {
        if (!StringUtils.contains(cidr, "/")) {
            cidr = cidr + "/32";
        }
        return cidr;
    }

    /**
     * IP v4 bytes to string.
     * 
     * @param v4Bytes
     *            bytes.
     * @return string.
     */
    public static String toString(byte[] v4Bytes) {
        if (null == v4Bytes) {
            return StringUtils.EMPTY;
        }
        if (v4Bytes.length < 4) {
            return StringUtils.EMPTY;
        }
        try {
            return (v4Bytes[0] & 0xff) + DOT_SEPARATOR + (v4Bytes[1] & 0xff)
                    + DOT_SEPARATOR + (v4Bytes[2] & 0xff) + DOT_SEPARATOR
                    + (v4Bytes[3] & 0xff);
        } catch (Exception e) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * parse int IP to byte array.
     * 
     * @param value
     * @return byte[].
     */
    public static byte[] toByteArray(int value) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(N_BYTES_V4).putInt(value);
        return byteBuffer.array();
    }

    /**
     * parse string IP to byte array.
     * 
     * @param ipPrefix
     *            ipPrefix.
     * @return bytes.
     */
    public static byte[] toByteArray(String ipPrefix) {
        String cidr = addDefaultMaskIfNotExist(ipPrefix);
        SubnetUtils subnet = new SubnetUtils(cidr);
        SubnetInfo info = subnet.getInfo();
        return IpV4.toByteArray(info.address());
    }

}
