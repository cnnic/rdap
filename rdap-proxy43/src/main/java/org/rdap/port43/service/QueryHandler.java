package org.rdap.port43.service;

/**
 * 
 * @author jiashuo
 * 
 */
public interface QueryHandler {
    /**
     * check if support this command.
     * 
     * @param commandStr
     *            command string.
     * @return true if support, false if not.
     */
    boolean supportCmd(String commandStr);

    /**
     * generate request URI, used to send request to RDAP server.
     * 
     * @return request URI.
     */
    String generateRequestURI();
}
