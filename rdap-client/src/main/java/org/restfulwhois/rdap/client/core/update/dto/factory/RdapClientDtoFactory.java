package org.restfulwhois.rdap.client.core.update.dto.factory;

import org.restfulwhois.rdap.client.core.update.dto.AutnumDto;
import org.restfulwhois.rdap.client.core.update.dto.DomainDto;
import org.restfulwhois.rdap.client.core.update.dto.EntityDto;
import org.restfulwhois.rdap.client.core.update.dto.IpDto;
import org.restfulwhois.rdap.client.core.update.dto.NameserverDto;

public class RdapClientDtoFactory{
	private static RdapClientDtoFactory rdapClientDtoFactory = new RdapClientDtoFactory();
	
	private RdapClientDtoFactory(){}
	
	public static RdapClientDtoFactory getInstance(){
		
		return rdapClientDtoFactory;
	}
	
	public IpDto createIpDto(){
		return new IpDto();
	}
	
	public AutnumDto createAutnumDto(){
		return new AutnumDto();
	}
	
	public DomainDto createDomainDto(){
		return new DomainDto();
	}
	
	public EntityDto createEntityDto(){
		return new EntityDto();
	}
	
	public NameserverDto createNameserverDto(){
		return new NameserverDto();
	}
}