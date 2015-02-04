package org.restfulwhois.rdap.client.type;

import org.restfulwhois.rdap.client.exception.ExceptionMessage;
import org.restfulwhois.rdap.client.exception.RdapClientException;

public enum ObjectType {

    ip("IpDto"), autnum("AutnumDto"), domain("DomainDto"), entity("EntityDto"), nameserver(
            "NameserverDto"), help("");

    String dtoName;

    private ObjectType(String dtoName) {
        this.dtoName = dtoName;
    }

    public static ObjectType valueOf(Class<?> dtoClass)
            throws RdapClientException {
        String dtoName = dtoClass.getSimpleName();
        for (ObjectType type : ObjectType.values()) {
            if (type.dtoName.equals(dtoName)) {
                return type;
            }
        }

        throw new RdapClientException(
                ExceptionMessage.NOT_LEGAL_DTO_ERROR.getMessage());
    }

}