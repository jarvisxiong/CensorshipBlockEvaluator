package com.global.elastic.core;

import java.util.List;

import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;

import com.global.elastic.enums.AggregationOpsEnum;

/**
 * The Class EsAggregation.
 *
 * @author sprai The Class EsAggregation.
 */
public class EsAggregation {

	/** The field name. */
	private String fieldname;

	/** The field label. */
	private String fieldLabel;

	/** The value of field. */
	private int size;

	/** The order. */
	private Order order;

	/** The next aggregation. */
	private EsAggregation nextAggregation;

	/** The aggregation operator. */
	private AggregationOpsEnum aggregationOperator;

	/** The filter on values. */
	private List<String> filterOnValues;

	/**
	 * Default constructor.
	 * 
	 * @param fieldname
	 *            the field name
	 * @param size
	 *            the size of result.
	 */
	public EsAggregation(String fieldname, int size) {
		this(fieldname, size, null);
	}

	/**
	 * Instantiates a new es aggregation.
	 *
	 * @param fieldname
	 *            the fieldname
	 * @param size
	 *            the size
	 * @param aggregationOperator
	 *            the aggregation operator
	 */
	public EsAggregation(String fieldname, int size, AggregationOpsEnum aggregationOperator) {
		this(fieldname, fieldname, size, aggregationOperator);
	}

	/**
	 * Instantiates a new es aggregation.
	 *
	 * @param fieldname
	 *            the fieldname
	 * @param fieldLabel
	 *            the field label
	 * @param size
	 *            the size
	 * @param aggregationOperator
	 *            the aggregation operator
	 */
	public EsAggregation(String fieldname, String fieldLabel, int size, AggregationOpsEnum aggregationOperator) {
		super();
		this.fieldname = fieldname;
		this.fieldLabel = fieldLabel;
		this.size = size;
		this.order = Terms.Order.term(true);
		this.aggregationOperator = aggregationOperator;
	}

	/**
	 * Instantiates a new logs es aggregation.
	 *
	 * @param fieldname
	 *            the fieldname
	 * @param oper
	 *            the oper
	 * @param filterOnValues
	 *            the filter on values
	 */
	public EsAggregation(String fieldname, AggregationOpsEnum oper, List<String> filterOnValues) {
		this(fieldname, fieldname, oper, filterOnValues);
	}

	/**
	 * Instantiates a new logs es aggregation.
	 *
	 * @param fieldname
	 *            the fieldname
	 * @param fieldLabel
	 *            the field label
	 * @param oper
	 *            the oper
	 * @param filterOnValues
	 *            the filter on values
	 */
	public EsAggregation(String fieldname, String fieldLabel, AggregationOpsEnum oper, List<String> filterOnValues) {
		this(fieldname, fieldLabel, -1, oper);
		this.setOrder(null);
		this.filterOnValues = filterOnValues;
	}

	/**
	 * Getter for property fieldname.
	 * 
	 * @return Value of property fieldname.
	 */
	public String getFieldname() {
		return fieldname;
	}

	/**
	 * Setter for property fieldname.
	 * 
	 * @param fieldname
	 *            New value of property fieldname.
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	/**
	 * Getter for property size.
	 * 
	 * @return Value of property size.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Setter for property size.
	 * 
	 * @param size
	 *            New value of property size.
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * Getter for property order.
	 * 
	 * @return Value of property order.
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * Setter for property order.
	 * 
	 * @param order
	 *            New value of property order.
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	/**
	 * Getter for property nextAggregation.
	 * 
	 * @return Value of property nextAggregation.
	 */
	public EsAggregation getNextAggregation() {
		return nextAggregation;
	}

	/**
	 * Setter for property nextAggregation.
	 * 
	 * @param nextAggregation
	 *            New value of property nextAggregation.
	 */
	public void setNextAggregation(EsAggregation nextAggregation) {
		this.nextAggregation = nextAggregation;
	}

	/**
	 * Getter for property aggregationOperator.
	 * 
	 * @return Value of property aggregationOperator.
	 */
	public AggregationOpsEnum getAggregationOperator() {
		return aggregationOperator;
	}

	/**
	 * Setter for property aggregationOperator.
	 * 
	 * @param aggregationOperator
	 *            New value of property aggregationOperator.
	 */
	public void setAggregationOperator(AggregationOpsEnum aggregationOperator) {
		this.aggregationOperator = aggregationOperator;
	}

	/**
	 * Getter for property fieldLabel.
	 * 
	 * @return Value of property fieldLabel.
	 */
	public String getFieldLabel() {
		return fieldLabel;
	}

	/**
	 * Setter for property fieldLabel.
	 * 
	 * @param fieldLabel
	 *            New value of property fieldLabel.
	 */
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	/**
	 * Gets the filter on values.
	 *
	 * @return the filter on values
	 */
	public List<String> getFilterOnValues() {
		return filterOnValues;
	}

	/**
	 * Sets the filter on values.
	 *
	 * @param filterOnValues
	 *            the new filter on values
	 */
	public void setFilterOnValues(List<String> filterOnValues) {
		this.filterOnValues = filterOnValues;
	}

}
