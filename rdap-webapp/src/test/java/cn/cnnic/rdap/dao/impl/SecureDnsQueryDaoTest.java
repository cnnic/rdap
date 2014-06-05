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

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cn.cnnic.rdap.BaseTest;
import cn.cnnic.rdap.bean.DsData;
import cn.cnnic.rdap.bean.KeyData;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.SecureDns;
import cn.cnnic.rdap.dao.QueryDao;

import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for link DAO
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class SecureDnsQueryDaoTest extends BaseTest {
    @Autowired
    private QueryDao<SecureDns> secureDnsQueryDao;

    /**
     * test query exist event
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "secureDns.xml")
    public void testQueryExistLink() {
        Long domainId = 1L;
        List<SecureDns> secureDnsList = secureDnsQueryDao.queryAsInnerObjects(
                domainId, ModelType.DOMAIN);
        assertNotNull(secureDnsList);
        assertEquals(1, secureDnsList.size());
        SecureDns secureDns = secureDnsList.get(0);
        assertNotNull(secureDns);
        assertEquals(1, secureDns.getMaxSigLife().intValue());
        assertEquals(true, secureDns.isDelegationSigned());
        assertEquals(true, secureDns.isZoneSigned());
        // dsData
        List<DsData> dsDataList = secureDns.getDsData();
        assertNotNull(dsDataList);
        assertEquals(1, dsDataList.size());
        DsData dsData = dsDataList.get(0);
        assertNotNull(dsData);
        assertEquals(dsData.getAlgorithm().intValue(), 1);
        assertEquals(
                "D4B7D520E7BB5F0F67674A0CCEB1E3E0614B93C4F9E99B8383F6A1E4469DA50A",
                dsData.getDigest());
        assertEquals(1, dsData.getDigestType());
        assertEquals(1, dsData.getKeyTag().intValue());
        // keyData
        List<KeyData> keyDataList = secureDns.getKeyData();
        assertNotNull(keyDataList);
        assertEquals(1, keyDataList.size());
        KeyData keyData = keyDataList.get(0);
        assertNotNull(keyData);
        assertEquals(1, keyData.getAlgorithm().intValue());
        assertEquals(
                "D4B7D520E7BB5F0F67674A0CCEB1E3E0614B93C4F9E99B8383F6A1E4469DA50A",
                keyData.getPublicKey());
        assertEquals(1, keyData.getProtocol().intValue());
        assertEquals(1, keyData.getFlags().intValue());
    }

    /**
     * test query non exist event
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup(type = DatabaseOperation.REFRESH, value = "secureDns.xml")
    public void testQueryNonExistEvent() {
        Long nonExistDomainId = 10000L;
        List<SecureDns> secureDnsList = secureDnsQueryDao.queryAsInnerObjects(
                nonExistDomainId, ModelType.DOMAIN);
        assertNotNull(secureDnsList);
        assertEquals(0, secureDnsList.size());
    }
}
