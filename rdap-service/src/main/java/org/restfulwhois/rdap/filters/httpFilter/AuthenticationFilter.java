package org.restfulwhois.rdap.filters.httpFilter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.acl.bean.Principal;
import org.restfulwhois.rdap.acl.bean.User;
import org.restfulwhois.rdap.authenticate.service.IdentityCheckService;
import org.restfulwhois.rdap.common.filter.FilterHelper;
import org.restfulwhois.rdap.common.filter.HttpFilter;
import org.restfulwhois.rdap.common.model.ErrorMessage;
import org.restfulwhois.rdap.common.support.PrincipalHolder;
import org.restfulwhois.rdap.common.support.RestResponse;
import org.restfulwhois.rdap.common.support.ServiceBeanFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import sun.misc.BASE64Decoder;

/**
 * This class is used to authenticate.
 * <p>
 * It support HTTP BASIC.
 * <p>
 * Client MUST use HTTPS when use HTTP BASIC authentication.
 * <p>
 * After success authentication, an {@link Principal} will be set in request
 * thread local variable, in PrincipalHolder; And HTTP 401 will be responsed if
 * fail.
 * 
 */
public class AuthenticationFilter implements HttpFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AuthenticationFilter.class);

    /**
     * constructor.
     */
    public AuthenticationFilter() {
        super();
        LOGGER.debug("init RDAP filter:{}", this.getName());
    }

    /**
     * do pre process request authorization.
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
        String authorizationStr = null;
        authorizationStr = request.getHeader("authorization");

        Principal principal = Principal.getAnonymousPrincipal();
        if (StringUtils.isNotBlank(authorizationStr)) {
            String authBasicPrefix = "Basic ";
            if (!StringUtils.startsWithIgnoreCase(authorizationStr,
                    authBasicPrefix)) {
                writeError401Response(response);
                return false;
            }
            authorizationStr =
                    authorizationStr.substring(authBasicPrefix.length(),
                            authorizationStr.length());
            String tempPassdeCode = "";
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(authorizationStr);
                tempPassdeCode = new String(b);
            } catch (Exception e) {
                writeError401Response(response);
                return false;
            }

            String userReqId = "";
            String userReqPwd = "";
            int indexOfSeparator = tempPassdeCode.indexOf(":");
            if (-1 == indexOfSeparator) {
                writeError401Response(response);
                return false;
            }
            userReqId = tempPassdeCode.substring(0, indexOfSeparator);
            userReqPwd = tempPassdeCode.substring(indexOfSeparator + 1);
            User user = null;
            IdentityCheckService idcService =
                    ServiceBeanFactory.getIdentityCheckService();
            user = idcService.identityCheckService(userReqId, userReqPwd);
            if (null == user) {
                request.getSession().removeAttribute("SESSION_ATTR_USER_ID");
                writeError401Response(response);
                return false;
            } else {
                principal = new Principal(user.getUserId());
            }
        }
        PrincipalHolder.setPrincipal(principal);
        return true;
    }

    /**
     * 401 error.
     * 
     * @param response
     *            response.
     * @throws IOException
     *             IOException.
     */
    private void writeError401Response(HttpServletResponse response)
            throws IOException {
        ResponseEntity<ErrorMessage> responseEntity =
                RestResponse.createResponse401();
        FilterHelper.writeResponse(responseEntity, response);
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
     * @return this class name.
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public boolean needFilter(HttpServletRequest req, HttpServletResponse res) {
        if (FilterHelper.isUpdateUri(req)) {
            return false;
        }
        return true;
    }

}
