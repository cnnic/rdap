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

import java.util.List;

/**
 * 
 * @author jiashuo
 * 
 */
public class SecureDns extends BaseModel {
    /**
     * secureDns record type.
     * 
     * @author jiashuo
     * 
     */
    public enum SecureDNSRType {
        /**
         * secureDns data type.
         */
        DsData("dsData"), KeyData("keyData");
        /**
         * type name.
         */
        private String name;

        /**
         * default constructor.
         * 
         * @param name
         *            name.
         */
        private SecureDNSRType(String name) {
            this.name = name;
        }

        /**
         * get name.
         * 
         * @return name.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * boolean true if the zone has been signed, false otherwise.
     */
    private boolean zoneSigned;
    /**
     * boolean true if there are DS records in the parent, false otherwise.
     */
    private boolean delegationSigned;
    /**
     * representing the signature life time in seconds to be used when creating
     * the RRSIG DS record in the parent zone [RFC5910].
     */
    private Integer maxSigLife = null;
    /**
     * dsData.
     */
    private List<DsData> dsData;
    /**
     * keyData.
     */
    private List<KeyData> keyData;

    /**
     * get zoneSigned.
     * 
     * @return zoneSigned.
     */
    public boolean isZoneSigned() {
        return zoneSigned;
    }

    /**
     * set zoneSigned.
     * 
     * @param zoneSigned
     *            zoneSigned.
     */
    public void setZoneSigned(boolean zoneSigned) {
        this.zoneSigned = zoneSigned;
    }

    /**
     * get delegationSigned.
     * 
     * @return delegationSigned.
     */
    public boolean isDelegationSigned() {
        return delegationSigned;
    }

    /**
     * set delegationSigned.
     * 
     * @param delegationSigned
     *            delegationSigned.
     */
    public void setDelegationSigned(boolean delegationSigned) {
        this.delegationSigned = delegationSigned;
    }

    /**
     * get maxSigLife.
     * 
     * @return maxSigLife.
     */
    public Integer getMaxSigLife() {
        return maxSigLife;
    }

    /**
     * set maxSigLife.
     * 
     * @param maxSigLife
     *            maxSigLife.
     */
    public void setMaxSigLife(Integer maxSigLife) {
        this.maxSigLife = maxSigLife;
    }

    /**
     * get dsData.
     * 
     * @return dsData.
     */
    public List<DsData> getDsData() {
        return dsData;
    }

    /**
     * set dsData.
     * 
     * @param dsData
     *            dsData.
     */
    public void setDsData(List<DsData> dsData) {
        this.dsData = dsData;
    }

    /**
     * get keyData.
     * 
     * @return keyData.
     */
    public List<KeyData> getKeyData() {
        return keyData;
    }

    /**
     * set keyData.
     * 
     * @param keyData
     *            keyData.
     */
    public void setKeyData(List<KeyData> keyData) {
        this.keyData = keyData;
    }
}
