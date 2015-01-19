package org.restfulwhois.rdap.common.dto;

import org.restfulwhois.rdap.common.dto.embedded.IpAddressDto;



public class NameserverDto extends BaseDto{
	private String nsName;
	private IpAddressDto ipAddresses;
	
	public String getNsName() {
		return nsName;
	}
	public void setNsName(String nsName) {
		this.nsName = nsName;
	}
	public IpAddressDto getIpAddresses() {
		return ipAddresses;
	}
	public void setIpAddresses(IpAddressDto ipAddresses) {
		this.ipAddresses = ipAddresses;
	}
	
	
}
