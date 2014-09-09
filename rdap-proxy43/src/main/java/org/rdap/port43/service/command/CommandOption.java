/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.rdap.port43.service.command;

import java.util.ArrayList;
import java.util.List;

/**
 * command option.
 * 
 * @author jiashuo
 * 
 */
public enum CommandOption {
    /**
     * not with arg
     */
    IP_OR_DOMAIN_QUERY("", false, "query ip/domain"), DOMAIN_SEARCH("domains",
            false, "domain search"), ENTITY_QUERY("entity", false,
            "query entity by handle"), ENTITY_SEARCH("entities", false,
            "search entity by handle/name"), NAMESERVER_QUERY("nameserver",
            false, "query nameserver by name"), NAMESERVER_SEARCH(
            "nameservers", false, "search nameserver by name"), AS("as", false,
            "query as number"),

    /**
     * has arg
     */
    ENTITY_SEARCH_FN("fn", true, "search entity by name"),
    ENTITY_SEARCH_HANDLE("handle", true, "search entity by handle"),
    NAMESERVER_SEARCH_BY_IP("ip", true, "search nameserver by ip"),
    NAMESERVER_SEARCH_BY_NAME("name", true, "search nameserver by name"), ;

    /**
     * all options that are not with args.This is used to parse query type.
     */
    private static List<CommandOption> OPTIONS_NOT_WITH_ARGS =
            new ArrayList<CommandOption>();
    /**
     * init.
     */
    static {
        OPTIONS_NOT_WITH_ARGS.add(IP_OR_DOMAIN_QUERY);
        OPTIONS_NOT_WITH_ARGS.add(DOMAIN_SEARCH);
        OPTIONS_NOT_WITH_ARGS.add(ENTITY_QUERY);
        OPTIONS_NOT_WITH_ARGS.add(ENTITY_SEARCH);
        OPTIONS_NOT_WITH_ARGS.add(NAMESERVER_QUERY);
        OPTIONS_NOT_WITH_ARGS.add(NAMESERVER_SEARCH);
        OPTIONS_NOT_WITH_ARGS.add(AS);
    }
    /**
     * option.
     */
    private String option;

    /**
     * has argument or not.
     */
    private boolean hasArg;

    /**
     * description.
     */
    private String description;

    private CommandOption(String option) {
        this.option = option;
    }

    /**
     * constructor.
     * 
     * @param option
     *            option.
     * @param description
     *            description.
     */
    private CommandOption(String option, boolean hasArg, String description) {
        this.option = option;
        this.hasArg = hasArg;
        this.description = description;
    }

    /**
     * get CommandOption by option string.
     * 
     * @param optionStr
     *            :option string.
     * @return CommandOption.
     */
    public static CommandOption getByStr(String optionStr) {
        for (CommandOption option : OPTIONS_NOT_WITH_ARGS) {
            if (option.getOption().equals(optionStr)) {
                return option;
            }
        }
        return null;
    }

    /**
     * get option.
     * 
     * @return option.
     */
    public String getOption() {
        return option;
    }

    /**
     * get description.
     * 
     * @return description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * get hasArg.
     * 
     * @return hasArg.
     */
    public boolean isHasArg() {
        return hasArg;
    }

    /**
     * set hasArg.
     * 
     * @param hasArg
     *            .
     */
    public void setHasArg(boolean hasArg) {
        this.hasArg = hasArg;
    }

}
