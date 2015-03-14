package org.restfulwhois.rdap.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

public class BeanUtil {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BeanUtil.class);

    public static void copyProperties(Object source, Object target,
            String... ignoreProperties) {
        try {
            BeanUtils.copyProperties(source, target, ignoreProperties);
        } catch (Exception e) {
            LOGGER.error("copyProperties error:{}", e);
        }
    }
}
