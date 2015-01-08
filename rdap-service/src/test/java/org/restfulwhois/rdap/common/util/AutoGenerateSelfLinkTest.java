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
package org.restfulwhois.rdap.common.util;

import org.junit.Test;
import org.restfulwhois.rdap.common.dao.impl.SelfLinkGenerator;
import org.restfulwhois.rdap.common.model.Autnum;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Entity;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.Nameserver;
import org.restfulwhois.rdap.common.model.Network;
import org.springframework.util.Assert;

/**
 * Test for SelfLinkGenerator
 * 
 * @author zhanyq
 * 
 */
@SuppressWarnings("rawtypes")
public class AutoGenerateSelfLinkTest {

    /**
     * test auto gennerate self-link of object.
     *      
     */
    @Test
    public void testGenerateSelfLink() {
        //domain
        Domain domain = new Domain();
        domain.setLdhName("cnnic.cn");
        Link  link = SelfLinkGenerator.generateSelfLink(domain);
        Assert.notNull(link);
        //nameserver
        link = null;
        Nameserver ns = new Nameserver();
		ns.setLdhName("cnnic.cn");
		link = SelfLinkGenerator.generateSelfLink(ns);
		Assert.notNull(link);
		
		//entity
		link = null;
		Entity entity = new Entity();
		entity.setHandle("h1");
		link = SelfLinkGenerator.generateSelfLink(entity);
		Assert.notNull(link);
		//ip
		link = null;
		Network network = new Network();
		network.setCidr("1.1.1.0/24");
		link = SelfLinkGenerator.generateSelfLink(network);
		Assert.notNull(link);
        //autnum
		link = null;
		Autnum as = new Autnum();
		as.setStartAutnum(1L);
		link = SelfLinkGenerator.generateSelfLink(as);
		Assert.notNull(link);
	}
	


}
