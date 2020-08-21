package io.quarkus.ext.querydsl.runtime;

import java.sql.Connection;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.AbstractSQLQueryFactory;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLCommonQueryFactory;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.dml.SQLUpdateClause;

/**
 * 
 * @author Leo Tu
 */
public class QueryFactory<Q extends AbstractSQLQuery<?, ?>, F extends AbstractSQLQueryFactory<Q>>
        implements SQLCommonQueryFactory<Q, SQLDeleteClause, SQLUpdateClause, SQLInsertClause, SQLMergeClause> {

    private F delegate;

    public QueryFactory(F delegate) {
        this.delegate = delegate;
    }

    public void setDelegate(F delegate) {
        this.delegate = delegate;
    }

    public F getDelegate() {
        return delegate;
    }

    @Override
    public SQLDeleteClause delete(RelationalPath<?> path) {
        return delegate.delete(path);
    }

    @Override
    public Q from(Expression<?> from) {
        return delegate.from(from);
    }

    @Override
    public Q from(Expression<?>... args) {
        return delegate.from(args);
    }

    public Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return delegate.from(subQuery, alias);
    }

    @Override
    public SQLInsertClause insert(RelationalPath<?> path) {
        return delegate.insert(path);
    }

    @Override
    public SQLMergeClause merge(RelationalPath<?> path) {
        return delegate.merge(path);
    }

    @Override
    public SQLUpdateClause update(RelationalPath<?> path) {
        return delegate.update(path);
    }

    public Configuration getConfiguration() {
        return delegate.getConfiguration();
    }

    @Override
    public Q query() {
        return delegate.query();
    }

    public Connection getConnection() {
        return delegate.getConnection();
    }

    public <T> AbstractSQLQuery<T, ?> select(Expression<T> expr) {
        return delegate.select(expr);
    }

    public AbstractSQLQuery<Tuple, ?> select(Expression<?>... exprs) {
        return delegate.select(exprs);
    }

    public <T> AbstractSQLQuery<T, ?> selectDistinct(Expression<T> expr) {
        return delegate.selectDistinct(expr);
    }

    public AbstractSQLQuery<Tuple, ?> selectDistinct(Expression<?>... exprs) {
        return delegate.selectDistinct(exprs);
    }

    public AbstractSQLQuery<Integer, ?> selectZero() {
        return delegate.selectZero();
    }

    public AbstractSQLQuery<Integer, ?> selectOne() {
        return delegate.selectOne();
    }

    public <T> AbstractSQLQuery<T, ?> selectFrom(RelationalPath<T> expr) {
        return delegate.selectFrom(expr);
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }

}
