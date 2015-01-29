package org.restfulwhois.rdap.client.common.util;

import org.restfulwhois.rdap.client.common.exception.ExceptionMessage;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;

public class ObjectTypeUtil{
	
	public static ObjectType getObjectType(Object dto) 
			throws RdapClientException{
		if(dto instanceof IpDto){
			return ObjectType.ip; 
		}else if(dto instanceof AutnumDto){
			return ObjectType.autnum;
		}else if(dto instanceof EntityDto){
			return ObjectType.entity;
		}else if(dto instanceof DomainDto){
			return ObjectType.domain;
		}else if(dto instanceof NameserverDto){
			return ObjectType.nameserver;
		}else{
			String message = ExceptionMessage.NOT_LEGAL_DTO_EXCEPTION
					.getMessage();
			throw new RdapClientException(message);
		}
	}
	
	public static boolean checkIsLegalDto(Object dto){
		boolean flag = false;
		if(dto instanceof IpDto || dto instanceof AutnumDto
				|| dto instanceof EntityDto
				|| dto instanceof DomainDto
				|| dto instanceof NameserverDto){
			flag = true;
		}
		return flag;
	}
}