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

import java.util.ArrayList;
import java.util.List;

import org.dbunit.database.AmbiguousTableNameException;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.bootstrap.bean.NetworkRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.core.ip.queryparam.NetworkQueryParam;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
import org.restfulwhois.rdap.redirect.dao.RedirectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * Test for network redirect DAO.
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class NetworkRedirectDaoForBootstrapTest extends BaseTest {
    /**
     * domainRedirectDao.
     */
    @Autowired
    @Qualifier("networkRedirectDao")
    private RedirectDao redirectDao;

    /**
     * testSync_v4.
     * 
     * @throws DataSetException
     *             DataSetException.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    public void testSync_v4() throws DataSetException {
        super.databaseSetupWithBinaryColumns("network-redirect-v4-sync.xml");
        IpVersion versionV4 = IpVersion.V4;
        QueryParam queryParam =
                NetworkQueryParam.generateQueryParam("1.0.0.0/8");
        RedirectResponse redirectResponse = redirectDao.query(queryParam);
        assertNotNull(redirectResponse);
        assertEquals("http://cnnic.cn/rdap", redirectResponse.getUrl());
        // check total before sync.
        checkTotalCount(2);
        // sync
        List<Redirect> bootstraps = new ArrayList<Redirect>();
        List<String> urls = new ArrayList<String>();
        String newUrl = "REDIRECT_URL_1_UPDATED_1";
        urls.add(newUrl);
        urls.add("REDIRECT_URL_1_UPDATED_2");
        NetworkRedirect redirect = new NetworkRedirect(urls);
        NetworkQueryParam networkQueryParam =
                NetworkQueryParam.generateQueryParam("1.0.0.0/8");
        redirect.setNetworkQueryParam(networkQueryParam);
        bootstraps.add(redirect);
        redirectDao.save(bootstraps);
        redirectResponse = redirectDao.query(queryParam);
        assertNotNull(redirectResponse);
        assertEquals(newUrl, redirectResponse.getUrl());
        // check total after sync.
        checkTotalCount(1);
    }

    /**
     * check total count.
     * 
     * @param expectCount
     *            expectCount.
     * @throws AmbiguousTableNameException
     *             AmbiguousTableNameException.
     * @throws DataSetException
     *             DataSetException.
     */
    private void checkTotalCount(int expectCount)
            throws AmbiguousTableNameException, DataSetException {
        QueryDataSet actual = getEmptyDataSet();
        actual.addTable("RDAP_IP_REDIRECT", "select * from RDAP_IP_REDIRECT ");
        ITable table = actual.getTable("RDAP_IP_REDIRECT");
        assertEquals(expectCount, table.getRowCount());
    }

    /**
     * testSync_v6.
     * 
     * @throws DataSetException
     *             DataSetException.
     */
    @Test
    @DatabaseTearDown("teardown.xml")
    public void testSync_v6() throws DataSetException {
        super.databaseSetupWithBinaryColumns("network-redirect-v6-sync.xml");
        IpVersion versionV6 = IpVersion.V6;
        QueryParam queryParam =
                NetworkQueryParam.generateQueryParam("0:0:0:0:2001:6a8:0:1/96");
        RedirectResponse redirectResponse = redirectDao.query(queryParam);
        assertNotNull(redirectResponse);
        assertEquals("http://cnnic.cn/rdap", redirectResponse.getUrl());
        // check total before sync.
        checkTotalCount(2);
        // sync
        List<Redirect> bootstraps = new ArrayList<Redirect>();
        List<String> urls = new ArrayList<String>();
        String newUrl = "REDIRECT_URL_1_UPDATED_1";
        urls.add(newUrl);
        urls.add("REDIRECT_URL_1_UPDATED_2");
        NetworkRedirect redirect = new NetworkRedirect(urls);
        NetworkQueryParam networkQueryParam =
                NetworkQueryParam.generateQueryParam("0:0:0:0:2001:6a8::/96");
        redirect.setNetworkQueryParam(networkQueryParam);
        bootstraps.add(redirect);
        redirectDao.save(bootstraps);
        redirectResponse = redirectDao.query(queryParam);
        assertNotNull(redirectResponse);
        assertEquals(newUrl, redirectResponse.getUrl());
        // check total after sync.
        checkTotalCount(1);
    }

}
