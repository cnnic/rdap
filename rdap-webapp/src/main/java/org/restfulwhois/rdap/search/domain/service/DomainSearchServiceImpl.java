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
package org.restfulwhois.rdap.search.domain.service;

import org.restfulwhois.rdap.core.common.dao.SearchDao;
import org.restfulwhois.rdap.core.common.model.base.BaseSearchModel;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.domain.model.Domain;
import org.restfulwhois.rdap.core.domain.model.DomainSearch;
import org.restfulwhois.rdap.core.domain.service.DomainSearchService;
import org.restfulwhois.rdap.search.common.service.AbstractSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * search service implementation.
 * 
 * RdapController's searching for domain/NS/IP/entity .etc .
 * 
 * The result list is paged by 'batchsizeSearch' property.
 * 
 * @author jiashuo
 * 
 */
@Service
public class DomainSearchServiceImpl extends AbstractSearchService implements
        DomainSearchService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainSearchServiceImpl.class);

    @Autowired
    private SearchDao<Domain> searchDao;
    
    /**
     * search domain.
     * 
     * @param queryParam
     *            param for domain.
     * @return domain search result.
     */
    @Override
    public DomainSearch searchDomain(QueryParam queryParam) {
        LOGGER.debug("searchDomain QueryParam:" + queryParam);
        BaseSearchModel<Domain> searchResult =
                this.search(queryParam, searchDao);
        LOGGER.debug("searchDomain searchResult:" + searchResult);
        if (null == searchResult) {
            return null;
        }
        DomainSearch domainSearch = new DomainSearch();
        BeanUtils.copyProperties(searchResult, domainSearch);
        domainSearch.setDomainSearchResults(searchResult.getSearchResults());
        LOGGER.debug("searchDomain domainSearch:" + domainSearch);
        return domainSearch;
    }

}
