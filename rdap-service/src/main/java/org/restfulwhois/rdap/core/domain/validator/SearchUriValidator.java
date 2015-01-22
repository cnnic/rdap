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
package org.restfulwhois.rdap.core.domain.validator;

import javax.servlet.http.HttpServletRequest;

import org.restfulwhois.rdap.common.model.base.QueryUri;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.RequestUtil;
import org.restfulwhois.rdap.common.validation.QueryValidationError;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.restfulwhois.rdap.common.validation.Validator;
import org.restfulwhois.rdap.core.domain.queryparam.DomainSearchParam;
import org.restfulwhois.rdap.core.entity.queryparam.EntitySearchParam;
import org.restfulwhois.rdap.core.help.queryparam.HelpQueryParam;
import org.restfulwhois.rdap.core.nameserver.queryparam.NameserverSearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * validate last split of URI is expected Query URI: /domains%1a?name=cnnic.cn
 * <p>
 * the '%1a' is invalid URL-encoded string, but spring URL mapping will ignore
 * the '%1a' section and map the other part URI '/domains'. This validator will
 * check for these last invalid URI.
 * <p>
 * this validator is used for search of domain, nameserver, and entity.
 * 
 * @author jiashuo
 * 
 */
public class SearchUriValidator implements Validator {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(SearchUriValidator.class);

    @SuppressWarnings("rawtypes")
    @Override
    public boolean supports(Class<?> clazz) {
        Class superClazz = clazz.getSuperclass();
        return superClazz.equals(DomainSearchParam.class)
                || superClazz.equals(NameserverSearchParam.class)
                || superClazz.equals(EntitySearchParam.class)
                || clazz.equals(HelpQueryParam.class);
    }

    @Override
    public void validate(QueryParam queryParam,
            ValidationResult validationResult) {
        if (!validateLastSplitUri(queryParam.getRequest(),
                queryParam.getQueryUri())) {
            LOGGER.debug("error:last split URI is not valid for search: {}",
                    queryParam.getQueryUri());
            validationResult.addError(QueryValidationError.build400Error());
        }
    }

    /**
     * validate last split of URI is expected Query URI.
     * 
     * @param request
     *            request.
     * @param expectedQueryUri
     *            expectedQueryUri.
     * @return true if is expected URI, false if not.
     */
    private boolean validateLastSplitUri(HttpServletRequest request,
            QueryUri expectedQueryUri) {
        try {
            String lastSpliInURI = RequestUtil.getLastSplitInURI(request);
            if (!expectedQueryUri.getNameWithoutUriSplitter().equals(
                    lastSpliInURI)) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
