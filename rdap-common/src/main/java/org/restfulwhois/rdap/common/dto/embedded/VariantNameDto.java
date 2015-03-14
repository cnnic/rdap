package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class VariantNameDto extends BaseDto{
	private String ldhName;
	private String unicodeName;
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
	
	
}	
