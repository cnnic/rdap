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