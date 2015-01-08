/* Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and
 * Numbers (ICANN) and China Internet Network Information Center (CNNIC)
 *
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 **Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 **Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 **Neither the name of the ICANN, CNNIC nor the names of its contributors 
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.restfulwhois.rdap.core.ip.queryparam;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.restfulwhois.rdap.common.model.IpVersion;
import org.restfulwhois.rdap.common.model.base.QueryUri;
import org.restfulwhois.rdap.common.support.QueryParam;
import org.restfulwhois.rdap.common.util.IpUtil;
import org.restfulwhois.rdap.common.util.NetworkInBytes;
import org.restfulwhois.rdap.core.ip.validator.NetworkQueryValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * network query parameter bean.
 * 
 * @author jiashuo
 * @author weijunkai
 * 
 */
public class NetworkQueryParam extends QueryParam {

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(NetworkQueryParam.class);

    /**
     * networkInBytes.
     */
    private NetworkInBytes networkInBytes;

    /**
     * constructor.
     * 
     * @param request
     *            request.
     * @param cidr
     *            cidr.
     */
    public NetworkQueryParam(HttpServletRequest request, String cidr) {
        super(request);
        super.setOriginalQ(cidr);
        super.setQ(cidr);
    }

    /**
     * constructor.
     * 
     * @param cidr
     *            q.
     */
    public NetworkQueryParam(String cidr) {
        super(cidr);
        super.setOriginalQ(cidr);
    }

    /**
     * generateQueryParam.
     * 
     * @param cidr
     *            cidr.
     * @return QueryParam.
     */
    public static NetworkQueryParam generateQueryParam(String cidr) {
        NetworkQueryParam queryParam = new NetworkQueryParam(cidr);
        try {
            queryParam.fillParam();
        } catch (Exception e) {
            LOGGER.error("parseIpQueryParam error:{}", e);
            return null;
        }
        return queryParam;
    }

    @Override
    protected void initValidators() {
        super.addValidator(new NetworkQueryValidator());
    }

    @Override
    public void fillParam() throws Exception {
        this.parseFromNetworkStr(getQ());
    }

    @Override
    public void convertParam() throws Exception {
    }

    /**
     * parseFromNetworkStr.
     * 
     * @param networkStr
     *            networkStr.
     */
    private void parseFromNetworkStr(String networkStr) {
        IpVersion ipVersion = IpUtil.getIpVersionOfNetwork(networkStr);
        networkInBytes = IpUtil.parseNetwork(networkStr, ipVersion);
    }

    /**
     * get networkInBytes.
     * 
     * @return networkInBytes.
     */
    public NetworkInBytes getNetworkInBytes() {
        return networkInBytes;
    }

    /**
     * set networkInBytes.
     * 
     * @param networkInBytes
     *            networkInBytes.
     */
    public void setNetworkInBytes(NetworkInBytes networkInBytes) {
        this.networkInBytes = networkInBytes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(getQ()).toString();
    }

    @Override
    public QueryUri getQueryUri() {
        return QueryUri.IP;
    }

}
