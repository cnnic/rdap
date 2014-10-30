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
package org.restfulwhois.rdap.core.common.util;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.restfulwhois.rdap.core.model.BaseNotice.NoticeType;
import org.restfulwhois.rdap.core.model.Notice;
import org.restfulwhois.rdap.core.model.Remark;
import org.restfulwhois.rdap.core.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class is used to create remark and notice object in cache
 * 
 * 
 * If object is not found, empty object will be returned.
 * 
 * @author zhanyq
 * 
 */
@Component
public class CustomizeNoticeandRemark {
    /**
     * all notices map,must be init before call.
     * getNoticeByReasonType() 
     */
    private static Map<String, Notice> noticesMap = null;
    
    /**
     * Remarks map,must be init before call. 
     * getRemarkByReasonType()
     */
    private static Map<String, Remark> remarksMap = null;    
    
    
    /**
     * error message service.
     */
    private static NoticeService noticeService;   

    /**
     * init the error message and policy service.
     */
    @PostConstruct
    private void init() {
        initNotices();  
        initRemarks(); 
    }
    
    /**
     * init Notices list.
     */
    public static void initNotices() {
        noticesMap = noticeService.getAllNoticesMap();          
    }
    
    /**
     * init Remarks list.
     */
    public static void initRemarks() {          
        remarksMap = noticeService.getAllRemarksMap();
    }

    /**
     * get ErrorMessage by reasonTypeShortName.
     * 
     * @param reasonTypeShortName
     *            reasonTypeShortName
     * @return Notice
     */
    public static Notice getNoticeByReasonType(String reasonTypeShortName) {
        if (null == noticesMap || noticesMap.isEmpty()) {
            initNotices();
        }
        Notice result = noticesMap
                .get(NoticeType.Notice.getName() + reasonTypeShortName);
        if (null != result) {
            return result;
        }
        return new Notice();
    }

    /**
     * get Remark by reasonTypeShortName.
     * 
     * @param reasonTypeShortName
     *            reasonTypeShortName
     * @return Remark
     */
    public static Remark getRemarkByReasonType(String reasonTypeShortName) {
         if (null == remarksMap || remarksMap.isEmpty()) {
               initRemarks();
         }
         Remark result = remarksMap
                 .get(NoticeType.REMARK.getName() + reasonTypeShortName);
         if (null != result) {
             return result;
         }
         return new Remark();
    }

    /**
     * set notice service.
     * 
     * @param noticeService
     *           notice service to set.
     */
    @Autowired
    public void setNoticeService(NoticeService noticeService) {
        CustomizeNoticeandRemark.noticeService = noticeService;
    }    
    
}
