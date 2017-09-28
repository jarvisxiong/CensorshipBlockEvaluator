package com.global.elastic.enums;

/**
 * The Enum EsModeQuery.
 * 
 * @author ankit.gupta4
 */
public enum EsModeQuery {

	/** The match. */
	MATCH,

	/** The range. */
	RANGE,

	GEO_DISTANCE,

	/** The term. */
	TERM,

	/** The exists. */
	EXISTS,

	/** The query string query. */
	QUERY_STRING_QUERY;
}