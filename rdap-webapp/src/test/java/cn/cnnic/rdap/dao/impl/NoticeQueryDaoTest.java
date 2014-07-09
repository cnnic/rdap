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
package cn.cnnic.rdap.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.Notice;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for notice DAO.
 * 
 * @author jiashuo
 * 
 */
//@SuppressWarnings("rawtypes")
public class NoticeQueryDaoTest extends BaseTest {
    /**
     * noticeDaoImpl.
     */
    @Autowired
    private NoticeDaoImpl noticeDaoImpl;

    /**
     * test query exist notice.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "notice.xml")
    public void testGetAllNotices() {
        List<Notice> notices = noticeDaoImpl.getAllNotices();
        Long noticeId = 1L;
        assertNotNull(notices);
        assertTrue(notices.size() > 0);
        for (Notice notice : notices) {
            if (noticeId.equals(notice.getId())) {
                continue;
            }
            assertEquals("Terms of Use", notice.getTitle());
            assertThat(notice.getDescription(),
                    CoreMatchers.hasItems("description1", "description2"));
            List<Link> links = notice.getLinks();
            assertNotNull(links);
            assertEquals(1, links.size());
            Link link = links.get(0);
            assertNotNull(link);
            assertEquals("http://example.com/context_uri", link.getValue());
        }
    }

}
