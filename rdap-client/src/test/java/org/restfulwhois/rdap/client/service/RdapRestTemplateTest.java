package org.restfulwhois.rdap.client.service;

import static org.junit.Assert.assertEquals;

import java.net.URL;
import java.util.List;

import org.apache.wink.client.MockHttpServer;
import org.apache.wink.client.MockHttpServer.MockHttpServerResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.URLUtil;
import org.restfulwhois.rdap.common.dto.UpdateResponse;

public class RdapRestTemplateTest {
    MockHttpServer mockHttpServer;
    String url = "http://127.0.0.1:8081";
    int port = 8080;
    RdapRestTemplate template = new RdapRestTemplate();
    String param = "{\"handle\":\"ip-handle1\",\"entities\":null,\"status\":null,"
            + "\"remarks\":null,\"links\":null,\"port43\":null,\"events\":null,"
            + "\"lang\":null,\"customProperties\":{\"custom1\":\"1\",\"custom2\":"
            + "\"{\\\"property\\\":2}\"},\"startAddress\":\"192.168.1.1\","
            + "\"endAddress\":\"192.168.1.100\",\"ipVersion\":\"v4\","
            + "\"name\":null,\"type\":null,\"country\":\"中国\",\"parentHandle\":null}";

    @Before
    public void startServer() {
        mockHttpServer = new MockHttpServer(port);
        mockHttpServer.startServer();
        template.setConnectTimeout(10000);
        template.setReadTimeout(10000);
    }

    public void setContent200() {
        MockHttpServerResponse response = new MockHttpServerResponse();

        response.setMockResponseContent("{\"handle\":\"ip-handle1\"}");
        response.setMockResponseCode(200);
        response.setMockResponseContentType("application/json");

        List<MockHttpServerResponse> list = mockHttpServer
                .getMockHttpServerResponses();
        list.add(response);
        mockHttpServer.setMockHttpServerResponses(response);
    }

    public void setContent400() {
        MockHttpServerResponse response = new MockHttpServerResponse();
        String responseString = "{\"handle\":\"domain-1\",\"errorCode\":400,"
                + "\"subErrorCode\":4002,\"description\":"
                + "[\"Property can't be empty: domainName\"]}";
        response.setMockResponseContent(responseString);
        response.setMockResponseCode(400);
        response.setMockResponseContentType("application/json");

        List<MockHttpServerResponse> list = mockHttpServer
                .getMockHttpServerResponses();
        list.add(response);
        mockHttpServer.setMockHttpServerResponses(response);
    }

    @Test
    public void test_execute_post200() {
        UpdateResponse response;
        int code;
        setContent200();
        try {
            URL url = URLUtil.makeURLWithPath(this.url, "ip");
            RdapResponse res = template
                    .execute(HttpMethodType.POST, url, param);
            code = res.getResponseCode();
            response = res.getResponseBody(UpdateResponse.class);
        } catch (RdapClientException e) {
            response = null;
            code = 0;
        }
        assertEquals(mockHttpServer.getRequestContentAsString(), param);
        assertEquals(code, 200);
        assertEquals(response.getHandle(), "ip-handle1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_execute_put200() {
        UpdateResponse response;
        int code;
        setContent200();
        try {
            URL url = URLUtil.makeURLWithPath(this.url, "ip");
            RdapResponse res = template.execute(HttpMethodType.PUT, url, param);
            code = res.getResponseCode();
            response = res.getResponseBody(UpdateResponse.class);
        } catch (RdapClientException e) {
            response = null;
            code = 0;
        }
        assertEquals(code, 200);
        assertEquals(response.getHandle(), "ip-handle1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_execute_delete200() {
        UpdateResponse response;
        int code;
        setContent200();
        try {
            URL url = URLUtil.makeURLWithPath(this.url, "ip", "ip-handle1");
            RdapResponse res = template.execute(HttpMethodType.DELETE, url);
            code = res.getResponseCode();
            response = res.getResponseBody(UpdateResponse.class);
        } catch (RdapClientException e) {
            response = null;
            code = 0;
        }
        assertEquals(code, 200);
        assertEquals(response.getHandle(), "ip-handle1");
        assertEquals(response.getErrorCode(), 0);
        assertEquals(response.getSubErrorCode(), 0);
        assertEquals(response.getDescription().size(), 0);
    }

    @Test
    public void test_execute_post400() {
        UpdateResponse response;
        int code;
        setContent400();
        try {
            URL url = URLUtil.makeURLWithPath(this.url, "domain");
            RdapResponse res = template.execute(HttpMethodType.DELETE, url,
                    null);
            code = res.getResponseCode();
            response = res.getResponseBody(UpdateResponse.class);
        } catch (RdapClientException e) {
            response = null;
            code = 0;
        }
        assertEquals(code, 400);
        assertEquals(response.getHandle(), "domain-1");
        assertEquals(response.getErrorCode(), 400);
        assertEquals(response.getSubErrorCode(), 4002);
        assertEquals(response.getDescription().get(0),
                "Property can't be empty: domainName");
    }

    @After
    public void stopServer() {
        mockHttpServer.stopServer();
    }
}