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
     * @param address ip address
     * @param cidrLength cidr length
     * @return IpDto
     * @throws RdapClientException if fail to query
     */
    public IpDto queryIp(String address, int cidrLength)
            throws RdapClientException {
        return query(IpDto.class, "ip", address, String.valueOf(cidrLength));
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
     * @param param domain name
     * @return DomainDto
     * @throws RdapClientException if fail to search
     */
    public DomainDto searchDomainByName(String param) throws RdapClientException {
        return search(DomainDto.class, "name", param, SearchUri.DOMAIN);
    }

    /**
     * Search nameserver by name
     * @param param nameserver name
     * @return NameserverDto
     * @throws RdapClientException if fail to search
     */
    public NameserverDto searchNameserverByName(String param)
            throws RdapClientException {
        return search(NameserverDto.class, "name", param, SearchUri.NAMESERVER);
    }

    /**
     * Search EntityDto by entity name
     * @param param entity name
     * @return EntityDto
     * @throws RdapClientException if fail to search
     */
    public EntityDto searchEntityByFn(String param) throws RdapClientException {
        return search(EntityDto.class, "fn", param, SearchUri.ENTITY);
    }

    /**
     * Search EntityDto by handle
     * @param param handle
     * @return EntityDto
     * @throws RdapClientException if fail to search
     */
    public EntityDto searchEntityByHandle(String param)
            throws RdapClientException {
        return search(EntityDto.class, "handle", param, SearchUri.ENTITY);
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