package org.restfulwhois.rdap.client.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.restfulwhois.rdap.client.exception.RdapClientException;

public class URLUtil {
    private static final String slash = "/";
    private static final String split = "&";
    private static final String separate = "?";
    private static final String assign = "=";

    public static URL makeURL(String url) throws RdapClientException {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    public static URL makeURLWithPath(String url, String... path)
            throws RdapClientException {

        if (path != null && path.length != 0) {
            return makeURLWithPath(url, Arrays.asList(path));
        } else {
            return makeURL(url);
        }
    }

    public static URL makeURLWithPath(String url, List<String> path)
            throws RdapClientException {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (path != null && path.size() != 0) {
            for (String p : path) {
                if (!isEmpty(p))
                    urlBuilder.append(slash).append(p);
            }
        }
        try {
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    public static URL makeURLWithParam(String url, Map<String, String> param)
            throws RdapClientException {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (param != null) {
            Set<String> keys = param.keySet();
            if (keys.size() != 0) {
                StringBuilder paramBuilder = new StringBuilder();
                for (String key : keys) {
                    paramBuilder.append(key).append(assign)
                            .append(param.get(key)).append(split);
                }
                urlBuilder.append(separate).append(
                        paramBuilder.substring(0, paramBuilder.length() - 1));
            }
        }
        try {
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    public static URL makeURLWithPathAndParam(String url,
            Map<String, String> param, String... path)
            throws RdapClientException {

        if (path != null && path.length != 0)
            return makeURLWithPathAndParam(url, param, Arrays.asList(path));
        else
            return makeURLWithParam(url, param);
    }

    public static URL makeURLWithPathAndParam(String url,
            Map<String, String> param, List<String> path)
            throws RdapClientException {

        StringBuilder urlBuilder = new StringBuilder(url);
        if (path != null && path.size() != 0) {
            for (String p : path) {
                if (!isEmpty(p))
                    urlBuilder.append(slash).append(p);
            }
        }
        return makeURLWithParam(urlBuilder.toString(), param);
    }

    private static boolean isEmpty(String s) {
        if (s == null || "".equals(s.trim()))
            return true;
        else
            return false;
    }

}