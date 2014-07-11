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
import org.springframework.http.ResponseEntity;

import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.service.impl.ConnectionControlService;

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
        LOGGER.debug("init RDAP filters ...");
        filters = new ArrayList<RdapFilter>();
        filters.add(new AuthenticationFilter());
        filters.add(new RateLimitFilter());
        filters.add(new HttpRequestFilter());
        filters.add(new InvalidUriFilter());
        LOGGER.debug("init RDAP filters end.");
    }

    /**
     * destructor .
     */
    @Override
    public void destroy() {
    }
    /**
     * filter chain .
     * 
     * @param req
     *            request.
     * @param res
     *            response.
     * @param chain
     *            filterchain.          
     * @throws IOException
     *             Exception.
     * @throws ServletException
     *             Exception.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("begin pre filter ...");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax()) {
            ConnectionControlService.decrementAndGetCurrentQueryCount();
            writeError509Response(response);
            return;
        }
        boolean success = preProcess(request, response);
        LOGGER.debug("end pre filter, are all success?:{}", success);
        if (!success) {
            LOGGER.error("some pre filter fail, not process service.");
            ConnectionControlService.decrementAndGetCurrentQueryCount();
            return;
        }
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            LOGGER.error("chain.doFilter error:{}" + e.getMessage());
        }
        LOGGER.debug("begin post filter ...");
        success = postProcess(request, response);
        LOGGER.debug("end post filter, are all success?:{}", success);
        ConnectionControlService.decrementAndGetCurrentQueryCount();
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
    /**
     * initialize.
     * 
     * @param config
     *            FilterConfig.
     * @throws ServletException
     *              exception.            
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    /**
     * write 509 error.
     * 
     * @param response
     *            response.
     * @throws IOException
     *             IOException.
     */
    private void writeError509Response(HttpServletResponse response)
            throws IOException {
        ResponseEntity<ErrorMessage> responseEntity =
                RestResponseUtil.createResponse509();
        FilterHelper.writeResponse(responseEntity, response);
    }

}
