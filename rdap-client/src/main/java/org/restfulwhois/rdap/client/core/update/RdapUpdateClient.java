package org.restfulwhois.rdap.client.core.update;

import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.client.common.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.common.type.HttpMethodType;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.JsonUtil;
import org.restfulwhois.rdap.client.common.util.ObjectTypeUtil;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

public class RdapUpdateClient<T extends BaseDto>{

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
	
	public UpdateResponse create(T dto) throws RdapClientException{
		return excute(dto, HttpMethodType.POST);
	} 
	
	public UpdateResponse update(T dto) throws RdapClientException{
		return excute(dto, HttpMethodType.PUT);
	}
	
	public UpdateResponse delete(T dto) throws RdapClientException{
		return excute(dto, HttpMethodType.DELETE);	
	}
	
	private UpdateResponse excute(T dto, HttpMethodType httpMethod) throws RdapClientException {
		ObjectType objectType = ObjectTypeUtil.getObjectType(dto);
		String param = JsonUtil.toJson(dto);
		RdapRestTemplate template = new RdapRestTemplate(connectTimeout, readTimeout);
		return template.excute(param, httpMethod, objectType);
	}
	
}