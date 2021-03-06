package io.quarkus.ext.querydsl.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

/**
 * 
 * @author Leo Tu
 */
@ConfigGroup
public class QueryFactoryItemConfig {

    /**
     * The QueryDSL sqlTemplate
     */
    @ConfigItem
    public String template;

    /**
     * The QueryDSL dataSource
     */
    @ConfigItem
    public Optional<String> datasource;

    /**
     * The QueryDSL factory alias name by inject named
     */
    @ConfigItem
    public Optional<String> factoryAlias;

    /**
     * The QueryDSL register Custom Type
     */
    @ConfigItem
    public Optional<String> registerCustomType;

    /**
     * The QueryDSL register Custom Type by inject named
     */
    @ConfigItem
    public Optional<String> registerCustomTypeInject;

    @Override
    public String toString() {
        return super.toString() + "[template=" + template + ", datasource=" + datasource + ", factoryAlias="
                + factoryAlias + ", registerCustomType=" + registerCustomType + ", registerCustomTypeInject="
                + registerCustomTypeInject + "]";
    }

}
