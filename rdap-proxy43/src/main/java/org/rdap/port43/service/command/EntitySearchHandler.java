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

import java.util.List;

import org.rdap.port43.service.ServiceException;

/**
 * entity search handler.
 * 
 * <pre>
 * whois entities fn=name_of_entity_search_string
 * whois entities handle=handle_of_entity_search_string
 * </pre>
 * 
 * @author jiashuo
 * 
 */
public class EntitySearchHandler extends QueryHandler {

    @Override
    public boolean supportCmd(Command command) {
        return CommandOption.ENTITY_SEARCH.equals(command.getCommandType());
    }

    @Override
    protected String getRelativeRequestURI(Command command) {
        List<String> argumentList = command.getArgumentList();
        throwExceptionIfArguementIsEmpty(argumentList);
        String uri = "entities?";
        String OPTION_FN = CommandOption.ENTITY_SEARCH_FN.getOption();
        String OPTION_HANDLE = CommandOption.ENTITY_SEARCH_HANDLE.getOption();
        if (isPrefixedArgument(argumentList.get(0), OPTION_FN + PARAM_SEPARATOR)) {
            uri =
                    uri + OPTION_FN + PARAM_SEPARATOR
                            + removePrefix(argumentList.get(0), OPTION_FN);
        } else if (isPrefixedArgument(argumentList.get(0), OPTION_HANDLE
                + PARAM_SEPARATOR)) {
            uri =
                    uri + OPTION_HANDLE + PARAM_SEPARATOR
                            + removePrefix(argumentList.get(0), OPTION_HANDLE);
        } else {
            throw new ServiceException("invalid argument");
        }
        return uri;
    }
}
