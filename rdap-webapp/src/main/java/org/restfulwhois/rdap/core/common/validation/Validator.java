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
package org.restfulwhois.rdap.core.common.validation;

import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.springframework.validation.Errors;

/**
 * Validator.
 * 
 * @author jiashuo
 * 
 */
public interface Validator {

    /**
     * Can this {@link Validator} {@link #validate(Object, Errors) validate}
     * instances of the supplied {@code clazz}?
     * <p>
     * This method is <i>typically</i> implemented like so:
     * 
     * <pre class="code">
     * return Foo.class.isAssignableFrom(clazz);
     * </pre>
     * 
     * (Where {@code Foo} is the class (or superclass) of the actual object
     * instance that is to be {@link #validate(Object, Errors) validated}.)
     * 
     * @param clazz
     *            the {@link Class} that this {@link Validator} is being asked
     *            if it can {@link #validate(Object, Errors) validate}
     * @return {@code true} if this {@link Validator} can indeed
     *         {@link #validate(Object, Errors) validate} instances of the
     *         supplied {@code clazz}
     */
    boolean supports(Class<?> clazz);

    /**
     * Validate the supplied {@code queryParam} object, which must be of a
     * {@link Class} for which the {@link #supports(Class)} method typically has
     * (or would) return {@code true}.
     * <p>
     * The supplied {@link ValidationResult validationResult} instance can be
     * used to report any resulting validation errors.
     * 
     * @param queryParam
     *            the queryParam that is to be validated (can be {@code null})
     * @param validationResult
     *            contextual state about the validation process (never
     *            {@code null})
     */
    void validate(QueryParam queryParam, ValidationResult validationResult);

}
