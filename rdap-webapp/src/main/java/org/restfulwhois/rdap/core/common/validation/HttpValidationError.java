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
package org.restfulwhois.rdap.core.common.validation;

import org.springframework.http.HttpStatus;

/**
 * HttpValidationError.
 * 
 * @author jiashuo
 * 
 */
public class HttpValidationError implements ValidationError {
    /**
     * statusCode.
     */
    private HttpStatus statusCode;

    /**
     * constructor.
     * 
     * @param statusCode
     *            statusCode.
     */
    private HttpValidationError(HttpStatus statusCode) {
        super();
        this.statusCode = statusCode;
    }

    /**
     * build400Error.
     * 
     * @return ValidationError.
     */
    public static ValidationError build400Error() {
        return new HttpValidationError(HttpStatus.BAD_REQUEST);
    }
    /**
     * build403Error.
     * 
     * @return ValidationError.
     */
    public static ValidationError build403Error() {
        return new HttpValidationError(HttpStatus.FORBIDDEN);
    }

    /**
     * build422Error.
     * 
     * @return build422Error.
     */
    public static ValidationError build422Error() {
        return new HttpValidationError(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    /**
     * statusCode.
     * 
     * @return statusCode.
     */
    public HttpStatus getStatusCode() {
        return statusCode;
    }

    @Override
    public String getCode() {
        return statusCode.name();
    }

    @Override
    public String getMessage() {
        return statusCode.getReasonPhrase();
    }

}
