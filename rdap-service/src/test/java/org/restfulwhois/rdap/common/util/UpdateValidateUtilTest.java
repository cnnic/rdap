package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.restfulwhois.rdap.common.validation.ValidationResult;

public class UpdateValidateUtilTest {
    @Test
    public void testCheckMinMaxInt_haserror() throws ParseException {
        ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkMinMaxInt(-1,
                UpdateValidateUtil.MIN_VAL_FOR_INT_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_INT_COLUMN, "maxSigLife",
                validationResult);
        assertTrue(validationResult.hasError());
    }

    @Test
    public void testCheckMinMaxDate_noerror() throws ParseException {
        Date minDate =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse("1970-01-02 00:00:00");
        ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkMinMaxDate(minDate,
                UpdateValidateUtil.MIN_VAL_FOR_TIMESTAMP_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_TIMESTAMP_COLUMN, "eventDate",
                validationResult);
        assertFalse(validationResult.hasError());
    }

    @Test
    public void testCheckMinMaxDate_haserror() throws ParseException {
        Date minDate =
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .parse("1970-01-01 00:00:00");
        ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkMinMaxDate(minDate,
                UpdateValidateUtil.MIN_VAL_FOR_TIMESTAMP_COLUMN,
                UpdateValidateUtil.MAX_VAL_FOR_TIMESTAMP_COLUMN, "eventDate",
                validationResult);
        assertTrue(validationResult.hasError());
    }
}
