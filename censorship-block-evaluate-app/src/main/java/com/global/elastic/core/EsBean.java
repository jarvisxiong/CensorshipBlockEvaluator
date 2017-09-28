package com.global.elastic.core;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * The Class EsInner.
 */
public abstract class EsBean {

	/** The Constant ID. */
	public static final String ID = "id";

	/** The id. */
	@JsonIgnore
	private String id;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

}
