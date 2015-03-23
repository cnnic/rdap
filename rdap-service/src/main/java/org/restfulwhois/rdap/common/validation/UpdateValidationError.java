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
package org.restfulwhois.rdap.common.validation;


import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * UpdateValidationError.
 * 
 * @author jiashuo
 * 
 */
public final class UpdateValidationError implements ValidationError {
    /**
     * httpStatusCode.
     */
    private int httpStatusCode;
    /**
     * error code.
     */
    private int errorCode;
    /**
     * error description.
     */
    private String description;
    
    /**
     * constructor.
     * @param error
     *    error.
     * @param errorMessageParams
     *     errorMessageParams.
     */
    public UpdateValidationError(ServiceErrorCode error,
            Object... errorMessageParams) {
        super();
        this.httpStatusCode = error.getStatusCode().value();
        this.errorCode = error.getCode();
        this.description =
                ServiceErrorCode.formatMessage(error,
                        (Object[]) errorMessageParams);
    }

    /**
     * build4001Error.
     * @return ValidationError.
     */
    public static ValidationError build4001Error() {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4001);
    }

    /**
     * build4002Error.
     * @param errorMessageParam
     *    errorMessageParam.
     * @return ValidationError.
     */
    public static ValidationError build4002Error(String errorMessageParam) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4002,
                errorMessageParam);
    }

    /**
     * build4003Error.
     * @param fieldName
     *    fieldName.
     * @param maxLength
     *      maxLength.
     * @return ValidationError.
     */
    public static ValidationError build4003Error(String fieldName,
            String maxLength) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4003,
                fieldName, maxLength);
    }

    /**
     * build4007Error.
     * @param errorMessageParam
     *    errorMessageParam.
     * @return ValidationError.
     */
    public static ValidationError build4007Error(String errorMessageParam) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4007,
                errorMessageParam);
    }
    
    /**
     * build4008Error.
     * @param errorMessageParam
     *    errorMessageParam.
     * @return ValidationError.
     */
    public static ValidationError build4008Error(String errorMessageParam) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4008,
                errorMessageParam);
    }

    /**
     * build4009Error.
     * @return ValidationError.
     */
    public static ValidationError build4009Error() {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4009);
    }

    /**
     * build40010Error
     * @param fieldName
     *     fieldName.
     * @param minValue
     *    minValue.
     * @param maxValue
     *    maxValue.
     * @return ValidationError
     */
    public static ValidationError build40010Error(String fieldName,
            Integer minValue, Long maxValue) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_40010,
                fieldName, minValue, maxValue);
    }

    /**
     * build40010Error.
     * @param fieldName
     *    fieldName.
     * @param minValue
     *   minValue (string).
     * @param maxValue
     *    maxValue (string).
     * @return build4010Error
     */
    public static ValidationError build40010Error(String fieldName,
            String minValue, String maxValue) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_40010,
                fieldName, minValue, maxValue);
    }

    /**
     * build4031Error.
     * @return ValidationError.
     */
    public static ValidationError build4031Error() {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4031);
    }
    
    /**
     * build4041Error.
     * @param errorMessageParam
     *     errorMessageParam.
     * @return ValidationError.
     */
    public static ValidationError build4041Error(String errorMessageParam) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4041,
                errorMessageParam);
    }

    /**
     * build4091Error.
     * @param errorMessageParam
     *    errorMessageParam.
     * @return ValidationError.
     */
    public static ValidationError build4091Error(String errorMessageParam) {
        return new UpdateValidationError(ServiceErrorCode.ERROR_4091,
                errorMessageParam);
    }

    /**
     * get httpStatuscode.
     * @return integer.
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * set httpStatusCode.
     * @param httpStatusCode
     *    httpStatusCode.
     */
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(errorCode).toString();
    }

    @Override
    public int getCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return description;
    }

}
