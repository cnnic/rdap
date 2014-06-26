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
package cn.cnnic.rdap.common;

import java.util.List;

import cn.cnnic.rdap.common.util.StringUtil;

/**
 * static properties,load from property file,by spring.
 * 
 * @author jiashuo
 * 
 */
public class RdapProperties {

    /**
     * max size for search.
     */
    private static Long maxsizeSearch;
    /**
     * batch size for search.
     */
    private static Long batchsizeSearch;

    /**
     * tlds in this registry, splited by ';'.
     */
    private static String inTlds;

    /**
     * tlds not in this registry, splited by ';'.
     */
    private static String notInTlds;

    /**
     * list for in tlds.
     */
    private static List<String> inTldList;
    /**
     * list for not in tlds.
     */
    private static List<String> notInTldList;

    /**
     * clear 'in' and 'not in' tlds in memory,used for reload tlds.
     */
    public static void clearTldsInMemory() {
        RdapProperties.inTldList = null;
        RdapProperties.notInTldList = null;
    }

    /**
     * get in-tlds in this registry.
     * 
     * @return tld list.
     */
    public static List<String> getInTldsInThisRegistry() {
        RdapProperties.inTldList =
                StringUtil.parseTldsToListIfTldListIsNull(inTlds, inTldList);
        return RdapProperties.inTldList;
    }

    /**
     * get not-in-tlds in this registry.
     * 
     * @return tld list.
     */
    public static List<String> getNotInTldsInThisRegistry() {
        RdapProperties.notInTldList =
                StringUtil.parseTldsToListIfTldListIsNull(notInTlds,
                        notInTldList);
        return RdapProperties.notInTldList;
    }

    /**
     * get maxsizeSearch.
     * 
     * @return maxsizeSearch
     */
    public static Long getMaxsizeSearch() {
        return maxsizeSearch;
    }

    /**
     * set maxsizeSearch.
     * 
     * @param maxsizeSearch
     *            maxsizeSearch.
     */
    public void setMaxsizeSearch(Long maxsizeSearch) {
        RdapProperties.maxsizeSearch = maxsizeSearch;
    }

    /**
     * get batchsizeSearch.
     * 
     * @return batchsizeSearch.
     */
    public static Long getBatchsizeSearch() {
        return batchsizeSearch;
    }

    /**
     * set batchsizeSearch.
     * 
     * @param batchsizeSearch
     *            batchsizeSearch.
     */
    public void setBatchsizeSearch(Long batchsizeSearch) {
        RdapProperties.batchsizeSearch = batchsizeSearch;
    }

    /**
     * get tlds.
     * 
     * @return tlds.
     */
    public static String getInTlds() {
        return inTlds;
    }

    /**
     * set tlds.
     * 
     * @param tlds
     *            tlds.
     */
    public void setInTlds(String tlds) {
        RdapProperties.inTlds = tlds;
    }

    /**
     * get notInTlds.
     * 
     * @return notInTlds.
     */
    public static String getNotInTlds() {
        return notInTlds;
    }

    /**
     * set notInTlds.
     * 
     * @param notInTlds
     *            notInTlds.
     */
    public void setNotInTlds(String notInTlds) {
        RdapProperties.notInTlds = notInTlds;
    }

}
