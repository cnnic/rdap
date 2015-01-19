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
package org.restfulwhois.rdap.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.validation.UpdateValidationError;
import org.restfulwhois.rdap.common.validation.ValidationResult;

/**
 * 
 * @author jiashuo
 * 
 */
public class UpdateValidateUtil {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final int MAX_LENGTH_LANG = 64;
    public static final int MAX_LENGTH_PORT43 = 4096;
    public static final int MAX_LENGTH_UNICODENAME = 1024;
    public static final int MAX_LENGTH_HANDLE = 100;
    public static final int MAX_LENGTH_32 = 32;
    public static final int MAX_LENGTH_STATUS = 20;
    public static final int MAX_LENGTH_LDHNAME = 255;
    public static final int MAX_LENGTH_255 = 255;
    public static final int MAX_LENGTH_2048 = 2048;
    public static final int MIN_VAL_FOR_INT_COLUMN = 0;
    // Math.pow(2, 31) - 1;
    public static final long MAX_VAL_FOR_INT_COLUMN = 2147483647L;
    public static Date MIN_VAL_FOR_TIMESTAMP_COLUMN = null;
    public static Date MAX_VAL_FOR_TIMESTAMP_COLUMN = null;
    static {
        try {
            MIN_VAL_FOR_TIMESTAMP_COLUMN =
                    new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                            .parse("1970-01-02 00:00:00");
            MAX_VAL_FOR_TIMESTAMP_COLUMN =
                    new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                            .parse("2038-01-02 00:00:00");
        } catch (ParseException e) {
        }
    }

    public static void checkNotEmpty(String value, String fieldName,
            ValidationResult validationResult) {
        if (StringUtils.isBlank(value)) {
            validationResult.addError(UpdateValidationError
                    .build4002Error(fieldName));
        }
    }

    public static void checkMaxLength(String value, int maxLength,
            String fieldName, ValidationResult validationResult) {
        if (StringUtils.length(value) > maxLength) {
            validationResult.addError(UpdateValidationError.build4003Error(
                    fieldName, maxLength + ""));
        }
    }

    public static void checkMinMaxInt(int value, int minValue, long maxValue,
            String fieldName, ValidationResult validationResult) {
        if (value < minValue || value > maxValue) {
            validationResult.addError(UpdateValidationError.build4010Error(
                    fieldName, minValue, maxValue));
        }
    }

    public static void checkMinMaxDate(Date value, Date minValue,
            Date maxValue, String fieldName, ValidationResult validationResult) {
        if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            validationResult
                    .addError(UpdateValidationError.build4010Error(fieldName,
                            format.format(minValue), format.format(maxValue)));
        }
    }

}
