package org.restfulwhois.rdap.filters.httpFilter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.acl.bean.Principal;
import org.restfulwhois.rdap.acl.bean.User;
import org.restfulwhois.rdap.authenticate.service.IdentityCheckService;
import org.restfulwhois.rdap.core.common.filter.FilterHelper;
import org.restfulwhois.rdap.core.common.filter.HttpFilter;
import org.restfulwhois.rdap.core.common.model.ErrorMessage;
import org.restfulwhois.rdap.core.common.support.PrincipalHolder;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.ServiceBeanUtil;
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
 * @author cnnic
 * @author jiashuo
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
        String tempPass = null;
        tempPass = request.getHeader("authorization");

        Principal principal = Principal.getAnonymousPrincipal();
        if (StringUtils.isNotBlank(tempPass)) {
            String authBasicPrefix = "Basic ";
            if (!StringUtils.startsWithIgnoreCase(tempPass, authBasicPrefix)) {
                writeError401Response(response);
                return false;
            }
            tempPass =
                    tempPass.substring(authBasicPrefix.length(),
                            tempPass.length());
            String tempPassdeCode = "";
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(tempPass);
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
                    ServiceBeanUtil.getIdentityCheckService();
            user = idcService.identityCheckService(userReqId, userReqPwd);
            if (null == user) {
                request.getSession().removeAttribute("SESSION_ATTR_USER_ID");
                writeError401Response(response);
                // chain.doFilter(request, response);
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
                RestResponseUtil.createResponse401();
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

}
