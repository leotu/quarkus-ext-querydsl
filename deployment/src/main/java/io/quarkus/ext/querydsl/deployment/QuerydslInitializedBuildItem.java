package io.quarkus.ext.querydsl.deployment;

import io.quarkus.builder.item.SimpleBuildItem;

/**
 * Marker build item indicating the QuerySQL has been fully initialized.
 * 
 * @author Leo Tu
 */
public final class QuerydslInitializedBuildItem extends SimpleBuildItem {

    public QuerydslInitializedBuildItem() {
    }
}
