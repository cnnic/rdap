package org.restfulwhois.rdap.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {
    private static final String FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DateUtil.class);

    public static Date parseUTC(String dateString) {
        return parse(dateString, FORMAT_UTC);
    }

    public static boolean validateUTCString(String utcDateString) {
        if (StringUtils.isBlank(utcDateString)) {
            return false;
        }
        Date date = parse(utcDateString, FORMAT_UTC);
        return null != date;
    }

    public static String formatUTC(Date date) {
        if (null == date) {
            return null;
        }
        return new SimpleDateFormat(FORMAT_DATETIME).format(date);
    }

    public static Date parse(String dateString, String format) {
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat(format);
        	sdf.setLenient(false);
            Date date = sdf.parse(dateString);
            return date;
        } catch (Exception e) {
            LOGGER.error("error timestamp format,error:{}", e);
            return null;
        }
    }
}
