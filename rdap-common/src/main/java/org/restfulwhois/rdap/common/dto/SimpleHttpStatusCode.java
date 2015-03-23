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
package org.restfulwhois.rdap.common.dto;

/**
 * SimpleHttpStatusCode.
 * 
 * @author jiashuo
 * 
 */
public enum SimpleHttpStatusCode {
    /**
     * OK.
     */
    OK_200(200, ""),
    /**
     * 400 error.
     */
    ERROR_400(400, ""),
    /**
     * 404 error.
     */
    NOT_FOUND_404(404, ""),
    /**
     * 409 error.
     */
    CONFLICT_409(409, ""),
    /**
     * 405 error.
     */
    METHOD_NOT_ALLOWED_405(405, ""),
    /**
     * 415 error.
     */
    UNSUPPORTED_MEDIA_TYPE_415(415, ""),
    /**
     * 500 error.
     */
    INTERNAL_SERVER_ERROR_500(500, "");
    /**
     * value.
     */
    private final int value;
    /**
     * reasonPhrase.
     */
    private final String reasonPhrase;

    /**
     * constructor.
     * 
     * @param value
     *            value.
     * @param reasonPhrase
     *            reasonPhrase.
     */
    private SimpleHttpStatusCode(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * Return the integer value of this status code.
     * 
     * @return int val.
     */
    public int value() {
        return this.value;
    }

    /**
     * Return the reason phrase of this status code.
     * 
     * @return reasonPhrase.
     */
    public String getReasonPhrase() {
        return reasonPhrase;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    /**
     * Return the enum constant of this type with the specified numeric value.
     * 
     * @param statusCode
     *            the numeric value of the enum to be returned
     * @return the enum constant with the specified numeric value
     */
    public static SimpleHttpStatusCode valueOf(int statusCode) {
        for (SimpleHttpStatusCode status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("No matching constant for ["
                + statusCode + "]");
    }

}
