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
package org.restfulwhois.rdap.core.common.support;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.util.RequestUtil;
import org.restfulwhois.rdap.core.common.validation.ValidationResult;
import org.restfulwhois.rdap.core.common.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the base query parameter bean.
 * <p>
 * sub class should be used for query service.
 * </p>
 * 
 * @author jiashuo
 * 
 */
public abstract class QueryParam {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueryParam.class);

    /**
     * used to fill HTTP parameters into this QueryParam.
     */
    private HttpServletRequest request;

    /**
     * validate query parameters.
     * <p>
     * MUST initialize this when create QueryParam.
     * </p>
     */
    private List<Validator> validators = new ArrayList<Validator>();

    /**
     * original query parameter, and never changed after assigned.
     */
    private String originalQ;

    /**
     * 'q' is query parameter used for query. It is assigned by convertParam().
     */
    private String q;

    /**
     * for search.
     */
    private PageBean pageBean;
    
    /**
     * getQueryUri.
     * @return QueryUri.
     */
    abstract public QueryUri getQueryUri(); 

    /**
     * initialize validators.
     * <p>
     * this method MUST be called when QueryParam initialized.
     * </p>
     */
    abstract protected void initValidators();

    /**
     * add validator.
     * 
     * @param validator
     *            validator.
     */
    protected void addValidator(Validator validator) {
        validators.add(validator);
    }

    /**
     * fill parameters from HTTP request into QueryParam's q.
     * 
     * @throws Exception
     *             Exception.
     */
    abstract public void fillParam() throws Exception;

    /**
     * convert parameters from HTTP request.
     * 
     * @throws Exception
     *             Exception.
     */
    abstract public void convertParam() throws Exception;

    /**
     * construction.
     * 
     * @param request
     *            request.
     */
    public QueryParam(HttpServletRequest request) {
        super();
        this.request = request;
        initValidators();
    }

    /**
     * get originalQ.
     * 
     * @return originalQ.
     */
    public String getOriginalQ() {
        return originalQ;
    }

    /**
     * set originalQ.
     * 
     * @param originalQ
     *            originalQ.
     */
    public void setOriginalQ(String originalQ) {
        this.originalQ = originalQ;
    }

    /**
     * get q.
     * 
     * @return q.
     */
    public String getQ() {
        return q;
    }

    /**
     * set q.
     * 
     * @param q
     *            q.
     */
    public void setQ(String q) {
        this.q = q;
    }

    /**
     * default construction.
     * 
     * @param q
     *            query parameter.
     */
    public QueryParam(String q) {
        super();
        this.q = q;
        initValidators();
    }

    /**
     * construction.
     */
    public QueryParam() {
        initValidators();
    }

    /**
     * get pageBean.
     * 
     * @return pageBean.
     */
    public PageBean getPageBean() {
        return pageBean;
    }

    /**
     * set pageBean.
     * 
     * @param pageBean
     *            pageBean.
     */
    public void setPageBean(PageBean pageBean) {
        this.pageBean = pageBean;
    }

    /**
     * get request.
     * 
     * @return request.
     */
    public HttpServletRequest getRequest() {
        return request;
    }

    /**
     * set request.
     * 
     * @param request
     *            request.
     */
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public List<Validator> getValidators() {
        return validators;
    }

    /**
     * validate parameters.
     * <p>
     * erros will be filled into validationResult if at least one validator is
     * failed.
     * </p>
     * 
     * @return ValidationResult.
     */
    public ValidationResult validate() {
        ValidationResult validationResult = new ValidationResult();
        List<Validator> validators = getValidators();
        for (Validator validator : validators) {
            if (!validator.supports(getClass())) {
                break;
            }
            validator.validate(this, validationResult);
            if (validationResult.hasError()) {
                break;
            }
        }
        return validationResult;
    }

    /**
     * get parameter from request,get first if has more than one param.
     * 
     * @param request
     *            HttpServletRequest.
     * @param strParamOrg
     *            the final String array for url params.
     * @return first right url param.
     */
    protected static String getFirstParameter(HttpServletRequest request,
            final String[] strParamOrg) {
        return RequestUtil.getFirstParameter(request, strParamOrg);
    }

    /**
     * getLastSplitInURI.
     * 
     * @param request
     *            request.
     * @return string.
     * @throws DecodeException
     *             DecodeException.
     */
    protected String getLastSplitInURI(HttpServletRequest request)
            throws DecodeException {
        return RequestUtil.getLastSplitInURI(request);
    }

    /**
     * get last second split in URI.
     * 
     * @param request
     *            request.
     * @return param value.
     */
    protected String getLastSecondSplitInURI(HttpServletRequest request) {
        return RequestUtil.getLastSecondSplitInURI(request);
    }

    /**
     * get parameter from request,get first if has more than one value.
     * 
     * @param request
     *            request.
     * @param name
     *            parameter name.
     * @return parameter value.
     */
    protected String getParameter(HttpServletRequest request, String name) {
        return RequestUtil.getParameter(request, name);
    }
    
    /**
     * get remote query ip. 
     *            
     * @return string  remote query ip.
     */
    public String getRemoteAddr() {
       if (this.request != null) {
           return request.getRemoteAddr();
       }
       return "";
    } 


}
