package org.restfulwhois.rdap.port43.util;

import java.io.FileInputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * properties, from file PROPERTIES_FILE.
 * 
 * @author jiashuo
 * 
 */
public class RdapProperties {
    /**
     * default value for service port.
     */
    private static final String DEFAULT_SERVICE_PORT = "43";
    /**
     * default value for manage port.
     */
    private static final String DEFAULT_MANAGE_PORT = "9999";
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
     * service port.
     */
    private static Integer servicePort;
    /**
     * manage port.
     */
    private static Integer managePort;
    /**
     * min milliseconds between access interval.
     */
    private static Long minSecondsAccessInterval;
    /**
     * responseFormater class name.
     */
    private static String responseFormater;
    /**
     * prop file.
     */
    private static final String PROPERTIES_FILE = "conf/proxy43.properties";

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
            setServicePort(Integer.parseInt(resource.getProperty("servicePort",
                    DEFAULT_SERVICE_PORT)));
            setManagePort(Integer.parseInt(resource.getProperty("managePort",
                    DEFAULT_MANAGE_PORT)));
            setMinSecondsAccessInterval(Long.parseLong(resource
                    .getProperty("minSecondsAccessInterval")));
            setResponseFormater(resource.getProperty("responseFormater"));
        } catch (Exception e) {
            LOGGER.info("load properties error:{}", e);
        }
    }

    /**
     * get rdapServerBaseUrl.
     * 
     * @return rdapServerBaseUrl.
     */
    public static String getRdapServerBaseUrl() {
        return rdapServerBaseUrl;
    }

    /**
     * set rdapServerBaseUrl.
     * 
     * @param rdapServerBaseUrl
     *            rdapServerBaseUrl.
     */
    public static void setRdapServerBaseUrl(String rdapServerBaseUrl) {
        LOGGER.info("set rdapServerBaseUrl:{}", rdapServerBaseUrl);
        RdapProperties.rdapServerBaseUrl = rdapServerBaseUrl;
    }

    /**
     * get servicePort.
     * 
     * @return servicePort.
     */
    public static Integer getServicePort() {
        return servicePort;
    }

    /**
     * set servicePort.
     * 
     * @return servicePort.
     */
    public static Integer getManagePort() {
        return managePort;
    }

    /**
     * get managePort.
     * 
     * @param managePort
     *            managePort.
     */
    public static void setManagePort(Integer managePort) {
        LOGGER.info("set managePort:{}", managePort);
        RdapProperties.managePort = managePort;
    }

    /**
     * set managePort.
     * 
     * @param servicePort
     *            managePort.
     */
    public static void setServicePort(Integer servicePort) {
        LOGGER.info("set servicePort:{}", servicePort);
        RdapProperties.servicePort = servicePort;
    }

    /**
     * get minSecondsAccessInterval.
     * 
     * @return minSecondsAccessInterval.
     */
    public static Long getMinSecondsAccessInterval() {
        return minSecondsAccessInterval;
    }

    /**
     * set minSecondsAccessInterval.
     * 
     * @param minSecondsAccessInterval
     *            .
     */
    public static void
            setMinSecondsAccessInterval(Long minSecondsAccessInterval) {
        LOGGER.info("set minSecondsAccessInterval:{}", minSecondsAccessInterval);
        RdapProperties.minSecondsAccessInterval = minSecondsAccessInterval;
    }

    /**
     * get responseFormater.
     * 
     * @return responseFormater.
     */
    public static String getResponseFormater() {
        return responseFormater;
    }

    /**
     * set responseFormater.
     * 
     * @param responseFormater
     *            responseFormater.
     */
    public static void setResponseFormater(String responseFormater) {
        LOGGER.info("set responseFormater:{}", responseFormater);
        RdapProperties.responseFormater = responseFormater;
    }

}
