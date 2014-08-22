package org.rdap.port43.service.command;

import org.rdap.port43.service.QueryHandler;

/**
 * 
 * @author jiashuo
 * 
 */
public class DomainQueryHandler implements QueryHandler {

    public String getCommandOption() {
        return "";
    }

    @Override
    public boolean supportCmd(String commandStr) {
        return true;
    }

    @Override
    public String generateRequestURI() {
        return "http://rdap.restfulwhois.org/.well-known/rdap/domain/cnnic.cn";
    }
}
