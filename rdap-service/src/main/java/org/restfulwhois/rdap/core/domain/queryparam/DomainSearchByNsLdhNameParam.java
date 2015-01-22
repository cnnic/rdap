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

import org.restfulwhois.rdap.common.util.DomainUtil;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.restfulwhois.rdap.core.domain.model.DomainSearchType;
import org.restfulwhois.rdap.core.domain.validator.DomainSearchByNsLdhNameValidator;

/**
 * domain search by nameserver LDH name parameter bean.
 * 
 * @author jiashuo
 * 
 */
public class DomainSearchByNsLdhNameParam extends DomainSearchParam {

    /**
     * constructor.
     */
    public DomainSearchByNsLdhNameParam() {
        super();
    }

    @Override
    public boolean supportSearchType(DomainSearchType searchType) {
        return DomainSearchType.NSLDHNAME.equals(searchType);
    }

    @Override
    protected void initValidators() {
        super.initValidators();
        super.addValidator(new DomainSearchByNsLdhNameValidator());
    }

    @Override
    public void fillParam() throws Exception {
        String domainName =
                getParameter(getRequest(), DomainSearchType.NSLDHNAME.getName());
        String decodeDomain = domainName;
        decodeDomain = DomainUtil.iso8859Decode(domainName);
        decodeDomain =
                DomainUtil.urlDecodeAndReplaceAsciiToLowercase(decodeDomain);
        decodeDomain = StringUtil.getNormalization(decodeDomain);
        setQ(decodeDomain);
        setPunyName(decodeDomain);
    }

    @Override
    public void convertParam() throws Exception {
        String decodeDomain = getQ();
        decodeDomain = StringUtil.foldCase(decodeDomain);
        decodeDomain = DomainUtil.deleteLastPoint(decodeDomain);
        setQ(decodeDomain);
        setPunyName(decodeDomain);
    }

}
