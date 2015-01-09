package org.restfulwhois.rdap.client.core.update.dto.factory;

import org.restfulwhois.rdap.client.core.update.dto.AutnumDto;
import org.restfulwhois.rdap.client.core.update.dto.DomainDto;
import org.restfulwhois.rdap.client.core.update.dto.EntityDto;
import org.restfulwhois.rdap.client.core.update.dto.IpDto;
import org.restfulwhois.rdap.client.core.update.dto.NameserverDto;

public class DtoFactory{
	public static IpDto createIpDto(){
		return new IpDto();
	}
	
	public static AutnumDto createAutnumDto(){
		return new AutnumDto();
	}
	
	public static DomainDto createDomainDto(){
		return new DomainDto();
	}
	
	public static EntityDto createEntityDto(){
		return new EntityDto();
	}
	
	public static NameserverDto createNameserverDto(){
		return new NameserverDto();
	}
}