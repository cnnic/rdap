package org.restfulwhois.rdap.client.common.model;

import java.util.List;
import java.util.Map;

public class BaseModel{
	private String handle;
	private String type;
	private String country;
	private String[] rdapConformance;
	private List<Notice> notices;
	private List<Remark> remarks;
	private String lang;
	private List<Link> links;
	private List<Event> events;
	private String[] status;
	private String port43;
	private List<PublicId> publicIds;
	private Map<String, String> unidentifiedFields;
	
	
	public String getHandle() {
		return handle;
	}
	public void setHandle(String handle) {
		this.handle = handle;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String[] getRdapConformance() {
		return rdapConformance;
	}
	public void setRdapConformance(String[] rdapConformance) {
		this.rdapConformance = rdapConformance;
	}
	public List<Notice> getNotices() {
		return notices;
	}
	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}
	public List<Remark> getRemarks() {
		return remarks;
	}
	public void setRemarks(List<Remark> remarks) {
		this.remarks = remarks;
	}
	public String getLang() {
		return lang;
	}
	public void setLang(String lang) {
		this.lang = lang;
	}
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public String[] getStatus() {
		return status;
	}
	public void setStatus(String[] status) {
		this.status = status;
	}
	public String getPort43() {
		return port43;
	}
	public void setPort43(String port43) {
		this.port43 = port43;
	}
	public List<PublicId> getPublicIds() {
		return publicIds;
	}
	public void setPublicIds(List<PublicId> publicIds) {
		this.publicIds = publicIds;
	}
	public Map<String, String> getUnidentifiedFields() {
		return unidentifiedFields;
	}
	public void setUnidentifiedFields(Map<String, String> unidentifiedFields) {
		this.unidentifiedFields = unidentifiedFields;
	}
}