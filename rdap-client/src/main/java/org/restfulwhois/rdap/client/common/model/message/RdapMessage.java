package org.restfulwhois.rdap.client.common.model.message;

public class RdapMessage{
	private String handle;
	private String errorCode;
	private String subErrorCode;
	private String[] description;
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getSubErrorCode() {
		return subErrorCode;
	}
	public void setSubErrorCode(String subErrorCode) {
		this.subErrorCode = subErrorCode;
	}
	public String[] getDescription() {
		return description;
	}
	public void setDescription(String[] description) {
		this.description = description;
	}
}