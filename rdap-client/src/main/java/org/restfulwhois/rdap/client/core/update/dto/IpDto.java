package org.restfulwhois.rdap.client.core.update.dto;

import java.util.List;

import org.restfulwhois.rdap.common.dto.request.RdapRequest;

public class IpDto extends RdapRequest{
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
     * entities.
     */
    private List<EntityDto> entities;
	public String getStartAddress() {
		return startAddress;
	}
	public void setStartAddress(String startAddress) {
		this.startAddress = startAddress;
	}
	public String getEndAddress() {
		return endAddress;
	}
	public void setEndAddress(String endAddress) {
		this.endAddress = endAddress;
	}
	public String getIpVersion() {
		return ipVersion;
	}
	public void setIpVersion(String ipVersion) {
		this.ipVersion = ipVersion;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getParentHandle() {
		return parentHandle;
	}
	public void setParentHandle(String parentHandle) {
		this.parentHandle = parentHandle;
	}
	public List<EntityDto> getEntities() {
		return entities;
	}
	public void setEntities(List<EntityDto> entities) {
		this.entities = entities;
	}
}