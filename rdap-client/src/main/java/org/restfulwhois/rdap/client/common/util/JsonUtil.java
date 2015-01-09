package org.restfulwhois.rdap.client.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil{
	private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtil.class);
	private static ObjectMapper objectMapper = new ObjectMapper();;
	
	public static String toJson(Object dto){
		String json = null;
		try {
			return objectMapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			LOGGER.error("Convert dto into json error:{}", e);
		}
		return json;
	}
	
	public static <T extends BaseModel> T responseConverter(String response, Class<T> model) throws JsonParseException, JsonMappingException, IOException{

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		T t = objectMapper.readValue(response, model);
		t.setCustomProperties(JsonUtil.unidentifiedFields(response, model));
		return t;
	}
	
	public static UpdateResponse responseConverter(String response){
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		UpdateResponse updateResponse = null;
		try {
			updateResponse = objectMapper.readValue(response, UpdateResponse.class);
		} catch (IOException e) {
			LOGGER.error("Convert response into UpdateResponse error:{}", e);
		}
		return updateResponse;
	}
	
	public static Map<String, String> unidentifiedFields(String json, Class<?> model){
		Map<String, String> rtnMap = new HashMap<String, String>();
		try{
			JsonNode node = objectMapper.readTree(json);
			Iterator<String> it = node.fieldNames();
			Class<?> superClass = model.getSuperclass();
			ArrayList<Field> fieldList = new ArrayList<Field>(Arrays.asList(model.getDeclaredFields()));
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
			LOGGER.error("set CustomProperties error:{}", e);
		}
		return rtnMap;
	}
}