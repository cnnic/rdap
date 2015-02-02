package org.restfulwhois.rdap.client.core.update;

import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.client.common.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.common.type.HttpMethodType;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.JsonUtil;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

public class RdapUpdateClient{

	private int connectTimeout;
	private int readTimeout;
	private String url;
	
	public RdapUpdateClient(String url){
		connectTimeout = 3000;
		readTimeout = 10000;
		this.url = url;
	}
	
	public RdapUpdateClient(int connectTimeout, int readTimeout, String url){
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.url = url;
	}
	
	public UpdateResponse create(BaseDto dto) throws RdapClientException{
		return excute(dto, HttpMethodType.POST);
	} 
	
	public UpdateResponse update(BaseDto dto) throws RdapClientException{
		return excute(dto, HttpMethodType.PUT);
	}
	
	public UpdateResponse delete(String handle, ObjectType objectType) 
			throws RdapClientException{
		return excute(handle, objectType);	
	}
	
	private UpdateResponse excute(BaseDto dto, HttpMethodType httpMethod) 
			throws RdapClientException {
		ObjectType objectType = ObjectType.valueOf(dto.getClass());
		String param = JsonUtil.toJson(dto);
		RdapRestTemplate template = 
				new RdapRestTemplate(connectTimeout, readTimeout, url);
		return template.excute(param, httpMethod, objectType);
	}
	
	private UpdateResponse excute(String param, ObjectType objectType) 
			throws RdapClientException{
		RdapRestTemplate template = 
				new RdapRestTemplate(connectTimeout, readTimeout, url);
		return template.excute(param, HttpMethodType.DELETE, objectType);
	}
	
}