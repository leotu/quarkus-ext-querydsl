package io.quarkus.ext.querydsl.runtime;

import java.util.Map;

import io.quarkus.runtime.annotations.ConfigItem;
import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;

/**
 * Read from application.properties file
 * 
 * @author Leo Tu
 *
 */
@ConfigRoot(name = "querydsl", phase = ConfigPhase.BUILD_TIME)
public class QuerydslConfig {

    /**
     * The default config.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public QueryFactoryItemConfig defaultConfig;

    /**
     * Additional configs.
     */
    @ConfigItem(name = ConfigItem.PARENT)
    public Map<String, QueryFactoryItemConfig> namedConfig;

}
