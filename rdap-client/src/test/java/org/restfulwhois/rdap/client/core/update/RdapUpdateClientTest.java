package org.restfulwhois.rdap.client.core.update;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;

public class RdapUpdateClientTest{
	RdapUpdateClient client = new RdapUpdateClient();
	
	
	@Test
	public void test_create_noerror() throws RdapClientException{
		IpDto dto = new IpDto();
		dto.setEndAddress("192.168.1.100");
		UpdateResponse response = client.create(dto);
		assertEquals(response.getHttpStatusCode(), 200);
	}
	
	@Test
	public void test_create_notlegal_dto_haserror(){
		PublicIdDto dto = new PublicIdDto();
		dto.setLang("chinese");
		UpdateResponse response;
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response, null);
	}
	
	@Test
	public void test_create_failToCreate_haserror() throws RdapClientException{
		IpDto dto = new IpDto();
		UpdateResponse response = client.create(dto);
		assertEquals(response.getHttpStatusCode(), 400);
		assertEquals(response.getErrorCode(), 400);
		assertEquals(response.getSubErrorCode(), 4002);
		assertEquals(response.getDescription(), "Property can¡¯t be empty");
	}
	
	
}