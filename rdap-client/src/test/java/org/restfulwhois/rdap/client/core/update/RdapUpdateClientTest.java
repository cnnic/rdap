package org.restfulwhois.rdap.client.core.update;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wink.client.MockHttpServer;
import org.apache.wink.client.MockHttpServer.MockHttpServerResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.client.RdapUpdateClient;
import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.DsDataDto;
import org.restfulwhois.rdap.common.dto.embedded.EntityHandleDto;
import org.restfulwhois.rdap.common.dto.embedded.EventDto;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.dto.embedded.PublicIdDto;
import org.restfulwhois.rdap.common.dto.embedded.SecureDnsDto;

public class RdapUpdateClientTest {
    MockHttpServer mockHttpServer;
    int port = 8080;
    String url = "http://127.0.0.1:8081";
    RdapUpdateClient client = new RdapUpdateClient(url);

    @Before
    public void startServer() {
        mockHttpServer = new MockHttpServer(port);
        mockHttpServer.startServer();
        client.setConnectTimeout(100000);
        client.setReadTimeout(100000);
    }

    public void setContent200(String objectType) {
        String responseString = "{\"handle\":\"" + objectType + "-1\"}";
        setMockResponse(200, responseString);

    }

    public void setContent400(String objectType) {
        String responseString = "{\"handle\":\"" + objectType
                + "-1\",\"errorCode\":400,"
                + "\"subErrorCode\":4002,\"description\":"
                + "[\"Property can't be empty\"]}";
        setMockResponse(400, responseString);
    }

    public void setContent404(String objectType) {
        String responseString = "{\"handle\":\"" + objectType
                + "-1\",\"errorCode\":404,"
                + "\"subErrorCode\":4041,\"description\":"
                + "[\"Object not found\"]}";
        setMockResponse(404, responseString);
    }

    public void setContent409(String objectType) {
        String responseString = "{\"handle\":\"" + objectType
                + "-1\",\"errorCode\":409,"
                + "\"subErrorCode\":4091,\"description\":"
                + "[\"Object already exist\"]}";
        setMockResponse(409, responseString);
    }

    private void setMockResponse(int code, String content) {
        MockHttpServerResponse response = new MockHttpServerResponse();
        response.setMockResponseContent(content);
        response.setMockResponseCode(code);
        response.setMockResponseContentType("application/json");
        List<MockHttpServerResponse> list = mockHttpServer
                .getMockHttpServerResponses();
        list.add(response);
        mockHttpServer.setMockHttpServerResponses(response);
    }

    @Test
    public void test_create_ip() {
        UpdateResponse response;

        IpDto dto = new IpDto();
        dto.setHandle("ip-1");
        dto.setStartAddress("192.168.1.1");
        dto.setEndAddress("192.168.1.100");
        dto.setIpVersion("v4");
        dto.setCountry("中国");
        Map<String, String> customProperties = new HashMap<String, String>();
        customProperties.put("custom1", "1");
        customProperties.put("custom2", "{\"property\":2}");
        dto.setCustomProperties(customProperties);
        try {
            setContent200(dto.getUpdateUri());
            response = client.create(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/ip");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_create_failToCreate_ip() {
        UpdateResponse response;

        IpDto dto = new IpDto();
        dto.setHandle("ip-1");
        try {
            setContent409(dto.getUpdateUri());
            response = client.create(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 409);
        assertEquals(response.getSubErrorCode(), 4091);
        assertEquals(response.getDescription().get(0), "Object already exist");
    }

    @Test
    public void test_update_ip() {
        UpdateResponse response;

        IpDto dto = new IpDto();
        dto.setHandle("ip-1");
        dto.setStartAddress("ff00::1111");
        dto.setEndAddress("ff00::ffff");
        dto.setIpVersion("v6");
        dto.setCountry("中国");
        Map<String, String> customProperties = new HashMap<String, String>();
        customProperties.put("custom1", "1");
        customProperties.put("custom2", "{\"property\":2}");
        try {
            setContent200(dto.getUpdateUri());
            response = client.update(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/ip/ip-1");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_update_failToUpdate_ip() {
        UpdateResponse response;

        IpDto dto = new IpDto();
        dto.setHandle("ip-1");
        try {
            setContent404(dto.getUpdateUri());
            response = client.update(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 404);
        assertEquals(response.getSubErrorCode(), 4041);
        assertEquals(response.getDescription().get(0), "Object not found");
    }

    @Test
    public void test_delete_ip() {
        UpdateResponse response;

        try {
            setContent200("ip");
            response = client.deleteIp("ip-1");
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/ip/ip-1");
        assertEquals(response.getHandle(), "ip-1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_delete_failToDelete_ip() {
        UpdateResponse response;

        try {
            setContent404("ip");
            response = client.deleteIp("ip-1");
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(response.getHandle(), "ip-1");
        assertEquals(response.getErrorCode(), 404);
        assertEquals(response.getSubErrorCode(), 4041);
        assertEquals(response.getDescription().get(0), "Object not found");
    }

    @Test
    public void test_create_Autnum() {
        UpdateResponse response;

        AutnumDto dto = new AutnumDto();
        dto.setHandle("autnum-1");
        List<EventDto> events = new ArrayList<EventDto>();
        EventDto eventDto = new EventDto();
        eventDto.setHandle("event-1");
        List<LinkDto> links = new ArrayList<LinkDto>();
        LinkDto linkDto = new LinkDto();
        linkDto.setHandle("link-1");
        links.add(linkDto);
        List<EntityHandleDto> entities = new ArrayList<EntityHandleDto>();
        EntityHandleDto entityHandleDto = new EntityHandleDto();
        entityHandleDto.setHandle("entityHandle-1");
        entities.add(entityHandleDto);
        linkDto.setEntities(entities);
        eventDto.setLinks(links);
        events.add(eventDto);
        dto.setEvents(events);

        try {
            setContent200(dto.getUpdateUri());
            response = client.create(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/autnum");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_update_Autnum() {
        UpdateResponse response;

        AutnumDto dto = new AutnumDto();
        dto.setHandle("autnum-1");
        List<EventDto> events = new ArrayList<EventDto>();
        EventDto eventDto = new EventDto();
        eventDto.setHandle("event-1");
        eventDto.setEventActor("eventActor1");
        List<LinkDto> links = new ArrayList<LinkDto>();
        LinkDto linkDto = new LinkDto();
        linkDto.setHandle("link-1");
        links.add(linkDto);
        List<EntityHandleDto> entities = new ArrayList<EntityHandleDto>();
        EntityHandleDto entityHandleDto = new EntityHandleDto();
        entityHandleDto.setHandle("entityHandle-1");
        entities.add(entityHandleDto);
        linkDto.setEntities(entities);
        eventDto.setLinks(links);
        events.add(eventDto);
        dto.setEvents(events);

        try {
            setContent200(dto.getUpdateUri());
            response = client.update(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/autnum/autnum-1");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_delete_Autnum() {
        UpdateResponse response;

        try {
            setContent200("autnum");
            response = client.deleteAutnum("autnum-1");
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/autnum/autnum-1");
        assertEquals(response.getHandle(), "autnum-1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_create_Domain() {
        UpdateResponse response;

        DomainDto dto = new DomainDto();
        dto.setHandle("domain-1");
        dto.setLdhName("ldhName1");
        SecureDnsDto secureDNS = new SecureDnsDto();
        secureDNS.setHandle("secureDNS-1");
        secureDNS.setDelegationSigned(false);
        List<DsDataDto> dsData = new ArrayList<DsDataDto>();
        DsDataDto dsDataDto = new DsDataDto();
        dsDataDto.setAlgorithm(1);
        dsData.add(dsDataDto);
        secureDNS.setDsData(dsData);
        dto.setSecureDNS(secureDNS);

        try {
            setContent200(dto.getUpdateUri());
            response = client.create(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/domain");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_update_Domain() {
        UpdateResponse response;

        DomainDto dto = new DomainDto();
        dto.setHandle("domain-1");
        dto.setLdhName("ldhName1");
        SecureDnsDto secureDNS = new SecureDnsDto();
        secureDNS.setHandle("secureDNS-1");
        secureDNS.setDelegationSigned(false);
        List<DsDataDto> dsData = new ArrayList<DsDataDto>();
        DsDataDto dsDataDto = new DsDataDto();
        dsDataDto.setAlgorithm(2);
        dsData.add(dsDataDto);
        secureDNS.setDsData(dsData);
        dto.setSecureDNS(secureDNS);

        try {
            setContent200(dto.getUpdateUri());
            response = client.update(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/domain/domain-1");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_delete_Domain() {
        UpdateResponse response;

        try {
            setContent200("domain");
            response = client.deleteDomain("domain-1");
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/domain/domain-1");
        assertEquals(response.getHandle(), "domain-1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_create_Entity() {
        UpdateResponse response;

        EntityDto dto = new EntityDto();
        dto.setHandle("entity-1");
        dto.setFn("fn");
        try {
            setContent200(dto.getUpdateUri());
            response = client.create(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/entity");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_update_Entity() {
        UpdateResponse response;

        EntityDto dto = new EntityDto();
        dto.setHandle("entity-1");
        dto.setFn("fn1");
        try {
            setContent200(dto.getUpdateUri());
            response = client.update(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/entity/entity-1");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_delete_Entity() {
        UpdateResponse response;

        try {
            setContent200("entity");
            response = client.deleteEntity("entity-1");
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/entity/entity-1");
        assertEquals(response.getHandle(), "entity-1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_create_Nameserver() {
        UpdateResponse response;

        NameserverDto dto = new NameserverDto();
        dto.setHandle("nameserver-1");
        dto.setLdhName("ldhName1");
        try {
            setContent200(dto.getUpdateUri());
            response = client.create(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(), "/u/nameserver");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_update_Nameserver() {
        UpdateResponse response;

        NameserverDto dto = new NameserverDto();
        dto.setHandle("nameserver-1");
        dto.setLdhName("ldhName2");
        try {
            setContent200(dto.getUpdateUri());
            response = client.update(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(),
                "/u/nameserver/nameserver-1");
        assertEquals(response.getHandle(), dto.getHandle());
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_delete_Nameserver() {
        UpdateResponse response;

        try {
            setContent200("nameserver");
            response = client.deleteNameserver("nameserver-1");
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(mockHttpServer.getRequestUrl(),
                "/u/nameserver/nameserver-1");
        assertEquals(response.getHandle(), "nameserver-1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_create_notlegal_dto_Exception() {
        UpdateResponse response;

        PublicIdDto dto = new PublicIdDto();
        dto.setLang("chinese");
        try {
            response = client.create(dto);
        } catch (RdapClientException e) {
            response = null;
        }
        assertEquals(response, null);
    }

    @After
    public void stopServer() {
        mockHttpServer.stopServer();
    }
}