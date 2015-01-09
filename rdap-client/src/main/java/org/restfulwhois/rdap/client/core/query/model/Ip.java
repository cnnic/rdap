package org.restfulwhois.rdap.client.core.query.model;

import org.restfulwhois.rdap.client.common.model.BaseModel;


public class Ip extends BaseModel{
	/**
     * representing a registry unique identifier of the ip object instance.
     */
    //private String handle;
    /**
     * startAddress labels as described by [//].
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
    //private String type;
    /**
     * represents the country.
     */
    //private String country;
    /**
     * represents the parentHandle.
     */
    private String parentHandle;
    /**
     * entities.
     */
    //private List<Entity> entities;
    
//	public String getHandle() {
//		return handle;
//	}
//	public void setHandle(String handle) {
//		this.handle = handle;
//	}
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
//	public String getType() {
//		return type;
//	}
//	public void setType(String type) {
//		this.type = type;
//	}
//	public String getCountry() {
//		return country;
//	}
//	public void setCountry(String country) {
//		this.country = country;
//	}
	public String getParentHandle() {
		return parentHandle;
	}
	public void setParentHandle(String parentHandle) {
		this.parentHandle = parentHandle;
	}
	/*public List<Entity> getEntities() {
		return entities;
	}
	public void setEntities(List<Entity> entities) {
		this.entities = entities;
	}*/
	
}