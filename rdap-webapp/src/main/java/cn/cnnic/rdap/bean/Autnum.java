/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package cn.cnnic.rdap.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * consists of information about the network registration and entities related
 * to the autnum registration.
 * 
 * @author jiashuo
 * 
 */
@JsonPropertyOrder({ "rdapConformance", "notices", "handle", "startAutnum",
        "endAutnum", "name", "type", "status", "country", "remarks", "links",
        "events", "entities", "port43", "lang" })
public class Autnum extends BaseModel {
    /**
     * handle of autnum.
     */
    private String handle;
    /**
     * start number of autnum.
     */
    private Long startAutnum;
    /**
     * end number of autnum.
     */
    private Long endAutnum;
    /**
     * name of autnum.
     */
    private String name;
    /**
     * RIR-specific classification of the autnum.
     */
    private String type;
    /**
     * 2 character country code of the autnum.
     */
    private String country;
    /**
     * status of autnum.
     */
    private List<String> status;
    /**
     * sub entities.
     */
    private List<Entity> entities;
    /**
     * remarks.
     */
    private List<Remark> remarks;
    /**
     * links.
     */
    private List<Link> links;
    /**
     * port43.
     */
    private String port43;
    /**
     * events.
     */
    private List<Event> events;

    @Override
    public ModelType getObjectType() {
        return ModelType.AUTNUM;
    }

    /**
     * add a status string to status list.
     * 
     * @param statusStr
     *            for string of status
     */
    public void addStatus(String statusStr) {
        if (StringUtils.isBlank(statusStr)) {
            return;
        }
        statusStr = StringUtils.trim(statusStr);
        if (null == this.status) {
            this.status = new ArrayList<String>();
        }
        if (!this.status.contains(statusStr)) {
            this.status.add(statusStr);
        }
    }

    @Override
    public String getHandle() {
        return handle;
    }

    @Override
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * getStartAutnum.
     * 
     * @return startAutnum for long.
     */
    public Long getStartAutnum() {
        return startAutnum;
    }

    /**
     * setStartAutnum.
     * 
     * @param startAutnum
     *            the start long number of autnum.
     */
    public void setStartAutnum(Long startAutnum) {
        this.startAutnum = startAutnum;
    }

    /**
     * getEndAutnum.
     * 
     * @return end number of autnum.
     */
    public Long getEndAutnum() {
        return endAutnum;
    }

    /**
     * setEndAutnum.
     * 
     * @param endAutnum
     *            for long end number of autnum.
     */
    public void setEndAutnum(Long endAutnum) {
        this.endAutnum = endAutnum;
    }

    /**
     * getName.
     * 
     * @return string of name
     */
    public String getName() {
        return name;
    }

    /**
     * setName.
     * 
     * @param name
     *            string of name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * getType.
     * 
     * @return the type string.
     */
    public String getType() {
        return type;
    }

    /**
     * setType.
     * 
     * @param type
     *            string of type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getCountry.
     * 
     * @return string of country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * setCountry.
     * 
     * @param country
     *            string of country to set.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getStatus.
     * 
     * @return list string of status.
     */
    public List<String> getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *            list string of status to set.
     */
    public void setStatus(List<String> status) {
        this.status = status;
    }

    /**
     * getEntities.
     * 
     * @return get list of Entity object.
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * setEntities.
     * 
     * @param entities
     *            for list of entity to set.
     */
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     * getRemarks.
     * 
     * @return get list of remark.
     */
    public List<Remark> getRemarks() {
        return remarks;
    }

    /**
     * setRemarks.
     * 
     * @param remarks
     *            for list of remark object to set.
     */
    public void setRemarks(List<Remark> remarks) {
        this.remarks = remarks;
    }

    /**
     * getLinks.
     * 
     * @return get list of link object
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * setLinks.
     * 
     * @param links
     *            for list of links to set.
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * getPort43.
     * 
     * @return get string of port43
     */
    public String getPort43() {
        return port43;
    }

    /**
     * setPort43.
     * 
     * @param port43
     *            for the String to set port43
     */
    public void setPort43(String port43) {
        this.port43 = port43;
    }

    /**
     * getEvents.
     * 
     * @return list of events
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * setEvents.
     * 
     * @param events
     *            list of events to set
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }
}