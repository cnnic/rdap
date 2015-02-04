package org.restfulwhois.rdap.client.core.update;

import java.net.URL;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapResponse;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.type.HttpMethodType;
import org.restfulwhois.rdap.client.type.ObjectType;
import org.restfulwhois.rdap.client.util.JsonUtil;
import org.restfulwhois.rdap.client.util.URLUtil;
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
	
	public UpdateResponse create(BaseDto dto) throws RdapClientException{
		return execute(dto, HttpMethodType.POST, null, null);
	} 
	
	public UpdateResponse update(BaseDto dto) throws RdapClientException{
		return execute(dto, HttpMethodType.PUT, dto.getHandle(), null);
	}
	
	public UpdateResponse delete(String handle, ObjectType type)
			throws RdapClientException{
		return execute(null, HttpMethodType.DELETE, handle, type);	
	}
	
	private UpdateResponse execute(BaseDto dto, 
			HttpMethodType httpMethod, String handle, ObjectType type)
					throws RdapClientException{
		RdapRestTemplate template = new RdapRestTemplate();
		template.setConnectTimeout(connectTimeout);
		template.setReadTimeout(readTimeout);
		
		String body = JsonUtil.toJson(dto);
		String objectType;
		if(!httpMethod.equals(HttpMethodType.DELETE))
			objectType = ObjectType.valueOf(dto.getClass()).name();
		else
			objectType = type.name();
		URL url =  URLUtil.makeURLWithPath(this.url, objectType, "u", handle);
		RdapResponse response = template.execute(httpMethod, url, body);
		return response.getResponseBody(UpdateResponse.class);
	}

	public int getConnectTimeout() {
		return connectTimeout;
	}

	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}
	
}