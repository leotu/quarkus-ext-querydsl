package io.quarkus.ext.querydsl.runtime;

import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigGroup;
import io.quarkus.runtime.annotations.ConfigItem;

/**
 * 
 * @author <a href="mailto:leo.tu.taipei@gmail.com">Leo Tu</a>
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
        return super.toString() + "[template=" + template + ", datasource=" + datasource + ", registerCustomType="
                + registerCustomType + ", registerCustomTypeInject=" + registerCustomTypeInject + "]";
    }

}
