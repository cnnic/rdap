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
package org.restfulwhois.rdap.core.common.support;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *  the truncated info include resultsTruncated hasNoAuthForAllObjects and  
 *  reasonTypeShortName.
 * 
 * @author zhanyq
 *            
 */
@JsonIgnoreProperties(value = {"resultsTruncated", "hasNoAuthForAllObjects",
        "reasonTypeShortName" })
public class TruncatedInfo {  

    /**
     * TruncatedReason enum.
     * 
     * @author jiashuo
     * 
     */
      public enum TruncateReason {
        /**
         * two  reason.
         */
       TRUNCATEREASON_AUTH("authorization"), TRUNCATEREASON_EXLOAD(
               "excessiveLoad");
        /**
         * type name.
         */
        private String name;

        /**
         * NoticeType.
         * 
         * @param name
         *            a string of type.
         */
        private TruncateReason(String name) {
            this.name = name;
        }

        /**
         * getName.
         * 
         * @return a string to get.
         */
        public String getName() {
            return name;
        }
    }


    /**
     * 'resultsTruncated' used where a single object has been returned and data
     * in that object has been truncated.
     */
    private Boolean resultsTruncated = false;

    /**
     * has no access to all objects, and should return HTTP 403 code.
     */
    private Boolean hasNoAuthForAllObjects = false;  
  
    /**
     * the reason of a single object has been returned and data
     * in that object has been truncated.
     */
   // private String reasonTypeShortName;
    
    private List<TruncateReason> truncateReasons;
    
    /**
     * reasonType authorization.
     */
   // public static final String REASONTYPE_AUTH = "authorization";
    
    /**
     * reasonType excessiveLoad.
     */
   // public static final String REASONTYPE_EXLOAD = "excessiveLoad";
    /**
     * reason type list.
     */
    public static final List<String> TYPES = new ArrayList<String>();
    
    static {
        TYPES.add("'" + TruncateReason.TRUNCATEREASON_AUTH.getName() + "'");
        TYPES.add("'" + TruncateReason.TRUNCATEREASON_EXLOAD.getName() + "'");
    }

    /**
     * get resultsTruncated.
     * 
     * @return resultsTruncated.
     */
    public Boolean getResultsTruncated() {
        return resultsTruncated;
    }

    /**
     * set resultsTruncated.
     * 
     * @param resultsTruncated
     *            resultsTruncated.
     */
    public void setResultsTruncated(Boolean resultsTruncated) {
        this.resultsTruncated = resultsTruncated;
    }

    /**
     * get hasNoAuthForAllObjects.
     * 
     * @return hasNoAuthForAllObjects.
     */
    public Boolean getHasNoAuthForAllObjects() {
        return hasNoAuthForAllObjects;
    }

    /**
     * set hasNoAuthForAllObjects.
     * 
     * @param hasNoAuthForAllObjects
     *            hasNoAuthForAllObjects.
     */
    public void setHasNoAuthForAllObjects(Boolean hasNoAuthForAllObjects) {
        this.hasNoAuthForAllObjects = hasNoAuthForAllObjects;
    }   

    /**
     * get truncateReasons.
     * 
     * @return truncateReasons.
     */
    public List<TruncateReason> getTruncateReasons() {
          return truncateReasons;
    }

    /**
     * set truncateReasons.
     * 
     * @param truncateReasons
     *            truncateReasons.
     */
    public void setTruncateReasons(List<TruncateReason> truncateReasons) {
        this.truncateReasons = truncateReasons;
    }   
    
    /**
     * set TruncatedReasonForAuth.
     *     
     */
   /* public void setTruncatedReasonForAuth() {
        this.reasonTypeShortName = REASONTYPE_AUTH;       
    }*/
    
    /**
     * add truncateReason.
     * @param truncateReason
     *  truncateReason.
     *     
     */   
    
    public void addTruncate(TruncateReason truncateReason) {
        if (null == this.truncateReasons) {
            this.truncateReasons = new ArrayList<TruncateReason>();
        }
        if (!this.truncateReasons.contains(truncateReason)) {
            this.truncateReasons.add(truncateReason);
            this.resultsTruncated = true;
        }
        
    }
    
}
