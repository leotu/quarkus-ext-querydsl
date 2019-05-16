package io.quarkus.ext.querydsl.runtime;

import org.jboss.logging.Logger;

import io.quarkus.agroal.runtime.AbstractDataSourceProducer;
import io.quarkus.arc.runtime.BeanContainer;
import io.quarkus.arc.runtime.BeanContainerListener;
import io.quarkus.runtime.annotations.Template;

/**
 * Quarkus Template class (runtime)
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
 */
@Template
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
                AbstractDataSourceProducer dataSourceProducer = beanContainer
                        .instance(AbstractDataSourceProducer.class);
                if (dataSourceProducer == null) {
                    log.warn("(dataSourceProducer == null)");
                } else {
                    log.debugv("dataSourceProducer.class: {0}", dataSourceProducer.getClass().getName());
                }

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
