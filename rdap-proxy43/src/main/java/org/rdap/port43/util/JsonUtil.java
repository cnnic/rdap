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
package org.rdap.port43.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.rdap.port43.service.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSON util.
 * 
 * @author jiashuo
 * 
 */
public class JsonUtil {
    /**
     * logger.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(JsonUtil.class);

    /**
     * convert object to JSON string.
     * 
     * @param object
     *            object.
     * @return string.
     */
    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("to JSON error:{}", e);
        }
        return "";
    }

    public static String toJsonWithPrettyFormat(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("toJsonWithPrettyFormat error:{}", e);
        }
        return StringUtils.EMPTY;
    }

    /**
     * deserializate JSON, to map.
     * 
     * @param restResponse
     *            restResponse.
     * @return map.
     */
    public static Map deserializateJsonToMap(RestResponse restResponse) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map result = null;
        try {
            result =
                    objectMapper.readValue(restResponse.getBody(),
                            LinkedHashMap.class);
            if (HttpStatus.SC_MOVED_PERMANENTLY == restResponse.getStatusCode()) {
                LOGGER.info(
                        "response code from RDAP server is 301,set location to result:{}",
                        restResponse.getLocationHeader());
                result.put("redirect location", restResponse.getLocationHeader());
            }
        } catch (Exception e) {
            LOGGER.error("deserializateJsonToMap error:{}", e);
            throw new ServiceException("deserializateJsonToMap error.", e);
        }
        return result;
    }

}
