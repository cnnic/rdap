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
import cn.cnnic.rdap.bean.Entity;
import cn.cnnic.rdap.bean.EntityAddress;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for entity address DAO.
 *
 * @author jiashuo
 *
 */
@SuppressWarnings("rawtypes")
public class EntityAddressDaoTest extends BaseTest {
    /**
     * entityAddressDao.
     */
    @Autowired
    private EntityAddressDao entityAddressDao;

    /**
     * test query exist.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("entityAddress.xml")
    public void testQueryExist() {
        Entity entity = new Entity();
        entity.setId(1L);
        List<EntityAddress> addresses = entityAddressDao.query(entity);
        assertNotNull(addresses);
        assertEquals(addresses.size(), 2);
        EntityAddress address = addresses.get(0);
        assertEquals("home;work", address.getTypes());
        assertEquals("post office Box", address.getPoBox());
        assertEquals("apartment addr", address.getExtendedAddress());
        assertEquals("123 Wall St.", address.getStreetAddress());
        assertEquals("New York", address.getLocality());
        assertEquals("NY", address.getRegion());
        assertEquals("12345", address.getPostalCode());
        assertEquals("USA", address.getCountry());
        assertEquals(Integer.valueOf(1), address.getPref());
    }

    /**
     * test query non exist.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("entityAddress.xml")
    public void testQueryNotExist() {
        Entity entity = new Entity();
        entity.setId(100000L);
        List<EntityAddress> addresses = entityAddressDao.query(entity);
        assertNotNull(addresses);
        assertEquals(addresses.size(), 0);
    }

}
