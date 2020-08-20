package io.quarkus.ext.querydsl.runtime;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.types.BigDecimalType;
import com.querydsl.sql.types.BytesType;
import com.querydsl.sql.types.JSR310InstantType;
import com.querydsl.sql.types.JSR310LocalDateTimeType;
import com.querydsl.sql.types.JSR310LocalDateType;
import com.querydsl.sql.types.JSR310LocalTimeType;
import com.querydsl.sql.types.StringType;

/**
 * Register custom type
 * 
 * @author Leo Tu
 */
public interface QuerydslCustomTypeRegister {

    /**
     * @see com.querydsl.sql.JDBCTypeMapping
     * @see com.querydsl.sql.JavaTypeMapping
     */
    default public void register(Configuration configuration) {
        // columnType == 1111 && typeName.equalsIgnoreCase("NVARCHAR2") // Oracle
        configuration.register(new StringType(java.sql.Types.OTHER));
        configuration.register(new BytesType(java.sql.Types.BLOB)); // com.querydsl.sql.type.BlobType
        configuration.register(new StringType(java.sql.Types.CLOB)); // com.querydsl.sql.type.ClobType

        configuration.register(new JSR310InstantType());
        configuration.register(new JSR310LocalDateType());
        configuration.register(new JSR310LocalDateTimeType());
        configuration.register(new JSR310LocalTimeType());

        // PostgreqSQL
        configuration.registerType("int2", Short.class);
        configuration.registerType("int4", Integer.class);
        configuration.registerType("int8", Long.class);

        //
        configuration.registerNumeric(1, 0, java.lang.Short.class);
        configuration.registerNumeric(2, 0, java.lang.Short.class);
        configuration.registerNumeric(1, 19, 1, 12, java.math.BigDecimal.class);
        configuration.registerNumeric(10, 19, 0, 0, Long.class); // int(10,0) ~ int(19,0)

        // ===
        configuration.register(new BigDecimalTypeExt(Types.DECIMAL));
        configuration.register(new BigDecimalTypeExt(Types.NUMERIC));
        configuration.register(new BigDecimalTypeExt(Types.DOUBLE));
        configuration.register(new BigDecimalTypeExt(Types.FLOAT));
        configuration.register(new BigDecimalTypeExt(Types.REAL));
    }

    class BigDecimalTypeExt extends BigDecimalType {

        public BigDecimalTypeExt() {
            super();
        }

        public BigDecimalTypeExt(int type) {
            super(type);
        }

        /**
         * @see java.math.BigDecimal#equals(Object)
         * @see java.math.BigDecimal#compareTo(Object)
         */
        @Override
        public BigDecimal getValue(ResultSet rs, int startIndex) throws SQLException {
            BigDecimal value = super.getValue(rs, startIndex);
            if (value != null && value.scale() != 0) {
                value = value.stripTrailingZeros();
            }
            return value;
        }
    }
}
