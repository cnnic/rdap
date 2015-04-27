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
package org.restfulwhois.rdap.client.service;

import org.restfulwhois.rdap.client.util.URLUtil;

/**
 * It is used to configure the RdapClient.
 * @author M.D.
 *
 */
public class RdapClientConfig {
    /**
     * default media type : application/json;charset=UTF-8
     */
    private final String mediaTypeJson = "application/json;charset=UTF-8";
    /**
     * default connect timeout 3000
     */
    private final int connTimeoutDefault = 3000;
    /**
     * default read timeout 3000
     */
    private final int readTimeoutDefault = 10000;
    /**
     * url string
     */
    private String url;
    /**
     * timeout for connection
     */
    private int connectTimeout;
    /**
     * timeout for reading response
     */
    private int readTimeout;
    /**
     * media type
     */
    private String mediaType;
    /**
     * keystore file path
     */
    private String keyStoreFilePath;
    /**
     * keystore file password
     */
    private String keyStorePassword;
    
    /**
     * Constructor
     * @param url the aim url
     */
    public RdapClientConfig(String url){
        this.url = url;
        connectTimeout = connTimeoutDefault;
        readTimeout = readTimeoutDefault;
        mediaType = mediaTypeJson;
    }
    
    /**
     * url getter
     * @return url
     */
    public String getUrl() {
        return url;
    }

    /**
     * url setter
     * @param url url string
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * connectTimeout getter
     * @return connectTimeout
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * connectTimeout settter
     * @param connectTimeout milliseconds
     */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    /**
     * read timeout getter
     * @return readTimeout
     */
    public int getReadTimeout() {
        return readTimeout;
    }
    
    /**
     * read timeout setter
     * @param readTimeout milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    /**
     * mediaType getter
     * @return mediaType
     */
    public String getMediaType() {
        return mediaType;
    }

    /**
     * keyStoreFilePath getter
     * @return keyStoreFilePath
     */
    public String getKeyStoreFilePath() {
        return keyStoreFilePath;
    }

    /**
     * keyStoreFilePath setter
     * @param keyStoreFilePath the path for .keystore file
     */
    public void setKeyStoreFilePath(String keyStoreFilePath) {
        this.keyStoreFilePath = keyStoreFilePath;
    }

    /**
     * keyStorePassword getter
     * @return keyStorePassword
     */
    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    /**
     * keyStorePassword setter
     * @param keyStorePassword the password for .keystore file
     */
    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }
    
    /**
     * Return true if it is a https connection
     * @return boolean
     */
    public boolean isHttps(){
        String protocol = getUrl().substring(0, 5);
        String https = URLUtil.ProtocolType.HTTPS.name();
        if(https.equalsIgnoreCase(protocol)){
            return true;
        }
        return false;
    }
}
