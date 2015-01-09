package org.restfulwhois.rdap.client.core.query.model;

import java.util.List;

import org.restfulwhois.rdap.client.common.model.BaseModel;
import org.restfulwhois.rdap.client.common.model.IpAddress;

public class Nameserver extends BaseModel{
	private String ldhName;
	private String unicodeName;
	private List<IpAddress> ipAddresses;
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
	public List<IpAddress> getIpAddresses() {
		return ipAddresses;
	}
	public void setIpAddresses(List<IpAddress> ipAddresses) {
		this.ipAddresses = ipAddresses;
	}
	
}