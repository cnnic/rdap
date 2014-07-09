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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.DomainQueryParam;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Help;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.Network;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.common.RdapProperties;
import cn.cnnic.rdap.common.util.StringUtil;
import cn.cnnic.rdap.dao.NoticeDao;
import cn.cnnic.rdap.dao.QueryDao;
import cn.cnnic.rdap.dao.impl.AutnumQueryDaoImpl;
import cn.cnnic.rdap.dao.impl.DomainQueryDaoImpl;
import cn.cnnic.rdap.dao.impl.NameserverQueryDaoImpl;
import cn.cnnic.rdap.dao.impl.NetworkQueryDaoImpl;
import cn.cnnic.rdap.service.QueryService;
import cn.cnnic.rdap.service.RdapConformanceService;

/**
 * query service implementation.
 * 
 * @author jiashuo
 * 
 */
@Service
public class QueryServiceImpl implements QueryService {
    
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueryServiceImpl.class);
    
    /**
     * rdap conformance service.
     */
    @Autowired
    private RdapConformanceService rdapConformanceService;

    /**
     * domain DAO.
     */
    @Autowired
    private DomainQueryDaoImpl domainDao;

    /**
     * autnum DAO.
     */
    @Autowired
    private AutnumQueryDaoImpl autnumQueryDao;

    /**
     * nameserver DAO.
     */
    @Autowired
    private NameserverQueryDaoImpl nameserverQueryDao;
    
    /**
     * ip DAO.
     */
    @Autowired
    private NetworkQueryDaoImpl ipQueryDao;

    /**
     * Help DAO.
     */
    @Autowired
    private NoticeDao noticeDao;
    
    /**
     * entityQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;

    /**
     * query domain by domain queryParam.
     * @param queryParam
     *            queryParam.
     * @return domain.
     */
    @Override
    public Domain queryDomain(QueryParam queryParam) {
        return domainDao.query(queryParam);
    }
    /**
     * query as by as queryParam.
     * @param queryParam
     *            queryParam.
     * @return as.
     */
    @Override
    public Autnum queryAutnum(QueryParam queryParam) {
        return autnumQueryDao.query(queryParam);
    }
    /**
     * query name server by NS queryParam.
     * @param queryParam
     *            queryParam.
     * @return name server.
     */
    @Override
    public Nameserver queryNameserver(QueryParam queryParam) {
        return nameserverQueryDao.query(queryParam);
    }
    /**
     * query entity by queryParam.
     * @param queryParam
     *            queryParam.
     * @return entity.
     */
    @Override
    public Entity queryEntity(QueryParam queryParam) {
        return entityQueryDao.query(queryParam);
    }
    /**
     * query IP by queryParam.
     * @param queryParam
     *            queryParam.
     * @return ip.
     */
    @Override
    public Network queryIp(QueryParam queryParam) {
        return ipQueryDao.query(queryParam);
    }
    /**
     * query help by queryParam.
     * @param queryParam
     *            queryParam.
     * @return Help.
     */
    @Override
    public Help queryHelp(QueryParam queryParam) {
        // construct a help object and fill it in decorator
        return new Help();
    }
    /**
     * Is query object in out Tld.
     * @param queryParam
     *            queryParam.
     * @return boolean.
     */
    @Override
    public boolean tldInThisRegistry(QueryParam queryParam) {
        LOGGER.info("tldInThisRegistry, queryParam:" + queryParam);
        DomainQueryParam domainQueryParam = (DomainQueryParam) queryParam;
        String fullPunyTld = domainQueryParam.getFullPunyTld();
        List<String> allTlds = getAllTlds(fullPunyTld);
        boolean inNotInTlds =
                CollectionUtils.containsAny(allTlds,
                        RdapProperties.getNotInTldsInThisRegistry());
        if (inNotInTlds) {
            LOGGER.info("tldInThisRegistry, false");
            return false;
        }
        boolean inThisRegTlds =
                CollectionUtils.containsAny(allTlds,
                        RdapProperties.getInTldsInThisRegistry());
        LOGGER.info("tldInThisRegistry, return:" + inThisRegTlds);
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
        LOGGER.info("getAllTlds, fullTld:" + fullTld);
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
        LOGGER.info("getAllTlds, tldList:" + tldList);
        return tldList;
    }

}
