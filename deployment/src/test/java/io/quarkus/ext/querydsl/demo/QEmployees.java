package io.quarkus.ext.querydsl.demo;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;
import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.sql.ColumnMetadata;

import io.quarkus.ext.querydsl.demo.pojos.Employees;

import java.sql.Types;

/**
 * QEmployees is a Querydsl query type for Employees
 */
@Generated("2020-08-21")
public class QEmployees extends com.querydsl.sql.RelationalPathBase<Employees> {

    private static final long serialVersionUID = 1626088085;

    public static final QEmployees $ = new QEmployees("employees");

    public final DatePath<java.time.LocalDate> birthDate = createDate("birthDate", java.time.LocalDate.class);

    public final NumberPath<Integer> empNo = createNumber("empNo", Integer.class);

    public final StringPath firstName = createString("firstName");

    public final StringPath gender = createString("gender");

    public final DatePath<java.time.LocalDate> hireDate = createDate("hireDate", java.time.LocalDate.class);

    public final StringPath lastName = createString("lastName");

    public final com.querydsl.sql.PrimaryKey<Employees> primary = createPrimaryKey(empNo);

    public QEmployees(String variable) {
        super(Employees.class, forVariable(variable), "null", "employees");
        addMetadata();
    }

    public QEmployees(String variable, String schema, String table) {
        super(Employees.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QEmployees(String variable, String schema) {
        super(Employees.class, forVariable(variable), schema, "employees");
        addMetadata();
    }

    public QEmployees(Path<? extends Employees> path) {
        super(path.getType(), path.getMetadata(), "null", "employees");
        addMetadata();
    }

    public QEmployees(PathMetadata metadata) {
        super(Employees.class, metadata, "null", "employees");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(birthDate, ColumnMetadata.named("birth_date").withIndex(2).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(empNo, ColumnMetadata.named("emp_no").withIndex(1).ofType(Types.INTEGER).withSize(10).notNull());
        addMetadata(firstName, ColumnMetadata.named("first_name").withIndex(3).ofType(Types.VARCHAR).withSize(14).notNull());
        addMetadata(gender, ColumnMetadata.named("gender").withIndex(5).ofType(Types.CHAR).withSize(1).notNull());
        addMetadata(hireDate, ColumnMetadata.named("hire_date").withIndex(6).ofType(Types.DATE).withSize(10).notNull());
        addMetadata(lastName, ColumnMetadata.named("last_name").withIndex(4).ofType(Types.VARCHAR).withSize(16).notNull());
    }
}
