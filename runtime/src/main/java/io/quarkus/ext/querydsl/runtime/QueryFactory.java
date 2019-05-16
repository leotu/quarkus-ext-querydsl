package io.quarkus.ext.querydsl.runtime;

import java.util.Objects;

import org.jboss.logging.Logger;

import com.querydsl.sql.CUBRIDTemplates;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.DB2Templates;
import com.querydsl.sql.DerbyTemplates;
import com.querydsl.sql.FirebirdTemplates;
import com.querydsl.sql.H2Templates;
import com.querydsl.sql.HSQLDBTemplates;
import com.querydsl.sql.MySQLTemplates;
import com.querydsl.sql.OracleTemplates;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.SQLServer2005Templates;
import com.querydsl.sql.SQLServer2012Templates;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.SQLiteTemplates;
import com.querydsl.sql.TeradataTemplates;
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

import io.agroal.api.AgroalDataSource;

/**
 * Fix Quarkus cannot override final method and register custom type
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public class QueryFactory {
    private static final Logger log = Logger.getLogger(QueryFactory.class);

    static public class PostgreSQL extends QueryFactoryWrapper<PostgreSQLQuery<?>, PostgreSQLQueryFactory> {

        public PostgreSQL() {
            super(null);
        }

        public PostgreSQL(PostgreSQLQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class MySQL extends QueryFactoryWrapper<MySQLQuery<?>, MySQLQueryFactory> {

        public MySQL() {
            super(null);
        }

        public MySQL(MySQLQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class Oracle extends QueryFactoryWrapper<OracleQuery<?>, OracleQueryFactory> {

        public Oracle() {
            super(null);
        }

        public Oracle(OracleQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class SQLServer extends QueryFactoryWrapper<SQLServerQuery<?>, SQLServerQueryFactory> {

        public SQLServer() {
            super(null);
        }

        public SQLServer(SQLServerQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class SQL extends QueryFactoryWrapper<SQLQuery<?>, SQLQueryFactory> {

        public SQL() {
            super(null);
        }

        public SQL(SQLQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class Teradata extends QueryFactoryWrapper<TeradataQuery<?>, TeradataQueryFactory> {

        public Teradata() {
            super(null);
        }

        public Teradata(TeradataQueryFactory delegate) {
            super(delegate);
        }
    }

    static public QueryFactoryWrapper<?, ?> create(String sqlTemplates, AgroalDataSource dataSource,
            QuerydslCustomTypeRegister customTypeRegister) {
        Objects.requireNonNull(dataSource, "dataSource");
        Objects.requireNonNull(customTypeRegister, "customTypeRegister");

        QueryFactoryWrapper<?, ?> queryFactory;
        if ("PostgreSQL".equalsIgnoreCase(sqlTemplates) || "Postgres".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = PostgreSQLTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            PostgreSQLQueryFactory qf = new QueryFactoryCreator.PostgreSQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new PostgreSQL(qf);
        } else if ("MySQL".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = MySQLTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            MySQLQueryFactory qf = new QueryFactoryCreator.MySQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new MySQL(qf);
        } else if ("Oracle".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = OracleTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            OracleQueryFactory qf = new QueryFactoryCreator.OracleQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new Oracle(qf);
        } else if ("SQLServer".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLServerTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLServerQueryFactory qf = new QueryFactoryCreator.SQLServerQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQLServer(qf);
        } else if ("SQLServer2005".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLServer2005Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLServerQueryFactory qf = new QueryFactoryCreator.SQLServerQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQLServer(qf);
        } else if ("SQLServer2012".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLServer2012Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLServerQueryFactory qf = new QueryFactoryCreator.SQLServerQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQLServer(qf);
        } else if ("DB2".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = DB2Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        } else if ("Derby".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = DerbyTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        } else if ("HSQLDB".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = HSQLDBTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        } else if ("H2".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = H2Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        } else if ("Firebird".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = FirebirdTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        } else if ("SQLite".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLiteTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        } else if ("CUBRID".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = CUBRIDTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        } else if ("Teradata".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = TeradataTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            TeradataQueryFactory qf = new QueryFactoryCreator.TeradataQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = new Teradata(qf);
        } else {
            log.warnv("Undefined sqlTemplates: {0}", sqlTemplates);
            SQLTemplates templates = SQLTemplates.DEFAULT;
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new SQLQueryFactory(configuration, new ConnectionProvider(dataSource));
            queryFactory = new SQL(qf);
        }
        customTypeRegister.register(queryFactory.getConfiguration());
        return queryFactory;
    }

    static public Class<?> getQueryFactoryType(String sqlTemplates) {
        if ("PostgreSQL".equalsIgnoreCase(sqlTemplates) || "Postgres".equalsIgnoreCase(sqlTemplates)) {
            return PostgreSQL.class;
        } else if ("MySQL".equalsIgnoreCase(sqlTemplates)) {
            return MySQL.class;
        } else if ("Oracle".equalsIgnoreCase(sqlTemplates)) {
            return Oracle.class;
        } else if ("SQLServer".equalsIgnoreCase(sqlTemplates)) {
            return SQLServer.class;
        } else if ("SQLServer2005".equalsIgnoreCase(sqlTemplates)) {
            return SQLServer.class;
        } else if ("SQLServer2012".equalsIgnoreCase(sqlTemplates)) {
            return SQLServer.class;
        } else if ("DB2".equalsIgnoreCase(sqlTemplates)) {
            return SQL.class;
        } else if ("Derby".equalsIgnoreCase(sqlTemplates)) {
            return SQL.class;
        } else if ("HSQLDB".equalsIgnoreCase(sqlTemplates)) {
            return SQL.class;
        } else if ("H2".equalsIgnoreCase(sqlTemplates)) {
            return SQL.class;
        } else if ("SQLite".equalsIgnoreCase(sqlTemplates)) {
            return SQL.class;
        } else if ("CUBRID".equalsIgnoreCase(sqlTemplates)) {
            return SQL.class;
        } else if ("Teradata".equalsIgnoreCase(sqlTemplates)) {
            return Teradata.class;
        } else {
            log.warnv("Undefined sqlTemplates: {0}", sqlTemplates);
            return SQL.class;
        }
    }
}
