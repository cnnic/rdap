package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class SecureDnsDto extends BaseDto{
    private boolean zoneSigned;
    private boolean delegationSigned;
    private int maxSigLife;
    private List<KeyDataDto> keyData;
    private List<DsDataDto> dsData;

    public boolean isZoneSigned() {
        return zoneSigned;
    }

    public void setZoneSigned(boolean zoneSigned) {
        this.zoneSigned = zoneSigned;
    }

    public boolean isDelegationSigned() {
        return delegationSigned;
    }

    public void setDelegationSigned(boolean delegationSigned) {
        this.delegationSigned = delegationSigned;
    }

    public int getMaxSigLife() {
        return maxSigLife;
    }

    public void setMaxSigLife(int maxSigLife) {
        this.maxSigLife = maxSigLife;
    }

    public List<KeyDataDto> getKeyData() {
        return keyData;
    }

    public void setKeyData(List<KeyDataDto> keyData) {
        this.keyData = keyData;
    }

    public List<DsDataDto> getDsData() {
        return dsData;
    }

    public void setDsData(List<DsDataDto> dsData) {
        this.dsData = dsData;
    }
}
