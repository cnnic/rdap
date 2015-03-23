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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.validation.UpdateValidationError;
import org.restfulwhois.rdap.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * update validate util.
 * @author jiashuo
 * 
 */
public final class UpdateValidateUtil {
    /**
     * private constructor.
     */
    private UpdateValidateUtil(){
        super();
    }
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(UpdateValidateUtil.class);
    /**
     * ERROR_PROP_NAME_PREFIX_EVENT
     */
    public static final String ERROR_PROP_NAME_PREFIX_EVENT = "events.";
    /**
     * ERROR_PROP_NAME_PREFIX_PUBLICID
     */
    public static final String ERROR_PROP_NAME_PREFIX_PUBLICID = "publicIds.";
    /**
     * ERROR_PROP_NAME_PREFIX_REMARK
     */
    public static final String ERROR_PROP_NAME_PREFIX_REMARK = "remarks.";
    /**
     * ERROR_PROP_NAME_PREFIX_LINK
     */
    public static final String ERROR_PROP_NAME_PREFIX_LINK = "links.";
    /**
     * ERROR_PROP_NAME_PREFIX_ENTITY
     */
    public static final String ERROR_PROP_NAME_PREFIX_ENTITY = "entities.";
    /**
     * ERROR_PROP_NAME_PREFIX_ENTITY_ADDR
     */
    public static final String ERROR_PROP_NAME_PREFIX_ENTITY_ADDR = "addresses.";
    /**
     * ERROR_PROP_NAME_PREFIX_ENTITY_TEL
     */
    public static final String ERROR_PROP_NAME_PREFIX_ENTITY_TEL = "telephones.";
    /**
     * DEFAULT_DATE_FORMAT
     */
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * MAX_LENGTH_LANG
     */
    public static final int MAX_LENGTH_LANG = 64;
    /**
     * MAX_LENGTH_PORT43
     */
    public static final int MAX_LENGTH_PORT43 = 4096;
    /**
     * MAX_LENGTH_UNICODENAME
     */
    public static final int MAX_LENGTH_UNICODENAME = 1024;
    /**
     * MAX_LENGTH_HANDLE
     */
    public static final int MAX_LENGTH_HANDLE = 100;
    /**
     * MAX_LENGTH_STATUS
     */
    public static final int MAX_LENGTH_STATUS = 20;
    /**
     * MAX_LENGTH_LDHNAME
     */
    public static final int MAX_LENGTH_LDHNAME = 255;
    /**
     * MAX_LENGTH_255
     */
    public static final int MAX_LENGTH_255 = 255;
    /**
     * MAX_LENGTH_2048
     */
    public static final int MAX_LENGTH_2048 = 2048;
    /**
     * MIN_VAL_FOR_INT_COLUMN
     */
    public static final int MIN_VAL_FOR_INT_COLUMN = 0;
    /**
     * MAX_VAL_FOR_INT_COLUMN :Math.pow(2, 31) - 1
     */    
    public static final int MAX_VAL_FOR_INT_COLUMN = 2147483647;
    /**
     * MIN_VAL_FOR_TINYINT_COLUMN
     */
    public static final int MIN_VAL_FOR_TINYINT_COLUMN = 0;
    /**
     * MAX_VAL_FOR_TINYINT_COLUMN
     */
    public static final int MAX_VAL_FOR_TINYINT_COLUMN = 127;
    /**
     * MIN_VAL_FOR_BIGINT_COLUMN
     */
    public static final int MIN_VAL_FOR_BIGINT_COLUMN = 0;
    /**
     * MAX_VAL_FOR_BIGINT_COLUMN:Math.pow(2, 63) - 1
     */    
    public static final long MAX_VAL_FOR_BIGINT_COLUMN = 9223372036854775807L;
    /**
     * minValForDateTimeColum
     */
    public static Date minValForDateTimeColum  = null;
    /**
     * maxValForDateTimeColum
     */
    public static Date maxValForDateTimeColum = null;
    /**
     * MAX_LENGTH_VERSION
     */
    public static final int MAX_LENGTH_VERSION = 2;
    
    static {
        try {
            minValForDateTimeColum =
                    new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                            .parse("1000-01-01 00:00:00");
            maxValForDateTimeColum =
                    new SimpleDateFormat(DEFAULT_DATE_FORMAT)
                            .parse("9999-12-31 23:59:59");
        } catch (ParseException e) {
            LOGGER.error("date format error:{}", e);
        }
    }

    /**
     * check value not empty.
     * @param value
     *     value.
     * @param fieldName
     *     fieldName.
     * @param validationResult
     *     validationResult.
     */
    public static void checkNotEmpty(String value, String fieldName,
            ValidationResult validationResult) {
        if (StringUtils.isBlank(value)) {
            validationResult.addError(UpdateValidationError
                    .build4002Error(fieldName));
        }
    }

    /**
     * check value is  not null,if value is null return 4002 error.
     * @param value
     *    value
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *    validationResult.
     */
    public static void checkNotNull(Object value, String fieldName,
            ValidationResult validationResult) {
        if (null == value) {
            validationResult.addError(UpdateValidationError
                    .build4002Error(fieldName));
        }
    }

    /**
     * check values is not empty.
     * @param values
     *    values.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *    validationResult.
     */
    public static void checkNotEmpty(List<String> values, String fieldName,
            ValidationResult validationResult) {
        if (null == values || values.isEmpty()) {
            validationResult.addError(UpdateValidationError
                    .build4002Error(fieldName));
        }
        boolean hasContent = false;
        for (String value : values) {
            if (StringUtils.isNotBlank(value)) {
                hasContent = true;
            }
        }
        if (!hasContent) {
            validationResult.addError(UpdateValidationError
                    .build4002Error(fieldName));
        }
    }

    /**
     * check the length of an string value is between the maximum and the minimum length.
     * @param value
     *    value.
     * @param maxLength
     *    maxLength.
     * @param fieldName
     *     fieldName.
     * @param validationResult
     *     validationResult.
     */
    public static void checkMaxLength(String value, int maxLength,
            String fieldName, ValidationResult validationResult) {
        if (StringUtils.length(value) > maxLength) {
            validationResult.addError(UpdateValidationError.build4003Error(
                    fieldName, maxLength + ""));
        }
    }

    /**
     * check the integer value is between the maximum and the minimum.
     * @param value
     *     value.
     * @param minValue
     *     minValue.
     * @param maxValue
     *     maxValue.
     * @param fieldName
     *     fieldName.
     * @param validationResult
     *     validationResult.
     */
    public static void checkMinMaxInt(Integer value, int minValue,
            long maxValue, String fieldName, ValidationResult validationResult) {
        if (null == value) {
            return;
        }
        if (value < minValue || value > maxValue) {
            validationResult.addError(UpdateValidationError.build40010Error(
                    fieldName, minValue, maxValue));
        }
    }
    
    /**
     * check the long value is between the maximum and the minimum.
     * @param value
     *     value.
     * @param minValue
     *     minValue.
     * @param maxValue
     *     maxValue.
     * @param fieldName
     *     fieldName.
     * @param validationResult
     *     validationResult.
     */
    public static void checkMinMaxLong(Long value, int minValue,
            long maxValue, String fieldName, ValidationResult validationResult) {
        if (null == value) {
            return;
        }
        if (value < minValue || value > maxValue) {
            validationResult.addError(UpdateValidationError.build40010Error(
                    fieldName, minValue, maxValue));
        }
    }

    /**
     * check date is between the maximum and the minimum.
     * @param value
     *     value.
     * @param minValue
     *     minValue.
     * @param maxValue
     *     maxValue.
     * @param fieldName
     *     fieldName.
     * @param validationResult
     *     validationResult.
     */
    public static void checkMinMaxDate(Date value, Date minValue,
            Date maxValue, String fieldName, ValidationResult validationResult) {
        if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
            SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
            validationResult
                    .addError(UpdateValidationError.build40010Error(fieldName,
                            format.format(minValue), format.format(maxValue)));
        }
    }

    /**
     * check date is valid and between the maximum and the minimum.
     * @param dateString
     *    dateString.
     * @param minValue
     *    minValue.
     * @param maxValue
     *    maxValue.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *    validationResult.
     */
    public static void checkValidAndMinMaxDate(String dateString,
            Date minValue, Date maxValue, String fieldName,
            ValidationResult validationResult) {
        if (StringUtils.isEmpty(dateString)) {
            return;
        }
        Date dateValue = DateUtil.parseUTC(dateString);
        if (null == dateValue) {
            validationResult.addError(UpdateValidationError
                    .build4007Error(fieldName));
            return;
        }
        checkMinMaxDate(dateValue, minValue, maxValue, fieldName,
                validationResult);
    }

    /**
     * check date is not empty,valid and between the maximum and the minimum.
     * @param dateString
     *    dateString.
     * @param fieldName
     *    fieldName.
     * @param validationResult
     *     validationResult.
     */
    public static void checkNotEmptyAndValidMinMaxDate(String dateString,
            String fieldName, ValidationResult validationResult) {
        checkNotEmpty(dateString, fieldName, validationResult);
        checkValidAndMinMaxDate(dateString,
                UpdateValidateUtil.minValForDateTimeColum,
                UpdateValidateUtil.maxValForDateTimeColum, fieldName,
                validationResult);
    }

}
