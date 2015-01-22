package org.restfulwhois.rdap.client.core.update.dto.embedded;

import java.util.List;

public class KeyDataDto{
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