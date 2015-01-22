/*
 * Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 * 
 * All rights reserved.
 *  
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  
 * * Redistributions of source code must retain the above copyright Remark,
 *  this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright Remark,
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
package org.restfulwhois.rdap.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.model.Remark;
import org.restfulwhois.rdap.common.service.RemarkService;
import org.restfulwhois.rdap.common.support.TruncatedInfo.TruncateReason;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RemarkServiceImplTest extends BaseTest {
    @Autowired
    private RemarkService remarkService;

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_get_truncated_Remark_excessiveLoad() {
        List<TruncateReason> truncateReasons = new ArrayList<TruncateReason>();
        truncateReasons.add(TruncateReason.TRUNCATEREASON_EXLOAD);
        List<Remark> result =
                remarkService.getTruncatedRemarkByReason(truncateReasons);
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void test_get_truncated_Remark_with_empty_parameters() {
        List<TruncateReason> truncateReasons = new ArrayList<TruncateReason>();
        List<Remark> result =
                remarkService.getTruncatedRemarkByReason(truncateReasons);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

}
