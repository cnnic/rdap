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
package org.restfulwhois.rdap.common.dto;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * UpdateResponse.
 * 
 * @author jiashuo
 * 
 */
@JsonInclude(Include.NON_DEFAULT)
public class UpdateResponse {
    /**
     * httpStatusCode.
     */
    @JsonIgnore
    private int httpStatusCode;
    /**
     * handle.
     */
    private String handle;
    /**
     * errorCode.
     */
    private int errorCode;
    /**
     * subErrorCode.
     */
    private int subErrorCode;
    /**
     * description list.
     */
    private List<String> description = new ArrayList<String>();

    /**
     * build success response.
     * 
     * @param handle
     *            handle.
     * @return UpdateResponse.
     */
    public static UpdateResponse buildSuccessResponse(String handle) {
        UpdateResponse response = new UpdateResponse();
        response.setHttpStatusCode(SimpleHttpStatusCode.OK_200.value());
        response.setHandle(handle);
        return response;
    }

    /**
     * build error response.
     * 
     * @param handle
     *            handle.
     * @param subErrorCode
     *            subErrorCode.
     * @param httpStatusCode
     *            httpStatusCode.
     * @param description
     *            description.
     * @return UpdateResponse.
     */
    public static UpdateResponse buildErrorResponse(String handle,
            int subErrorCode, int httpStatusCode, String description) {
        UpdateResponse response = new UpdateResponse();
        response.setErrorCode(httpStatusCode);
        response.setSubErrorCode(subErrorCode);
        response.setHttpStatusCode(httpStatusCode);
        response.setHandle(handle);
        response.addDescription(description);
        return response;
    }

    /**
     * build error response.
     * 
     * @param subErrorCode
     *            subErrorCode.
     * @param httpStatusCode
     *            httpStatusCode.
     * @param description
     *            description.
     * @return UpdateResponse.
     */
    public static UpdateResponse buildErrorResponse(int subErrorCode,
            int httpStatusCode, String description) {
        UpdateResponse response = new UpdateResponse();
        response.setErrorCode(httpStatusCode);
        response.setSubErrorCode(subErrorCode);
        response.setHttpStatusCode(httpStatusCode);
        response.addDescription(description);
        return response;
    }

    /**
     * add description.
     * 
     * @param desc
     *            desc.
     */
    private void addDescription(String desc) {
        this.description.add(desc);
    }

    /**
     * get httpStatusCode.
     * 
     * @return httpStatusCode.
     */
    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * set httpStatusCode.
     * 
     * @param httpStatusCode
     *            httpStatusCode.
     */
    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * get handle.
     * 
     * @return handle.
     */
    public String getHandle() {
        return handle;
    }

    /**
     * set handle.
     * 
     * @param handle
     *            handle.
     */
    public void setHandle(String handle) {
        this.handle = handle;
    }

    /**
     * get errorCode.
     * 
     * @return errorCode.
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * set errorCode.
     * 
     * @param errorCode
     *            errorCode.
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * get description.
     * 
     * @return description.
     */
    public List<String> getDescription() {
        return description;
    }

    /**
     * set description.
     * 
     * @param description
     *            description.
     */
    public void setDescription(List<String> description) {
        this.description = description;
    }

    /**
     * get subErrorCode.
     * 
     * @return subErrorCode.
     */
    public int getSubErrorCode() {
        return subErrorCode;
    }

    /**
     * set subErrorCode.
     * 
     * @param subErrorCode
     *            subErrorCode.
     */
    public void setSubErrorCode(int subErrorCode) {
        this.subErrorCode = subErrorCode;
    }

}
