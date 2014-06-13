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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.Ip;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.dao.QueryDao;
import cn.cnnic.rdap.dao.impl.AutnumQueryDaoImpl;
import cn.cnnic.rdap.dao.impl.DomainQueryDaoImpl;
import cn.cnnic.rdap.dao.impl.NameserverQueryDaoImpl;
import cn.cnnic.rdap.dao.impl.IpQueryDaoImpl;
import cn.cnnic.rdap.service.QueryService;
import cn.cnnic.rdap.service.RdapConformanceService;

/**
 * query service implementation
 * 
 * @author jiashuo
 * 
 */
@Service
public class QueryServiceImpl implements QueryService {
	/**
	 * rdap conformance service
	 */
	@Autowired
	private RdapConformanceService rdapConformanceService;
	/**
	 * domain DAO
	 */
	@Autowired
	private DomainQueryDaoImpl domainDao;

	/**
	 * autnum DAO
	 */
	@Autowired
	private AutnumQueryDaoImpl autnumQueryDao;
	
	/**
     * nameserver DAO
     */
    @Autowired
    private NameserverQueryDaoImpl nameserverQueryDao;
    
    /**
     * ip DAO
     */
    @Autowired
    private IpQueryDaoImpl ipQueryDao;

    /**
     * entityQueryDao.
     */
    @Autowired
    private QueryDao<Entity> entityQueryDao;

	/**
	 * query domain by domain name
	 */
	@Override
	public Domain queryDomain(QueryParam queryParam) {
		return domainDao.query(queryParam);
	}

	@Override
	public Autnum queryAutnum(QueryParam queryParam) {
		return autnumQueryDao.query(queryParam);
	}

	@Override
	public Nameserver queryNameserver(QueryParam queryParam) {
	    return nameserverQueryDao.query(queryParam);
	}

    @Override
    public Entity queryEntity(QueryParam queryParam) {
        return entityQueryDao.query(queryParam);
    }

	@Override
    public Ip queryIp(QueryParam queryParam) {
        return ipQueryDao.query(queryParam);
    }

}
