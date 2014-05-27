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

import java.io.UnsupportedEncodingException;
import java.net.IDN;
import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * domain util.
 * 
 * @author jiashuo
 * 
 */
public final class DomainUtil {
    /**
     * default constructor.
     */
    private DomainUtil() {
        super();
    }

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainUtil.class);
    /**
     * 'xn' prefix.
     */
    public static final String XN_PREFIX = "xn";
    /**
     * 'xn--' prefix.
     */
    public static final String ACE_PREFIX = "xn--";
    /**
     * max domain length,without last '.'.
     */
    public static final int MAX_DOMAIN_LENGTH_WITHOUT_LAST_DOT = 253;
    /**
     * min domain length,without last '.'.
     */
    public static final int MIN_DOMAIN_LENGTH_WITHOUT_LAST_DOT = 3;
    /**
     * max ASCII code.
     */
    public static final int MAX_ASCII_CODE = 0x7F;

    /**
     * validate domain puny name is valid idna.
     * 
     * @param domainName
     *            domain name.
     * @return true if is valid idna,false if not.
     */
    public static boolean validateDomainNameIsValidIdna(String domainName) {
        if (StringUtils.isBlank(domainName) || !domainName.contains(".")) {
            return false;
        }
        if (!domainName.startsWith(ACE_PREFIX) && isLdh(domainName)) {
            return true;
        }
        domainName = deleteLastPoint(domainName);
        if (StringUtils.isBlank(domainName) || !domainName.contains(".")) {
            return false;
        }
        if (!validateDomainLength(domainName)) {
            return false;
        }
        return IdnaUtil.isValidIdn(domainName);
    }

    /**
     * remove the last '.' in paramStr.
     * 
     * @param paramStr
     *            paramStr.
     * @return paramStr deleted last '.'.
     */
    public static String deleteLastPoint(String paramStr) {
        if (StringUtils.isBlank(paramStr)) {
            return paramStr;
        }
        if (paramStr.length() <= 1 || !paramStr.endsWith(".")) {
            return paramStr;
        }
        return paramStr.substring(0, paramStr.length() - 1);
    }

    /**
     * get lower case if label is all ASCII chars.
     * 
     * @param str
     *            string.
     * @return String string.
     */
    public static String getLowerCaseByLabel(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String[] splits = StringUtils.split(str, ".");
        StringBuffer result = new StringBuffer();
        if (str.startsWith(".")) {
            str = str.replaceFirst("\\.", "");
            result.append(".");
        }
        for (String split : splits) {
            split = getLowercaseIfAllAscii(split);
            result.append(split);
            result.append(".");
        }
        String resultStr = result.toString();
        if (!str.endsWith(".") && resultStr.endsWith(".")) {
            resultStr = resultStr.substring(0, resultStr.length() - 1);
        }
        return resultStr;
    }

    /**
     * generate domain puny name.
     * 
     * @param domainName
     *            domain name.
     * @return domain puny name.
     */
    public static String geneDomainPunyName(String domainName) {
        return IDN.toASCII(domainName); // long lable exception
    }

    /**
     * decode and trim string.
     * 
     * @param str
     *            string.
     * @return str after decode and trim.
     */
    public static String decodeAndTrim(String str) {
        if (StringUtils.isBlank(str)) {
            return StringUtils.trim(str);
        }
        str = urlDecode(str);
        str = StringUtils.trim(str);
        return str;
    }

    /**
     * encoded url use UTF-8.
     * 
     * @param str
     *            string.
     * @return String decoded string.
     */
    private static String urlDecode(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String result = str;
        try {
            result = URLDecoder.decode(str, StringUtil.CHAR_SET_UTF8);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * check domain is valid ldh.
     * 
     * @param domain
     *            domain name.
     * @return true if is,false if not.
     */
    private static boolean isLdh(String domain) {
        if (StringUtils.isBlank(domain)) {
            return false;
        }
        if (domain.indexOf("--") == XN_PREFIX.length()) { // '--' can't at 2th
            return false;
        }
        String domainWithoutLastPoint = deleteLastPoint(domain);
        if (!validateDomainLength(domainWithoutLastPoint)) {
            return false;
        }
        String ldhReg = "^(?!-)(?!.*?-$)([0-9a-zA-Z][0-9a-zA-Z-]{0,62}\\.)+"
                + "([0-9a-zA-Z][0-9a-zA-Z-]{0,62})?$";
        if (domainWithoutLastPoint.matches(ldhReg)) {
            return true;
        }
        return false;
    }

    /**
     * validate domain length, length is without last dot.
     * 
     * @param domainWithoutLastDot
     *            domain without last dot.
     * @return true if valid,false if not.
     */
    private static boolean validateDomainLength(String domainWithoutLastDot) {
        if (StringUtils.isBlank(domainWithoutLastDot)) {
            return false;
        }
        if (domainWithoutLastDot.length() < MIN_DOMAIN_LENGTH_WITHOUT_LAST_DOT
                || domainWithoutLastDot.length() 
                > MAX_DOMAIN_LENGTH_WITHOUT_LAST_DOT) {
            return false;
        }
        return true;
    }

    /**
     * get lowercase If all chars in str are ASCII char.
     * 
     * @param str
     *            string.
     * @return String lower cased str if all ascii, str if not.
     */
    private static String getLowercaseIfAllAscii(String str) {
        if (isAllASCII(str)) {
            return StringUtils.lowerCase(str);
        }
        return str;
    }

    /**
     * all chars in input string are ASCII.
     * 
     * @param input
     *            string.
     * @return true if all ASCII,false if not.
     */
    private static boolean isAllASCII(String input) {
        if (StringUtils.isBlank(input)) {
            return false;
        }
        boolean isASCII = true;
        for (int i = 0; i < input.length(); i++) {
            int c = input.charAt(i);
            if (c > MAX_ASCII_CODE) {
                isASCII = false;
                break;
            }
        }
        return isASCII;
    }
}
