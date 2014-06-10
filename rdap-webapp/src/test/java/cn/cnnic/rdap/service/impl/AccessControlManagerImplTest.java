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
package cn.cnnic.rdap.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.Domain;
import cn.cnnic.rdap.service.AccessControlManager;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for AccessControlManagerImpl.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class AccessControlManagerImplTest extends BaseTest {
    @Autowired
    private AccessControlManager accessControlManager;

    /**
     * test for exist entry.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/acl.xml")
    public void testHasEntry() {
        Domain domain = new Domain();
        domain.setId(1L);
        /**
         * anonymous user
         */
        assertFalse(accessControlManager.hasPermission(domain));
        /**
         * userId with 1
         */
        super.setUserIdToPrincipal(1L);
        assertTrue(accessControlManager.hasPermission(domain));
        /**
         * userId with 10000000
         */
        super.setUserIdToPrincipal(10000000L);
        assertFalse(accessControlManager.hasPermission(domain));
    }

    /**
     * test for hasEntry for non-exist entry.
     */
    @Test
    @DatabaseTearDown("classpath:cn/cnnic/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:cn/cnnic/rdap/dao/impl/acl.xml")
    public void testHasEntryForNonExistEntry() {
        Domain domain = new Domain();
        /**
         * domain with id 2000000 has non acl entry.
         */
        domain.setId(2000000L);
        assertTrue(accessControlManager.hasPermission(domain));
    }
}
