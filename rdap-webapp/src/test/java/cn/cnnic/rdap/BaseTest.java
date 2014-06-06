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
package cn.cnnic.rdap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import cn.cnnic.rdap.bean.Principal;
import cn.cnnic.rdap.controller.support.PrincipalHolder;

import com.github.springtestdbunit.DbUnitTestExecutionListener;

/**
 * base class for all test classes.Test can rollback after test complete.
 * 
 * @author jiashuo
 * 
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {
        "/spring/spring-applicationContext-test.xml",
        "/spring/spring-servlet.xml" })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@Transactional
public abstract class BaseTest {

    /**
     * dataSource.
     */
    private static DataSource dataSource;

    /**
     * connection.
     */
    private static IDatabaseConnection connection;

    /**
     * BeforeClass.
     * 
     * @throws Exception
     *             Exception.
     */
    @PostConstruct
    public static void beforeClass() throws Exception {
        initDbunitConnection();
        clearAllTable();
        initCommonData();
        setAnonymousPrincipal();
    }

    /**
     * init connection.
     * 
     * @throws Exception
     *             Exception.
     */
    public static void initDbunitConnection() throws Exception {
        connection =
                new DatabaseConnection(
                        DataSourceUtils.getConnection(dataSource));
    }

    /**
     * get dataset.
     * 
     * @return dataset.
     * @throws Exception
     *             Exception.
     */
    protected static IDataSet getDeleteAllTableRowsDataSet() throws Exception {
        String dataSetFilePath = "cn/cnnic/rdap/dao/impl/teardown.xml";
        return getDataSet(dataSetFilePath);
    }

    /**
     * get init data dataset.
     * 
     * @return dataset.
     * @throws Exception
     *             Exception.
     */
    protected static IDataSet getInitDataSet() throws Exception {
        String dataSetFilePath = "cn/cnnic/rdap/dao/impl/initData.xml";
        return getDataSet(dataSetFilePath);
    }

    /**
     * get dataSet by file path.
     * 
     * @param dataSetFilePath
     *            file path,relative to classpath.
     * @return IDataSet.
     * @throws DataSetException
     *             DataSetException.
     * @throws FileNotFoundException
     *             FileNotFoundException.
     */
    private static IDataSet getDataSet(String dataSetFilePath)
            throws DataSetException, FileNotFoundException {
        URL url = BaseTest.class.getClassLoader().getResource(dataSetFilePath);
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        return builder.build(new FileInputStream(url.getPath()));
    }

    /**
     * clear all table.
     * 
     * @throws Exception
     *             exception.
     */
    protected static void clearAllTable() throws Exception {
        DatabaseOperation.DELETE_ALL.execute(connection,
                getDeleteAllTableRowsDataSet());
    }

    /**
     * init common data,such errorMessage, rdap_conformance, etc.
     * 
     * @throws Exception
     *             exception.
     */
    protected static void initCommonData() throws Exception {
        DatabaseOperation.REFRESH.execute(connection, getInitDataSet());
    }

    /**
     * set anonymous as principal.
     */
    private static void setAnonymousPrincipal() {
        PrincipalHolder.setPrincipal(Principal.getAnonymousPrincipal());
    }

    /**
     * set userId to principal.
     * 
     * @param userId
     *            userId.
     */
    protected void setUserIdToPrincipal(Long userId) {
        PrincipalHolder.setPrincipal(new Principal(userId));
    }

    /**
     * set dataSource.
     * 
     * @param dataSource
     *            dataSource.
     */
    @Autowired
    public void setDataSource(DataSource dataSource) {
        BaseTest.dataSource = dataSource;
    }

}
