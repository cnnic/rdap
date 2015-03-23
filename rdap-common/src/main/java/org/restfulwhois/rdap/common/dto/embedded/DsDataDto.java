package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * DsDataDto.
 * 
 * @author jiashuo
 * 
 */
public class DsDataDto extends BaseDto {
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
     * @param algorithm algorithm.
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
    public Integer getDigestType() {
        return digestType;
    }

    /**
     * set digestType.
     * 
     * @param digestType
     *            digestType.
     */
    public void setDigestType(Integer digestType) {
        this.digestType = digestType;
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
     * get links.
     * 
     * @return links links.
     */
    public List<LinkDto> getLinks() {
        return links;
    }

    /**
     * set links.
     * 
     * @param links
     *            .
     */
    public void setLinks(List<LinkDto> links) {
        this.links = links;
    }

}