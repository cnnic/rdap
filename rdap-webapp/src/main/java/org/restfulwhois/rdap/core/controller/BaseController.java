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
package org.restfulwhois.rdap.core.controller;

import org.restfulwhois.rdap.core.controller.support.MappingExceptionResolver;
import org.restfulwhois.rdap.core.controller.support.QueryParser;
import org.restfulwhois.rdap.core.queryparam.QueryParam;
import org.restfulwhois.rdap.core.service.AccessControlManager;
import org.restfulwhois.rdap.core.service.QueryService;
import org.restfulwhois.rdap.core.service.SearchService;
import org.restfulwhois.rdap.core.service.impl.ResponseDecorator;
import org.restfulwhois.rdap.redirect.service.RedirectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

/**
 * This is the central class in this package.
 * <p>
 * 
 * <pre>
 * This class accept request,and then query/search/redirect result.
 * Ref <a href= 'http://www.ietf.org/id/draft-ietf-weirds-rdap-query-10.txt'>
 * draft-ietf-weirds-rdap-query</a>.
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
 * This class is use as 'controller' in MVC, and modified by
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
 *      422:unprocessable query parameter for search. See search* method.
 *      429:too many requests.Client should reduce request Frequency.
 *      500:internal server error.
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
     * query service.
     */
    @Autowired
    protected QueryService queryService;
    /**
     * search service.
     */
    @Autowired
    protected SearchService searchService;
    /**
     * query parser.
     */
    @Autowired
    protected QueryParser queryParser;
    /**
     * response decorator.
     */
    @Autowired
    protected ResponseDecorator responseDecorator;

    /**
     * access control manager.
     */
    @Autowired
    protected AccessControlManager accessControlManager;

    /**
     * redirect service.
     */
    @Autowired
    protected RedirectService redirectService;

    protected ResponseEntity query(QueryParam queryParam) {
        boolean result = validate(queryParam);
        if (!result) {
            // handle error and return.
        }
        return doQuery(queryParam);
    }

    protected ResponseEntity doQuery(QueryParam queryParam) {
        throw new UnsupportedOperationException();
    }

    protected boolean validate(QueryParam queryParam) {
        throw new UnsupportedOperationException();
    }

}
