package org.restfulwhois.rdap.filters.httpFilter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restfulwhois.rdap.core.common.filter.FilterHelper;
import org.restfulwhois.rdap.core.common.filter.HttpFilter;
import org.restfulwhois.rdap.core.common.model.ErrorMessage;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.filters.httpFilter.service.ConnectionControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 * This class is used to limit access rate.
 * <p>
 * Rate limit is done by compareing the time interval between two request for
 * each IP address.
 * <p>
 * This filter is validate before query service, and do nothing in postProcess.
 * <p>
 * If exceed rate limit, it will return HTTP 429 error.
 * 
 * @author jiashuo
 * 
 */
public class RateLimitFilter implements HttpFilter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RateLimitFilter.class);

    /**
     * constructor.
     */
    public RateLimitFilter() {
        super();
        LOGGER.debug("init RDAP filter:{}", this.getName());
    }

    /**
     * do pre process request address.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @throws Exception
     *             Exception.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String remoteAddr = request.getRemoteAddr();
        if (ConnectionControlService.exceedRateLimit(remoteAddr)) {
            LOGGER.debug("exceedRateLimit,return 429 error.");
            this.writeError429Response(response);
            return false;
        }
        return true;
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
                RestResponseUtil.createResponse429();
        FilterHelper.writeResponse(responseEntity, response);
    }

    /**
     * @return this class name.
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * do post process.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @throws Exception
     *             Exception.
     * @return true .
     */
    @Override
    public boolean postProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return true;
    }
}
