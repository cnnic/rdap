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

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.googlecode.ipv6.IPv6Address;

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
                strings[i] =
                        String.format("%x", (short) (((highBits << i
                                * twoByteSize) >>> twoByteSize
                                * (oneByteSize - 1)) & fourByteMask));
            } else {
                strings[i] =
                        String.format("%x", (short) (((lowBits << i
                                * twoByteSize) >>> twoByteSize
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
     * check if IP v4 string is valid.
     * 
     * @param str
     *            IP string(include *).
     * @return true if valid, false if not.
     */
    public static boolean isIpV4StrValid(String str) {
        Pattern pattern =
                Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|"
                        + "2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(str).matches();
    }

    /**
     * check if IP v4 whole string is valid.
     * 
     * @param str
     *            IP string(without *).
     * @return true if valid, false if not.
     */
    public static boolean isIpV4StrWholeValid(String str) {
        Pattern pattern =
                Pattern.compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + ")\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$");
        return pattern.matcher(str).matches();
    }

    /**
     * verify if IP v6 string is valid.
     * 
     * @param strIp
     *            IP string.
     * @return true if valid, false if not.
     */
    public static boolean isIpV6StrValid(String strIp) {
        return isIpV6RegexValid(strIp);
    }

    /**
     * verify if IP v6 string is valid by jar.
     * 
     * @param strIp
     *            IP string.
     * @return true if valid, false if not.
     */
    public static boolean isIpV6JarValid(String strIp) {
        try {
            IPv6Address v6Addr = IPv6Address.fromString(strIp);
            if (v6Addr != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * check if IP v6 string is valid by regex.
     * 
     * @param strIp
     *            IP string.
     * @return true if valid, false if not.
     */
    public static boolean isIpV6RegexValid(String strIp) {
        final String regexV6 =
                "^([\\da-fA-F]{1,4}:){6}"
                        + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                        + "^::([\\da-fA-F]{1,4}:){0,5}"
                        + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                        + "^([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,4}"
                        + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                        + "^([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,3}"
                        + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                        + "^([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,2}"
                        + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                        + "^([\\da-fA-F]{1,4}:){4}:([\\da-fA-F]{1,4}:){0,1}"
                        + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                        + "^([\\da-fA-F]{1,4}:){5}:"
                        + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                        + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                        + "^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$|"
                        + "^:((:[\\da-fA-F]{1,4}){1,7}|:)$|"
                        + "^[\\da-fA-F]{1,4}:((:[\\da-fA-F]{1,4}){1,6}|:)$|"
                        + "^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,5}|:)$|"
                        + "^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,4}|:)$|"
                        + "^([\\da-fA-F]{1,4}:){4}((:[\\da-fA-F]{1,4}){1,3}|:)$|"
                        + "^([\\da-fA-F]{1,4}:){5}((:[\\da-fA-F]{1,4}){1,2}|:)$|"
                        + "^([\\da-fA-F]{1,4}:){6}:([\\da-fA-F]{1,4})?$|"
                        + "^([\\da-fA-F]{1,4}:){7}:$";
        Pattern pattern = Pattern.compile(regexV6);
        boolean isRegular = pattern.matcher(strIp).matches();
        return isRegular;
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
    public static boolean isIpLongValid(String ipStr, boolean isV4) {
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

    /**
     * Ip converted to type long.
     * 
     * @param ipStr
     *            string of ip
     * @return ipLongArr[]
     */
    public static long[] ipToLong(String ipStr) {
        long[] ipLongArr = new long[2];
        final int leftShift3 = 24;
        final int leftShift2 = 16;
        final int leftShift1 = 8;
        if (ipStr.indexOf(".") >= 0) {
            String[] ip = ipStr.split("\\.");
            ipLongArr[1] =
                    (Long.parseLong(ip[0]) << leftShift3)
                            + (Long.parseLong(ip[1]) << leftShift2)
                            + (Long.parseLong(ip[2]) << leftShift1)
                            + Long.parseLong(ip[3]);
            ipLongArr[0] = 0;
        } else {
            return ipV6ToLong(ipStr);
        }
        return ipLongArr;
    }

    /**
     * Ip converted to type decimal.
     * 
     * @param ipStr
     *            string for IP
     * @return BigDecimal[]
     */
    public static BigDecimal[] ipToBigDecimal(String ipStr) {

        if (ipStr.indexOf(":") >= 0) {
            return ipV6ToBigDecimalJar(ipStr);
        } else {
            return ipV4ToDecimal(ipStr);
        }
    }

    /**
     * string of IpV4 converted to Decimal.
     * 
     * @param ipStr
     *            ipV4 string
     * @return BigDecimal[]
     */
    public static BigDecimal[] ipV4ToDecimal(String ipStr) {
        BigDecimal[] ipLongArr = new BigDecimal[2];
        String[] ip = ipStr.split("\\.");
        // trim ':'
        for (int i = 0; i < ip.length; ++i) {
            ip[i] = ip[i].replaceAll(":", "");
        }
        final int leftShift1 = 8;
        final int leftShift2 = leftShift1 * 2;
        final int leftShift3 = leftShift1 * 3;
        ipLongArr[1] =
                BigDecimal.valueOf((Long.parseLong(ip[0]) << leftShift3)
                        + (Long.parseLong(ip[1]) << leftShift2)
                        + (Long.parseLong(ip[2]) << leftShift1)
                        + Long.parseLong(ip[3]));
        ipLongArr[0] = BigDecimal.valueOf(0);
        return ipLongArr;
    }

    /**
     * string of IpV6 converted to long.
     * 
     * @param longip
     *            a ipv6 string
     * @return long[]
     */
    public static long[] ipV6ToLong(String longip) {
        String[] strings = expandShortNotation(longip).split(":");
        long[] longs = new long[strings.length];

        long high = 0L;
        long low = 0L;
        final int radix = 16;
        final int fieldSize = 4;
        for (int i = 0; i < strings.length; i++) {
            if (i >= 0 && i < fieldSize) {
                high |=
                        (Long.parseLong(strings[i], radix) << ((longs.length
                                - i - 1) * radix));
            } else {
                low |=
                        (Long.parseLong(strings[i], radix) << ((longs.length
                                - i - 1) * radix));
            }
        }
        longs[0] = high;
        if (longs.length > 1) {
            longs[1] = low;
        }
        return longs;
    }

    /**
     * string of IpV6 converted to Decimal,mainly use the jar.
     * 
     * @param strIp
     *            the IP string
     * @return BigDecimal[]
     */
    public static BigDecimal[] ipV6ToBigDecimalJar(String strIp) {
        BigDecimal[] decimalIp = new BigDecimal[2];
        final IPv6Address iPv6Address = IPv6Address.fromString(strIp);
        decimalIp = ipV6ToBigDecimal(iPv6Address.toLongString());
        return decimalIp;
    }

    /**
     * string of IpV6 converted to Decimal,mainly convert to string.
     * 
     * @param strIp
     *            the IP string
     * @return BigDecimal[]
     */
    public static BigDecimal[] ipV6ToBigDecimal(String strIp) {
        final int radix = 16;
        final int numMulti = 65536;
        final int filedEachSeg = 4;
        String[] strFields = expandShortNotation(strIp).split(":");
        if (strFields.length <= 0) {
            return null;
        }
        String strIpV4 = "";
        int nV6Fields = strFields.length;
        for (int i = 0; i < strFields.length; ++i) {
            if (strFields[i].indexOf(".") >= 0) {
                nV6Fields = i;
                strIpV4 = strFields[i];
                break;
            }
        }
        BigDecimal[] decimalIp = new BigDecimal[strFields.length];
        for (int i = 0; i < nV6Fields; i++) {
            long numIp = Long.parseLong(strFields[i], radix);
            BigDecimal numEachField = BigDecimal.valueOf(numIp);
            int iFieldNum = i % filedEachSeg;
            int iSegNum = i / filedEachSeg;

            BigDecimal numShift =
                    BigDecimal.valueOf(Math.pow(numMulti, (filedEachSeg
                            - iFieldNum - 1)));
            numEachField = numEachField.multiply(numShift);
            if (0 == iFieldNum) {
                decimalIp[iSegNum] = numEachField;
            } else {
                decimalIp[iSegNum] = decimalIp[iSegNum].add(numEachField);
            }
        }
        if (StringUtils.isNotBlank(strIpV4)) {
            BigDecimal[] ipV4Decimal = ipV4ToDecimal(strIpV4);
            int indexSeg = nV6Fields / filedEachSeg;
            decimalIp[indexSeg] = decimalIp[indexSeg].add(ipV4Decimal[0]);
            decimalIp[indexSeg] = decimalIp[indexSeg].add(ipV4Decimal[1]);
        }
        return decimalIp;
    }

    /**
     * The abbreviated IPv6 converted into a standard wording.
     * 
     * @param strIp
     *            ip string
     * @return ipv6String
     */
    public static String expandShortNotation(String strIp) {
        final String strDoubleColon = "::";
        final String strSingleColon = ":";
        final int allIpV6Colons = 7;
        int allColons = allIpV6Colons;
        if (!strIp.contains(strDoubleColon)) {
            return strIp;
        } else if (strIp.equals(strDoubleColon)) {
            return generateZeroes(allColons + 1);
        } else {
            final int numberOfColons = countOccurrences(strIp, ':');

            if (strIp.indexOf(".") >= 0) {
                allColons -= 1;
            }
            if (strIp.startsWith(strDoubleColon)) {
                return strIp.replace(strDoubleColon,
                        generateZeroes((allColons + 2) - numberOfColons));
            } else if (strIp.endsWith(strDoubleColon)) {
                return strIp.replace(strDoubleColon, strSingleColon
                        + generateZeroes((allColons + 2) - numberOfColons));
            } else {
                return strIp.replace(strDoubleColon, strSingleColon
                        + generateZeroes((allColons + 2 - 1) - numberOfColons));
            }
        }
    }

    /**
     * Generated IPv6 address 0.
     * 
     * @param number
     *            from number to string
     * @return ipv6String
     */
    public static String generateZeroes(int number) {
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < number; i++) {
            builder.append("0:");
        }

        return builder.toString();
    }

    /**
     * The record ipv6 address: Number of.
     * 
     * @param haystack
     *            the string
     * @param needle
     *            the split char
     * @return count
     */
    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }
}
