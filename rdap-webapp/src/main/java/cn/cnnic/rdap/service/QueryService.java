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
package cn.cnnic.rdap.service;

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Network;

/**
 * query service interface.
 * 
 * @author jiashuo
 * 
 */
public interface QueryService {
    /**
     * query domain by domain name.
     * 
     * @param queryParam
     *            queryParam.
     * @return domain object.
     */
    Domain queryDomain(QueryParam queryParam);

    /**
     * query autnm.
     * 
     * @param queryParam
     *            queryParam.
     * @return autnum autnum.
     */
    Autnum queryAutnum(QueryParam queryParam);

    /**
     * query nameserver.
     * 
     * @param queryParam
     *            queryParam.
     * @return Nameserver for the result.
     */
    Nameserver queryNameserver(QueryParam queryParam);

    /**
     * query entity.
     * 
     * @param queryParam
     *            queryParam.
     * @return Entity.
     */
    Entity queryEntity(QueryParam queryParam);

    /**
     * query Ip.
     * 
     * @param queryParam
     *            queryParam.
     * @return Ip for the result.
     */
    Network queryIp(QueryParam queryParam);

}
