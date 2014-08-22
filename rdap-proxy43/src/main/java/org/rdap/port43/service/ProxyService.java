package org.rdap.port43.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.rdap.port43.service.command.DomainQueryHandler;
import org.rdap.port43.service.format.ResponseFormater;

/**
 * proxy service.
 * 
 * @author jiashuo
 * 
 */
public class ProxyService {
    /**
     * singleton instance.
     */
    private static ProxyService proxyService = new ProxyService();
    /**
     * query handlers.
     */
    private List<QueryHandler> queryHandlers = new ArrayList<QueryHandler>();

    /**
     * default constructor.
     */
    public ProxyService() {
        super();
        queryHandlers.add(new DomainQueryHandler());
    }

    public static ProxyService getInstance() {
        return proxyService;
    }

    public String execute(String commandStr) {
        String requestURI = StringUtils.EMPTY;
        for (QueryHandler handler : queryHandlers) {
            if (handler.supportCmd(commandStr)) {
                requestURI = handler.generateRequestURI();
                break;
            }
        }
        if (StringUtils.isBlank(requestURI)) {
            return "";
        }
        String jsonStr = RestClient.getInstance().execute(requestURI);
        Map jsonMap = JsonUtil.convertToMap(jsonStr);
        String result = ResponseFormater.format(jsonMap);
        return result;
        // return "this is response for:" + commandStr;
    }
}