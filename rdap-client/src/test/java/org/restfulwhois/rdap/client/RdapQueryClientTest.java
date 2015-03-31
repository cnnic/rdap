package org.restfulwhois.rdap.client;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.apache.wink.client.MockHttpServer;
import org.apache.wink.client.MockHttpServer.MockHttpServerResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.common.dto.AutnumDto;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.EntityDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.NameserverDto;
import org.restfulwhois.rdap.common.model.Help;

public class RdapQueryClientTest {
    MockHttpServer mockHttpServer;
    int port = 8080;
    String url = "http://127.0.0.1:8081";
    RdapQueryClient client;

    @Before
    public void startServer() {
        mockHttpServer = new MockHttpServer(port);
        mockHttpServer.startServer();
        initClient();
    }
    
    public void initClient(){
        RdapClientConfig config = new RdapClientConfig(url);
        config.setConnectTimeout(10000);
        config.setReadTimeout(10000);
        client = new RdapQueryClient(config);
    }
    
    public void setContent200(String objectType) {
        String responseString = "{\"handle\":\"" + objectType + "-1\"}";
        setMockResponse(200, responseString);

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
    public void test_queryIp1(){
        IpDto ip;
        try {
            setContent200("ip");
            ip = client.queryIp("192.168.1.1");
        } catch (RdapClientException e) {
            ip = null;
        }
        assertEquals("/ip/192.168.1.1", mockHttpServer.getRequestUrl());
        assertEquals("ip-1", ip.getHandle());
    }
    
    @Test
    public void test_queryIp2(){
        IpDto ip;
        try {
            setContent200("ip");
            ip = client.queryIp("192.168.1.1", 8);
        } catch (RdapClientException e) {
            ip = null;
        }
        assertEquals("/ip/192.168.1.1/8", mockHttpServer.getRequestUrl());
        assertEquals("ip-1", ip.getHandle());
    }
    
    @Test
    public void test_queryDomain(){
        DomainDto domain;
        try {
            setContent200("domain");
            domain = client.queryDomain("domainName");
        } catch (RdapClientException e) {
            domain = null;
        }
        assertEquals("/domain/domainName", mockHttpServer.getRequestUrl());
        assertEquals("domain-1", domain.getHandle());
    }
    
    @Test
    public void test_queryNameserver(){
        NameserverDto nameserver;
        try {
            setContent200("nameserver");
            nameserver = client.queryNameserver("nameserverName");
        } catch (RdapClientException e) {
            nameserver = null;
        }
        assertEquals("/nameserver/nameserverName", mockHttpServer.getRequestUrl());
        assertEquals("nameserver-1", nameserver.getHandle());
    }
    
    @Test
    public void test_queryAutnum(){
        AutnumDto autnum;
        try {
            setContent200("autnum");
            autnum = client.queryAutnum(2100);
        } catch (RdapClientException e) {
            autnum = null;
        }
        assertEquals("/autnum/2100", mockHttpServer.getRequestUrl());
        assertEquals("autnum-1", autnum.getHandle());
    }
    
    @Test
    public void test_queryEntity(){
        EntityDto entity;
        try {
            setContent200("entity");
            entity = client.queryEntity("entityName");
        } catch (RdapClientException e) {
            entity = null;
        }
        assertEquals("/entity/entityName", mockHttpServer.getRequestUrl());
        assertEquals("entity-1", entity.getHandle());
    }
    
    @Test
    public void test_searchDomainByName(){
        DomainDto domain;
        try {
            setContent200("domain");
            domain = client.searchDomainByName("domainName");
        } catch (RdapClientException e) {
            domain = null;
        }
        assertEquals("/domains?name=domainName", mockHttpServer.getRequestUrl());
        assertEquals("domain-1", domain.getHandle());
    }
    
    @Test
    public void test_searchNameserverByName(){
        NameserverDto nameserver;
        try {
            setContent200("nameserver");
            nameserver = client.searchNameserverByName("nameserverName");
        } catch (RdapClientException e) {
            nameserver = null;
        }
        assertEquals("/nameservers?name=nameserverName", mockHttpServer.getRequestUrl());
        assertEquals("nameserver-1", nameserver.getHandle());
    }
    
    @Test
    public void test_searchEntityByFn(){
        EntityDto entity;
        try {
            setContent200("entity");
            entity = client.searchEntityByFn("entityFn");
        } catch (RdapClientException e) {
            entity = null;
        }
        assertEquals("/entities?fn=entityFn", mockHttpServer.getRequestUrl());
        assertEquals("entity-1", entity.getHandle());
    }
    
    @Test
    public void test_searchEntityByHandle(){
        EntityDto entity;
        try {
            setContent200("entity");
            entity = client.searchEntityByHandle("entity-1");
        } catch (RdapClientException e) {
            entity = null;
        }
        assertEquals("/entities?handle=entity-1", mockHttpServer.getRequestUrl());
        assertEquals("entity-1", entity.getHandle());
    }
    
    @Test
    public void test_help(){
        Help help;
        try {
            setContent200("help");
            help = client.help();
        } catch (RdapClientException e) {
            help = null;
        }
        assertEquals("/help", mockHttpServer.getRequestUrl());
        assertEquals("help-1", help.getHandle());
    }
    
    
    @After
    public void stopServer() {
        mockHttpServer.stopServer();
    }
}
