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

import com.googlecode.ipv6.IPv6Address;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * ip util.
 * 
 * @author jiashuo
 * 
 */
public final class IpUtil {

    public static long[] IPV4Array = { 0x80000000l, // 1000 0000 0000 0000 0000
            // 0000 0000 0000,//1
            0xC0000000l, // 1100 0000 0000 0000 0000 0000 0000 0000,//2
            0xE0000000l, // 1110 0000 0000 0000 0000 0000 0000 0000,//3
            0xF0000000l, // 1111 0000 0000 0000 0000 0000 0000 0000,//4
            0xF8000000l, // 1111 1000 0000 0000 0000 0000 0000 0000,//5
            0xFC000000l, // 1111 1100 0000 0000 0000 0000 0000 0000,//6
            0xFE000000l, // 1111 1110 0000 0000 0000 0000 0000 0000,//7
            0xFF000000l, // 1111 1111 0000 0000 0000 0000 0000 0000,//8
            0xFF800000l, // 1111 1111 1000 0000 0000 0000 0000 0000,//9
            0xFFC00000l, // 1111 1111 1100 0000 0000 0000 0000 0000,//10
            0xFFE00000l, // 1111 1111 1110 0000 0000 0000 0000 0000,//11
            0xFFF00000l, // 1111 1111 1111 0000 0000 0000 0000 0000,//12
            0xFFF80000l, // 1111 1111 1111 1000 0000 0000 0000 0000,//13
            0xFFFC0000l, // 1111 1111 1111 1100 0000 0000 0000 0000,//14
            0xFFFE0000l, // 1111 1111 1111 1110 0000 0000 0000 0000,//15
            0xFFFF0000l, // 1111 1111 1111 1111 0000 0000 0000 0000,//16
            0xFFFF8000l, // 1111 1111 1111 1111 1000 0000 0000 0000,//17
            0xFFFFC000l, // 1111 1111 1111 1111 1100 0000 0000 0000,//18
            0xFFFFE000l, // 1111 1111 1111 1111 1110 0000 0000 0000,//19
            0xFFFFF000l, // 1111 1111 1111 1111 1111 0000 0000 0000,//20
            0xFFFFF800l, // 1111 1111 1111 1111 1111 1000 0000 0000,//21
            0xFFFFFC00l, // 1111 1111 1111 1111 1111 1100 0000 0000,//22
            0xFFFFFE00l, // 1111 1111 1111 1111 1111 1110 0000 0000,//23
            0xFFFFFF00l, // 1111 1111 1111 1111 1111 1111 0000 0000,//24
            0xFFFFFF80l, // 1111 1111 1111 1111 1111 1111 1000 0000,//25
            0xFFFFFFC0l, // 1111 1111 1111 1111 1111 1111 1100 0000,//26
            0xFFFFFFE0l, // 1111 1111 1111 1111 1111 1111 1110 0000,//27
            0xFFFFFFF0l, // 1111 1111 1111 1111 1111 1111 1111 0000,//28
            0xFFFFFFF8l, // 1111 1111 1111 1111 1111 1111 1111 1000//29
            0xFFFFFFFCl, // 1111 1111 1111 1111 1111 1111 1111 1100//30
            0xFFFFFFFEl, // 1111 1111 1111 1111 1111 1111 1111 1110//31
            0xFFFFFFFFl // 1111 1111 1111 1111 1111 1111 1111 1111//32
            };

    public static long[] IPV6Array = { 0x8000000000000000l, // 1
            0xC000000000000000l, // 2
            0xE000000000000000l, // 3
            0xF000000000000000l, // 4
            0xF800000000000000l, // 5
            0xFC00000000000000l, // 6
            0xFE00000000000000l, // 7
            0xFF00000000000000l, // 8
            0xFF80000000000000l, // 9
            0xFFC0000000000000l, // 10
            0xFFE0000000000000l, // 11
            0xFFF0000000000000l, // 12
            0xFFF8000000000000l, // 13
            0xFFFC000000000000l, // 14
            0xFFFE000000000000l, // 15
            0xFFFF000000000000l, // 16
            0xFFFF800000000000l, // 17
            0xFFFFC00000000000l, // 18
            0xFFFFE00000000000l, // 19
            0xFFFFF00000000000l, // 20
            0xFFFFF80000000000l, // 21
            0xFFFFFC0000000000l, // 22
            0xFFFFFE0000000000l, // 23
            0xFFFFFF0000000000l, // 24
            0xFFFFFF8000000000l, // 25
            0xFFFFFFC000000000l, // 26
            0xFFFFFFE000000000l, // 27
            0xFFFFFFF000000000l, // 28
            0xFFFFFFF800000000l, // 29
            0xFFFFFFFC00000000l, // 30
            0xFFFFFFFE00000000l, // 31
            0xFFFFFFFF00000000l, // 32
            0xFFFFFFFF80000000l, // 33
            0xFFFFFFFFC0000000l, // 34
            0xFFFFFFFFE0000000l, // 35
            0xFFFFFFFFF0000000l, // 36
            0xFFFFFFFFF8000000l, // 37
            0xFFFFFFFFFC000000l, // 38
            0xFFFFFFFFFE000000l, // 39
            0xFFFFFFFFFF000000l, // 40
            0xFFFFFFFFFF800000l, // 41
            0xFFFFFFFFFFC00000l, // 42
            0xFFFFFFFFFFE00000l, // 43
            0xFFFFFFFFFFF00000l, // 44
            0xFFFFFFFFFFF80000l, // 45
            0xFFFFFFFFFFFC0000l, // 46
            0xFFFFFFFFFFFE0000l, // 47
            0xFFFFFFFFFFFF0000l, // 48
            0xFFFFFFFFFFFF8000l, // 49
            0xFFFFFFFFFFFFC000l, // 50
            0xFFFFFFFFFFFFE000l, // 51
            0xFFFFFFFFFFFFF000l, // 52
            0xFFFFFFFFFFFFF800l, // 53
            0xFFFFFFFFFFFFFC00l, // 54
            0xFFFFFFFFFFFFFE00l, // 55
            0xFFFFFFFFFFFFFF00l, // 56
            0xFFFFFFFFFFFFFF80l, // 57
            0xFFFFFFFFFFFFFFC0l, // 58
            0xFFFFFFFFFFFFFFE0l, // 59
            0xFFFFFFFFFFFFFFF0l, // 60
            0xFFFFFFFFFFFFFFF8l, // 61
            0xFFFFFFFFFFFFFFFCl, // 62
            0xFFFFFFFFFFFFFFFEl, // 63
            0xFFFFFFFFFFFFFFFFl // 64
            };

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
     * check if IP v6 string is valid.
     * 
     * @param str
     *            IP string.
     * @return true if valid, false if not.
     */
    public static boolean isIpV6StrValid(String str) {
        final String regexV6 =
                "^([\\da-fA-F]{1,4}:){6}((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}"
                + "(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^::([\\da-fA-F]{1,4}:){0,4}"
                + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                + "^([\\da-fA-F]{1,4}:):([\\da-fA-F]{1,4}:){0,3}"
                + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                + "^([\\da-fA-F]{1,4}:){2}:([\\da-fA-F]{1,4}:){0,2}"
                + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|"
                + "^([\\da-fA-F]{1,4}:){3}:([\\da-fA-F]{1,4}:){0,1}"
                + "((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2["
                        + "0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){4}:((25[0-5]|2[0-4]\\d|[01]"
                        + "?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$|^([\\da-fA-F]{1,4}:){7}"
                        + "[\\da-fA-F]{1,4}$|^:((:[\\da-fA-F]{1,4}){1,6}|:)$|^[\\da-fA-F]{1,4}:((:["
                        + "\\da-fA-F]{1,4}){1,5}|:)$|^([\\da-fA-F]{1,4}:){2}((:[\\da-fA-F]{1,4}){1,4}|"
                        + ":)$|^([\\da-fA-F]{1,4}:){3}((:[\\da-fA-F]{1,4}){1,3}|:)$|^([\\da-fA-F]{1,4}"
                        + ":){4}((:[\\da-fA-F]{1,4}){1,2}|:)$|^([\\da-fA-F]{1,4}:){5}:([\\da-fA-F]{1,4}"
                        + ")?$|^([\\da-fA-F]{1,4}:){6}:$";
        Pattern pattern = Pattern.compile(regexV6);
        boolean isRegular = pattern.matcher(str).matches();
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
     * parse a IPv4/v6 string to long[].
     * 
     * @param ipInfo
     *            ip infomation
     * @param ipLength
     *            length of ip
     * @return long[] for a IPv4/v6 address
     */
    public static long[] parsingIp(String ipInfo, int ipLength) {
        long startHighAddr = 0, endHighAddr = 0;
        long startLowAddr = 0, endLowAddr = 0;
        long[] ipInfoLong = ipToLong(ipInfo);
        final int sizeAll = 65;
        final long maxOneField = 0xFFFF;
        if (ipLength != 0) {
            if (ipInfo.indexOf(":") != -1) {
                if (ipLength < sizeAll) {
                    ipLength = ipLength - 1;
                    startHighAddr = ipInfoLong[0] & IPV6Array[ipLength];
                    startLowAddr = ipInfoLong[1];

                    long inversion =
                            (~IPV6Array[ipLength]) & 0xFFFFFFFFFFFFFFFFL;
                    endHighAddr = ipInfoLong[0] | inversion;
                    endLowAddr = startLowAddr;
                } else {
                    ipLength = ipLength - sizeAll;
                    startHighAddr = ipInfoLong[0];
                    startLowAddr = ipInfoLong[1] & IPV6Array[ipLength];

                    long inversion =
                            (~IPV6Array[ipLength]) & 0xFFFFFFFFFFFFFFFFL;
                    endLowAddr = ipInfoLong[0] | inversion;
                    endHighAddr = startHighAddr;
                }

            } else {
                ipLength = ipLength - 1;
                startLowAddr = ipInfoLong[0] & IPV4Array[ipLength];
                long inversion = (~IPV4Array[ipLength]) & maxOneField;
                endLowAddr = ipInfoLong[0] | inversion;
            }

        } else {
            if (ipInfo.indexOf(":") != -1) {
                startHighAddr = ipInfoLong[0];
                startLowAddr = ipInfoLong[1];
            } else {
                startLowAddr = ipInfoLong[0];
            }
        }

        long[] iplongs =
                { startHighAddr, endHighAddr, startLowAddr, endLowAddr };
        return iplongs;
    }

    /**
     * parse two IPv4/v6 strings to long[].
     * 
     * @param startAddress
     *            start ip
     * @param endAddress
     *            end ip
     * @param startIpLength
     *            length of ip
     * @param endIpLength
     *            length of ip
     * @return long[] for a IPv4/v6 address
     */
    public static long[] parsingIp(String startAddress, String endAddress,
            int startIpLength, int endIpLength) {
        long startHighAddr = 0, endHighAddr = 0;
        long startLowAddr = 0, endLowAddr = 0;
        long[] ipStartInfoLong = ipToLong(startAddress);
        long[] ipEndInfoLong = ipToLong(endAddress);
        final int sizeAll = 65;
        if (startIpLength != 0 && endIpLength != 0) {
            if (startAddress.indexOf(":") != -1
                    && endAddress.indexOf(":") != -1) {
                if (endIpLength < sizeAll) {
                    startHighAddr =
                            ipStartInfoLong[0] & IPV6Array[startIpLength];
                    startLowAddr = ipStartInfoLong[1];

                    endHighAddr = ipEndInfoLong[0] & IPV6Array[endIpLength];
                    endLowAddr = ipEndInfoLong[1];
                } else {
                    startHighAddr = ipStartInfoLong[0];
                    startLowAddr =
                            ipStartInfoLong[1] & IPV6Array[startIpLength];

                    endLowAddr = ipEndInfoLong[0] & IPV6Array[endIpLength];
                    endHighAddr = ipEndInfoLong[1];
                }

            } else {
                startLowAddr = ipStartInfoLong[0] & IPV4Array[startIpLength];
                endLowAddr = ipStartInfoLong[0] & IPV4Array[endIpLength];
            }

        } else {
            if (startAddress.indexOf(":") != -1
                    && endAddress.indexOf(":") != -1) {
                startHighAddr = ipStartInfoLong[0];
                startLowAddr = ipStartInfoLong[1];

                endHighAddr = ipEndInfoLong[0];
                endLowAddr = ipEndInfoLong[1];
            } else {
                startHighAddr = 0;
                startLowAddr = ipStartInfoLong[0];

                endHighAddr = 0;
                endLowAddr = ipEndInfoLong[0];
            }

        }
        long[] iplongs =
                { startHighAddr, endHighAddr, startLowAddr, endLowAddr };
        return iplongs;
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
            // return Ipv6ToBigDecimal(ipStr);
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
        int allColons = 7;
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
