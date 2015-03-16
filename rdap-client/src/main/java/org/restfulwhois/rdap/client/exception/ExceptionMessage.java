package org.restfulwhois.rdap.client.exception;

public enum ExceptionMessage {

    NOT_LEGAL_DTO_ERROR("The dto object is not a instance of "
            + "IpDto, AutnumDto, DomainDto, EntityDto or NameserverDto."),

    OBJECT_TO_JSON_ERROR("Convert object to json error:\n"),

    JSON_TO_OBJECT_ERROR("Convert json to object error:\n"),

    SET_CUSTOMPROPERTIES_ERROR("Set CustomProperties error:\n");

    String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
