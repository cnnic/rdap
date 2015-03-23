package org.restfulwhois.rdap.common.dto.embedded;

import java.util.List;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * KeyDataDto.
 * 
 * @author jiashuo
 * 
 */
public class KeyDataDto extends BaseDto {
    /**
     * flags.
     */
    private int flags;
    /**
     * protocol.
     */
    private int protocol;
    /**
     * algorithm.
     */
    private int algorithm;
    /**
     * publicKey.
     */
    private String publicKey;
    /**
     * events.
     */
    private List<EventDto> events;

    /**
     * get flags.
     * 
     * @return flags.
     */
    public int getFlags() {
        return flags;
    }

    /**
     * set flags.
     * 
     * @param flags
     *            flags.
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * get protocol.
     * 
     * @return protocol.
     */
    public int getProtocol() {
        return protocol;
    }

    /**
     * set protocol.
     * 
     * @param protocol
     *            protocol.
     */
    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    /**
     * get algorithm.
     * 
     * @return algorithm.
     */
    public int getAlgorithm() {
        return algorithm;
    }

    /**
     * set algorithm.
     * 
     * @param algorithm
     *            algorithm.
     */
    public void setAlgorithm(int algorithm) {
        this.algorithm = algorithm;
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
     * get events.
     * 
     * @return events events.
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

}
