package org.restfulwhois.rdap.client.core.update;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.client.common.type.ObjectType;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;

public class RdapUpdateClientTest{
	RdapUpdateClient client = new RdapUpdateClient();
	
	
	
	@Test
	public void test_create_noerror(){
		UpdateResponse response;

		IpDto dto = new IpDto();
		dto.setHandle("ip-handle1");
		dto.setStartAddress("192.168.1.1");
		dto.setEndAddress("192.168.1.100");
		dto.setIpVersion("v4");
		dto.setCountry("中国");
		Map<String, String> customProperties = new HashMap<String, String>();
		customProperties.put("custom1", "1");
		customProperties.put("custom2", "{\"property\":2}");
		dto.setCustomProperties(customProperties);
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_create_failToCreate_haserror(){
		UpdateResponse response;
		
		IpDto dto = new IpDto();
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 400);
		assertEquals(response.getErrorCode(), 400);
		assertEquals(response.getSubErrorCode(), 4002);
		assertEquals(response.getDescription(), "Property can't be empty");
	}
	
	@Test
	public void test_update_noerror(){
		UpdateResponse response;
		
		IpDto dto = new IpDto();
		dto.setHandle("ip-handle1");
		dto.setStartAddress("ff00::1111");
		dto.setEndAddress("ff00::ffff");
		dto.setIpVersion("v6");
		dto.setCountry("中国");
		Map<String, String> customProperties = new HashMap<String, String>();
		customProperties.put("custom1", "1");
		customProperties.put("custom2", "{\"property\":2}");
		try {
			response = client.update(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_update_failToUpdate_haserror(){
		UpdateResponse response;
		
		IpDto dto = new IpDto();
		try {
			response = client.update(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 400);
		assertEquals(response.getErrorCode(), 400);
		assertEquals(response.getSubErrorCode(), 4002);
		assertEquals(response.getDescription(), "Property can��t be empty");
	}
	
	@Test
	public void test_delete_noerror(){
		UpdateResponse response;
		
		try {
			response = client.delete("ip-handle1", ObjectType.ip);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_delete_failToDelete_haserror(){
		UpdateResponse response;
		
		try {
			response = client.delete("ip-handle2", ObjectType.nameserver);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 404);
		assertEquals(response.getErrorCode(), 404);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), "Object not found");
	}
	
	@Test
	public void test_create_Autnum(){
		UpdateResponse response;
		
		AutnumDto dto = new AutnumDto();
		dto.setHandle("autnum-handle1");
		List<EventDto> events = new ArrayList<EventDto>();
		EventDto eventDto = new EventDto();
		eventDto.setHandle("event-handle1");
		List<LinkDto> links = new ArrayList<LinkDto>();
		LinkDto linkDto = new LinkDto();
		linkDto.setHandle("link-handle1");
		links.add(linkDto);
		List<EntityHandleDto> entities = new ArrayList<EntityHandleDto>();
		EntityHandleDto entityHandleDto = new EntityHandleDto();
		entityHandleDto.setHandle("entityHandle-handle1");
		entities.add(entityHandleDto);
		linkDto.setEntities(entities);
		eventDto.setLinks(links);
		events.add(eventDto);
		dto.setEvents(events);
		
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_update_Autnum(){
		UpdateResponse response;
		
		AutnumDto dto = new AutnumDto();
		dto.setHandle("autnum-handle1");
		List<EventDto> events = new ArrayList<EventDto>();
		EventDto eventDto = new EventDto();
		eventDto.setHandle("event-handle1");
		eventDto.setEventActor("eventActor1");
		List<LinkDto> links = new ArrayList<LinkDto>();
		LinkDto linkDto = new LinkDto();
		linkDto.setHandle("link-handle1");
		links.add(linkDto);
		List<EntityHandleDto> entities = new ArrayList<EntityHandleDto>();
		EntityHandleDto entityHandleDto = new EntityHandleDto();
		entityHandleDto.setHandle("entityHandle-handle1");
		entities.add(entityHandleDto);
		linkDto.setEntities(entities);
		eventDto.setLinks(links);
		events.add(eventDto);
		dto.setEvents(events);
		
		try {
			response = client.update(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_delete_Autnum(){
		UpdateResponse response;
		
		try {
			response = client.delete("autnum-handle1", ObjectType.autnum);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_create_Domain(){
		UpdateResponse response;
		
		DomainDto dto = new DomainDto();
		dto.setHandle("domain-handle1");
		dto.setLdhName("ldhName1");
		SecureDnsDto secureDNS = new SecureDnsDto();
		secureDNS.setHandle("secureDNS-handle1");
		secureDNS.setDelegationSigned(false);
		List<DsDataDto> dsData = new ArrayList<DsDataDto>();
		DsDataDto dsDataDto = new DsDataDto();
		dsDataDto.setAlgorithm(1);
		dsData.add(dsDataDto);
		secureDNS.setDsData(dsData);
		dto.setSecureDNS(secureDNS);
		
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_update_Domain(){
		UpdateResponse response;
		
		DomainDto dto = new DomainDto();
		dto.setHandle("domain-handle1");
		dto.setLdhName("ldhName1");
		SecureDnsDto secureDNS = new SecureDnsDto();
		secureDNS.setHandle("secureDNS-handle1");
		secureDNS.setDelegationSigned(false);
		List<DsDataDto> dsData = new ArrayList<DsDataDto>();
		DsDataDto dsDataDto = new DsDataDto();
		dsDataDto.setAlgorithm(2);
		dsData.add(dsDataDto);
		secureDNS.setDsData(dsData);
		dto.setSecureDNS(secureDNS);
		
		try {
			response = client.update(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_delete_Domain(){
		UpdateResponse response;

		try {
			response = client.delete("domain-handle1", ObjectType.domain);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_create_Entity(){
		UpdateResponse response;
		
		EntityDto dto = new EntityDto();
		dto.setHandle("entity-handle1");
		dto.setFn("fn");
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_update_Entity(){
		UpdateResponse response;
		
		EntityDto dto = new EntityDto();
		dto.setHandle("entity-handle1");
		dto.setFn("fn1");
		try {
			response = client.update(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_delete_Entity(){
		UpdateResponse response;

		try {
			response = client.delete("entity-handle1", ObjectType.entity);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_create_Nameserver(){
		UpdateResponse response;
		
		NameserverDto dto = new NameserverDto();
		dto.setHandle("nameserver-handle1");
		dto.setLdhName("ldhName1");
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_update_Nameserver(){
		UpdateResponse response;
		
		NameserverDto dto = new NameserverDto();
		dto.setHandle("nameserver-handle1");
		dto.setLdhName("ldhName2");
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_delete_Nameserver(){
		UpdateResponse response;

		try {
			response = client.delete("nameserver-handle1", ObjectType.nameserver);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response.getHttpStatusCode(), 200);
		assertEquals(response.getErrorCode(), 0);
		assertEquals(response.getSubErrorCode(), 0);
		assertEquals(response.getDescription(), null);
	}
	
	@Test
	public void test_create_notlegal_dto_Exception(){
		UpdateResponse response;
		
		PublicIdDto dto = new PublicIdDto();
		dto.setLang("chinese");
		try {
			response = client.create(dto);
		} catch (RdapClientException e) {
			response = null;
		}
		assertEquals(response, null);
	}
}