package io.quarkus.ext.querydsl;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

import org.jboss.logging.Logger;

import io.quarkus.ext.querydsl.runtime.QuerydslCustomTypeRegister;

/**
 * 
 * @author Leo Tu
 */
@Singleton
public class MyCustomTypeRegisterProducer {
    private static final Logger LOGGER = Logger.getLogger(MyCustomTypeRegisterProducer.class);

    @Singleton
    @Produces
    @Named("myCustomTypeRegister2")
    public QuerydslCustomTypeRegister create() {
        LOGGER.debug("MyCustomTypeRegisterFactory: create");
        return new QuerydslCustomTypeRegister() {
        };
    }
}
