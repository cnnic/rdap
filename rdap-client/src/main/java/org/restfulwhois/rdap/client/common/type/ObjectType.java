package org.restfulwhois.rdap.client.common.type;

import org.restfulwhois.rdap.client.common.exception.ExceptionMessage;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;


public enum ObjectType{

	ip("IpDto"),
	autnum("AutnumDto"),
	domain("DomainDto"),
	entity("EntityDto"),
	nameserver("NameserverDto"),
	help("");
	
	String dtoName;
	
	private ObjectType(String dtoName){
		this.dtoName = dtoName;
	}
	
	public static ObjectType getObjectType(String dtoName) 
			throws RdapClientException{
		for(ObjectType type : ObjectType.values()){
			if(type.dtoName.equals(dtoName)){
				return type;
			}
		}
		
		throw new RdapClientException(
				ExceptionMessage.NOT_LEGAL_DTO_EXCEPTION.getMessage());
	}
	
}