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
package sample;

import java.util.ArrayList;
import java.util.List;

import org.restfulwhois.rdap.client.RdapUpdateClient;
import org.restfulwhois.rdap.client.exception.RdapClientException;
import org.restfulwhois.rdap.client.service.RdapClientConfig;
import org.restfulwhois.rdap.common.dto.IpDto;
import org.restfulwhois.rdap.common.dto.UpdateResponse;
import org.restfulwhois.rdap.common.dto.embedded.RemarkDto;

public class RdapUpdateClientSample {
    public RdapClientConfig config(){
        String url = "http://www.sample.com";
        RdapClientConfig config = new RdapClientConfig(url);
        config.setConnectTimeout(10000);
        config.setReadTimeout(10000);
        return config;
    }

    public UpdateResponse createSample() {
        RdapUpdateClient client = new RdapUpdateClient(config());

        IpDto ipDto = new IpDto();
        ipDto.setHandle("ip-1");
        ipDto.setStartAddress("192.168.1.1");
        ipDto.setEndAddress("192.168.1.255");
        List<RemarkDto> remarks = new ArrayList<RemarkDto>();
        RemarkDto remarkDto = new RemarkDto();
        remarkDto.setHandle("remark-1");
        remarks.add(remarkDto);
        ipDto.setRemarks(remarks);

        UpdateResponse response;
        try {
            response = client.create(ipDto);
        } catch (RdapClientException e) {
            response = null;
        }
        return response;
    }

    public UpdateResponse updateSample() {
        RdapUpdateClient client = new RdapUpdateClient(config());

        IpDto ipDto = new IpDto();
        ipDto.setHandle("ip-1");
        ipDto.setStartAddress("0000::0001");
        ipDto.setEndAddress("0000:ffff");
        ipDto.setIpVersion("v6");
        List<RemarkDto> remarks = new ArrayList<RemarkDto>();
        RemarkDto remarkDto = new RemarkDto();
        remarkDto.setHandle("remark-1");
        remarks.add(remarkDto);
        ipDto.setRemarks(remarks);

        UpdateResponse response;
        try {
            response = client.update(ipDto);
        } catch (RdapClientException e) {
            response = null;
        }
        return response;
    }

    public UpdateResponse deleteSample() {
        RdapUpdateClient client = new RdapUpdateClient(config());

        UpdateResponse response;
        try {
            response = client.deleteIp("ip-1");
        } catch (RdapClientException e) {
            response = null;
        }
        return response;
    }
}
