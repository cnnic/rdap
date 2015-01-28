package org.restfulwhois.rdap.client.common.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.client.common.type.HttpMethodType;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.JsonUtil;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

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
	
	public <T extends BaseDto> T excute(String param, ObjectType objectType, Class<T> model) 
			throws RdapClientException{
		try{
			URL url = new URL(makeUrl(objectType));
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
			prepareExcute(httpURLConnection, param, HttpMethodType.GET);
			
			int code = httpURLConnection.getResponseCode();
			String response;
			if(code != 200){
				throw new RdapClientException(httpURLConnection.getResponseMessage());
			}else{
				response = getResponseJSON(httpURLConnection.getInputStream());
			}
			return JsonUtil.responseConverter(response, model);
		}catch(IOException io){
			throw new RdapClientException(io.getMessage());
		}catch(RdapClientException e){
			throw e;
		}
	}
	
	
	public UpdateResponse excute(String param, HttpMethodType httpMethod, ObjectType objectType) 
			throws RdapClientException{
		try{
			URL url = new URL(makeUrl(objectType));
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
			prepareExcute(httpURLConnection, param, httpMethod);
			
			int code = httpURLConnection.getResponseCode();
			String response;
			if(code != 200){
				if(isOtherError(code)){
					return UpdateResponse.buildErrorResponse(
							0, code, httpURLConnection.getResponseMessage());
				}else{
					response = getResponseJSON(httpURLConnection.getErrorStream());
				}
			}else{
				response = getResponseJSON(httpURLConnection.getInputStream());
			}
			return JsonUtil.responseConverter(response);
		}catch(IOException io){
			throw new RdapClientException(io.getMessage());
		}catch(RdapClientException e){
			throw e;
		}
	}

	private void prepareExcute(HttpURLConnection httpURLConnection, 
			String param, HttpMethodType httpMethod) throws IOException{
		
		httpURLConnection.setConnectTimeout(connectTimeout);
		httpURLConnection.setReadTimeout(readTimeout);
		httpURLConnection.setDoInput(true);
		if(!httpMethod.equals(HttpMethodType.GET)){
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
		OutputStream out = httpURLConnection.getOutputStream();
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
		writer.write(param);
		writer.flush();
		writer.close();
		out.close();
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
	
	private boolean isOtherError(int code){
		switch(code){
		case 400:
		case 404:
		case 405:
		case 409:
		case 415:
		case 500:
			return false;
		default:
			return true;
		}
	}
	
	private String getResponseJSON(InputStream input) throws IOException{
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(new InputStreamReader(input, "utf-8"));
		String line;
		while((line = reader.readLine())!=null){
			sb.append(line);
		}
		return sb.toString();
	}
}