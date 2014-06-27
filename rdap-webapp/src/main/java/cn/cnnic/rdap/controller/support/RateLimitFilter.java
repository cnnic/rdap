package cn.cnnic.rdap.controller.support;

import java.io.IOException;

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
 * rate limit filter.
 * 
 * @author jiashuo
 * 
 */
public class RateLimitFilter implements Filter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RateLimitFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("RateLimitFilter ...");
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        String remoteAddr = request.getRemoteAddr();
        if (ConnectionControlService.exceedRateLimit(remoteAddr)) {
            LOGGER.info("exceedRateLimit,return 429 error.");
            this.writeError429Response(response);
        }
        LOGGER.debug("not exceedRateLimit.");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    /**
     * write 429 error.
     * 
     * @param response
     *            response.
     * @throws IOException
     *             IOException.
     */
    private void writeError429Response(HttpServletResponse response)
            throws IOException {
        ResponseEntity<ErrorMessage> responseEntity =
                RestResponseUtil.createResponse401();
        FilterHelper.writeResponse(responseEntity, response);
    }

}
