package com.global.elastic.builder;

import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;

/**
 * The Class UpdateByIdBuilder.
 */
public class UpdateByIdBuilder implements Builder<UpdateResponse> {

	/**
	 * Comment for <code>ubqrb</code>
	 */
	UpdateRequestBuilder urb;

	/**
	 * Default constructor.
	 *
	 * @param urb
	 *            the urb
	 */
	public UpdateByIdBuilder(UpdateRequestBuilder urb) {
		this.urb = urb;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UpdateResponse get() {
		return urb.get();
	}

}
