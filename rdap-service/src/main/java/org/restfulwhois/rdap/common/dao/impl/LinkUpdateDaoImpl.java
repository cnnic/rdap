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
package org.restfulwhois.rdap.common.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.common.dao.AbstractUpdateDao;
import org.restfulwhois.rdap.common.dto.embedded.LinkDto;
import org.restfulwhois.rdap.common.model.Link;
import org.restfulwhois.rdap.common.model.base.BaseModel;
import org.restfulwhois.rdap.common.model.base.ModelType;
import org.restfulwhois.rdap.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;



/**
 * @author zhanyq
 * 
 */
@Repository
public class LinkUpdateDaoImpl extends AbstractUpdateDao<Link, LinkDto> {
   /**
     * logger for record log.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(LinkUpdateDaoImpl.class);    
 

    @Override
    public Link save(Link model) {
        return null;
    }

    @Override
    public void update(Link model) {

    }

    @Override
    public void delete(Link model) {

    }
    /**
     * batch create link.
     * 
     * @param outerModel
     *         outer object
     * @param models
     *        links of outer Object
     */
    @Override
    public void saveAsInnerObjects(BaseModel outerModel,
             List<LinkDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        for (LinkDto linkDto: models) {
            Long linkId = createLink(linkDto);
            createRelLink(outerModel.getId(), outerModel.getObjectType(),
                      linkId);
            createLinkHreflang(linkDto, linkId);
        }
    }
    
    @Override
    public void updateAsInnerObjects(BaseModel outerModel,
             List<LinkDto> models) {
        if (null == models || models.size() == 0) {
             return;
        }
        deleteAsInnerObjects(outerModel);
        saveAsInnerObjects(outerModel, models);
    }

    @Override
    public void deleteAsInnerObjects(BaseModel outerModel) {
        if (null == outerModel) {
              return;
        }
        List<Long> linkIds = super.findIdsByOuterIdAndType(outerModel,
                  "LINK_ID", "REL_LINK_OBJECT");
        if (null != linkIds) {
            String linkIdStr = StringUtils.join(linkIds, ",");
            //delete link
            super.delete(linkIdStr, "RDAP_LINK", "LINK_ID");
            //delete link hreflang
            super.delete(linkIdStr, "RDAP_LINK_HREFLANG", "LINK_ID");
            super.deleteRel(outerModel, "REL_LINK_OBJECT");
        }
    }
    
   /**
     * create link hreflang.
     * @param linkDto 
     *        link
     * @param linkId
     *        linkId
     */
	private void createLinkHreflang(final LinkDto linkDto, final Long linkId) {
		final List<String> hreflang = linkDto.getHreflang();
		final List<String> notEmptyHreflangs = StringUtil.getNotEmptyStringList(hreflang);
		if(notEmptyHreflangs.isEmpty()){
		    return;
		}
		final String sql = "insert into RDAP_LINK_HREFLANG(HREFLANG, LINK_ID)"
                    +  " values (?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
		    public int getBatchSize() {
		        return notEmptyHreflangs.size();
		    }
		    @Override
              public void setValues(PreparedStatement ps, int i)
              	throws SQLException {
		    	ps.setString(1, notEmptyHreflangs.get(i));
                ps.setLong(2, linkId);                
              }
              	
		    });
	}
	/**
	 * 
	 * @param outerObjectId
	 *        object id of outer object
	 * @param outerModelType
	 *        model type of outer object
	 * @param linkId
	 *        linkId
	 */
	private void createRelLink(final Long outerObjectId, final ModelType
              outerModelType,	final Long linkId) {
		final String sql = "insert into REL_LINK_OBJECT(REL_ID,REL_OBJECT_TYPE,LINK_ID)"
                    +  " values (?,?,?)"; 		       
		       jdbcTemplate.update(new PreparedStatementCreator() {
		           public PreparedStatement createPreparedStatement(Connection connection) 
		                      throws SQLException {           
		            PreparedStatement ps = connection.prepareStatement(
		            		sql);
		            ps.setLong(1, outerObjectId);
		            ps.setString(2, outerModelType.getName());
		            ps.setLong(3, linkId);
              		return ps;
              		}
              	
		        });
		
	}
	/**
	 * @param link
	 *        link
	 * @return linkId.
	 */
    private Long createLink(final LinkDto link) {		
        final String sql = "insert into RDAP_LINK(VALUE,REL,HREF,MEDIA,TYPE,TITLE)"
	      +  " values (?,?,?,?,?,?)";    
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
        	public PreparedStatement createPreparedStatement(Connection connection) 
                      throws SQLException {           
             PreparedStatement ps = connection.prepareStatement(
            		 sql, Statement.RETURN_GENERATED_KEYS);
              	ps.setString(1, link.getValue());
              	ps.setString(2, link.getRel());
              	ps.setString(3, link.getHref());
              	ps.setString(4, link.getMedia());
              	ps.setString(5, link.getType());
              	ps.setString(6, link.getTitle());
              	return ps;
              }		
        }, keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Override
	public Long findIdByHandle(String handle) {
		// TODO Auto-generated method stub
		return null;
	}	
}
