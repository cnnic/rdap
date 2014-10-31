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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.filters.QueryParser;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * NetworkRegistryHandlerTest.
 * 
 * @author jiashuo
 * 
 */
public class NetworkRegistryHandlerTest {
    /**
     * handler.
     */
    NetworkRegistryHandler handler = new NetworkV4RegistryHandler() {
        @Override
        String getRegistryRelativateUrl() {
            return null;
        }
    };

    @Test
    public void testGenerateRedirects_normal() {
        List<String> registryUrls = new ArrayList<String>();
        registryUrls.add("URL1");
        registryUrls.add("URL2");
        List<Redirect> redirects =
                handler.generateRedirects("1.0.0.0/8", registryUrls);
        assertNotNull(redirects);
        assertEquals(1, redirects.size());
        List<String> results = redirects.get(0).getUrls();
        assertEquals(2, results.size());
        assertThat(results, CoreMatchers.hasItems("URL1", "URL2"));
    }

    @Test
    public void testGenerateRedirects_empty_key() {
        List<String> registryUrls = new ArrayList<String>();
        registryUrls.add("URL1");
        registryUrls.add("URL2");
        // null.
        List<Redirect> redirects =
                handler.generateRedirects(null, registryUrls);
        assertNotNull(redirects);
        assertEquals(0, redirects.size());
        // "".
        redirects = handler.generateRedirects("", registryUrls);
        assertNotNull(redirects);
        assertEquals(0, redirects.size());
    }

    @Test
    public void testGenerateRedirects_all_urls_are_empty() {
        List<String> registryUrls = new ArrayList<String>();
        // all items are empty.
        registryUrls.add(null);
        registryUrls.add("");
        List<Redirect> redirects =
                handler.generateRedirects("1.0.0.0/8", registryUrls);
        assertNotNull(redirects);
        assertEquals(0, redirects.size());
        // list are null.
        registryUrls = null;
        redirects = handler.generateRedirects("1.0.0.0/8", registryUrls);
        assertNotNull(redirects);
        assertEquals(0, redirects.size());
    }

    @Test
    public void testGenerateRedirects_one_url_is_empty() {
        List<String> registryUrls = new ArrayList<String>();
        registryUrls.add(null);
        registryUrls.add("URL1");
        List<Redirect> redirects =
                handler.generateRedirects("1.0.0.0/8", registryUrls);
        assertEquals(1, redirects.size());
        List<String> results = redirects.get(0).getUrls();
        assertEquals(1, results.size());
        assertThat(results, CoreMatchers.hasItems("URL1"));
    }

    /**
     * init.
     */
    @Before
    public void init() {
        ReflectionTestUtils.setField(handler, "queryParser", new QueryParser());
    }

}
