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
package org.restfulwhois.rdap.common.controller;

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.common.filter.QueryFilter;
import org.restfulwhois.rdap.common.filter.QueryFilterManager;
import org.restfulwhois.rdap.common.filter.QueryFilterResult;
import org.restfulwhois.rdap.common.support.MappingExceptionResolver;
import org.restfulwhois.rdap.common.support.PrincipalHolder;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.support.RestResponse;
import org.restfulwhois.rdap.common.validation.QueryValidationError;
import org.restfulwhois.rdap.common.validation.ValidationError;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * BaseController is base class for all controllers.
 * <p>
 * 
 * <pre>
 * Sub classes of BaseController attempt to accept request,and then 
 * query objects.
 * Please see draft-ietf-weirds-rdap-query.
 * </pre>
 * <p>
 * Redirect service is called in this class.
 * <p>
 * Access control is checked before return response to client.
 * <p>
 * Some columns can not be shown for Policy reason, and this is checked before
 * return response to client.
 * <p>
 * 
 * <pre>
 * This class is used as 'controller' in MVC, and modified by
 * {@link org.springframework.stereotype.Controller}, so this class MUST under
 * spring {@link org.springframework.context.annotation.ComponentScan}. 
 * The spring dispatcher scans such annotated classes for mapped methods and
 * detects @RequestMapping annotations.
 * 
 * </pre>
 * 
 * Request:
 * 
 * <pre>
 *      Only support HTTP 'GET' method.
 *      'Accept' in HTTP header MUST be 'application/rdap+json'.
 *      URI and parameters MUST be encoded in UTF-8.
 *      Query data in database MUST be NFKC and case-folded.
 *      Unknown parameters will be ignored.
 * </pre>
 * 
 * All response is in JSON format.
 * 
 * Response code:
 * 
 * <pre>
 *      200:exist for query, or no error for search.
 *      400:parameter is invalid, or URI can't be handled.
 *      401:unauthorized.
 *      403:forbidden.
 *      404:not found for query.
 *      405:method not allowed. Only support GET method.
 *      415:unsupported media type. Only 'application/rdap+json' is supported.
 *      422:unprocessable search parameter.
 *      429:too many requests.Client should reduce request Frequency.
 *      500:internal server error.
 *      501:not Implemented query.
 *      509:bandwidth limit exceed.
 * </pre>
 * 
 * <p>
 * Exception:
 * 
 * <pre>
 *      1.Service Exception is handled in each methods, returning Corresponding
 *        HTTP error code; 
 *      2.Unchecked Exception is handled in {@link MappingExceptionResolver},so
 *        'exceptionResolver' with MappingExceptionResolver MUST be configured 
 *        in spring configuration file, and now this configuration is in 
 *        spring-servlet.xml;
 * </pre>
 * 
 * @author jiashuo
 * 
 */
@Controller
public class BaseController {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BaseController.class);

    /**
     * queryFilterManager.
     */
    @Autowired
    private QueryFilterManager queryFilterManager;

    /**
     * Query method and write log.
     * 
     * <pre>
     * All sub classes should call this method for query.
     * </pre>
     * 
     * @param queryParam
     *            * queryParam.
     * @return ResponseEntity.
     */
    @SuppressWarnings("rawtypes")
    protected ResponseEntity query(QueryParam queryParam) {
        if (queryParam == null) {
            return RestResponse.createResponse400();
        }
        long queryStart = System.currentTimeMillis();
        ResponseEntity responseEntity = queryTemplate(queryParam);
        LOGGER.info("query ip:{};query user:{}; query object and param:{}."
                + queryParam, queryParam.getRemoteAddr(), PrincipalHolder
                .getPrincipal().getId());
        long usedTime = System.currentTimeMillis() - queryStart;
        LOGGER.info("query used time:{}ms;responseCode:{}.", usedTime,
                responseEntity.getStatusCode());
        return responseEntity;
    }

    /**
     * Query template method.
     * 
     * <pre>
     * All sub classes should call this method for query.
     * 
     * Note:queryParam.convertParam()  and queryParam.convertParam() 
     * can throw any Exception, and these exceptions will handled 
     * as HTTP 400 error.
     * </pre>
     * 
     * @param queryParam
     *            queryParam.
     * @return ResponseEntity.
     */
    @SuppressWarnings("rawtypes")
    protected ResponseEntity queryTemplate(QueryParam queryParam) {
        try {
            queryParam.fillParam();
        } catch (Exception e) {
            LOGGER.warn("fillParam error:{}", e);
            return RestResponse.createResponse400();
        }
        QueryFilterResult preParamFilterResult =
                queryFilterManager.preParamValidate(queryParam,
                        getQueryFilters());
        if (null != preParamFilterResult && preParamFilterResult.hasResult()) {
            return preParamFilterResult.getResult();
        }
        ValidationResult validateResult = validateParam(queryParam);
        if (validateResult.hasError()) {
            return handleError(validateResult);
        }
        QueryFilterResult postParamFilterResult =
                queryFilterManager.postParamValidate(queryParam,
                        getQueryFilters());
        if (null != postParamFilterResult && postParamFilterResult.hasResult()) {
            return postParamFilterResult.getResult();
        }
        try {
            queryParam.convertParam();
        } catch (Exception e) {
            LOGGER.warn("convertParam error:{}", e);
            return RestResponse.createResponse400();
        }
        ResponseEntity result = doQuery(queryParam);
        QueryFilterResult postQueryResult =
                queryFilterManager.postQuery(queryParam, result,
                        getQueryFilters());
        if (null != postQueryResult && postQueryResult.hasResult()) {
            return postQueryResult.getResult();
        }
        return result;
    }

    /**
     * handle error in validation result.
     * 
     * <pre>
     * unrecognized error will be handled as HTTP 400 error.
     * </pre>
     * 
     * @param result
     *            result.
     * @return ResponseEntity.
     */
    @SuppressWarnings("rawtypes")
    private ResponseEntity handleError(ValidationResult result) {
        ValidationError error = result.getFirstError();
        if (null != error) {
            QueryValidationError httpError = (QueryValidationError) error;
            return RestResponse.createCommonErrorResponse(httpError
                    .getStatusCode());
        }
        LOGGER.warn(
                "can't found error when handleError,for ValidationResult:{}",
                result);
        return RestResponse.createResponse400();
    }

    /**
     * do query.
     * <p>
     * Sub concrete classes MUST implement this method.
     * </p>
     * 
     * @param queryParam
     *            queryParam.
     * @return ResponseEntity.
     */
    @SuppressWarnings("rawtypes")
    protected ResponseEntity doQuery(QueryParam queryParam) {
        throw new UnsupportedOperationException();
    }

    /**
     * validate queryParam.
     * 
     * @param queryParam
     *            queryParam.
     * @return ValidationResult.
     */
    protected ValidationResult validateParam(QueryParam queryParam) {
        ValidationResult validationResult = queryParam.validate();
        return validationResult;
    }

    /**
     * get query filters.
     * 
     * @return service filters.
     */
    protected List<QueryFilter> getQueryFilters() {
        LOGGER.warn("MAYBE you have forgot to initialize queryFilters!");
        return new ArrayList<QueryFilter>();
    }

    /**
     * get queryFilterManager.
     * 
     * @return queryFilterManager.
     */
    public QueryFilterManager getQueryFilterManager() {
        return queryFilterManager;
    }

}
