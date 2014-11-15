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
package org.restfulwhois.rdap.core.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.core.common.dao.AbstractQueryDao;
import org.restfulwhois.rdap.core.common.model.Link;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * link query DAO select link object from RDAP_LINK.
 * there are title and Hreflang details in link object.
 * 
 * @author jiashuo
 * 
 */
@Repository
public class LinkQueryDaoImpl extends AbstractQueryDao<Link> {
  
    /**
     * logger for record log.
     */
    protected static Logger logger = LoggerFactory
            .getLogger(LinkQueryDaoImpl.class);

    @Override
    public List<Link> queryAsInnerObjects(final Long outerObjectId,
            final ModelType outerModelType) {
        List<Link> linksWithHreflang = queryLinkWithHreflang(outerObjectId,
                outerModelType);        
        return linksWithHreflang;
    }
   

    /**
     * query link with hreflang as inner objects for outer object.
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
                + " on (rel.LINK_ID = link.LINK_ID and rel.REL_ID = ?"
                + " and rel.REL_OBJECT_TYPE = ? ) "
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
     * link ResultSetExtractor extract data from ResultSet.
     * 
     * @author jiashuo
     * 
     */
    class LinkWithHreflangResultSetExtractor implements
            ResultSetExtractor<List<Link>> {
        @Override
        public List<Link> extractData(ResultSet rs) throws SQLException {
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
                    link.setTitle(rs.getString("TITLE"));
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
     *          link to encode uri
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
         * link id.
         */
        private Long linkId;
        /**
         * link title string.
         */
        private String title;

        /**
         * get the link id.
         * 
         * @return link id.
         */
        public Long getLinkId() {
            return linkId;
        }

        /**
         * set the link id.
         * 
         * @param linkId
         *            link id will be set.
         */
        public void setLinkId(Long linkId) {
            this.linkId = linkId;
        }

        /**
         * get title of link.
         * 
         * @return title string.
         */
        public String getTitle() {
            return title;
        }

        /**
         * set title of link.
         * 
         * @param title
         *            the title will be set.
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * construct the link Title.
         * 
         * @param linkId
         *            linkId from construct.
         * @param title
         *            title from construct.
         */
        public LinkTitle(Long linkId, String title) {
            super();
            this.linkId = linkId;
            this.title = title;
        }
    }
}
