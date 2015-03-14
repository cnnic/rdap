package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class EntityTelephoneDto extends BaseDto{
	private int pref;
	private String types;
	private String number;
	private String extNumber;
	
	public int getPref() {
		return pref;
	}
	public void setPref(int pref) {
		this.pref = pref;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getExtNumber() {
		return extNumber;
	}
	public void setExtNumber(String extNumber) {
		this.extNumber = extNumber;
	}
}
