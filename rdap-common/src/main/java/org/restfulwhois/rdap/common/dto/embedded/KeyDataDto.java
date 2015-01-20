package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class KeyDataDto extends BaseDto{
	private int flags;
	private int protocol;
	private int algorithm;
	private String publicKey;
	private List<EventDto> events;
	public int getFlags() {
		return flags;
	}
	public void setFlags(int flags) {
		this.flags = flags;
	}
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
	public int getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(int algorithm) {
		this.algorithm = algorithm;
	}
	public String getPublicKey() {
		return publicKey;
	}
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public List<EventDto> getEvents() {
		return events;
	}
	public void setEvents(List<EventDto> events) {
		this.events = events;
	}
	
}
