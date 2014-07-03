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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * error message for HTTP response.
 * 
 * @author jiashuo
 * 
 */
@JsonInclude(Include.NON_EMPTY)
@JsonPropertyOrder({ "rdapConformance", "notices", "errorCode", "title",
        "description", "lang" })
public class ErrorMessage extends BaseModel {
    /**
     * identity of object.
     */
    private Long id;
    /**
     * HTTP response code.
     */
    private Long errorCode;
    /**
     * title of error.
     */
    private String title;
    /**
     * description of error.
     */
    private List<String> description = new ArrayList<String>();

    /**
     * get null safe ErrorMessage.
     * 
     * @return ErrorMessage with null properties
     */
    public static ErrorMessage getNullErrorMessage() {
        return new ErrorMessage();
    }

    /**
     * add a description string to description list.
     * 
     * @param descriptionStr
     *            string to add as a description.
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
     * get id.
     * 
     * @return long id.
     */
    public Long getId() {
        return id;
    }

    /**
     * set id.
     * 
     * @param id
     *            long to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get error code.
     * 
     * @return errorCode.
     */
    public Long getErrorCode() {
        return errorCode;
    }

    /**
     * set error code.
     * 
     * @param errorCode
     *            long code of error.
     */
    public void setErrorCode(Long errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * get title of object.
     * 
     * @return string title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * set title of object.
     * 
     * @param title
     *            string title.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * get description of error.
     * 
     * @return string description.
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * set description of object.
     * 
     * @param description
     *            string to set.
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }
    
    @Override
    public ModelType getObjectType() {
        return ModelType.ERRORMESSAGE;
    }
}
