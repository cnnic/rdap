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

package cn.cnnic.rdap.common.util;

import org.apache.commons.lang.StringUtils;

/**
 * ip util.
 * 
 * @author jiashuo
 * 
 */
public final class IpUtil {

    /**
     * default constructor.
     */
    private IpUtil() {
        super();
    }

    /**
     * convert ipV4 Long format to String.
     * 
     * @param longIp
     *            ipV4 long value.
     * @return ipv4 string.
     */
    public static String longToIpV4(long longIp) {
        final long numBeyond = 0xffffffffL;
        if (longIp > numBeyond) {
            return "";
        }
        final int threeByteSize = 24;
        final int threeByteMask = 0x00ffffff;
        final int twoByteSize = 16;
        final int twoByteMask = 0x0000ffff;
        final int oneByteSize = 8;
        final int oneByteMask = 0x000000ff;
        return String.format("%d.%d.%d.%d", (longIp >>> threeByteSize)
                & oneByteMask, (longIp & threeByteMask) >>> twoByteSize,
                (longIp & twoByteMask) >>> oneByteSize, longIp & oneByteMask);
    }

    /**
     * convert ipV6 Long format to String.
     * 
     * @param highBits
     *            high 64 bits long value.
     * @param lowBits
     *            low 64 bits long value.
     * @return ipv6 string.
     */
    public static String longToIpV6(long highBits, long lowBits) {
        final int oneByteSize = 8;
        final int v6MaxSegment = 8;
        final int twoByteSize = 16;
        final int fourByteMask = 0xFFFF;
        short[] shorts = new short[v6MaxSegment];
        String[] strings = new String[shorts.length];
        for (int i = 0; i < v6MaxSegment; i++) {
            if (i >= 0 && i < v6MaxSegment / 2) {
                strings[i] = String.format("%x",
                        (short) (((highBits << i * twoByteSize) >>> twoByteSize
                                * (oneByteSize - 1)) & fourByteMask));
            } else {
                strings[i] = String.format("%x",
                        (short) (((lowBits << i * twoByteSize) >>> twoByteSize
                                * (oneByteSize - 1)) & fourByteMask));
            }
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            result.append(strings[i]);
            if (i < strings.length - 1) {
                result.append(":");
            }
        }
        return result.toString();
    }

    /**
     * check if ip string is valid.
     * 
     * @param ipStr
     *            ip string.
     * @param isV4
     *            if the ip type is V4
     * 
     * @return true if valid, false if not.
     */
    public static boolean isIpValid(String ipStr, boolean isV4) {
        if (StringUtils.isBlank(ipStr)) {
            return false;
        }
        /**
         * ipLimitV6 2^64.
         */
        final String ipLimitV6 = "18446744073709551616";
        /**
         * ipLimitV4 2^32.
         */
        final String ipLimitV4 = "4294967296";

        String ipLimit = isV4 ? ipLimitV4 : ipLimitV6;
        if (ipStr.length() == ipLimit.length()) {
            if (ipStr.compareTo(ipLimit) < 0) {
                return true;
            }
        } else {
            if (ipStr.length() < ipLimit.length()) {
                return true;
            }
        }
        return false;
    }
}
