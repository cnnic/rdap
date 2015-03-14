package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class EntityAddressDto extends BaseDto{
	private int pref;
	private String types;
	private String postbox;
	private String extendedAddress;
	private String streetAddress;
	private String locality;
	private String region;
	private String postalcode;
	private String country;
	public int getPref() {
		return pref;
	}
	public void setPref(int pref) {
		this.pref = pref;
	}
	public String getTypes() {
		return types;
	}
	public void setTypes(String types) {
		this.types = types;
	}
	public String getPostbox() {
		return postbox;
	}
	public void setPostbox(String postbox) {
		this.postbox = postbox;
	}
	public String getExtendedAddress() {
		return extendedAddress;
	}
	public void setExtendedAddress(String extendedAddress) {
		this.extendedAddress = extendedAddress;
	}
    public String getStreetAddress() {
        return streetAddress;
    }   
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }	
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
