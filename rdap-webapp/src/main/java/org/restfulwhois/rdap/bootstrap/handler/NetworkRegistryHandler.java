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
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.bean.Network.IpVersion;
import org.restfulwhois.rdap.bean.NetworkQueryParam;
import org.restfulwhois.rdap.bean.QueryParam;
import org.restfulwhois.rdap.bootstrap.bean.NetworkRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.controller.support.QueryParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This abstract class is used to Update network registry.
 * 
 * @author jiashuo
 * 
 */
@Component
public abstract class NetworkRegistryHandler extends RegistryHandler {
    /**
     * query parser.
     */
    @Autowired
    private QueryParser queryParser;

    /**
     * CIDR separator.
     */
    private static final String CIDR_SEPARATOR = "/";

    @Override
    void saveRedirects(List<Redirect> redirects) {
        redirectService.saveNetworkRedirect(redirects);
    }

    @Override
    List<Redirect> generateRedirects(String key, List<String> registryUrls) {
        List<Redirect> redirects = new ArrayList<Redirect>();
        if (!validateKey(key) || !removeEmptyUrlsAndValidate(registryUrls)) {
            return redirects;
        }
        key = StringUtils.lowerCase(key);
        String[] splits = StringUtils.split(key, CIDR_SEPARATOR);
        if (splits.length != 2) {
            return redirects;
        }
        String ipPrefix = splits[0];
        String ipMask = splits[1];
        long ipMaskLongVal = 0;
        try {
            ipMaskLongVal = Long.parseLong(ipMask);
        } catch (Exception e) {
            return redirects;
        }
        NetworkRedirect networkRedirect =
                new NetworkRedirect(ipPrefix, ipMask, registryUrls);
        IpVersion ipVersion = parseIpVersion(ipPrefix);
        if (null == ipVersion) {
            return redirects;
        }
        QueryParam queryParam =
                queryParser.parseIpQueryParam(ipPrefix, ipMaskLongVal,
                        ipVersion);
        networkRedirect.setNetworkQueryParam((NetworkQueryParam) queryParam);
        redirects.add(networkRedirect);
        return redirects;
    }

    /**
     * validate key.
     * 
     * @param key
     *            key.
     * @return true if valid, false if not.
     */
    private boolean validateKey(String key) {
        if (StringUtils.isBlank(key)
                || !StringUtils.contains(key, CIDR_SEPARATOR)) {
            return false;
        }
        return true;
    }

    /**
     * parse IP version.
     * 
     * @param ipPrefix
     *            ipPrefix.
     * @return IpVersion.
     */
    private IpVersion parseIpVersion(String ipPrefix) {
        IpVersion ipVersion = IpVersion.V6;
        boolean isV4 = IpUtil.isIpV4StrWholeValid(ipPrefix);
        boolean isV6 = IpUtil.isIpV6StrValid(ipPrefix);
        if (!isV4 && !isV6) {
            return null;
        }
        if (isV4) {
            ipVersion = IpVersion.V4;
        } else if (isV6) {
            ipVersion = IpVersion.V6;
        }
        return ipVersion;
    }

}
