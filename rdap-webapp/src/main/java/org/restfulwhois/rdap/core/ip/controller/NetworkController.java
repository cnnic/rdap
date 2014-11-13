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
package org.restfulwhois.rdap.core.ip.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.restfulwhois.rdap.core.common.controller.BaseController;
import org.restfulwhois.rdap.core.common.exception.DecodeException;
import org.restfulwhois.rdap.core.common.filter.QueryFilter;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.support.QueryUri;
import org.restfulwhois.rdap.core.common.util.IpUtil;
import org.restfulwhois.rdap.core.common.util.RequestUtil;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.restfulwhois.rdap.core.ip.model.Network;
import org.restfulwhois.rdap.core.ip.queryparam.NetworkQueryParam;
import org.restfulwhois.rdap.core.ip.service.IpService;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for network query.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class NetworkController extends BaseController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NetworkController.class);
    /**
     * query service.
     */
    @Autowired
    protected IpService queryService;

    @Resource(name = "commonServiceFilters")
    private List<QueryFilter> serviceFilters;

    @Override
    protected List<QueryFilter> getQueryFilters() {
        return serviceFilters;
    }
    
    /**
     * <pre>
     * query ip by ip and mask.
     * URI:/ip/{ipAddr}/{mask} 
     * 
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param ipAddr
     *            the query ip
     * @param mask
     *            the ip mask
     * @param request
     *            request.
     * @return JSON formatted result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = { "/ip/{ipAddr}/{mask}" },
            method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryIpWithMask(@PathVariable String ipAddr,
            @PathVariable String mask, HttpServletRequest request)
            throws DecodeException {
        ipAddr = RequestUtil.getLastSecondSplitInURI(request);
        mask = RequestUtil.getLastSplitInURI(request);
        return doQueryIp(request, ipAddr + "/" + mask);
    }

    /**
     * <pre>
     * query ip by ip address.
     * URI:/ip/{ipAddr} 
     * 
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param ipAddr
     *            the query ip
     * @param request
     *            request.
     * @return ResponseEntity ResponseEntity.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = { "/ip/{ipAddr}" }, method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity queryIp(@PathVariable String ipAddr,
            HttpServletRequest request) throws DecodeException {
        ipAddr = RequestUtil.getLastSplitInURI(request);
        return doQueryIp(request, ipAddr);
    }

    /**
     * do query IP.
     * 
     * @param request
     *            request.
     * 
     * @param cidr
     *            CIDR.
     * @param originQueryParam
     *            originQueryParam.
     * @return ResponseEntity ResponseEntity.
     */
    private ResponseEntity doQueryIp(HttpServletRequest request, String cidr) {
        cidr = IpUtil.addNetworkMaskIfNotContainsMask(cidr);
        NetworkQueryParam queryParam = new NetworkQueryParam(request, cidr);
        return super.query(queryParam);
    }

    @Override
    protected ResponseEntity doQuery(QueryParam queryParam) {
        Network ip = queryService.queryIp(queryParam);
        if (null != ip) {
            return RestResponseUtil.createResponse200(ip);
        }
        LOGGER.debug("query redirect network :{}", queryParam);
        RedirectResponse redirect = redirectService.queryIp(queryParam);
        if (redirectService.isValidRedirect(redirect)) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(
                            queryParam.getOriginalQ(), QueryUri.IP.getName(),
                            redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        LOGGER.debug("   redirect network not found:{}", queryParam);
        return RestResponseUtil.createResponse404();
    }

}
