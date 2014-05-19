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
package cn.cnnic.rdap.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.common.util.AutnumValidator;
import cn.cnnic.rdap.common.util.RestResponseUtil;
import cn.cnnic.rdap.controller.support.QueryParser;
import cn.cnnic.rdap.service.QueryService;
import cn.cnnic.rdap.service.RdapConformanceService;

/**
 * controller for query and search.All methods return message in JSON format.
 * 
 * @author jiashuo
 * 
 */
@RestController
@RequestMapping("/{dot}well-known/rdap")
public class RdapController {
    /**
     * query service
     */
    @Autowired
    private QueryService queryService;

    @Autowired
    private QueryParser queryParser;

    @Autowired
    private RdapConformanceService rdapConformanceService;

    @RequestMapping(value = "/autnum/{autnum}", method = RequestMethod.GET)
    public ResponseEntity queryAs(@PathVariable String autnum,
            HttpServletRequest request, HttpServletResponse response) {
        if (!AutnumValidator.isValidAutnum(autnum)) {
            return RestResponseUtil.createResponse400();
        }
        Autnum result = queryService.queryAutnum(queryParser
                .parseQueryParam(autnum));
        if (null != result) {
            rdapConformanceService.setRdapConformance(result);
            return RestResponseUtil.createResponse200(result);
        }
        return RestResponseUtil.createResponse404();
    }

    /**
     * query domain by domain name
     * 
     * @param domainName
     *            domain name
     * @param response
     *            servlet response
     * @return JSON formated result,with HTTP code
     */
    @RequestMapping(value = "/domain/{domainName}")
    public ResponseEntity<Domain> queryDomain(@PathVariable String domainName,
            HttpServletResponse response) {
        Domain domain = queryService.queryDomain(new QueryParam(domainName));
        return RestResponseUtil.createResponse200(domain);
    }

    /**
     * other invalid query uri will response 400 error.
     */
    @RequestMapping(value = "/**")
    public ResponseEntity error400(HttpServletRequest request,
            HttpServletResponse response) {
        return RestResponseUtil.createResponse400();
    }
}
