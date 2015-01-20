package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

public class DsDataDto extends BaseDto{
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
    private List<EventDto> events;
    /**
     * links.
     */
    private List<LinkDto> links;

    public Integer getKeyTag() {
        return keyTag;
    }

    public void setKeyTag(Integer keyTag) {
        this.keyTag = keyTag;
    }

    public Integer getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(Integer algorithm) {
        this.algorithm = algorithm;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public Integer getDigestType() {
        return digestType;
    }

    public void setDigestType(Integer digestType) {
        this.digestType = digestType;
    }

    public List<EventDto> getEvents() {
        return events;
    }

    public void setEvents(List<EventDto> events) {
        this.events = events;
    }

    public List<LinkDto> getLinks() {
        return links;
    }

    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }

}