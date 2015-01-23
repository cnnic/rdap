package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;

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
    
    @Test
    public void test_checkNotEmptyAndValidMinMaxDate_maxdate_noerror() {
    	ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                "9999-12-31T23:59:59Z", "eventDate", validationResult);
        assertFalse(validationResult.hasError());
    }
    
    @Test
    public void test_checkNotEmptyAndValidMinMaxDate_exceed_maxdate_haserror() {
    	ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                "10000-01-01T00:00:00Z", "eventDate", validationResult);
        assertTrue(validationResult.hasError());
    }
    
    @Test
    public void test_checkNotEmptyAndValidMinMaxDate_normal_date_norror() {
    	ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                "2015-01-28T00:00:00Z", "eventDate", validationResult);
        assertFalse(validationResult.hasError());
    }
    
    @Test
    public void test_checkNotEmptyAndValidMinMaxDate_impossible_date_hasrror() {
    	ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                "2015-25-80T00:00:00Z", "eventDate", validationResult);
        assertTrue(validationResult.hasError());
    }
    
    @Test
    public void test_checkNotEmptyAndValidMinMaxDate_empty_date_hasrror() {
    	ValidationResult validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                "", "eventDate", validationResult);
        assertTrue(validationResult.hasError());
        
        validationResult = new ValidationResult();
        UpdateValidateUtil.checkNotEmptyAndValidMinMaxDate(
                null, "eventDate", validationResult);
        assertTrue(validationResult.hasError());
    }
}
