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
package org.restfulwhois.rdap.core.service.impl;

import org.restfulwhois.rdap.core.bean.BaseModel;
import org.restfulwhois.rdap.core.service.PolicyControlService;
import org.restfulwhois.rdap.core.service.RdapConformanceService;
import org.restfulwhois.rdap.dao.NoticeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * decorate response.
 * 
 * RdapController's query for rdapconformace, notices or helps,
 * 
 * The response should be decorated after query service. 
 *  
 * @see RdapConformanceService 
 * @see PolicyControlService
 * @author jiashuo
 * 
 */
@Service
public class ResponseDecorator {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ResponseDecorator.class);
    /**
     * rdapConformanceService.
     */
    @Autowired
    private RdapConformanceService rdapConformanceService;
    /**
     * noticeDao.
     */
    @Autowired
    private NoticeDao noticeDao;
    /**
     * policy control service.
     */
    @Autowired
    private PolicyControlService policyControlService;
    
    /**
     * init rdapConformance.
     */
    public void initRdapConformance() {
        LOGGER.debug("initRdapConformance begin.");
        rdapConformanceService.initRdapConformance();
        LOGGER.debug("initRdapConformance end.");
    }

    /**
     * decorate response: add properties to response.
     * 
     * @param model
     *            response
     */
    public void decorateResponse(BaseModel model) {
        LOGGER.debug("decorateResponse:" + model);
        addRdapConformance(model);
        addNotices(model);
        policyControlService.applyPolicy(model);
        LOGGER.debug("decorateResponse end");
    }
    /**
     * add notices to model.
     * 
     * @param model
     *            model.
     */
    private void addNotices(BaseModel model) {
        LOGGER.debug("addNotices:" + model);
        if (null == model) {
            return;
        }
        model.setNotices(noticeDao.getAllNotices());
        LOGGER.debug("addNotices end");
    }
    /**
     * decorate response: add properties to help response.
     * 
     * @param model
     *            response
     */
    public void decorateResponseForHelp(BaseModel model) {
        LOGGER.debug("decorateResponseForHelp:" + model);
        addRdapConformance(model);
        addHelp(model);
        
        LOGGER.debug("decorateResponseForHelp end");
    }
    /**
     * add help to model.
     * 
     * @param model
     *            model.
     */
    private void addHelp(BaseModel model) {
        LOGGER.debug("addHelp:" + model);
        if (null == model) {
            return;
        }
        model.setNotices(noticeDao.getHelp());
        LOGGER.debug("addHelp end");
    }
    /**
     * add rdapConformance to model.
     * 
     * @param model
     *            model.
     */
    private void addRdapConformance(BaseModel model) {
        LOGGER.debug("addRdapConformance begin.");
        rdapConformanceService.setRdapConformance(model);
        LOGGER.debug("addRdapConformance end.");
    }
}
