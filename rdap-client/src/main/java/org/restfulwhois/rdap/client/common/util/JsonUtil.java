package org.restfulwhois.rdap.client.common.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.restfulwhois.rdap.client.common.model.BaseModel;
import org.restfulwhois.rdap.client.common.model.message.RdapMessage;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil{
	private static ObjectMapper objectMapper = new ObjectMapper();;
	
	public static <T extends BaseModel> T toBean(String response, Class<T> model) throws JsonParseException, JsonMappingException, IOException{

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		T t = objectMapper.readValue(response, model);
		t.setUnidentifiedFields(JsonUtil.unidentifiedFields(response, model));
		return t;
	}
	
	public static RdapMessage toRdapMessage(String json) throws JsonParseException, JsonMappingException, IOException{
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper.readValue(json, RdapMessage.class);
	}
	
	
	
	public static Map<String, String> unidentifiedFields(String json, Class<?> model) throws JsonProcessingException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(json);
		Iterator<String> it = node.fieldNames();
		Class<?> superClass = model.getSuperclass();
		ArrayList<Field> fieldList = new ArrayList<Field>(Arrays.asList(model.getDeclaredFields()));
		if(superClass != null && !superClass.getName().equals(Object.class.getName())){
			fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
		}
		
		Map<String, String> rtnMap = new HashMap<String, String>();
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
		return rtnMap;
	}
	
	public static void main(String[] args) throws JsonProcessingException, IOException{
		/*String json = "{\"handle\":\"XXXX-RIR\",\"lang\":\"haha\",\"startAddress\":\"2001:db8::0\",\"endAddress\":\"2001:db8::0:FFFF:FFFF:FFFF:FFFF:FFFF\",\"ipVersion\":\"v6\",\"name\":\"NET-RTR-1\",\"type\":\"DIRECT ALLOCATION\",\"country\":\"AU\",\"parentHandle\":\"YYYY-RIR\",\"status\":[\"allocated\"],\"remarks\":[{\"description\":[\"She sells sea shells down by the sea shore.\",\"Originally written by Terry Sullivan.\"]}],\"links\":[{\"value\":\"http://example.ent/ip/2001:db8::/48\",\"rel\":\"self\",\"href\":\"http://example.net/ip/2001:db8::/48\",\"type\":\"application/rdap+json\"},{\"value\":\"http://example.net/ip/2001:db8::/48\",\"rel\":\"up\",\"href\":\"http://example.net/ip/2001:C00::/23\",\"type\":\"application/rdap+json\"}],\"events\":[{\"eventAction\":\"registration\",\"eventDate\":\"1990-12-31T23:59:60Z\"},{\"eventAction\":\"last changed\",\"eventDate\":\"1991-12-31T23:59:60Z\"}],\"entities\":[{\"handle\":\"XXXX\",\"vcardArray\":[\"vcard\",[[\"version\",{},\"text\",\"4.0\"],[\"fn\",{},\"text\",\"Joe User\"],[\"kind\",{},\"text\",\"individual\"],[\"lang\",{\"pref\":\"1\"},\"language-tag\",\"fr\"],[\"lang\",{\"pref\":\"2\"},\"language-tag\",\"en\"],[\"org\",{\"type\":\"work\"},\"text\",\"Example\"],[\"title\",{},\"text\",\"Research Scientist\"],[\"role\",{},\"text\",\"Project Lead\"],[\"adr\",{\"type\":\"work\"},\"text\",[\"\",\"Suite 1234\",\"4321 Rue Somewhere\",\"Quebec\",\"QC\",\"G1V 2M2\",\"Canada\"]],[\"tel\",{\"type\":[\"work\",\"voice\"],\"pref\":\"1\"},\"uri\",\"tel:+1-555-555-1234;ext=102\"],[\"email\",{\"type\":\"work\"},\"text\",\"joe.user@example.com\"]]],\"roles\":[\"registrant\"],\"remarks\":[{\"description\":[\"She sells sea shells down by the sea shore.\",\"Originally written by Terry Sullivan.\"]}],\"links\":[{\"value\":\"http://example.net/entity/xxxx\",\"rel\":\"self\",\"href\":\"http://example.net/entity/xxxx\",\"type\":\"application/rdap+json\"}],\"events\":[{\"eventAction\":\"registration\",\"eventDate\":\"1990-12-31T23:59:60Z\"},{\"eventAction\":\"last changed\",\"eventDate\":\"1991-12-31T23:59:60Z\"}]}]}";
		
		IpQueryResponse ip = JsonUtil.toBean(json, IpQueryResponse.class);
		System.out.println(ip.getLang());*/
		/*String json = "{\"success\":false,\"error\":{\"errorCode\":400,\"description\":\"request URI error\"}}";
		RdapMessage message = JsonUtil.toRdapMessage(json);
		System.out.println(message.getSuccess());*/
		String a = "192:168";
		System.out.println(a.contains("."));

	}
}