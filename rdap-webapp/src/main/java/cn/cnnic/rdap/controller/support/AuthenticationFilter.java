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

import cn.cnnic.rdap.bean.Principal;

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
        // TODO:authentication if header has auth info,and set userId to
        // session.
        // especially, userId set 0 for anonymous user.
        HttpSession session = request.getSession();
        Principal principal = Principal.getAnonymousPrincipal();
        if (null != session) {
            Object userId = session.getAttribute("SESSION_ATTR_USER_ID");
            // TODO: SESSION_ATTR_USER_ID change to constant.
            if(null != userId){
                principal = new Principal((Long)userId);
            }
        }
        PrincipalHolder.setPrincipal(principal);
        chain.doFilter(request, response);
        PrincipalHolder.remove();
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }
}
