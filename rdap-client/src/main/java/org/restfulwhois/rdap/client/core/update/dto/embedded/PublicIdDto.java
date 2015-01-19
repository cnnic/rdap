package org.restfulwhois.rdap.client.core.update.dto.embedded;

public class PublicIdDto{
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