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

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.Principal;
import cn.cnnic.rdap.bean.SecureObject;
import cn.cnnic.rdap.dao.AclDao;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;

/**
 * Test for acl dao.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class AclDaoTest extends BaseTest {
    /**
     * acl dao.
     */
    @Autowired
    private AclDao aclDao;

    /**
     * test for exist entry - domain.
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "acl.xml")
    public void testHasEntryDomain() {
        Principal principal = new Principal(1L);
        SecureObject secureObject = new SecureObject(1L,
                ModelType.DOMAIN.getName());
        assertTrue(aclDao.hasEntry(principal, secureObject));
    }
    /**
     * test for hasEntry for non-exist entry.
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "acl.xml")
    public void testHasEntryForNonExistEntry() {
        Principal principal = new Principal(2L);
        SecureObject secureObject = new SecureObject(2L,
                ModelType.DOMAIN.getName());
        assertTrue(aclDao.hasEntry(principal, secureObject));
    }
    

    /**
     * test for exist entry - nameserver.
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "acl.xml")
    public void testHasEntryNameserver() {
        Principal principal = new Principal(1L);
        SecureObject secureObject = new SecureObject(1L,
                ModelType.NAMESERVER.getName());
        assertTrue(aclDao.hasEntry(principal, secureObject));
    }
    
    /**
     * test for exist entry - autnum.
     */
    @Test
    // @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "acl.xml")
    public void testHasEntryAutnum() {
        Principal principal = new Principal(1L);
        SecureObject secureObject = new SecureObject(1L,
                ModelType.AUTNUM.getName());
        assertTrue(aclDao.hasEntry(principal, secureObject));
    }

}
