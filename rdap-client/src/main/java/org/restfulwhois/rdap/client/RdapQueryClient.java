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
import java.util.HashMap;
import java.util.Map;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.client.service.RdapResponse;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.URLUtil;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.model.Help;

/**
 * Supply query function
 * @author M.D.
 *
 */
public class RdapQueryClient extends RdapClient{

    /**
     * constructor
     * @param config RdapClientConfig
     */
    public RdapQueryClient(RdapClientConfig config) {
        super(config);
    }

    /**
     * Query IpDto by address
     * @param address ip address
     * @return IpDto
     * @throws RdapClientException if fail to query
     */
    public IpDto queryIp(String address) throws RdapClientException {
        return query(IpDto.class, "ip", address);
    }

    /**
     * Query IpDto by address and cidrLength
     * @param cidrPrefix ip address
     * @param cidrLength cidr length
     * @return IpDto
     * @throws RdapClientException if fail to query
     */
    public IpDto queryIp(String cidrPrefix, int cidrLength)
            throws RdapClientException {
        return query(IpDto.class, "ip", cidrPrefix, String.valueOf(cidrLength));
    }

    /**
     * Query DomainDto by name
     * @param name domain name
     * @return DomainDto
     * @throws RdapClientException  if fail to query
     */
    public DomainDto queryDomain(String name) throws RdapClientException {
        return query(DomainDto.class, "domain", name);
    }

    /**
     * Query NameserverDto by name
     * @param name NameserverDto name
     * @return NameserverDto
     * @throws RdapClientException if fail to query
     */
    public NameserverDto queryNameserver(String name)
            throws RdapClientException {
        return query(NameserverDto.class, "nameserver", name);
    }

    /**
     * Query AutnumDto by autnum
     * @param autnum AutnumDto autnum
     * @return AutnumDto
     * @throws RdapClientException  if fail to query
     */
    public AutnumDto queryAutnum(long autnum) throws RdapClientException {
        return query(AutnumDto.class, "autnum", String.valueOf(autnum));
    }

    /**
     * Query EntityDto by handle
     * @param handle EntityDto handle
     * @return EntityDto
     * @throws RdapClientException if fail to query
     */
    public EntityDto queryEntity(String handle) throws RdapClientException {
        return query(EntityDto.class, "entity", handle);
    }

    /**
     * Search domain by name
     * @param name domain name
     * @return DomainDto
     * @throws RdapClientException if fail to search
     */
    public DomainDto searchDomainByName(String name) throws RdapClientException {
        return search(DomainDto.class, "name", name, SearchUri.DOMAIN);
    }
    
    /**
     * Search domain by ns ldhName
     * @param nsLdhName ns ldhName
     * @return DomainDto
     * @throws RdapClientException if fail to search
     */
    public DomainDto searchDomainByNsLdhName(String nsLdhName)
            throws RdapClientException {
        return search(DomainDto.class, "nsLdhName", nsLdhName, SearchUri.DOMAIN);
    }
    
    /**
     * Search domain by ns ip
     * @param nsIp ns ip
     * @return DomainDto
     * @throws RdapClientException if fail to search
     */
    public DomainDto searchDomainByNsIp(String nsIp) throws RdapClientException {
        return search(DomainDto.class, "nsIp", nsIp, SearchUri.DOMAIN);
    }

    /**
     * Search nameserver by name
     * @param name nameserver name
     * @return NameserverDto
     * @throws RdapClientException if fail to search
     */
    public NameserverDto searchNameserverByName(String name)
            throws RdapClientException {
        return search(NameserverDto.class, "name", name, SearchUri.NAMESERVER);
    }

    /**
     * Search nameserver by ip
     * @param ip ip address
     * @return NameserverDto
     * @throws RdapClientException if fail to search
     */
    public NameserverDto searchNameserverByIp(String ip) throws RdapClientException{
        return search(NameserverDto.class, "ip", ip, SearchUri.NAMESERVER);
    }
    
    /**
     * Search EntityDto by entity name
     * @param name entity name
     * @return EntityDto
     * @throws RdapClientException if fail to search
     */
    public EntityDto searchEntityByFn(String name) throws RdapClientException {
        return search(EntityDto.class, "fn", name, SearchUri.ENTITY);
    }

    /**
     * Search EntityDto by handle
     * @param handle handle
     * @return EntityDto
     * @throws RdapClientException if fail to search
     */
    public EntityDto searchEntityByHandle(String handle)
            throws RdapClientException {
        return search(EntityDto.class, "handle", handle, SearchUri.ENTITY);
    }

    /**
     * Get help
     * @return help
     * @throws RdapClientException if fail to get help
     */
    public Help help() throws RdapClientException {
        return query(Help.class, "help");
    }

    /**
     * To query dto object
     * @param type dto class type
     * @param param uri
     * @param <T> dto class type
     * @return dto object
     * @throws RdapClientException if fail to query
     */
    private <T> T query(Class<T> type, String... param)
            throws RdapClientException {
        URL url = URLUtil.makeURLWithPath(config.getUrl(), param);
        RdapResponse response = createTemplate().execute(HttpMethodType.GET,
                url);
        return response.getResponseBody(type);
    }

    /**
     * Search dto object by parameter
     * @param type dto class type
     * @param key parameter key
     * @param value parameter value
     * @param uri uri
     * @param <T> dto class type
     * @return dto object
     * @throws RdapClientException if fail to search
     */
    private <T> T search(Class<T> type, String key, String value, SearchUri uri)
            throws RdapClientException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        URL url = URLUtil.makeURLWithPathAndParam(config.getUrl(), map, uri.getUri());
        RdapResponse response = createTemplate().execute(HttpMethodType.GET,
                url);
        return response.getResponseBody(type);
    }

    /**
     * URI enum
     * @author M.D.
     *
     */
    private enum SearchUri{
        /**
         * domains
         */
        DOMAIN("domains"),
        /**
         * nameservers
         */
        NAMESERVER("nameservers"),
        /**
         * entities
         */
        ENTITY("entities");
        /**
         * uri
         */
        String uri;

        /**
         * constructor
         * @param uri uri string
         */
        private SearchUri(String uri){
            this.uri = uri;
        }
        
        /**
         * get uri
         * @return uri string
         */
        private String getUri(){
            return this.uri;
        }
    }
}
