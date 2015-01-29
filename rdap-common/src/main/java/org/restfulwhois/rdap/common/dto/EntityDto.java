package org.restfulwhois.rdap.common.dto;

import java.util.List;

import org.restfulwhois.rdap.common.dto.embedded.EntityAddressDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityTelephoneDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;


public class EntityDto extends BaseDto{
	private String fn;
	private String kind;
	private String email;
	private String title;
	private String org;
	private String url;
	private String lang;
	private List<EntityAddressDto> addresses;
	private List<EntityTelephoneDto> telephones;
	private List<PublicIdDto> publicIds;

	
	public String getFn() {
		return fn;
	}
	public void setFn(String fn) {
		this.fn = fn;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOrg() {
		return org;
	}
	public void setOrg(String org) {
		this.org = org;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}	
    public List<EntityTelephoneDto> getTelephones() {
        return telephones;
    }    
    public void setTelephones(List<EntityTelephoneDto> telephones) {
        this.telephones = telephones;
    }
    public List<EntityAddressDto> getAddresses() {
        return addresses;
    }    
    public void setAddresses(List<EntityAddressDto> addresses) {
        this.addresses = addresses;
    }
	public List<PublicIdDto> getPublicIds() {
		return publicIds;
	}
	public void setPublicIds(List<PublicIdDto> publicIds) {
		this.publicIds = publicIds;
	}
	
	
}
