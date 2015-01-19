package org.restfulwhois.rdap.client.core.update.dto;

import java.util.List;

import org.restfulwhois.rdap.client.core.update.dto.base.RdapClientDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.NameserverDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.SecureDnsDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.VariantDto;


public class DomainDto extends RdapClientDto{
	private String domainName;
	private List<VariantDto> variants;
	private List<NameserverDto> nameservers;
	private SecureDnsDto secureDNS;
	private List<PublicIdDto> publicIds;
	private String type;
	private String networkHandle;
	
	public String getDomainName() {
		return domainName;
	}
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	public List<VariantDto> getVariants() {
		return variants;
	}
	public void setVariants(List<VariantDto> variants) {
		this.variants = variants;
	}
	public List<NameserverDto> getNameservers() {
		return nameservers;
	}
	public void setNameservers(List<NameserverDto> nameservers) {
		this.nameservers = nameservers;
	}
	public SecureDnsDto getSecureDNS() {
		return secureDNS;
	}
	public void setSecureDNS(SecureDnsDto secureDNS) {
		this.secureDNS = secureDNS;
	}
	public List<PublicIdDto> getPublicIds() {
		return publicIds;
	}
	public void setPublicIds(List<PublicIdDto> publicIds) {
		this.publicIds = publicIds;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getNetworkHandle() {
		return networkHandle;
	}
	public void setNetworkHandle(String networkHandle) {
		this.networkHandle = networkHandle;
	}
	
}