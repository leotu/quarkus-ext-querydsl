package io.quarkus.ext.querydsl.runtime;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

import javax.inject.Qualifier;

import org.jboss.logging.Logger;

import io.agroal.api.AgroalDataSource;

/**
 * Produces QueryFactory
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
public abstract class AbstractQueryFactoryProducer {
    private static final Logger log = Logger.getLogger(AbstractQueryFactoryProducer.class);

    public QueryFactoryWrapper<?, ?> createQueryFactory(String sqlTemplates, AgroalDataSource dataSource,
            String registerCustomType) {
        Objects.requireNonNull(sqlTemplates, "sqlTemplates");
        Objects.requireNonNull(dataSource, "dataSource");

        if (registerCustomType == null || registerCustomType.isEmpty()) {
            return createQueryFactory(sqlTemplates, dataSource, new QuerydslCustomTypeRegister() {
            });
        } else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = QuerydslCustomTypeRegister.class.getClassLoader();
            }
            try {
                Class<?> clazz = cl.loadClass(registerCustomType);
                QuerydslCustomTypeRegister instance = (QuerydslCustomTypeRegister) clazz.newInstance();
                return createQueryFactory(sqlTemplates, dataSource, instance);
            } catch (Exception e) {
                log.error(registerCustomType, e);
                throw new RuntimeException(e);
            }
        }
    }

    public QueryFactoryWrapper<?, ?> createQueryFactory(String sqlTemplates, AgroalDataSource dataSource,
            QuerydslCustomTypeRegister registerCustomType) {
        Objects.requireNonNull(sqlTemplates, "sqlTemplates");
        Objects.requireNonNull(dataSource, "dataSource");
        Objects.requireNonNull(registerCustomType, "registerCustomType");
        return QueryFactory.create(sqlTemplates, dataSource, registerCustomType);
    }

    /**
     * CDI: Ambiguous dependencies
     */
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Qualifier
    static public @interface QueryFactoryQualifier {

        String value();
    }
}
