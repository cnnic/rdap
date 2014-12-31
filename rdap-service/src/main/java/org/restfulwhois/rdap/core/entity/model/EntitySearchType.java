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
package org.restfulwhois.rdap.core.entity.model;

import java.util.ArrayList;
import java.util.List;

/**
 * search type.
 * 
 * @return by name, nsLdhName, nsIP.
 */
public enum EntitySearchType {

    /**
     * handle type.
     */
    HANDLE("handle"),
    /**
     * fn type.
     */
    FN("fn");

    /**
     * name of model type.
     */
    private String name;

    /**
     * constructor.
     * 
     * @param name
     *            name.
     */
    private EntitySearchType(String name) {
        this.name = name;
    }

    /**
     * get name.
     * 
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * valuesOfString.
     * 
     * @return all values in string.
     */
    public static String[] valuesOfString() {
        EntitySearchType[] types = values();
        List<String> result = new ArrayList<String>();
        for (EntitySearchType type : types) {
            result.add(type.name);
        }
        return result.toArray(new String[0]);
    }

    /**
     * getByName.
     * 
     * @param name
     *            name.
     * @return EntitySearchType.
     */
    public static EntitySearchType getByName(String name) {
        EntitySearchType[] queryTypes = EntitySearchType.values();
        for (EntitySearchType type : queryTypes) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

}
