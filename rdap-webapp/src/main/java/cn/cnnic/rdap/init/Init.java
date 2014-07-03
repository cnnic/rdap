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
package cn.cnnic.rdap.init;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.cnnic.rdap.init.mysql.InitDao;

/**
 * init database schema and import data.
 * 
 * @author jiashuo
 * 
 */
public class Init {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InitDao.class);

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
        if (!(isInitSchemaCmd(arg) || isInitDataCmd(arg))) {
            printUsage();
            return;
        }
        ApplicationContext ctx =
                new ClassPathXmlApplicationContext(
                        "classpath:init/spring-serviceContext-init.xml");
        InitDao initDao = (InitDao) ctx.getBean("initDao");
        if (isInitSchemaCmd(arg)) {
            initDao.initSchema();
        }
        if (isInitDataCmd(arg)) {
            initDao.initData();
        }
        LOGGER.info("init successful...............");
    }

    private static void printUsage() {
        LOGGER.info("usage:");
        LOGGER.info("first init schema:");
        LOGGER.info("   java cn.cnnic.rdap.init.Init schema");
        LOGGER.info("if you are testing,you may init test data:");
        LOGGER.info("   java cn.cnnic.rdap.init.Init data");
    }

    private static boolean isInitDataCmd(String arg) {
        return arg.equalsIgnoreCase("data");
    }

    private static boolean isInitSchemaCmd(String arg) {
        return arg.equalsIgnoreCase("schema");
    }

}
