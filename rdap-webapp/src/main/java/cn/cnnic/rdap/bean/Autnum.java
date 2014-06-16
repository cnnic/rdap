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
 * to the autnum registration
 * 
 * @author jiashuo
 * 
 */
@JsonPropertyOrder({ "rdapConformance", "notices", "handle", "startAutnum",
        "endAutnum", "name", "type", "status", "country", "remarks", "links",
        "events", "entities", "port43", "lang" })
public class Autnum extends BaseModel {
    /**
     * handle of autnum
     */
    private String handle;
    /**
     * start number
     */
    private Long startAutnum;
    /**
     * end number
     */
    private Long endAutnum;
    /**
     * name
     */
    private String name;
    /**
     * RIR-specific classification of the autnum
     */
    private String type;
    /**
     * 2 character country code of the autnum
     */
    private String country;
    /**
     * status
     */
    private List<String> status;
    /**
     * sub entities
     */
    private List<Entity> entities;
    /**
     * remarks
     */
    private List<Remark> remarks;
    /**
     * links
     */
    private List<Link> links;
    /**
     * port43
     */
    private String port43;
    /**
     * events
     */
    private List<Event> events;
    
    @Override
    public ModelType getObjectType() {
        return ModelType.AUTNUM;
    }

    /**
     * add a status string to status list
     * 
     * @param statusStr
     */
    public void addStatus(String statusStr) {
        if (StringUtils.isBlank(statusStr)) {
            return;
        }
        statusStr = StringUtils.trim(statusStr);
        if (null == this.status) {
            this.status = new ArrayList<String>();
        }
        if(!this.status.contains(statusStr)){
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

    public Long getStartAutnum() {
        return startAutnum;
    }

    public void setStartAutnum(Long startAutnum) {
        this.startAutnum = startAutnum;
    }

    public Long getEndAutnum() {
        return endAutnum;
    }

    public void setEndAutnum(Long endAutnum) {
        this.endAutnum = endAutnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<Remark> getRemarks() {
        return remarks;
    }

    public void setRemarks(List<Remark> remarks) {
        this.remarks = remarks;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getPort43() {
        return port43;
    }

    public void setPort43(String port43) {
        this.port43 = port43;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}