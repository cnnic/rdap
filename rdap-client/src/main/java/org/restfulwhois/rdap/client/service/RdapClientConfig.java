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
