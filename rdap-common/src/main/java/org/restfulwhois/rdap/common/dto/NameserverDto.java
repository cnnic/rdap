package org.restfulwhois.rdap.common.dto;

import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;

/**
 * NameserverDto.
 * 
 * @author jiashuo.
 * 
 */
public class NameserverDto extends BaseDto {
    /**
     * ldhName.
     */
    private String ldhName;
    /**
     * unicodeName.
     */
    private String unicodeName;
    /**
     * ipAddresses.
     */
    private IpAddressDto ipAddresses;

    /**
     * get ldhName.
     * 
     * @return ldhName.
     */
    public String getLdhName() {
        return ldhName;
    }

    /**
     * set ldhName.
     * 
     * @param ldhName
     *            ldhName.
     */
    public void setLdhName(String ldhName) {
        this.ldhName = ldhName;
    }

    /**
     * get unicodeName.
     * 
     * @return unicodeName.
     */
    public String getUnicodeName() {
        return unicodeName;
    }

    /**
     * set unicodeName.
     * 
     * @param unicodeName
     *            unicodeName.
     */
    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    /**
     * get ipAddresses.
     * 
     * @return ipAddresses.
     */
    public IpAddressDto getIpAddresses() {
        return ipAddresses;
    }

    /**
     * set ipAddresses.
     * 
     * @param ipAddresses
     *            ipAddresses.
     */
    public void setIpAddresses(IpAddressDto ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

}
