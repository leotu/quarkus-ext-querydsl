package io.quarkus.ext.querydsl.demo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;

import io.quarkus.ext.querydsl.demo.pojos.Departments;

import java.sql.Types;

/**
 * QDepartments is a Querydsl query type for Departments
 */
@Generated("2020-08-21")
public class QDepartments extends com.querydsl.sql.RelationalPathBase<Departments> {

    private static final long serialVersionUID = -1087646095;

    public static final QDepartments $ = new QDepartments("departments");

    public final StringPath deptName = createString("deptName");

    public final StringPath deptNo = createString("deptNo");

    public final com.querydsl.sql.PrimaryKey<Departments> primary = createPrimaryKey(deptNo);

    public QDepartments(String variable) {
        super(Departments.class, forVariable(variable), "null", "departments");
        addMetadata();
    }

    public QDepartments(String variable, String schema, String table) {
        super(Departments.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QDepartments(String variable, String schema) {
        super(Departments.class, forVariable(variable), schema, "departments");
        addMetadata();
    }

    public QDepartments(Path<? extends Departments> path) {
        super(path.getType(), path.getMetadata(), "null", "departments");
        addMetadata();
    }

    public QDepartments(PathMetadata metadata) {
        super(Departments.class, metadata, "null", "departments");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(deptName, ColumnMetadata.named("dept_name").withIndex(2).ofType(Types.VARCHAR).withSize(40).notNull());
        addMetadata(deptNo, ColumnMetadata.named("dept_no").withIndex(1).ofType(Types.CHAR).withSize(4).notNull());
    }
}
