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

package org.restfulwhois.rdap.port43.util;

import java.util.regex.Pattern;

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
     * check if str is ip.
     * 
     * @param ipStr
     *            ip string.
     * @return true if ipStr is valid IP,false if not.
     */
    public static boolean isIpV4OrV6Str(String ipStr) {
        return IpUtil.isIpV4StrValid(ipStr) || IpUtil.isIpV6StrValid(ipStr);
    }

    /**
     * check if IP v4 string is valid.
     * 
     * @param str
     *            IP string(without *).
     * @return true if valid, false if not.
     */
    public static boolean isIpV4StrValid(String str) {
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

}
