package org.restfulwhois.rdap.client.core.update.dto;

import org.restfulwhois.rdap.client.core.update.dto.base.RdapClientDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.IpAddressDto;



public class NameserverDto extends RdapClientDto{
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