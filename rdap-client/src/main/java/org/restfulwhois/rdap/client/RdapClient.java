package org.restfulwhois.rdap.client;

import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.util.StringUtil;

public class RdapClient {
    protected RdapClientConfig config;
    
    protected RdapClient(RdapClientConfig config){
        this.config = config;
    }
    
    protected RdapRestTemplate createTemplate() {
        RdapRestTemplate template = new RdapRestTemplate();
        if(!StringUtil.isEmpty(config.getKeyStoreFilePath())){
            template.setFilePath(config.getKeyStoreFilePath());
            template.setPassword(config.getKeyStorePassword());
        }
        template.setConnectTimeout(config.getConnectTimeout());
        template.setReadTimeout(config.getReadTimeout());
        template.setMediaType(config.getMediaType());
        return template;
    }
}
