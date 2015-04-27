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
package org.restfulwhois.rdap.client;

import java.net.URL;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.client.service.RdapResponse;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.JsonUtil;
import org.restfulwhois.rdap.client.util.URLUtil;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

/**
 * Supply create, update and delete dto object function
 * @author M.D.
 *
 */
public class RdapUpdateClient extends RdapClient {

    /**
     * character "u"
     */
    private final String update = "u";

    /**
     * constructor
     * @param config RdapClientConfig
     */
    public RdapUpdateClient(RdapClientConfig config) {
        super(config);
    }

    /**
     * Create dto object
     * @param dto dto object
     * @return UpdateResponse
     * @throws RdapClientException if fail to create
     */
    public UpdateResponse create(BaseDto dto) throws RdapClientException {
        return execute(dto, HttpMethodType.POST);
    }

    /**
     * Update dto object
     * @param dto dto object
     * @return UpdateResponse
     * @throws RdapClientException if fail to update
     */
    public UpdateResponse update(BaseDto dto) throws RdapClientException {
        return execute(dto, HttpMethodType.PUT);
    }

    /**
     * Delete IpDto by handle
     * @param handle IpDto handle
     * @return UpdateResponse
     * @throws RdapClientException if fail to delete
     */
    public UpdateResponse deleteIp(String handle) throws RdapClientException {
        IpDto dto = new IpDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    /**
     * Delete DomainDto by handle
     * @param handle DomainDto handle
     * @return UpdateResponse
     * @throws RdapClientException if fail to delete
     */
    public UpdateResponse deleteDomain(String handle)
            throws RdapClientException {
        DomainDto dto = new DomainDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    /**
     * Delete EntityDto by handle
     * @param handle IpDto handle
     * @return UpdateResponse
     * @throws RdapClientException if fail to delete
     */
    public UpdateResponse deleteEntity(String handle)
            throws RdapClientException {
        EntityDto dto = new EntityDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    /**
     * Delete NameserverDto by handle
     * @param handle NameserverDto handle
     * @return UpdateResponse
     * @throws RdapClientException if fail to delete
     */
    public UpdateResponse deleteNameserver(String handle)
            throws RdapClientException {
        NameserverDto dto = new NameserverDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    /**
     * Delete IpDto by handle
     * @param handle AutnumDto handle
     * @return UpdateResponse
     * @throws RdapClientException if fail to delete
     */
    public UpdateResponse deleteAutnum(String handle)
            throws RdapClientException {
        AutnumDto dto = new AutnumDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    /**
     * Execute update request
     * @param dto dto handle
     * @param httpMethod POST, PUT or DELETE
     * @return UpdateResponse
     * @throws RdapClientException if fail to update, create or delete
     */
    private UpdateResponse execute(BaseDto dto, HttpMethodType httpMethod)
            throws RdapClientException {
        String body = JsonUtil.toJson(dto);
        URL url;
        if (!httpMethod.equals(HttpMethodType.POST)) {
            url = URLUtil.makeURLWithPath(config.getUrl(), update,
                    dto.getUpdateUri(), dto.getHandle());
        } else {
            url = URLUtil.makeURLWithPath(config.getUrl(), update,
                    dto.getUpdateUri());
        }
        RdapResponse response = createTemplate().execute(httpMethod, url, body);
        return response.getResponseBody(UpdateResponse.class);
    }

}
