package org.restfulwhois.rdap.common.dto;

/**
 * IpDto.
 * 
 * @author jiashuo.
 * 
 */
public class IpDto extends BaseDto {
    /**
     * startAddress.
     */
    private String startAddress;
    /**
     * endAddress as described by [//].
     */
    private String endAddress;
    /**
     * represents the IPVersion.
     */
    private String ipVersion;
    /**
     * represents the name.
     */
    private String name;
    /**
     * represents the type.
     */
    private String type;
    /**
     * represents the country.
     */
    private String country;
    /**
     * represents the parentHandle.
     */
    private String parentHandle;

    /**
     * network name cidr.
     */
    private String cidr;

    /**
     * get startAddress.
     * 
     * @return startAddress.
     */
    public String getStartAddress() {
        return startAddress;
    }

    /**
     * set startAddress.
     * 
     * @param startAddress
     *            startAddress.
     */
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    /**
     * get endAddress.
     * 
     * @return endAddress.
     */
    public String getEndAddress() {
        return endAddress;
    }

    /**
     * set endAddress.
     * 
     * @param endAddress
     *            endAddress.
     */
    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    /**
     * get ipVersion.
     * 
     * @return ipVersion.
     */
    public String getIpVersion() {
        return ipVersion;
    }

    /**
     * set ipVersion.
     * 
     * @param ipVersion
     *            ipVersion.
     */
    public void setIpVersion(String ipVersion) {
        this.ipVersion = ipVersion;
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
     * set name.
     * 
     * @param name
     *            name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get type.
     * 
     * @return type.
     */
    public String getType() {
        return type;
    }

    /**
     * set type.
     * 
     * @param type
     *            type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get country.
     * 
     * @return country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * set country.
     * 
     * @param country
     *            country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * get parentHandle.
     * 
     * @return parentHandle.
     */
    public String getParentHandle() {
        return parentHandle;
    }

    /**
     * set parentHandle.
     * 
     * @param parentHandle
     *            parentHandle.
     */
    public void setParentHandle(String parentHandle) {
        this.parentHandle = parentHandle;
    }

    /**
     * get cidr.
     * 
     * @return cidr.
     */
    public String getCidr() {
        return cidr;
    }

    /**
     * set cidr.
     * 
     * @param cidr
     *            cidr.
     */
    public void setCidr(String cidr) {
        this.cidr = cidr;
    }
}
