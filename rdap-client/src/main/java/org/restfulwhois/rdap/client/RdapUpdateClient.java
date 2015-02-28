package org.restfulwhois.rdap.client;

import java.net.URL;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapResponse;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.JsonUtil;
import org.restfulwhois.rdap.client.util.StringUtil;
import org.restfulwhois.rdap.client.util.URLUtil;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.BaseDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

public class RdapUpdateClient {

    private int connectTimeout = 3000;
    private int readTimeout = 10000;
    private final String mediaTypeJson = "application/json;charset=UTF-8";
    private final String UPDATE = "u";
    private String urlStr;
    private String filePath;
    private String password;

    public RdapUpdateClient(String url) {
        this.urlStr = url;
    }

    public UpdateResponse create(BaseDto dto) throws RdapClientException {
        return execute(dto, HttpMethodType.POST);
    }

    public UpdateResponse update(BaseDto dto) throws RdapClientException {
        return execute(dto, HttpMethodType.PUT);
    }

    public UpdateResponse deleteIp(String handle) throws RdapClientException {
        IpDto dto = new IpDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    public UpdateResponse deleteDomain(String handle)
            throws RdapClientException {
        DomainDto dto = new DomainDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    public UpdateResponse deleteEntity(String handle)
            throws RdapClientException {
        EntityDto dto = new EntityDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    public UpdateResponse deleteNameserver(String handle)
            throws RdapClientException {
        NameserverDto dto = new NameserverDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    public UpdateResponse deleteAutnum(String handle)
            throws RdapClientException {
        AutnumDto dto = new AutnumDto();
        dto.setHandle(handle);
        return execute(dto, HttpMethodType.DELETE);
    }

    private UpdateResponse execute(BaseDto dto, HttpMethodType httpMethod)
            throws RdapClientException {
        String body = JsonUtil.toJson(dto);
        URL url;
        if (!httpMethod.equals(HttpMethodType.POST)) {
            url = URLUtil.makeURLWithPath(urlStr, UPDATE, dto.getUpdateUri(),
                    dto.getHandle());
        } else {
            url = URLUtil.makeURLWithPath(urlStr, UPDATE, dto.getUpdateUri());
        }
        RdapResponse response = createTemplate().execute(httpMethod,
                url, body);
        return response.getResponseBody(UpdateResponse.class);
    }

    private RdapRestTemplate createTemplate() {
        RdapRestTemplate template = new RdapRestTemplate();
        if(!StringUtil.isEmpty(filePath)){
            template.setFilePath(filePath);
            template.setPassword(password);
        }
        template.setConnectTimeout(connectTimeout);
        template.setReadTimeout(readTimeout);
        template.setMediaType(mediaTypeJson);
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