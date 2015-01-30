package org.restfulwhois.rdap.client.common.type;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.restfulwhois.rdap.client.common.exception.ExceptionMessage;
import org.restfulwhois.rdap.client.common.exception.RdapClientException;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;

public class ObjectTypeTest{
	
	@Test
	public void test_valueOf_IpDto_noerror(){
		ObjectType type;
		try {
			type = ObjectType.valueOf(IpDto.class);
		} catch (RdapClientException e) {
			type = null;
		}
		assertEquals(ObjectType.ip, type);
	}
	
	@Test
	public void test_valueOf_DomainDto_noerror(){
		ObjectType type;
		try {
			type = ObjectType.valueOf(DomainDto.class);
		} catch (RdapClientException e) {
			type = null;
		}
		assertEquals(ObjectType.domain, type);
	}
	
	@Test
	public void test_valueOf_EntityDto_noerror(){
		ObjectType type;
		try {
			type = ObjectType.valueOf(EntityDto.class);
		} catch (RdapClientException e) {
			type = null;
		}
		assertEquals(ObjectType.entity, type);
	}
	
	@Test
	public void test_valueOf_NameserverDto_noerror(){
		ObjectType type;
		try {
			type = ObjectType.valueOf(NameserverDto.class);
		} catch (RdapClientException e) {
			type = null;
		}
		assertEquals(ObjectType.nameserver, type);
	}
	
	@Test
	public void test_valueOf_AutnumDto_noerror(){
		ObjectType type;
		try {
			type = ObjectType.valueOf(AutnumDto.class);
		} catch (RdapClientException e) {
			type = null;
		}
		assertEquals(ObjectType.autnum, type);
	}
	
	@Test
	public void test_valueOf_haserror(){
		ObjectType type;
		String message = null;
		try {
			type = ObjectType.valueOf(String.class);
		} catch (RdapClientException e) {
			type = null;
			message = e.getMessage();
		}
		assertEquals(null, type);
		assertEquals(ExceptionMessage.NOT_LEGAL_DTO_ERROR.getMessage(), message);
	}
}