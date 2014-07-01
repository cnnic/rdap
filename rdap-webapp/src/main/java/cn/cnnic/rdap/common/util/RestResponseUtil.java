/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * * Neither the name of the ICANN, CNNIC nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package cn.cnnic.rdap.common.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.service.ErrorMessageService;
import cn.cnnic.rdap.service.impl.ResponseDecorator;
import cn.cnnic.rdap.service.PolicyControlService;

/**
 * RestResponseUtil is used to create ResponseEntity.
 * 
 * @author jiashuo
 * 
 */
@Component
public class RestResponseUtil {
    /**
     * all ErrorMessage map,must be init before call.
     * getErrorMessageByErrorCode()
     */
    private static Map<Long, ErrorMessage> errorMessageMap = null;

    /**
     * error message service.
     */
    private static ErrorMessageService errorMessageService;

    /**
     * for response object.
     */
    private static ResponseDecorator responseDecorator;
    
    /**
     * for policy service.
     */
    private static PolicyControlService policyService;

    /**
     * init the error message and policy service.
     */
    @PostConstruct
    private void init() {
        initErrorMessages();
        initPolicyService();
    }

    /**
     * init policy service.
     */
    public static void initPolicyService() {
        policyService.loadAllPolicyByMap();
    }
    
    /**
     * init ErrorMessages list.
     */
    public static void initErrorMessages() {
        errorMessageMap = errorMessageService.getAllErrorMessageMap();
    }

    /**
     * get ErrorMessage by error code.
     * 
     * @param errorCode
     *            error code
     * @return ErrorMessage
     */
    private static ErrorMessage getErrorMessageByErrorCode(String errorCode) {
        Long codeLongVal = Long.valueOf(errorCode);
        if (null == errorMessageMap) {
            initErrorMessages();
        }
        ErrorMessage result = errorMessageMap.get(codeLongVal);
        if (null != result) {
            return result;
        }
        return ErrorMessage.getNullErrorMessage();
    }

    /**
     * create response with HTTP status code 200.
     * 
     * @param response
     *            model T of response.
     * @param <T>
     *            a model
     * @return ResponseEntity<T> ResponseEntity model.
     */
    public static <T> ResponseEntity<T> createResponse200(T response) {
        return new ResponseEntity<T>(response, HttpStatus.OK);
    }

    /**
     * create response with HTTP status code 400.
     * 
     * @return ResponseEntity entity of response.
     */
    public static ResponseEntity<ErrorMessage> createResponse400() {
        return createCommonErrorResponse(HttpStatus.BAD_REQUEST);
    }

    /**
     * create response with HTTP status code 401.
     * 
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorMessage> createResponse401() {
        return createCommonErrorResponse(HttpStatus.UNAUTHORIZED);
    }

    /**
     * create response with HTTP status code 403.
     * 
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorMessage> createResponse403() {
        return createCommonErrorResponse(HttpStatus.FORBIDDEN);
    }

    /**
     * create response with HTTP status code 405.
     * 
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorMessage> createResponse405() {
        HttpHeaders headers = new HttpHeaders();
        Set<HttpMethod> allowMethods = new HashSet<HttpMethod>();
        allowMethods.add(HttpMethod.GET);
        headers.setAllow(allowMethods);
        ResponseEntity<ErrorMessage> response =
                createErrorResponseWithHeaders(HttpStatus.METHOD_NOT_ALLOWED,
                        headers);
        return response;
    }

    /**
     * create response with HTTP status code 415.
     * 
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorMessage> createResponse415() {
        return createCommonErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * create response with HTTP status code 404.
     * 
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorMessage> createResponse404() {
        return createCommonErrorResponse(HttpStatus.NOT_FOUND);
    }

    /**
     * create response with HTTP status code 500.
     * 
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorMessage> createResponse500() {
        return createCommonErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * create response with HTTP status code 422.
     * 
     * @return ResponseEntity
     */
    public static ResponseEntity<ErrorMessage> createResponse422() {
        return createCommonErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * create response with HTTP status code 301.
     * 
     * @param response
     *            model object.
     * @return ResponseEntity.
     */
    public static ResponseEntity<ErrorMessage> createResponse301(
            String redirectUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Location", redirectUrl);
        ResponseEntity<ErrorMessage> response =
                createErrorResponseWithHeaders(HttpStatus.MOVED_PERMANENTLY,
                        headers);
        return response;
    }

    /**
     * create response with HTTP status code 429.
     * 
     * @param response
     *            model object.
     * @return ResponseEntity.
     */
    public static ResponseEntity<ErrorMessage> createResponse429() {
        return createCommonErrorResponse(HttpStatus.TOO_MANY_REQUESTS);
    }

    /**
     * create response with HTTP status code 509.
     * 
     * @param response
     *            model object.
     * @return ResponseEntity.
     */
    public static ResponseEntity<ErrorMessage> createResponse509() {
        return createCommonErrorResponse(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
    }

    /**
     * create error response.
     * 
     * @param errorStatus
     *            HttpStatus of error message
     * @return ResponseEntity
     */
    private static ResponseEntity<ErrorMessage> createCommonErrorResponse(
            HttpStatus errorStatus) {
        ErrorMessage errorMessage =
                getErrorMessageByErrorCode(errorStatus.toString());
        responseDecorator.decorateResponse(errorMessage);
        return new ResponseEntity<ErrorMessage>(errorMessage, errorStatus);
    }

    /**
     * create error response,with headers.
     * 
     * @param errorStatus
     *            HttpStatus of error message
     * @param responseHeaders
     *            headers of response.
     * @return ResponseEntity entity of response.
     */
    private static ResponseEntity<ErrorMessage> createErrorResponseWithHeaders(
            HttpStatus errorStatus, HttpHeaders responseHeaders) {
        ErrorMessage errorMessage =
                getErrorMessageByErrorCode(errorStatus.toString());
        responseDecorator.decorateResponse(errorMessage);
        return new ResponseEntity<ErrorMessage>(errorMessage, responseHeaders,
                errorStatus);
    }

    /**
     * set error message service.
     * 
     * @param errorMessageService
     *            error message service to set.
     */
    @Autowired
    public void setErrorMessageService(ErrorMessageService errorMessageService) {
        RestResponseUtil.errorMessageService = errorMessageService;
    }

    /**
     * response object.
     * 
     * @param responseDecorator
     *            response object.
     */
    @Autowired
    public void setResponseDecorator(ResponseDecorator responseDecorator) {
        RestResponseUtil.responseDecorator = responseDecorator;
    }
    /**
     * policy service.
     * 
     * @param policyService
     *            policy control service.
     */
    @Autowired
    public void setPolicyService(PolicyControlService policyService) {
        RestResponseUtil.policyService = policyService;
    }
}
