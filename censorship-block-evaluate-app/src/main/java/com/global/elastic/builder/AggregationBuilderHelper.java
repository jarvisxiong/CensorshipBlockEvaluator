package com.global.elastic.builder;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Order;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.max.MaxAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;

import com.global.elastic.core.EsAggregation;

public class AggregationBuilderHelper {

	/**
	 * Create an aggregation query.
	 *
	 * @param aggregations
	 *            list of suggestion to add in the query.
	 * @return an suggestion query.
	 */
	public static AggregationBuilder aggregation(List<EsAggregation> aggregations) {
		AggregationBuilder builder = new AggregationBuilder(false);
		List<String> entriesToRemove = new ArrayList<>();
		EsAggregation prevAggr = null;
		for (EsAggregation currAggregation : aggregations) {
			String fieldname = currAggregation.getFieldname();
			String fieldLabel = currAggregation.getFieldLabel();
			int size = currAggregation.getSize();
			Order order = currAggregation.getOrder();

			switch (currAggregation.getAggregationOperator()) {
			case TERMS:
				TermsAggregationBuilder termBuilder = AggregationBuilders.terms(fieldLabel).field(fieldname).size(size);
				if (order != null) {
					termBuilder.order(order);
				}
				builder.put(fieldLabel, termBuilder);
				break;
			case FILTER:
				QueryBuilder filter = QueryBuilders.termsQuery(fieldname, currAggregation.getFilterOnValues());
				FilterAggregationBuilder filterBuilder = AggregationBuilders.filter(fieldLabel, filter);
				builder.put(fieldLabel, filterBuilder);
				break;
			case TOPHITS:
				TopHitsAggregationBuilder topHitsBuilder = AggregationBuilders.topHits(fieldLabel)
						.sort(fieldname, currAggregation.getAggregationOperator().getSortOrder()).size(size)
						.fetchSource(true);
				builder.put(fieldLabel, topHitsBuilder);
				break;
			case MAX:
				MaxAggregationBuilder maxBuilder = AggregationBuilders.max(fieldLabel).field(fieldname);
				builder.put(fieldLabel, maxBuilder);
				break;
			default:
				TermsAggregationBuilder defTermBuilder = AggregationBuilders.terms(fieldLabel).field(fieldname)
						.size(size);
				if (order != null) {
					defTermBuilder.order(order);
				}
				builder.put(fieldLabel, defTermBuilder);
			}
			if (prevAggr != null && prevAggr.getNextAggregation() == currAggregation) {
				((org.elasticsearch.search.aggregations.AggregationBuilder) builder.get(prevAggr.getFieldLabel()))
						.subAggregation(builder.get(fieldLabel));
				entriesToRemove.add(fieldLabel);
			}
			prevAggr = currAggregation;
		}

		entriesToRemove.stream().forEach(builderEntryToRemove -> builder.remove(builderEntryToRemove));
		return builder;
	}

}
