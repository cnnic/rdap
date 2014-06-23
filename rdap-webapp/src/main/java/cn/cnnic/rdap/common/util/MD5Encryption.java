/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cn.cnnic.rdap.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * algorithm to use md5.
 * 
 * @author wang
 */
public final class MD5Encryption {
    /**
     * private constructor.
     */
    private MD5Encryption() {
        super();
    }

    /**
     * 
     * @param plainText
     *            the string to encrypt.
     * @return string.
     */
    public static String encryption(String plainText) {
        String reMd5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }

            reMd5 = buf.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return reMd5;
    }
}
