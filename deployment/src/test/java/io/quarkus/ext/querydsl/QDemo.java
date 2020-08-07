package io.quarkus.ext.querydsl;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDateTime;

import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPathBase;

/**
 * 
 * @author Leo Tu
 */
@SuppressWarnings("serial")
public class QDemo extends RelationalPathBase<Demo> {

    public static final QDemo $ = new QDemo("demo");

    public final StringPath id = createString("id");
    public final StringPath name = createString("name");
    public final NumberPath<BigDecimal> amount = createNumber("amount", BigDecimal.class);
    public final DateTimePath<LocalDateTime> createdAt = createDateTime("createdAt", LocalDateTime.class);

    protected QDemo(String table) {
        super(Demo.class, forVariable(table), "public", table);
        addMetadata();
    }

    protected void addMetadata() {
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.CHAR).withSize(32).notNull());
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128).notNull());
        addMetadata(createdAt, ColumnMetadata.named("created_at").withIndex(4).ofType(Types.TIMESTAMP).withSize(29)
                .withDigits(6).notNull());
        addMetadata(amount,
                ColumnMetadata.named("amount").withIndex(3).ofType(Types.DECIMAL).withSize(12).withDigits(3));
    }

}
