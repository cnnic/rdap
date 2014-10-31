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
package org.restfulwhois.rdap.core.common.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.restfulwhois.rdap.core.common.dao.NoticeDao;
import org.restfulwhois.rdap.core.common.model.Notice;
import org.restfulwhois.rdap.core.common.model.Remark;
import org.restfulwhois.rdap.core.common.model.BaseNotice.NoticeType;
import org.restfulwhois.rdap.core.common.service.NoticeService;
import org.restfulwhois.rdap.dao.impl.RemarkQueryDaoImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Notice service implementation. 
 * 
 * @author zhanyq
 * 
 */
@Service
public class NoticeServiceImpl implements NoticeService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NoticeServiceImpl.class);
    
    
    /**
     * NoticeDao.
     */
    @Autowired
    private NoticeDao noticeDao;
    
    /**
     * remarkQueryDao.
     */
    @Autowired
    private RemarkQueryDaoImpl remarkQueryDao;

    /**
     * getAllNoticeMap.
     * 
     * @return Map of error messages.
     */   
    @Override
    public Map<String, Notice> getAllNoticesMap() {
        LOGGER.debug("getAllNoticeMap");
        List<Notice> notices = noticeDao.loadNoticesForTruncated();
        Map<String, Notice> noticeMap 
            = new HashMap<String, Notice>();
        for (Notice notice : notices) {      
          noticeMap.put(NoticeType.Notice.getName() 
                       + notice.getReasonTypeShortName(), notice);
        }
        LOGGER.debug("getAllNoticeMap noticeMap:" 
                 + noticeMap);
        return noticeMap;
     }
    /**
     * getAllNoticeMap.
     * 
     * @return Map of error messages.
     */   
    @Override
    public Map<String, Remark> getAllRemarksMap() {
        LOGGER.debug("getAllRemarksMap");         
        List<Remark> remarks = remarkQueryDao
               .loadRemarksByTypes();
       Map<String, Remark> remarkMap 
            = new HashMap<String, Remark>();
        for (Remark remark : remarks) {      
           remarkMap.put(NoticeType.REMARK.getName()
               + remark.getReasonTypeShortName(), remark);
        }
        LOGGER.debug("getAllRemarksMap remarkMap:" 
                 + remarkMap);
        return remarkMap;
     }
}
