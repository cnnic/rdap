package org.restfulwhois.rdap.client.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.restfulwhois.rdap.client.common.exception.ExceptionMessage;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil{

	private static ObjectMapper objectMapper;
	
	static{
		objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	/**
	 * convert dto object to json for update.
	 */
	public static String toJson(Object dto) throws RdapClientException{
		try {
			return objectMapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			throw new RdapClientException(
					makeMessage(ExceptionMessage.DTO_TO_JSON_EXCEPTION, e));
		}
	}
	
	public static <T extends BaseDto> T responseConverter(String response, Class<T> model) 
			throws RdapClientException{
		try {
			T t = objectMapper.readValue(response, model);
			t.setCustomProperties(unidentifiedFields(response, model));
			return t;
		} catch (IOException e) {
			throw new RdapClientException(
					makeMessage(ExceptionMessage.JSON_TO_DTO_EXCEPTION, e));
		} catch (RdapClientException e){
			throw e;
		}
		
	}
	
	public static UpdateResponse responseConverter(String response) throws RdapClientException{

		UpdateResponse updateResponse = null;
		try {
			updateResponse = objectMapper.readValue(response, UpdateResponse.class);
		} catch (IOException e) {
			throw new RdapClientException(
					makeMessage(ExceptionMessage.JSON_TO_UPDATERESPONSE_EXCEPTION, e));
		}
		return updateResponse;
	}
	
	private static Map<String, String> unidentifiedFields(String json, Class<?> model) 
			throws RdapClientException{
		Map<String, String> rtnMap = new HashMap<String, String>();
		try{
			JsonNode node = objectMapper.readTree(json);
			Iterator<String> it = node.fieldNames();
			Class<?> superClass = model.getSuperclass();
			ArrayList<Field> fieldList = new ArrayList<Field>(
					Arrays.asList(model.getDeclaredFields()));
			if(superClass != null && !superClass.getName().equals(Object.class.getName())){
				fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
			}
			
			boolean identifiableFlag = true;
			String currentString;
			while(it.hasNext()){
				currentString = it.next();
				for(Field field : fieldList){
					if(currentString.equals(field.getName())){
						identifiableFlag = true;
						break;
					}else{
						identifiableFlag = false;
					}
				}
				if(!identifiableFlag){
					rtnMap.put(currentString, node.findValue(currentString).toString());
					identifiableFlag = true;
				}
			}
		}catch(IOException e){
			throw new RdapClientException(
					makeMessage(ExceptionMessage.SET_CUSTOMPROPERTIES_EXCEPTION, e));
		}
		return rtnMap;
	}
	
	private static String makeMessage(ExceptionMessage em, Exception e){
		return em.getMessage() + e.getMessage();
	}
}