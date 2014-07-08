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

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.util.StringUtils;

import cn.cnnic.rdap.common.util.StringUtil;

/**
 * init context, sql script util.
 * 
 * @author jiashuo
 * 
 */
public class InitContext implements ApplicationContextAware {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(InitContext.class);
    /**
     * default comment prefix.
     */
    private static final String DEFAULT_COMMENT_PREFIX = "﻿# ";
    /**
     * default statement separator.
     */
    private static final char DEFAULT_STATEMENT_SEPARATOR = ';';

    /**
     * The {@link ApplicationContext} that was injected into this test instance
     * via {@link #setApplicationContext(ApplicationContext)}.
     */
    private ApplicationContext applicationContext;

    /**
     * Execute the given SQL script.
     * <p>
     * Use with caution outside of a transaction!
     * <p>
     * The script will normally be loaded by classpath. There should be one
     * statement per line. Any semicolons will be removed. <b>Do not use this
     * method to execute DDL if you expect rollback.</b>
     * 
     * @param jdbcTemplate
     *            jdbc template dao
     * @param sqlResourcePath
     *            the Spring resource path for the SQL script
     * @param databaseName
     *            the name of database
     * @param continueOnError
     *            whether or not to continue without throwing an exception in
     *            the event of an error
     * @throws DataAccessException
     *             if there is an error executing a statement and
     *             continueOnError was {@code false}
     * @see #setSqlScriptEncoding
     */
    public void
            executeSqlScript(JdbcTemplate jdbcTemplate, String sqlResourcePath,
                    String databaseName, boolean continueOnError)
                    throws DataAccessException {
        Resource resource =
                this.applicationContext.getResource(sqlResourcePath);
        executeSqlScript(jdbcTemplate, new EncodedResource(resource,
                StringUtil.CHAR_SET_UTF8), databaseName, continueOnError);
    }

    /**
     * Execute the given SQL script.
     * <p>
     * The script will typically be loaded from the classpath. There should be
     * one statement per line. Any semicolons and line comments will be removed.
     * <p>
     * <b>Do not use this method to execute DDL if you expect rollback.</b>
     * 
     * @param jdbcTemplate
     *            the JdbcTemplate with which to perform JDBC operations
     * @param resource
     *            the resource (potentially associated with a specific encoding)
     *            to load the SQL script from
     * @param databaseName
     *            database name
     * @param continueOnError
     *            whether or not to continue without throwing an exception in
     *            the event of an error
     * @throws DataAccessException
     *             if there is an error executing a statement and
     *             {@code continueOnError} is {@code false}
     * @see ResourceDatabasePopulator
     */
    private static void executeSqlScript(JdbcTemplate jdbcTemplate,
            EncodedResource resource, String databaseName,
            boolean continueOnError) throws DataAccessException {

        long startTime = System.currentTimeMillis();
        List<String> statements = new LinkedList<String>();
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(resource.getReader());
            String script = readScript(reader);
            char delimiter = DEFAULT_STATEMENT_SEPARATOR;
            if (!containsSqlScriptDelimiters(script, delimiter)) {
                delimiter = '\n';
            }
            splitSqlScript(script, delimiter, statements);
            int lineNumber = 0;
            for (String statement : statements) {
                lineNumber++;
                executeUseDatabaseStatement(statement, jdbcTemplate,
                        databaseName);
                try {
                    int rowsAffected = jdbcTemplate.update(statement);
                    LOGGER.info(rowsAffected + " rows affected by SQL: "
                            + statement);
                } catch (DataAccessException ex) {
                    if (continueOnError) {
                        LOGGER.error(
                              "Failed to execute SQL script statement at line "
                               + lineNumber + " of resource "
                               + resource + ": " + statement, ex);
                    } else {
                        throw ex;
                    }
                }
            }
            long elapsedTime = System.currentTimeMillis() - startTime;
            LOGGER.info(String.format("Executed SQL script from %s in %s ms.",
                    resource, elapsedTime));
        } catch (IOException ex) {
            throw new DataAccessResourceFailureException(
                    "Failed to open SQL script from " + resource, ex);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage());
            }
        }
    }

    /**
     * add 'USE dbName' statement.
     * 
     * @param statement
     *            statement.
     * @param jdbcTemplate
     *            jdbcTemplate.
     * @param databaseName
     *            databaseName.
     */
    private static void executeUseDatabaseStatement(String statement,
            JdbcTemplate jdbcTemplate, String databaseName) {
        String[] notAddArrays =
                new String[] {"CREATE DATABASE", "DROP DATABASE", "USE "};
        if (StringUtils.isEmpty(statement)) {
            return;
        }
        String statementUppercase =
                org.apache.commons.lang.StringUtils.upperCase(statement);
        boolean startWithNotAddSql = false;
        for (String prefix : notAddArrays) {
            if (statementUppercase.startsWith(prefix)) {
                startWithNotAddSql = true;
            }
        }
        if (startWithNotAddSql) {
            return;
        }
        try {
            jdbcTemplate.update("USE `" + databaseName + "`");
        } catch (DataAccessException ex) {
            LOGGER.error("'USE 'database' error.dbName:{},error:{}",
                    databaseName, ex.getMessage());
        }
    }

    /**
     * Read a script from the provided {@code LineNumberReader}, using "
     * {@code --}" as the comment prefix, and build a {@code String} containing
     * the lines.
     * 
     * @param lineNumberReader
     *            the {@code LineNumberReader} containing the script to be
     *            processed
     * @return a {@code String} containing the script lines
     * @see #readScript(LineNumberReader, String)
     */
    public static String readScript(LineNumberReader lineNumberReader)
            throws IOException {
        return readScript(lineNumberReader, DEFAULT_COMMENT_PREFIX);
    }

    /**
     * Read a script from the provided {@code LineNumberReader}, using the
     * supplied comment prefix, and build a {@code String} containing the lines.
     * <p>
     * Lines <em>beginning</em> with the comment prefix are excluded from the
     * results; however, line comments anywhere else &mdash; for example, within
     * a statement &mdash; will be included in the results.
     * 
     * @param lineNumberReader
     *            the {@code LineNumberReader} containing the script to be
     *            processed
     * @param commentPrefix
     *            the prefix that identifies comments in the SQL script &mdash;
     *            typically "--"
     * @return a {@code String} containing the script lines
     */
    public static String readScript(LineNumberReader lineNumberReader,
            String commentPrefix) throws IOException {
        String currentStatement = lineNumberReader.readLine();
        StringBuilder scriptBuilder = new StringBuilder();
        while (currentStatement != null) {
            if (StringUtils.hasText(currentStatement)
                    && (commentPrefix != null && !currentStatement
                            .startsWith(commentPrefix))) {
                if (scriptBuilder.length() > 0) {
                    scriptBuilder.append('\n');
                }
                scriptBuilder.append(currentStatement);
            }
            currentStatement = lineNumberReader.readLine();
        }
        return scriptBuilder.toString();
    }

    /**
     * Determine if the provided SQL script contains the specified delimiter.
     * 
     * @param script
     *            the SQL script
     * @param delim
     *            character delimiting each statement &mdash; typically a ';'
     *            character
     * @return {@code true} if the script contains the delimiter; {@code false}
     *         otherwise
     */
    public static boolean
            containsSqlScriptDelimiters(String script, char delim) {
        boolean inLiteral = false;
        char[] content = script.toCharArray();
        for (int i = 0; i < script.length(); i++) {
            if (content[i] == '\'') {
                inLiteral = !inLiteral;
            }
            if (content[i] == delim && !inLiteral) {
                return true;
            }
        }
        return false;
    }

    /**
     * Split an SQL script into separate statements delimited by the provided
     * delimiter character. Each individual statement will be added to the
     * provided {@code List}.
     * <p>
     * Within a statement, "{@code --}" will be used as the comment prefix; any
     * text beginning with the comment prefix and extending to the end of the
     * line will be omitted from the statement. In addition, multiple adjacent
     * whitespace characters will be collapsed into a single space.
     * 
     * @param script
     *            the SQL script
     * @param delim
     *            character delimiting each statement &mdash; typically a ';'
     *            character
     * @param statements
     *            the list that will contain the individual statements
     */
    public static void splitSqlScript(String script, char delim,
            List<String> statements) {
        splitSqlScript(script, "" + delim, DEFAULT_COMMENT_PREFIX, statements);
    }

    /**
     * Split an SQL script into separate statements delimited by the provided
     * delimiter string. Each individual statement will be added to the provided
     * {@code List}.
     * <p>
     * Within a statement, the provided {@code commentPrefix} will be honored;
     * any text beginning with the comment prefix and extending to the end of
     * the line will be omitted from the statement. In addition, multiple
     * adjacent whitespace characters will be collapsed into a single space.
     * 
     * @param script
     *            the SQL script
     * @param delim
     *            character delimiting each statement &mdash; typically a ';'
     *            character
     * @param commentPrefix
     *            the prefix that identifies line comments in the SQL script
     *            &mdash; typically "--"
     * @param statements
     *            the List that will contain the individual statements
     */
    private static void splitSqlScript(String script, String delim,
            String commentPrefix, List<String> statements) {
        StringBuilder sb = new StringBuilder();
        boolean inLiteral = false;
        boolean inEscape = false;
        char[] content = script.toCharArray();
        for (int i = 0; i < script.length(); i++) {
            char c = content[i];
            if (inEscape) {
                inEscape = false;
                sb.append(c);
                continue;
            }
            // MySQL style escapes
            if (c == '\\') {
                inEscape = true;
                sb.append(c);
                continue;
            }
            if (c == '\'') {
                inLiteral = !inLiteral;
            }
            if (!inLiteral) {
                if (script.startsWith(delim, i)) {
                    // we've reached the end of the current statement
                    if (sb.length() > 0) {
                        statements.add(sb.toString());
                        sb = new StringBuilder();
                    }
                    i += delim.length() - 1;
                    continue;
                } else if (script.startsWith(commentPrefix, i)) {
                    // skip over any content from the start of the comment to
                    // the EOL
                    int indexOfNextNewline = script.indexOf("\n", i);
                    if (indexOfNextNewline > i) {
                        i = indexOfNextNewline;
                        continue;
                    } else {
                        // if there's no newline after the comment, we must be
                        // at the end
                        // of the script, so stop here.
                        break;
                    }
                } else if (c == ' ' || c == '\n' || c == '\t') {
                    // avoid multiple adjacent whitespace characters
                    if (sb.length() > 0 && sb.charAt(sb.length() - 1) != ' ') {
                        c = ' ';
                    } else {
                        continue;
                    }
                }
            }
            sb.append(c);
        }
        if (StringUtils.hasText(sb)) {
            statements.add(sb.toString());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        this.applicationContext = applicationContext;
    }

}
