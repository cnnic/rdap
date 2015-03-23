package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * IpAddressDto.
 * 
 * @author jiashuo.
 * 
 */
public class IpAddressDto extends BaseDto {
    /**
     * ipList.
     */
    private List<String> ipList;

    /**
     * get ipList.
     * 
     * @return ipList.
     */
    public List<String> getIpList() {
        return ipList;
    }

    /**
     * set ipList.
     * 
     * @param ipList
     *            ipList.
     */
    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }
}
