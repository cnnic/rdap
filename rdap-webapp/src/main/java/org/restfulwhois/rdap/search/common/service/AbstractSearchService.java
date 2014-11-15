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
package org.restfulwhois.rdap.search.common.service;

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.core.common.dao.SearchDao;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.BaseSearchModel;
import org.restfulwhois.rdap.core.common.service.AccessControlManager;
import org.restfulwhois.rdap.core.common.support.PageBean;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo.TruncateReason;
import org.restfulwhois.rdap.core.common.util.RdapProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * abstract search service.
 * 
 * searching for domain/NS/entity.
 * 
 * @author jiashuo
 * 
 */
@Service
abstract public class AbstractSearchService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractSearchService.class);
    /**
     * access control manager.
     */
    @Autowired
    private AccessControlManager accessControlManager;

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
    protected <T extends BaseModel> BaseSearchModel<T> search(
            QueryParam queryParam, SearchDao<T> searchDao) {

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

}
