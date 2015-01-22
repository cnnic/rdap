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

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.restfulwhois.rdap.common.model.base.BaseCustomModel;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.entity.model.Entity;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
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
    public void test_map() throws JsonProcessingException {
        Entity entity = null;
        entity = new Entity();
        entity.setHandle("entityHandle");
        CustomEntity customEntity = new CustomEntity();
        entity.setCustomModel(customEntity);
        customEntity.addEntry("key1", "value1");
        customEntity.addEntry("handle", "customHandle");
        ObjectMapper mapper = new ObjectMapper();
        String entityJson = null;
        entityJson = mapper.writeValueAsString(entity);
        assertNotNull(entityJson);
        assertThat(entityJson, StringContains.containsString("key1"));
        assertThat(entityJson, StringContains.containsString("value1"));

    }

    /**
     * test prefix.
     */
    @Test
    public void testBasecCustomMode() {
        Entity entity = null;
        entity = new Entity();
        entity.setPort43("port43");
        CustomModel custom = new CustomModel();
        custom.setProtocol("HTTP");
        custom.setDigest(1);
        entity.setCustomModel(custom);
        ObjectMapper mapper = new ObjectMapper();
        // Convert object to JSON string
        String entityJson = null;
        try {
            entityJson = mapper.writeValueAsString(entity);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        assertNotNull(entityJson);
        assertThat(entityJson, new StringContains(
                BaseModel.CUSTOM_PROPERTY_PREFIX + "protocol"));
        assertThat(entityJson, new StringContains(
                BaseModel.CUSTOM_PROPERTY_PREFIX + "digest"));

    }

    /**
     * test prefix.
     */
    class CustomModel extends BaseCustomModel {
        private String protocol;
        private int digest;

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public int getDigest() {
            return digest;
        }

        public void setDigest(int digest) {
            this.digest = digest;
        }

    }

    class CustomEntity extends BaseCustomModel {
        private Map<String, String> keyValues = new HashMap<String, String>();

        @JsonAnySetter
        public CustomEntity addEntry(String key, String value) {
            keyValues.put(key, value);
            return this;
        }

        @JsonAnyGetter
        public Map<String, String> getKeyValues() {
            return keyValues;
        }

        public void setKeyValues(Map<String, String> keyValues) {
            this.keyValues = keyValues;
        }

    }
}
