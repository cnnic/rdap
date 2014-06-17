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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnnic.rdap.bean.BaseModel;
import cn.cnnic.rdap.bean.BaseSearchModel;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.DomainSearch;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntitySearch;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.NameserverSearch;
import cn.cnnic.rdap.bean.PageBean;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.common.RdapProperties;
import cn.cnnic.rdap.dao.QueryDao;
import cn.cnnic.rdap.service.AccessControlManager;
import cn.cnnic.rdap.service.SearchService;

/**
 * search service implementation.
 * 
 * @author jiashuo
 * 
 */
@Service
public class SearchServiceImpl implements SearchService {
    /**
     * domain dao.
     */
    @Autowired
    private QueryDao<Domain> domainDao;

    /**
     * nameserver dao.
     */
    @Autowired
    private QueryDao<Nameserver> nameserverDao;
    /**
     * access control manager.
     */
    @Autowired
    private AccessControlManager accessControlManager;
    
    /**
     * entity dao.
     */
    @Autowired
    private QueryDao<Entity> entityDao;

    /**
     * common search.
     * 
     * @param queryParam
     *            queryParam.
     * @param queryDao
     *            queryDao.
     * @return BaseSearchModel.
     */
    private <T extends BaseModel> BaseSearchModel<T> search(
            QueryParam queryParam, QueryDao<T> queryDao) {
        Long totalCount = queryDao.searchCount(queryParam);
        if (totalCount == 0) {
            return null;
        }
        List<T> authedObjects = new ArrayList<T>();
        Long totalAuthedObjectSize = 0L;
        PageBean page = new PageBean();
        page.setMaxRecords(RdapProperties.getBatchsizeSearch().intValue());
        page.setRecordsCount(totalCount.intValue());
        queryParam.setPageBean(page);
        boolean gotEnoughResults = false;
        do {
            List<T> objects = queryDao.search(queryParam);
            for (T object : objects) {
                if (authedObjects.size() < RdapProperties.getMaxsizeSearch()
                        && accessControlManager.hasPermission(object)) {
                    authedObjects.add(object);
                }
                if (accessControlManager.hasPermission(object)) {
                    totalAuthedObjectSize++;
                }
                if (authedObjects.size() == RdapProperties.getMaxsizeSearch()
                        && totalAuthedObjectSize > authedObjects.size()) {
                    gotEnoughResults = true;
                    break;
                }
            }
            page.incrementCurrentPage();
        } while (page.isNotLastPage() && !gotEnoughResults
        // && authedDomains.size() < RdapProperties.getMaxsizeSearch()
        );
        BaseSearchModel<T> searchResult = new BaseSearchModel<T>();
        if (totalAuthedObjectSize > authedObjects.size()) {
            searchResult.setResultsTruncated(true);
        }
        if (authedObjects.size() == 0) {
            searchResult.setHasNoAuthForAllObjects(true);
        }
        searchResult.setSearchResults(authedObjects);
        return searchResult;
    }

    @Override
    public DomainSearch searchDomain(QueryParam queryParam) {
        BaseSearchModel<Domain> searchResult = this.search(queryParam,
                domainDao);
        if (null == searchResult) {
            return null;
        }
        DomainSearch domainSearch = new DomainSearch();
        BeanUtils.copyProperties(searchResult, domainSearch);
        domainSearch.setDomainSearchResults(searchResult.getSearchResults());
        return domainSearch;
    }

    @Override
    public NameserverSearch searchNameserver(QueryParam queryParam) {
        BaseSearchModel<Nameserver> searchResult = this.search(queryParam,
                nameserverDao);
        if (null == searchResult) {
            return null;
        }
        NameserverSearch nameserverSearch = new NameserverSearch();
        BeanUtils.copyProperties(searchResult, nameserverSearch);
        nameserverSearch.setNameserverSearchResults(searchResult
                .getSearchResults());
        return nameserverSearch;
    }
    
    @Override
    public EntitySearch searchEntity(QueryParam queryParam) {
        BaseSearchModel<Entity> searchResult = this.search(queryParam,
                entityDao);
        if (null == searchResult) {
            return null;
        }
        EntitySearch entitySearch = new EntitySearch();
        BeanUtils.copyProperties(searchResult, entitySearch);
        entitySearch.setEntitySearchResults(searchResult.getSearchResults());
        return entitySearch;
    }
}
