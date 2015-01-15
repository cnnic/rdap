package org.restfulwhois.rdap.common.support;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restfulwhois.rdap.common.exception.DecodeException;
import org.restfulwhois.rdap.common.filter.FilterHelper;
import org.restfulwhois.rdap.common.model.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * This is an {@link SimpleMappingExceptionResolver} implementation, which
 * handles {@link org.springframework.http.InvalidMediaTypeException} and other
 * exceptions.
 * <p>
 * For InvalidMediaTypeException it will return HTTP 415 error, and reutrn 500
 * for all other exceptions.
 * </p>
 * 
 * @author jiashuo
 * 
 */
public class MappingExceptionResolver extends SimpleMappingExceptionResolver {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(MappingExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex) {
        ResponseEntity<ErrorMessage> responseEntity = null;
        LOGGER.error("error:", ex);
        if (ex instanceof DecodeException) {
            responseEntity = RestResponse.createResponse400();
        } else if (ex instanceof InvalidMediaTypeException
                || ex instanceof HttpMediaTypeNotAcceptableException
                || ex instanceof HttpMediaTypeNotSupportedException) {
            responseEntity = RestResponse.createResponse415();
        } else if (ex instanceof HttpMessageNotReadableException) {
            responseEntity = RestResponse.createResponse422();
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            responseEntity = RestResponse.createResponse405();
        } else {
            responseEntity = RestResponse.createResponse500();
        }
        try {
            FilterHelper.writeResponse(responseEntity, response);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return new ModelAndView();
    }

}
