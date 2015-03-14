package org.restfulwhois.rdap.common.dto;

import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;

public class NameserverDto extends BaseDto {
    private String ldhName;
    private String unicodeName;
    private IpAddressDto ipAddresses;

    public String getLdhName() {
        return ldhName;
    }

    public void setLdhName(String ldhName) {
        this.ldhName = ldhName;
    }

    public String getUnicodeName() {
        return unicodeName;
    }

    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    public IpAddressDto getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(IpAddressDto ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

}
