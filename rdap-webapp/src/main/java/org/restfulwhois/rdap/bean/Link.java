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
package org.restfulwhois.rdap.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * signify links to other resources on the Internet.
 * 
 * @author jiashuo
 * 
 */
public class Link extends BaseModel {

    /**
     * value string.
     */
    private String value;
    /**
     * relationship between two objects.
     */
    private String rel;
    /**
     * href of link.
     */
    private String href;
    /**
     * href language of link.
     */
    private List<String> hreflang;
    /**
     * title of link.
     */
    private List<String> title;
    /**
     * media of link.
     */
    private String media;
    /**
     * type of link.
     */
    private String type;

    /**
     * add a hreflang.
     * 
     * @param hreflangStr
     *            string for href lang.
     */
    public void addHreflang(String hreflangStr) {
        if (StringUtils.isBlank(hreflangStr)) {
            return;
        }
        if (null == this.hreflang) {
            this.hreflang = new ArrayList<String>();
        }
        this.hreflang.add(hreflangStr);
    }

    /**
     * add a title.
     * 
     * @param titleStr
     *            string of tile.
     */
    public void addTitle(String titleStr) {
        if (StringUtils.isBlank(titleStr)) {
            return;
        }
        if (null == this.title) {
            this.title = new ArrayList<String>();
        }
        this.title.add(titleStr);
    }

    /**
     * get value of link.
     * 
     * @return value string.
     */
    public String getValue() {
        return value;
    }

    /**
     * set value of link.
     * 
     * @param value
     *            value string to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * get relationship of link.
     * 
     * @return rel string.
     */
    public String getRel() {
        return rel;
    }

    /**
     * set relationship of link.
     * 
     * @param rel
     *            relationship string to set.
     * 
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * get href of link.
     * 
     * @return href string.
     */
    public String getHref() {
        return href;
    }

    /**
     * set href of link.
     * 
     * @param href
     *            string to set.
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * get hreflang of link.
     * 
     * @return list string of hreflang.
     */
    public List<String> getHreflang() {
        return hreflang;
    }

    /**
     * set href language of link.
     * 
     * @param hreflang
     *            list string of hreflang to set.
     */
    public void setHreflang(List<String> hreflang) {
        this.hreflang = hreflang;
    }

    /**
     * get title of link.
     * 
     * @return a list of string.
     */
    public List<String> getTitle() {
        return title;
    }

    /**
     * set title of link.
     * 
     * @param title
     *            a list string of title.
     */
    public void setTitle(List<String> title) {
        this.title = title;
    }

    /**
     * get media of link.
     * 
     * @return string of media.
     */
    public String getMedia() {
        return media;
    }

    /**
     * set media of link.
     * 
     * @param media
     *            string to set.
     */
    public void setMedia(String media) {
        this.media = media;
    }

    /**
     * get type of link.
     * 
     * @return type string.
     */
    public String getType() {
        return type;
    }

    /**
     * set type of link.
     * 
     * @param type
     *            type of link.
     */
    public void setType(String type) {
        this.type = type;
    }
    
    @Override
    public ModelType getObjectType() {
        return ModelType.LINK;
    }
}
