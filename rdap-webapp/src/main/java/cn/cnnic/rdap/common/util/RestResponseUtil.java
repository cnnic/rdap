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
package cn.cnnic.rdap.common.util;

import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import cn.cnnic.rdap.bean.ErrorMessage;
import cn.cnnic.rdap.service.ErrorMessageService;

/**
 * RestResponseUtil is used to create ResponseEntity.
 * 
 * @author jiashuo
 * 
 */
@Component
public class RestResponseUtil {
	/**
	 * all ErrorMessage map,must be init before call
	 * getErrorMessageByErrorCode()
	 */
	private static Map<Long, ErrorMessage> errorMessageMap = null;

	private static ErrorMessageService errorMessageService;

	@PostConstruct
	private void init() {
		initErrorMessages();
	}

	/**
	 * init ErrorMessages list
	 */
	private static void initErrorMessages() {
		errorMessageMap = errorMessageService.getAllErrorMessageMap();
	}

	/**
	 * get ErrorMessage by error code
	 * 
	 * @param errorCode
	 *            error code
	 * @return ErrorMessage
	 */
	private static ErrorMessage getErrorMessageByErrorCode(String errorCode) {
		Long codeLongVal = Long.valueOf(errorCode);
		if (null == errorMessageMap) {
			initErrorMessages();
		}
		ErrorMessage result = errorMessageMap.get(codeLongVal);
		if (null != result) {
			return result;
		}
		return ErrorMessage.getNullErrorMessage();
	}

	/**
	 * create response with HTTP status code 200
	 * 
	 * @param response
	 *            model object
	 * @return ResponseEntity
	 */
	public static <T> ResponseEntity<T> createResponse200(T response) {
		return new ResponseEntity<T>(response, HttpStatus.OK);
	}

	/**
	 * create response with HTTP status code 400
	 * 
	 * @param response
	 *            model object
	 * @return ResponseEntity
	 */
	public static ResponseEntity<ErrorMessage> createResponse400() {
		return createCommonErrorResponse(HttpStatus.BAD_REQUEST);
	}

	/**
	 * create response with HTTP status code 404
	 * 
	 * @param response
	 *            model object
	 * @return ResponseEntity
	 */
	public static ResponseEntity<ErrorMessage> createResponse404() {
		return createCommonErrorResponse(HttpStatus.NOT_FOUND);
	}

	/**
	 * create response with HTTP status code 500
	 * 
	 * @param response
	 *            model object
	 * @return ResponseEntity
	 */
	public static ResponseEntity<ErrorMessage> createResponse500() {
		return createCommonErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	/**
	 * create error response
	 * 
	 * @param errorStatus
	 *            HttpStatus of error message
	 * @return ResponseEntity
	 */
	private static ResponseEntity<ErrorMessage> createCommonErrorResponse(
			HttpStatus errorStatus) {
		ErrorMessage errorMessage = getErrorMessageByErrorCode(errorStatus
				.toString());
		return new ResponseEntity<ErrorMessage>(errorMessage, errorStatus);
	}

	@Autowired
	public void setErrorMessageService(ErrorMessageService errorMessageService) {
		RestResponseUtil.errorMessageService = errorMessageService;
	}
}
