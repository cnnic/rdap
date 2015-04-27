/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
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
