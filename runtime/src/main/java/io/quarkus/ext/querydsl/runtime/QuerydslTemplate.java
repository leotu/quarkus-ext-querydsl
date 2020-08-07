package io.quarkus.ext.querydsl.runtime;

import org.jboss.logging.Logger;

import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.annotations.Recorder;

/**
 * Quarkus Template class (runtime)
 * 
 * @author Leo Tu
 */
@Recorder
public class QuerydslTemplate {
    private static final Logger log = Logger.getLogger(QuerydslTemplate.class);

    /**
     * Build Time
     */
    public BeanContainerListener addContainerCreatedListener(
            Class<? extends AbstractQueryFactoryProducer> queryFactoryProducerClass) {

        /**
         * Runtime Time
         */
        return new BeanContainerListener() {
            @Override
            public void created(BeanContainer beanContainer) { // Arc.container()
                AbstractQueryFactoryProducer queryFactoryProducer = beanContainer.instance(queryFactoryProducerClass);
                if (queryFactoryProducer == null) {
                    log.warn("(queryFactoryProducer == null)");
                } else {
                    log.debugv("queryFactoryProducer.class: {0}", queryFactoryProducer.getClass().getName());
                }
            }
        };
    }

}
