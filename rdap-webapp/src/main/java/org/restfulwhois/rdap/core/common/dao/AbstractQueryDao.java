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
package org.restfulwhois.rdap.core.common.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.restfulwhois.rdap.core.common.model.base.BaseModel;
import org.restfulwhois.rdap.core.common.model.base.ModelType;
import org.restfulwhois.rdap.core.common.support.QueryParam;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * <pre>
 * This is the abstract class in this package, base class for all query DAO.
 * Using jdbc to execute sql query.
 * Pass the query parameter by QueryParam class.
 * </pre>
 * 
 * @param <T>
 *          object derived from BaseModel.
 * @author jiashuo
 * 
 */
public abstract class AbstractQueryDao<T extends BaseModel> implements
        QueryDao<T> {
    /**
     * logger.
     */
    protected static final Logger LOGGER = LoggerFactory
            .getLogger(AbstractQueryDao.class);

    /**
     * %:used for SQL 'like' clause.
     */
    private static final String CHAR_PERCENT = "%";
    /**
     * JDBC template simplifies the use of JDBC and helps to avoid common
     * errors.
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    /**
     * query result of object T.
     * 
     * @param queryParam
     *            query object id/name.
     * @return T
     *            here is an abstract method.
     * 
     * @throws UnsupportedOperationException.
     *             
     */
    @Override
    public T query(QueryParam queryParam) {
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }
    /**
     * query results of object list of T to an associated object.
     *   ie. domain to name servers,
     *       use queryAsInnerObjects(domainId) to query name servers
     * @param outerObjectId
     *            associated object id.
     * @param outerModelType
     *            associated object type.            
     * @return List<T>
     *            here is an abstract method.
     * 
     * @throws UnsupportedOperationException.
     *             
     */
    @Override
    public List<T> queryAsInnerObjects(Long outerObjectId,
            ModelType outerModelType) {
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }
    
    
    @Override
    public void queryAndSetInnerObjectsForSearch(List<T> result) {
        throw new UnsupportedOperationException(
                "must be implemented in sub class if I'am called.");
    }

    /**
     * generate SQL 'like' clause:replace '*' with '%'.
     * 
     * @param q
     *            query string.
     * @return string.
     */
    public static String generateLikeClause(String q) {
        if (StringUtils.isBlank(q)) {
            return q;
        }
        return q.replace(StringUtil.ASTERISK, CHAR_PERCENT);
    }

    /**
     * get id from object list.
     * 
     * @param baseModelObjects
     *            object list.
     * @return object id list.
     */
    protected List<Long>
    getModelIds(List<? extends BaseModel> baseModelObjects) {
        List<Long> result = new ArrayList<Long>();
        if (null == baseModelObjects) {
            return result;
        }
        for (BaseModel baseModelObj : baseModelObjects) {
            result.add(baseModelObj.getId());
        }
        return result;
    }

    /**
     * extract timestamp value from ResultSet.
     * @param rs
     *          set of query result.
     * @param columnName
     *          string of column name.
     * @return timestamp str value if value is valid format, null if not.
     */
    protected String extractTimestampFromRs(ResultSet rs, String columnName) {
        try {
            String dateTimeStr = rs.getString(columnName);
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(dateTimeStr);
            return DateFormatUtils.format(date, "yyyy-MM-dd'T'HH:mm:ss'Z'");
        } catch (Exception e) {
            LOGGER.error("error timestamp format,error:", e);
            return null;
        }
    }
    /**
     * get integer from ResultSet.rs.getInt() will return 0 for null value.
     * 
     * @param rs
     *            ResultSet.
     * @param columnName
     *            column name.
     * @return integer if value is not blank, null if blank.
     * @throws SQLException
     *             SQLException
     */
    protected Integer getIntegerFromRs(ResultSet rs, String columnName)
            throws SQLException {
        String str = rs.getString(columnName);
        Integer intVal = null;
        if (StringUtils.isNotBlank(str)) {
            intVal = Integer.valueOf(str);
        }
        return intVal;
    }
    
}
