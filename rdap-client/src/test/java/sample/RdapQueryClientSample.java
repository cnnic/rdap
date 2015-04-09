package sample;

import org.restfulwhois.rdap.client.RdapQueryClient;
import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.model.Help;

public class RdapQueryClientSample {
    public RdapClientConfig config(){
        String url = "http://www.sample.com";
        RdapClientConfig config = new RdapClientConfig(url);
        config.setConnectTimeout(10000);
        config.setReadTimeout(10000);
        return config;
    }
    
    public IpDto querySample(){
        RdapQueryClient client = new RdapQueryClient(config());
        try {
            return client.queryIp("192.168.1.1");
        } catch (RdapClientException e) {
            return null;
        }
    }
    
    public DomainDto searchSample(){
        RdapQueryClient client = new RdapQueryClient(config());
        try {
            return client.searchDomainByName("domainName");
        } catch (RdapClientException e) {
            return null;
        }
    }
    
    public Help helpSample(){
        RdapQueryClient client = new RdapQueryClient(config());
        try {
            return client.help();
        } catch (RdapClientException e) {
            return null;
        }
    }
}
