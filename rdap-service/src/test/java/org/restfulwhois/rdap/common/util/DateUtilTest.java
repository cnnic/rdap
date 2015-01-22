package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.assertEquals;

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
}
