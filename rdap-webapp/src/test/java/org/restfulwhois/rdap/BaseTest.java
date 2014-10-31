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
package org.restfulwhois.rdap;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableIterator;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.ReplacementDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.restfulwhois.rdap.acl.bean.Principal;
import org.restfulwhois.rdap.core.common.support.PrincipalHolder;
import org.restfulwhois.rdap.core.common.util.RdapProperties;
import org.restfulwhois.rdap.core.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

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
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@Transactional
public abstract class BaseTest {

    /**
     * defaultMaxSizeSearch.
     */
    private static Long defaultMaxSizeSearch = null;

    /**
     * dataSource.
     */
    private static DataSource dataSource;

    /**
     * connection.
     */
    private static IDatabaseConnection connection;

    /**
     * or use BeforeClass.
     * 
     * @throws Exception
     *             Exception.
     */
    @PostConstruct
    public static void postConstruct() throws Exception {
        initDbunitConnection();
        clearAllTable();
        initCommonData();
        setAnonymousPrincipal();
        initDefaultMaxSizeSearch();
    }

    /**
     * init defaultMaxSizeSearch,from prop file.
     */
    private static void initDefaultMaxSizeSearch() {
        if (null == BaseTest.defaultMaxSizeSearch) {
            BaseTest.defaultMaxSizeSearch = RdapProperties.getMaxsizeSearch();
        }
    }

    /**
     * Before.
     * 
     * @throws Exception
     *             Exception.
     */
    @Before
    public void before() throws Exception {
        resetDefaultMaxSizeSearch();
    }

    @After
    public void after() throws Exception {
        connection.close();
    }

    /**
     * set default max size search.
     */
    private void resetDefaultMaxSizeSearch() {
        ReflectionTestUtils.setField(new RdapProperties(), "maxsizeSearch",
                BaseTest.defaultMaxSizeSearch);
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
        String dataSetFilePath = "org/restfulwhois/rdap/dao/impl/teardown.xml";
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
        String dataSetFilePath = "org/restfulwhois/rdap/dao/impl/initData.xml";
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
    protected static IDataSet getDataSet(String dataSetFilePath)
            throws DataSetException, FileNotFoundException {
        URL url = BaseTest.class.getClassLoader().getResource(dataSetFilePath);
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        IDataSet dataSet = builder.build(new FileInputStream(url.getPath()));
        ReplacementDataSet result = new ReplacementDataSet(dataSet);
        dataFilter(result);
        return result;
    }

    /**
     * filter dataset.
     * 
     * @param dataSet
     *            dataSet.
     * @throws DataSetException
     *             DataSetException.
     */
    private static void dataFilter(ReplacementDataSet dataSet)
            throws DataSetException {
        final String DATA_TYPE_NULL = "NULL";
        dataSet.addReplacementObject("[" + DATA_TYPE_NULL + "]", null);
        ITableIterator tables = dataSet.iterator();
        while (tables.next()) {
            ITable table = tables.getTable();
            ITableMetaData metaData = table.getTableMetaData();
            Column[] columns = metaData.getColumns();
            int rowCount = table.getRowCount();
            for (int row = 0; row < rowCount; row++) {
                for (int i = 0; i < columns.length; i++) {
                    Column c = columns[i];
                    String name = c.getColumnName();
                    Object object = table.getValue(row, name);
                    if (object instanceof String) {
                        String value = (String) object;
                        addReplacementForBinary(dataSet, value);
                    }
                }
            }
        }
    }

    /**
     * addReplacementForBinary.
     * 
     * @param dataSet
     * @param DATA_TYPE_BINARY
     * @param value
     */
    private static void addReplacementForBinary(ReplacementDataSet dataSet,
            String value) {
        final String DATA_TYPE_BINARY = "0x";
        if (StringUtils.isBlank(value)) {
            return;
        }
        if (!StringUtils.startsWith(value, DATA_TYPE_BINARY)) {
            return;
        }
        String bytes = StringUtils.removeStart(value, DATA_TYPE_BINARY);
        byte[] bin = new byte[bytes.length() / 2];
        for (int index = 0; index < bytes.length() / 2; index++) {
            bin[index] =
                    Integer.valueOf(bytes.substring(index * 2, index * 2 + 2),
                            16).byteValue();
        }
        dataSet.addReplacementObject(value, bin);

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

    /**
     * encode with 8859.
     * 
     * @param str
     *            str.
     * @return encoded str.
     * @throws UnsupportedEncodingException
     *             UnsupportedEncodingException.
     */
    protected String encodeWithIso8859(String str)
            throws UnsupportedEncodingException {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return new String(str.getBytes(), StringUtil.CHAR_SET_ISO8859);
    }

    /**
     * databaseSetupWithBinaryColumns.
     * 
     * @param dataFile
     */
    protected void databaseSetupWithBinaryColumns(String dataFile) {
        String dataSetFilePath = "org/restfulwhois/rdap/dao/impl/" + dataFile;
        IDataSet dataSet;
        try {
            dataSet = getDataSet(dataSetFilePath);
            DatabaseOperation.INSERT.execute(connection, dataSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * get empty dataSet.
     * 
     * @return
     */
    protected QueryDataSet getEmptyDataSet() {
        return new QueryDataSet(connection);
    }

}