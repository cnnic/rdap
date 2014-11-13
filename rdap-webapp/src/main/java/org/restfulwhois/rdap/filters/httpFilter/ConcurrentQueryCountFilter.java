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
 * This class is used to control concurrent connection count.
 * <p>
 * This filter will increase concurrent connection count before query service,
 * and MUST　decrease concurrent connection count before query service.
 * <p>
 * If exceed max concurrent connection count, it will return HTTP 509 error.
 * 
 * @author jiashuo
 * 
 */
public class ConcurrentQueryCountFilter implements HttpFilter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConcurrentQueryCountFilter.class);

    /**
     * constructor.
     */
    public ConcurrentQueryCountFilter() {
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
        if (ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax()) {
            writeError509Response(response);
            return false;
        }
        return true;
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
        ConnectionControlService.decrementAndGetCurrentQueryCount();
        return true;
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
