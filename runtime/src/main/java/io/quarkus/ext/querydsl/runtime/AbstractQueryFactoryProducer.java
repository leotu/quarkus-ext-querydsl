package io.quarkus.ext.querydsl.runtime;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

import javax.inject.Qualifier;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

/**
 * Produces QueryFactory
 * 
 * @author Leo Tu
 */
public abstract class AbstractQueryFactoryProducer {
    private static final Logger log = Logger.getLogger(AbstractQueryFactoryProducer.class);

    public QueryFactory<?, ?> createQueryFactory(String sqlTemplates, DataSource dataSource,
            String registerCustomType, String factoryAlias) {
        Objects.requireNonNull(sqlTemplates, "sqlTemplates");
        Objects.requireNonNull(dataSource, "dataSource");

        if (registerCustomType == null || registerCustomType.isEmpty()) {
            return createQueryFactory(sqlTemplates, dataSource, new QuerydslCustomTypeRegister() {
            }, factoryAlias);
        } else {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            if (cl == null) {
                cl = QuerydslCustomTypeRegister.class.getClassLoader();
            }
            try {
                Class<?> clazz = cl.loadClass(registerCustomType);
                QuerydslCustomTypeRegister instance = (QuerydslCustomTypeRegister) clazz.getDeclaredConstructor().newInstance();
                return createQueryFactory(sqlTemplates, dataSource, instance, factoryAlias);
            } catch (Exception e) {
                log.error(registerCustomType, e);
                throw new RuntimeException(e);
            }
        }
    }

    public QueryFactory<?, ?> createQueryFactory(String sqlTemplates, DataSource dataSource,
            QuerydslCustomTypeRegister registerCustomType, String factoryAlias) {
        Objects.requireNonNull(sqlTemplates, "sqlTemplates");
        Objects.requireNonNull(dataSource, "dataSource");
        Objects.requireNonNull(registerCustomType, "registerCustomType");
        return QueryFactoryCreator.create(sqlTemplates, dataSource, registerCustomType, factoryAlias);
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
