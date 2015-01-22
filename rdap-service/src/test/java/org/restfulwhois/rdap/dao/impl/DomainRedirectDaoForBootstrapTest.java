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
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.bootstrap.bean.DomainRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
import org.restfulwhois.rdap.redirect.dao.RedirectDao;
import org.restfulwhois.rdap.redirect.dao.impl.DomainRedirectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for domain redirect DAO for sync bootstrap.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class DomainRedirectDaoForBootstrapTest extends BaseTest {
    /**
     * domainRedirectDao.
     */
    @Autowired
    @Qualifier("domainRedirectDao")
    private RedirectDao redirectDao;

    /**
     * domainRedirectDao.
     */
    @Autowired
    private DomainRedirectDao domainRedirectDao;

    /**
     * testSync_1add_2delete.
     * 
     * @throws DataSetException
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("domain-redirect-sync.xml")
    public void testSync_1add_2delete() throws DataSetException {
        QueryParam parseDomainQueryParam =
                DomainQueryParam.generateQueryParam("cnnic.cn", "cnnic.cn");
        // before sync
        RedirectResponse redirectResponse =
                redirectDao.query(parseDomainQueryParam);
        assertNotNull(redirectResponse);
        assertEquals("REDIRECT_URL_1", redirectResponse.getUrl());
        // sync
        List<Redirect> bootstraps = new ArrayList<Redirect>();
        List<String> urls = new ArrayList<String>();
        String newUrl = "REDIRECT_URL_1_UPDATED_1";
        urls.add(newUrl);
        urls.add("REDIRECT_URL_1_UPDATED_2");
        DomainRedirect redirect = new DomainRedirect("cn", urls);
        bootstraps.add(redirect);
        redirectDao.save(bootstraps);
        QueryDataSet actual = getEmptyDataSet();
        actual.addTable("RDAP_DOMAIN_REDIRECT",
                "select REDIRECT_TLD from RDAP_DOMAIN_REDIRECT ");
        ITable table = actual.getTable("RDAP_DOMAIN_REDIRECT");
        assertEquals(1, table.getRowCount());
        List<String> allTlds = new ArrayList<String>();
        allTlds.add(table.getValue(0, "REDIRECT_TLD").toString());
        assertThat(allTlds, CoreMatchers.hasItems("cn"));
        assertThat(allTlds, CoreMatchers.not(CoreMatchers.hasItems("com")));
        assertThat(allTlds, CoreMatchers.not(CoreMatchers.hasItems("com.cn")));
        redirectResponse = redirectDao.query(parseDomainQueryParam);
        assertNotNull(redirectResponse);
        assertEquals(newUrl, redirectResponse.getUrl());
    }

    /**
     * testSync_2add_2delete.
     * 
     * @throws DataSetException
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    @DatabaseSetup("domain-redirect-sync.xml")
    public void testSync_2add_2delete() throws DataSetException {
        QueryParam parseDomainQueryParam =
                DomainQueryParam.generateQueryParam("cnnic.cn", "cnnic.cn");
        // before sync
        RedirectResponse redirectResponse =
                redirectDao.query(parseDomainQueryParam);
        assertNotNull(redirectResponse);
        assertEquals("REDIRECT_URL_1", redirectResponse.getUrl());
        // sync
        List<Redirect> bootstraps = new ArrayList<Redirect>();
        List<String> urls = new ArrayList<String>();
        String newUrl = "REDIRECT_URL_1_UPDATED_1";
        urls.add(newUrl);
        urls.add("REDIRECT_URL_1_UPDATED_2");
        DomainRedirect redirect = new DomainRedirect("cn", urls);
        DomainRedirect redirectComCn = new DomainRedirect("com.cn", urls);
        bootstraps.add(redirect);
        bootstraps.add(redirectComCn);
        redirectDao.save(bootstraps);

        QueryDataSet actual = getEmptyDataSet();
        actual.addTable("RDAP_DOMAIN_REDIRECT",
                "select REDIRECT_TLD from RDAP_DOMAIN_REDIRECT ");
        ITable table = actual.getTable("RDAP_DOMAIN_REDIRECT");
        assertEquals(2, table.getRowCount());
        List<String> allTlds = new ArrayList<String>();
        allTlds.add(table.getValue(0, "REDIRECT_TLD").toString());
        allTlds.add(table.getValue(1, "REDIRECT_TLD").toString());
        assertThat(allTlds, CoreMatchers.hasItems("cn"));
        assertThat(allTlds, CoreMatchers.hasItems("com.cn"));

        redirectResponse = redirectDao.query(parseDomainQueryParam);
        assertNotNull(redirectResponse);
        assertEquals(newUrl, redirectResponse.getUrl());
    }

}
