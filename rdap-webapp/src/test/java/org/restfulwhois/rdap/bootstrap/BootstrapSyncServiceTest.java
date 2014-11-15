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
package org.restfulwhois.rdap.bootstrap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.QueryParamHelper;
import org.restfulwhois.rdap.bootstrap.bean.Bootstrap;
import org.restfulwhois.rdap.bootstrap.bean.BootstrapEntry;
import org.restfulwhois.rdap.bootstrap.bean.BootstrapRegistries;
import org.restfulwhois.rdap.bootstrap.bean.DomainRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.bootstrap.handler.AutnumRegistryHandler;
import org.restfulwhois.rdap.bootstrap.handler.DomainRegistryHandler;
import org.restfulwhois.rdap.bootstrap.handler.NetworkV4RegistryHandler;
import org.restfulwhois.rdap.bootstrap.handler.NetworkV6RegistryHandler;
import org.restfulwhois.rdap.bootstrap.registry.DataProvider;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.domain.queryparam.DomainQueryParam;
import org.restfulwhois.rdap.redirect.bean.RedirectResponse;
import org.restfulwhois.rdap.redirect.dao.impl.AutnumRedirectDao;
import org.restfulwhois.rdap.redirect.dao.impl.DomainRedirectDao;
import org.restfulwhois.rdap.redirect.dao.impl.NetworkRedirectDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;

/**
 * BootstrapSyncServiceTest.
 * 
 * @author jiashuo
 * 
 */
public class BootstrapSyncServiceTest extends BaseTest {

    /**
     * domainRedirectDao.
     */
    @Autowired
    private DomainRedirectDao domainRedirectDao;
    /**
     * domainRedirectDao.
     */
    @Autowired
    private NetworkRedirectDao networkRedirectDao;

    /**
     * autnumRedirectDao.
     */
    @Autowired
    private AutnumRedirectDao autnumRedirectDao;

    /**
     * syncRedirectDataService.
     */
    @Autowired
    private BootstrapSyncService syncRedirectDataService;

    /**
     * domainRegistryHandler.
     */
    @Autowired
    private DomainRegistryHandler domainRegistryHandler;
    /**
     * NetworkV4RegistryHandler.
     */
    @Autowired
    private NetworkV4RegistryHandler networkV4RegistryHandler;
    /**
     * networkV6RegistryHandler.
     */
    @Autowired
    private NetworkV6RegistryHandler networkV6RegistryHandler;
    /**
     * AutnumRegistryHandler.
     */
    @Autowired
    private AutnumRegistryHandler autnumRegistryHandler;

    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:"
            + "org/restfulwhois/rdap/dao/impl/domain-redirect-sync.xml")
    public void testSync_domain() {
        beforeAssertDomain();
        syncRedirectDataService.syncAllRegistry();
        afterAssertDomainResult();
    }

    /**
     * testSync_network_v4.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testSync_network_v4() {
        databaseSetupWithBinaryColumns("network-redirect-v4-sync.xml");
        assertNetworkV4("http://cnnic.cn/rdap");
        syncRedirectDataService.syncAllRegistry();
        assertNetworkV4("REDIRECT_URL_1_UPDATED_1");
    }

    /**
     * testSync_network_v6.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    public void testSync_network_v6() {
        databaseSetupWithBinaryColumns("network-redirect-v6-sync.xml");
        assertNetworkV6("http://cnnic.cn/rdap");
        syncRedirectDataService.syncAllRegistry();
        assertNetworkV6("REDIRECT_URL_1_UPDATED_1");
    }

    /**
     * testSync_autnum.
     */
    @Test
    @DatabaseTearDown("classpath:org/restfulwhois/rdap/dao/impl/teardown.xml")
    @DatabaseSetup("classpath:"
            + "org/restfulwhois/rdap/dao/impl/autnum-redirect-sync.xml")
    public void testSync_autnum() {
        assertAutnum("http://cnnic.cn/rdap");
        syncRedirectDataService.syncAllRegistry();
        assertAutnum("REDIRECT_URL_1_UPDATED_1");
    }

    /**
     * assertAutnum.
     * 
     * @param expectUrl
     *            expectUrl.
     */
    private void assertAutnum(String expectUrl) {
        QueryParam queryParam = QueryParamHelper.buildQueryParam("1");
        RedirectResponse redirectResponse = autnumRedirectDao.query(queryParam);
        assertNotNull(redirectResponse);
        assertEquals(expectUrl, redirectResponse.getUrl());
    }

    /**
     * assertNetworkV6.
     * 
     * @param expectUrl
     *            expectUrl.
     */
    private void assertNetworkV6(String expectUrl) {
        QueryParam queryParam =
                QueryParamHelper
                        .generateNetworkParam("0:0:0:0:2001:6a8:0:0/112");
        RedirectResponse redirectResponse =
                networkRedirectDao.query(queryParam);
        assertNotNull(redirectResponse);
        assertEquals(expectUrl, redirectResponse.getUrl());
    }

    /**
     * assertNetworkV4.
     * 
     * @param expectUrl
     *            expectUrl.
     */
    private void assertNetworkV4(String expectUrl) {
        QueryParam queryParam =
                QueryParamHelper.generateNetworkParam("1.0.0.0/8");
        RedirectResponse redirectResponse =
                networkRedirectDao.query(queryParam);
        assertNotNull(redirectResponse);
        assertEquals(expectUrl, redirectResponse.getUrl());
    }

    /**
     * initBootstrapDataProvider.
     */
    @Before
    public void initBootstrapDataProvider() {
        DataProvider domain = getMockDataProviderForDomain();
        ReflectionTestUtils.setField(domainRegistryHandler, "dataProvider",
                domain);
        DataProvider networkV4 = getMockDataProviderForV4();
        ReflectionTestUtils.setField(networkV4RegistryHandler, "dataProvider",
                networkV4);
        DataProvider networkV6 = getMockDataProviderForV6();
        ReflectionTestUtils.setField(networkV6RegistryHandler, "dataProvider",
                networkV6);
        DataProvider autnum = getMockDataProviderForAutnum();
        ReflectionTestUtils.setField(autnumRegistryHandler, "dataProvider",
                autnum);
    }

    /**
     * getMockDataProviderForAutnum.
     * 
     * @return DataProvider.
     */
    private DataProvider getMockDataProviderForAutnum() {
        return new DataProvider() {
            @Override
            public BootstrapRegistries
                    getDataFromRegistry(String relativateUrl) {
                BootstrapRegistries bootstrap =
                        BootstrapRegistriesBuilder.build();
                BootstrapEntry entry =
                        BootstrapRegistriesBuilder.appendEntry(bootstrap);
                BootstrapRegistriesBuilder.appendKey(entry, "1,100");
                BootstrapRegistriesBuilder.appendRegistryUrl(entry,
                        "REDIRECT_URL_1_UPDATED_1");
                BootstrapRegistriesBuilder.appendRegistryUrl(entry,
                        "REDIRECT_URL_1_UPDATED_2");
                return bootstrap;
            }
        };
    }

    /**
     * get mock DataProvider for domain.
     * 
     * @return DataProvider.
     */
    private DataProvider getMockDataProviderForDomain() {
        return new DataProvider() {
            @Override
            public BootstrapRegistries
                    getDataFromRegistry(String relativateUrl) {
                BootstrapRegistries bootstrap =
                        BootstrapRegistriesBuilder.build();
                BootstrapEntry entry =
                        BootstrapRegistriesBuilder.appendEntry(bootstrap);
                BootstrapRegistriesBuilder.appendKey(entry, "cn");
                BootstrapRegistriesBuilder.appendRegistryUrl(entry,
                        "REDIRECT_URL_1_UPDATED_1");
                BootstrapRegistriesBuilder.appendRegistryUrl(entry,
                        "REDIRECT_URL_1_UPDATED_2");
                return bootstrap;
            }
        };
    }

    /**
     * get mock DataProvider for v4.
     * 
     * @return DataProvider.
     */
    private DataProvider getMockDataProviderForV4() {
        return new DataProvider() {
            @Override
            public BootstrapRegistries
                    getDataFromRegistry(String relativateUrl) {
                BootstrapRegistries bootstrap =
                        BootstrapRegistriesBuilder.build();
                BootstrapEntry networkV4 =
                        BootstrapRegistriesBuilder.appendEntry(bootstrap);
                BootstrapRegistriesBuilder.appendKey(networkV4, "1.0.0.0/8");
                BootstrapRegistriesBuilder.appendRegistryUrl(networkV4,
                        "REDIRECT_URL_1_UPDATED_1");
                BootstrapRegistriesBuilder.appendRegistryUrl(networkV4,
                        "REDIRECT_URL_1_UPDATED_2");
                return bootstrap;
            }
        };
    }

    /**
     * get mock DataProvider for v6.
     * 
     * @return DataProvider.
     */
    private DataProvider getMockDataProviderForV6() {
        return new DataProvider() {
            @Override
            public BootstrapRegistries
                    getDataFromRegistry(String relativateUrl) {
                BootstrapRegistries bootstrap =
                        BootstrapRegistriesBuilder.build();
                BootstrapEntry networkV6 =
                        BootstrapRegistriesBuilder.appendEntry(bootstrap);
                BootstrapRegistriesBuilder.appendKey(networkV6,
                        "0:0:0:0:2001:6a8::/32");
                BootstrapRegistriesBuilder.appendRegistryUrl(networkV6,
                        "REDIRECT_URL_1_UPDATED_1");
                BootstrapRegistriesBuilder.appendRegistryUrl(networkV6,
                        "REDIRECT_URL_1_UPDATED_2");
                return bootstrap;
            }
        };
    }

    /**
     * assert after test.
     */
    private void afterAssertDomainResult() {
        QueryParam parseDomainQueryParam =
                DomainQueryParam.generateQueryParam("cnnic.cn", "cnnic.cn");
        // sync
        List<Redirect> bootstraps = new ArrayList<Redirect>();
        List<String> urls = new ArrayList<String>();
        String newUrl = "REDIRECT_URL_1_UPDATED_1";
        urls.add(newUrl);
        urls.add("REDIRECT_URL_1_UPDATED_2");
        DomainRedirect redirect = new DomainRedirect("cn", urls);
        bootstraps.add(redirect);
        domainRedirectDao.save(bootstraps);
        RedirectResponse redirectResponse =
                domainRedirectDao.query(parseDomainQueryParam);
        assertNotNull(redirectResponse);
        assertEquals(newUrl, redirectResponse.getUrl());
    }

    /**
     * assert before test.
     */
    private void beforeAssertDomain() {
        QueryParam parseDomainQueryParam =
                DomainQueryParam.generateQueryParam("cnnic.cn", "cnnic.cn");
        RedirectResponse redirectResponse =
                domainRedirectDao.query(parseDomainQueryParam);
        assertNotNull(redirectResponse);
        assertEquals("REDIRECT_URL_1", redirectResponse.getUrl());
    }

}

/**
 * This class is used to build BootstrapRegistries.
 * 
 * @author jiashuo
 * 
 */
class BootstrapRegistriesBuilder {
    /**
     * build BootstrapRegistries..
     * 
     * @return BootstrapRegistries.
     */
    public static BootstrapRegistries build() {
        BootstrapRegistries result = new BootstrapRegistries();
        Bootstrap bootstrap = new Bootstrap();
        result.setBootstrap(bootstrap);
        List<BootstrapEntry> services = new ArrayList<BootstrapEntry>();
        bootstrap.setServices(services);
        return result;
    }

    /**
     * append Entry to bootstrap.
     * 
     * @param bootstrap
     *            bootstrap.
     * @return BootstrapEntry.
     */
    public static BootstrapEntry appendEntry(BootstrapRegistries bootstrap) {
        BootstrapEntry entry = new BootstrapEntry();
        List<String> keys = new ArrayList<String>();
        List<String> registryUrls = new ArrayList<String>();
        entry.setKeys(keys);
        entry.setRegistryUrls(registryUrls);
        bootstrap.getBootstrap().getServices().add(entry);
        return entry;
    }

    /**
     * append key to entry.
     * 
     * @param entry
     *            entry.
     * @param key
     *            key.
     */
    public static void appendKey(BootstrapEntry entry, String key) {
        entry.getKeys().add(key);
    }

    /**
     * append registry URL to entry.
     * 
     * @param entry
     *            entry.
     * @param registryUrl
     *            registryUrl.
     */
    public static void appendRegistryUrl(BootstrapEntry entry,
            String registryUrl) {
        entry.getRegistryUrls().add(registryUrl);
    }
}