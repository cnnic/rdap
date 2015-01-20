package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class PublicIdDto extends BaseDto{
	private String type;
	private String identifier;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
}
