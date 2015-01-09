package org.restfulwhois.rdap.client.common.model;

public class Link{
	private String value;
	private String rel;
	private String[] hreflang;
	private String[] title;
	private String media;
	private String type;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String[] getHreflang() {
		return hreflang;
	}
	public void setHreflang(String[] hreflang) {
		this.hreflang = hreflang;
	}
	public String[] getTitle() {
		return title;
	}
	public void setTitle(String[] title) {
		this.title = title;
	}
	public String getMedia() {
		return media;
	}
	public void setMedia(String media) {
		this.media = media;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}