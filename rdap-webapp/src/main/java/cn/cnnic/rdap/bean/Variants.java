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
 * list of variants,which have same relations.
 * 
 * @author jiashuo
 * 
 */
public class Variants extends BaseModel {
    /**
     * an array of strings, with each string denoting the relationship between
     * the variants and the containing domain object.
     */
    private List<String> relation;
    /**
     * the name of the IDN table of codepoints, such as one listed with the
     * IANA.
     */
    private String idnTable;
    /**
     * an array of objects, with each object containing an "ldhName" member and
     * a "unicodeName" member.
     */
    private List<Variant> variantNames;

    /**
     * check if equals for relation and idntable.
     * 
     * @param variant
     *            variant.
     * @return true if equals for relation and idntable,false if not.
     */
    public boolean equalsForRelationAndIdnTable(Variant variant) {
        if (null == variant) {
            return false;
        }
        if (!StringUtils.equals(this.getIdnTable(), variant.getIdnTable())) {
            return false;
        }
        return variant.getSortedRelationStringList().equals(this.getRelation());
    }

    /**
     * add relation string.
     * 
     * @param relationStr
     *            relation string.
     */
    public void addRelation(String relationStr) {
        if (null == relation) {
            relation = new ArrayList<String>();
        }
        relation.add(relationStr);
    }

    /**
     * add variant to variantNames.
     * 
     * @param variant
     *            variant.
     */
    public void addVariant(Variant variant) {
        if (null == variantNames) {
            variantNames = new ArrayList<Variant>();
        }
        variantNames.add(variant);
    }

    /**
     * get relation list.
     * 
     * @return relation list.
     */
    public List<String> getRelation() {
        return relation;
    }

    /**
     * set relation list.
     * 
     * @param relation
     *            relation list.
     */
    public void setRelation(List<String> relation) {
        this.relation = relation;
    }

    /**
     * get idn table.
     * 
     * @return idn table.
     */
    public String getIdnTable() {
        return idnTable;
    }

    /**
     * set idn table.
     * 
     * @param idnTable
     *            idn table.
     */
    public void setIdnTable(String idnTable) {
        this.idnTable = idnTable;
    }

    /**
     * get variantNames.
     * 
     * @return variantNames.
     */
    public List<Variant> getVariantNames() {
        return variantNames;
    }

    /**
     * set variantNames.
     * 
     * @param variantNames
     *            variantNames.
     */
    public void setVariantNames(List<Variant> variantNames) {
        this.variantNames = variantNames;
    }
    
    @Override
    public ModelType getObjectType() {
        return ModelType.VARIANT;
    }
}
