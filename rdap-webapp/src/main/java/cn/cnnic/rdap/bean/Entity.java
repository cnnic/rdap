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
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * represents the information of organizations, corporations, governments,
 * non-profits, clubs, individual persons, and informal groups of people.
 * 
 * @author jiashuo
 * 
 */
@JsonPropertyOrder({ "rdapConformance", "notices", "handle", "vcardArray",
        "roles", "publicIds", "entities", "remarks", "links", "events",
        "asEventActor", "status", "port43", "networks", "autnums",
        "resultsTruncated", "lang" })
public class Entity extends BaseModel {
    /**
     * a JSON vCard with the entity's contact information.
     */
    @JsonRawValue
    private String vcardArray;
    /**
     * an array of strings, each signifying the relationship an object would
     * have with its closest containing object.
     */
    private List<String> roles;
    /**
     * an array of publicIds.
     */
    private List<PublicId> publicIds;
    /**
     * an array of entities.
     */
    private List<Entity> entities;
    /**
     * an array of remarks.
     */
    private List<Remark> remarks;
    /**
     * an array of links.
     */
    private List<Link> links;
    /**
     * an array of events.
     */
    private List<Event> events;
    /**
     * each object in the array MUST NOT have an 'eventActor' member.These
     * objects denote that the entity is an event actor for the given events.
     */
    private List<Event> asEventActor;

    /**
     * an array of status.
     */
    private List<String> status;
    /**
     * port43.
     */
    private String port43;

    /**
     * 'resultsTruncated' used where a single object has been returned and data
     * in that object has been truncated.
     */
    private Boolean resultsTruncated = null;

    /**
     * an array of IP network objects.
     */
    private List<Network> networks;
    /**
     * an array of autnum objects.
     */
    private List<Autnum> autnums;

    /**
     * address.
     */
    @JsonIgnore
    private List<EntityAddress> addresses;
    /**
     * telephones.
     */
    @JsonIgnore
    private List<EntityTel> telephones;
    /**
     * kind.
     */
    @JsonIgnore
    private String kind;
    /**
     * fn.
     */
    @JsonIgnore
    private String fn;
    /**
     * email.
     */
    @JsonIgnore
    private String email;
    /**
     * title.
     */
    @JsonIgnore
    private String title;
    /**
     * org.
     */
    @JsonIgnore
    private String org;
    /**
     * url.
     */
    @JsonIgnore
    private String url;

    @Override
    public ModelType getObjectType() {
        return ModelType.ENTITY;
    }

    /**
     * add a status string to status list.
     * 
     * @param statusStr
     *            statusStr.
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

    /**
     * add a status string to role list.
     * 
     * @param roleStr
     *            roleStr.
     */
    public void addRole(String roleStr) {
        if (StringUtils.isBlank(roleStr)) {
            return;
        }
        roleStr = StringUtils.trim(roleStr);
        if (null == this.roles) {
            this.roles = new ArrayList<String>();
        }
        if (!this.roles.contains(roleStr)) {
            this.roles.add(roleStr);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getId()).append(getHandle())
                .toString();
    }

    /**
     * get vcardArray.
     * 
     * @return vcardArray.
     */
    public String getVcardArray() {
        return vcardArray;
    }

    /**
     * set vcardArray.
     * 
     * @param vcardArray
     *            vcardArray.
     */
    public void setVcardArray(String vcardArray) {
        this.vcardArray = vcardArray;
    }

    /**
     * get roles.
     * 
     * @return roles.
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * set roles.
     * 
     * @param roles
     *            roles.
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * get publicIds.
     * 
     * @return publicIds.
     */
    public List<PublicId> getPublicIds() {
        return publicIds;
    }

    /**
     * set publicIds.
     * 
     * @param publicIds
     *            publicIds.
     */
    public void setPublicIds(List<PublicId> publicIds) {
        this.publicIds = publicIds;
    }

    /**
     * get remarks.
     * 
     * @return remarks.
     */
    public List<Remark> getRemarks() {
        return remarks;
    }

    /**
     * set remarks.
     * 
     * @param remarks
     *            remarks.
     */
    public void setRemarks(List<Remark> remarks) {
        this.remarks = remarks;
    }

    /**
     * get links.
     * 
     * @return links.
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * set links.
     * 
     * @param links
     *            links.
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * get events.
     * 
     * @return events.
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * set events.
     * 
     * @param events
     *            events.
     */
    public void setEvents(List<Event> events) {
        this.events = events;
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
     * get networks.
     * 
     * @return networks.
     */
    public List<Network> getNetworks() {
        return networks;
    }

    /**
     * set networks.
     * 
     * @param networks
     *            networks.
     */
    public void setNetworks(List<Network> networks) {
        this.networks = networks;
    }

    /**
     * get autnums.
     * 
     * @return autnums.
     */
    public List<Autnum> getAutnums() {
        return autnums;
    }

    /**
     * set autnums.
     * 
     * @param autnums
     *            autnums.
     */
    public void setAutnums(List<Autnum> autnums) {
        this.autnums = autnums;
    }

    /**
     * get asEventActor.
     * 
     * @return asEventActor.
     */
    public List<Event> getAsEventActor() {
        return asEventActor;
    }

    /**
     * set asEventActor.
     * 
     * @param asEventActor
     *            asEventActor.
     */
    public void setAsEventActor(List<Event> asEventActor) {
        this.asEventActor = asEventActor;
    }

    /**
     * get entities.
     * 
     * @return entities.
     */
    public List<Entity> getEntities() {
        return entities;
    }

    /**
     * set entities.
     * 
     * @param entities
     *            entities.
     */
    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    /**
     * get addresses.
     * 
     * @return addresses.
     */
    public List<EntityAddress> getAddresses() {
        return addresses;
    }

    /**
     * set addresses.
     * 
     * @param addresses
     *            addresses.
     */
    public void setAddresses(List<EntityAddress> addresses) {
        this.addresses = addresses;
    }

    /**
     * get kind.
     * 
     * @return kind.
     */
    public String getKind() {
        return kind;
    }

    /**
     * set kind.
     * 
     * @param kind
     *            kind.
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * get fn.
     * 
     * @return fn.
     */
    public String getFn() {
        return fn;
    }

    /**
     * set fn.
     * 
     * @param fn
     *            fn.
     */
    public void setFn(String fn) {
        this.fn = fn;
    }

    /**
     * get email.
     * 
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email.
     * 
     * @param email
     *            email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get title.
     * 
     * @return title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * set title.
     * 
     * @param title
     *            title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get org.
     * 
     * @return org.
     */
    public String getOrg() {
        return org;
    }

    /**
     * set org.
     * 
     * @param org
     *            org.
     */
    public void setOrg(String org) {
        this.org = org;
    }

    /**
     * get url.
     * 
     * @return url.
     */
    public String getUrl() {
        return url;
    }

    /**
     * set url.
     * 
     * @param url
     *            url.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * get telephones.
     * 
     * @return telephones.
     */
    public List<EntityTel> getTelephones() {
        return telephones;
    }

    /**
     * set telephones.
     * 
     * @param telephones
     *            telephones.
     */
    public void setTelephones(List<EntityTel> telephones) {
        this.telephones = telephones;
    }

    /**
     * get resultsTruncated.
     * 
     * @return resultsTruncated.
     */
    public Boolean getResultsTruncated() {
        return resultsTruncated;
    }

    /**
     * set resultsTruncated.
     * 
     * @param resultsTruncated
     *            resultsTruncated.
     */
    public void setResultsTruncated(Boolean resultsTruncated) {
        this.resultsTruncated = resultsTruncated;
    }

}
