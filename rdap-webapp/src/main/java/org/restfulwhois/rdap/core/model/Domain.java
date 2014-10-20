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
package org.restfulwhois.rdap.core.model;

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
@JsonPropertyOrder({
        "rdapConformance", "notices", "objectClassName", "handle", "ldhName",
        "unicodeName", "variants", "nameservers", "secureDNS", "entities",
        "status", "publicIds", "remarks", "links", "port43", "events",
        "network", "lang" })
public class Domain extends BaseModel {
    /**
     * domain type.
     * 
     * @author jiashuo
     * 
     */
    public enum DomainType {
        /**
         * all type.
         */
        DNR("dnr"), ARPA("arpa");
        /**
         * type name.
         */
        private String name;

        /**
         * constructor.
         * 
         * @param name
         *            name.
         */
        private DomainType(String name) {
            this.name = name;
        }

        /**
         * get name.
         * 
         * @return name.
         */
        public String getName() {
            return name;
        }

        /**
         * get domain type by name.
         * 
         * @param name
         *            name.
         * @return DomainType.
         */
        public static DomainType getByTypeName(String name) {
            DomainType[] types = DomainType.values();
            for (DomainType type : types) {
                if (type.getName().equals(name)) {
                    return type;
                }
            }
            return DomainType.DNR;
        }

    }

    /**
     * domain type.
     */
    @JsonIgnore
    private DomainType type;

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
    private List<Variants> variants;

    /**
     * list of nameservers.
     */
    private List<Nameserver> nameservers;

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
    /**
     * the object class name of a particular object as a string.
     */
    private ObjectClassNameEnum objectClassName = ObjectClassNameEnum.DOMAIN;
    
    /**
     * networkId.
     */
    @JsonIgnore
    private Long networkId;

    /**
     * check if is DNR domain.
     * 
     * @return true if is, false if not.
     */
    @JsonIgnore
    public boolean isDnrDomain() {
        return DomainType.DNR.equals(type);
    }

    /**
     * check if is ARPA domain.
     * 
     * @return true if is, false if not.
     */
    @JsonIgnore
    public boolean isArpaDomain() {
        return DomainType.ARPA.equals(type);
    }

    @Override
    public ModelType getObjectType() {
        return ModelType.DOMAIN;
    }
    
    @Override
    public QueryUri getUri() {
        return QueryUri.DOMAIN;
    }

    @Override
    public String generateLinkHref() {
        return getUri().getName() + getLdhName();
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
     * get variants.
     * 
     * @return variants.
     */
    public List<Variants> getVariants() {
        return variants;
    }

    /**
     * set variants.
     * 
     * @param variants
     *            variants.
     */
    public void setVariants(List<Variants> variants) {
        this.variants = variants;
    }

    /**
     * get nameservers.
     * 
     * @return nameservers.
     */
    public List<Nameserver> getNameservers() {
        return nameservers;
    }

    /**
     * set nameservers.
     * 
     * @param nameservers
     *            nameservers.
     */
    public void setNameservers(List<Nameserver> nameservers) {
        this.nameservers = nameservers;
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
     * get ObjectClassNameEnum.
     * 
     * @return objectClassName.
     */
    public ObjectClassNameEnum getObjectClassName() {
        return objectClassName;
    }

    /**
     * set ObjectClassNameEnum.
     * 
     * @param objectClassNameEnum
     *            objectClassName for set.
     */
    public void setObjectClassName(ObjectClassNameEnum objectClassName) {
        this.objectClassName = objectClassName;
    }

    /**
     * get type.
     * 
     * @return type.
     */
    public DomainType getType() {
        return type;
    }

    /**
     * set type.
     * 
     * @param type
     *            type.
     */
    public void setType(DomainType type) {
        this.type = type;
    }

    /**
     * get networkId.
     * @return networkId.
     */
    public Long getNetworkId() {
        return networkId;
    }
    /**
     * set networkId.
     * @param networkId networkId.
     */
    public void setNetworkId(Long networkId) {
        this.networkId = networkId;
    }

}