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

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.ibm.icu.lang.UCharacter;

/**
 * *
 * <p>
 * Operations on {@link java.lang.String} that are <code>null</code> safe.
 * </p>
 * <ul>
 * <li><b>generateEncodedRedirectURL</b> - generate encoded redirect URL, for
 * redirect service</li>
 * <li><b>parseTldsToListIfTldListIsNull</b> - parse tlds to list</li>
 * <li><b>addQuotas</b> - add "'" before and after str</li>
 * <li><b>checkIsValidSearchPattern</b> - check if valid search pattern.</li>
 * <li><b>containsAtMostOnce</b> - check if str contains searchStr more than
 * once.</li>
 * <li><b>isValidEntityHandleOrName</b> - check is valid entity handle</li>
 * <li><b>urlEncode</b> - encoded url with UTF-8 encoding</li>
 * <li><b>getNormalization</b> - get normalization format string</li>
 * <li><b>parseUnsignedLong</b> - Parses the string argument as an unsigned
 * decimal</li>
 * </ul>
 * 
 * @author jiashuo
 * 
 */
public final class StringUtil {

    /**
     * URL separator.
     */
    private static final String URL_SEPARATOR = "/";

    /**
     * space.
     */
    public static final String SPACE = " ";

    /**
     * tld splitor ".".
     */
    public static final String TLD_SPLITOR = ".";
    /**
     * search wildcard.
     */
    public static final String ASTERISK = "*";

    /**
     * default constructor.
     */
    private StringUtil() {
        super();
    }

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(StringUtil.class);
    /**
     * UTF8 encoding.
     */
    public static final String CHAR_SET_UTF8 = "UTF-8";
    /**
     * iso8859-1 encoding.
     */
    public static final String CHAR_SET_ISO8859 = "ISO8859-1";

    /**
     * max entity handle length.
     */
    public static final int MAX_ENTITY_HANDLE_LENGTH = 253;

    /**
     * parse media types from media type string.
     * 
     * @param mediaTypesStr
     *            media type string.
     * @return media type list.
     */
    public static List<MediaType> parseMediaTypes(String mediaTypesStr) {
        try {
            return MediaType.parseMediaTypes(mediaTypesStr);
        } catch (Exception e) {
            LOGGER.error("invalid media type for {}, error :{}", mediaTypesStr,
                    e);
        }
        return null;
    }

    /**
     * check if decodeUri contain non-ASCII-printable chars.
     * 
     * @param decodeUri
     *            decodeUri
     * @return true if contains,false if not.
     */
    public static boolean containNonAsciiPrintableChars(String decodeUri) {
        if (StringUtils.isBlank(decodeUri)) {
            return false;
        }
        return !StringUtils.isAsciiPrintable(decodeUri);
    }

    /**
     * generate url encoded redirect URL.
     * 
     * @param param
     *            query param.
     * @param servicePartUri
     *            servicePartUri.
     * @param baseUrl
     *            baseUrl.
     * @return absolute URL.
     */
    public static String generateEncodedRedirectURL(String param,
            String servicePartUri, String baseUrl) {
        LOGGER.debug("   redirect found,baseUrl:{},return 301.", baseUrl);
        if (StringUtils.endsWith(baseUrl, URL_SEPARATOR)) {
            baseUrl = StringUtils.removeEnd(baseUrl, URL_SEPARATOR);
        }
        if (StringUtils.startsWith(servicePartUri, URL_SEPARATOR)) {
            servicePartUri =
                    StringUtils.removeStart(servicePartUri, URL_SEPARATOR);
        }
        if (StringUtils.endsWith(servicePartUri, URL_SEPARATOR)) {
            servicePartUri =
                    StringUtils.removeEnd(servicePartUri, URL_SEPARATOR);
        }

        String absoluteUrl =
                baseUrl + URL_SEPARATOR + servicePartUri + URL_SEPARATOR
                        + param;
        absoluteUrl = StringUtil.urlEncode(absoluteUrl);
        LOGGER.debug("   redirect URL:{}", absoluteUrl);
        return absoluteUrl;
    }

    /**
     * parse separated string to list if list is null.
     * 
     * @param separatedStr
     *            ';' separated string, eg: cn;edu.cn
     * @param list
     *            list.
     * @return List<String>.
     */
    public static List<String> parseSeparatedStringToListIfListIsNull(
            String separatedStr, List<String> list) {
        if (null != list) {
            return list;
        }
        list = new ArrayList<String>();
        if (null == separatedStr) {
            return list;
        }
        String[] splits = StringUtils.split(separatedStr, ";");
        for (String tld : splits) {
            tld = StringUtils.trim(tld);
            if (StringUtils.isNotBlank(tld)) {
                list.add(tld);
            }
        }
        return list;
    }

    /**
     * add "'" before and after str.
     * 
     * @param str
     *            str.
     * @return string.
     */
    public static String addQuotas(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return "'" + str + "'";
    }

    /**
     * check if str is valid search pattern.
     * 
     * <pre>
     * StringUtil.checkIsValidSearchPattern(null)      = false
     * StringUtil.checkIsValidSearchPattern("")        = false
     * StringUtil.checkIsValidSearchPattern(" ")       = false
     * StringUtil.checkIsValidSearchPattern("*")       = false
     * StringUtil.checkIsValidSearchPattern("*bob")    = false
     * StringUtil.checkIsValidSearchPattern("*bob*")   = false
     * </pre>
     * 
     * @param str
     *            str.
     * @return true if valid,false if not.
     */
    public static boolean checkIsValidSearchPattern(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        if (ASTERISK.equals(str) || str.startsWith(ASTERISK)) {
            return false;
        }
        return containsAtMostOnce(str, ASTERISK);
    }

    /**
     * check if str contains searchStr more than once.
     * 
     * @param str
     *            str
     * @param searchStr
     *            searchStr.
     * @return true if contains more than once, false if not.
     */
    public static boolean containsAtMostOnce(String str, String searchStr) {
        if (StringUtils.isBlank(str) || StringUtils.isBlank(searchStr)) {
            return false;
        }
        return StringUtils.indexOf(str, searchStr) == StringUtils.lastIndexOf(
                str, searchStr);
    }

    /**
     * check is valid entity handle.
     * 
     * @param handle
     *            handle.
     * @return bool
     */
    public static boolean isValidEntityHandleOrName(String handle) {
        if (StringUtils.isBlank(handle)
                || handle.length() > MAX_ENTITY_HANDLE_LENGTH) {
            return false;
        }
        return true;
    }

    /**
     * encoded url with UTF-8 encoding. This will escape protocol(eg:'http://')
     * and '/'.
     * 
     * @param str
     *            string
     * @return String encoded string
     */
    public static String urlEncode(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        String result = str;
        try {
            UriComponents uriComponents =
                    UriComponentsBuilder.fromUriString(str).build();
            return uriComponents.encode().toUriString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * get normalization format string.
     * 
     * @param str
     *            :string.
     * @return string.
     */
    public static String getNormalization(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return Normalizer.normalize(str, Form.NFKC);
    }

    /**
     * Parses the string argument as an unsigned decimal {@code long}. The
     * characters in the string must all be decimal digits, except that the
     * first character may be an an ASCII plus sign {@code '+'} (
     * {@code '\u005Cu002B'}). The resulting integer value is returned, exactly
     * as if the argument and the radix 10 were given as arguments to the
     * {@link #parseUnsignedLong(java.lang.String, int)} method.
     * 
     * @param s
     *            a {@code String} containing the unsigned {@code long}
     *            representation to be parsed
     * @return the unsigned {@code long} value represented by the decimal string
     *         argument
     */
    public static long parseUnsignedLong(String s) {
        if (StringUtils.isBlank(s)) {
            return 0L;
        }
        final int baseRadix = 10;
        return parseUnsignedLong(s, baseRadix);
    }

    /**
     * Parses the string argument as an unsigned {@code long} in the radix
     * specified by the second argument. An unsigned integer maps the values
     * usually associated with negative numbers to positive numbers larger than
     * {@code MAX_VALUE}.
     * 
     * The characters in the string must all be digits of the specified radix
     * (as determined by whether {@link java.lang.Character#digit(char, int)}
     * returns a nonnegative value), except that the first character may be an
     * ASCII plus sign {@code '+'} ({@code '\u005Cu002B'}). The resulting
     * integer value is returned.
     * 
     * <p>
     * An exception of type {@code NumberFormatException} is thrown if any of
     * the following situations occurs:
     * <ul>
     * <li>The first argument is {@code null} or is a string of length zero.
     * 
     * <li>The radix is either smaller than
     * {@link java.lang.Character#MIN_RADIX} or larger than
     * {@link java.lang.Character#MAX_RADIX}.
     * 
     * <li>Any character of the string is not a digit of the specified radix,
     * except that the first character may be a plus sign {@code '+'} (
     * {@code '\u005Cu002B'}) provided that the string is longer than length 1.
     * 
     * <li>The value represented by the string is larger than the largest
     * unsigned {@code long}, 2<sup>64</sup>-1.
     * 
     * </ul>
     * 
     * 
     * @param s
     *            the {@code String} containing the unsigned integer
     *            representation to be parsed
     * @param radix
     *            the radix to be used while parsing {@code s}.
     * @return the unsigned {@code long} represented by the string argument in
     *         the specified radix.
     */
    private static long parseUnsignedLong(String s, int radix) {
        final int maxRadix = 13;
        final int baseRadix = 10;
        final int lengBaseRadix = 18;
        if (s == null) {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new NumberFormatException(String.format(
                        "Illegal leading minus sign "
                                + "on unsigned string %s.", s));
            } else {
                if (len <= maxRadix - 1
                // Long.MAX_VALUE in Character.
                // MAX_RADIX is 13 digits
                        || (radix == baseRadix && len <= lengBaseRadix)) {
                    // Long.MAX_VALUE in base 10 length is 19 digits
                    return Long.parseLong(s, radix);
                }

                // No need for range checks on len due to testing above.
                long first = Long.parseLong(s.substring(0, len - 1), radix);
                int second = Character.digit(s.charAt(len - 1), radix);
                if (second < 0) {
                    throw new NumberFormatException("Bad digit at end of " + s);
                }
                long result = first * radix + second;
                if (compareUnsigned(result, first) < 0) {
                    /*
                     * The maximum unsigned value, (2^64)-1, takes at most one
                     * more digit to represent than the maximum signed value,
                     * (2^63)-1. Therefore, parsing (len - 1) digits will be
                     * appropriately in-range of the signed parsing. In other
                     * words, if parsing (len -1) digits overflows signed
                     * parsing, parsing len digits will certainly overflow
                     * unsigned parsing.
                     * 
                     * The compareUnsigned check above catches situations where
                     * an unsigned overflow occurs incorporating the
                     * contribution of the final digit.
                     */
                    throw new NumberFormatException(String.format(
                            "String value %s exceeds "
                                    + "range of unsigned long.", s));
                }
                return result;
            }
        } else {
            throw new NumberFormatException("exception for: " + s);
        }
    }

    /**
     * Compares two {@code long} values numerically treating the values as
     * unsigned.
     * 
     * @param x
     *            the first {@code long} to compare
     * @param y
     *            the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y}; a value less than
     *         {@code 0} if {@code x < y} as unsigned values; and a value
     *         greater than {@code 0} if {@code x > y} as unsigned values
     */
    private static int compareUnsigned(long x, long y) {
        return compare(x + Long.MIN_VALUE, y + Long.MIN_VALUE);
    }

    /**
     * Compares two {@code long} values numerically. The value returned is
     * identical to what would be returned by:
     * 
     * <pre>
     * Long.valueOf(x).compareTo(Long.valueOf(y))
     * </pre>
     * 
     * @param x
     *            the first {@code long} to compare
     * @param y
     *            the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y}; a value less than
     *         {@code 0} if {@code x < y}; and a value greater than {@code 0} if
     *         {@code x > y}
     */
    private static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }

    /**
     * get foldCase format string by input string.
     * 
     * @param strParam
     *            :string.
     * @return string.
     */
    public static String foldCase(String strParam) {
        return UCharacter.foldCase(strParam, true);
    }

    /**
     * get foldCase and normalization format string by input string.
     * 
     * @param strParam
     *            :string.
     * @return string.
     */
    public static String foldCaseAndNormalization(String strParam) {
        String strFold = UCharacter.foldCase(strParam, true);
        return StringUtil.getNormalization(strFold);
    }
}
