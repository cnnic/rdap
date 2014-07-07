package cn.cnnic.rdap.controller.support;

import java.io.IOException;

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
public class RateLimitFilter implements RdapFilter {

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
        LOGGER.info("init RDAP filter:{}", this.getName());
    }

    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String remoteAddr = request.getRemoteAddr();
        if (ConnectionControlService.exceedRateLimit(remoteAddr)) {
            LOGGER.info("exceedRateLimit,return 429 error.");
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
