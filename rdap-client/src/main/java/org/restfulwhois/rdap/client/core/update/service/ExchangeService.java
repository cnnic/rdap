package org.restfulwhois.rdap.client.core.update.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.client.common.ClientHttpRequestInterceptorImpl;
import org.restfulwhois.rdap.client.common.model.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.PropertiesUtil;
import org.restfulwhois.rdap.client.core.query.model.Autnum;
import org.restfulwhois.rdap.client.core.query.model.Domain;
import org.restfulwhois.rdap.client.core.query.model.Entity;
import org.restfulwhois.rdap.client.core.query.model.Help;
import org.restfulwhois.rdap.client.core.query.model.Ip;
import org.restfulwhois.rdap.client.core.query.model.Nameserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class ExchangeService{
	
	@Autowired
	private RestTemplate restTemplate;
	
	private final String url = PropertiesUtil.getValue("localServiceUrl");
	private final static String MEDIA_TYPE = "application/json;chaset=UTF-8";
	
	public String doQuery(List<String> param, ObjectType type) throws RestClientException{
		ResponseEntity<?> responseEntity;
		
		switch(type){
		case ip:
			responseEntity = restTemplate.getForEntity(makeUrl(param, type), 
					Ip.class);break;
		case domain:
			responseEntity = restTemplate.getForEntity(makeUrl(param, type), 
					Domain.class);break;
		case autnum:
			responseEntity = restTemplate.getForEntity(makeUrl(param, type), 
					Autnum.class);break;
		case entity:
			responseEntity = restTemplate.getForEntity(makeUrl(param, type),
					Entity.class);break;
		case nameserver:
			responseEntity = restTemplate.getForEntity(makeUrl(param, type), 
					Nameserver.class);break;
		case help:
			responseEntity = restTemplate.getForEntity(makeUrl(param, type), 
					Help.class);break;
		default:
			responseEntity = null;break;
		}
		
		return responseEntity.getBody().toString();
	}
	
	
	public String doUpdate(String param, HttpMethod httpMethod, ObjectType objectType) throws RestClientException, URISyntaxException{
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
		ClientHttpRequestInterceptorImpl interceptor = new ClientHttpRequestInterceptorImpl(MEDIA_TYPE);
		interceptors.add(interceptor);
		restTemplate.setInterceptors(interceptors);
		
		HttpEntity<String> requestEntity = new HttpEntity<String>(param);
		URI uri = new URI(makeUrl(objectType));
		ResponseEntity<String> responseEntity = restTemplate.exchange(uri, httpMethod, requestEntity, String.class);
		
		return responseEntity.getBody().toString();
	}
	
	private String makeUrl(ObjectType objectType){
		/*StringBuilder requestUrl = new StringBuilder(url);
		requestUrl.append("/").append(objectType.toString());
		return requestUrl.toString();*/
		return makeUrl(null, objectType);
	}
	
	private String makeUrl(List<String> param, ObjectType objectType){
		StringBuilder requestUrl = new StringBuilder(url);
		requestUrl.append("/").append(objectType.toString());
		for(String path : param){
			requestUrl.append("/").append(path);
		}
		return requestUrl.toString();
	}
}