/**
 * 
 */
package com.global.elastic.builder;

import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;

import com.global.elastic.enums.AggregationOpsEnum;

// TODO: Auto-generated Javadoc
/**
 * The Class EsAggregation.
 *
 * @author ankit.gupta4
 */
public class EsAggregation {

	/** The field name. */
	private String fieldName;

	/** The field label. */
	private String fieldLabel;

	/** The aggregation operator. */
	private AggregationOpsEnum aggregationOperator;

	/** The date histogram interval. */
	private DateHistogramInterval dateHistogramInterval;

	/** The next aggregation. */
	private EsAggregation nextAggregation;

	/**
	 * Default constructor.
	 *
	 * @param fieldName            the field name
	 * @param fieldLabel the field label
	 * @param aggregationOperator            the aggregation operator
	 */
	public EsAggregation(String fieldName, String fieldLabel, AggregationOpsEnum aggregationOperator) {
		this.fieldName = fieldName;
		this.fieldLabel = fieldLabel;
		this.aggregationOperator = aggregationOperator;
	}

	/**
	 * Instantiates a new es aggregation.
	 *
	 * @param fieldName            the field name
	 * @param fieldLabel the field label
	 * @param aggregationOperator            the aggregation operator
	 * @param nextAggregation            the next aggregation
	 */
	public EsAggregation(String fieldName, String fieldLabel, AggregationOpsEnum aggregationOperator,
			EsAggregation nextAggregation) {
		this(fieldName, fieldLabel, aggregationOperator);
		this.nextAggregation = nextAggregation;
	}

	/**
	 * Instantiates a new Elastic aggregation.
	 *
	 * @param fieldname            the fieldName
	 * @param fieldLabel the field label
	 * @param aggregationOperator            the aggregation operator
	 * @param dateHistogramInterval            the date histogram interval
	 */
	public EsAggregation(String fieldname, String fieldLabel, AggregationOpsEnum aggregationOperator,
			DateHistogramInterval dateHistogramInterval) {
		this(fieldname, fieldLabel, aggregationOperator);
		this.dateHistogramInterval = dateHistogramInterval;
	}

	/**
	 * Gets the fieldName.
	 *
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * Gets the aggregation operator.
	 *
	 * @return the aggregation operator
	 */
	public AggregationOpsEnum getAggregationOperator() {
		return aggregationOperator;
	}

	/**
	 * Gets the date histogram interval.
	 *
	 * @return the date histogram interval
	 */
	public DateHistogramInterval getDateHistogramInterval() {
		return dateHistogramInterval;
	}

	/**
	 * Gets the next aggregation.
	 *
	 * @return the next aggregation
	 */
	public EsAggregation getNextAggregation() {
		return nextAggregation;
	}

	/**
	 * Sets the next aggregation.
	 *
	 * @param nextAggregation the new next aggregation
	 */
	public void setNextAggregation(EsAggregation nextAggregation) {
		this.nextAggregation = nextAggregation;
	}

	/**
	 * Gets the field label.
	 *
	 * @return the fieldLabel
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}
}
