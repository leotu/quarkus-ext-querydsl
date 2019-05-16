package io.quarkus.ext.querydsl.deployment;

import io.quarkus.builder.item.SimpleBuildItem;

/**
 * Marker build item indicating the QuerySQL has been fully initialized.
 */
public final class QuerydslInitializedBuildItem extends SimpleBuildItem {

    public QuerydslInitializedBuildItem() {
    }
}
