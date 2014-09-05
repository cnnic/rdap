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
package cn.cnnic.rdap.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.cnnic.rdap.common.RdapProperties;
import cn.cnnic.rdap.controller.support.PrincipalHolder;

/**
 * connection control service.
 * 
 * Requirement from
 * http://tools.ietf.org/html/draft-ietf-weirds-rdap-sec-06#section-3.3
 * 
 * limit the number of connections.
 * 
 * @author jiashuo
 * 
 */
public final class ConnectionControlService {
    /**
     * min access interval in seconds.
     */
    private static final Long MIN_SECONDS_ACCESS_INTERVAL = RdapProperties
            .getMinSecondsAccessIntervalAnonymous();

    /**
     * ip white list for access interval.
     */
    private static final List<String> IP_WHITE_LIST_FOR_ACCESS_INTERVAL =
            RdapProperties.getIpWhiteListArrayForAccessInterval();

    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ConnectionControlService.class);

    /**
     * constructor.
     */
    private ConnectionControlService() {
        super();
    }

    /**
     * client last access time map:<ip:lastAccessTime>. It will be updated for
     * every access.
     */
    public static final Map<String, Long> CLIENT_IP2LAST_ACCESS_TIME_MAP =
            Collections.synchronizedMap(new HashMap<String, Long>());

    /**
     * concurrent query count.
     */
    public static final AtomicInteger CONCURRENT_Q_COUNT = new AtomicInteger(0);

    /**
     * get exceed rate limit.
     * 
     * @param ip
     *            client ip.
     * @return true if exceed rate limit,false if not.
     */
    public static boolean exceedRateLimit(String ip) {
        if (!hasLimit()) {
            return false;
        }
        LOGGER.debug("check exceedRateLimit, ip:{}" + ip);
        if (StringUtils.isBlank(ip)) {
            return false;
        }
        if (isInIpWhiteList(ip)) {
            return false;
        }
        Long lastAccessTime = CLIENT_IP2LAST_ACCESS_TIME_MAP.get(ip);
        long currentTimeMillis = System.currentTimeMillis();
        if (hasLimit()) {
            CLIENT_IP2LAST_ACCESS_TIME_MAP.put(ip, currentTimeMillis);
        }
        if (null == lastAccessTime) {
            return false;
        }
        long accessTimeInterval = currentTimeMillis - lastAccessTime;
        if (PrincipalHolder.getPrincipal().isAnonymous()) {
            LOGGER.debug("check exceedRateLimit, is anonymous.");
            return accessTimeInterval <= MIN_SECONDS_ACCESS_INTERVAL;
        } else {
            LOGGER.debug("check exceedRateLimit, is logined user.");
            return accessTimeInterval <= RdapProperties
                    .getMinSecondsAccessIntervalAuthed();
        }
    }

    /**
     * check if has limit. MIN_SECONDS_ACCESS_INTERVAL >0 is has limit.
     * 
     * @return true if has limit, false if not.
     */
    private static boolean hasLimit() {
        return MIN_SECONDS_ACCESS_INTERVAL > 0;
    }

    /**
     * check if ip in white list.
     * 
     * @param ip
     *            ip.
     * @return true if in white list, false if not.
     */
    private static boolean isInIpWhiteList(String ip) {
        List<String> whiteList = IP_WHITE_LIST_FOR_ACCESS_INTERVAL;
        if (null != whiteList && whiteList.contains(ip)) {
            return true;
        }
        return false;
    }

    /**
     * clear ip map.
     */
    public static void clearIpMap() {
        LOGGER.trace("clearIpMap begin...");
        if (null == CLIENT_IP2LAST_ACCESS_TIME_MAP) {
            return;
        }
        Set<String> keys = CLIENT_IP2LAST_ACCESS_TIME_MAP.keySet();
        for (String key : keys) {
            if ((System.currentTimeMillis() - CLIENT_IP2LAST_ACCESS_TIME_MAP
                    .get(key)) >= MIN_SECONDS_ACCESS_INTERVAL) {
                LOGGER.trace("remove ip:{}", key);
                CLIENT_IP2LAST_ACCESS_TIME_MAP.remove(key);
            }
        }
        LOGGER.trace("clearIpMap end.");
    }

    /**
     * increment concurrent query count, and check if exceed max count.MUST call
     * decrementAndGetCurrentQueryCount after query.
     * 
     * @return true if exceed, false if not.
     */
    public static boolean incrementConcurrentQCountAndCheckIfExceedMax() {
        LOGGER.debug("incrementConcurrentQCountAndCheckIfExceedMax.");
        if (isConcurrentCountNotLimit()) {
            return false;
        }
        int count = CONCURRENT_Q_COUNT.getAndIncrement();
        LOGGER.debug("incrementConcurrentQCountAndCheckIfExceedMax:{}" + count);
        if (count > RdapProperties.getMaxConcurrentCount() - 1) {
            LOGGER.debug("incrementConcurrentQCountAndCheckIfExceedMax :{} "
                    + RdapProperties.getMaxConcurrentCount());
            return true;
        }
        return false;
    }

    /**
     * decrement current query count.
     */
    public static void decrementAndGetCurrentQueryCount() {
        LOGGER.debug("decrementAndGetCurrentQueryCount.");
        if (isConcurrentCountNotLimit()) {
            return;
        }
        int count = CONCURRENT_Q_COUNT.decrementAndGet();
        LOGGER.debug("decrementAndGetCurrentQueryCount:{}", count);
    }

    /**
     * check if concurrent count is not limit.
     * 
     * @return true if not limit, false if limit.
     */
    private static boolean isConcurrentCountNotLimit() {
        boolean isNotLimit = 0 == RdapProperties.getMaxConcurrentCount();
        LOGGER.debug("isConcurrentCountNotLimit:{}", isNotLimit);
        return isNotLimit;
    }
}
