package cn.cnnic.rdap.controller.support;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RDAP proxy filter.
 * 
 * @author jiashuo
 * 
 */
public class FilterChainProxy implements Filter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FilterChainProxy.class);

    /**
     * all filters.
     */
    private static List<RdapFilter> filters;

    /**
     * init filters when class loading.
     */
    static {
        LOGGER.info("init RDAP filters ...");
        filters = new ArrayList<RdapFilter>();
        filters.add(new RateLimitFilter());
        filters.add(new AuthenticationFilter());
        filters.add(new HttpRequestFilter());
        filters.add(new InvalidUriFilter());
        LOGGER.info("init RDAP filters end.");
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("begin pre filter ...");
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        boolean success = preProcess(request, response);
        LOGGER.debug("end pre filter, are all success?:{}", success);
        if (!success) {
            LOGGER.error("some pre filter fail, not process service.");
            return;
        }
        chain.doFilter(request, response);
        LOGGER.debug("begin post filter ...");
        success = postProcess(request, response);
        LOGGER.debug("end post filter, are all success?:{}", success);
    }

    /**
     * post process service method.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    private boolean postProcess(HttpServletRequest request,
            HttpServletResponse response) {
        for (RdapFilter filter : filters) {
            LOGGER.debug("call postProcess for:{}", filter.getName());
            try {
                if (!filter.postProcess(request, response)) {
                    LOGGER.error("  postProcess error:{}", filter.getName());
                    return false;
                }
            } catch (Exception e) {
                LOGGER.error("error:{}" + e.getMessage());
            }
        }
        return true;
    }

    /**
     * pre process service method.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    private boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) {
        for (RdapFilter filter : filters) {
            LOGGER.debug("call preProcess for:{}", filter.getName());
            try {
                if (!filter.preProcess(request, response)) {
                    LOGGER.error("  preProcess error:{}", filter.getName());
                    return false;
                }
            } catch (Exception e) {
                LOGGER.error("error:{}" + e.getMessage());
            }
        }
        return true;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

}
