package com.global.elastic.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FiltersFunctionScoreQuery.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import com.global.elastic.builder.EsQuery.EsQueryIntMode;
import com.global.elastic.core.EsBean;
import com.global.elastic.core.FieldFactorParams;
import com.global.elastic.enums.EsModeQuery;
import com.google.common.collect.Lists;

public class QueryBuilderHelper {

	public QueryBuilderHelper() {
	}

	/**
	 * Allows to create a seach query.
	 *
	 * @param matches
	 *            list item to search or filter.
	 * @param initialMode
	 *            the initial mode
	 * @return a search query builder.
	 */
	public static QueryBuilder search(List<EsQuery> matches, EsQueryIntMode initialMode) {
		BoolQueryBuilder searchQueryBuilder = null;
		if (matches != null && !matches.isEmpty()) {
			searchQueryBuilder = QueryBuilders.boolQuery();
			for (EsQuery esQuery : matches) {
				if (initialMode == EsQueryIntMode.SHOULD) {
					searchQueryBuilder.should(esQuery.toQuery());
				} else if (initialMode == EsQueryIntMode.FILTER) {
					searchQueryBuilder.filter(esQuery.toQuery());
				} else {
					searchQueryBuilder.must(esQuery.toQuery());
				}
			}
		}
		return searchQueryBuilder;
	}

	/**
	 * Union search.
	 *
	 * @param unionSearchQuery
	 *            the union search query
	 * @param searchQuery
	 *            the search query
	 * @return the query builder
	 */
	public static QueryBuilder unionSearch(List<QueryBuilder> searchQueries) {
		BoolQueryBuilder unionSearchQueryBuilder = QueryBuilders.boolQuery();
		searchQueries.stream().forEach(searchQuery -> unionSearchQueryBuilder.should(searchQuery));
		return unionSearchQueryBuilder;
	}

	/**
	 * Allows to create a seach query.
	 *
	 * @param allQuery
	 *            the query_string on all element.
	 * @param searchQuery
	 *            list item to search.
	 * @param filterQuery
	 *            list of item to filter query
	 * @param functionQuery
	 *            list of item to define a score and filter.
	 * @param initialMode
	 *            the initial mode
	 * @param scoreMode
	 *            string to set ElasticSearch initial score value
	 *            (sum/avg/max/min...)
	 * @param boostMode
	 *            string to set ElasticSearch boost value (sum/avg/max/min...)
	 * @return a search query builder.
	 */
	public static QueryBuilder functionScoreSearch(List<EsQuery> allQuery, List<EsQuery> searchQuery,
			List<EsQuery> filterQuery, List<EsQuery> functionQuery, EsQueryIntMode initialMode, ScoreMode scoreMode,
			CombineFunction boostMode) {
		return functionScoreSearch(allQuery, searchQuery, filterQuery, functionQuery, null, initialMode, scoreMode,
				boostMode);
	}

	/**
	 * Allows to create a seach query.
	 *
	 * @param allQuery
	 *            the query_string on all element.
	 * @param searchQuery
	 *            list item to search.
	 * @param filterQuery
	 *            list of item to filter query
	 * @param functionQuery
	 *            list of item to define a score and filter.
	 * @param factorQuery
	 *            the factor field value query
	 * @param initialMode
	 *            the initial mode
	 * @param scoreMode
	 *            string to set ElasticSearch initial score value
	 *            (sum/avg/max/min...)
	 * @param boostMode
	 *            string to set ElasticSearch boost value (sum/avg/max/min...)
	 * @return a search query builder.
	 */
	public static QueryBuilder functionScoreSearch(List<EsQuery> allQuery, List<EsQuery> searchQuery,
			List<EsQuery> filterQuery, List<EsQuery> functionQuery, List<FieldFactorParams> factorQuery,
			EsQueryIntMode initialMode, ScoreMode scoreMode, CombineFunction boostMode) {

		FunctionScoreQueryBuilder functionScoreQueryBuilder = null;
		List<FunctionScoreQueryBuilder.FilterFunctionBuilder> functions = new ArrayList<>();
		FunctionScoreQueryBuilder.FilterFunctionBuilder[] functionsArray = {};

		if (searchQuery != null && !searchQuery.isEmpty()) {

			BoolQueryBuilder searchQueryBuilder = QueryBuilders.boolQuery();

			for (EsQuery esQuery : searchQuery) {

				if (initialMode == EsQueryIntMode.SHOULD) {
					searchQueryBuilder.should(esQuery.toQuery());
				} else {
					searchQueryBuilder.must(esQuery.toQuery());
				}

			}

			if (factorQuery != null) {
				for (FieldFactorParams fieldFactory : factorQuery) {
					FieldValueFactorFunctionBuilder factoryField = ScoreFunctionBuilders
							.fieldValueFactorFunction(fieldFactory.getFactorField());
					factoryField.factor(fieldFactory.getFactor());
					factoryField.modifier(fieldFactory.getModifier());

					functions.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(factoryField));
				}
			}

			if (functionQuery != null) {
				for (EsQuery esQuery : functionQuery) {

					QueryBuilder constantScoreQuery = QueryBuilders.constantScoreQuery(esQuery.toQuery());
					ScoreFunctionBuilder<?> scoreFunctionBuilder = ScoreFunctionBuilders
							.weightFactorFunction(esQuery.getWeight());

					functions.add(new FunctionScoreQueryBuilder.FilterFunctionBuilder(constantScoreQuery,
							scoreFunctionBuilder));

				}
			}

			if (functions != null && !functions.isEmpty()) {
				functionsArray = functions.toArray(new FilterFunctionBuilder[functions.size()]);
			}

			if (allQuery != null && !allQuery.isEmpty()) {
				BoolQueryBuilder allQueryBuilder = QueryBuilders.boolQuery();

				for (EsQuery esQuery : allQuery) {
					allQueryBuilder.must(QueryBuilders.constantScoreQuery(esQuery.toQuery()));
				}

				filterQueryBuilder(filterQuery, allQueryBuilder);

				allQueryBuilder.must(searchQueryBuilder);

				functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(allQueryBuilder, functionsArray);
			} else {

				if (filterQuery != null) {
					filterQuery.stream().forEach(q -> searchQueryBuilder.filter(q.toQuery()));
				}
				functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(searchQueryBuilder, functionsArray);
			}
		}

		if (functionQuery != null && functionScoreQueryBuilder != null) {
			if (scoreMode != null) {
				functionScoreQueryBuilder.scoreMode(scoreMode);
			}
			if (boostMode != null) {
				functionScoreQueryBuilder.boostMode(boostMode);
			}
		}

		return functionScoreQueryBuilder;
	}

	/**
	 * Add filters to a search query.
	 *
	 * @param filterQuery
	 *            the filter query
	 * @param searchQueryBuilder
	 *            the search query builder
	 */
	private static void filterQueryBuilder(List<EsQuery> filterQuery, BoolQueryBuilder searchQueryBuilder) {
		if (filterQuery != null && !filterQuery.isEmpty()) {
			filterQuery.stream().forEach(q -> searchQueryBuilder.filter(q.toQuery()));
		}
	}

	/**
	 * Gets the search query.
	 *
	 * @param clientRequest
	 *            the client request
	 * @param fields
	 *            the fields
	 * @return the search query
	 */
	public static List<EsQuery> getSearchQuery(List<Object> values, List<String> fields) {
		return Lists.newArrayList(QueryBuilderHelper.createTermQuery(fields.get(0), values, EsQueryIntMode.MUST));
	}

	/**
	 * Gets the multi search query.
	 *
	 * @param values
	 *            the values
	 * @param fields
	 *            the fields
	 * @return the multi search query
	 */
	public static List<EsQuery> getMultiSearchQuery(List<Object> values, List<String> fields) {
		List<EsQuery> queryList = new ArrayList<>();
		EsQuery query = null;
		for (int i = 0; i < fields.size(); i++) {
			query = QueryBuilderHelper.createTermQuery(fields.get(i), values.get(i), EsQueryIntMode.MUST);
			queryList.add(query);
		}
		return queryList;
	}

	/**
	 * Creates the range query.
	 *
	 * @param fieldName
	 *            the field name
	 * @param gte
	 *            the gte
	 * @param lte
	 *            the lte
	 * @param intMode
	 *            the int mode
	 * @return the es query
	 */
	public static EsQuery createRangeQuery(String fieldName, String gte, String lte, EsQueryIntMode intMode) {
		return new EsQuery(fieldName, Lists.newArrayList(gte, lte), EsModeQuery.RANGE, intMode);
	}

	/**
	 * Creates the term query.
	 *
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @param intMode
	 *            the int mode
	 * @return the es query
	 */
	public static EsQuery createTermQuery(String fieldName, Object value, EsQueryIntMode intMode) {
		return new EsQuery(fieldName, value, EsModeQuery.TERM, intMode);
	}

	/**
	 * Gets the index request.
	 *
	 * @param <T>
	 *            the generic type
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param id
	 *            the id
	 * @param source
	 *            the source
	 * @return the index request
	 */
	public static <T> IndexRequest getIndexRequest(final String indexName, final String docType, String source) {
		IndexRequest indexRequest = null;
		try {
			indexRequest = new IndexRequest(indexName, docType);
			indexRequest.source(source);
		} catch (Exception e) {
		}
		return indexRequest;
	}


	/**
	 * Creates the match query.
	 *
	 * @param fieldName
	 *            the field name
	 * @param value
	 *            the value
	 * @param intMode
	 *            the int mode
	 * @return the es query
	 */
	public static EsQuery createMatchQuery(String fieldName, Object value, EsQueryIntMode intMode) {
		return new EsQuery(fieldName, value, EsModeQuery.MATCH, intMode);
	}

	/**
	 * Creates the exists query.
	 *
	 * @param fieldName
	 *            the field name
	 * @param intMode
	 *            the int mode
	 * @return the es query
	 */
	public static EsQuery createExistsQuery(String fieldName, EsQueryIntMode intMode) {
		return new EsQuery(fieldName, null, EsModeQuery.EXISTS, intMode);
	}

	/**
	 * Creates the match queries.
	 *
	 * @param <T>
	 *            the generic type
	 * @param fieldName
	 *            the field name
	 * @param matches
	 *            the matches
	 * @param intMode
	 *            the int mode
	 * @return the list
	 */
	public static <T extends EsBean> EsQuery createMatchQueries(String fieldName, List<String> matches,
			EsQueryIntMode intMode) {
		EsQuery esQuery = createMatchQuery(fieldName, matches.get(0), intMode);
		if (matches.size() > 1) {
			IntStream.range(1, matches.size())
					.forEach(idx -> esQuery.addQuery(createMatchQuery(fieldName, matches.get(idx), intMode)));
		}
		return esQuery;
	}
}
