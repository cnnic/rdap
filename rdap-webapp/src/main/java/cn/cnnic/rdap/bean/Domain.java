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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * represents a DNS name and point of delegation.
 * 
 * @author jiashuo
 * 
 */
@JsonPropertyOrder({ "rdapConformance", "notices", "handle", "ldhName",
        "unicodeName", "variants", "nameServers", "secureDNS", "entities",
        "status", "publicIds", "remarks", "links", "port43", "events",
        "network", "lang" })
public class Domain extends BaseModel {
    /**
     * representing a registry unique identifier of the domain object instance.
     */
    private String handle;
    /**
     * Textual representations of DNS names where the labels of the domain are
     * all "letters, digits, hyphen" labels as described by [RFC5890].
     */
    private String ldhName;
    /**
     * Textual representations of DNS names where one or more of the labels are
     * U-labels as described by [RFC5890].
     */
    private String unicodeName;

    /**
     * list of varients.
     */
    private List<Variants> varients;

    /**
     * list of nameServers.
     */
    private List<Nameserver> nameServers;

    /**
     * secureDNS.
     */
    @JsonProperty("secureDNS")
    private SecureDns secureDns;
    /**
     * entities.
     */
    private List<Entity> entities;
    /**
     * status.
     */
    private List<String> status;
    /**
     * publicId.
     */
    private List<PublicId> publicIds;
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
    /**
     * represents the IP network for which a reverse DNS domain is referenced.
     */
    private Network network;
    
    @Override
    public ModelType getObjectType() {
        return ModelType.DOMAIN;
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
        if(!this.status.contains(statusStr)){
            this.status.add(statusStr);
        }
    }

    /**
     * get handle.
     * 
     * @return handle
     */
    @Override
    public String getHandle() {
        return handle;
    }

    /**
     * set handle.
     * 
     * @param handle
     *            handle of domain
     */
    @Override
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * get ldhName.
     * 
     * @return domain name in LDH format
     */
    public String getLdhName() {
        return ldhName;
    }

    /**
     * set domain name in LDH format.
     * 
     * @param ldhName
     *            LHD formated domain name
     */
    public void setLdhName(String ldhName) {
        this.ldhName = ldhName;
    }

    /**
     * get domain name in unicode format.
     * 
     * @return domain name in unicode format
     */
    public String getUnicodeName() {
        return unicodeName;
    }

    /**
     * set domain name in unicode format.
     * 
     * @param unicodeName
     *            domain name in unicode format
     */
    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    /**
     * get varients.
     * 
     * @return varients.
     */
    public List<Variants> getVarients() {
        return varients;
    }

    /**
     * set varients.
     * 
     * @param varients
     *            varients.
     */
    public void setVarients(List<Variants> varients) {
        this.varients = varients;
    }

    /**
     * get nameServers.
     * 
     * @return nameServers.
     */
    public List<Nameserver> getnameServers() {
        return nameServers;
    }

    /**
     * set nameServers.
     * 
     * @param nameServers
     *            nameServers.
     */
    public void setNameServers(List<Nameserver> nameServers) {
        this.nameServers = nameServers;
    }

    /**
     * get secureDNS.
     * 
     * @return secureDNS.
     */
    public SecureDns getSecureDns() {
        return secureDns;
    }

    /**
     * set secureDNS.
     * 
     * @param secureDns
     *            secureDNS.
     */
    public void setSecureDns(SecureDns secureDns) {
        this.secureDns = secureDns;
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
     * get network.
     * 
     * @return network.
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * set network.
     * 
     * @param network
     *            network.
     */
    public void setNetwork(Network network) {
        this.network = network;
    }
    
    /**
     * get domain type .
     * 
     * @return domain type : ARPA or DOMAIN .
     *            
     */    
    @JsonIgnore
    public ModelType getDomainType() {
        if (null == this.ldhName) {
            return ModelType.DOMAIN;
        } else if (ldhName.endsWith("ip6.arpa")) {
            return ModelType.ARPA;
        } else if (ldhName.endsWith("in-addr.arpa")) {
            return ModelType.ARPA;
        }
        return ModelType.DOMAIN;
    }
}
