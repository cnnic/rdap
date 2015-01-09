package org.restfulwhois.rdap.client.common.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class PropertiesUtil{
	private static Properties properties;
	private static final String PROPERTIES_NAME = "rdap.properties";
	
	private PropertiesUtil(){}
	
	static{
		if(properties == null){
			init();
		}
	}
	
	private static synchronized void init(){
		if(properties == null){
			try {
				Resource resource = new ClassPathResource(PROPERTIES_NAME);
				properties = new Properties();
				properties.load(resource.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static String getValue(String name){
		
		return properties.getProperty(name);
	}
}