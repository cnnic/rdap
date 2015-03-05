package org.restfulwhois.rdap.client;

import java.util.List;

import org.apache.wink.client.MockHttpServer;
import org.apache.wink.client.MockHttpServer.MockHttpServerResponse;
import org.junit.Before;
import org.restfulwhois.rdap.client.service.RdapClientConfig;

public class RdapQueryClientTest {
    MockHttpServer mockHttpServer;
    int port = 8080;
    String url = "http://127.0.0.1:8081";
    RdapUpdateClient client;

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
        client = new RdapUpdateClient(config);
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
    
    public void test_queryIp(){
        
    }
}
