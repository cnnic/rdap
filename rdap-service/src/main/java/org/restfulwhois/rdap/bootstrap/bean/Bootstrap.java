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
package org.restfulwhois.rdap.bootstrap.bean;

import java.util.List;

/**
 * 
 * bootstrap registry data structure.
 * 
 * @author jiashuo
 * 
 */
public class Bootstrap {
    /**
     * corresponds to the format version of the registry.
     */
    private String version;
    /**
     * date/time of the registry, conforms to the Internet date/time format.
     */
    private String publication;
    /**
     * contain a comment regarding the content of the bootstrap object.
     */
    private String description;
    /**
     * Then the "services" element is an array of arrays. Each second level
     * array contains two elements, each of them being an array (third-level
     * arrays). The first third-level array contains all entries that have the
     * same set of base RDAP URLs, as strings, arrays, or integers. The second
     * third-level array contains the list of base RDAP URLs usable for the
     * entries found in the first third-level array.
     * <pre>
     * "services": [
     *   [
     *     ["entry1", "entry2", "entry3"],
     *     [
     *       "https://registry.example.com/myrdap/",
     *       "http://registry.example.com/myrdap/"
     *     ]
     *   ],
     *   [
     *     ["entry4"],
     *     [
     *       "http://example.org/"
     *     ]
     *   ]
     * ]
     * </pre>
     */
    private List<BootstrapEntry> services;

    /**
     * get version.
     * 
     * @return version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * set version.
     * 
     * @param version
     *            version.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * get publication.
     * 
     * @return publication.
     */
    public String getPublication() {
        return publication;
    }

    /**
     * set publication.
     * 
     * @param publication
     *            publication.
     */
    public void setPublication(String publication) {
        this.publication = publication;
    }

    /**
     * get description.
     * 
     * @return description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * set description.
     * 
     * @param description
     *            description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get services.
     * 
     * @return services.
     */
    public List<BootstrapEntry> getServices() {
        return services;
    }

    /**
     * set services.
     * 
     * @param services
     *            services..
     */
    public void setServices(List<BootstrapEntry> services) {
        this.services = services;
    }

}
