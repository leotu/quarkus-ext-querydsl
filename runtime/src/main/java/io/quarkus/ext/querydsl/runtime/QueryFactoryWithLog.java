package io.quarkus.ext.querydsl.runtime;

import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.dml.SQLUpdateClause;

/**
 * For SQL command with logger
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public interface QueryFactoryWithLog {

    AbstractSQLQuery<?, ?> queryWithLog();

    SQLDeleteClause deleteWithLog(RelationalPath<?> path);

    SQLUpdateClause updateWithLog(RelationalPath<?> path);

    SQLInsertClause insertWithLog(RelationalPath<?> path);

    SQLMergeClause mergeWithLog(RelationalPath<?> path);

}
