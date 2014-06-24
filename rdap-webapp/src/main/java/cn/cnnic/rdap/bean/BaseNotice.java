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
package cn.cnnic.rdap.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * base notice class.
 * 
 * @author jiashuo
 * 
 */
public abstract class BaseNotice extends BaseModel {

    /**
     * notice type enum.
     * 
     * @author jiashuo
     * 
     */
    public enum NoticeType {
        /**
         * three objects.
         */
        Notice("notice"), REMARK("remark"), HELP("help");
        /**
         * type name.
         */
        private String name;

        /**
         * NoticeType.
         * 
         * @param name
         *            a string of type.
         */
        private NoticeType(String name) {
            this.name = name;
        }

        /**
         * getName.
         * 
         * @return a string to get.
         */
        public String getName() {
            return name;
        }
    }

    /**
     * title.
     */
    private String title;
    /**
     * description.
     */
    private List<String> description;
    /**
     * links.
     */
    private List<Link> links;

    /**
     * add a description.
     * 
     * @param descriptionStr
     *            a string to add for a description.
     */
    public void addDescription(String descriptionStr) {
        if (StringUtils.isBlank(descriptionStr)) {
            return;
        }
        if (null == this.description) {
            this.description = new ArrayList<String>();
        }
        this.description.add(descriptionStr);
    }

    /**
     * get Title of notice.
     * 
     * @return a string of title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * setTitle of notice.
     * 
     * @param title
     *            a string to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get description of notice.
     * 
     * @return a list string to get as a description.
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * set description of notice.
     * 
     * @param description
     *            a list string to set.
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * get links of object.
     * 
     * @return list of object link.
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * set links of object.
     * 
     * @param links
     *            list of object to set.
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
