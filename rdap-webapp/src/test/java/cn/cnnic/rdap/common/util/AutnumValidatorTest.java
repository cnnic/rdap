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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Test for RestResponseUtil
 * 
 * @author jiashuo
 * 
 */
@SuppressWarnings("rawtypes")
public class AutnumValidatorTest {

	/**
	 * test valid autnum of one number
	 */
	@Test
	public void testValidAutnumOfOneNum() {
		String validAutnum = "3";
		assertTrue(AutnumValidator.isValidAutnum(validAutnum));
	}

	/**
	 * test valid autnum of two number
	 */
	@Test
	public void testValidAutnumOfTwoNum() {
		String validAutnum = "13";
		assertTrue(AutnumValidator.isValidAutnum(validAutnum));
	}

	/**
	 * test valid autnum of ten number
	 */
	@Test
	public void testValidAutnumOfTenNum() {
		String validAutnum = "1234567890";
		assertTrue(AutnumValidator.isValidAutnum(validAutnum));
	}

	/**
	 * test valid autnum of min autnum
	 */
	@Test
	public void testValidAutnumOfMin() {
		String validAutnum = "0";
		assertTrue(AutnumValidator.isValidAutnum(validAutnum));
	}

	/**
	 * test valid autnum of max autnum
	 */
	@Test
	public void testValidAutnumOfMax() {
		String validAutnum = "4294967295";
		assertTrue(AutnumValidator.isValidAutnum(validAutnum));
	}

	/**
	 * test invalid autnum of negative num
	 */
	@Test
	public void testInValidAutnumOfNegativeNum() {
		String validAutnum = "-13";
		assertFalse(AutnumValidator.isValidAutnum(validAutnum));
	}
	/**
	 * test invalid autnum of negative num
	 */
	@Test
	public void testInValidAutnumOfLargeNum() {
		String validAutnum = "4294967296";
		assertFalse(AutnumValidator.isValidAutnum(validAutnum));
	}
}
