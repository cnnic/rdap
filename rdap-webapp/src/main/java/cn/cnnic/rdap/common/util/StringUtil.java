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

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.text.Normalizer.Form;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * String util.
 * 
 * @author jiashuo
 * 
 */
public final class StringUtil {
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
     * check is valid entity handle.
     * 
     * @param handle
     *            handle.
     * @return
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
            String decodedURL = URLDecoder.decode(str, CHAR_SET_UTF8);
            decodedURL =
                    decodedURL.replaceAll("\\\\",
                            URLEncoder.encode("\\", CHAR_SET_UTF8));
            URI uri = new URI(decodedURL);
            result = uri.toASCIIString();
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
     * @throws NumberFormatException
     *             if the string does not contain a parsable unsigned integer.
     */
    public static long parseUnsignedLong(String s) throws NumberFormatException {
        return parseUnsignedLong(s, 10);
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
     * @throws NumberFormatException
     *             if the {@code String} does not contain a parsable
     *             {@code long}.
     */
    private static long parseUnsignedLong(String s, int radix)
            throws NumberFormatException {
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
                if (len <= 12 || // Long.MAX_VALUE in Character.MAX_RADIX is 13
                                 // digits
                        (radix == 10 && len <= 18)) { // Long.MAX_VALUE in base
                                                      // 10 is 19 digits
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
}
