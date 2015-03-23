package org.restfulwhois.rdap.common.dto.embedded;

import org.restfulwhois.rdap.common.dto.BaseDto;

/**
 * PublicIdDto.
 * 
 * @author jiashuo.
 * 
 */
public class PublicIdDto extends BaseDto {
    /**
     * type.
     */
    private String type;
    /**
     * identifier.
     */
    private String identifier;

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
     * get identifier.
     * 
     * @return identifier.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * set identifier.
     * 
     * @param identifier
     *            identifier.
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
