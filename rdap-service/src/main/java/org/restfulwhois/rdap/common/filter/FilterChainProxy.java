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

package org.restfulwhois.rdap.common.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.restfulwhois.rdap.filters.httpFilter.AuthenticationFilter;
import org.restfulwhois.rdap.filters.httpFilter.AuthenticationForUpdateApiFilter;
import org.restfulwhois.rdap.filters.httpFilter.ConcurrentQueryCountFilter;
import org.restfulwhois.rdap.filters.httpFilter.DecodeUriForSpringFilter;
import org.restfulwhois.rdap.filters.httpFilter.HttpRequestFilter;
import org.restfulwhois.rdap.filters.httpFilter.InvalidUriFilter;
import org.restfulwhois.rdap.filters.httpFilter.NotImplementedUriFilter;
import org.restfulwhois.rdap.filters.httpFilter.RateLimitFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The FilterChainProxy is used to do filter for all {@link HttpFilter}.
 * <p>
 * All RDAP filters must be initialized in static block below. And filters list
 * are ordered.
 * <p>
 * It will stop execute filter chain whenever preProcess method or postProcess
 * method in RDAP filter returns false, or throws exception.
 * <p>
 * Concurrent query count can't be used as RDAP filter, for it maintains a
 * global counter, which must be increased and decreased for each request.
 * 
 * @author jiashuo
 * 
 */
public class FilterChainProxy implements Filter {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FilterChainProxy.class);

    /**
     * all filters.
     */
    private static List<HttpFilter> filters;

    /**
     * init filters when class loading.
     */
    static {
        LOGGER.debug("init RDAP filters ...");
        filters = new ArrayList<HttpFilter>();
        filters.add(new ConcurrentQueryCountFilter());
        filters.add(new AuthenticationFilter());
        filters.add(new AuthenticationForUpdateApiFilter());
        filters.add(new RateLimitFilter());
        filters.add(new HttpRequestFilter());
        filters.add(new DecodeUriForSpringFilter());
        filters.add(new NotImplementedUriFilter());
        filters.add(new InvalidUriFilter());
        LOGGER.debug("init RDAP filters end.");
    }

    /**
     * destroy.
     */
    @Override
    public void destroy() {
    }

    /**
     * filter chain .
     * <p>
     * This method call preProcess method for each RDAP filter in filters,
     * before call service method, and then call postProcess for each filters.
     * Filter chain will stop whenever these methods fail.
     * 
     * @param req
     *            request.
     * @param res
     *            response.
     * @param chain
     *            filter chain.
     * @throws IOException
     *             Exception.
     * @throws ServletException
     *             Exception.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
            FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("begin pre filter ...");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        try {
            boolean success = safePreProcess(request, response);
            if (!success) {
                LOGGER.error("some pre filter failed, not process service.");
                return;
            }
            safeDoService(chain, request, response);
            LOGGER.debug("begin post filter ...");
        } catch (Exception e) {
            LOGGER.error("safeDoService error:", e);
        } finally {
            boolean success = safePostProcess(request, response);
            LOGGER.debug("end post filter, are all success?:{}", success);
        }
    }

    /**
     * safe do service.
     * 
     * @param chain
     *            chain.
     * @param request
     *            request.
     * @param response
     *            response.
     */
    private void safeDoService(FilterChain chain, HttpServletRequest request,
            HttpServletResponse response) {
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            LOGGER.error("chain.doFilter error:{}" + e.getMessage());
        }
    }

    /**
     * safe do post process.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    private boolean safePostProcess(HttpServletRequest request,
            HttpServletResponse response) {
        for (HttpFilter filter : filters) {
            if (!filter.needFilter(request, response)) {
                continue;
            }
            LOGGER.debug("call postProcess for:{}", filter.getName());
            try {
                if (!filter.postProcess(request, response)) {
                    LOGGER.error("  postProcess error:{}", filter.getName());
                    return false;
                }
            } catch (Exception e) {
                LOGGER.error("error:{}" + e.getMessage());
            }
        }
        return true;
    }

    /**
     * safe do pre process.
     * 
     * @param request
     *            request.
     * @param response
     *            response.
     * @return true if success processed,and can do service operation; false if
     *         not.
     */
    private boolean safePreProcess(HttpServletRequest request,
            HttpServletResponse response) {
        for (HttpFilter filter : filters) {
            if (!filter.needFilter(request, response)) {
                continue;
            }
            LOGGER.debug("call preProcess for:{}", filter.getName());
            try {
                if (!filter.preProcess(request, response)) {
                    LOGGER.error("  preProcess error:{}", filter.getName());
                    return false;
                }
            } catch (Exception e) {
                LOGGER.error("error:{}" + e.getMessage());
            }
        }
        return true;
    }

    /**
     * initialize.
     * 
     * @param config
     *            FilterConfig.
     * @throws ServletException
     *             exception.
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
    }

}
