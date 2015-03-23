package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * SecureDnsDto.
 * 
 * @author jiashuo.
 * 
 */
public class SecureDnsDto extends BaseDto {
    /**
     * zoneSigned.
     */
    private boolean zoneSigned;
    /**
     * delegationSigned.
     */
    private boolean delegationSigned;
    /**
     * maxSigLife.
     */
    private int maxSigLife;
    /**
     * keyData list.
     */
    private List<KeyDataDto> keyData;
    /**
     * dsData list.
     */
    private List<DsDataDto> dsData;

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
    public int getMaxSigLife() {
        return maxSigLife;
    }

    /**
     * set maxSigLife.
     * 
     * @param maxSigLife
     *            maxSigLife.
     */
    public void setMaxSigLife(int maxSigLife) {
        this.maxSigLife = maxSigLife;
    }

    /**
     * get keyData.
     * 
     * @return keyData.
     */
    public List<KeyDataDto> getKeyData() {
        return keyData;
    }

    /**
     * set keyData.
     * 
     * @param keyData
     *            keyData.
     */
    public void setKeyData(List<KeyDataDto> keyData) {
        this.keyData = keyData;
    }

    /**
     * get list.
     * 
     * @return list.
     */
    public List<DsDataDto> getDsData() {
        return dsData;
    }

    /**
     * set list.
     * 
     * @param dsData
     *            list.
     */
    public void setDsData(List<DsDataDto> dsData) {
        this.dsData = dsData;
    }
}
