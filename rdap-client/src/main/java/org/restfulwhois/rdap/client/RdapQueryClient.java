package org.restfulwhois.rdap.client;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapResponse;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.service.impl.HttpTemplate;
import org.restfulwhois.rdap.client.service.impl.HttpsTemplate;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.TrustType;
import org.restfulwhois.rdap.client.util.URLUtil;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.model.Help;

public class RdapQueryClient {

    private int connectTimeout = 3000;
    private int readTimeout = 10000;
    private String urlStr;
    private TrustType trustType;
    private String filePath;
    private String password;

    public RdapQueryClient(String urlStr) {
        this.urlStr = urlStr;
    }
    
    public RdapQueryClient(String urlStr, TrustType trustType) {
        this.urlStr = urlStr;
        this.trustType = trustType;
    }

    public IpDto queryIp(String address) throws RdapClientException {
        return query(IpDto.class, HttpMethodType.GET, "ip", address);
    }

    public IpDto queryIp(String address, int cidrLength)
            throws RdapClientException {
        return query(IpDto.class, HttpMethodType.GET, "ip", address,
                String.valueOf(cidrLength));
    }

    public DomainDto queryDomain(String name) throws RdapClientException {
        return query(DomainDto.class, HttpMethodType.GET, "domain", name);
    }

    public NameserverDto queryNameserver(String name)
            throws RdapClientException {
        return query(NameserverDto.class, HttpMethodType.GET, "nameserver",
                name);
    }

    public AutnumDto queryAutnum(String autnum) throws RdapClientException {
        return query(AutnumDto.class, HttpMethodType.GET, "autnum", autnum);
    }

    public EntityDto queryEntity(String handle) throws RdapClientException {
        return query(EntityDto.class, HttpMethodType.GET, "entity", handle);
    }

    public IpDto searchDomainByName(String param) throws RdapClientException {
        return search(IpDto.class, HttpMethodType.GET, "name", param);
    }

    public NameserverDto searchNameserverByName(String param)
            throws RdapClientException {
        return search(NameserverDto.class, HttpMethodType.GET, "name", param);
    }

    public EntityDto searchEntityByFn(String param) throws RdapClientException {
        return search(EntityDto.class, HttpMethodType.GET, "fn", param);
    }

    public EntityDto searchEntityByHandle(String param)
            throws RdapClientException {
        return search(EntityDto.class, HttpMethodType.GET, "handle", param);
    }

    public Help help() throws RdapClientException {
        return query(Help.class, HttpMethodType.GET, "help");
    }

    private <T> T query(Class<T> type, HttpMethodType httpMethod,
            String... param) throws RdapClientException {
        URL url = URLUtil.makeURLWithPath(urlStr, param);
        boolean isHttps = URLUtil.isHttps(url);
        RdapResponse response = createTemplate(isHttps)
                .execute(httpMethod, url);
        return response.getResponseBody(type);
    }

    private <T> T search(Class<T> type, HttpMethodType httpMethod, String key,
            String value) throws RdapClientException {
        Map<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        URL url = URLUtil.makeURLWithParam(urlStr, map);
        boolean isHttps = URLUtil.isHttps(url);
        RdapResponse response = createTemplate(isHttps)
                .execute(httpMethod, url);
        return response.getResponseBody(type);
    }

    private RdapRestTemplate createTemplate(boolean isHttps) {
        RdapRestTemplate template;
        if (isHttps) {
            template = new HttpsTemplate(trustType, filePath, password);
        } else {
            template = new HttpTemplate();
        }
        template.setConnectTimeout(connectTimeout);
        template.setReadTimeout(readTimeout);
        return template;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getUrlStr() {
        return urlStr;
    }

    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    public TrustType getTrustType() {
        return trustType;
    }

    public void setTrustType(TrustType trustType) {
        this.trustType = trustType;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}