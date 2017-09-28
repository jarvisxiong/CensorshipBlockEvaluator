package com.global.elastic.enums;

import org.elasticsearch.search.sort.SortOrder;

/**
 * @author sprai
 * The Enum AggregationOpsEnum.
 */
public enum AggregationOpsEnum {

    /** The top_hits. */
    TOPHITS("top_hits", SortOrder.DESC),

    /** The filter. */
    FILTER("filter", null),

    /** The terms. */
    TERMS("terms", null),

    /** Max. */
    MAX("max", null);

    /** The operation. */
    private String operation;

    /** The sort order. */
    private SortOrder sortOrder;

    /**
     * Instantiates a new aggregation operator enum.
     *
     * @param operation the operation
     * @param sortOrder the sort order
     */
    private AggregationOpsEnum(String operation, SortOrder sortOrder) {
        this.operation = operation;
        this.sortOrder = sortOrder;
    }

    /**
     * Getter for property operation.
     * 
     * @return Value of property operation.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Getter for property sortOrder.
     * 
     * @return Value of property sortOrder.
     */
    public SortOrder getSortOrder() {
        return sortOrder;
    }

}
