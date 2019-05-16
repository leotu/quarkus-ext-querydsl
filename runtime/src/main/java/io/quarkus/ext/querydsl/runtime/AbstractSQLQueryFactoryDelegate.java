package io.quarkus.ext.querydsl.runtime;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jboss.logging.Logger;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.AbstractSQLQueryFactory;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.AbstractSQLDeleteClause;
import com.querydsl.sql.dml.AbstractSQLInsertClause;
import com.querydsl.sql.dml.AbstractSQLUpdateClause;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.dml.SQLUpdateClause;

/**
 * Show SQL commands
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public abstract class AbstractSQLQueryFactoryDelegate implements QueryFactoryWithLog {

    private static final Logger log = Logger.getLogger(AbstractSQLQueryFactoryDelegate.class);

    protected static final org.jboss.logging.Logger sqlLog = org.jboss.logging.Logger
            .getLogger("io.quarkus.ext.querydsl.sql");

    final private AbstractSQLQueryFactory<? extends AbstractSQLQuery<?, ?>> delegate;

    public AbstractSQLQueryFactoryDelegate(AbstractSQLQueryFactory<? extends AbstractSQLQuery<?, ?>> delegate) {
        this.delegate = delegate;
    }

    @Override
    public final SQLDeleteClause deleteWithLog(RelationalPath<?> path) {
        return new SQLDeleteClause(delegate.getConnection(), delegate.getConfiguration(), path) {
            @Override
            protected void logQuery(org.slf4j.Logger logger, String queryString, Collection<Object> parameters) {
                if (sqlLog.isTraceEnabled()) {
                    QueryMetadata metadata = getField(this, AbstractSQLDeleteClause.class, "metadata");
                    sqlLog.debug(toSql(configuration, queryString, (List<Object>) parameters, metadata.getParams()));
                }
            }
        };
    }

    @Override
    public final SQLUpdateClause updateWithLog(RelationalPath<?> path) {
        return new SQLUpdateClause(delegate.getConnection(), delegate.getConfiguration(), path) {
            @Override
            protected void logQuery(org.slf4j.Logger logger, String queryString, Collection<Object> parameters) {
                if (sqlLog.isTraceEnabled()) {
                    QueryMetadata metadata = getField(this, AbstractSQLUpdateClause.class, "metadata");
                    sqlLog.debug(toSql(configuration, queryString, (List<Object>) parameters, metadata.getParams()));
                }
            }
        };
    }

    @Override
    public final SQLInsertClause insertWithLog(RelationalPath<?> path) {
        return new SQLInsertClause(delegate.getConnection(), delegate.getConfiguration(), path) {
            @Override
            protected void logQuery(org.slf4j.Logger logger, String queryString, Collection<Object> parameters) {
                if (sqlLog.isTraceEnabled()) {
                    QueryMetadata metadata = getField(this, AbstractSQLInsertClause.class, "metadata");
                    sqlLog.debug(toSql(configuration, queryString, (List<Object>) parameters, metadata.getParams()));
                }
            }
        };
    }

    @Override
    public final SQLMergeClause mergeWithLog(RelationalPath<?> path) {
        return new SQLMergeClause(delegate.getConnection(), delegate.getConfiguration(), path) {
            @Override
            protected void logQuery(org.slf4j.Logger logger, String queryString, Collection<Object> parameters) {
                if (sqlLog.isTraceEnabled()) {
                    QueryMetadata metadata = getField(this, SQLMergeClause.class, "metadata");
                    sqlLog.debug(toSql(configuration, queryString, (List<Object>) parameters, metadata.getParams()));
                }
            }
        };
    }

    private <T> T getField(Object reflectObj, Class<?> clsObj, String fieldName) {
        if (clsObj == null) {
            throw new IllegalArgumentException("(clsObj == null)");
        }
        if (fieldName == null || fieldName.length() == 0) {
            throw new IllegalArgumentException(
                    "(fieldName == null || fieldName.length() == 0), fieldName=[" + fieldName + "]");
        }
        try {
            Field field = clsObj.getDeclaredField(fieldName);
            boolean keepStatus = field.isAccessible();
            if (!keepStatus) {
                field.setAccessible(true);
            }
            try {
                Object fieldObj = field.get(reflectObj);
                @SuppressWarnings("unchecked")
                T t = (T) fieldObj;
                return t;
            } finally {
                field.setAccessible(keepStatus);
            }
        } catch (Exception e) {
            String msg = "reflect object class: " + (reflectObj == null ? "<null>" : reflectObj.getClass().getName())
                    + ", declared class: " + clsObj.getName() + ", field name: " + fieldName;
            log.warn(msg + ", error: " + e.toString());
            throw new RuntimeException(msg, e);
        }
    }

    String toSql(Configuration configuration, String sql, List<Object> parameters,
            Map<ParamExpression<?>, Object> paramExpressions) {
        sql = sql.replace('\n', ' ');
        int size = sql.length();
        StringBuilder sb = new StringBuilder((int) (size * 1.2));
        sb.append("<<SQL>> ");
        int idx = -1;
        for (int i = 0; i < size; i++) {
            char ch = sql.charAt(i);
            if (ch == '?') {
                idx++;
                Object o = parameters.get(idx);
                if (o instanceof ParamExpression) { // FIXME
                    if (!paramExpressions.containsKey(o)) {
                        throw new ParamNotSetException((ParamExpression<?>) o);
                    }
                    o = paramExpressions.get(o);
                }
                sb.append(configuration.asLiteral(o));
            } else {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

}
