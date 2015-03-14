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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.restfulwhois.rdap.common.model.base.QueryUri;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.DomainUtil;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.restfulwhois.rdap.common.validation.Validator;
import org.restfulwhois.rdap.core.domain.validator.DomainNameAlabelValidator;
import org.restfulwhois.rdap.core.domain.validator.DomainNameValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * domain query parameter bean.
 * 
 * @author jiashuo
 * 
 */
public class DomainQueryParam extends QueryParam {
    /**
     * DOMAIN_NAME_VALIDATOR.
     */
    private static final DomainNameValidator DOMAIN_NAME_VALIDATOR =
            new DomainNameValidator();
    /**
     * DOMAIN_NAME_ALABEL_VALIDATOR.
     */
    private static final Validator DOMAIN_NAME_ALABEL_VALIDATOR = 
            new DomainNameAlabelValidator();
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(DomainQueryParam.class);

    /**
     * domain puny name.
     */
    private String punyName;

    /**
     * generateQueryParam.
     * 
     * @param domainName
     *            domainName.
     * @param punyDomainName
     *            punyDomainName.
     * @return DomainQueryParam.
     */
    public static DomainQueryParam generateQueryParam(String domainName,
            String punyDomainName) {
        return new DomainQueryParam(domainName, punyDomainName);
    }

    @Override
    protected void initValidators() {
        addValidator(DOMAIN_NAME_ALABEL_VALIDATOR);
        addValidator(DOMAIN_NAME_VALIDATOR);
    }

    @Override
    public QueryUri getQueryUri() {
        return QueryUri.DOMAIN;
    }

    /**
     * constructor.
     * 
     * @param request
     *            request.
     */
    public DomainQueryParam(HttpServletRequest request) {
        super(request);
    }

    @Override
    public void fillParam() throws Exception {
        String domainName = getLastSplitInURI(getRequest());
        super.setOriginalQ(domainName);
        String decodeDomain =
                DomainUtil.urlDecodeAndReplaceAsciiToLowercase(domainName);
        decodeDomain = StringUtil.getNormalization(decodeDomain);
        super.setQ(decodeDomain);
    }

    @Override
    public void convertParam() throws Exception {
        String decodeDomain = getQ();
        decodeDomain = StringUtil.foldCase(decodeDomain);
        LOGGER.debug("after foldCaseAndNormalization: {}", decodeDomain);
        // long lable exception
        String punyDomainName = DomainUtil.geneDomainPunyName(decodeDomain);
        decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);
        super.setQ(decodeDomain);
        setPunyName(punyDomainName);
    }

    /**
     * constructor.
     * 
     * @param q
     *            query string.
     * @param punyName
     *            domain puny name.
     */
    public DomainQueryParam(String q, String punyName) {
        super(q);
        this.punyName = punyName;
    }

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
        String punyDomainName = DomainUtil.safeGeneDomainPunyName(super.getQ());
        if (StringUtils.isBlank(punyDomainName)) {
            return null;
        }
        String fullTld = StringUtils.substringAfter(punyDomainName, ".");
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

}
