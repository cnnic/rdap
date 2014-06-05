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

/**
 * IP network registrations found in RIRs and is the expected response for the
 * "/ip" query as defined by [I-D.ietf-weirds-rdap-query].
 * 
 * @author jiashuo
 * 
 */
public class Network extends BaseModel {
    /**
     * the starting IP address of the network.
     */
    private String startAddress;
    /**
     * the ending IP address of the network.
     */
    private String endAddress;
    /**
     * @see IpVersion.
     */
    private IpVersion ipVersion;
    /**
     * an identifier assigned to the network registration by the registration
     * holder.
     */
    private String name;
    /**
     * a string containing an RIR-specific classification of the network.
     */
    private String type;
    /**
     * a string containing the name of the 2 character country code of the
     * network.
     */
    private String country;
    /**
     * a string containing an RIR-unique identifier of the parent network of
     * this network registration.
     */
    private String parentHandle;
    /**
     * an array of strings indicating the state of the IP network.
     */
    private List<String> status;
    /**
     * entities.
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

    /**
     * ip version:v4,v6.
     * 
     * @author jiashuo
     * 
     */
    public enum IpVersion {
        /**
         * The representation of IPv4 addresses in this document uses the
         * dotted-decimal notation described in [RFC1166]. The representation of
         * IPv6 addresses in this document follow the forms outlined in
         * [RFC5952].
         */
        V4("v4"), V6("v6");
        /**
         * a string signifying the IP protocol version of the network: "v4"
         * signifying an IPv4 network, "v6" signifying an IPv6 network.
         */
        private String name;

        /**
         * check ip version string is ipv4.
         * 
         * @param ipVersionStr
         *            ip version string.
         * @return true if is, false if not.
         */
        public static boolean isV4(String ipVersionStr) {
            if (V4.getName().equals(ipVersionStr)) {
                return true;
            }
            return false;
        }

        /**
         * check ip version string is ipv6.
         * 
         * @param ipVersionStr
         *            ip version string.
         * @return true if is, false if not.
         */
        public static boolean isV6(String ipVersionStr) {
            if (V6.getName().equals(ipVersionStr)) {
                return true;
            }
            return false;
        }
        /**
         * default constructor.
         * 
         * @param name
         *            ip version name.
         */
        private IpVersion(String name) {
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
    }

    /**
     * add a status string to status list.
     * 
     * @param statusStr
     *            status.
     */
    public void addStatus(String statusStr) {
        if (StringUtils.isBlank(statusStr)) {
            return;
        }
        if (null == this.status) {
            this.status = new ArrayList<String>();
        }
        this.status.add(statusStr);
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
     * get startAddress.
     * 
     * @return startAddress.
     */
    public String getStartAddress() {
        return startAddress;
    }

    /**
     * set startAddress.
     * 
     * @param startAddress
     *            startAddress.
     */
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    /**
     * get endAddress.
     * 
     * @return endAddress.
     */
    public String getEndAddress() {
        return endAddress;
    }

    /**
     * set endAddress.
     * 
     * @param endAddress
     *            endAddress.
     */
    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    /**
     * get ipVersion.
     * 
     * @return ipVersion.
     */
    public IpVersion getIpVersion() {
        return ipVersion;
    }

    /**
     * set ipVersion.
     * 
     * @param ipVersion
     *            ipVersion.
     */
    public void setIpVersion(IpVersion ipVersion) {
        this.ipVersion = ipVersion;
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
     * set name.
     * 
     * @param name
     *            name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get type.
     * 
     * @return type.
     */
    public String getType() {
        return type;
    }

    /**
     * set type.
     * 
     * @param type
     *            type.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * get country.
     * 
     * @return country.
     */
    public String getCountry() {
        return country;
    }

    /**
     * set country.
     * 
     * @param country
     *            country.
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * get parentHandle.
     * 
     * @return parentHandle.
     */
    public String getParentHandle() {
        return parentHandle;
    }

    /**
     * set parentHandle.
     * 
     * @param parentHandle
     *            parentHandle.
     */
    public void setParentHandle(String parentHandle) {
        this.parentHandle = parentHandle;
    }
}
