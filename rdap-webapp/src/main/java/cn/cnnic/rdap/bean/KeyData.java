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

import java.util.List;

/**
 * KeyData as specified by RFC 4034.
 * 
 * @author jiashuo
 * 
 */
public class KeyData extends BaseModel {
    /**
     * an integer representing the flags field value in the DNSKEY record
     * [RFC4034] in presentation format.
     */
    private Integer flags = null;
    /**
     * an integer representation of the protocol field value of the DNSKEY
     * record [RFC4034] in presentation format.
     */
    private Integer protocol = null;
    /**
     * a string representation of the public key in the DNSKEY record [RFC4034]
     * in presentation format.
     */
    private String publicKey;
    /**
     * an integer as specified by the algorithm field of a DNSKEY record as
     * specified by RFC 4034 [RFC4034] in presentation format.
     */
    private Integer algorithm = null;
    /**
     * events.
     */
    private List<Event> events;
    /**
     * links.
     */
    private List<Link> links;

    /**
     * get flags.
     * 
     * @return flags.
     */
    public Integer getFlags() {
        return flags;
    }

    /**
     * set flags.
     * 
     * @param flags
     *            flags.
     */
    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    /**
     * get protocol.
     * 
     * @return protocol.
     */
    public Integer getProtocol() {
        return protocol;
    }

    /**
     * set protocol.
     * 
     * @param protocol
     *            protocol.
     */
    public void setProtocol(Integer protocol) {
        this.protocol = protocol;
    }

    /**
     * get publicKey.
     * 
     * @return publicKey.
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * set publicKey.
     * 
     * @param publicKey
     *            publicKey.
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * get algorithm.
     * 
     * @return algorithm.
     */
    public Integer getAlgorithm() {
        return algorithm;
    }

    /**
     * set algorithm.
     * 
     * @param algorithm
     *            algorithm.
     */
    public void setAlgorithm(Integer algorithm) {
        this.algorithm = algorithm;
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
}
