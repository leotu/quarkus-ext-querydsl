package io.quarkus.ext.querydsl.demo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;

import io.quarkus.ext.querydsl.demo.pojos.DeptManager;

import java.sql.Types;

/**
 * QDeptManager is a Querydsl query type for DeptManager
 */
@Generated("2020-08-21")
public class QDeptManager extends com.querydsl.sql.RelationalPathBase<DeptManager> {

    private static final long serialVersionUID = -1355042632;

    public static final QDeptManager $ = new QDeptManager("dept_manager");

    public final StringPath deptNo = createString("deptNo");

    public final NumberPath<Integer> empNo = createNumber("empNo", Integer.class);

    public final DatePath<java.time.LocalDate> fromDate = createDate("fromDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> toDate = createDate("toDate", java.time.LocalDate.class);

    public final com.querydsl.sql.PrimaryKey<DeptManager> primary = createPrimaryKey(deptNo, empNo);

    public QDeptManager(String variable) {
        super(DeptManager.class, forVariable(variable), "null", "dept_manager");
        addMetadata();
    }

    public QDeptManager(String variable, String schema, String table) {
        super(DeptManager.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QDeptManager(String variable, String schema) {
        super(DeptManager.class, forVariable(variable), schema, "dept_manager");
        addMetadata();
    }

    public QDeptManager(Path<? extends DeptManager> path) {
        super(path.getType(), path.getMetadata(), "null", "dept_manager");
        addMetadata();
    }

    public QDeptManager(PathMetadata metadata) {
        super(DeptManager.class, metadata, "null", "dept_manager");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(deptNo, ColumnMetadata.named("dept_no").withIndex(2).ofType(Types.CHAR).withSize(4).notNull());
        addMetadata(empNo, ColumnMetadata.named("emp_no").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(fromDate, ColumnMetadata.named("from_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(toDate, ColumnMetadata.named("to_date").withIndex(4).ofType(Types.DATE).withSize(10).notNull());
    }
}
