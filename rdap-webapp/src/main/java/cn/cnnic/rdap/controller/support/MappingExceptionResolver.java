package cn.cnnic.rdap.controller.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.common.util.StringUtil;

/**
 * handle exception.
 * 
 * @author jiashuo
 * 
 */
public class MappingExceptionResolver extends SimpleMappingExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(StringUtil.class);

    /**
     * handle redirect exception
     */
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        if (ex instanceof InvalidMediaTypeException) {
            try {
                ResponseEntity<ErrorMessage> responseEntity = RestResponseUtil
                        .createResponse415();
                FilterHelper.writeResponse(responseEntity, response);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return new ModelAndView();
        }
        return null;
    }
}