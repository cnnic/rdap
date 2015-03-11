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
package org.restfulwhois.rdap.init;

import org.apache.commons.lang.StringUtils;
import org.restfulwhois.rdap.init.dao.InitDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * init database schema and import data.
 * 
 * @author jiashuo
 * 
 */
public final class Init {
    /**
     * private constructor.
     */
    private Init() {

    }

    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitDao.class);

    /**
     * spring create database conf file.
     */
    private static final String SPRING_CONF_CREATEDB =
            "spring-serviceContext-init-createDb.xml";
    /**
     * spring load data into database conf file.
     */
    private static final String SPRING_CONF_LOADDATA =
            "spring-serviceContext-init-loadData.xml";

    /**
     * main method.
     * 
     * @param args
     *            args.
     */
    public static void main(String[] args) {
        LOGGER.info("init begin...");
        LOGGER.info("args:{}", args);
        if (null == args || args.length < 1 || StringUtils.isBlank(args[0])) {
            LOGGER.info("args is null, end init.");
            printUsage();
            return;
        }
        String arg = args[0];
        if (!(isInitSchemaCmd(arg) || isInitDataCmd(args))) {
            printUsage();
            return;
        }
        if (isInitSchemaCmd(arg)) {
            initSchema();
        }
        if (isInitDataCmd(args)) {
            initData(args);
        }
        LOGGER.info("init end...............");
    }

    /**
     * init data.
     * 
     * @param args
     *            args.
     */
    private static void initData(String[] args) {
        try {
            ApplicationContext ctx = createSpringCtx(SPRING_CONF_LOADDATA);
            InitDao initDao = (InitDao) ctx.getBean("initDao");
            initDao.initData("file:" + args[1]);
        } catch (Exception e) {
            LOGGER.error("initData error:", e);
        }
    }

    /**
     * init schema.
     */
    private static void initSchema() {
        try {
            ApplicationContext ctx = createSpringCtx(SPRING_CONF_CREATEDB);
            InitDao initDao = (InitDao) ctx.getBean("initDao");
            initDao.createDatabase();
            initDao.resetDataSourceUrl();
            initDao.initSchema();
//            initDao.initBaseData();
        } catch (Exception e) {
            LOGGER.error("initSchema error:", e);
        }
    }

    /**
     * get file in classpath.
     * 
     * @param fileName
     *            fileName.
     * @return file path.
     */
    private static String getClassPathFile(String fileName) {
        return "classpath:init/" + fileName;
    }

    /**
     * create spring context.
     * 
     * @param ctxFile
     *            ctx file.
     * @return ApplicationContext.
     */
    private static ApplicationContext createSpringCtx(String ctxFile) {
        return new ClassPathXmlApplicationContext(getClassPathFile(ctxFile));
    }

    /**
     * logger to print messages.
     */
    private static void printUsage() {
        LOGGER.info("usage:");
        LOGGER.info("1.Before run this init, first configure database in "
                + "$TOMCAT_HOME/webapps/rdap/WEB-INF/classes/jdbc.properties");
        LOGGER.info("2.First init schema:");
        LOGGER.info("   java org.restfulwhois.rdap.init.Init initschema");
        LOGGER.info("3.If you want load test data into database, "
                + "run following command,"
                + " $ABS_DATAFILE_PATH MUST be replaced by "
                + "real data file name:");
        LOGGER.info("   java org.restfulwhois.rdap.init.Init loaddata "
                + "$ABS_DATAFILE_PATH");
    }

    /**
     * check if command is the init data.
     * 
     * @param args
     *            param to check.
     * @return if yes return true, or return false.
     */
    private static boolean isInitDataCmd(String[] args) {
        if (null == args || args.length < 2 || StringUtils.isBlank(args[0])
                || StringUtils.isBlank(args[1])) {
            return false;
        }
        return args[0].equalsIgnoreCase("initdata");
    }

    /**
     * check if is init schema command.
     * 
     * @param arg
     *            string to check.
     * @return if yes return true, or return false.
     */
    private static boolean isInitSchemaCmd(String arg) {
        return arg.equalsIgnoreCase("initschema");
    }

}
