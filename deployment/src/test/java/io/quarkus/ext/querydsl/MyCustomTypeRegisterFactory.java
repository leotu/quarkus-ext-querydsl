package io.quarkus.ext.querydsl;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.jboss.logging.Logger;

import io.quarkus.ext.querydsl.runtime.QuerydslCustomTypeRegister;

/**
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@ApplicationScoped
//@Singleton
public class MyCustomTypeRegisterFactory {
    private static final Logger LOGGER = Logger.getLogger(MyCustomTypeRegisterFactory.class);

    @PostConstruct
    void onPostConstruct() {
        LOGGER.debug("MyCustomTypeRegisterFactory: onPostConstruct");
    }

    @ApplicationScoped
    @Produces
    @Named("myCustomTypeRegister2")
    public QuerydslCustomTypeRegister create() {
        LOGGER.debug("MyCustomTypeRegisterFactory: create");
        return new QuerydslCustomTypeRegister() {
        };
    }
}
