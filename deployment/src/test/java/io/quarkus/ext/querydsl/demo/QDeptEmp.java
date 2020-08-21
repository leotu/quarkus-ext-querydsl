package io.quarkus.ext.querydsl.demo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;

import io.quarkus.ext.querydsl.demo.pojos.DeptEmp;

import java.sql.Types;

/**
 * QDeptEmp is a Querydsl query type for DeptEmp
 */
@Generated("2020-08-21")
public class QDeptEmp extends com.querydsl.sql.RelationalPathBase<DeptEmp> {

    private static final long serialVersionUID = -2106824877;

    public static final QDeptEmp $ = new QDeptEmp("dept_emp");

    public final StringPath deptNo = createString("deptNo");

    public final NumberPath<Integer> empNo = createNumber("empNo", Integer.class);

    public final DatePath<java.time.LocalDate> fromDate = createDate("fromDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> toDate = createDate("toDate", java.time.LocalDate.class);

    public final com.querydsl.sql.PrimaryKey<DeptEmp> primary = createPrimaryKey(deptNo, empNo);

    public QDeptEmp(String variable) {
        super(DeptEmp.class, forVariable(variable), "null", "dept_emp");
        addMetadata();
    }

    public QDeptEmp(String variable, String schema, String table) {
        super(DeptEmp.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QDeptEmp(String variable, String schema) {
        super(DeptEmp.class, forVariable(variable), schema, "dept_emp");
        addMetadata();
    }

    public QDeptEmp(Path<? extends DeptEmp> path) {
        super(path.getType(), path.getMetadata(), "null", "dept_emp");
        addMetadata();
    }

    public QDeptEmp(PathMetadata metadata) {
        super(DeptEmp.class, metadata, "null", "dept_emp");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(deptNo, ColumnMetadata.named("dept_no").withIndex(2).ofType(Types.CHAR).withSize(4).notNull());
        addMetadata(empNo, ColumnMetadata.named("emp_no").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(fromDate, ColumnMetadata.named("from_date").withIndex(3).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(toDate, ColumnMetadata.named("to_date").withIndex(4).ofType(Types.DATE).withSize(10).notNull());
    }
}
