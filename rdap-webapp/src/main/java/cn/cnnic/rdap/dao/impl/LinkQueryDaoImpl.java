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

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.common.util.StringUtil;
import cn.cnnic.rdap.dao.AbstractQueryDao;

/**
 * link query DAO
 * 
 * @author jiashuo
 * 
 */
@Repository
public class LinkQueryDaoImpl extends AbstractQueryDao<Link> {
    private static Logger logger = LoggerFactory
            .getLogger(LinkQueryDaoImpl.class);

    /**
     * query link as inner objects of other object
     */
    @Override
    public List<Link> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        List<Link> linksWithHreflang = queryLinkWithHreflang(outerObjectId,
                outerModelType);
        List<Long> linksIds = getModelIds(linksWithHreflang);
        List<LinkTitle> linksTitle = queryLinksTitle(linksIds);
        List<Link> result = setTitleToLinks(linksWithHreflang, linksTitle);
        return result;
    }

    /**
     * set title to links. This method will modify links
     * 
     * @param links
     *            link list
     * @param linksTitle
     *            link title
     * @return modified link list
     */
    private List<Link> setTitleToLinks(List<Link> links,
            List<LinkTitle> linksTitle) {
        if (null == links || null == linksTitle || linksTitle.size() == 0) {
            return links;
        }
        for (LinkTitle linkTitle : linksTitle) {
            setTitleToLink(linkTitle, links);
        }
        return links;
    }

    /**
     * set title to link. This method will modify link in links
     * 
     * @param linkTitle
     *            link title
     * @param links
     *            link list
     */
    private void setTitleToLink(LinkTitle linkTitle, List<Link> links) {
        if (null == links) {
            return;
        }
        Link link = findLinkFromListById(linkTitle.getLinkId(), links);
        if (null != link) {
            link.addTitle(linkTitle.getTitle());
        }
    }

    /**
     * find link from link list by link id
     * 
     * @param linkId
     *            link id
     * @param links
     *            link list
     * @return link if find, null if not
     */
    private Link findLinkFromListById(Long linkId, List<Link> links) {
        if (null == links || null == linkId) {
            return null;
        }
        for (Link link : links) {
            if (linkId.equals(link.getId())) {
                return link;
            }
        }
        return null;
    }

    /**
     * query link's title
     * 
     * @param linksIds
     *            link id list
     * @return link title list
     */
    private List<LinkTitle> queryLinksTitle(List<Long> linksIds) {
        if (null == linksIds || linksIds.size() == 0) {
            return new ArrayList<LinkTitle>();
        }
        final String linksIdsJoinedByComma = StringUtils.join(linksIds, ",");
        final String sqlTpl = "select * from RDAP_LINK_TITLE where LINK_ID in (%s)";
        final String sql = String.format(sqlTpl, linksIdsJoinedByComma);
        List<LinkTitle> result = jdbcTemplate.query(sql,
                new RowMapper<LinkTitle>() {
                    @Override
                    public LinkTitle mapRow(ResultSet rs, int rowNum)
                            throws SQLException {
                        return new LinkTitle(rs.getLong("LINK_ID"), rs
                                .getString("TITLE"));
                    }

                });
        return result;
    }

    /**
     * query link with hreflang
     * 
     * @param outerObjectId
     *            object id of outer object
     * @param outerModelType
     *            model type of outer object
     * @return link list
     */
    private List<Link> queryLinkWithHreflang(final Long outerObjectId,
            final ModelType outerModelType) {
        final String sql = "select link.*,hreflang.HREFLANG from RDAP_LINK link"
                + " inner join REL_LINK_OBJECT rel "
                + " on (rel.LINK_ID = link.LINK_ID and rel.REL_ID = ? and rel.REL_OBJECT_TYPE =?) "
                + " left outer join RDAP_LINK_HREFLANG hreflang "
                + " on link.LINK_ID = hreflang.LINK_ID";
        List<Link> result = jdbcTemplate.query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(
                    Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setLong(1, outerObjectId);
                ps.setString(2, outerModelType.getName());
                return ps;
            }
        }, new LinkWithHreflangResultSetExtractor());
        return result;
    }

    /**
     * link ResultSetExtractor, extract data from ResultSet
     * 
     * @author jiashuo
     * 
     */
    class LinkWithHreflangResultSetExtractor implements
            ResultSetExtractor<List<Link>> {
        @Override
        public List<Link> extractData(ResultSet rs) throws SQLException,
                DataAccessException {
            List<Link> result = new ArrayList<Link>();
            Map<Long, Link> mapById = new HashMap<Long, Link>();
            while (rs.next()) {
                Long linkId = rs.getLong("LINK_ID");
                Link link = mapById.get(linkId);
                if (null == link) {
                    link = new Link();
                    link.setId(rs.getLong("LINK_ID"));
                    link.setValue(rs.getString("VALUE"));
                    link.setRel(rs.getString("REL"));
                    link.setHref(rs.getString("HREF"));
                    link.setMedia(rs.getString("MEDIA"));
                    link.setType(rs.getString("TYPE"));
                    result.add(link);
                    encodeUriAndSetToLink(link);
                    mapById.put(linkId, link);
                }
                link.addHreflang(rs.getString("HREFLANG"));
            }
            return result;
        }
    }

    /**
     * encode URI for properties 'value' and 'href' in link,and set properties
     * to link.
     * 
     * @param link
     */
    private void encodeUriAndSetToLink(Link link) {
        link.setHref(StringUtil.urlEncode(link.getHref()));
        link.setValue(StringUtil.urlEncode(link.getValue()));
    }

    /**
     * link title. Only used in LinkQueryDao class
     * 
     * @author jiashuo
     * 
     */
    class LinkTitle {
        /**
         * link id
         */
        private Long linkId;
        /**
         * link title str
         */
        private String title;

        public Long getLinkId() {
            return linkId;
        }

        public void setLinkId(Long linkId) {
            this.linkId = linkId;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public LinkTitle(Long linkId, String title) {
            super();
            this.linkId = linkId;
            this.title = title;
        }
    }
}