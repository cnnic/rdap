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
package org.restfulwhois.rdap.common.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * BaseDto.
 * 
 * @author jiashuo.
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseDto {
    /**
     * handle.
     */
    private String handle;
    /**
     * entities.
     */
    private List<EntityHandleDto> entities;
    /**
     * status.
     */
    private List<String> status;
    /**
     * remarks.
     */
    private List<RemarkDto> remarks;
    /**
     * links.
     */
    private List<LinkDto> links;
    /**
     * port43.
     */
    private String port43;
    /**
     * events.
     */
    private List<EventDto> events;
    /**
     * lang.
     */
    private String lang;
    /**
     * customProperties.
     */
    private Map<String, String> customProperties =
            new LinkedHashMap<String, String>();

    /**
     * add custom property.
     * 
     * @param key
     *            key.
     * @param value
     *            value.
     */
    public void addCustomProperty(String key, String value) {
        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) {
            customProperties.put(key, value);
        }
    }

    /**
     * get handle.
     * 
     * @return handle.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * set handle.
     * 
     * @param handle
     *            handle.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * get entities.
     * 
     * @return entities.
     */
    public List<EntityHandleDto> getEntities() {
        return entities;
    }

    /**
     * set entities.
     * 
     * @param entities
     *            entities.
     */
    public void setEntities(List<EntityHandleDto> entities) {
        this.entities = entities;
    }

    /**
     * get status.
     * 
     * @return status.
     */
    public List<String> getStatus() {
        return status;
    }

    /**
     * set status.
     * 
     * @param status
     *            status.
     */
    public void setStatus(List<String> status) {
        this.status = status;
    }

    /**
     * get remarks.
     * 
     * @return remarks.
     */
    public List<RemarkDto> getRemarks() {
        return remarks;
    }

    /**
     * set remarks.
     * 
     * @param remarks
     *            remarks.
     */
    public void setRemarks(List<RemarkDto> remarks) {
        this.remarks = remarks;
    }

    /**
     * get links.
     * 
     * @return links.
     */
    public List<LinkDto> getLinks() {
        return links;
    }

    /**
     * set links.
     * 
     * @param links
     *            links.
     */
    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }

    /**
     * get port43.
     * 
     * @return port43.
     */
    public String getPort43() {
        return port43;
    }

    /**
     * set port43.
     * 
     * @param port43
     *            port43.
     */
    public void setPort43(String port43) {
        this.port43 = port43;
    }

    /**
     * get events.
     * 
     * @return events.
     */
    public List<EventDto> getEvents() {
        return events;
    }

    /**
     * set events.
     * 
     * @param events
     *            events.
     */
    public void setEvents(List<EventDto> events) {
        this.events = events;
    }

    /**
     * get lang.
     * 
     * @return lang.
     */
    public String getLang() {
        return lang;
    }

    /**
     * set lang.
     * 
     * @param lang
     *            lang.
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * get customProperties.
     * 
     * @return customProperties.
     */
    public Map<String, String> getCustomProperties() {
        return customProperties;
    }

    /**
     * set customProperties.
     * 
     * @param customProperties
     *            customProperties.
     */
    public void setCustomProperties(Map<String, String> customProperties) {
        this.customProperties = customProperties;
    }

    /**
     * get update URI.
     * 
     * @return URI.
     */
    @JsonIgnore
    public String getUpdateUri() {
        return this.getClass().getSimpleName().replace("Dto", "").toLowerCase();
    }

}
