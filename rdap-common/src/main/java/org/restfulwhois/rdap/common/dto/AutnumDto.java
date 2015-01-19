package org.restfulwhois.rdap.common.dto;


public class AutnumDto extends BaseDto{
	private int startAutnum;
	private int endAutnum;
	private String name;
	private String type;
	private String country;
	
	
	public int getStartAutnum() {
		return startAutnum;
	}
	public void setStartAutnum(int startAutnum) {
		this.startAutnum = startAutnum;
	}
	public int getEndAutnum() {
		return endAutnum;
	}
	public void setEndAutnum(int endAutnum) {
		this.endAutnum = endAutnum;
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
	
}