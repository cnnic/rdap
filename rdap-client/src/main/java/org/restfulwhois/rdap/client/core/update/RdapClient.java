package org.restfulwhois.rdap.client.core.update;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.client.common.model.BaseModel;
import org.restfulwhois.rdap.client.common.model.RdapException;
import org.restfulwhois.rdap.client.common.model.message.RdapMessage;
import org.restfulwhois.rdap.client.common.model.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.JsonUtil;
import org.restfulwhois.rdap.client.core.query.model.Autnum;
import org.restfulwhois.rdap.client.core.query.model.Domain;
import org.restfulwhois.rdap.client.core.query.model.Entity;
import org.restfulwhois.rdap.client.core.query.model.Help;
import org.restfulwhois.rdap.client.core.query.model.Ip;
import org.restfulwhois.rdap.client.core.query.model.Nameserver;
import org.restfulwhois.rdap.client.core.update.service.ExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class RdapClient{
	@Autowired
	private ExchangeService service;

	
	public RdapMessage createObject(ObjectType objectType, String param){
		try {
			return callService(param, HttpMethod.POST, objectType);
		} catch (RestClientException | URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new RdapException();
		} catch (IOException e){
			throw new RdapException();
		}
	} 
	public RdapMessage updateObject(ObjectType objectType, String param){
		try {
			return callService(param, HttpMethod.PUT, objectType);
		} catch (RestClientException | URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new RdapException();
		} catch (IOException e){
			throw new RdapException();
		}
	}
	public RdapMessage deleteObject(ObjectType objectType, String param){
		try {
			return callService(param, HttpMethod.DELETE, objectType);	
		} catch (RestClientException | URISyntaxException e) {
			// TODO Auto-generated catch block
			throw new RdapException();
		} catch (IOException e){
			throw new RdapException();
		}
	}
	
	private RdapMessage callService(String param, HttpMethod httpMethod, ObjectType objectType) 
			throws RestClientException, URISyntaxException, JsonParseException, JsonMappingException, IOException{
		String reponse = service.doUpdate(param, HttpMethod.DELETE, objectType);
		return JsonUtil.toRdapMessage(reponse);	
	}
	
}