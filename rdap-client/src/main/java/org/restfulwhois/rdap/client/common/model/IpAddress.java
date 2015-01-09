package org.restfulwhois.rdap.client.common.model;

public class IpAddress{
	private String[] v4;
	private String[] v6;
	public String[] getV4() {
		return v4;
	}
	public void setV4(String[] v4) {
		this.v4 = v4;
	}
	public String[] getV6() {
		return v6;
	}
	public void setV6(String[] v6) {
		this.v6 = v6;
	}
	
}