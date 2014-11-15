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
package org.restfulwhois.rdap.search.nameserver.service;

import org.restfulwhois.rdap.core.common.dao.SearchDao;
import org.restfulwhois.rdap.core.common.model.base.BaseSearchModel;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.nameserver.model.Nameserver;
import org.restfulwhois.rdap.core.nameserver.service.NameserverSearchService;
import org.restfulwhois.rdap.search.common.service.AbstractSearchService;
import org.restfulwhois.rdap.search.nameserver.bean.NameserverSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * NS search service implementation.
 * 
 * @author jiashuo
 * 
 */
@Service
public class NameserverSearchServiceImpl extends AbstractSearchService
        implements NameserverSearchService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NameserverSearchServiceImpl.class);

    @Autowired
    private SearchDao<Nameserver> searchDao;

    /**
     * search nameserver.
     * 
     * @param queryParam
     *            param for nameserver.
     * @return nameserver search result.
     */
    @Override
    public NameserverSearch searchNameserver(QueryParam queryParam) {
        LOGGER.debug("searchNameserver QueryParam:" + queryParam);
        BaseSearchModel<Nameserver> searchResult =
                this.search(queryParam, searchDao);
        LOGGER.debug("searchNameserver searchResult:" + searchResult);
        if (null == searchResult) {
            return null;
        }
        NameserverSearch nameserverSearch = new NameserverSearch();
        BeanUtils.copyProperties(searchResult, nameserverSearch);
        nameserverSearch.setNameserverSearchResults(searchResult
                .getSearchResults());
        LOGGER.debug("searchNameserver nameserverSearch:" + nameserverSearch);
        return nameserverSearch;
    }

}
