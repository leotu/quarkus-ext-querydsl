package io.quarkus.ext.querydsl.demo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;

import io.quarkus.ext.querydsl.demo.pojos.Salaries;

import java.sql.Types;

/**
 * QSalaries is a Querydsl query type for Salaries
 */
@Generated("2020-08-21")
public class QSalaries extends com.querydsl.sql.RelationalPathBase<Salaries> {

    private static final long serialVersionUID = 99515096;

    public static final QSalaries $ = new QSalaries("salaries");

    public final NumberPath<Integer> empNo = createNumber("empNo", Integer.class);

    public final DatePath<java.time.LocalDate> fromDate = createDate("fromDate", java.time.LocalDate.class);

    public final NumberPath<Integer> salary = createNumber("salary", Integer.class);

    public final DatePath<java.time.LocalDate> toDate = createDate("toDate", java.time.LocalDate.class);

    public final com.querydsl.sql.PrimaryKey<Salaries> primary = createPrimaryKey(empNo, fromDate);

    public QSalaries(String variable) {
        super(Salaries.class, forVariable(variable), "null", "salaries");
        addMetadata();
    }

    public QSalaries(String variable, String schema, String table) {
        super(Salaries.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QSalaries(String variable, String schema) {
        super(Salaries.class, forVariable(variable), schema, "salaries");
        addMetadata();
    }

    public QSalaries(Path<? extends Salaries> path) {
        super(path.getType(), path.getMetadata(), "null", "salaries");
        addMetadata();
    }

    public QSalaries(PathMetadata metadata) {
        super(Salaries.class, metadata, "null", "salaries");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(empNo, ColumnMetadata.named("emp_no").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(fromDate, ColumnMetadata.named("from_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(salary, ColumnMetadata.named("salary").withIndex(2).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(toDate, ColumnMetadata.named("to_date").withIndex(4).ofType(Types.DATE).withSize(10).notNull());
    }
}
