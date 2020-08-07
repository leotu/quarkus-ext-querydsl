package io.quarkus.ext.querydsl.runtime;

import java.util.Objects;
import java.util.Optional;

import javax.sql.DataSource;

import org.jboss.logging.Logger;

import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.AbstractSQLQueryFactory;
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

/**
 * Fix Quarkus cannot override final method and register custom type
 * 
 * @author Leo Tu
 */
public class QueryFactory {
    private static final Logger log = Logger.getLogger(QueryFactory.class);

    static public class PostgreSQLFactory extends QueryFactoryWrapper<PostgreSQLQuery<?>, PostgreSQLQueryFactory> {

        public PostgreSQLFactory() {
            super(null);
        }

        public PostgreSQLFactory(PostgreSQLQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class MySQLFactory extends QueryFactoryWrapper<MySQLQuery<?>, MySQLQueryFactory> {

        public MySQLFactory() {
            super(null);
        }

        public MySQLFactory(MySQLQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class OracleFactory extends QueryFactoryWrapper<OracleQuery<?>, OracleQueryFactory> {

        public OracleFactory() {
            super(null);
        }

        public OracleFactory(OracleQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class SQLServerFactory extends QueryFactoryWrapper<SQLServerQuery<?>, SQLServerQueryFactory> {

        public SQLServerFactory() {
            super(null);
        }

        public SQLServerFactory(SQLServerQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class SQLFactory extends QueryFactoryWrapper<SQLQuery<?>, SQLQueryFactory> {

        public SQLFactory() {
            super(null);
        }

        public SQLFactory(SQLQueryFactory delegate) {
            super(delegate);
        }
    }

    static public class TeradataFactory extends QueryFactoryWrapper<TeradataQuery<?>, TeradataQueryFactory> {

        public TeradataFactory() {
            super(null);
        }

        public TeradataFactory(TeradataQueryFactory delegate) {
            super(delegate);
        }
    }

    static public QueryFactoryWrapper<?, ?> create(String sqlTemplates, DataSource dataSource,
            QuerydslCustomTypeRegister customTypeRegister, String factoryAlias) {
        Objects.requireNonNull(dataSource, "dataSource");
        Objects.requireNonNull(customTypeRegister, "customTypeRegister");

        QueryFactoryWrapper<?, ?> queryFactory;
        if ("PostgreSQL".equalsIgnoreCase(sqlTemplates) || "Postgres".equalsIgnoreCase(sqlTemplates)
                || "PgSQL".equalsIgnoreCase(sqlTemplates) || "PG".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = PostgreSQLTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            PostgreSQLQueryFactory qf = new QueryFactoryCreator.PostgreSQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new PostgreSQLFactory(qf));
        } else if ("MySQL".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = MySQLTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            MySQLQueryFactory qf = new QueryFactoryCreator.MySQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new MySQLFactory(qf));
        } else if ("Oracle".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = OracleTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            OracleQueryFactory qf = new QueryFactoryCreator.OracleQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new OracleFactory(qf));
        } else if ("SQLServer".equalsIgnoreCase(sqlTemplates) || "MSSQL".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLServerTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLServerQueryFactory qf = new QueryFactoryCreator.SQLServerQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLServerFactory(qf));
        } else if ("SQLServer2005".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLServer2005Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLServerQueryFactory qf = new QueryFactoryCreator.SQLServerQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLServerFactory(qf));
        } else if ("SQLServer2012".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLServer2012Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLServerQueryFactory qf = new QueryFactoryCreator.SQLServerQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLServerFactory(qf));
        } else if ("DB2".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = DB2Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        } else if ("Derby".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = DerbyTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        } else if ("HSQLDB".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = HSQLDBTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        } else if ("H2".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = H2Templates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        } else if ("Firebird".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = FirebirdTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        } else if ("SQLite".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = SQLiteTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        } else if ("CUBRID".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = CUBRIDTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new QueryFactoryCreator.SQLQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        } else if ("Teradata".equalsIgnoreCase(sqlTemplates)) {
            SQLTemplates templates = TeradataTemplates.builder().build();
            Configuration configuration = new Configuration(templates);
            TeradataQueryFactory qf = new QueryFactoryCreator.TeradataQueryFactoryExt(configuration,
                    new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new TeradataFactory(qf));
        } else {
            log.warnv("Undefined sqlTemplates: {0}, factoryAlias: {1}", sqlTemplates, factoryAlias);
            SQLTemplates templates = SQLTemplates.DEFAULT;
            Configuration configuration = new Configuration(templates);
            SQLQueryFactory qf = new SQLQueryFactory(configuration, new ConnectionProvider(dataSource));
            queryFactory = getFactoryAliasInstance(factoryAlias, qf).orElse(new SQLFactory(qf));
        }
        customTypeRegister.register(queryFactory.getConfiguration());
        return queryFactory;
    }

    static public <Q extends AbstractSQLQuery<?, ?>, F extends AbstractSQLQueryFactory<Q>> Class<? extends QueryFactoryWrapper<?, ?>> getQueryFactoryType(
            String sqlTemplates, String factoryAlias) {
        Optional<Class<? extends QueryFactoryWrapper<Q, F>>> factoryAliasClass = getFactoryAliasClass(factoryAlias);
        if ("PostgreSQL".equalsIgnoreCase(sqlTemplates) || "Postgres".equalsIgnoreCase(sqlTemplates)
                || "PgSQL".equalsIgnoreCase(sqlTemplates) || "PG".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : PostgreSQLFactory.class;
        } else if ("MySQL".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : MySQLFactory.class;
        } else if ("Oracle".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : OracleFactory.class;
        } else if ("SQLServer".equalsIgnoreCase(sqlTemplates) || "MSSQL".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLServerFactory.class;
        } else if ("SQLServer2005".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLServerFactory.class;
        } else if ("SQLServer2012".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLServerFactory.class;
        } else if ("DB2".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLFactory.class;
        } else if ("Derby".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLFactory.class;
        } else if ("HSQLDB".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLFactory.class;
        } else if ("H2".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLFactory.class;
        } else if ("SQLite".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLFactory.class;
        } else if ("CUBRID".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : SQLFactory.class;
        } else if ("Teradata".equalsIgnoreCase(sqlTemplates)) {
            return factoryAliasClass.isPresent() ? factoryAliasClass.get() : TeradataFactory.class;
        } else {
            log.warnv("Undefined sqlTemplates: {0}, factoryAlias: {1}", sqlTemplates, factoryAlias);
            return SQLFactory.class;
        }
    }

    static <Q extends AbstractSQLQuery<?, ?>, F extends AbstractSQLQueryFactory<Q>> Optional<Class<? extends QueryFactoryWrapper<Q, F>>> getFactoryAliasClass(
            String factoryAlias) {
        Optional<Class<? extends QueryFactoryWrapper<Q, F>>> factoryAliasClass;
        if (factoryAlias == null || factoryAlias.isEmpty()) {
            factoryAliasClass = Optional.empty();
        } else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = QueryFactoryWrapper.class.getClassLoader();
            }
            try {
                @SuppressWarnings("unchecked")
                Class<? extends QueryFactoryWrapper<Q, F>> clazz = (Class<? extends QueryFactoryWrapper<Q, F>>) cl
                        .loadClass(factoryAlias);
                factoryAliasClass = Optional.of(clazz);
            } catch (Exception e) {
                log.error(factoryAlias, e);
                throw new RuntimeException(e);
            }
        }
        return factoryAliasClass;
    }

    static public <Q extends AbstractSQLQuery<?, ?>, F extends AbstractSQLQueryFactory<Q>> Optional<QueryFactoryWrapper<Q, F>> getFactoryAliasInstance(
            String factoryAlias, F qf) {
        Optional<Class<? extends QueryFactoryWrapper<Q, F>>> factoryAliasClass = getFactoryAliasClass(factoryAlias);
        QueryFactoryWrapper<Q, F> factoryInstance = null;
        try {
            if (factoryAliasClass.isPresent()) {
                factoryInstance = factoryAliasClass.get().getDeclaredConstructor().newInstance();
                factoryInstance.setDelegate(qf);
            }
        } catch (Exception e) {
            log.error(factoryAlias, e);
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(factoryInstance);
    }

}
