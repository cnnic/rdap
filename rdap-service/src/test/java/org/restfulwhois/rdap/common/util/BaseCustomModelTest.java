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
package org.restfulwhois.rdap.common.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.LinkedHashMap;
import java.util.Map;

import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.support.RdapProperties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Test for BasecCustomModel json.
 * 
 * @author zhanyq
 * 
 */
public class BaseCustomModelTest {
    @Test
    public void test_custom_serialize() throws JsonProcessingException {
        Entity entity = null;
        entity = new Entity();
        entity.setHandle("entityHandle");
        Map<String, String> customProperties =
                new LinkedHashMap<String, String>();
        entity.setCustomProperties(customProperties);
        entity.setCustomPropertyPrefix(RdapProperties.getCustomPropertyPrefix());
        customProperties.put("key1", "value1");
        customProperties.put("handle", "customHandle");
        ObjectMapper mapper = new ObjectMapper();
        String entityJson = null;
        entityJson = mapper.writeValueAsString(entity);
        assertNotNull(entityJson);
        assertThat(entityJson,
                new StringContains(RdapProperties.getCustomPropertyPrefix()
                        + "key1"));
        assertThat(entityJson, StringContains.containsString("value1"));
        assertThat(entityJson, StringContains.containsString("entityHandle"));
        assertThat(
                entityJson,
                StringContains.containsString(RdapProperties
                        .getCustomPropertyPrefix() + "handle"));
    }

    @Test
    public void test_custom_deserialize() throws JsonProcessingException {
        Entity entity = null;
        entity = new Entity();
        entity.setHandle("entityHandle");
        String customPropJSON =
                "{\"key1\":\"value1\",\"handle\":\"customHandle\"}";
        entity.setCustomProperties(JsonUtil
                .deserializeJsonToMap(customPropJSON));
        ObjectMapper mapper = new ObjectMapper();
        String entityJson = null;
        entityJson = mapper.writeValueAsString(entity);
        assertNotNull(entityJson);
        assertThat(entityJson, StringContains.containsString("key1"));
        assertThat(entityJson, StringContains.containsString("value1"));
        assertThat(entityJson, StringContains.containsString("entityHandle"));
        assertThat(entityJson, StringContains.containsString("customHandle"));
    }

}
