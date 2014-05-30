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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vgrs.xcode.idna.Idna;
import com.vgrs.xcode.idna.Punycode;
import com.vgrs.xcode.util.XcodeException;

/**
 * Idna util. rfc5890-5894.
 * 
 * @author qichao.
 * 
 */
public final class IdnaUtil {

    /**
     * default constructor.
     */
    private IdnaUtil() {
        super();
    }

    /**
     * logger.
     */
    private static Logger logger = LoggerFactory.getLogger(IdnaUtil.class);
    /**
     * idna object,will initialize when class constructing.
     */
    private static Idna idna = null;

    static {
        try {
            idna = new Idna(new Punycode(), true, true);
        } catch (XcodeException e) {
            logger.info("Idna init failed :" + e);
        }
    }

    /**
     * Make a domain name to unicode domain.
     * 
     * @param ascii
     *            ascii domain.
     * @return String unicode domain.
     * @throws XcodeException
     *             XcodeException.
     */
    private static String toUnicode(String ascii) throws XcodeException {
        if (ascii == null || ascii.isEmpty()) {
            return ascii;
        }
        char[] input = ascii.toCharArray();

        int[] output = idna.domainToUnicode(input);
        StringBuilder sb = new StringBuilder();
        for (int i : output) {
            sb.append((char) i);
        }
        return sb.toString();

    }

    /**
     * Make a domain name to ascii domain.
     * 
     * @param unicode
     *            unicode domain.
     * @return ASCII domain.
     * @throws XcodeException
     *             XcodeException.
     */
    private static String toAscii(String unicode) throws XcodeException {
        if (unicode == null || unicode.isEmpty()) {
            return unicode;
        }

        int[] input = new int[unicode.length()];
        int i = 0;
        for (int u : unicode.toCharArray()) {
            input[i++] = u;
        }
        char[] output;
        output = idna.domainToAscii(input);
        return new String(output);
    }

    /**
     * is Valid IDN,toAscii()and toUnicode() will return ok for
     * xn--55qx5d.[U-LABEL],which should be wrong idn.
     * 
     * @param domain
     *            domain name.
     * @return true if is valid idn,false if not.
     */
    public static boolean isValidIdn(String domain) {
        if (domain == null || domain.isEmpty()) {
            return false;
        }
        // convert punycode to unicode
        try {
            return isValidIdnWithException(domain);
        } catch (Exception e) {
            logger.info("Idna validate failed :" + e);
            return false;
        }
    }

    /**
     * is Valid IDN ,may throw exception.
     * 
     * @param domain
     *            domain name.
     * @return true if valid idn,false if not.
     * @throws XcodeException
     *             XcodeException.
     */
    private static boolean isValidIdnWithException(String domain)
            throws XcodeException {
        String[] labels = domain.split("\\.", -1);
        StringBuilder tmp = new StringBuilder();
        int i = 0;
        // 0: flag of ascii
        int idnType = 0;
        for (String label : labels) {
            if (label == null || label.isEmpty()) {
                return false;
            }
            if (StringUtils.startsWithIgnoreCase(label, "xn--")) {
                tmp.append(toUnicode(label));
                // -1:flag of punycode
                if (idnType > 0) {
                    // mixed unicode and punycode
                    return false;
                } else if (0 == idnType) {
                    idnType = -1;
                }
            } else {
                tmp.append(label);
                for (char ch : label.toCharArray()) {
                    // 1:flag of unicode
                    if (ch > DomainUtil.MAX_ASCII_CODE) {
                        if (idnType < 0) {
                            // mixed punycode & unicode
                            return false;
                        } else if (0 == idnType) {
                            idnType = 1;
                        }
                        break;
                    }
                }
            }
            if (i < labels.length) {
                tmp.append(".");
                i++;
            }
        }
        // convert unicode to ascii
        if (null != toAscii(tmp.toString())) {
            return true;
        }
        return false;
    }
}
