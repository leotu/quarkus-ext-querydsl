package io.quarkus.ext.querydsl.demo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;

import io.quarkus.ext.querydsl.demo.pojos.Titles;

import java.sql.Types;

/**
 * QTitles is a Querydsl query type for Titles
 */
@Generated("2020-08-21")
public class QTitles extends com.querydsl.sql.RelationalPathBase<Titles> {

    private static final long serialVersionUID = 1917931531;

    public static final QTitles $ = new QTitles("titles");

    public final NumberPath<Integer> empNo = createNumber("empNo", Integer.class);

    public final DatePath<java.time.LocalDate> fromDate = createDate("fromDate", java.time.LocalDate.class);

    public final StringPath title = createString("title");

    public final DatePath<java.time.LocalDate> toDate = createDate("toDate", java.time.LocalDate.class);

    public final com.querydsl.sql.PrimaryKey<Titles> primary = createPrimaryKey(empNo, fromDate, title);

    public QTitles(String variable) {
        super(Titles.class, forVariable(variable), "null", "titles");
        addMetadata();
    }

    public QTitles(String variable, String schema, String table) {
        super(Titles.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTitles(String variable, String schema) {
        super(Titles.class, forVariable(variable), schema, "titles");
        addMetadata();
    }

    public QTitles(Path<? extends Titles> path) {
        super(path.getType(), path.getMetadata(), "null", "titles");
        addMetadata();
    }

    public QTitles(PathMetadata metadata) {
        super(Titles.class, metadata, "null", "titles");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(empNo, ColumnMetadata.named("emp_no").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(fromDate, ColumnMetadata.named("from_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(title, ColumnMetadata.named("title").withIndex(2).ofType(Types.VARCHAR).withSize(50).notNull());
        addMetadata(toDate, ColumnMetadata.named("to_date").withIndex(4).ofType(Types.DATE).withSize(10));
    }
}
