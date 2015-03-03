package org.restfulwhois.rdap.client.service;

public class RdapRequest {
    private final String mediaTypeJson = "application/json;charset=UTF-8";
    private final int connTimeoutDefault = 3000;
    private final int readTimeoutDefault = 10000;
    
    private String url;
    private int connectTimeout;
    private int readTimeout;
    private String mediaType;
    private String keyStoreFilePath;
    private String keyStorePassword;
    
    public RdapRequest(){
        connectTimeout = connTimeoutDefault;
        readTimeout = readTimeoutDefault;
        mediaType = mediaTypeJson;
    }

    
    
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getKeyStoreFilePath() {
        return keyStoreFilePath;
    }

    public void setKeyStoreFilePath(String keyStoreFilePath) {
        this.keyStoreFilePath = keyStoreFilePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public void setKeyStorePassword(String keyStorePassword) {
        this.keyStorePassword = keyStorePassword;
    }
    
    
    
    
}
