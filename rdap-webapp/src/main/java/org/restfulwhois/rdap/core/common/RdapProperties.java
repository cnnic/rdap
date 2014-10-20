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
package org.restfulwhois.rdap.core.common;

import java.util.List;

import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * This class contains variables.
 * <p>
 * Variables in this class is set by spring, from file 'rdap.properties'.
 * <p>
 * Bean 'rdapProperties' MUST configured in spring configuration file,and now
 * configuration file is load by {@link PropertyPlaceholderConfigurer}.
 * <p>
 * If property is changed, system need restart to reload it.
 * 
 * @author jiashuo
 * 
 */
public class RdapProperties {

    /**
     * bootstrapRegistryBaseUrl.
     */
    private static String bootstrapRegistryBaseUrl;

    /**
     * bootstrapRegistryUriForDomain.
     */
    private static String bootstrapRegistryUriForDomain;
    /**
     * bootstrapRegistryUriForAs.
     */
    private static String bootstrapRegistryUriForAs;
    /**
     * bootstrapRegistryUriForIpv4.
     */
    private static String bootstrapRegistryUriForIpv4;
    /**
     * bootstrapRegistryUriForIpv6.
     */
    private static String bootstrapRegistryUriForIpv6;

    /**
     * max concurrent count.
     */
    private static Integer maxConcurrentCount;

    /**
     * localServiceUrl.
     */
    private static String localServiceUrl;

    /**
     * ip white list for access interval, these ip are not limited.
     */
    private static String ipWhiteListForAccessInterval;

    /**
     * min seconds between access interval,for Anonymous.
     */
    private static Long minSecondsAccessIntervalAnonymous;

    /**
     * min seconds between access interval, for authenticated user.
     */
    private static Long minSecondsAccessIntervalAuthed;

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
     * access control for inner object for search.
     */
    private static boolean accessControlForInnerObjectForSearch;
    
    /**
     * get accessControlForInnerObjectForSearch.
     * 
     * @return accessControlForInnerObjectForSearch.
     */
    public static boolean getAccessControlForInnerObjectForSearch() {
         return accessControlForInnerObjectForSearch;
    }
    
    /**
     * set accessControlForInnerObjectForSearch.
     * 
     * @param accessControlForInnerObjectForSearch
     *            accessControlForInnerObjectForSearch.
     */
    public static void setAccessControlForInnerObjectForSearch(
           boolean accessControlForInnerObjectForSearch) {
        RdapProperties.accessControlForInnerObjectForSearch = 
                   accessControlForInnerObjectForSearch;
    }

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
                StringUtil.parseSeparatedStringToListIfListIsNull(inTlds,
                        inTldList);
        return RdapProperties.inTldList;
    }

    /**
     * get not-in-tlds in this registry.
     * 
     * @return tld list.
     */
    public static List<String> getNotInTldsInThisRegistry() {
        RdapProperties.notInTldList =
                StringUtil.parseSeparatedStringToListIfListIsNull(notInTlds,
                        notInTldList);
        return RdapProperties.notInTldList;
    }

    /**
     * get ip white list for access interval.
     * 
     * @return list.
     */
    public static List<String> getIpWhiteListArrayForAccessInterval() {
        List<String> ipWhiteListForAccessIntervalList = null;
        ipWhiteListForAccessIntervalList =
                StringUtil.parseSeparatedStringToListIfListIsNull(
                        ipWhiteListForAccessInterval,
                        ipWhiteListForAccessIntervalList);
        return ipWhiteListForAccessIntervalList;
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

    /**
     * get minSecondsAccessIntervalAnonymous.
     * 
     * @return minSecondsAccessIntervalAnonymous.
     */
    public static Long getMinSecondsAccessIntervalAnonymous() {
        return minSecondsAccessIntervalAnonymous;
    }

    /**
     * set minSecondsAccessIntervalAnonymous.
     * 
     * @param minSecondsAccessIntervalAnonymous
     *            minSecondsAccessIntervalAnonymous.
     */
    public void setMinSecondsAccessIntervalAnonymous(
            Long minSecondsAccessIntervalAnonymous) {
        RdapProperties.minSecondsAccessIntervalAnonymous =
                minSecondsAccessIntervalAnonymous;
    }

    /**
     * get minSecondsAccessIntervalAuthed.
     * 
     * @return minSecondsAccessIntervalAuthed.
     */
    public static Long getMinSecondsAccessIntervalAuthed() {
        return minSecondsAccessIntervalAuthed;
    }

    /**
     * set minSecondsAccessIntervalAuthed.
     * 
     * @param minSecondsAccessIntervalAuthed
     *            minSecondsAccessIntervalAuthed.
     */
    public void setMinSecondsAccessIntervalAuthed(
            Long minSecondsAccessIntervalAuthed) {
        RdapProperties.minSecondsAccessIntervalAuthed =
                minSecondsAccessIntervalAuthed;
    }

    /**
     * get localServiceUrl.
     * 
     * @return localServiceUrl.
     */
    public static String getLocalServiceUrl() {
        return localServiceUrl;
    }

    /**
     * set localServiceUrl.
     * 
     * @param localServiceUrl
     *            localServiceUrl.
     */
    public void setLocalServiceUrl(String localServiceUrl) {
        RdapProperties.localServiceUrl = localServiceUrl;
    }

    /**
     * get maxConcurrentCount.
     * 
     * @return maxConcurrentCount.
     */
    public static Integer getMaxConcurrentCount() {
        return maxConcurrentCount;
    }

    /**
     * set maxConcurrentCount.
     * 
     * @param maxConcurrentCount
     *            maxConcurrentCount.
     */
    public void setMaxConcurrentCount(Integer maxConcurrentCount) {
        RdapProperties.maxConcurrentCount = maxConcurrentCount;
    }

    /**
     * get ipWhiteListForAccessInterval.
     * 
     * @return ipWhiteListForAccessInterval.
     */
    public static String getIpWhiteListForAccessInterval() {
        return ipWhiteListForAccessInterval;
    }

    /**
     * set ipWhiteListForAccessInterval.
     * 
     * @param ipWhiteListForAccessInterval
     *            ipWhiteListForAccessInterval.
     */
    public void setIpWhiteListForAccessInterval(
            String ipWhiteListForAccessInterval) {
        RdapProperties.ipWhiteListForAccessInterval =
                ipWhiteListForAccessInterval;
    }

    /**
     * get bootstrapRegistryBaseUrl.
     * 
     * @return bootstrapRegistryBaseUrl.
     */
    public static String getBootstrapRegistryBaseUrl() {
        return bootstrapRegistryBaseUrl;
    }

    /**
     * set bootstrapRegistryBaseUrl.
     * 
     * @param bootstrapRegistryBaseUrl
     *            bootstrapRegistryBaseUrl.
     */
    public void setBootstrapRegistryBaseUrl(String bootstrapRegistryBaseUrl) {
        RdapProperties.bootstrapRegistryBaseUrl = bootstrapRegistryBaseUrl;
    }

    /**
     * get bootstrapRegistryUriForDomain.
     * 
     * @return bootstrapRegistryUriForDomain.
     */
    public static String getBootstrapRegistryUriForDomain() {
        return bootstrapRegistryUriForDomain;
    }

    /**
     * set bootstrapRegistryUriForDomain.
     * 
     * @param bootstrapRegistryUriForDomain
     *            bootstrapRegistryUriForDomain.
     */
    public static void setBootstrapRegistryUriForDomain(
            String bootstrapRegistryUriForDomain) {
        RdapProperties.bootstrapRegistryUriForDomain =
                bootstrapRegistryUriForDomain;
    }

    /**
     * get bootstrapRegistryUriForAs.
     * 
     * @return bootstrapRegistryUriForAs.
     */
    public static String getBootstrapRegistryUriForAs() {
        return bootstrapRegistryUriForAs;
    }

    /**
     * set bootstrapRegistryUriForAs.
     * 
     * @param bootstrapRegistryUriForAs
     *            bootstrapRegistryUriForAs.
     */
    public static void setBootstrapRegistryUriForAs(
            String bootstrapRegistryUriForAs) {
        RdapProperties.bootstrapRegistryUriForAs = bootstrapRegistryUriForAs;
    }

    /**
     * get bootstrapRegistryUriForIpv4.
     * 
     * @return bootstrapRegistryUriForIpv4.
     */
    public static String getBootstrapRegistryUriForIpv4() {
        return bootstrapRegistryUriForIpv4;
    }

    /**
     * set bootstrapRegistryUriForIpv4.
     * 
     * @param bootstrapRegistryUriForIpv4
     *            bootstrapRegistryUriForIpv4.
     */
    public static void setBootstrapRegistryUriForIpv4(
            String bootstrapRegistryUriForIpv4) {
        RdapProperties.bootstrapRegistryUriForIpv4 =
                bootstrapRegistryUriForIpv4;
    }

    /**
     * get bootstrapRegistryUriForIpv6.
     * 
     * @return bootstrapRegistryUriForIpv6.
     */
    public static String getBootstrapRegistryUriForIpv6() {
        return bootstrapRegistryUriForIpv6;
    }

    /**
     * set bootstrapRegistryUriForIpv6.
     * 
     * @param bootstrapRegistryUriForIpv6
     *            bootstrapRegistryUriForIpv6.
     */
    public static void setBootstrapRegistryUriForIpv6(
            String bootstrapRegistryUriForIpv6) {
        RdapProperties.bootstrapRegistryUriForIpv6 =
                bootstrapRegistryUriForIpv6;
    }

}
