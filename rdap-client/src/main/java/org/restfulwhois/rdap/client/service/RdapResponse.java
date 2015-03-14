package org.restfulwhois.rdap.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.JsonUtil;

public class RdapResponse {
    private int responseCode;
    private String responseMessage;
    private InputStream in;
    private Map<String, String> unknownPropertiesMap;
    private boolean saveUnknownProperties;

    public <T> T getResponseBody(Class<T> responseType)
            throws RdapClientException {
        T t = null;
        if (getIn() != null)
            t = convert(getIn(), responseType);
        return t;
    }

    private <T> T convert(InputStream in, Class<T> responseType)
            throws RdapClientException {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "utf-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            in.close();
            if (isSaveUnknownProperties()) {
                unknownPropertiesMap = new HashMap<String, String>();
            }
            return JsonUtil.toObject(sb.toString(), responseType,
                    unknownPropertiesMap);
        } catch (IOException io) {
            throw new RdapClientException(io.getMessage());
        } catch (RdapClientException rce) {
            throw rce;
        }
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    private InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
    }

    public Map<String, String> getUnknownPropertiesMap() {
        return unknownPropertiesMap;
    }

    public boolean isSaveUnknownProperties() {
        return saveUnknownProperties;
    }

    public void setSaveUnknownProperties(boolean saveUnknownProperties) {
        this.saveUnknownProperties = saveUnknownProperties;
    }

}