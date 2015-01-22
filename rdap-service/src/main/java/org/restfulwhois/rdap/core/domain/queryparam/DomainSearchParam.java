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
package org.restfulwhois.rdap.core.domain.queryparam;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.restfulwhois.rdap.common.model.base.QueryUri;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.core.domain.model.DomainSearchType;
import org.restfulwhois.rdap.core.domain.validator.SearchUriValidator;

/**
 * base domain search parameter bean.
 * 
 * @author jiashuo
 * 
 */
public abstract class DomainSearchParam extends QueryParam {

    /**
     * construnction.
     */
    public DomainSearchParam() {
        super();
    }

    /**
     * check if support type.
     * 
     * @param searchType
     *            searchType.
     * @return true if support, false if not.
     */
    public boolean supportSearchType(DomainSearchType searchType) {
        return false;
    }

    @Override
    protected void initValidators() {
        super.addValidator(new SearchUriValidator());
    }
    
    /**
     * constructor.
     * 
     * @param q
     *            search string.
     * @param punyName
     *            domain puny name.
     */
    public DomainSearchParam(String q, String punyName) {
        super(q);
        this.punyName = punyName;
    }

    /**
     * domain puny name.
     */
    private String punyName;

    /**
     * get punyName.
     * 
     * @return punyName.
     */
    public String getPunyName() {
        return punyName;
    }

    /**
     * set punyName.
     * 
     * @param punyName
     *            punyName.
     */
    public void setPunyName(String punyName) {
        this.punyName = punyName;
    }

    /**
     * is domain from RIR, like 192.in-addr.arpa or F.0.0.ip6.arpa .
     * 
     * @return punyName is from RIR.
     */
    public boolean isRirDomain() {
        if (null != getQ()) {
            return getQ().endsWith(".in-addr.arpa")
                    || getQ().endsWith(".ip6.arpa");
        }
        return false;
    }

    /**
     * get full tld of domain puny name.
     * 
     * @return tld tld.
     */
    public String getFullPunyTld() {
        if (StringUtils.isBlank(punyName)) {
            return null;
        }
        String fullTld = StringUtils.substringAfter(punyName, ".");
        if (StringUtils.isBlank(fullTld)) {
            return ".";
        }
        return fullTld;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getQ()).append(punyName)
                .toString();
    }

    /**
     * search by domain name, nsLdhName, nsIp.
     */
    private String searchByParam;

    /**
     * get method for SearchByParam.
     * 
     * @return String searchByParam.
     */
    public String getSearchByParam() {
        return searchByParam;
    }

    /**
     * set method for SearchByParam.
     * 
     * @param searchByParam
     *            searchByParam.
     */
    public void setSearchByParam(String searchByParam) {
        this.searchByParam = searchByParam;
    }
    
    @Override
    public QueryUri getQueryUri() {
        return QueryUri.DOMAINS;
    }

}
