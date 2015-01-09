package org.restfulwhois.rdap.client.common.model.type;

public enum ObjectType{
	/*ip("ip");
	
	private final String value;
	
	TypeEnum(String value){
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}*/
	
	ip,
	autnum,
	domain,
	entity,
	nameserver,
	help;
	
}