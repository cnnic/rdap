package org.rdap.port43.service.format;

import java.util.Map;

import org.rdap.port43.service.JsonUtil;

/**
 * 
 * @author jiashuo
 * 
 */
public class JsonFormater implements Formater {

    @Override
    public String format(Map map) {
        return JsonUtil.toJsonWithPrettyFormat(map);
    }
}
