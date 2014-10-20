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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.util.AutnumValidator;
import org.restfulwhois.rdap.core.common.util.IpUtil;
import org.restfulwhois.rdap.core.common.util.RestResponseUtil;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.restfulwhois.rdap.core.common.util.IpUtil.IpVersion;
import org.restfulwhois.rdap.core.exception.DecodeException;
import org.restfulwhois.rdap.core.model.Autnum;
import org.restfulwhois.rdap.core.model.Network;
import org.restfulwhois.rdap.core.model.RedirectResponse;
import org.restfulwhois.rdap.core.queryparam.QueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller for RIR.
 * 
 * @author jiashuo
 * 
 */
@Controller
public class RirController extends BaseController {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RirController.class);
    /**
     * autnum query URI.
     */
    private static final String SERVICE_URI_AS_Q = "/autnum/";
    /**
     * ip query URI.
     */
    private static final String SERVICE_URI_IP_Q = "/ip/";

    /**
     * <pre>
     * query autnum.
     * URI:/autnum/{autnum}
     * 
     * First query autnum in local registry, if not exist, then query 
     * for redirect.
     *   
     * This service is under permission control, @see AccessControlManager.
     * This service is under policy control, @see PolicyControlService.
     * 
     * </pre>
     * 
     * @param autnum
     *            an AS Plain autonomous system number [RFC5396].
     * @param request
     *            HttpServletRequest.
     * @param response
     *            HttpServletResponse
     * @return JSON formated result,with HTTP code.
     * @throws DecodeException
     *             DecodeException.
     */
    @RequestMapping(value = "/autnum/{autnum}", method = RequestMethod.GET)
    public ResponseEntity queryAs(@PathVariable String autnum,
            HttpServletRequest request, HttpServletResponse response)
            throws DecodeException {
        LOGGER.debug("query autnum:" + autnum);
        autnum = queryParser.getLastSplitInURI(request);
        if (!AutnumValidator.isValidAutnum(autnum)) {
            return RestResponseUtil.createResponse400();
        }
        QueryParam queryParam = queryParser.parseQueryParam(autnum);
        Autnum result = queryService.queryAutnum(queryParam);
        if (null != result) {
            if (!accessControlManager.hasPermission(result)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(result);
            return RestResponseUtil.createResponse200(result);
        }
        LOGGER.debug("query redirect autnum :{}", queryParam);
        RedirectResponse redirect = redirectService.queryAutnum(queryParam);
        if (redirectService.isValidRedirect(redirect)) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(autnum,
                            SERVICE_URI_AS_Q, redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        return RestResponseUtil.createResponse404();
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
        ipAddr = queryParser.getLastSecondSplitInURI(request);
        mask = queryParser.getLastSplitInURI(request);
        return doQueryIp(ipAddr + "/" + mask);
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
        ipAddr = queryParser.getLastSplitInURI(request);
        return doQueryIp(ipAddr);
    }

    /**
     * do query IP.
     * 
     * @param cidr
     *            CIDR.
     * @param originQueryParam
     *            originQueryParam.
     * @return ResponseEntity ResponseEntity.
     */
    private ResponseEntity doQueryIp(String cidr) {
        if (StringUtils.isBlank(cidr)) {
            return RestResponseUtil.createResponse400();
        }
        cidr = IpUtil.addNetworkMaskIfNotContainsMask(cidr);
        IpVersion ipVersion = IpUtil.getIpVersionOfNetwork(cidr);
        if (ipVersion.isNotValidIp()) {
            return RestResponseUtil.createResponse400();
        }
        QueryParam queryParam = queryParser.parseIpQueryParam(cidr, ipVersion);
        Network ip = queryService.queryIp(queryParam);
        if (null != ip) {
            if (!accessControlManager.hasPermission(ip)) {
                return RestResponseUtil.createResponse403();
            }
            responseDecorator.decorateResponse(ip);
            return RestResponseUtil.createResponse200(ip);
        }
        LOGGER.debug("query redirect network :{}", queryParam);
        RedirectResponse redirect = redirectService.queryIp(queryParam);
        if (redirectService.isValidRedirect(redirect)) {
            String redirectUrl =
                    StringUtil.generateEncodedRedirectURL(cidr,
                            SERVICE_URI_IP_Q, redirect.getUrl());
            return RestResponseUtil.createResponse301(redirectUrl);
        }
        LOGGER.debug("   redirect network not found:{}", queryParam);
        return RestResponseUtil.createResponse404();
    }
}
