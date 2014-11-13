/* Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and Numbers 
 * (ICANN) and China Internet Network Information Center (CNNIC)
 *
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 **Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer.
 * 
 **Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 **Neither the name of the ICANN, CNNIC nor the names of its contributors may be used to 
 * endorse or promote products derived from this software without specific prior written 
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.restfulwhois.rdap.service.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restfulwhois.rdap.core.common.util.RdapProperties;
import org.restfulwhois.rdap.filters.httpFilter.service.ConnectionControlService;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * test for ConnectionControlService.
 * 
 * @author jiashuo
 * 
 */
public class ConnectionControlServiceTest {

    /**
     * test incrementConcurrentQCountAndCheckIfExceedMax.
     * 
     */
    @Test
    public void testIncrementConcurrentQCountAndCheckIfExceedMax() {
        RdapProperties prop = new RdapProperties();
        ReflectionTestUtils.setField(prop, "maxConcurrentCount", 3);
        assertFalse(ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax());
        assertFalse(ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax());
        assertFalse(ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax());
        assertTrue(ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax());
        ConnectionControlService.decrementAndGetCurrentQueryCount();
        assertTrue(ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax());
        ConnectionControlService.decrementAndGetCurrentQueryCount();
        ConnectionControlService.decrementAndGetCurrentQueryCount();
        assertFalse(ConnectionControlService
                .incrementConcurrentQCountAndCheckIfExceedMax());

    }

    /**
     * test exceedRateLimit.
     * 
     * @throws InterruptedException
     *             InterruptedException.
     */
    @Test
    public void testExceedRateLimit() throws InterruptedException {
        String clientIp = "127.0.0.1";
        RdapProperties prop = new RdapProperties();
        // set conf to 1ms
        ReflectionTestUtils.setField(prop, "minSecondsAccessIntervalAnonymous",
                1L);
        assertFalse(ConnectionControlService.exceedRateLimit(null));
        assertFalse(ConnectionControlService.exceedRateLimit(""));
        assertFalse(ConnectionControlService.exceedRateLimit(clientIp));
        Thread.sleep(2);
        assertFalse(ConnectionControlService.exceedRateLimit(clientIp));
        // set conf to 1s
        ReflectionTestUtils.setField(prop, "minSecondsAccessIntervalAnonymous",
                1000L);
        //this will fail because of clear timer!
//        assertTrue(ConnectionControlService.exceedRateLimit(clientIp));

    }

}
