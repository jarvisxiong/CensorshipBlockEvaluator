/**
 * 
 */
package com.global.elastic.builder;

import org.elasticsearch.action.ActionResponse;

// TODO: Auto-generated Javadoc
/**
 * The Interface Builder.
 *
 * @author ankit.gupta4
 * @param <R> the generic type
 */
public interface Builder<R extends ActionResponse> {

	/**
	 * Get element.
	 *
	 * @return response of builder
	 */
	public R get();
}
