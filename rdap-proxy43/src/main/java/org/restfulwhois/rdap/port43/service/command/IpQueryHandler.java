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
package org.restfulwhois.rdap.port43.service.command;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.port43.service.ServiceException;
import org.restfulwhois.rdap.port43.util.IpUtil;

/**
 * ip query handler.
 * 
 * <pre>
 * always support! 
 * so MUST check ipQueryHandler at last,before DomainQueryHandler.
 * </pre>
 * 
 * <pre>
 * command option for IP query is null, eg: whois 218.241.1.1
 * </pre>
 * 
 * @author jiashuo
 * 
 */
public class IpQueryHandler extends QueryHandler {

    @Override
    public boolean supportCmd(Command command) {
        List<String> argumentList = command.getArgumentList();
        if (null == argumentList || argumentList.isEmpty()) {
            return false;
        }
        String argument = argumentList.get(0);
        if (StringUtils.isBlank(argument)) {
            return false;
        }
        String ipPart = argument;
        if (StringUtils.contains(argument, "/")) {
            ipPart = StringUtils.substringBefore(argument, "/");
        }
        boolean isIp = IpUtil.isIpV4OrV6Str(ipPart);
        return isIp;
    }

    @Override
    protected String getRelativeRequestURI(Command command) {
        List<String> argumentList = command.getArgumentList();
        if (null == argumentList || argumentList.isEmpty()) {
            throw new ServiceException("invalid argument");
        }
        if (StringUtils.isBlank(argumentList.get(0))) {
            throw new ServiceException("invalid argument");
        }
        return "ip/" + urlEncode(argumentList.get(0));
    }

}
