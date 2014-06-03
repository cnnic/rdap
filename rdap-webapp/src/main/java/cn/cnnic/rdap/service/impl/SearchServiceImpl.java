/* Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and Numbers 
 * (ICANN) and China Internet Network Information Center (CNNIC)
 *
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 **Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer.
 * 
 **Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 **Neither the name of the ICANN, CNNIC nor the names of its contributors may be used to 
 * endorse or promote products derived from this software without specific prior written 
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package cn.cnnic.rdap.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.DomainSearch;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.NameserverSearch;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.common.RdapProperties;
import cn.cnnic.rdap.dao.impl.DomainQueryDaoImpl;
import cn.cnnic.rdap.dao.impl.NameserverQueryDaoImpl;
import cn.cnnic.rdap.service.SearchService;

/**
 * search service implementation.
 * 
 * @author jiashuo
 * 
 */
@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private DomainQueryDaoImpl domainDao;

    @Autowired
    private NameserverQueryDaoImpl nameserverDao;

    @Override
    public DomainSearch searchDomain(QueryParam queryParam) {
        Long totalCount = domainDao.searchCount(queryParam);
        if (totalCount == 0) {
            return null;
        }
        DomainSearch domainSearch = new DomainSearch();
        if (totalCount > RdapProperties.getMaxsizeSearch()) {
            domainSearch.setResultsTruncated(true);
        }
        List<Domain> domans = domainDao.search(queryParam);
        domainSearch.setDomainSearchResults(domans);
        return domainSearch;
    }

    @Override
    public NameserverSearch searchNameserver(QueryParam queryParam) {
        Long totalCount = nameserverDao.searchCount(queryParam);
        if (totalCount == 0) {
            return null;
        }
        NameserverSearch nsSearch = new NameserverSearch();
        if (totalCount > RdapProperties.getMaxsizeSearch()) {
            nsSearch.setResultsTruncated(true);
        }
        List<Nameserver> listNS = nameserverDao.search(queryParam);
        nsSearch.setNameserverSearchResults(listNS);
        return nsSearch;
    }
}
