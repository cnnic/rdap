package org.restfulwhois.rdap.client.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

public class ClientHttpRequestInterceptorImpl implements ClientHttpRequestInterceptor{
	private String headerValue;
	
	public ClientHttpRequestInterceptorImpl(String headerValue){
		this.headerValue = headerValue;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest paramHttpRequest,
			byte[] paramArrayOfByte,
			ClientHttpRequestExecution paramClientHttpRequestExecution)
			throws IOException {
		HttpRequestWrapper requestWrapper = new HttpRequestWrapper(paramHttpRequest);
		List<MediaType> mediaType = new ArrayList<MediaType>();
		mediaType.add(MediaType.valueOf(headerValue));
	    requestWrapper.getHeaders().setAccept(mediaType);
	    
	    return paramClientHttpRequestExecution.execute(requestWrapper, paramArrayOfByte);

	}
	
}