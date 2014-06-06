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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.controller.support.QueryParser;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for Remark DAO.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class RemarkQueryDaoTest extends BaseTest {
    /**
     * queryParser.
     */
    @Autowired
    private QueryParser queryParser;
    /**
     * remarkQueryDao.
     */
    @Autowired
    private RemarkQueryDaoImpl remarkQueryDao;

    /**
     * test query exist.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "remark.xml")
    public void testQueryExistRemark() {
        Long autnumId = 1L;
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(autnumId, ModelType.AUTNUM);
        assertNotNull(remarks);
        assertTrue(remarks.size() > 0);
        Remark remark = remarks.get(0);
        assertNotNull(remark);
        assertEquals("Terms of Use", remark.getTitle());
        assertThat(remark.getDescription(),
                CoreMatchers.hasItems("description1", "description2"));
        List<Link> links = remark.getLinks();
        assertNotNull(links);
        assertTrue(links.size() >= 1);
        Link link = links.get(0);
        assertNotNull(link);
        assertEquals("http://example.com/context_uri", link.getValue());
    }

    /**
     * test query exist.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "remark.xml")
    public void testQueryRemarkWithoutLinksAndDesc() {
        Long autnumId = 2L;
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(autnumId, ModelType.AUTNUM);
        assertNotNull(remarks);
        assertTrue(remarks.size() > 0);
        Remark remark = remarks.get(0);
        assertNotNull(remark);
        assertEquals("Terms of Use", remark.getTitle());
        assertNull(remark.getDescription());
        assertNotNull(remark.getLinks());
        assertEquals(0, remark.getLinks().size());
    }

    /**
     * test query non exist remark.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "remark.xml")
    public void testQueryNonExistRemark() {
        Long autnumId = 1000L;
        List<Remark> remarks =
                remarkQueryDao.queryAsInnerObjects(autnumId, ModelType.AUTNUM);
        assertNotNull(remarks);
        assertEquals(0, remarks.size());
    }
}
