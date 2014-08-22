package org.rdap.port43.service.format;

import java.util.Map;

/**
 * response formater.
 * 
 * @author jiashuo
 * 
 */
public interface Formater {
    /**
     * format to string.
     * 
     * @param map
     *            map.
     * @return formated string.
     */
    String format(Map map);
}
