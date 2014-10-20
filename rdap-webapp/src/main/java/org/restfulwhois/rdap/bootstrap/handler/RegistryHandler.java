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
package org.restfulwhois.rdap.bootstrap.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.bootstrap.bean.Bootstrap;
import org.restfulwhois.rdap.bootstrap.bean.BootstrapEntry;
import org.restfulwhois.rdap.bootstrap.bean.BootstrapRegistries;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.bootstrap.registry.DataProvider;
import org.restfulwhois.rdap.core.common.RdapProperties;
import org.restfulwhois.rdap.core.service.RedirectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 
 * This class is the base class to update registry.
 * 
 * <pre>
 *    Process flow:
 *      1. get data from registry, using URI from getRegistryUrl();
 *      2. parse data using generateRedirects();
 *      3. save redirects by calling saveRedirects().
 * </pre>
 * 
 * @author jiashuo
 * 
 */
public abstract class RegistryHandler {
    /**
     * logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * redirectService
     */
    @Autowired
    protected RedirectService redirectService;

    /**
     * redirectService
     */
    @Autowired
    @Qualifier("restDataProvider")
    private DataProvider dataProvider;

    /**
     * save redirect from bootstrap.
     * 
     */
    public void handle() {
        BootstrapRegistries bootstrapRegistries =
                dataProvider.getDataFromRegistry(getRegistryRelativateUrl());
        if (null == bootstrapRegistries) {
            logger.error("bootstrapRegistries is null,not do sync.");
            return;
        }
        List<Redirect> redirects =
                generateRedirectsFromBootstraps(bootstrapRegistries);
        logger.debug("generateRedirectsFromBootstraps,result:{}", redirects);
        saveRedirects(redirects);
    }

    /**
     * get registry URL.
     * 
     * @return URL.
     */
    abstract String getRegistryRelativateUrl();

    /**
     * save redirects.
     * 
     * @param redirects
     *            redirects.
     */
    abstract void saveRedirects(List<Redirect> redirects);

    /**
     * generate redirects from key/url.
     * 
     * @param key
     *            key.
     * @param registryUrls
     *            registryUrls.
     * @return redirect list.
     */
    abstract List<Redirect> generateRedirects(String key,
            List<String> registryUrls);

    /**
     * generate redirects from bootstraps.
     * 
     * @param bootstrapRegistries
     *            bootstrapRegistries.
     * @return redirect list.
     */
    private List<Redirect> generateRedirectsFromBootstraps(
            BootstrapRegistries bootstrapRegistries) {
        List<Redirect> redirects = new ArrayList<Redirect>();
        Bootstrap bootstrap = bootstrapRegistries.getBootstrap();
        List<BootstrapEntry> services = bootstrap.getServices();
        for (BootstrapEntry service : services) {
            List<String> keys = service.getKeys();
            List<String> registryUrls = service.getRegistryUrls();
            for (String key : keys) {
                redirects.addAll(generateRedirects(key, registryUrls));
            }
        }
        return redirects;
    }

    /**
     * get registry base URL.
     * 
     * @return registry base URL.
     */
    protected String getRegistryBaseUrl() {
        return RdapProperties.getBootstrapRegistryBaseUrl();
    }

    /**
     * remove empty registryUrls, and validate.
     * 
     * @param registryUrls
     *            registryUrls.
     * @return true if valid, false if not.
     */
    protected boolean removeEmptyUrlsAndValidate(List<String> registryUrls) {
        if (null == registryUrls) {
            return false;
        }
        for (Iterator<String> it = registryUrls.iterator(); it.hasNext();) {
            String url = it.next();
            if (StringUtils.isBlank(url)) {
                it.remove();
            }
        }
        if (registryUrls.size() == 0) {
            return false;
        }
        return true;
    }
}
