package io.quarkus.ext.querydsl.runtime;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

import com.querydsl.sql.Configuration;
import com.querydsl.sql.types.BigDecimalType;
import com.querydsl.sql.types.BytesType;
import com.querydsl.sql.types.StringType;
import com.querydsl.sql.types.UtilDateType;

/**
 * Register custom type
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public interface QuerydslCustomTypeRegister {

    /**
     * @see com.querydsl.sql.JDBCTypeMapping
     */
    default public void register(Configuration configuration) {
        // XXX: columnType == 1111 && typeName.equalsIgnoreCase("NVARCHAR2") // Oracle
        configuration.register(new StringType(java.sql.Types.OTHER)); // XXX
        configuration.register(new BytesType(java.sql.Types.BLOB)); // XXX
        configuration.register(new StringType(java.sql.Types.CLOB)); // XXX

        configuration.register(new JavaUtilDateType(java.sql.Types.DATE)); // XXX
        configuration.register(new JavaUtilDateType(java.sql.Types.TIMESTAMP)); // XXX
        configuration.register(new JavaUtilDateType(java.sql.Types.TIME)); // XXX

        // XXX: PostgreqSQL
        configuration.registerType("int2", Short.class);
        configuration.registerType("int4", Integer.class);
        configuration.registerType("int8", Long.class);

        //
        configuration.registerNumeric(1, 0, java.lang.Short.class);
        configuration.registerNumeric(2, 0, java.lang.Short.class);
        configuration.registerNumeric(1, 19, 1, 12, java.math.BigDecimal.class);

        configuration.registerNumeric(10, 19, 0, 0, Long.class); // XXX: int(10,0) ~ int(19,0)

        // ===
        configuration.register(new BigDecimalTypeExt(Types.DECIMAL));
        configuration.register(new BigDecimalTypeExt(Types.NUMERIC));

        // override JDBCTypeMapping.registerDefault(Types.DOUBLE,Double.class)
        configuration.register(new BigDecimalTypeExt(Types.DOUBLE));

        // override DBCTypeMapping.registerDefault(Types.FLOAT,Float.class)
        configuration.register(new BigDecimalTypeExt(Types.FLOAT));

        // override DBCTypeMapping.registerDefault(Types.REAL,Float.class)
        configuration.register(new BigDecimalTypeExt(Types.REAL));

        // override DBCTypeMapping.registerDefault(Types.DATE,java.sql.Date.class)
        // configuration.register(new JavaUtilDateType(Types.DATE));

        // override
        // DBCTypeMapping.registerDefault(Types.TIMESTAMP,java.sql.Timestamp.class)
        // configuration.register(new JavaUtilDateType(Types.TIMESTAMP));
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
         * @see leo.common.util.LangUtil#equalsBigDecimal(BigDecimal, BigDecimal)
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

    class JavaUtilDateType extends UtilDateType {

        protected final static DateTimeFormatter withMillisFormatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        public JavaUtilDateType(int type) {
            super(type);
        }

        @Override
        public String getLiteral(java.util.Date value) {
            return formatDateTime(value, null);
        }

        @Override
        public java.util.Date getValue(ResultSet rs, int startIndex) throws SQLException {
            Timestamp dt = rs.getTimestamp(startIndex);
            return dt == null ? null : new java.util.Date(dt.getTime());
        }

        @Override
        public Class<java.util.Date> getReturnedClass() {
            return java.util.Date.class;
        }

        @Override
        public void setValue(PreparedStatement st, int startIndex, java.util.Date value) throws SQLException {
            st.setTimestamp(startIndex, value == null ? null : new java.sql.Timestamp(value.getTime()));
        }

        /**
         * https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#ISO_OFFSET_DATE_TIME
         * 
         * @see java.time.format.DateTimeFormatterBuilder#parsePattern(String pattern)
         * @param tz null will using ZoneId.systemDefault()
         */
        private String formatDateTime(Date date, TimeZone tz) {
            if (date == null) {
                return null;
            }
            OffsetDateTime dateTime = OffsetDateTime.ofInstant(date.toInstant(),
                    tz == null ? ZoneId.systemDefault() : tz.toZoneId());
            return dateTime.format(withMillisFormatter);
        }
    }

}
