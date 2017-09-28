/**
 * 
 */
package com.global.elastic.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;

import com.global.elastic.enums.EsModeQuery;
import com.google.common.collect.Lists;

/**
 * The Class EsQuery.
 *
 * @author ankit.gupta4
 */
public final class EsQuery {

	/** The field name. */
	private String fieldName;

	/** The value. */
	private Object value;

	/** The fuzzy required. */
	private boolean fuzzyRequired;

	/**
	 * Represent the query mode.
	 */
	private EsModeQuery mode;

	/** The query int mode. */
	private EsQueryIntMode queryIntMode;

	/** The weight */
	private float weight;

	/** The queries. */
	private List<EsQuery> queries;

	/**
	 * The Enum EsQueryIntMode.
	 */
	public enum EsQueryIntMode {

		/** The must. */
		MUST,

		/** The should. */
		SHOULD,

		/** The must not. */
		MUST_NOT,

		/** The filter. */
		FILTER;
	}

	/**
	 * Instantiates a new es query.
	 *
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @param mode
	 *            the mode
	 */
	public EsQuery(String fieldName, Object value, EsModeQuery mode) {
		this.fieldName = fieldName;
		this.value = value;
		this.mode = mode;
	}

	/**
	 * Instantiates a new es query.
	 *
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @param mode
	 *            the mode
	 * @param queryIntMode
	 *            the query int mode
	 */
	public EsQuery(String fieldName, Object value, EsModeQuery mode, EsQueryIntMode queryIntMode) {
		this(fieldName, value, mode);
		this.queryIntMode = queryIntMode;
	}

	/**
	 * Default constructor.
	 *
	 * @param fieldname
	 *            the fieldname
	 * @param value
	 *            the value
	 * @param mode
	 *            the mode
	 * @param queryIntMode
	 *            the query int mode
	 * @param fuzzyRequired
	 *            the fuzzy required
	 */
	public EsQuery(String fieldname, Object value, EsModeQuery mode, EsQueryIntMode queryIntMode,
			boolean fuzzyRequired) {
		this(fieldname, value, mode);
		this.queryIntMode = queryIntMode;
		this.fuzzyRequired = fuzzyRequired;
	}

	/**
	 * Instantiates a new es query.
	 *
	 * @param fieldname
	 *            the fieldname
	 * @param value
	 *            the value
	 * @param mode
	 *            the mode
	 * @param queryIntMode
	 *            the query int mode
	 * @param fuzzyRequired
	 *            the fuzzy required
	 * @param weight
	 *            the weight
	 */
	public EsQuery(String fieldname, Object value, EsModeQuery mode, EsQueryIntMode queryIntMode, boolean fuzzyRequired,
			float weight) {
		this(fieldname, value, mode);
		this.queryIntMode = queryIntMode;
		this.fuzzyRequired = fuzzyRequired;
		this.weight = weight;
	}

	/**
	 * Gets the field name.
	 *
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public EsModeQuery getMode() {
		return mode;
	}

	/**
	 * Sets the value.
	 *
	 * @param value
	 *            the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * To query.
	 *
	 * @return the query builder
	 */
	public QueryBuilder toQuery() {
		QueryBuilder query = null;
		Object refinedValue = getEscapedValue(value);
		switch (this.getMode()) {
		case TERM:
			if (isCollectionValue(refinedValue)) {
				Collection<?> matchCollect = (Collection<?>) refinedValue;
				query = QueryBuilders.termsQuery(fieldName, matchCollect);
			} else {
				query = QueryBuilders.termsQuery(fieldName, refinedValue);
			}
			break;
		case MATCH:
			String matchFieldName = fieldName;
			MatchQueryBuilder matchQuery = QueryBuilders.matchQuery(matchFieldName, refinedValue);
			matchQuery.queryName(matchFieldName);
			if (isFuzzyRequired()) {
				matchQuery = matchQuery.fuzziness(Fuzziness.ONE);
			}
			matchQuery = matchQuery.operator(Operator.AND);
			query = matchQuery;
			break;
		case RANGE:
			Iterator<?> itr = ((Collection<?>) refinedValue).iterator();
			String from = itr.next().toString();
			String to = itr.next().toString();
			query = QueryBuilders.rangeQuery(fieldName).from(from).to(to);
			break;
		case QUERY_STRING_QUERY:
			QueryStringQueryBuilder queryStringQuery = QueryBuilders.queryStringQuery(refinedValue.toString());
			queryStringQuery.defaultOperator(Operator.AND);
			queryStringQuery.fuzziness(Fuzziness.ONE);
			query = queryStringQuery.field(fieldName);
			break;
		case GEO_DISTANCE:
			GeoDistanceQueryBuilder geoBuilder = QueryBuilders.geoDistanceQuery(fieldName);
			geoBuilder.distance(10, DistanceUnit.KILOMETERS);

			GeoPoint point = new GeoPoint();
			point.resetFromString(refinedValue.toString());
			geoBuilder.point(point);
			query = geoBuilder;
			break;
		case EXISTS:
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
			query = boolQuery.must(QueryBuilders.existsQuery(fieldName));
			break;
		default:
			break;
		}

		if (CollectionUtils.isNotEmpty(queries)) {
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

			if (query != null) {
				if (getQueryIntMode() == EsQueryIntMode.MUST) {
					boolQuery.must(query);
				} else if (getQueryIntMode() == EsQueryIntMode.SHOULD) {
					boolQuery.should(query);
				} else if (getQueryIntMode() == EsQueryIntMode.MUST_NOT) {
					boolQuery.mustNot(query);
				}
			}

			queries.forEach(currQuery -> {
				if (getQueryIntMode() == EsQueryIntMode.MUST) {
					boolQuery.must(currQuery.toQuery());
				} else if (getQueryIntMode() == EsQueryIntMode.SHOULD) {
					boolQuery.should(currQuery.toQuery());
				} else if (getQueryIntMode() == EsQueryIntMode.MUST_NOT) {
					boolQuery.mustNot(currQuery.toQuery());
				}
			});
			query = boolQuery;
		}
		return query;
	}

	/**
	 * Gets the escaped value.
	 *
	 * @param value
	 *            the value
	 * @return the escaped value
	 */
	private Object getEscapedValue(Object value) {
		Object escapedValue = value;
		if (value != null) {
			if (Collection.class.isAssignableFrom(value.getClass())) {
				Collection<String> escapedList = Lists.newArrayList();
				for (Object item : ((Collection<?>) value)) {
					escapedList.add(item.toString());
				}
				if (!escapedList.isEmpty()) {
					escapedValue = escapedList;
				}
			} else if (String.class.isAssignableFrom(value.getClass())) {
				escapedValue = value.toString();
			}
		}
		return escapedValue;
	}

	/**
	 * Convert the value for elasticsearch.
	 * 
	 * @param value
	 *            the value to convert.
	 * @return the value converted.
	 */
	private static boolean isCollectionValue(Object value) {
		return Collection.class.isAssignableFrom(value.getClass());
	}

	/**
	 * Gets the query int mode.
	 *
	 * @return the queryIntMode
	 */
	public EsQueryIntMode getQueryIntMode() {
		return queryIntMode;
	}

	/**
	 * Checks if is fuzzy required.
	 *
	 * @return the fuzzyRequired
	 */
	public boolean isFuzzyRequired() {
		return fuzzyRequired;
	}

	/**
	 * Gets the weight.
	 *
	 * @return the weight
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * Sets the weight.
	 *
	 * @param weight
	 *            the new weight
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}

	/**
	 * Adds the query.
	 *
	 * @param esQuery
	 *            the es query
	 */
	public void addQuery(EsQuery esQuery) {

		if (queries == null) {
			queries = new ArrayList<>();
		}

		this.queries.add(esQuery);
	}
}
