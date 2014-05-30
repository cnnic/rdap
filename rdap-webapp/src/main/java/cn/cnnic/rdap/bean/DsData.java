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
 * DsData as specified by RFC 4034.
 * 
 * @author jiashuo
 * 
 */
public class DsData extends BaseModel {
    /**
     * an integer as specified by the key tag field of a DNS DS record as
     * specified by RFC 4034 RFC 4034 [RFC4034] in presentation format.
     */
    private Integer keyTag = null;
    /**
     * an integer as specified by the algorithm field of a DNS DS record as
     * specified by RFC 4034 in presentation format.
     */
    private Integer algorithm = null;
    /**
     * a string as specified by the digest field of a DNS DS record as specified
     * by RFC 4034 in presentation format.
     */
    private String digest;
    /**
     * an integer as specified by the digest type field of a DNS DS record as
     * specified by RFC 4034 in presentation format.
     */
    private Integer digestType = null;
    /**
     * events.
     */
    private List<Event> events;
    /**
     * links.
     */
    private List<Link> links;

    /**
     * get keyTag.
     * 
     * @return keyTag.
     */
    public Integer getKeyTag() {
        return keyTag;
    }

    /**
     * set keyTag.
     * 
     * @param keyTag
     *            keyTag.
     */
    public void setKeyTag(Integer keyTag) {
        this.keyTag = keyTag;
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
     * get digest.
     * 
     * @return digest.
     */
    public String getDigest() {
        return digest;
    }

    /**
     * set digest.
     * 
     * @param digest
     *            digest.
     */
    public void setDigest(String digest) {
        this.digest = digest;
    }

    /**
     * get digestType.
     * 
     * @return digestType.
     */
    public int getDigestType() {
        return digestType;
    }

    /**
     * set digestType.
     * 
     * @param digestType
     *            digestType.
     */
    public void setDigestType(int digestType) {
        this.digestType = digestType;
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
