package org.restfulwhois.rdap.client.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.restfulwhois.rdap.client.common.type.HttpMethodType;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.springframework.http.HttpMethod;

public class RdapRestTemplate{
	private final String url;
	private final String MEDIA_TYPE;
	private final int connectTimeout;
	private final int readTimeout;
	
	public RdapRestTemplate(int connectTimeout, int readTimeout){
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.url = "";
		this.MEDIA_TYPE = "application/json;charset=UTF-8";
	}
	
	public String excute(String param, HttpMethodType httpMethod, ObjectType objectType) throws Exception{
		URL url = new URL(makeUrl(objectType));
		URLConnection urlConnection = url.openConnection();
		HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
		httpURLConnection.setConnectTimeout(connectTimeout);
		httpURLConnection.setReadTimeout(readTimeout);
		httpURLConnection.setDoInput(true);
		if(!httpMethod.equals(HttpMethodType.PUT)){
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setInstanceFollowRedirects(false);
		}else{
			httpURLConnection.setDoOutput(false);
			httpURLConnection.setInstanceFollowRedirects(true);
		}
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setRequestMethod(httpMethod.name());
		httpURLConnection.setRequestProperty("content-type", MEDIA_TYPE);
		httpURLConnection.connect();
		httpURLConnection.getOutputStream().write(param.getBytes());
		if(httpURLConnection.getResponseCode() != 200){
			//TODO
		}
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while((line = reader.readLine())!=null){
			sb.append(line);
		}
		
		return sb.toString();
	}

	private String makeUrl(ObjectType objectType){
		return makeUrl(null, objectType);
	}
	
	private String makeUrl(List<String> param, ObjectType objectType){
		StringBuilder requestUrl = new StringBuilder(url);
		requestUrl.append("/").append(objectType.name());
		if(param != null){
			for(String path : param){
				requestUrl.append("/").append(path);
			}
		}
		return requestUrl.toString();
	}
}