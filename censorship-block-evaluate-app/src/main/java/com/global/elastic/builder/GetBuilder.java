/**
 * 
 */
package com.global.elastic.builder;

import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;

/**
 * The Class SearchBuilder.
 *
 * @author ankit.gupta4
 */
public class GetBuilder implements Builder<GetResponse> {

	/** Comment for <code>srb</code>. */
	GetRequestBuilder grb;

	/**
	 * Default constructor.
	 * 
	 * @param srb
	 *            e
	 */
	public GetBuilder(GetRequestBuilder grb) {
		this.grb = grb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public GetResponse get() {
		return grb.get();
	}

}