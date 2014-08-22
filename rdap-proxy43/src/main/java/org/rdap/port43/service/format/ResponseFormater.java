package org.rdap.port43.service.format;

import java.util.Map;

import org.rdap.port43.util.ReflectionUtil;

/**
 * 
 * @author jiashuo
 * 
 */
public class ResponseFormater {

    public static String format(Map map) {
        Formater formater =
                (Formater) ReflectionUtil
                        .createInstance("org.rdap.port43.service.format.JsonFormater");
        return formater.format(map);
    }

}
