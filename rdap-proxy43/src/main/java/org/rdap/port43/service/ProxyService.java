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
package org.rdap.port43.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.rdap.port43.service.command.Command;
import org.rdap.port43.service.command.CommandOption;
import org.rdap.port43.service.command.CommandParser;
import org.rdap.port43.service.command.DomainQueryHandler;
import org.rdap.port43.service.command.DomainSearchHandler;
import org.rdap.port43.service.command.EntityQueryHandler;
import org.rdap.port43.service.command.EntitySearchHandler;
import org.rdap.port43.service.command.ErrorQueryHandler;
import org.rdap.port43.service.command.IpQueryHandler;
import org.rdap.port43.service.command.NameserverSearchHandler;
import org.rdap.port43.service.command.QueryHandler;
import org.rdap.port43.service.format.ResponseFormater;
import org.rdap.port43.util.JsonUtil;

/**
 * proxy service.
 * 
 * @author jiashuo
 * 
 */
public class ProxyService {
    /**
     * singleton instance.
     */
    private static ProxyService proxyService = new ProxyService();
    /**
     * query handlers.
     * 
     * <pre>
     * Handlers ARE ordered.
     * </pre>
     */
    private List<QueryHandler> queryHandlers = new ArrayList<QueryHandler>();

    /**
     * default constructor.
     */
    public ProxyService() {
        super();
        queryHandlers.add(new DomainSearchHandler());
        queryHandlers.add(new EntityQueryHandler());
        queryHandlers.add(new EntitySearchHandler());
        queryHandlers.add(new NameserverSearchHandler());
        queryHandlers.add(new IpQueryHandler());// MUST before domain query.
        queryHandlers.add(new DomainQueryHandler());// MUST at last.
    }

    /**
     * get singleton instance.
     * 
     * @return ProxyService.
     */
    public static ProxyService getInstance() {
        return proxyService;
    }

    /**
     * main service method.This will parse command, construct URI and request
     * RDAP server for JSON response, and then convert JSON result to text
     * format.
     * 
     * @param commandStr
     *            commandStr.
     * @return query result.
     * @throws ServiceException
     *             ServiceException.
     */
    public String execute(String commandStr) throws ServiceException {
        String requestURI = StringUtils.EMPTY;
        try {
            Command command = CommandParser.parse(commandStr);
            requestURI = generateRequestURI(command);
            if (StringUtils.isBlank(requestURI)) {
                throw new ServiceException("invalid command:" + commandStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            requestURI = generateRequestURIForError();
        }
        String jsonStr = RestClient.getInstance().execute(requestURI);
        Map jsonMap = JsonUtil.deserializateJsonToMap(jsonStr);
        String result = ResponseFormater.format(jsonMap);
        return result;
    }

    /**
     * generate request URI for error, this will return 400 error.
     * 
     * @return URI.
     */
    private String generateRequestURIForError() {
        ErrorQueryHandler errorQueryHandler = new ErrorQueryHandler();
        return errorQueryHandler.generateRequestURI(new Command(
                CommandOption.IP_OR_DOMAIN_QUERY));
    }

    /**
     * generate request URI, used to request RDAP server.
     * 
     * @param command
     *            command.
     * @return URI.
     */
    private String generateRequestURI(Command command) {
        String requestURI = StringUtils.EMPTY;
        for (QueryHandler handler : queryHandlers) {
            if (handler.supportCmd(command)) {
                requestURI = handler.generateRequestURI(command);
                break;
            }
        }
        return requestURI;
    }
}
