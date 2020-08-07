package io.quarkus.ext.querydsl.runtime;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

import javax.inject.Provider;

import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.sql.mssql.SQLServerQuery;
import com.querydsl.sql.mssql.SQLServerQueryFactory;
import com.querydsl.sql.mysql.MySQLQuery;
import com.querydsl.sql.mysql.MySQLQueryFactory;
import com.querydsl.sql.oracle.OracleQuery;
import com.querydsl.sql.oracle.OracleQueryFactory;
import com.querydsl.sql.postgresql.PostgreSQLQuery;
import com.querydsl.sql.postgresql.PostgreSQLQueryFactory;
import com.querydsl.sql.teradata.TeradataQuery;
import com.querydsl.sql.teradata.TeradataQueryFactory;

/**
 * 
 * @author Leo Tu
 */
public class QueryFactoryCreator {

    static public class PostgreSQLQueryFactoryExt extends PostgreSQLQueryFactory implements QueryFactoryWithLog {

        final private AbstractSQLQueryFactoryDelegate delegateWithLog;

        public PostgreSQLQueryFactoryExt(Configuration configuration, Provider<Connection> connProvider) {
            super(configuration, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        public PostgreSQLQueryFactoryExt(SQLTemplates templates, Provider<Connection> connProvider) {
            super(templates, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        private AbstractSQLQueryFactoryDelegate init(Provider<Connection> connProvider) {
            return new AbstractSQLQueryFactoryDelegate(this) {

                @SuppressWarnings("serial")
                @Override
                public AbstractSQLQuery<?, ?> queryWithLog() {
                    return new PostgreSQLQuery<Void>(connProvider, PostgreSQLQueryFactoryExt.this.getConfiguration()) {
                        @Override
                        protected void logQuery(String queryString, Collection<Object> parameters) {
                            if (sqlLog.isTraceEnabled()) {
                                sqlLog.debug(delegateWithLog.toSql(configuration, queryString,
                                        (List<Object>) parameters, getMetadata().getParams()));
                            }
                        }
                    };
                }
            };
        }

        @Override
        public PostgreSQLQuery<?> query() {
            return queryWithLog();
        }

        @Override
        public PostgreSQLQuery<?> queryWithLog() {
            return (PostgreSQLQuery<?>) delegateWithLog.queryWithLog();
        }

        @Override
        public final SQLDeleteClause deleteWithLog(RelationalPath<?> path) {
            return delegateWithLog.deleteWithLog(path);
        }

        @Override
        public final SQLUpdateClause updateWithLog(RelationalPath<?> path) {
            return delegateWithLog.updateWithLog(path);
        }

        @Override
        public final SQLInsertClause insertWithLog(RelationalPath<?> path) {
            return delegateWithLog.insertWithLog(path);
        }

        @Override
        public final SQLMergeClause mergeWithLog(RelationalPath<?> path) {
            return delegateWithLog.mergeWithLog(path);
        }
    }

    static public class OracleQueryFactoryExt extends OracleQueryFactory implements QueryFactoryWithLog {

        final private AbstractSQLQueryFactoryDelegate delegateWithLog;

        public OracleQueryFactoryExt(Configuration configuration, Provider<Connection> connProvider) {
            super(configuration, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        public OracleQueryFactoryExt(SQLTemplates templates, Provider<Connection> connProvider) {
            super(templates, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        private AbstractSQLQueryFactoryDelegate init(Provider<Connection> connProvider) {
            return new AbstractSQLQueryFactoryDelegate(this) {

                @SuppressWarnings("serial")
                @Override
                public AbstractSQLQuery<?, ?> queryWithLog() {
                    return new PostgreSQLQuery<Void>(connProvider, OracleQueryFactoryExt.this.getConfiguration()) {
                        @Override
                        protected void logQuery(String queryString, Collection<Object> parameters) {
                            if (sqlLog.isTraceEnabled()) {
                                sqlLog.debug(delegateWithLog.toSql(configuration, queryString,
                                        (List<Object>) parameters, getMetadata().getParams()));
                            }
                        }
                    };
                }
            };
        }

        @Override
        public OracleQuery<?> query() {
            return queryWithLog();
        }

        @Override
        public OracleQuery<?> queryWithLog() {
            return (OracleQuery<?>) delegateWithLog.queryWithLog();
        }

        @Override
        public final SQLDeleteClause deleteWithLog(RelationalPath<?> path) {
            return delegateWithLog.deleteWithLog(path);
        }

        @Override
        public final SQLUpdateClause updateWithLog(RelationalPath<?> path) {
            return delegateWithLog.updateWithLog(path);
        }

        @Override
        public final SQLInsertClause insertWithLog(RelationalPath<?> path) {
            return delegateWithLog.insertWithLog(path);
        }

        @Override
        public final SQLMergeClause mergeWithLog(RelationalPath<?> path) {
            return delegateWithLog.mergeWithLog(path);
        }
    }

    static public class MySQLQueryFactoryExt extends MySQLQueryFactory implements QueryFactoryWithLog {

        final private AbstractSQLQueryFactoryDelegate delegateWithLog;

        public MySQLQueryFactoryExt(Configuration configuration, Provider<Connection> connProvider) {
            super(configuration, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        public MySQLQueryFactoryExt(SQLTemplates templates, Provider<Connection> connProvider) {
            super(templates, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        private AbstractSQLQueryFactoryDelegate init(Provider<Connection> connProvider) {
            return new AbstractSQLQueryFactoryDelegate(this) {

                @SuppressWarnings("serial")
                @Override
                public AbstractSQLQuery<?, ?> queryWithLog() {
                    return new MySQLQuery<Void>(connProvider, MySQLQueryFactoryExt.this.getConfiguration()) {
                        @Override
                        protected void logQuery(String queryString, Collection<Object> parameters) {
                            if (sqlLog.isTraceEnabled()) {
                                sqlLog.debug(delegateWithLog.toSql(configuration, queryString,
                                        (List<Object>) parameters, getMetadata().getParams()));
                            }
                        }
                    };
                }
            };
        }

        @Override
        public MySQLQuery<?> query() {
            return queryWithLog();
        }

        @Override
        public MySQLQuery<?> queryWithLog() {
            return (MySQLQuery<?>) delegateWithLog.queryWithLog();
        }

        @Override
        public final SQLDeleteClause deleteWithLog(RelationalPath<?> path) {
            return delegateWithLog.deleteWithLog(path);
        }

        @Override
        public final SQLUpdateClause updateWithLog(RelationalPath<?> path) {
            return delegateWithLog.updateWithLog(path);
        }

        @Override
        public final SQLInsertClause insertWithLog(RelationalPath<?> path) {
            return delegateWithLog.insertWithLog(path);
        }

        @Override
        public final SQLMergeClause mergeWithLog(RelationalPath<?> path) {
            return delegateWithLog.mergeWithLog(path);
        }
    }

    static public class SQLServerQueryFactoryExt extends SQLServerQueryFactory implements QueryFactoryWithLog {

        final private AbstractSQLQueryFactoryDelegate delegateWithLog;

        public SQLServerQueryFactoryExt(Configuration configuration, Provider<Connection> connProvider) {
            super(configuration, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        public SQLServerQueryFactoryExt(SQLTemplates templates, Provider<Connection> connProvider) {
            super(templates, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        private AbstractSQLQueryFactoryDelegate init(Provider<Connection> connProvider) {
            return new AbstractSQLQueryFactoryDelegate(this) {

                @SuppressWarnings("serial")
                @Override
                public AbstractSQLQuery<?, ?> queryWithLog() {
                    return new MySQLQuery<Void>(connProvider, SQLServerQueryFactoryExt.this.getConfiguration()) {
                        @Override
                        protected void logQuery(String queryString, Collection<Object> parameters) {
                            if (sqlLog.isTraceEnabled()) {
                                sqlLog.debug(delegateWithLog.toSql(configuration, queryString,
                                        (List<Object>) parameters, getMetadata().getParams()));
                            }
                        }
                    };
                }
            };
        }

        @Override
        public SQLServerQuery<?> query() {
            return queryWithLog();
        }

        @Override
        public SQLServerQuery<?> queryWithLog() {
            return (SQLServerQuery<?>) delegateWithLog.queryWithLog();
        }

        @Override
        public final SQLDeleteClause deleteWithLog(RelationalPath<?> path) {
            return delegateWithLog.deleteWithLog(path);
        }

        @Override
        public final SQLUpdateClause updateWithLog(RelationalPath<?> path) {
            return delegateWithLog.updateWithLog(path);
        }

        @Override
        public final SQLInsertClause insertWithLog(RelationalPath<?> path) {
            return delegateWithLog.insertWithLog(path);
        }

        @Override
        public final SQLMergeClause mergeWithLog(RelationalPath<?> path) {
            return delegateWithLog.mergeWithLog(path);
        }
    }

    static public class SQLQueryFactoryExt extends SQLQueryFactory implements QueryFactoryWithLog {

        final private AbstractSQLQueryFactoryDelegate delegateWithLog;

        public SQLQueryFactoryExt(Configuration configuration, Provider<Connection> connProvider) {
            super(configuration, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        public SQLQueryFactoryExt(SQLTemplates templates, Provider<Connection> connProvider) {
            super(templates, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        private AbstractSQLQueryFactoryDelegate init(Provider<Connection> connProvider) {
            return new AbstractSQLQueryFactoryDelegate(this) {

                @SuppressWarnings("serial")
                @Override
                public AbstractSQLQuery<?, ?> queryWithLog() {
                    return new SQLQuery<Void>(connProvider, SQLQueryFactoryExt.this.getConfiguration()) {
                        @Override
                        protected void logQuery(String queryString, Collection<Object> parameters) {
                            if (sqlLog.isTraceEnabled()) {
                                sqlLog.debug(delegateWithLog.toSql(configuration, queryString,
                                        (List<Object>) parameters, getMetadata().getParams()));
                            }
                        }
                    };
                }
            };
        }

        @Override
        public SQLQuery<?> query() {
            return queryWithLog();
        }

        @Override
        public SQLQuery<?> queryWithLog() {
            return (SQLQuery<?>) delegateWithLog.queryWithLog();
        }

        @Override
        public final SQLDeleteClause deleteWithLog(RelationalPath<?> path) {
            return delegateWithLog.deleteWithLog(path);
        }

        @Override
        public final SQLUpdateClause updateWithLog(RelationalPath<?> path) {
            return delegateWithLog.updateWithLog(path);
        }

        @Override
        public final SQLInsertClause insertWithLog(RelationalPath<?> path) {
            return delegateWithLog.insertWithLog(path);
        }

        @Override
        public final SQLMergeClause mergeWithLog(RelationalPath<?> path) {
            return delegateWithLog.mergeWithLog(path);
        }
    }

    static public class TeradataQueryFactoryExt extends TeradataQueryFactory implements QueryFactoryWithLog {

        final private AbstractSQLQueryFactoryDelegate delegateWithLog;

        public TeradataQueryFactoryExt(Configuration configuration, Provider<Connection> connProvider) {
            super(configuration, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        public TeradataQueryFactoryExt(SQLTemplates templates, Provider<Connection> connProvider) {
            super(templates, connProvider);
            this.delegateWithLog = init(connProvider);
        }

        private AbstractSQLQueryFactoryDelegate init(Provider<Connection> connProvider) {
            return new AbstractSQLQueryFactoryDelegate(this) {

                @SuppressWarnings("serial")
                @Override
                public AbstractSQLQuery<?, ?> queryWithLog() {
                    return new SQLQuery<Void>(connProvider, TeradataQueryFactoryExt.this.getConfiguration()) {
                        @Override
                        protected void logQuery(String queryString, Collection<Object> parameters) {
                            if (sqlLog.isTraceEnabled()) {
                                sqlLog.debug(delegateWithLog.toSql(configuration, queryString,
                                        (List<Object>) parameters, getMetadata().getParams()));
                            }
                        }
                    };
                }
            };
        }

        @Override
        public TeradataQuery<?> query() {
            return queryWithLog();
        }

        @Override
        public TeradataQuery<?> queryWithLog() {
            return (TeradataQuery<?>) delegateWithLog.queryWithLog();
        }

        @Override
        public final SQLDeleteClause deleteWithLog(RelationalPath<?> path) {
            return delegateWithLog.deleteWithLog(path);
        }

        @Override
        public final SQLUpdateClause updateWithLog(RelationalPath<?> path) {
            return delegateWithLog.updateWithLog(path);
        }

        @Override
        public final SQLInsertClause insertWithLog(RelationalPath<?> path) {
            return delegateWithLog.insertWithLog(path);
        }

        @Override
        public final SQLMergeClause mergeWithLog(RelationalPath<?> path) {
            return delegateWithLog.mergeWithLog(path);
        }
    }
}
