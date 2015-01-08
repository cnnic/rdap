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
package org.restfulwhois.rdap.core.domain.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.QueryDao;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.support.RdapProperties;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.core.domain.service.DomainQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * query service implementation.
 * 
 * RdapController's main query service for querying or searching.
 * 
 * Provide the all tlds to be supported
 * 
 * Requirement from http://www.ietf.org/id/draft-ietf-weirds-rdap-query-10.txt.
 * 
 * @author jiashuo
 * 
 */
@Service
public class DomainQueryServiceImpl implements DomainQueryService {

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainQueryServiceImpl.class);

    /**
     * domain DAO.
     */
    @Autowired
    private QueryDao<Domain> domainDao;

    /**
     * query domain by domain queryParam.
     * 
     * @param queryParam
     *            queryParam.
     * @return domain.
     */
    @Override
    public Domain queryDomain(QueryParam queryParam) {
        return domainDao.query(queryParam);
    }

    /**
     * Is query object in out Tld.
     * 
     * @param queryParam
     *            queryParam.
     * @return boolean.
     */
    @Override
    public boolean tldInThisRegistry(QueryParam queryParam) {
        LOGGER.debug("tldInThisRegistry, queryParam:" + queryParam);
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        String fullPunyTld = domainQueryParam.getFullPunyTld();
        List<String> allTlds = getAllTlds(fullPunyTld);
        boolean inNotInTlds =
                CollectionUtils.containsAny(allTlds,
                        RdapProperties.getNotInTldsInThisRegistry());
        if (inNotInTlds) {
            LOGGER.debug("tldInThisRegistry, false");
            return false;
        }
        boolean inThisRegTlds =
                CollectionUtils.containsAny(allTlds,
                        RdapProperties.getInTldsInThisRegistry());
        LOGGER.debug("tldInThisRegistry, return:" + inThisRegTlds);
        return inThisRegTlds;
    }

    /**
     * get all tlds,eg: for "edu.cn", return ["edu.cn","cn"].
     * 
     * @param fullTld
     *            fullTld.
     * @return tld list.
     */
    private List<String> getAllTlds(String fullTld) {
        LOGGER.debug("getAllTlds, fullTld:" + fullTld);
        if (StringUtils.isBlank(fullTld)) {
            return null;
        }
        List<String> tldList = new ArrayList<String>();
        String currentTld = fullTld;
        tldList.add(currentTld);
        while (StringUtils.isNotBlank(currentTld)) {
            String subTld =
                    StringUtils.substringAfter(currentTld,
                            StringUtil.TLD_SPLITOR);
            if (StringUtils.isNotBlank(subTld)) {
                tldList.add(subTld);
            }
            currentTld = subTld;
        }
        LOGGER.debug("getAllTlds, tldList:" + tldList);
        return tldList;
    }

}
