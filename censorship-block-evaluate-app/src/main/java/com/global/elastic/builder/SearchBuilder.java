/**
 * 
 */
package com.global.elastic.builder;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;

// TODO: Auto-generated Javadoc
/**
 * The Class SearchBuilder.
 *
 * @author ankit.gupta4
 */
public class SearchBuilder implements Builder<SearchResponse> {

	/** Comment for <code>srb</code>. */
	SearchRequestBuilder srb;

	/**
	 * Default constructor.
	 * 
	 * @param srb
	 *            e
	 */
	public SearchBuilder(SearchRequestBuilder srb) {
		this.srb = srb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SearchResponse get() {
		return srb.get();
	}

}