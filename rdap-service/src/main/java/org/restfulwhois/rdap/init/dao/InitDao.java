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
package org.restfulwhois.rdap.init.dao;

import org.restfulwhois.rdap.init.InitContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

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
     * combo pool data source.
     */
    @Autowired
    private ComboPooledDataSource dataSource;
    /**
     * drop db sql.
     */
    private static final String SQL_DROP_DB = "DROP DATABASE IF EXISTS `%s`";
    /**
     * create db sql.
     */
    private static final String SQL_CREATE_DB =
            "CREATE DATABASE `%s` /*!40100 DEFAULT CHARACTER SET"
            + " utf8 COLLATE utf8_bin */;";
    /**
     * db name.
     */
    @Value("${jdbc.url.dbName}")
    private String databaseName;

    /**
     * jdbc url host and port.
     */
    @Value("${jdbc.url.hostPort}")
    private String databaseHostAndPort;

    /**
     * jdbc url params.
     */
    @Value("${jdbc.url.params}")
    private String databaseUrlParams;

    /**
     * reset datasource url.
     */
    public void resetDataSourceUrl() {
        String jdbcUrl = getJdbcUrl();
        LOGGER.info("resetDataSource jdbc url ...");
        dataSource.setJdbcUrl(jdbcUrl);
    }

    /**
     * get jdbc url.
     * 
     * @return jdbc url.
     */
    private String getJdbcUrl() {
        String jdbcUrl = databaseHostAndPort + databaseName + "?"
                + databaseUrlParams;
        LOGGER.info("jdbc url:{}", jdbcUrl);
        return jdbcUrl;
    }

    /**
     * create database.
     */
    public void createDatabase() {
        LOGGER.info("init database begin...");
        LOGGER.info("drop database if exist :{}", databaseName);
        String dropSql = String.format(SQL_DROP_DB, databaseName);
        LOGGER.info("sql:{}", dropSql);
        jdbcTemplate.update(dropSql);
        LOGGER.info("create database :{}", databaseName);
        String createSql = String.format(SQL_CREATE_DB, databaseName);
        LOGGER.info("sql:{}", createSql);
        jdbcTemplate.update(createSql);
        LOGGER.info("init database end.");
    }

    /**
     * init base data:RDAP_ERRORMESSAGE and RDAP_CONFORMANCE.
     */
    @Deprecated
    public void initBaseData() {
        LOGGER.info("initBaseData begin...");
        LOGGER.info("file:{},database:{}", SQL_RESOURCE_DATA_PATH,
                databaseName);
        initContext.executeSqlScript(jdbcTemplate, SQL_RESOURCE_DATA_PATH,
                databaseName, false);
        LOGGER.info("initBaseData end.");
    }

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
     * 
     * @param dataFile
     *            dataFile.
     */
    public void initData(String dataFile) {
        LOGGER.info("initData begin...");
        LOGGER.info("data:{},database:{}", dataFile, databaseName);
        initContext.executeSqlScript(jdbcTemplate, dataFile, databaseName,
                false);
        LOGGER.info("initData end.");
    }

    /**
     * set databaseName.
     * 
     * @param databaseName
     *            databaseName.
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
     * set host and port.
     * 
     * @param databaseHostAndPort
     *            databaseHostAndPort
     */
    public void setDatabaseHostAndPort(String databaseHostAndPort) {
        this.databaseHostAndPort = databaseHostAndPort;
    }

    /**
     * set url params.
     * 
     * @param databaseUrlParams
     *            databaseUrlParams.
     */
    public void setDatabaseUrlParams(String databaseUrlParams) {
        this.databaseUrlParams = databaseUrlParams;
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

    /**
     * set datasource.
     * 
     * @param dataSource
     *            dataSource.
     */
    public void setDataSource(ComboPooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

}
