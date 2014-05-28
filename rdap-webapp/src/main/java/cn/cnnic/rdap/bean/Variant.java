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
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * variant for domain.
 * 
 * @author jiashuo
 * 
 */
@JsonIgnoreProperties(value = { "id", "objectType", "handle", "lang",
        "relations", "idnTable", "sortedRelationStringList" })
public class Variant extends BaseModel {
    /**
     * relation type.
     */
    private List<RelDomainVariant> relations;
    /**
     * name in LDH format.
     */
    private String ldhName;
    /**
     * name in unicode format.
     */
    private String unicodeName;
    /**
     * idn table.
     */
    private String idnTable;

    /**
     * add a relation.
     * 
     * @param relation
     *            relation.
     */
    public void addRelation(RelDomainVariant relation) {
        if (null == relation) {
            return;
        }
        if (null == relations) {
            relations = new ArrayList<RelDomainVariant>();
        }
        relations.add(relation);
    }

    /**
     * get sorted relation string list.
     * 
     * @return sorted relation string list.
     */
    public List<String> getSortedRelationStringList() {
        List<String> result = new ArrayList<String>();
        if (null == relations) {
            return result;
        }
        for (RelDomainVariant rel : relations) {
            result.add(rel.getVariantType());
        }
        Collections.sort(result);
        return result;
    }

    /**
     * get relations.
     * 
     * @return relations.
     */
    public List<RelDomainVariant> getRelations() {
        return relations;
    }

    /**
     * set relations.
     * 
     * @param relations
     *            relations.
     */
    public void setRelations(List<RelDomainVariant> relations) {
        this.relations = relations;
    }

    /**
     * get name in LDH format.
     * 
     * @return name in LDH format.
     */
    public String getLdhName() {
        return ldhName;
    }

    /**
     * set name in LDH format.
     * 
     * @param ldhName
     *            name in LDH format.
     */
    public void setLdhName(String ldhName) {
        this.ldhName = ldhName;
    }

    /**
     * get name in unicode format.
     * 
     * @return name in unicode format.
     */
    public String getUnicodeName() {
        return unicodeName;
    }

    /**
     * set name in unicode format.
     * 
     * @param unicodeName
     *            name in unicode format.
     */
    public void setUnicodeName(String unicodeName) {
        this.unicodeName = unicodeName;
    }

    /**
     * get the name of the IDN table of codepoints.
     * 
     * @return the name of the IDN table of codepoints.
     */
    public String getIdnTable() {
        return idnTable;
    }

    /**
     * set the name of the IDN table of codepoints.
     * 
     * @param idnTable
     *            the name of the IDN table of codepoints.
     */
    public void setIdnTable(String idnTable) {
        this.idnTable = idnTable;
    }
}
