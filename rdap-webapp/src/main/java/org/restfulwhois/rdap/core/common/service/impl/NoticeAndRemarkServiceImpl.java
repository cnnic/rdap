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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.restfulwhois.rdap.core.common.dao.NoticeDao;
import org.restfulwhois.rdap.core.common.dao.impl.RemarkQueryDaoImpl;
import org.restfulwhois.rdap.core.common.model.Notice;
import org.restfulwhois.rdap.core.common.model.Remark;
import org.restfulwhois.rdap.core.common.service.NoticeAndRemarkService;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo.TruncateReason;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Notice and remark service implementation. 
 * 
 * @author zhanyq
 * 
 */
@Service
public class NoticeAndRemarkServiceImpl implements 
                            NoticeAndRemarkService {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NoticeAndRemarkServiceImpl.class);    
    
    /**
     * query all notice list ,must be init before call 
     * getNoticeByReasonType() getNoticeNoTruncated().
     */
    private static List<Notice> noticesList = null;
    /**
     * query remark list for truncated,must be init before call
     * getRemarkByReasonType() .
     */
    private static List<Remark> remarksList = null;
    
    /**
     * NoticeDao.
     */
    @Autowired
    private  NoticeDao noticeDao;
    
    /**
     * remarkQueryDao.
     */
    @Autowired
    private  RemarkQueryDaoImpl remarkQueryDao;
    
    /**
     * init the notice and remark .
     */
    @PostConstruct
    public void initNoticeAndRemark() {
        noticesList = noticeDao.queryAllNotices();
        remarksList = remarkQueryDao.loadRemarksByTypes(); 
    }  
    
    /**
     * get notice by reasonTypeShortName.
     * 
     * @param reasonTypeShortName
     *            reasonTypeShortName
     * @return Notice
     */
    @Override
    public Notice getNoticeByReasonType(String reasonTypeShortName) {
        Notice result = new Notice();
        if (null != noticesList) {
            for (Notice notice: noticesList) {
            String shortName = notice.getReasonTypeShortName();
              if (shortName != null && shortName.equals(reasonTypeShortName)) {
                    result = notice;
                    break;
              }
            }           
        }       
        return result;
    }
    
    /**
     * get notice list for no truncated. 
     *
     * @return notice list
     */
    @Override
    public  List<Notice> getNoticeNoTruncated() {
        List<Notice> result = new ArrayList<Notice>();
        if (null != noticesList) {        
            for (Notice notice: noticesList) {
                String shortName = notice.getReasonTypeShortName(); 
                if (!TruncateReason.getAllReasonTypes()
                           .contains("'" + shortName + "'")) {
                    result.add(notice);
                }
            }   
        }          
        return result;
    }

    /**
     * get Remark by reasonTypeShortName.
     * 
     * @param reasonTypeShortName
     *            reasonTypeShortName
     * @return Remark
     */
    @Override
    public Remark getRemarkByReasonType(String reasonTypeShortName) {
        Remark result = new Remark();
        if (null != remarksList) {
            for (Remark remark: remarksList) {
              if (remark.getReasonTypeShortName().equals(reasonTypeShortName)) {
                    result = remark;
                    break;
              }
            }           
        }       
        return result;
    }

    
    
    
}
