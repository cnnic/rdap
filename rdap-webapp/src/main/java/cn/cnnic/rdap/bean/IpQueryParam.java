/* Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and Numbers 
 * (ICANN) and China Internet Network Information Center (CNNIC)
 *
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 **Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer.
 * 
 **Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 **Neither the name of the ICANN, CNNIC nor the names of its contributors may be used to 
 * endorse or promote products derived from this software without specific prior written 
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package cn.cnnic.rdap.bean;

import java.math.BigDecimal;

import cn.cnnic.rdap.common.util.IpUtil;

/**
 * base query parameter bean.
 * 
 * @author weijunkai
 * 
 */
public class IpQueryParam extends QueryParam {
    /**
     * constructor.
     * 
     * @param q
     *            for query string.
     * @param punyName
     *            nameserver puny name.
     */
    public IpQueryParam(String strIp, long numMask, String version) {
        super(strIp);
        this.numMask = numMask;
        this.strIpVersion = version;
        initAllIpZero();
    }

    private void initAllIpZero() {
        ipQueryStartHigh = BigDecimal.valueOf(0);
        ipQueryEndHigh = BigDecimal.valueOf(0);
        ipQueryStartLow = BigDecimal.valueOf(0);
        ipQueryEndLow = BigDecimal.valueOf(0);
    }

    /**
     * mask for ip.
     */
    private long numMask;

    /**
     * start high segment for ip.
     */
    private BigDecimal ipQueryStartHigh;
    /**
     * start low segment for ip.
     */
    private BigDecimal ipQueryStartLow;
    /**
     * end high segment for ip.
     */
    private BigDecimal ipQueryEndHigh;
    /**
     * end low segment for ip.
     */
    private BigDecimal ipQueryEndLow;
    /**
     * version for ip.
     */
    private String strIpVersion;

    /**
     * get numMask.
     * 
     * @return numMask.
     */
    public long getNumMask() {
        return numMask;
    }

    /**
     * set numMask.
     * 
     * @param numMask
     *            numMask.
     */
    public void setNumMask(long numMask) {
        this.numMask = numMask;
    }

    /**
     * get start low segment of ip .
     * 
     */
    public BigDecimal getIpQueryStartLow() {
        return ipQueryStartLow;
    }

    /**
     * setIpQueryStartLow .
     * 
     * @param low
     *            start low segment of ip.
     */
    public void setIpQueryStartLow(BigDecimal low) {
        this.ipQueryStartLow = low;
    }

    /**
     * get end low segment of ip .
     * 
     */
    public BigDecimal getIpQueryEndLow() {
        return ipQueryEndLow;
    }

    /**
     * setIpQueryEndLow .
     * 
     * @param low
     *            end low segment of ip.
     */
    public void setIpQueryEndLow(BigDecimal low) {
        this.ipQueryEndLow = low;
    }

    /**
     * get high segment of ip .
     * 
     * @return the high query ip. BigDecimal.
     */
    public BigDecimal getIpQueryStartHigh() {
        return ipQueryStartHigh;
    }

    /**
     * setIpQueryHigh .
     * 
     * @param high
     *            high segment of ip.
     */
    public void setIpQueryStartHigh(BigDecimal high) {
        this.ipQueryStartHigh = high;
    }

    /**
     * get end high segment of ip .
     * 
     * @return the end high query ip. BigDecimal.
     */
    public BigDecimal getIpQueryEndHigh() {
        return ipQueryEndHigh;
    }

    /**
     * setIpQueryEndHigh .
     * 
     * @param high
     *            end high segment of ip.
     */
    public void setIpQueryEndHigh(BigDecimal high) {
        this.ipQueryEndHigh = high;
    }

    /**
     * parseQueryIpMask .
     */
    public void parseQueryIpMask() {
        String strQuery = getQ();
        if (strIpVersion.compareTo("v4") == 0) {
            final long numBase = 2;
            final long numBytes = 32;
            BigDecimal[] ipV4 = IpUtil.ipV4ToDecimal(strQuery);
            if (numMask > 0 && numMask <= 32) {
                final long maskV4High = (long) (Math.pow(numBase, numMask) - 1L);
                final long maskV4Low = (long) Math.pow(numBase, numBytes)
                        - maskV4High - 1L;
                long[] longIpV4 = IpUtil.ipToLong(strQuery);
                long ipStart = longIpV4[1] & maskV4Low;
                long ipEnd = longIpV4[1] | maskV4High;
                ipQueryEndLow = BigDecimal.valueOf(ipEnd);
                ipQueryStartLow = BigDecimal.valueOf(ipStart);
            } else {
                ipQueryEndLow = ipV4[1];
                ipQueryStartLow = ipV4[1];
            }
        } else if (strIpVersion.compareTo("v6") == 0) {
            BigDecimal[] ipV6 = IpUtil.ipV6ToBigDecimalJar(strQuery);
            if (numMask > 0 && numMask <= 128) {
                final int lowLimitShift = 64;
                int lowShift = (int) numMask;
                BigDecimal powDecimalBase = BigDecimal.valueOf(2L);
                BigDecimal endSubDecimalBase = BigDecimal.valueOf(1L);

                ipQueryStartHigh = ipV6[0];
                ipQueryEndHigh = ipV6[0];
                if (numMask > lowLimitShift) {
                    int highShift = (int) numMask - lowLimitShift;
                    // start high
                    BigDecimal powDecimalhigh = powDecimalBase.pow(highShift);
                    BigDecimal modDecimalHigh = ipV6[0]
                            .remainder(powDecimalhigh);
                    ipQueryStartHigh = ipV6[0].subtract(modDecimalHigh);
                    lowShift = lowLimitShift;
                    // endHigh 2^shift - 1
                    BigDecimal highEndPlusDecimal = powDecimalhigh
                            .subtract(endSubDecimalBase);
                    ipQueryEndHigh = ipQueryStartHigh.add(highEndPlusDecimal);
                }
                // start low
                BigDecimal powDecimalLow = powDecimalBase.pow(lowShift);
                BigDecimal modDecimalLow = ipV6[1].remainder(powDecimalLow);
                ipQueryStartLow = ipV6[1].subtract(modDecimalLow);
                // end Low
                BigDecimal lowEndPlusDecimal = powDecimalLow
                        .subtract(endSubDecimalBase);
                ipQueryEndLow = ipQueryStartLow.add(lowEndPlusDecimal);
            } else {
                ipQueryEndHigh = ipV6[0];
                ipQueryStartHigh = ipV6[0];
                ipQueryEndLow = ipV6[1];
                ipQueryStartLow = ipV6[1];
            }
        } else {
            initAllIpZero();
        }
    }

    /**
     * setQueryIpVersion .
     * 
     * @param version
     *            v4 or v6
     */
    public void setQueryIpVesion(String version) {
        this.strIpVersion = version;
    }

    /**
     * getQueryIpVersion .
     */
    public String getQueryIpVersion() {
        return strIpVersion;
    }
}
