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
package cn.cnnic.rdap.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Nameserver;
import cn.cnnic.rdap.bean.NameserverQueryParam;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.IPAddress;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * nameserver query DAO.
 * 
 * @author weijunkai
 * 
 */
@Repository
public class NameserverQueryDaoImpl extends AbstractQueryDao<Nameserver> {
    /**
     * remark dao.
     */
    @Autowired
    @Qualifier("remarkQueryDaoImpl")
    private QueryDao<Remark> remarkQueryDao;
    /**
     * link dao.
     */
    @Autowired
    @Qualifier("linkQueryDaoImpl")
    private QueryDao<Link> linkQueryDao;
    /**
     * event dao.
     */
    @Autowired
    @Qualifier("eventQueryDaoImpl")
    private QueryDao<Event> eventQueryDao;
    /**
     * IPAddress dao.
     */
    @Autowired
    private QueryDao<IPAddress> ipAddressQueryDao;

    /**
     * query inner objects of nameserver,and set object value to them.
     * 
     * @param outerObjectId
     *            id from domain.
     * 
     * @param outerModelType
     *            type of the object
     * 
     * @return result for List<Nameserver>
     */
    private List<Nameserver> queryNameserverWithDomainID(
            final Long outerObjectId, ModelType outerModelType) {
        final String sql = "select * from RDAP_NAMESERVER ns inner join "
                + "REL_DOMAIN_NAMESERVER rel on (ns.NAMESERVER_ID = "
                + "rel.NAMESERVER_ID and rel.DOMAIN_ID = ?) left outer "
                + "join RDAP_NAMESERVER_STATUS status on ns.NAMESERVER_ID "
                + "=status.NAMESERVER_ID";

        List<Nameserver> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setLong(1, outerObjectId);
                        return ps;
                    }
                }, new NSResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }

        return result;
    }

    @Override
    public List<Nameserver> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        List<Nameserver> listNameserver = queryNameserverWithDomainID(
                outerObjectId, outerModelType);
        final int sizeNameserver = listNameserver.size();
        for (int i = 0; i < sizeNameserver; ++i) {
            queryAndSetInnerObjects(listNameserver.get(i));
        }
        return listNameserver;
    }

    @Override
    public Nameserver query(QueryParam queryParam) {
        Nameserver ns = queryWithoutInnerObjects(queryParam);
        queryAndSetInnerObjects(ns);
        return ns;
    }

    /**
     * query inner objects of nameserver,and set object value to them.
     * 
     * @param ns
     *            nameserver which inner objects value will be filled.
     */
    private void queryAndSetInnerObjects(Nameserver ns) {
        if (null == ns) {
            return;
        }
        Long nsID = ns.getId();
        List<IPAddress> listIPAddress = ipAddressQueryDao.queryAsInnerObjects(
                nsID, ModelType.NAMESERVER);
        if (listIPAddress.size() > 0) {
            IPAddress objIPAddress = listIPAddress.get(0);
            ns.setIpAddresses(objIPAddress);
        }
        List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(nsID,
                ModelType.NAMESERVER);
        ns.setRemarks(remarks);
        List<Link> links = linkQueryDao.queryAsInnerObjects(nsID,
                ModelType.NAMESERVER);
        ns.setLinks(links);
        List<Event> events = eventQueryDao.queryAsInnerObjects(nsID,
                ModelType.NAMESERVER);
        ns.setEvents(events);
    }

    /**
     * query nameserver, without inner objects.
     * 
     * @param queryParam
     *            query parameter
     * @return autnum
     */
    private Nameserver queryWithoutInnerObjects(QueryParam queryParam) {
        NameserverQueryParam nsQueryParam = (NameserverQueryParam) queryParam;
        final String nsName = nsQueryParam.getQ();
        final String punyName = nsQueryParam.getPunyName();
        final String sql = "select * from RDAP_NAMESERVER ns "
                + " left outer join RDAP_NAMESERVER_STATUS status "
                + " on ns.NAMESERVER_ID = status.NAMESERVER_ID "
                + " where LDH_NAME= ? or UNICODE_NAME= ?";
        List<Nameserver> result = jdbcTemplate.query(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(
                            Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, punyName);
                        ps.setString(2, nsName);
                        return ps;
                    }
                }, new NSResultSetExtractor());
        if (null == result || result.size() == 0) {
            return null;
        }
        return result.get(0);
    }

    /**
     * nameserver NSResultInnerExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class NSResultInnerExtractor implements
            ResultSetExtractor<List<Nameserver>> {
        @Override
        public List<Nameserver> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Nameserver> result = new ArrayList<Nameserver>();
            Map<Long, Nameserver> nsMapById = new HashMap<Long, Nameserver>();
            while (rs.next()) {
                Long nsId = rs.getLong("NAMESERVER_ID");
                Nameserver ns = nsMapById.get(nsId);
                if (null == ns) {
                    ns = new Nameserver();
                    ns.setId(nsId);
                    ns.setHandle(rs.getString("HANDLE"));
                    ns.setLdhName(rs.getString("LDH_NAME"));
                    ns.setUnicodeName(rs.getString("UNICODE_NAME"));
                    ns.setPort43(rs.getString("PORT43"));
                    ns.setLang(rs.getString("LANG"));
                    result.add(ns);
                    nsMapById.put(nsId, ns);
                }
            }
            return result;
        }
    }

    /**
     * nameserver ResultSetExtractor, extract data from ResultSet.
     * 
     * @author weijunkai
     * 
     */
    class NSResultSetExtractor implements ResultSetExtractor<List<Nameserver>> {
        @Override
        public List<Nameserver> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Nameserver> result = new ArrayList<Nameserver>();
            Map<Long, Nameserver> nsMapById = new HashMap<Long, Nameserver>();
            while (rs.next()) {
                Long nsId = rs.getLong("NAMESERVER_ID");
                Nameserver ns = nsMapById.get(nsId);
                if (null == ns) {
                    ns = new Nameserver();
                    ns.setId(nsId);
                    ns.setHandle(rs.getString("HANDLE"));
                    ns.setLdhName(rs.getString("LDH_NAME"));
                    ns.setUnicodeName(rs.getString("UNICODE_NAME"));
                    ns.setPort43(rs.getString("PORT43"));
                    ns.setLang(rs.getString("LANG"));
                    result.add(ns);
                    nsMapById.put(nsId, ns);
                }
                ns.addStatus(rs.getString("STATUS"));
            }
            return result;
        }
    }
}
