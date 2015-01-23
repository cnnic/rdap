package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class DateUtilTest {
    @Test
    public void test_parseUTC() {
        Calendar c = Calendar.getInstance();
        Date date = DateUtil.parseUTC("2015-01-01T01:01:01Z");
        c.setTime(date);
        assertEquals(2015, c.get(Calendar.YEAR));
        assertEquals(0, c.get(Calendar.MONTH));
        assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
        assertEquals(1, c.get(Calendar.HOUR_OF_DAY));
        assertEquals(1, c.get(Calendar.MINUTE));
        assertEquals(1, c.get(Calendar.SECOND));
    }

    @Test
    public void test_parse_and_format_UTC() {
        assertEquals("2015-01-01 01:01:01",
                DateUtil.formatUTC(DateUtil.parseUTC("2015-01-01T01:01:01Z")));
    }

    @Test
    public void test_formatUTC() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2015);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 1);
        c.set(Calendar.MINUTE, 1);
        c.set(Calendar.SECOND, 1);
        assertEquals("2015-01-01 01:01:01", DateUtil.formatUTC(c.getTime()));
    }
    
    @Test
    public void test_parse_noerror(){
    	String formatString = "yyyy-MM-dd HH:mm:ss";
    	String dateString = "2015-01-22 01:44:50";
    	Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2015);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 22);
        c.set(Calendar.HOUR_OF_DAY, 01);
        c.set(Calendar.MINUTE, 44);
        c.set(Calendar.SECOND, 50);
        c.set(Calendar.MILLISECOND, 0);
    	Date date = new Date(c.getTimeInMillis());
    	assertEquals(date.compareTo(DateUtil.parse(dateString, formatString)), 0);
    }
    
    @Test
    public void test_parse_impossible_date_hasrror(){
    	String formatString = "yyyy-MM-dd HH:mm:ss";
    	String dateString = "2015-25-22 01:44:50";
    	assertEquals(null, DateUtil.parse(dateString, formatString));
    }
    
    @Test
    public void test_parse_emptydate_haserror() throws ParseException{
    	String formatString = "yyyy-MM-dd HH:mm:ss";
    	String dateString = "";
    	assertEquals(null, DateUtil.parse(dateString, formatString));
    }
    
    @Test
    public void test_parse_nulldate_haserror() throws ParseException{
    	String formatString = "yyyy-MM-dd HH:mm:ss";
    	String dateString = null;
    	assertEquals(null, DateUtil.parse(dateString, formatString));
    }
    
    @Test
    public void test_validateUTCString_utcdate_true() throws ParseException{
    	String dateString = "2015-01-22T17:30:00Z";
    	assertEquals(true, DateUtil.validateUTCString(dateString));
    }
    
    @Test
    public void test_validateUTCString_not_utcdate_false() throws ParseException{
    	String dateString = "2015-01-22 17:30:00";
    	assertEquals(false, DateUtil.validateUTCString(dateString));
    }
    
    @Test
    public void test_validateUTCString_emptydate_false() throws ParseException{
    	String dateString = "";
    	assertEquals(false, DateUtil.validateUTCString(dateString));
    }
    
    @Test
    public void test_validateUTCString_nulldate_false() throws ParseException{
    	String dateString = null;
    	assertEquals(false, DateUtil.validateUTCString(dateString));
    }
}
