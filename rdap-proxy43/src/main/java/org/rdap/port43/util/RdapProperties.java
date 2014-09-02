package org.rdap.port43.util;

import java.io.FileInputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties.
 * 
 * @author jiashuo
 * 
 */
public class RdapProperties {
    /**
     * default value for port.
     */
    private static final String DEFAULT_VALUE_PORT = "43";
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RdapProperties.class);
    /**
     * properties.
     */
    private static Properties resource;

    /**
     * rdapServerBaseUrl.
     */
    private static String rdapServerBaseUrl;
    /**
     * port.
     */
    private static Integer port;
    /**
     * min seconds between access interval.
     */
    private static Long minSecondsAccessInterval;
    /**
     * responseFormater class name.
     */
    private static String responseFormater;
    /**
     * prop file.
     */
    private static final String PROPERTIES_FILE = "proxy43.properties";

    /**
     * Load the resource file
     */
    static {
        resource = new Properties();
        try {
            LOGGER.info("load properties from file:{}", PROPERTIES_FILE);
            CodeSource src =
                    RdapProperties.class.getProtectionDomain().getCodeSource();
            URL url = new URL(src.getLocation(), PROPERTIES_FILE);
            resource.load(new FileInputStream(url.getPath()));
            // this dosen't work if property file is out of jar.
            // resource.load(RdapProperties.class
            // .getResourceAsStream(PROPERTIES_FILE));
            setRdapServerBaseUrl(resource.getProperty("rdapServerBaseUrl"));
            setPort(Integer.parseInt(resource.getProperty("port",
                    DEFAULT_VALUE_PORT)));
            setMinSecondsAccessInterval(Long.parseLong(resource
                    .getProperty("minSecondsAccessInterval")));
            setResponseFormater(resource.getProperty("responseFormater"));
        } catch (Exception e) {
            LOGGER.info("load properties error:{}", e);
        }
    }

    public static String getRdapServerBaseUrl() {
        return rdapServerBaseUrl;
    }

    public static void setRdapServerBaseUrl(String rdapServerBaseUrl) {
        LOGGER.info("set rdapServerBaseUrl:{}", rdapServerBaseUrl);
        RdapProperties.rdapServerBaseUrl = rdapServerBaseUrl;
    }

    public static Integer getPort() {
        return port;
    }

    public static void setPort(Integer port) {
        LOGGER.info("set port:{}", port);
        RdapProperties.port = port;
    }

    public static Long getMinSecondsAccessInterval() {
        return minSecondsAccessInterval;
    }

    public static void
            setMinSecondsAccessInterval(Long minSecondsAccessInterval) {
        LOGGER.info("set minSecondsAccessInterval:{}", minSecondsAccessInterval);
        RdapProperties.minSecondsAccessInterval = minSecondsAccessInterval;
    }

    public static String getResponseFormater() {
        return responseFormater;
    }

    public static void setResponseFormater(String responseFormater) {
        LOGGER.info("set responseFormater:{}", responseFormater);
        RdapProperties.responseFormater = responseFormater;
    }

}
