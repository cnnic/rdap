package org.restfulwhois.rdap.client.core.query.model;

import org.restfulwhois.rdap.client.common.model.BaseModel;

public class Autnum extends BaseModel{
	private String startAutnum;
	private String endAutnum;
	public String getStartAutnum() {
		return startAutnum;
	}
	public void setStartAutnum(String startAutnum) {
		this.startAutnum = startAutnum;
	}
	public String getEndAutnum() {
		return endAutnum;
	}
	public void setEndAutnum(String endAutnum) {
		this.endAutnum = endAutnum;
	}
	
}