package org.restfulwhois.rdap.client.core.query.model;

import java.util.List;

import org.restfulwhois.rdap.client.common.model.BaseModel;
import org.restfulwhois.rdap.client.common.model.Event;

public class Entity extends BaseModel{
	private String[] vCard;
	private String[] roles;
	private List<Event> asEventActor;
}