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
package org.restfulwhois.rdap.filters.queryFilter;

import java.util.List;

import org.restfulwhois.rdap.core.common.dao.NoticeDao;
import org.restfulwhois.rdap.core.common.filter.QueryFilter;
import org.restfulwhois.rdap.core.common.model.Notice;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.BaseSearchModel;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.support.TruncatedInfo.TruncateReason;
import org.restfulwhois.rdap.core.common.util.CustomizeNoticeandRemark;
import org.restfulwhois.rdap.core.common.validation.ValidationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * NoticeQueryFilter.
 * 
 * @author jiashuo
 * 
 */
@Component
public class NoticeQueryFilter implements QueryFilter {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NoticeQueryFilter.class);
    /**
     * noticeDao.
     */
    @Autowired
    private NoticeDao noticeDao;

    @Override
    public ValidationResult preParamValidate(QueryParam queryParam) {
        return null;
    }

    @Override
    public ValidationResult postParamValidate(QueryParam queryParam) {
        return null;
    }

    @Override
    public ValidationResult postQuery(QueryParam queryParam,
            ResponseEntity responseEntity) {
        Object responseBody = responseEntity.getBody();
        if (responseBody instanceof BaseModel) {
            BaseModel model = (BaseModel) responseBody;
            LOGGER.debug("addNotices begin.");
            addNotices(model);
            LOGGER.debug("addNotices end.");
        }
        return null;
    }

    /**
     * add notices to model.
     * 
     * @param model
     *            model.
     */
    public void addNotices(BaseModel model) {
        if (null == model) {
            return;
        }
        List<Notice> notices = noticeDao.getNoticesNoTruncated();
        addTruncatedNotice(model, notices);
        model.setNotices(notices);
    }

    /**
     * addTruncatedNotice.
     * 
     * @param model
     *            model.
     * @param notices
     *            notices.
     */
    private void addTruncatedNotice(BaseModel model, List<Notice> notices) {
        if (model instanceof BaseSearchModel<?>
                && ((BaseSearchModel<?>) model).getTruncatedInfo()
                        .getResultsTruncated()) {
            BaseSearchModel<?> baseSearchModel = (BaseSearchModel<?>) model;
            List<TruncateReason> truncateReasons =
                    baseSearchModel.getTruncatedInfo().getTruncateReasons();
            for (TruncateReason truncateReason : truncateReasons) {
                notices.add(CustomizeNoticeandRemark
                        .getNoticeByReasonType(truncateReason.getName()));
            }
        }
    }

}
