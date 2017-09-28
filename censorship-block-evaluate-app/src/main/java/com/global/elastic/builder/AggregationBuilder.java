package com.global.elastic.builder;

import java.util.HashMap;

import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;

/**
 * The Class AggregationBuilder.
 *
 * @author ankit.gupta4
 */
public class AggregationBuilder extends HashMap<String, AbstractAggregationBuilder> {

	/**
	 * The serial version.
	 */
	private static final long serialVersionUID = 3755186558479138257L;

	/** The nested. */
	private boolean nested = true;

	/**
	 * Default constructor.
	 */
	public AggregationBuilder() {
		super();
	}

	/**
	 * Instantiates a new aggregation builder.
	 *
	 * @param nested
	 *            the nested
	 */
	public AggregationBuilder(boolean nested) {
		this.nested = nested;
	}

	/**
	 * Getter for property nested.
	 * 
	 * @return Value of property nested.
	 */
	public boolean isNested() {
		return nested;
	}

}
