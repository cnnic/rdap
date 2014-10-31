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

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.core.common.dao.QueryDao;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.BaseSearchModel;
import org.restfulwhois.rdap.core.common.support.PageBean;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo.TruncateReason;
import org.restfulwhois.rdap.core.common.util.RdapProperties;
import org.restfulwhois.rdap.core.domain.model.Domain;
import org.restfulwhois.rdap.core.domain.model.DomainSearch;
import org.restfulwhois.rdap.core.entity.model.Entity;
import org.restfulwhois.rdap.core.nameserver.model.Nameserver;
import org.restfulwhois.rdap.core.service.AccessControlManager;
import org.restfulwhois.rdap.core.service.SearchService;
import org.restfulwhois.rdap.search.entity.bean.EntitySearch;
import org.restfulwhois.rdap.search.nameserver.bean.NameserverSearch;
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
public class SearchServiceImpl implements SearchService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SearchServiceImpl.class);
    /**
     * domain dao.
     */
    @Autowired
    private QueryDao<Domain> domainSearchDao;

    /**
     * nameserver dao.
     */
    @Autowired
    private QueryDao<Nameserver> nameserverSearchDao;
    /**
     * access control manager.
     */
    @Autowired
    private AccessControlManager accessControlManager;

    /**
     * entity dao.
     */
    @Autowired
    private QueryDao<Entity> entitySearchDao;

    /**
     * common search.
     * 
     * @param queryParam
     *            queryParam.
     * @param searchDao
     *            searchDao.
     * @param <T>
     *            model in the base search.
     * @return BaseSearchModel.
     */
    private <T extends BaseModel> BaseSearchModel<T> search(
            QueryParam queryParam, QueryDao<T> searchDao) {

        LOGGER.debug("search QueryParam:" + queryParam + ",QueryDao:"
                + searchDao);

        Long totalCount = searchDao.searchCount(queryParam);
        LOGGER.debug("search count is " + totalCount);
        if (totalCount == 0) {
            return null;
        }
        List<T> authedObjects = new ArrayList<T>();
        PageBean page = new PageBean();
        page.setMaxRecords(RdapProperties.getBatchsizeSearch().intValue());
        page.setRecordsCount(totalCount.intValue());
        queryParam.setPageBean(page);
        boolean gotEnoughResults = false;
        TruncatedInfo truncatedInfo = new TruncatedInfo();
        do {
            List<T> objects = searchDao.search(queryParam);
            for (T object : objects) {
                if (authedObjects.size() == RdapProperties.getMaxsizeSearch()) {
                    gotEnoughResults = true;
                    truncatedInfo
                            .addTruncate(TruncateReason.TRUNCATEREASON_EXLOAD);
                    break;
                }
                if (authedObjects.size() < RdapProperties.getMaxsizeSearch()
                        && accessControlManager.hasPermission(object)) {
                    authedObjects.add(object);
                }
                if (!accessControlManager.hasPermission(object)) {
                    truncatedInfo
                            .addTruncate(TruncateReason.TRUNCATEREASON_AUTH);
                }
            }
            page.incrementCurrentPage();
        } while (page.isNotLastPage() && !gotEnoughResults
        // && authedDomains.size() < RdapProperties.getMaxsizeSearch()
        );
        BaseSearchModel<T> searchResult = new BaseSearchModel<T>();
        if (authedObjects.size() == 0) {
            truncatedInfo.setHasNoAuthForAllObjects(true);
        }
        searchResult.setTruncatedInfo(truncatedInfo);
        searchResult.setSearchResults(authedObjects);

        LOGGER.debug("search result " + searchResult);
        return searchResult;
    }

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
                this.search(queryParam, domainSearchDao);
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
                this.search(queryParam, nameserverSearchDao);
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

    /**
     * search entity.
     * 
     * @param queryParam
     *            param for entity.
     * @return entity search result.
     */
    @Override
    public EntitySearch searchEntity(QueryParam queryParam) {
        LOGGER.debug("searchEntity QueryParam:" + queryParam);
        BaseSearchModel<Entity> searchResult =
                this.search(queryParam, entitySearchDao);
        LOGGER.debug("searchEntity searchResult:" + searchResult);
        if (null == searchResult) {
            return null;
        }
        EntitySearch entitySearch = new EntitySearch();
        BeanUtils.copyProperties(searchResult, entitySearch);
        entitySearch.setEntitySearchResults(searchResult.getSearchResults());
        LOGGER.debug("searchEntity entitySearch:" + entitySearch);
        return entitySearch;
    }
}
