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

public class RdapQueryClient extends RdapClient{

    public RdapQueryClient(RdapClientConfig config) {
        super(config);
    }

    public IpDto queryIp(String address) throws RdapClientException {
        return query(IpDto.class, "ip", address);
    }

    public IpDto queryIp(String address, int cidrLength)
            throws RdapClientException {
        return query(IpDto.class, "ip", address, String.valueOf(cidrLength));
    }

    public DomainDto queryDomain(String name) throws RdapClientException {
        return query(DomainDto.class, "domain", name);
    }

    public NameserverDto queryNameserver(String name)
            throws RdapClientException {
        return query(NameserverDto.class, "nameserver", name);
    }

    public AutnumDto queryAutnum(String autnum) throws RdapClientException {
        return query(AutnumDto.class, "autnum", autnum);
    }

    public EntityDto queryEntity(String handle) throws RdapClientException {
        return query(EntityDto.class, "entity", handle);
    }

    public IpDto searchDomainByName(String param) throws RdapClientException {
        return search(IpDto.class, "name", param);
    }

    public NameserverDto searchNameserverByName(String param)
            throws RdapClientException {
        return search(NameserverDto.class, "name", param);
    }

    public EntityDto searchEntityByFn(String param) throws RdapClientException {
        return search(EntityDto.class, "fn", param);
    }

    public EntityDto searchEntityByHandle(String param)
            throws RdapClientException {
        return search(EntityDto.class, "handle", param);
    }

    public Help help() throws RdapClientException {
        return query(Help.class, "help");
    }

    private <T> T query(Class<T> type, String... param)
            throws RdapClientException {
        URL url = URLUtil.makeURLWithPath(config.getUrl(), param);
        RdapResponse response = createTemplate().execute(HttpMethodType.GET,
                url);
        return response.getResponseBody(type);
    }

    private <T> T search(Class<T> type, String key, String value)
            throws RdapClientException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        URL url = URLUtil.makeURLWithParam(config.getUrl(), map);
        RdapResponse response = createTemplate().execute(HttpMethodType.GET,
                url);
        return response.getResponseBody(type);
    }

}