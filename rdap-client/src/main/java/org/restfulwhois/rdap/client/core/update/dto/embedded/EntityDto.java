package org.restfulwhois.rdap.client.core.update.dto.embedded;

import java.util.List;

public class EntityDto{
	private String handle;
	private List<String> roles;
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}