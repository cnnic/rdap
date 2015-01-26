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
package org.restfulwhois.rdap.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.restfulwhois.rdap.BaseTest;
import org.restfulwhois.rdap.common.dao.UpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.VariantDto;
import org.restfulwhois.rdap.common.dto.embedded.VariantNameDto;
import org.restfulwhois.rdap.common.model.Domain;
import org.restfulwhois.rdap.common.model.Variants;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.assertion.DatabaseAssertionMode;

/**
 * @author zhanyq
 * 
 */
public class VariantsUpdateDaoTest extends BaseTest {

	 private static final String TABLE_RDAP_VARIANT = "RDAP_VARIANT";
	 private static final String TABLE_REL_DOMAIN_VARIANT = "REL_DOMAIN_VARIANT";


	    @Autowired
	    private UpdateDao<Variants, VariantDto> updateDao;	   

	    @Test
	    @DatabaseSetup("teardown.xml")
	    @DatabaseTearDown("teardown.xml") 
	    @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/variants-update.xml")
	    public void testcreateVariants() throws Exception {
	    	Domain domain = new Domain();
	    	domain.setId(1L);
	    	List<VariantDto> variantsList = createVariants();	 
	        updateDao.saveAsInnerObjects(domain, variantsList);	     
	    }

	    public static List<VariantDto> createVariants() {
            List<VariantDto> variantsList = new ArrayList<VariantDto>();
	    	List<VariantNameDto> variantNames = new ArrayList<VariantNameDto>();
            VariantDto variants = new VariantDto();
            VariantNameDto variant = new VariantNameDto();
            List<String> relation = new ArrayList<String>();
            variants.setRelation(relation);
            relation.add("conjoined");
            relation.add("open registration");
            variants.setIdnTable(".EXAMPLE Swedish");
            variant.setLdhName("xn--g6ws64d.xn--fiqs8s");
            variant.setUnicodeName("測试.中国");
            variantNames.add(variant);
            variants.setVariantNames(variantNames);
	    	variantsList.add(variants);
            return variantsList;
        }

        public static void assertTable() throws Exception {
            assertTablesForUpdate("variants-update.xml", TABLE_RDAP_VARIANT,
	        		TABLE_REL_DOMAIN_VARIANT);
        }
        
        @Test
        @DatabaseSetup("variants-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/variants-empty.xml")
        public void testDeleteVariant() throws Exception {
            Domain domain = new Domain();
            domain.setId(1L);
            updateDao.deleteAsInnerObjects(domain);
            
        }
        
        @Test
        @DatabaseSetup("variants-delete.xml")
        @DatabaseTearDown("teardown.xml")
        @ExpectedDatabase(
                assertionMode = DatabaseAssertionMode.NON_STRICT,
                value = "classpath:/org/restfulwhois/rdap/dao/impl/variants-update.xml")
        public void testUpdateVariant() throws Exception {
        	Domain domain = new Domain();
	    	domain.setId(1L);
	    	List<VariantDto> variantsList = createVariants();
            updateDao.updateAsInnerObjects(domain, variantsList);
            
        }
}
