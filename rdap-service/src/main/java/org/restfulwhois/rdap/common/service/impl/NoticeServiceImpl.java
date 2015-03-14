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
package org.restfulwhois.rdap.common.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.NoticeDao;
import org.restfulwhois.rdap.common.model.Notice;
import org.restfulwhois.rdap.common.model.base.TruncatedInfo.TruncateReason;
import org.restfulwhois.rdap.common.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Notice service.
 * 
 * @author jiashuo
 * 
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    /**
     * noticeDao.
     */
    @Autowired
    private NoticeDao noticeDao;
    /**
     * not-truncated notice list.
     * <p>
     * initialized when startup.
     */
    private static List<Notice> notTruncatedNoticeList;
    /**
     * truncated notice list.
     * <p>
     * initialized from constants, or file.
     */
    private static List<Notice> truncatedNoticeList;
   

    @Override
    public List<Notice> getAllNotTruncatedNotice() {
        return notTruncatedNoticeList;
    }

    @Override
    public List<Notice> getTruncatedNoticeByReason(
            List<TruncateReason> truncateReasons) {
        List<Notice> result = new ArrayList<Notice>();
        if (null == truncateReasons) {
            return result;
        }
        if (null == truncatedNoticeList) {
            return result;
        }
        for (TruncateReason reason : truncateReasons) {
            Notice notice =
                    getTruncatedNoticeFromListByReason(reason,
                            truncatedNoticeList);
            if (null != notice) {
                result.add(notice);
            }
        }
        return result;
    }

    /**
     * get notice from noticeList by reason.
     * 
     * @param reason
     *            reason.
     * @param truncatedNoticeList
     *            truncatedNoticeList.
     * @return notice if exist, null if not.
     */
    private Notice getTruncatedNoticeFromListByReason(TruncateReason reason,
            List<Notice> truncatedNoticeList) {
        for (Notice notice : truncatedNoticeList) {
            if (StringUtils.equalsIgnoreCase(notice.getReasonTypeShortName(),
                    reason.getName())) {
                return notice;
            }
        }
        return null;
    }

    /**
     * setTruncatedNoticeList.
     * 
     * @param truncatedNoticeList
     *            setTruncatedNoticeList.
     */
    @Resource(name = "truncatedNoticeList")
    public void setTruncatedNoticeList(List<Notice> truncatedNoticeList) {
        NoticeServiceImpl.truncatedNoticeList = truncatedNoticeList;
    }
    
    /**
     * setNotTruncatedNoticeList.
     * 
     * @param notTruncatedNoticeList
     *            setNotTruncatedNoticeList.
     */
    @Resource(name = "notTruncatedNoticeList")
    public void setNotTruncatedNoticeList(List<Notice> notTruncatedNoticeList) {
        NoticeServiceImpl.notTruncatedNoticeList = notTruncatedNoticeList;
    }

}
