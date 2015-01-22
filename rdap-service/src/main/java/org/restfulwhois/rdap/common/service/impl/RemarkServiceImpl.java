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

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.impl.RemarkQueryDaoImpl;
import org.restfulwhois.rdap.common.model.Remark;
import org.restfulwhois.rdap.common.model.base.TruncatedInfo.TruncateReason;
import org.restfulwhois.rdap.common.service.RemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * remark service.
 * 
 * @author jiashuo
 * 
 */
@Service
public class RemarkServiceImpl implements RemarkService {
    /**
     * remarkQueryDao.
     */
    @Autowired
    private RemarkQueryDaoImpl remarkQueryDao;
    /**
     * truncated notice list.
     * <p>
     * initialized from constants, or file.
     */
    private static List<Remark> truncatedRemarkList;

    @Override
    public void init() {
    }

    @Override
    public List<Remark> getTruncatedRemarkByReason(
            List<TruncateReason> truncateReasons) {
        List<Remark> result = new ArrayList<Remark>();
        if (null == truncateReasons) {
            return result;
        }
        if (null == truncatedRemarkList) {
            return result;
        }
        for (TruncateReason reason : truncateReasons) {
            Remark remark =
                    getTruncatedRemarkFromListByReason(reason,
                            truncatedRemarkList);
            if (null != remark) {
                result.add(remark);
            }
        }
        return result;
    }

    /**
     * get truncated remark from remarkList by reason.
     * 
     * @param reason
     *            reason.
     * @param truncatedRemarkList
     *            truncatedNoticeList.
     * @return remark if exist, null if not.
     */
    private Remark getTruncatedRemarkFromListByReason(TruncateReason reason,
            List<Remark> truncatedRemarkList) {
        for (Remark remark : truncatedRemarkList) {
            if (StringUtils.equalsIgnoreCase(remark.getReasonTypeShortName(),
                    reason.getName())) {
                return remark;
            }
        }
        return null;
    }

    /**
     * setTruncatedRemarkList.
     * 
     * @param truncatedRemarkList
     *            setTruncatedRemarkList.
     */
    @Resource(name = "truncatedRemarkList")
    public void setTruncatedRemarkList(List<Remark> truncatedRemarkList) {
        RemarkServiceImpl.truncatedRemarkList = truncatedRemarkList;
    }

}
