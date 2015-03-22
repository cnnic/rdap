package org.restfulwhois.rdap.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * date util.
 * @author jiashuo
 *
 */
public final class DateUtil {
    
    /**
     * private constructor.
     */
    private DateUtil(){
        super();
    }
    /**
     * utc format.
     */
    private static final String FORMAT_UTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    /**
     * dateTime format.
     */
    private static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(DateUtil.class);

    /**
     * parse utc date.
     * @param dateString
     *       dateString.
     * @return date
     */
    public static Date parseUTC(String dateString) {
        return parse(dateString, FORMAT_UTC);
    }

    /**
     * validate utc. 
     * @param utcDateString
     *       utcDateString.
     * @return true or false.
     */
    public static boolean validateUTCString(String utcDateString) {
        if (StringUtils.isBlank(utcDateString)) {
            return false;
        }
        Date date = parse(utcDateString, FORMAT_UTC);
        return null != date;
    }

    /**
     * format date.
     * @param date
     *       date.
     * @return string.
     */
    public static String formatUTC(Date date) {
        if (null == date) {
            return null;
        }
        return new SimpleDateFormat(FORMAT_DATETIME).format(date);
    }

    /**
     * parse dateString by format.
     * @param dateString
     *     dateString.
     * @param format
     *      format.
     * @return date.
     */
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
