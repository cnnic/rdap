package org.restfulwhois.rdap.client.service;

import java.net.URL;
import java.net.URLConnection;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.HttpMethodType;

public abstract class RdapRestTemplate {
    protected String MEDIA_TYPE;
    protected int connectTimeout;
    protected int readTimeout;

    public RdapRestTemplate() {
        this.MEDIA_TYPE = "application/json;charset=UTF-8";
    }

    public RdapResponse execute(HttpMethodType httpMethod, URL url)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), null);
    }

    public RdapResponse execute(HttpMethodType httpMethod, URL url, String body)
            throws RdapClientException {

        return doExecute(this.prepareExecute(url, httpMethod), body);
    }

    protected abstract URLConnection prepareExecute(URL url,
            HttpMethodType httpMethod) throws RdapClientException;

    protected abstract RdapResponse doExecute(URLConnection urlConnection,
            String body) throws RdapClientException;

    public String getMEDIA_TYPE() {
        return MEDIA_TYPE;
    }

    public void setMEDIA_TYPE(String mEDIA_TYPE) {
        MEDIA_TYPE = mEDIA_TYPE;
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

}