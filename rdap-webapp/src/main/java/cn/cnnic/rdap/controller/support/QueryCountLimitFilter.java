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
 * concurrent query count limit filter.
 * 
 * @author jiashuo
 * 
 */
public class QueryCountLimitFilter implements RdapFilter {

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
        LOGGER.info("init RDAP filter:{}", this.getName());
    }

    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        if (ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax()) {
            LOGGER.info("exceed max concurrent count,return 509 error.");
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
