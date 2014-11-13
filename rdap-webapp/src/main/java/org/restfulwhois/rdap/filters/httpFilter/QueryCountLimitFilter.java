package org.restfulwhois.rdap.filters.httpFilter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restfulwhois.rdap.core.common.filter.FilterChainProxy;
import org.restfulwhois.rdap.core.common.filter.FilterHelper;
import org.restfulwhois.rdap.core.common.filter.HttpFilter;
import org.restfulwhois.rdap.core.common.model.ErrorMessage;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.filters.httpFilter.service.ConnectionControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 * Concurrent query count limit filter.
 * <p>
 * This class is deprecated, and its service is moved into
 * {@link FilterChainProxy}, For postProcess can not guarantee to be called
 * after response.
 * 
 * @author jiashuo
 * 
 */
@Deprecated
public class QueryCountLimitFilter implements HttpFilter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueryCountLimitFilter.class);

    /**
     * constructor.
     */
    public QueryCountLimitFilter() {
        super();
        LOGGER.debug("init RDAP filter:{}", this.getName());
    }

    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax()) {
            LOGGER.debug("exceed max concurrent count,return 509 error.");
            this.writeError509Response(response);
            return false;
        }
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

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean postProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return true;
    }
}
