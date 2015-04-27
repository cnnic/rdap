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
package org.restfulwhois.rdap.client.util;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.restfulwhois.rdap.client.exception.ExceptionMessage;
import org.restfulwhois.rdap.client.exception.RdapClientException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * To handle json and dto object convert each other.
 * @author M.D.
 *
 */
public final class JsonUtil {

    /**
     * ObjectMapper instance
     */
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
    
    /**
     * constructor
     */
    private JsonUtil(){}

    /**
     * Converts object to json string.
     * @param object dto object
     * @return json string
     * @throws RdapClientException if fail to convert
     */
    public static String toJson(Object object) throws RdapClientException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RdapClientException(makeMessage(
                    ExceptionMessage.OBJECT_TO_JSON_ERROR, e));
        }
    }

    /**
     * Converts json string to object.
     * @param json json string
     * @param objectType dto class type
     * @param <T> dto class type
     * @return dto object
     * @throws RdapClientException if fail to convert
     */
    public static <T> T toObject(String json, Class<T> objectType)
            throws RdapClientException {
        return toObject(json, objectType, null);
    }

    /**
     * Converts json string to object and saves unkonwn properties into the
     * paramter unkonwnPropertiesMap.
     * @param json json string
     * @param objectType dto class type
     * @param unknownPropertiesMap 
     * @param <T> dto class type
     * @return dto object
     * @throws RdapClientException if fail to convert
     */
    public static <T> T toObject(String json, Class<T> objectType,
            Map<String, String> unknownPropertiesMap)
            throws RdapClientException {

        T object = null;
        try {
            object = objectMapper.readValue(json, objectType);
        } catch (IOException e) {
            throw new RdapClientException(makeMessage(
                    ExceptionMessage.JSON_TO_OBJECT_ERROR, e));
        }
        if (unknownPropertiesMap != null) {
            Map<String, String> map = unidentifiedFields(json, objectType);
            unknownPropertiesMap.putAll(map);
        }
        return object;
    }

    /**
     * Convert unknown property to map.
     * @param json json string
     * @param model Class type
     * @return Map<String, String>
     * @throws RdapClientException if fail to convert
     */
    private static Map<String, String> unidentifiedFields(String json,
            Class<?> model) throws RdapClientException {
        Map<String, String> rtnMap = new HashMap<String, String>();
        try {
            JsonNode node = objectMapper.readTree(json);
            Iterator<String> it = node.fieldNames();
            Class<?> superClass = model.getSuperclass();
            ArrayList<Field> fieldList = new ArrayList<Field>(
                    Arrays.asList(model.getDeclaredFields()));
            if (superClass != null
                    && !superClass.getName().equals(Object.class.getName())) {
                fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
            }

            boolean identifiableFlag = true;
            String currentString;
            while (it.hasNext()) {
                currentString = it.next();
                for (Field field : fieldList) {
                    if (currentString.equals(field.getName())) {
                        identifiableFlag = true;
                        break;
                    } else {
                        identifiableFlag = false;
                    }
                }
                if (!identifiableFlag) {
                    rtnMap.put(currentString, node.findValue(currentString)
                            .toString());
                    identifiableFlag = true;
                }
            }
        } catch (IOException e) {
            throw new RdapClientException(makeMessage(
                    ExceptionMessage.SET_CUSTOMPROPERTIES_ERROR, e));
        }
        return rtnMap;
    }

    /**
     * To creat exception message
     * @param em ExceptionMessage 
     * @param e Exception
     * @return message string
     */
    private static String makeMessage(ExceptionMessage em, Exception e) {
        return em.getMessage() + e.getMessage();
    }
}
