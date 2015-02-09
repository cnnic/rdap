package org.restfulwhois.rdap.filters.httpFilter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.filter.FilterHelper;
import org.restfulwhois.rdap.common.filter.HttpFilter;
import org.restfulwhois.rdap.common.model.ErrorMessage;
import org.restfulwhois.rdap.common.support.RdapProperties;
import org.restfulwhois.rdap.common.support.RestResponse;
import org.restfulwhois.rdap.common.validation.UpdateValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

/**
 * This class is used to authenticate for update API.
 * <p>
 * Update Requests from IP white list will be handled, and others will return
 * 403 error.
 * <p>
 * 
 * @author jiashuo
 * 
 */
public class AuthenticationForUpdateApiFilter implements HttpFilter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthenticationForUpdateApiFilter.class);

    /**
     * constructor.
     */
    public AuthenticationForUpdateApiFilter() {
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
        String ip = request.getRemoteAddr();
        List<String> ipWhiteList =
                RdapProperties.getIpWhiteListArrayForUpdateApi();
        if (null == ipWhiteList || !ipWhiteList.contains(ip)) {
            LOGGER.warn("update request from ip [{}] not in white list", ip);
            LOGGER.debug("ip white list:{}", ipWhiteList);
            writeError403Response(response);
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
        return true;
    }

    /**
     * write 403 error.
     * 
     * @param response
     *            response.
     * @throws IOException
     *             IOException.
     */
    private void writeError403Response(HttpServletResponse response)
            throws IOException {
        UpdateValidationError error =
                (UpdateValidationError) UpdateValidationError
                        .build4031Error();
        ResponseEntity<UpdateResponse> responseEntity =
                RestResponse.createUpdateResponse(UpdateResponse
                        .buildErrorResponse(error.getCode(),
                                error.getHttpStatusCode(),
                                error.getMessage()));
        FilterHelper.writeResponse(responseEntity, response);
    }

    @Override
    public boolean needFilter(HttpServletRequest req, HttpServletResponse res) {
        if (FilterHelper.isUpdateUri(req)) {
            return true;
        }
        return false;
    }
}
