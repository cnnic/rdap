package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

public class EntityHandleDto extends HandleDto{
	private List<String> roles;
	
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
}
