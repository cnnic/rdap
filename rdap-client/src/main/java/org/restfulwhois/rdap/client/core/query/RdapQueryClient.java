package org.restfulwhois.rdap.client.core.query;

import java.util.ArrayList;
import java.util.List;

public class RdapQueryClient {
    
    private int connectTimeout;
    private int readTimeout;
    private String urlStr;
    
    public RdapQueryClient(String url) {
        connectTimeout = 3000;
        readTimeout = 10000;
        this.urlStr = url;
    }
    
    

    private List<String> arrayToList(String[] array) {
        List<String> rtnList = new ArrayList<String>();
        if (array == null || array.length == 0 || array.length > 2) {
        } else {
            if (array.length > 1) {
                String a = array[0];
                String b = array[1];
                if (a.contains(":") || a.contains(".")) {
                    rtnList.add(a);
                    rtnList.add(b);
                } else {
                    rtnList.add(b);
                    rtnList.add(a);
                }
            } else
                rtnList.add(array[0]);
        }
        return rtnList;
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


    public String getUrlStr() {
        return urlStr;
    }


    public void setUrlStr(String urlStr) {
        this.urlStr = urlStr;
    }

    
    
}