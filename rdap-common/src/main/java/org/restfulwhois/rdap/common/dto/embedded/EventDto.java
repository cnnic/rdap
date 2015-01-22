package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class EventDto extends BaseDto{
	private String eventAction;
	private String eventActor;
	private String eventDate;
	public String getEventAction() {
		return eventAction;
	}
	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}
	public String getEventActor() {
		return eventActor;
	}
	public void setEventActor(String eventActor) {
		this.eventActor = eventActor;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
}
