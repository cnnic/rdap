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
        ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                "1000-01-01T00:00:00Z", "eventDate", validationResult);
        assertFalse(validationResult.hasError());
    }

    @Test
    public void testCheckMinMaxDate_haserror() throws ParseException {
        ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                "0001-01-01T00:00:00Z", "eventDate", validationResult);
        assertTrue(validationResult.hasError());
    }
}
