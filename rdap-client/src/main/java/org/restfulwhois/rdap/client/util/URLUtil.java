package org.restfulwhois.rdap.client.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.restfulwhois.rdap.client.exception.RdapClientException;

/**
 * This class is mainly used to make URL with uri or parameter
 * @author M.D.
 *
 */
public final class URLUtil {
    /**
     * "/" character
     */
    private static final String SLASH = "/";
    /**
     * "&" character
     */
    private static final String SPLIT = "&";
    /**
     * "?" character
     */
    private static final String SEPARATE = "?";
    /**
     * "=" character
     */
    private static final String ASSIGN = "=";

    /**
     * Default constructor
     */
    private URLUtil(){}
    
    /**
     * Creates a URL object from the url string.
     * @param url the String to parse as a URL
     * @return URL 
     * @throws RdapClientException  if no protocol is specified, 
     * or an unknown protocol is found, or url is null.
     */
    public static URL makeURL(String url) throws RdapClientException {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    /**
     * Creates a URL object from the url string and path string.
     * @param url the String to parse as a URL
     * @param path the String marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPath(String url, String... path)
            throws RdapClientException {

        if (path != null && path.length != 0) {
            return makeURLWithPath(url, Arrays.asList(path));
        } else {
            return makeURL(url);
        }
    }

    /**
     * Creates a URL object from the url string and path list.
     * @param url the String to parse as a URL
     * @param path the List marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPath(String url, List<String> path)
            throws RdapClientException {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (path != null && path.size() != 0) {
            for (String p : path) {
                if (!StringUtil.isEmpty(p)){
                    urlBuilder.append(SLASH).append(p);
                }
            }
        }
        try {
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    /**
     * Creates a URL object from the url string and parameter map.
     * @param url the String to parse as a URL
     * @param param parameter map
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithParam(String url, Map<String, String> param)
            throws RdapClientException {
        StringBuilder urlBuilder = new StringBuilder(url);
        if (param != null) {
            Set<String> keys = param.keySet();
            if (keys.size() != 0) {
                StringBuilder paramBuilder = new StringBuilder();
                for (String key : keys) {
                    paramBuilder.append(key).append(ASSIGN)
                            .append(param.get(key)).append(SPLIT);
                }
                urlBuilder.append(SEPARATE).append(
                        paramBuilder.substring(0, paramBuilder.length() - 1));
            }
        }
        try {
            return new URL(urlBuilder.toString());
        } catch (MalformedURLException e) {
            throw new RdapClientException(e.getMessage());
        }
    }

    /**
     * Creates a URL object from the url string ,path string and parameter map.
     * @param url the String to parse as a URL
     * @param param parameter map
     * @param path the String marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPathAndParam(String url,
            Map<String, String> param, String... path)
            throws RdapClientException {

        if (path != null && path.length != 0){
            return makeURLWithPathAndParam(url, param, Arrays.asList(path));
        }else{
            return makeURLWithParam(url, param);
        }
    }

    /**
     * Creates a URL object from the url string ,path string and parameter map.
     * @param url the String to parse as a URL
     * @param param parameter map
     * @param path the List marks resource
     * @return URL
     * @throws RdapClientException if no protocol 
     * is specified, or an unknown protocol is found, or url is null.
     */
    public static URL makeURLWithPathAndParam(String url,
            Map<String, String> param, List<String> path)
            throws RdapClientException {

        StringBuilder urlBuilder = new StringBuilder(url);
        if (path != null && path.size() != 0) {
            for (String p : path) {
                if (!StringUtil.isEmpty(p)){
                    urlBuilder.append(SLASH).append(p);
                }
            }
        }
        return makeURLWithParam(urlBuilder.toString(), param);
    }

    /**
     * It will return true if it used https protocol.
     * @param url url
     * @return boolean
     */
    public static boolean isHttps(URL url) {
        String protocol = url.getProtocol();
        if (protocol.equalsIgnoreCase(ProtocolType.HTTPS.name())) {
            return true;
        }
        return false;
    }

    /**
     * This enumeration lists two protocol types are http and https.
     * @author M.D.
     *
     */
    public enum ProtocolType {
        /**
         * http
         */
        HTTP, 
        /**
         * https
         */
        HTTPS;
    }
}