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
package org.restfulwhois.rdap.client.exception;

/**
 * The exception message enum <br>
 * It is mainly used for conversion between DTO objects and JSON.
 * @author M.D.
 *
 */
public enum ExceptionMessage {

    /**
     * Dto object is not the instance of IpDto, AutnumDto, DomainDto, 
     * EntityDto or NameserverDto
     */
    NOT_LEGAL_DTO_ERROR("The dto object is not the instance of "
            + "IpDto, AutnumDto, DomainDto, EntityDto or NameserverDto."),

    /**
     * Convert dto object to json error
     */
    OBJECT_TO_JSON_ERROR("Convert object to json error:\n"),

    /**
     * Convert json to dto object error
     */
    JSON_TO_OBJECT_ERROR("Convert json to object error:\n"),

    /**
     * Set custom properties error
     */
    SET_CUSTOMPROPERTIES_ERROR("Set CustomProperties error:\n");

    /**
     * Exception message
     */
    String message;

    /**
     * Constructor
     * @param message the exception message
     */
    private ExceptionMessage(String message) {
        this.message = message;
    }

    /**
     * Get the exception message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
