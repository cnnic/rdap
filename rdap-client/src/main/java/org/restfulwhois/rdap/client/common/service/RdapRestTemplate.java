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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.client.common.type.HttpMethodType;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.JsonUtil;
import org.restfulwhois.rdap.client.common.util.URLUtil;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.SimpleHttpStatusCode;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

public class RdapRestTemplate{
	private final String MEDIA_TYPE;
	private final int connectTimeout;
	private final int readTimeout;
	
	public RdapRestTemplate(int connectTimeout, int readTimeout){
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.MEDIA_TYPE = "application/json;charset=UTF-8";
	}
	
	public <T> T execute(HttpMethodType httpMethod, URL url, Class<T> objectClass)
			throws RdapClientException{
		
		HttpURLConnection httpURLConnection = this.prepareExecute(url, httpMethod);
		return null;
	}
	
	public <T> T execute(HttpMethodType httpMethod, URL url, String body, Class<T> objectClass)
			throws RdapClientException{
		
		HttpURLConnection httpURLConnection = this.prepareExecute(url, httpMethod, body);
		return null;
	}
	
	
	
	public <T extends BaseDto> T execute(List<String> param, ObjectType objectType, Class<T> dtoClass) 
			throws RdapClientException{
		try{
			URL url = URLUtil.makeURLWithPath(objectType.name(), param);
			
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
			prepareExecute(httpURLConnection, null, HttpMethodType.GET);
			
			int code = httpURLConnection.getResponseCode();
			String response;
			if(code != 200){
				throw new RdapClientException(httpURLConnection.getResponseMessage());
			}else{
				response = getResponseJSON(httpURLConnection.getInputStream());
			}
			return JsonUtil.responseConverter(response, dtoClass);
		}catch(IOException io){
			throw new RdapClientException(io.getMessage());
		}catch(RdapClientException e){
			throw e;
		}
	}
	
	
	public UpdateResponse execute(String param, HttpMethodType httpMethod, ObjectType objectType) 
			throws RdapClientException{
		try{
			URL url;
			if(httpMethod.equals(HttpMethodType.POST)){
				url = URLUtil.makeURLWithPath(this.url, objectType.name());
			}else{
				url = new URL(makeUrl(objectType));
			}
			
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection httpURLConnection = (HttpURLConnection)urlConnection;
			prepareExecute(httpURLConnection, param, httpMethod);
			
			int code = httpURLConnection.getResponseCode();
			String response;
			if(code != 200){
				try{
					SimpleHttpStatusCode.valueOf(code);
					response = getResponseJSON(httpURLConnection.getErrorStream());
				}catch(IllegalArgumentException e){
					return UpdateResponse.buildErrorResponse(
							0, code, httpURLConnection.getResponseMessage());
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

	private HttpURLConnection prepareExecute(URL url, HttpMethodType httpMethod)
			throws RdapClientException{
		return prepareExecute(url, httpMethod, null);
	}
	
	private HttpURLConnection prepareExecute(URL url, 
			HttpMethodType httpMethod,
			String body) throws RdapClientException{
		try{
			HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
			httpURLConnection.setConnectTimeout(connectTimeout);
			httpURLConnection.setReadTimeout(readTimeout);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod(httpMethod.name());
			httpURLConnection.setDoInput(true);
			if(!httpMethod.equals(HttpMethodType.GET)){
				httpURLConnection.setDoOutput(true);
				httpURLConnection.setInstanceFollowRedirects(false);
				httpURLConnection.connect();
			}else{
				httpURLConnection.setDoOutput(false);
				httpURLConnection.setInstanceFollowRedirects(true);
				httpURLConnection.setRequestProperty("content-type", MEDIA_TYPE);
				httpURLConnection.connect();
				OutputStream out = httpURLConnection.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(out, "utf-8"));
				writer.write(body);
				writer.flush();
				writer.close();
				out.close();
			}
			return httpURLConnection;
		}catch(IOException e){
			throw new RdapClientException(e.getMessage());
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