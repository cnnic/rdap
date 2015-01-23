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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.DomainDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Domain.DomainType;
import org.restfulwhois.rdap.common.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author jiashuo
 * 
 */
public class DomainUpdateDaoTest extends BaseTest {

    public static final String TABLE_RDAP_DOMAIN = "RDAP_DOMAIN";

    @Autowired
    private UpdateDao<Domain, DomainDto> updateDao;

    @Test
    @DatabaseSetup("teardown.xml")
    @DatabaseTearDown("teardown.xml")
    @ExpectedDatabase(
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "classpath:/org/restfulwhois/rdap/dao/impl/domain-update.xml")
    public
            void test_save_domain_and_status() throws Exception {
        Domain domain = new Domain();
        domain.setHandle("h1");
        domain.setLdhName("cnnic.cn");
        domain.setUnicodeName("cnnic.cn");
        domain.setPort43("port43");
        domain.setLang("zh");
        domain.setType(DomainType.DNR);
        List<String> status = new ArrayList<String>();
        status.add("validated");
        status.add("update prohibited");
        domain.setStatus(status);
        Map<String, String> customProperties = new HashMap<String, String>();
        customProperties.put("customKey1", "customValue1");
        customProperties.put("customKey2", "customValue2");
        domain.setCustomProperties(customProperties);
        domain.setCustomPropertiesJsonVal(JsonUtil
                .serializeMap(customProperties));
        updateDao.save(domain);
        updateDao.saveStatus(domain);
    }

    @Test
    @DatabaseSetup("domain-delete.xml")
    @DatabaseTearDown("teardown.xml")
    @ExpectedDatabase(
            assertionMode = DatabaseAssertionMode.NON_STRICT,
            value = "classpath:/org/restfulwhois/rdap/dao/impl/domain-empty.xml")
    public
            void test_delete_domain_and_status() throws Exception {
        Domain domain = new Domain();
        domain.setId(1L);
        updateDao.delete(domain);
        updateDao.deleteStatus(domain);
    }

}
