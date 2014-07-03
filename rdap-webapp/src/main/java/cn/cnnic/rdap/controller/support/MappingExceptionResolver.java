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

/**
 * handle exception.
 * 
 * @author jiashuo
 * 
 */
public class MappingExceptionResolver extends SimpleMappingExceptionResolver {
    /**
     * logger.
     */
    private static Logger LOGGER = LoggerFactory
            .getLogger(MappingExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        ResponseEntity<ErrorMessage> responseEntity = null;
        LOGGER.error("error:", ex);
        if (ex instanceof InvalidMediaTypeException) {
            responseEntity = RestResponseUtil.createResponse415();
        } else {
            responseEntity = RestResponseUtil.createResponse500();
        }
        try {
            FilterHelper.writeResponse(responseEntity, response);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return new ModelAndView();
    }

}