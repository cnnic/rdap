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
package cn.cnnic.rdap.init.mysql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import cn.cnnic.rdap.init.InitContext;

/**
 * init schema and data dao.
 * 
 * @author jiashuo
 * 
 */
public class InitDao {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitDao.class);
    /**
     * init schema file path.
     */
    private static final String SQL_RESOURCE_SCHEMA_PATH =
            "classpath:init/mysql/init-schema.sql";
    /**
     * init data file path.
     */
    private static final String SQL_RESOURCE_DATA_PATH =
            "classpath:init/mysql/init-data.sql";
    /**
     * jdbcTemplate.
     */
    private JdbcTemplate jdbcTemplate;
    /**
     * init context.
     */
    private InitContext initContext;

    /**
     * db name.
     */
    @Value("${jdbc.url.dbName}")
    private String databaseName;

    /**
     * init schema.
     */
    public void initSchema() {
        LOGGER.info("initSchema begin...");
        LOGGER.info("file:{},database:{}", SQL_RESOURCE_SCHEMA_PATH,
                databaseName);
        initContext.executeSqlScript(jdbcTemplate, SQL_RESOURCE_SCHEMA_PATH,
                databaseName, false);
        LOGGER.info("initSchema end.");
    }

    /**
     * initData.
     */
    public void initData() {
        LOGGER.info("initData begin...");
        LOGGER.info("file:{},database:{}", SQL_RESOURCE_DATA_PATH, databaseName);
        initContext.executeSqlScript(jdbcTemplate, SQL_RESOURCE_DATA_PATH,
                databaseName, false);
        LOGGER.info("initData end.");
    }

    /**
     * set databaseName.
     * @param databaseName databaseName.
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * set jdbcTemplate.
     * 
     * @param jdbcTemplate
     *            jdbcTemplate
     */
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * set initContext.
     * 
     * @param initContext
     *            initContext.
     */
    public void setInitContext(InitContext initContext) {
        this.initContext = initContext;
    }

}
