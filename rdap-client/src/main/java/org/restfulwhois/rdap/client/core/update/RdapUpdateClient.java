package org.restfulwhois.rdap.client.core.update;

import java.io.IOException;

import org.restfulwhois.rdap.client.common.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.common.type.HttpMethodType;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.JsonUtil;
import org.restfulwhois.rdap.client.core.update.dto.AutnumDto;
import org.restfulwhois.rdap.client.core.update.dto.DomainDto;
import org.restfulwhois.rdap.client.core.update.dto.EntityDto;
import org.restfulwhois.rdap.client.core.update.dto.IpDto;
import org.restfulwhois.rdap.client.core.update.dto.NameserverDto;
import org.restfulwhois.rdap.client.core.update.dto.base.RdapClientDto;
import org.restfulwhois.rdap.client.core.update.dto.factory.RdapClientDtoFactory;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RdapUpdateClient{

	private static final Logger LOGGER = LoggerFactory.getLogger(RdapUpdateClient.class);
	private int connectTimeout;
	private int readTimeout;
	
	public RdapUpdateClient(){
		connectTimeout = 3000;
		readTimeout = 10000;
	}
	
	public RdapUpdateClient(int connectTimeout, int readTimeout){
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	
	public RdapClientDtoFactory getDtoFactory(){
		return RdapClientDtoFactory.getInstance();
	}
	
	public UpdateResponse create(Object dto) throws Exception{
		ObjectType objectType = checkObjectType(dto);
		return excute(dto, HttpMethodType.POST, objectType);
	} 
	
	public UpdateResponse update(Object dto) throws Exception{
		ObjectType objectType = checkObjectType(dto);
		return excute(dto, HttpMethodType.PUT, objectType);
	}
	
	public UpdateResponse delete(Object dto) throws Exception{
		ObjectType objectType = checkObjectType(dto);
		return excute(dto, HttpMethodType.DELETE, objectType);	
	}
	
	private ObjectType checkObjectType(Object dto){
		
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
			return null;
		}
		
	}
	
	private UpdateResponse excute(Object dto, HttpMethodType httpMethod, ObjectType objectType) throws Exception {
	
		String param = JsonUtil.toJson(dto);
		String reponse = null;
		UpdateResponse updateResponse = null;
		if(param != null){
			try {
				reponse = new RdapRestTemplate(connectTimeout, readTimeout).excute(param, httpMethod, objectType);
				updateResponse = JsonUtil.responseConverter(reponse);
			} catch (IOException e) {
				LOGGER.error("Send request to server error:{}");
			}
		}
		return updateResponse;
	}
	
}