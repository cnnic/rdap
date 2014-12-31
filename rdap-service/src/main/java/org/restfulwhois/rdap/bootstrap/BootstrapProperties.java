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

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * The properties in this class is read from property file.
 * <p>
 * Variables in this class is set by spring, from file 'bootstrap.properties'.
 * <p>
 * Bean 'BootstrapProperties' MUST configured in spring configuration file,and now
 * configuration file is load by {@link PropertyPlaceholderConfigurer}.
 * <p>
 * If property is changed, system need restart to reload it.
 * 
 * @author jiashuo
 * 
 */
public class BootstrapProperties {

    /**
     * bootstrapRegistryBaseUrl.
     */
    private static String bootstrapRegistryBaseUrl;

    /**
     * bootstrapRegistryUriForDomain.
     */
    private static String bootstrapRegistryUriForDomain;
    /**
     * bootstrapRegistryUriForAs.
     */
    private static String bootstrapRegistryUriForAs;
    /**
     * bootstrapRegistryUriForIpv4.
     */
    private static String bootstrapRegistryUriForIpv4;
    /**
     * bootstrapRegistryUriForIpv6.
     */
    private static String bootstrapRegistryUriForIpv6;

    /**
     * get bootstrapRegistryBaseUrl.
     * 
     * @return bootstrapRegistryBaseUrl.
     */
    public static String getBootstrapRegistryBaseUrl() {
        return bootstrapRegistryBaseUrl;
    }

    /**
     * set bootstrapRegistryBaseUrl.
     * 
     * @param bootstrapRegistryBaseUrl
     *            bootstrapRegistryBaseUrl.
     */
    public void setBootstrapRegistryBaseUrl(String bootstrapRegistryBaseUrl) {
        BootstrapProperties.bootstrapRegistryBaseUrl = bootstrapRegistryBaseUrl;
    }

    /**
     * get bootstrapRegistryUriForDomain.
     * 
     * @return bootstrapRegistryUriForDomain.
     */
    public static String getBootstrapRegistryUriForDomain() {
        return bootstrapRegistryUriForDomain;
    }

    /**
     * set bootstrapRegistryUriForDomain.
     * 
     * @param bootstrapRegistryUriForDomain
     *            bootstrapRegistryUriForDomain.
     */
    public static void setBootstrapRegistryUriForDomain(
            String bootstrapRegistryUriForDomain) {
        BootstrapProperties.bootstrapRegistryUriForDomain =
                bootstrapRegistryUriForDomain;
    }

    /**
     * get bootstrapRegistryUriForAs.
     * 
     * @return bootstrapRegistryUriForAs.
     */
    public static String getBootstrapRegistryUriForAs() {
        return bootstrapRegistryUriForAs;
    }

    /**
     * set bootstrapRegistryUriForAs.
     * 
     * @param bootstrapRegistryUriForAs
     *            bootstrapRegistryUriForAs.
     */
    public static void setBootstrapRegistryUriForAs(
            String bootstrapRegistryUriForAs) {
        BootstrapProperties.bootstrapRegistryUriForAs =
                bootstrapRegistryUriForAs;
    }

    /**
     * get bootstrapRegistryUriForIpv4.
     * 
     * @return bootstrapRegistryUriForIpv4.
     */
    public static String getBootstrapRegistryUriForIpv4() {
        return bootstrapRegistryUriForIpv4;
    }

    /**
     * set bootstrapRegistryUriForIpv4.
     * 
     * @param bootstrapRegistryUriForIpv4
     *            bootstrapRegistryUriForIpv4.
     */
    public static void setBootstrapRegistryUriForIpv4(
            String bootstrapRegistryUriForIpv4) {
        BootstrapProperties.bootstrapRegistryUriForIpv4 =
                bootstrapRegistryUriForIpv4;
    }

    /**
     * get bootstrapRegistryUriForIpv6.
     * 
     * @return bootstrapRegistryUriForIpv6.
     */
    public static String getBootstrapRegistryUriForIpv6() {
        return bootstrapRegistryUriForIpv6;
    }

    /**
     * set bootstrapRegistryUriForIpv6.
     * 
     * @param bootstrapRegistryUriForIpv6
     *            bootstrapRegistryUriForIpv6.
     */
    public static void setBootstrapRegistryUriForIpv6(
            String bootstrapRegistryUriForIpv6) {
        BootstrapProperties.bootstrapRegistryUriForIpv6 =
                bootstrapRegistryUriForIpv6;
    }

}
