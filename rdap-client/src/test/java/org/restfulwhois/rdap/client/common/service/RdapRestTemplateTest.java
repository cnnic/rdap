package org.restfulwhois.rdap.client.common.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.wink.client.MockHttpServer;
import org.apache.wink.client.MockHttpServer.MockHttpServerResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.client.common.type.HttpMethodType;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.client.common.util.JsonUtil;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

public class RdapRestTemplateTest{
	MockHttpServer mockHttpServer;
	String url = "http://127.0.0.1";
	int port = 8080;
	RdapRestTemplate template = new RdapRestTemplate(10000, 10000);
	String param = "{\"handle\":\"ip-handle1\",\"entities\":null,\"status\":null,"
			+ "\"remarks\":null,\"links\":null,\"port43\":null,\"events\":null,"
			+ "\"lang\":null,\"customProperties\":{\"custom1\":\"1\",\"custom2\":"
			+ "\"{\\\"property\\\":2}\"},\"startAddress\":\"192.168.1.1\","
			+ "\"endAddress\":\"192.168.1.100\",\"ipVersion\":\"v4\","
			+ "\"name\":null,\"type\":null,\"country\":\"±±¾©\",\"parentHandle\":null}";
	
	@Before
	public void startServer(){
		mockHttpServer = new MockHttpServer(port);
		mockHttpServer.startServer();
		
	}
	
	public void setContent(UpdateResponse updateResponse){  
        MockHttpServerResponse response = new MockHttpServerResponse();  
          
        try {
			response.setMockResponseContent(JsonUtil.toJson(updateResponse));
		} catch (RdapClientException e) {
			response.setMockResponseContent("{\"handle\":\"ip-handle1\"}");
		}  
        response.setMockResponseCode(200);  
        response.setMockResponseContentType("application/json");  

	    List<MockHttpServerResponse> list = mockHttpServer.getMockHttpServerResponses();  
	    list.add(response);  
	    mockHttpServer.setMockHttpServerResponses(response);  
    }
	
	@Test
	public void test_excute_post(){
		UpdateResponse response;
		setContent(UpdateResponse.buildSuccessResponse("ip-handle1"));
		try {
			response = template.excute(param, HttpMethodType.POST, ObjectType.ip);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_excute_put(){
		UpdateResponse response;
		try {
			response = template.excute(param, HttpMethodType.PUT, ObjectType.ip);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_excute_delete(){
		UpdateResponse response;
		try {
			response = template.excute("ip-handle1", HttpMethodType.DELETE, ObjectType.ip);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@After
	public void stopServer(){
		mockHttpServer.stopServer();
	}
}