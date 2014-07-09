package cn.cnnic.rdap.controller.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import sun.misc.BASE64Decoder;
import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.bean.Principal;
import cn.cnnic.rdap.bean.User;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.common.util.ServiceBeanUtil;
import cn.cnnic.rdap.service.IdentityCheckService;

/**
 * authentication filter, set user id to session after logined.
 * 
 * @author
 * 
 */
public class AuthenticationFilter implements RdapFilter {
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
        LOGGER.info("init RDAP filter:{}", this.getName());
    }

    @Override
    public boolean preProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        String tempPass = null;
        tempPass = request.getHeader("authorization");

        Principal principal = Principal.getAnonymousPrincipal();
        if (StringUtils.isNotBlank(tempPass)) {
            String authBasicPrefix = "Basic ";
            if (!StringUtils.startsWithIgnoreCase(tempPass,
                    authBasicPrefix)) {
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

    @Override
    public boolean postProcess(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return true;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

}
