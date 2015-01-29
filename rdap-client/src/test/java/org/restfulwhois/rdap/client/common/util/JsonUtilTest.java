package org.restfulwhois.rdap.client.common.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;

public class JsonUtilTest{
	private String json = "{\"handle\":\"handle1\",\"entities\":null,\"status\""
			+ ":null,\"remarks\":null,\"links\":null,\"port43\":null,"
			+ "\"events\":null,\"lang\":null,\"customProperties\":{},"
			+ "\"ldhName\":null,\"unicodeName\":null,\"type\":null,"
			+ "\"networkHandle\":null,\"nameservers\":null,"
			+ "\"secureDNS\":{\"handle\":\"dns-handle1\",\"entities\":null,"
			+ "\"status\":null,\"remarks\":[{\"handle\":null,"
			+ "\"entities\":null,\"status\":null,\"remarks\":null,"
			+ "\"links\":null,\"port43\":null,\"events\":null,\"lang\":null,"
			+ "\"customProperties\":{},\"title\":null,"
			+ "\"description\":[\"description1\",\"description2\"]}],"
			+ "\"links\":null,\"port43\":null,\"events\":null,\"lang\":null,"
			+ "\"customProperties\":{},\"zoneSigned\":false,"
			+ "\"delegationSigned\":false,\"maxSigLife\":0,\"keyData\":null,"
			+ "\"dsData\":null},\"publicIds\":null,\"variants\":[]}";
	private String jsonCustom = "{\"handle\":\"handle1\",\"myCustom\":"
			+ "{\"innerCustom1\":\"1\",\"innerCustom2\":[\"1\",\"2\"]},"
			+ "\"entities\":null,\"status\":null,\"remarks\":null,\"links\":null,"
			+ "\"port43\":null,\"events\":null,\"lang\":null,\"customProperties\":{},"
			+ "\"ldhName\":null,\"unicodeName\":null,\"type\":null,\"networkHandle\":null,"
			+ "\"nameservers\":null,\"secureDNS\":{\"handle\":\"dns-handle1\","
			+ "\"entities\":null,\"status\":null,\"remarks\":[{\"handle\":null,"
			+ "\"entities\":null,\"status\":null,\"remarks\":null,\"links\":null,"
			+ "\"port43\":null,\"events\":null,\"lang\":null,\"customProperties\":{},"
			+ "\"title\":null,\"description\":[\"description1\",\"description2\"]}],"
			+ "\"links\":null,\"port43\":null,\"events\":null,\"lang\":null,"
			+ "\"customProperties\":{},\"zoneSigned\":false,\"delegationSigned\":false,"
			+ "\"maxSigLife\":0,\"keyData\":null,\"dsData\":null},\"publicIds\":null,"
			+ "\"variants\":[]}";
	
	private String responseString = "{\"handle\":\"domain-1\",\"errorCode\":400,"
			+ "\"subErrorCode\":4002,\"description\":"
			+ "[\" Property can¡¯t be empty: domainName\"]}";
	
	@Test
	public void test_toJson() throws RdapClientException{
		DomainDto domain = new DomainDto();
		domain.setHandle("handle1");
		
		
		RemarkDto remark = new RemarkDto();
		ArrayList<String> description = new ArrayList<String>();
		description.add("description1");
		description.add("description2");
		remark.setDescription(description);
		ArrayList<RemarkDto> remarks = new ArrayList<RemarkDto>();
		remarks.add(remark);
		
		SecureDnsDto dns = new SecureDnsDto();
		dns.setHandle("dns-handle1");
		dns.setRemarks(remarks);
		domain.setSecureDNS(dns);
		assertEquals(json, JsonUtil.toJson(domain));
	}
	
	@Test
	public void test_responseConverter() throws RdapClientException{
		DomainDto dto = JsonUtil.responseConverter(json, DomainDto.class);
		assertEquals("handle1", dto.getHandle());
		assertEquals("dns-handle1", dto.getSecureDNS().getHandle());
		assertEquals("description1", dto.getSecureDNS().getRemarks().get(0)
				.getDescription().get(0));
		assertEquals("description2", dto.getSecureDNS().getRemarks().get(0)
				.getDescription().get(1));
	}
	
	@Test
	public void test_responseConverter_custom() 
			throws RdapClientException{
		DomainDto dto = JsonUtil.responseConverter(jsonCustom, DomainDto.class);
		String customString = "{\"innerCustom1\":\"1\","
				+ "\"innerCustom2\":[\"1\",\"2\"]}";
		Map<String, String> custom = dto.getCustomProperties();
		assertEquals(customString, custom.get("myCustom"));
	}
	
	@Test
	public void test_responseConverter_updateresponse() 
			throws RdapClientException{
		UpdateResponse response = JsonUtil.responseConverter(responseString);
		assertEquals(400, response.getErrorCode());
		assertEquals(4002,response.getSubErrorCode());
		assertEquals("domain-1",response.getHandle());
		assertEquals("Property can¡¯t be empty: domainName",
				response.getDescription().get(0));
	}
	
}