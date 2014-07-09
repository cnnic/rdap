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
package cn.cnnic.rdap.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.dao.NoticeDao;
import cn.cnnic.rdap.service.PolicyControlService;
import cn.cnnic.rdap.service.RdapConformanceService;

/**
 * decorate response.
 * 
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
        LOGGER.info("initRdapConformance begin.");
        rdapConformanceService.initRdapConformance();
        LOGGER.info("initRdapConformance end.");
    }

    /**
     * decorate response: add properties to response.
     * 
     * @param model
     *            response
     */
    public void decorateResponse(BaseModel model) {
        LOGGER.info("decorateResponse:" + model);
        addRdapConformance(model);
        addNotices(model);
        policyControlService.applyPolicy(model);
        LOGGER.info("decorateResponse end");
    }
    /**
     * add notices to model.
     * 
     * @param model
     *            model.
     */
    private void addNotices(BaseModel model) {
        LOGGER.info("addNotices:" + model);
        if (null == model) {
            return;
        }
        model.setNotices(noticeDao.getAllNotices());
        LOGGER.info("addNotices end");
    }
    /**
     * decorate response: add properties to help response.
     * 
     * @param model
     *            response
     */
    public void decorateResponseForHelp(BaseModel model) {
        LOGGER.info("decorateResponseForHelp:" + model);
        addRdapConformance(model);
        addHelp(model);
        
        LOGGER.info("decorateResponseForHelp end");
    }
    /**
     * add help to model.
     * 
     * @param model
     *            model.
     */
    private void addHelp(BaseModel model) {
        LOGGER.info("addHelp:" + model);
        if (null == model) {
            return;
        }
        model.setNotices(noticeDao.getHelp());
        LOGGER.info("addHelp end");
    }
    /**
     * add rdapConformance to model.
     * 
     * @param model
     *            model.
     */
    private void addRdapConformance(BaseModel model) {
        LOGGER.info("addRdapConformance begin.");
        rdapConformanceService.setRdapConformance(model);
        LOGGER.info("addRdapConformance end.");
    }
}
