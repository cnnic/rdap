package org.restfulwhois.rdap.client.core.update.dto.base;

import java.util.List;

import org.restfulwhois.rdap.client.core.update.dto.embedded.EntityDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.EventDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.LinkDto;
import org.restfulwhois.rdap.client.core.update.dto.embedded.RemarkDto;
import org.restfulwhois.rdap.common.dto.BaseDto;


public class RdapClientDto extends BaseDto{
	private List<EntityDto> entities;
	private List<String> status;
	private List<RemarkDto> remarks;
	private List<LinkDto> links;
	private String port43;
	private List<EventDto> events;
	
	
	public List<EntityDto> getEntities() {
		return entities;
	}
	public void setEntities(List<EntityDto> entities) {
		this.entities = entities;
	}
	public List<String> getStatus() {
		return status;
	}
	public void setStatus(List<String> status) {
		this.status = status;
	}
	public List<RemarkDto> getRemarks() {
		return remarks;
	}
	public void setRemarks(List<RemarkDto> remarks) {
		this.remarks = remarks;
	}
	public List<LinkDto> getLinks() {
		return links;
	}
	public void setLinks(List<LinkDto> links) {
		this.links = links;
	}
	public String getPort43() {
		return port43;
	}
	public void setPort43(String port43) {
		this.port43 = port43;
	}
	public List<EventDto> getEvents() {
		return events;
	}
	public void setEvents(List<EventDto> events) {
		this.events = events;
	}
	
}