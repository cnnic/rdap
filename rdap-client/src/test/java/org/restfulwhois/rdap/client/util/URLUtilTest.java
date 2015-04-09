package org.restfulwhois.rdap.client.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.client.exception.RdapClientException;

public class URLUtilTest {
    String url = "http://www.baidu.com";
    String path = "/ip/192.168.1.1";
    String param = "?p2=2&p1=1";
    String param1 = "?p1=1&p2=2";
    String urlWithPath = url + path;
    String urlWithParam = url + param;
    String urlWithParam1 = url + param1;
    String urlWithPathAndParam = url + path + param;
    String urlWithPathAndParam1 = url + path + param1;

    @Test
    public void test_makeURL() {
        String urlString;
        try {
            urlString = URLUtil.makeURL(url).toString();
        } catch (RdapClientException e) {
            urlString = null;
        }

        assertEquals(url, urlString);
    }

    @Test
    public void test_makeURLWithPath() {
        String urlString;
        try {
            urlString = URLUtil.makeURLWithPath(url, "ip", "192.168.1.1")
                    .toString();
        } catch (RdapClientException e) {
            urlString = null;
        }

        assertEquals(urlWithPath, urlString);
    }

    @Test
    public void test_makeURLWithPath_list() {
        String urlString;
        try {
            List<String> list = new ArrayList<String>();
            list.add("ip");
            list.add("192.168.1.1");
            urlString = URLUtil.makeURLWithPath(url, list).toString();
        } catch (RdapClientException e) {
            urlString = null;
        }

        assertEquals(urlWithPath, urlString);
    }

    @Test
    public void test_makeURLWithParam() {
        String urlString;
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("p1", "1");
            map.put("p2", "2");
            urlString = URLUtil.makeURLWithParam(url, map).toString();
        } catch (RdapClientException e) {
            urlString = null;
        }

        assertTrue(urlWithParam.equals(urlString)||urlWithParam1.equals(urlString)); 
    }

    @Test
    public void test_makeURLWithPathAndParam() {
        String urlString;
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("p1", "1");
            map.put("p2", "2");
            urlString = URLUtil.makeURLWithPathAndParam(url, map, "ip",
                    "192.168.1.1").toString();
        } catch (RdapClientException e) {
            urlString = null;
        }

        assertTrue(urlWithPathAndParam.equals(urlString)
                ||urlWithPathAndParam1.equals(urlString));
    }

    @Test
    public void test_makeURLWithPathAndParam_list() {
        String urlString;
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("p1", "1");
            map.put("p2", "2");
            List<String> list = new ArrayList<String>();
            list.add("ip");
            list.add("192.168.1.1");
            urlString = URLUtil.makeURLWithPathAndParam(url, map, list)
                    .toString();
        } catch (RdapClientException e) {
            urlString = null;
        }

        assertTrue(urlWithPathAndParam.equals(urlString)
                ||urlWithPathAndParam1.equals(urlString));
    }
}