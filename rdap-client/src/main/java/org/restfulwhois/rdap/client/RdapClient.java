package org.restfulwhois.rdap.client;

import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.client.service.RdapHttpsTemplate;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.util.StringUtil;

class RdapClient {
    protected RdapClientConfig config;
    
    protected RdapClient(RdapClientConfig config){
        this.config = config;
    }
    
    protected RdapRestTemplate createTemplate() {
        if(config.isHttps()){
            RdapHttpsTemplate httpsTemplate = new RdapHttpsTemplate();
            generalSetting(httpsTemplate);
            if(!StringUtil.isEmpty(config.getKeyStoreFilePath())){
                httpsTemplate.setFilePath(config.getKeyStoreFilePath());
                httpsTemplate.setPassword(config.getKeyStorePassword());
            }
            return httpsTemplate;
        }else{
            RdapRestTemplate httpTemplate = new RdapRestTemplate();
            generalSetting(httpTemplate);
            return httpTemplate;
        }
    }
    
    private void generalSetting(RdapRestTemplate template){
        template.setConnectTimeout(config.getConnectTimeout());
        template.setReadTimeout(config.getReadTimeout());
        template.setMediaType(config.getMediaType());
    }
}
