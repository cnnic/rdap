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
package org.restfulwhois.rdap.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.bean.QueryParam;
import org.restfulwhois.rdap.bean.RedirectResponse;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.common.RdapProperties;
import org.restfulwhois.rdap.dao.RedirectDao;
import org.restfulwhois.rdap.service.RedirectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * redirect service implementation.
 * 
 * Requirement from http://tools.ietf.org/html/draft-ietf-weirds-redirects-04.
 * 
 * get redirect url for domain/IP/AS .
 * 
 * @author jiashuo
 * 
 */
@Service
public class RedirectServiceImpl implements RedirectService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RedirectServiceImpl.class);

    /**
     * domain redirect DAO.
     */
    @Autowired
    @Qualifier("domainRedirectDao")
    private RedirectDao domainRedirectDao;
    /**
     * autnum redirect DAO.
     */
    @Autowired
    @Qualifier("autnumRedirectDao")
    private RedirectDao autnumRedirectDao;
    /**
     * network redirect DAO.
     */
    @Autowired
    @Qualifier("networkRedirectDao")
    private RedirectDao networkRedirectDao;

    /**
     * query domain redirect.
     * 
     * @param queryParam
     *            param for domain RedirectResponse.
     * @return redirect response.
     */
    @Override
    public RedirectResponse queryDomain(QueryParam queryParam) {
        LOGGER.debug("queryDomain:" + queryParam);
        return domainRedirectDao.query(queryParam);
    }

    /**
     * query AS redirect.
     * 
     * @param queryParam
     *            param for AS RedirectResponse.
     * @return redirect response.
     */
    @Override
    public RedirectResponse queryAutnum(QueryParam queryParam) {
        LOGGER.debug("queryAutnum:" + queryParam);
        return autnumRedirectDao.query(queryParam);
    }

    /**
     * query ip redirect.
     * 
     * @param queryParam
     *            param for IP RedirectResponse.
     * @return redirect response.
     */
    @Override
    public RedirectResponse queryIp(QueryParam queryParam) {
        LOGGER.debug("queryIp:" + queryParam);
        return networkRedirectDao.query(queryParam);
    }

    /**
     * is a redirect response valid.
     * 
     * @param redirect
     *            param for RedirectResponse.
     * @return boolean.
     */
    @Override
    public boolean isValidRedirect(RedirectResponse redirect) {
        if (null == redirect || StringUtils.isBlank(redirect.getUrl())) {
            return false;
        }
        if (redirect.getUrl().contains(RdapProperties.getLocalServiceUrl())) {
            LOGGER.debug("redirect url: {} is local RDAP,ignore.",
                    redirect.getUrl());
            return false;
        }
        LOGGER.debug("redirect url {} can be redirected.", redirect.getUrl());
        return true;
    }

    @Override
    public void saveDomainRedirect(List<Redirect> bootstraps) {
        domainRedirectDao.save(bootstraps);
    }

    @Override
    public void saveNetworkRedirect(List<Redirect> bootstraps) {
        networkRedirectDao.save(bootstraps);
    }

    @Override
    public void saveAutnumRedirect(List<Redirect> bootstraps) {
        autnumRedirectDao.save(bootstraps);
    }

}
