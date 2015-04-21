package org.restfulwhois.rdap.client.exception;

/**
 * The exception message enum <br>
 * It is mainly used for conversion between DTO objects and JSON.
 * @author M.D.
 *
 */
public enum ExceptionMessage {

    /**
     * Dto object is not the instance of IpDto, AutnumDto, DomainDto, 
     * EntityDto or NameserverDto
     */
    NOT_LEGAL_DTO_ERROR("The dto object is not the instance of "
            + "IpDto, AutnumDto, DomainDto, EntityDto or NameserverDto."),

    /**
     * Convert dto object to json error
     */
    OBJECT_TO_JSON_ERROR("Convert object to json error:\n"),

    /**
     * Convert json to dto object error
     */
    JSON_TO_OBJECT_ERROR("Convert json to object error:\n"),

    /**
     * Set custom properties error
     */
    SET_CUSTOMPROPERTIES_ERROR("Set CustomProperties error:\n");

    /**
     * Exception message
     */
    String message;

    /**
     * Constructor
     * @param message the exception message
     */
    private ExceptionMessage(String message) {
        this.message = message;
    }

    /**
     * Get the exception message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
