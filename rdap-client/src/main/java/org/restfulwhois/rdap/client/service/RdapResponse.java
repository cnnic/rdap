package org.restfulwhois.rdap.client.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.util.HttpMethodType;
import org.restfulwhois.rdap.client.util.JsonUtil;

/**
 * Handle the response from rdap server
 * @author M.D.
 *
 */
public class RdapResponse {
    /**
     * http status code
     */
    private int responseCode;
    /**
     * http method: GET, POST, PUT or DELETE
     */
    private HttpMethodType methodType;
    /**
     * response message
     */
    private String responseMessage;
    /**
     * response body
     */
    private InputStream in;
    /**
     * unknown properties map
     */
    private Map<String, String> unknownPropertiesMap;
    /**
     * flag to mark it is need to save unknown properties into map
     */
    private boolean saveUnknownProperties;

    /**
     * Convert response body to object
     * @param responseType object class type
     * @param <T> class type
     * @return object
     * @throws RdapClientException if fail to convert
     */
    public <T> T getResponseBody(Class<T> responseType)
            throws RdapClientException {
        T t = null;
        
        if (getIn() != null){
            t = convert(getIn(), responseType);
        }
        return t;
    }

    /**
     * Convert response body to object
     * @param in respnse body
     * @param responseType object class type
     * @param <T> class type
     * @return object
     * @throws RdapClientException if fail to convert
     */
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
            if(responseCode == 200 || !getMethodType().equals(HttpMethodType.GET)){
                if (isSaveUnknownProperties()) {
                    unknownPropertiesMap = new HashMap<String, String>();
                }
                return JsonUtil.toObject(sb.toString(), responseType,
                        unknownPropertiesMap);
            }else{
                return null;
            }
            
        } catch (IOException io) {
            throw new RdapClientException(io.getMessage());
        } catch (RdapClientException rce) {
            throw rce;
        }
    }

    /**
     * responseCode getter
     * @return responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * responseCode setter
     * @param responseCode http status code
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }
    
    /**
     * methodType getter
     * @return methodType
     */
    public HttpMethodType getMethodType() {
        return methodType;
    }

    /**
     * methodType setter
     * @param methodType HttpMethodType
     */
    public void setMethodType(HttpMethodType methodType) {
        this.methodType = methodType;
    }

    /**
     * responseMessage getter
     * @return responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * responseMessage setter
     * @param responseMessage String
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    /**
     * in getter
     * @return in
     */
    private InputStream getIn() {
        return in;
    }

    /**
     * in setter
     * @param in InputStream
     */
    public void setIn(InputStream in) {
        this.in = in;
    }

    /**
     * unknownPropertiesMap getter
     * @return unknownPropertiesMap
     */
    public Map<String, String> getUnknownPropertiesMap() {
        return unknownPropertiesMap;
    }

    /**
     * saveUnknownProperties getter
     * @return true or false
     */
    public boolean isSaveUnknownProperties() {
        return saveUnknownProperties;
    }

    /**
     * saveUnknownProperties setter
     * @param saveUnknownProperties boolean
     */
    public void setSaveUnknownProperties(boolean saveUnknownProperties) {
        this.saveUnknownProperties = saveUnknownProperties;
    }

}