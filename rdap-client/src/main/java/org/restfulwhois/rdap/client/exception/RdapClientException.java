package org.restfulwhois.rdap.client.exception;

/**
 * The rdap-client exception class.
 * @author M.D.
 *
 */
public class RdapClientException extends Exception {

    /**
     * Serial version UID
     */
    private static final long serialVersionUID = -8153426113324316809L;

    /**
     * Constructor
     * @param message the exception message
     */
    public RdapClientException(String message) {
        super(message);
    }

}
