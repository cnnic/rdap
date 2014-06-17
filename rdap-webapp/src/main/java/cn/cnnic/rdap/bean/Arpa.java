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
package cn.cnnic.rdap.bean;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import cn.cnnic.rdap.bean.Network.IpVersion;
import cn.cnnic.rdap.common.util.DomainUtil;

/**
 * ARPA registrations found in RIRs and is the expected response for the
 * "/domain" query as defined by [I-D.ietf-weirds-rdap-query].
 * 
 * @author dana
 * 
 */
public class Arpa {

    /**
     * the V6 IP address has 16 bytes.
     */
    private static final int LENGTH_OF_V6_BYTES = 16;

    /**
     * the V4 IP address has 4 bytes.
     */
    @SuppressWarnings("unused")
    private static final int LENGTH_OF_V4_BYTES = 4;

    /**
     * the V6 ARPA has 32 sections, like F.F.1.0.ip6.arpa .
     */
    private static final int LENGTH_OF_V6_SECTIONS = 32;

    /**
     * the V4 IP address has 4 sections, like 192.0.0.in-addr.arpa .
     */
    private static final int LENGTH_OF_V4_SECTIONS = 4;

    /**
     * Radix 16 .
     */
    public static final int RADIX_HEX = 16;

    /**
     * Radix 10 .
     */
    public static final int RADIX_DECIMAL = 10;

    /**
     * 0xFF .
     */
    private static final byte MASK_FF = (byte) 0xFF;

    /**
     * 0x0F .
     */
    private static final byte MASK_0F = (byte) 0x0F;

    /**
     * High 4 bits of byte .
     */
    private static final byte HIGH_BYTE = 4;

    /**
     * the starting IP address of the network.
     */
    private BigInteger startHighAddress;

    /**
     * the starting IP address of the network.
     */
    private BigInteger startLowAddress;

    /**
     * the ending IP address of the network.
     */
    private BigInteger endHighAddress;

    /**
     * the ending IP address of the network.
     */
    private BigInteger endLowAddress;

    /**
     * @see IpVersion.
     */
    private IpVersion ipVersion;
    /**
     * an identifier assigned to the network registration by the registration
     * holder.
     */
    private String name;

    /**
     * constructor for Arpa.
     * 
     * @param sections
     *            IP address with byte[].
     * @param ipVersion
     *            IP version, v4 or v6.
     * 
     */
    public Arpa(byte[] sections, IpVersion ipVersion) {

        this.ipVersion = ipVersion;
        if (IpVersion.V6 == ipVersion) {

            setStartAndEndAddressForIp6(sections);

        } else if (IpVersion.V4 == ipVersion) {

            setStartAndEndAddressForInAddr(sections);
        }
    }

    /**
     * get startHighAddress.
     * 
     * @return startHighAddress.
     */
    public BigInteger getStartHighAddress() {
        return startHighAddress;
    }

    /**
     * get startLowAddress.
     * 
     * @return startLowAddress.
     */
    public BigInteger getStartLowAddress() {
        return startLowAddress;
    }

    /**
     * get EndHighAddress.
     * 
     * @return startAddress.
     */
    public BigInteger getEndHighAddress() {
        return endHighAddress;
    }

    /**
     * get endLowAddress.
     * 
     * @return endLowAddress.
     */
    public BigInteger getEndLowAddress() {
        return endLowAddress;
    }

    /**
     * get ipVersion.
     * 
     * @return ipVersion.
     */
    public IpVersion getIpVersion() {
        return ipVersion;
    }

    /**
     * get name.
     * 
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * decode a string to an Arpa.
     * 
     * @param name
     *            an arpa string.
     * @return Arpa.
     */
    public static Arpa decodeArpa(String name) {

        if (StringUtils.isEmpty(name)) {
            return null;
        } else if (StringUtils.endsWith(name, DomainUtil.IPV4_ARPA_SUFFIX)) {
            return decodeInAddrArpa(name);
        } else if (StringUtils.endsWith(name, DomainUtil.IPV6_ARPA_SUFFIX)) {
            return decodeIp6Arpa(name);
        }

        return null;
    }

    /**
     * decode a string to an Arpa.
     * 
     * @param name
     *            an arpa string.
     * @return Arpa.
     */
    private static Arpa decodeInAddrArpa(String name) {
        String arpa =
                StringUtils.removeEndIgnoreCase(name, "."
                        + DomainUtil.IPV4_ARPA_SUFFIX);

        String[] labels = arpa.split("\\.");
        byte[] ipSecs = new byte[labels.length];
        for (int i = 0; i < labels.length; i++) {
            ipSecs[i] = Integer.valueOf(labels[i], RADIX_DECIMAL).byteValue();
        }

        if (null != ipSecs && ipSecs.length <= LENGTH_OF_V4_SECTIONS) {
            ArrayUtils.reverse(ipSecs);
            return new Arpa(ipSecs, IpVersion.V4);
        }

        return null;
    }

    /**
     * decode a byte array from in-addr.arpa to an Arpa.
     * 
     * @param sections
     *            a byte array from an arpa string.
     */
    private void setStartAndEndAddressForInAddr(byte[] sections) {

        byte[] byteStart = { 0, 0, 0, 0 };
        System.arraycopy(sections, 0, byteStart, 0, sections.length);
        this.startLowAddress = new BigInteger(1, byteStart);
        this.startHighAddress = BigInteger.ZERO;

        byte[] byteEnd = { MASK_FF, MASK_FF, MASK_FF, MASK_FF };
        System.arraycopy(sections, 0, byteEnd, 0, sections.length);

        this.endLowAddress = new BigInteger(1, byteEnd);
        this.endHighAddress = BigInteger.ZERO;
    }

    /**
     * decode a string to an Arpa.
     * 
     * @param name
     *            an arpa string.
     * @return Arpa.
     */
    private static Arpa decodeIp6Arpa(String name) {
        String arpa =
                StringUtils.removeEndIgnoreCase(name, "."
                        + DomainUtil.IPV6_ARPA_SUFFIX);

        String[] labels = arpa.split("\\.");
        byte[] ipSecs = new byte[labels.length];
        for (int i = 0; i < labels.length; i++) {
            ipSecs[i] = Integer.valueOf(labels[i], RADIX_HEX).byteValue();
        }

        if (null != ipSecs && ipSecs.length <= LENGTH_OF_V6_SECTIONS) {
            ArrayUtils.reverse(ipSecs);
            return new Arpa(ipSecs, IpVersion.V6);
        }

        return null;
    }

    /**
     * decode a byte array from in-addr.arpa to an Arpa.
     * 
     * @param sections
     *            a byte array from an arpa string.
     */
    private void setStartAndEndAddressForIp6(byte[] sections) {

        setStartAddressForIp6(sections);

        setEndAddressForIp6(sections);
    }
    
    /**
     * decode a byte array from in-addr.arpa to an Arpa.
     * 
     * @param sections
     *            a byte array from an arpa string.
     */
    private void setStartAddressForIp6(byte[] sections) {
        
        byte[] byteStart =
                { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        System.arraycopy(sections, 0, byteStart, 0, sections.length);

        byte[] highSecs = new byte[LENGTH_OF_V6_SECTIONS / 2];
        byte[] lowSecs = new byte[LENGTH_OF_V6_SECTIONS / 2];

        byte[] highBytes = new byte[LENGTH_OF_V6_BYTES / 2];
        byte[] lowBytes = new byte[LENGTH_OF_V6_BYTES / 2];

        System.arraycopy(byteStart, 0, highSecs, 0, highSecs.length);
        for (int i = 0; i < highBytes.length; i++) {
            int hb = (highSecs[i << 1] << HIGH_BYTE) + highSecs[(i << 1) + 1];
            highBytes[i] = (byte) (MASK_FF & hb);
        }
        this.startHighAddress = new BigInteger(1, highBytes);

        System.arraycopy(byteStart, lowSecs.length, lowSecs, 0, lowSecs.length);
        for (int i = 0; i < lowBytes.length; i++) {
            int lb = (lowSecs[i << 1] << HIGH_BYTE) + lowSecs[(i << 1) + 1];
            lowBytes[i] = (byte) (MASK_FF & lb);
        }
        this.startLowAddress = new BigInteger(1, lowBytes);
    }
    
    /**
     * decode a byte array from in-addr.arpa to an Arpa.
     * 
     * @param sections
     *            a byte array from an arpa string.
     */
    private void setEndAddressForIp6(byte[] sections) {
        
        byte[] highSecs = new byte[LENGTH_OF_V6_SECTIONS / 2];
        byte[] lowSecs = new byte[LENGTH_OF_V6_SECTIONS / 2];

        byte[] highBytes = new byte[LENGTH_OF_V6_BYTES / 2];
        byte[] lowBytes = new byte[LENGTH_OF_V6_BYTES / 2];
        
        byte[] byteEnd =
                { MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F,
                        MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F,
                        MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F,
                        MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F,
                        MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F, MASK_0F,
                        MASK_0F, MASK_0F };
        System.arraycopy(sections, 0, byteEnd, 0, sections.length);

        System.arraycopy(byteEnd, 0, highSecs, 0, highSecs.length);
        for (int i = 0; i < highBytes.length; i++) {
            int hb = (highSecs[i << 1] << HIGH_BYTE) + highSecs[(i << 1) + 1];
            highBytes[i] = (byte) (MASK_FF & hb);
        }
        this.endHighAddress = new BigInteger(1, highBytes);

        System.arraycopy(byteEnd, highSecs.length, lowSecs, 0, lowSecs.length);
        for (int i = 0; i < lowBytes.length; i++) {
            int lb = (lowSecs[i << 1] << HIGH_BYTE) + lowSecs[(i << 1) + 1];
            lowBytes[i] = (byte) (MASK_FF & lb);
        }
        this.endLowAddress = new BigInteger(1, lowBytes);
    }
    
    public NetworkQueryParam toNetworkQueryParam() {
        
        NetworkQueryParam param = new NetworkQueryParam(name, 
                new BigDecimal(startHighAddress), new BigDecimal(endHighAddress), 
                new BigDecimal(startLowAddress), new BigDecimal(endLowAddress), 
                ipVersion);
        
        return param;
    }
}
