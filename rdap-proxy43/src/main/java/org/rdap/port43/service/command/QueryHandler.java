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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.rdap.port43.service.ServiceException;
import org.rdap.port43.util.RdapProperties;

/**
 * abstract query handler.
 * 
 * @author jiashuo
 * 
 */
public abstract class QueryHandler {
    /**
     * RDAP server base URL.
     */
    private static final String RDAP_SERVER_BASE_URL = RdapProperties
            .getRdapServerBaseUrl();

    /**
     * check if support this command.
     * 
     * @param command
     *            command.
     * @return true if support, false if not.
     */
    public boolean supportCmd(Command command) {
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }

    /**
     * generate URI to query RDAP server.
     * 
     * @return URI.
     */
    public String generateRequestURI(Command command) {
        String relativeURI = this.getRelativeRequestURI(command);
        return RDAP_SERVER_BASE_URL + relativeURI;
    }

    /**
     * generate request URI, used to send request to RDAP server.
     * 
     * @return request URI.
     */
    protected String getRelativeRequestURI(Command command) {
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }

    /**
     * check if q is search pattern.
     * 
     * @param q
     *            q.
     * @return true if q is search pattern, false if not.
     */
    protected boolean isSearchPattern(String q) {
        return StringUtils.contains(q, "*");
    }

    /**
     * check if argument is empty.
     * 
     * @param argumentList
     *            argument list.
     * @throws ServiceException
     *             if argument is empty.
     */
    protected void throwExceptionIfArguementIsEmpty(List<String> argumentList)
            throws ServiceException {
        if (null == argumentList || argumentList.isEmpty()) {
            throw new ServiceException("invalid argument");
        }
        String argument = argumentList.get(0);
        if (StringUtils.isBlank(argument)) {
            throw new ServiceException("invalid argument");
        }
    }

    /**
     * check if command contains option.
     * 
     * @param option
     *            option.
     * @param command
     *            command.
     * @return true if contains,false if not.
     */
    protected boolean containsOption(String option, Command command) {
        if (StringUtils.isBlank(option)) {
            return false;
        }
        boolean contains = false;
        for (Iterator<String> keyIt =
                command.getAllOptionsMap().keySet().iterator(); keyIt.hasNext();) {
            String key = keyIt.next();
            if (option.equals(key)) {
                contains = true;
                break;
            }
        }
        return contains;
    }

    /**
     * get value of command option.
     * 
     * @param option
     *            option.
     * @param command
     *            command.
     * @return value of option.
     */
    protected String getValueOfOption(String option, Command command) {
        if (StringUtils.isBlank(option)) {
            return StringUtils.EMPTY;
        }
        String result = StringUtils.EMPTY;
        Map<String, String> allOptionsMap = command.getAllOptionsMap();
        for (Iterator<String> keyIt = allOptionsMap.keySet().iterator(); keyIt
                .hasNext();) {
            String key = keyIt.next();
            if (option.equals(key)) {
                result = allOptionsMap.get(key);
                break;
            }
        }
        return result;
    }

    /**
     * check if is prefixed agrument.
     * 
     * @param prefixedArgument
     *            prefixedArgument.
     * @param prefix
     *            prefix.
     * @return true if prefixed,false if not.
     */
    protected boolean
            isPrefixedArgument(String prefixedArgument, String prefix) {
        if (StringUtils.isBlank(prefixedArgument)) {
            return false;
        }
        return StringUtils.startsWith(prefixedArgument, prefix);
    }

    /**
     * get prefixed agrument.
     * 
     * @param prefixedArgument
     *            prefixedArgument.
     * @param prefix
     *            prefix.
     * @return value.
     */
    protected String removePrefix(String prefixedArgument, String prefix) {
        if (StringUtils.isBlank(prefixedArgument)) {
            return StringUtils.EMPTY;
        }
        return StringUtils.removeStart(prefixedArgument, prefix + "=");
    }
}
