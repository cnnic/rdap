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
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
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
public class AuthenticationFilter implements Filter {

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;

        String tempPass = null;
        tempPass = request.getHeader("authorization");

        Principal principal = Principal.getAnonymousPrincipal();
        if (StringUtils.isNotBlank(tempPass)) {
            String AUTH_BASIC_PREFIX = "Basic ";
            if(!StringUtils.startsWith(tempPass,AUTH_BASIC_PREFIX)){
                writeError401Response(response);
                return;
            }
            tempPass = tempPass.substring(AUTH_BASIC_PREFIX.length(), tempPass.length());
            String tempPassdeCode = "";
            BASE64Decoder decoder = new BASE64Decoder();

            try {
                byte[] b = decoder.decodeBuffer(tempPass);
                tempPassdeCode = new String(b);
            } catch (Exception e) {
                writeError401Response(response);
                return;
            }

            String userReqId = "";
            String userReqPwd = "";
            int indexOfSeparator = tempPassdeCode.indexOf(":");
            if (-1 == indexOfSeparator) {
                writeError401Response(response);
                return;
            }
            userReqId = tempPassdeCode.substring(0, indexOfSeparator);
            userReqPwd = tempPassdeCode.substring(indexOfSeparator + 1);
            User user = null;
            IdentityCheckService idcService =
                    ServiceBeanUtil.getIdentityCheckService();
            user = idcService.IdentityCheckService(userReqId, userReqPwd);
            if (null == user) {
                request.getSession().removeAttribute("SESSION_ATTR_USER_ID");
                writeError401Response(response);
                // chain.doFilter(request, response);
                return;
            } else {
                principal = new Principal(user.getUserId());
            }
        }
        PrincipalHolder.setPrincipal(principal);
        chain.doFilter(request, response);
        PrincipalHolder.remove();
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    private void writeError401Response(HttpServletResponse response)
            throws IOException {
        ResponseEntity<ErrorMessage> responseEntity =
                RestResponseUtil.createResponse401();
        FilterHelper.writeResponse(responseEntity, response);
    }
}
