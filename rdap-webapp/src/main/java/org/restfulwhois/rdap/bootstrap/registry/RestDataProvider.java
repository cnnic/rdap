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
package org.restfulwhois.rdap.bootstrap.registry;

import org.restfulwhois.rdap.bootstrap.bean.BootstrapRegistries;
import org.restfulwhois.rdap.core.common.RdapProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Get registry data from restful service.
 * 
 * @author jiashuo
 * 
 */
@Component
public class RestDataProvider implements DataProvider {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(RestDataProvider.class);

    @Override
    public BootstrapRegistries getDataFromRegistry(String relativateUrl) {
        String registryUrl =
                RdapProperties.getBootstrapRegistryBaseUrl() + relativateUrl;
        LOGGER.info("getDataFromRegistry, registryUrl:{}", registryUrl);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<BootstrapRegistries> response =
                    restTemplate.getForEntity(registryUrl,
                            BootstrapRegistries.class);
            return response.getBody();
        } catch (Exception e) {
            LOGGER.info("restTemplate error:{}", e);
            return null;
        }
    }

}
