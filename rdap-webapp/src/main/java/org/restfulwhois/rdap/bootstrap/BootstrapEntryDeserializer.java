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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.restfulwhois.rdap.bootstrap.bean.BootstrapEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Deserialize bootstrap entry.
 * 
 * <pre>
 *   Structure:
 *     ["entry1", "entry2", "entry3"],
 *     [
 *       "https://registry.example.com/myrdap/",
 *       "http://registry.example.com/myrdap/"
 *     ]
 * </pre>
 * <p>
 * Will return null if JSON is unformatted.
 * </p>
 * 
 * @author jiashuo
 * 
 */
public class BootstrapEntryDeserializer extends
        JsonDeserializer<BootstrapEntry> {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BootstrapEntryDeserializer.class);
    /**
     * entries length.
     */
    private static final int ENTRY_LENGTH = 2;

    @Override
    public BootstrapEntry
            deserialize(JsonParser jp, DeserializationContext ctxt)
                    throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);
        if (node.size() < ENTRY_LENGTH) {
            LOGGER.error("ignore bootstrapEntry, wrong formated:{}", node);
            return null;
        }
        JsonNode keysNode = node.get(0);
        List<String> keys = parseStringList(keysNode);
        JsonNode urlsNode = node.get(1);
        List<String> registryUrls = parseStringList(urlsNode);
        BootstrapEntry result = new BootstrapEntry();
        result.setKeys(keys);
        result.setRegistryUrls(registryUrls);
        return result;
    }

    /**
     * parseStringList.
     * 
     * @param keysNode
     *            keysNode.
     * @return string list.
     */
    private List<String> parseStringList(JsonNode keysNode) {
        List<String> keys = new ArrayList<String>();
        for (Iterator<JsonNode> it = keysNode.iterator(); it.hasNext();) {
            keys.add(it.next().toString());
        }
        return keys;
    }

}
