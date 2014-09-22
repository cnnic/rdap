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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.bootstrap.bean.AutnumRedirect;
import org.restfulwhois.rdap.bootstrap.bean.Redirect;
import org.restfulwhois.rdap.common.RdapProperties;
import org.springframework.stereotype.Service;

/**
 * This class is used to Update as number registry.
 * 
 * @author jiashuo
 * 
 */
@Service
public class AutnumRegistryHandler extends RegistryHandler {
    /**
     * start/end as number separator.
     */
    private static final String AS_START_END_SEPARATOR = ",";

    @Override
    String getRegistryRelativateUrl() {
        return RdapProperties.getBootstrapRegistryUriForAs();
    }

    @Override
    void saveRedirects(List<Redirect> redirects) {
        redirectService.saveAutnumRedirect(redirects);
    }

    @Override
    List<Redirect> generateRedirects(String key, List<String> registryUrls) {
        // key:[1,100]
        List<Redirect> redirects = new ArrayList<Redirect>();
        if (StringUtils.isBlank(key)
                || !removeEmptyUrlsAndValidate(registryUrls)) {
            logger.error("ignore this key/urls. Key or registryUrls"
                    + " is blank,key:{}, urls:{}", key, registryUrls);
            return redirects;
        }
        key = StringUtils.trim(key);
        key = StringUtils.removeStart(key, "[");
        key = StringUtils.removeEnd(key, "]");
        String[] splits = StringUtils.split(key, AS_START_END_SEPARATOR);
        if (splits.length != 2) {
            logger.error("ignore this key/value. Key's format"
                    + " MUST be 'startNum,endNumber'. But it's:{}",
                    Arrays.toString(splits));
            return redirects;
        }
        Long startAsNumber = 0L;
        Long endAsNumber = 0L;
        try {
            startAsNumber = Long.parseLong(StringUtils.trim(splits[0]));
            endAsNumber = Long.parseLong(StringUtils.trim(splits[1]));
        } catch (Exception e) {
            logger.error("{},{}parseLong error:{}", new Object[] {
                    splits[0], splits[1], e });
            logger.error("ignore this key/urls:{},{}", key, registryUrls);
            return redirects;
        }
        AutnumRedirect autnumRedirect =
                new AutnumRedirect(startAsNumber, endAsNumber, registryUrls);
        redirects.add(autnumRedirect);
        return redirects;
    }

}
