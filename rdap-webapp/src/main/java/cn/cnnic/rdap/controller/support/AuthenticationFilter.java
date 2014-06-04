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

import org.springframework.http.ResponseEntity;

import cn.cnnic.rdap.bean.Principal;
import cn.cnnic.rdap.bean.User;
import cn.cnnic.rdap.service.IdentityCheckService;
import cn.cnnic.rdap.service.impl.IdentityCheckServiceImpl;
import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.common.util.RestResponseUtil;

import sun.misc.BASE64Decoder;


/**
 * authentication filter, set user id to session after logined.
 * 
 * @author
 * 
 */
public class AuthenticationFilter implements Filter {
    
    private IdentityCheckService idcService;

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        
        String tempPass= null;
        tempPass=request.getHeader("authorization");
        
        if(tempPass!=null){
            
            tempPass= tempPass.substring(6,tempPass.length()); 
            String tempPassdeCode=""; 
            BASE64Decoder decoder = new BASE64Decoder();
            
            try {
                byte[] b = decoder.decodeBuffer(tempPass);
                tempPassdeCode= new String(b); 
                } catch (Exception e) {
                return ;
            }
            
            String userReqId="";
            String userReqPwd="";
            userReqId = tempPassdeCode.substring(0,tempPassdeCode.indexOf(":")); 
            userReqPwd = tempPassdeCode.substring(tempPassdeCode.indexOf(":")+1); 
            User user=null;
            user =idcService.IdentityCheckService(userReqId, userReqPwd);
            if(user.getUserType().equals(User.UserType.Anonymous)){
                writeError401Response(response);
                chain.doFilter(request, response);
                return;
            }
            else{
                request.getSession().setAttribute("SESSION_ATTR_USER_ID", user.getUserId());
            }
        }

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
        idcService = new IdentityCheckServiceImpl();
    }
    
     private void writeError401Response(HttpServletResponse response)
            throws IOException {
        ResponseEntity<ErrorMessage> responseEntity = RestResponseUtil
                .createResponse401();
        FilterHelper.writeResponse(responseEntity, response);
    }
}
