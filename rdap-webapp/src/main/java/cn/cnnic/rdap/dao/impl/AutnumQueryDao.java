/* Copyright (c) 2012 - 2015, Internet Corporation for Assigned Names and Numbers 
 * (ICANN) and China Internet Network Information Center (CNNIC)
 *
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 *  are permitted provided that the following conditions are met:
 *
 **Redistributions of source code must retain the above copyright notice, this list 
 * of conditions and the following disclaimer.
 * 
 **Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other materials 
 * provided with the distribution.
 * 
 **Neither the name of the ICANN, CNNIC nor the names of its contributors may be used to 
 * endorse or promote products derived from this software without specific prior written 
 * permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL ICANN OR CNNIC BE LIABLE FOR ANY DIRECT, INDIRECT, 
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT 
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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

import cn.cnnic.rdap.bean.Autnum;
import cn.cnnic.rdap.bean.Event;
import cn.cnnic.rdap.bean.Link;
import cn.cnnic.rdap.bean.ModelType;
import cn.cnnic.rdap.bean.QueryParam;
import cn.cnnic.rdap.bean.Remark;
import cn.cnnic.rdap.dao.AbstractQueryDao;
import cn.cnnic.rdap.dao.QueryDao;

/**
 * autnum query DAO
 * 
 * @author jiashuo
 * 
 */
@Repository
public class AutnumQueryDao extends AbstractQueryDao<Autnum> {
	@Autowired
	@Qualifier("remarkQueryDao")
	private QueryDao<Remark> remarkQueryDao;
	@Autowired
	@Qualifier("linkQueryDao")
	private QueryDao<Link> linkQueryDao;
	@Autowired
	@Qualifier("eventQueryDao")
	private QueryDao<Event> eventQueryDao;

	/**
	 * query autnum.If more than one results exist, the minimal (exact) result
	 * will returned.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Autnum query(QueryParam queryParam) {
		Autnum autnum = queryWithoutInnerObjects(queryParam);
		queryAndSetInnerObjects(autnum);
		return autnum;
	}

	/**
	 * query inner objects of autnum,and set fill them to autnum
	 * 
	 * @param autnum
	 *            inner objects will be filled
	 */
	private void queryAndSetInnerObjects(Autnum autnum) {
		if (null == autnum) {
			return;
		}
		Long autnumId = autnum.getId();
		List<Remark> remarks = remarkQueryDao.queryAsInnerObjects(autnumId,
				ModelType.AUTNUM);
		autnum.setRemarks(remarks);
		List<Link> links = linkQueryDao.queryAsInnerObjects(autnumId,
				ModelType.AUTNUM);
		autnum.setLinks(links);
		List<Event> events = eventQueryDao.queryAsInnerObjects(autnumId,
				ModelType.AUTNUM);
		autnum.setEvents(events);
	}

	/**
	 * query autnum, without inner objects
	 * 
	 * @param queryParam
	 *            query parameter
	 * @return autnum
	 */
	private Autnum queryWithoutInnerObjects(QueryParam queryParam) {
		final String autnumQ = queryParam.getQ();
		final String sql = "select *,end_autnum - start_autnum as asInterval "
				+ " from RDAP_AUTNUM autnum join RDAP_AUTNUM_STATUS status "
				+ " on autnum.as_id = status.as_id "
				+ "where autnum.start_autnum <= ? and end_autnum >= ? order by asInterval ";
		List<Autnum> result = jdbcTemplate.query(
				new PreparedStatementCreator() {
					public PreparedStatement createPreparedStatement(
							Connection connection) throws SQLException {
						PreparedStatement ps = connection.prepareStatement(sql);
						ps.setString(1, autnumQ);
						ps.setString(2, autnumQ);
						return ps;
					}
				}, new AutnumResultSetExtractor());
		Autnum autnum = null;
		if (null != result && result.size() > 0) {
			autnum = result.get(0);
		}
		return autnum;
	}

	/**
	 * autnum ResultSetExtractor, extract data from ResultSet
	 * 
	 * @author jiashuo
	 * 
	 */
	class AutnumResultSetExtractor implements ResultSetExtractor<List<Autnum>> {
		@Override
		public List<Autnum> extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			List<Autnum> result = new ArrayList<Autnum>();
			Map<Long, Autnum> autnumMapById = new HashMap<Long, Autnum>();
			while (rs.next()) {
				Long autnumId = rs.getLong("as_id");
				Autnum autnum = autnumMapById.get(autnumId);
				if (null == autnum) {
					autnum = new Autnum();
					autnum.setId(autnumId);
					autnum.setStartAutnum(rs.getLong("START_AUTNUM"));
					autnum.setEndAutnum(rs.getLong("END_AUTNUM"));
					autnum.setName(rs.getString("NAME"));
					autnum.setType(rs.getString("TYPE"));
					autnum.setCountry(rs.getString("COUNTRY"));
					autnum.setLang(rs.getString("LANG"));
					autnum.setPort43(rs.getString("PORT43"));
					result.add(autnum);
					autnumMapById.put(autnumId, autnum);
				}
				autnum.addStatus(rs.getString("STATUS"));
			}
			return result;
		}
	}
}