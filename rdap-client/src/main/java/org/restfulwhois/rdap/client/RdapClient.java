package org.restfulwhois.rdap.client;

import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.client.service.RdapHttpsTemplate;
import org.restfulwhois.rdap.client.service.RdapRestTemplate;
import org.restfulwhois.rdap.client.util.StringUtil;

/**
 * This is the super class of RdapQueryClient and RdapUpdateClient.
 * @author M.D.
 *
 */
class RdapClient {
    /**
     * It is used to configure the RdapClient
     */
    protected RdapClientConfig config;
    
    /**
     * Constructor
     * @param config the instance of RdapClientConfig
     */
    protected RdapClient(RdapClientConfig config){
        this.config = config;
    }
    
    /**
     * Use the property <B>config</B> to create a instance of RdapRestTemplate
     * @return RdapRestTemplate
     */
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
    
    /**
     * General setting of RdapRestTemplate.It is apply to http only.
     * @param template the instance of RdapRestTemplate
     */
    private void generalSetting(RdapRestTemplate template){
        template.setConnectTimeout(config.getConnectTimeout());
        template.setReadTimeout(config.getReadTimeout());
        template.setMediaType(config.getMediaType());
    }
}
