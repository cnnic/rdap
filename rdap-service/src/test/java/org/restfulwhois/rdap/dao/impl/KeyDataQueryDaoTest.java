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
package org.restfulwhois.rdap.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.QueryDao;
import org.restfulwhois.rdap.common.model.KeyData;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for KeyData DAO.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class KeyDataQueryDaoTest extends BaseTest {
    /**
     * keyDataQueryDaoImpl.
     */
    @Autowired
    private QueryDao<KeyData> keyDataQueryDaoImpl;

    /**
     * test query exist.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "keyData.xml")
    public void testQueryExist() {
        Long secureDnsId = 1L;
        List<KeyData> keyDataList =
                keyDataQueryDaoImpl.queryAsInnerObjects(secureDnsId,
                        ModelType.SECUREDNS);
        assertNotNull(keyDataList);
        assertTrue(keyDataList.size() >= 1);
        KeyData keyData = keyDataList.get(0);
        assertNotNull(keyData);
        assertEquals(keyData.getAlgorithm().intValue(), 1);
        assertEquals(keyData.getPublicKey(),
                "D4B7D520E7BB5F0F67674A0CCEB1E3E0614B93C4F9E99B8383F6A1E4469DA50A");
        assertEquals(keyData.getProtocol().intValue(), 1);
        assertEquals(keyData.getFlags().intValue(), 1);
        // link
        List<Link> links = keyData.getLinks();
        assertNotNull(links);
        assertEquals(1, links.size());
        Link link = links.get(0);
        assertNotNull(link);
        assertEquals("http://example.com/context_uri", link.getValue());
    }

    /**
     * test query non exist.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(value = "keyData.xml")
    public void testQueryNonExist() {
        final Long nonExistSecureDnsId = 10000L;
        List<KeyData> keyDataList =
                keyDataQueryDaoImpl.queryAsInnerObjects(nonExistSecureDnsId,
                        ModelType.SECUREDNS);
        assertNotNull(keyDataList);
        assertEquals(keyDataList.size(), 0);
    }

}
