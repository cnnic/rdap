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
package org.restfulwhois.rdap.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that performs some subnet calculations given a network address and a
 * subnet mask.
 * 
 * @see "http://www.faqs.org/rfcs/rfc1519.html"
 * @author <rwinston@apache.org>
 * @since 2.0
 */
public class SubnetUtils {

    /**
     * ip reg.
     */
    private static final String IP_ADDRESS =
            "(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})";
    /**
     * mask reg.
     */
    private static final String SLASH_FORMAT = IP_ADDRESS + "/(\\d{1,3})";
    /**
     * pattern for ip reg.
     */
    private static final Pattern addressPattern = Pattern.compile(IP_ADDRESS);
    /**
     * pattern for cidr reg.
     */
    private static final Pattern cidrPattern = Pattern.compile(SLASH_FORMAT);
    /**
     * bit size.
     */
    private static final int NBITS = 32;

    /**
     * netmask.
     */
    private int netmask = 0;
    /**
     * address.
     */
    private int address = 0;
    /**
     * network.
     */
    private int network = 0;
    /**
     * broadcast.
     */
    private int broadcast = 0;

    /** Whether the broadcast/network address are included in host count */
    private boolean inclusiveHostCount = false;

    /**
     * Constructor that takes a CIDR-notation string, e.g. "192.168.0.1/16"
     * 
     * @param cidrNotation
     *            A CIDR-notation string, e.g. "192.168.0.1/16"
     * @throws IllegalArgumentException
     *             if the parameter is invalid, i.e. does not match n.n.n.n/m
     *             where n=1-3 decimal digits, m = 1-3 decimal digits in range
     *             1-32
     */
    public SubnetUtils(String cidrNotation) {
        calculate(cidrNotation);
    }

    /**
     * Constructor that takes a dotted decimal address and a dotted decimal
     * mask.
     * 
     * @param address
     *            An IP address, e.g. "192.168.0.1"
     * @param mask
     *            A dotted decimal netmask e.g. "255.255.0.0"
     * @throws IllegalArgumentException
     *             if the address or mask is invalid, i.e. does not match
     *             n.n.n.n where n=1-3 decimal digits and the mask is not all
     *             zeros
     */
    public SubnetUtils(String address, String mask) {
        calculate(toCidrNotation(address, mask));
    }

    /**
     * Returns <code>true</code> if the return value of
     * {@link SubnetInfo#getAddressCount()} includes the network address and
     * broadcast addresses.
     * 
     * @since 2.2
     */
    public boolean isInclusiveHostCount() {
        return inclusiveHostCount;
    }

    /**
     * Set to <code>true</code> if you want the return value of
     * {@link SubnetInfo#getAddressCount()} to include the network and broadcast
     * addresses.
     * 
     * @param inclusiveHostCount
     * @since 2.2
     */
    public void setInclusiveHostCount(boolean inclusiveHostCount) {
        this.inclusiveHostCount = inclusiveHostCount;
    }

    /**
     * Convenience container for subnet summary information.
     * 
     */
    public final class SubnetInfo {

        /**
         * constructor.
         */
        private SubnetInfo() {
        }

        /**
         * get netmask.
         * 
         * @return netmask.
         */
        private int netmask() {
            return netmask;
        }

        /**
         * get network.
         * 
         * @return network.
         */
        public int network() {
            return network;
        }

        /**
         * get address.
         * 
         * @return address.
         */
        public int address() {
            return address;
        }

        /**
         * get broadcast.
         * 
         * @return broadcast.
         */
        public int broadcast() {
            return broadcast;
        }

        /**
         * get low.
         * 
         * @return low.
         */
        private int low() {
            return (isInclusiveHostCount() ? network() : broadcast()
                    - network() > 1 ? network() + 1 : 0);
        }

        /**
         * get high.
         * 
         * @return high.
         */
        private int high() {
            return (isInclusiveHostCount() ? broadcast() : broadcast()
                    - network() > 1 ? broadcast() - 1 : 0);
        }

        /**
         * Returns true if the parameter <code>address</code> is in the range of
         * usable endpoint addresses for this subnet. This excludes the network
         * and broadcast adresses.
         * 
         * @param address
         *            A dot-delimited IPv4 address, e.g. "192.168.0.1"
         * @return True if in range, false otherwise
         */
        public boolean isInRange(String address) {
            return isInRange(toInteger(address));
        }

        /**
         * isInRange.
         * 
         * @param address
         *            address.
         * @return boolean.
         */
        private boolean isInRange(int address) {
            int diff = address - low();
            return (diff >= 0 && (diff <= (high() - low())));
        }

        /**
         * getBroadcastAddress.
         * 
         * @return string
         */
        public String getBroadcastAddress() {
            return format(toArray(broadcast()));
        }

        /**
         * getNetworkAddress.
         * 
         * @return string.
         */
        public String getNetworkAddress() {
            return format(toArray(network()));
        }

        /**
         * getNetmask.
         * 
         * @return string.
         */
        public String getNetmask() {
            return format(toArray(netmask()));
        }

        /**
         * getAddress.
         * 
         * @return string.
         */
        public String getAddress() {
            return format(toArray(address()));
        }

        /**
         * Return the low address as a dotted IP address. Will be zero for
         * CIDR/31 and CIDR/32 if the inclusive flag is false.
         * 
         * @return the IP address in dotted format, may be "0.0.0.0" if there is
         *         no valid address
         */
        public String getLowAddress() {
            return format(toArray(low()));
        }

        /**
         * Return the high address as a dotted IP address. Will be zero for
         * CIDR/31 and CIDR/32 if the inclusive flag is false.
         * 
         * @return the IP address in dotted format, may be "0.0.0.0" if there is
         *         no valid address
         */
        public String getHighAddress() {
            return format(toArray(high()));
        }

        /**
         * Get the count of available addresses. Will be zero for CIDR/31 and
         * CIDR/32 if the inclusive flag is false.
         * 
         * @return the count of addresses, may be zero.
         */
        public int getAddressCount() {
            int count =
                    broadcast() - network() + (isInclusiveHostCount() ? 1 : -1);
            return count < 0 ? 0 : count;
        }

    }

    /**
     * Return a {@link SubnetInfo} instance that contains subnet-specific
     * statistics
     * 
     * @return new instance
     */
    public final SubnetInfo getInfo() {
        return new SubnetInfo();
    }

    /**
     * Initialize the internal fields from the supplied CIDR mask
     * 
     * @param mask
     *            mask.
     */
    private void calculate(String mask) {
        Matcher matcher = cidrPattern.matcher(mask);

        if (matcher.matches()) {
            address = matchAddress(matcher);

            // Create a binary netmask from the number of bits specification /x
            int cidrPart =
                    rangeCheck(Integer.parseInt(matcher.group(5)), 0, NBITS);
            for (int j = 0; j < cidrPart; ++j) {
                netmask |= (1 << 31 - j);
            }

            // Calculate base network address
            network = (address & netmask);

            // Calculate broadcast address
            broadcast = network | ~(netmask);
        } else {
            throw new IllegalArgumentException("Could not parse [" + mask + "]");
        }
    }

    /**
     * Convert a dotted decimal format address to a packed integer format
     * 
     * @param address
     *            address.
     * @return int.
     */
    private int toInteger(String address) {
        Matcher matcher = addressPattern.matcher(address);
        if (matcher.matches()) {
            return matchAddress(matcher);
        } else {
            throw new IllegalArgumentException("Could not parse [" + address
                    + "]");
        }
    }

    /**
     * Convenience method to extract the components of a dotted decimal address
     * and pack into an integer using a regex match
     * 
     * @param matcher
     *            matcher.
     * @return int.
     */
    private int matchAddress(Matcher matcher) {
        int addr = 0;
        for (int i = 1; i <= 4; ++i) {
            int n = (rangeCheck(Integer.parseInt(matcher.group(i)), -1, 255));
            addr |= ((n & 0xff) << 8 * (4 - i));
        }
        return addr;
    }

    /**
     * Convert a packed integer address into a 4-element array
     * 
     * @param val
     *            val.
     * @return int[].
     */
    private int[] toArray(int val) {
        int ret[] = new int[4];
        for (int j = 3; j >= 0; --j) {
            ret[j] |= ((val >>> 8 * (3 - j)) & (0xff));
        }
        return ret;
    }

    /**
     * Convert a 4-element array into dotted decimal format
     * 
     * @param octets
     *            octets.
     * @return string.
     */
    private String format(int[] octets) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < octets.length; ++i) {
            str.append(octets[i]);
            if (i != octets.length - 1) {
                str.append(".");
            }
        }
        return str.toString();
    }

    /**
     * Convenience function to check integer boundaries. Checks if a value x is
     * in the range (begin,end]. Returns x if it is in range, throws an
     * exception otherwise.
     * 
     * @param value
     *            value.
     * @param begin
     *            begin
     * @param end
     *            end.
     * @return int.
     */
    private int rangeCheck(int value, int begin, int end) {
        if (value >= begin && value <= end) { // (begin,end]
            return value;
        }

        throw new IllegalArgumentException("Value [" + value
                + "] not in range (" + begin + "," + end + "]");
    }

    /**
     * Count the number of 1-bits in a 32-bit integer using a divide-and-conquer
     * strategy see Hacker's Delight section 5.1
     * 
     * @param x
     *            x.
     * @return int.
     */
    int pop(int x) {
        x = x - ((x >>> 1) & 0x55555555);
        x = (x & 0x33333333) + ((x >>> 2) & 0x33333333);
        x = (x + (x >>> 4)) & 0x0F0F0F0F;
        x = x + (x >>> 8);
        x = x + (x >>> 16);
        return x & 0x0000003F;
    }

    /**
     * Convert two dotted decimal addresses to a single xxx.xxx.xxx.xxx/yy
     * format by counting the 1-bit population in the mask address. (It may be
     * better to count NBITS-#trailing zeroes for this case)
     * 
     * @param addr
     *            addr.
     * @param mask
     *            mask.
     * @return string.
     */
    private String toCidrNotation(String addr, String mask) {
        return addr + "/" + pop(toInteger(mask));
    }
}
