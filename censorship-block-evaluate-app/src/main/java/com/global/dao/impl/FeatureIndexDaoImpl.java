package com.global.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.global.dao.FeatureIndexDao;
import com.global.dao.exception.DAOException;
import com.global.elastic.builder.EsQuery;
import com.global.elastic.builder.EsQuery.EsQueryIntMode;
import com.global.elastic.builder.QueryBuilderHelper;
import com.global.elastic.builder.ResponseBuilder;
import com.global.elastic.builder.ScriptBuilder;
import com.global.elastic.core.ElasticEntityManager;
import com.global.elastic.core.ElasticException;
import com.global.model.LinkFeature;
import com.google.common.collect.Lists;

/**
 * The Class FeatureIndexDaoImpl.
 */
@Component
public class FeatureIndexDaoImpl implements FeatureIndexDao {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(FeatureIndexDaoImpl.class);

	/** The entity manager. */
	@Autowired
	private ElasticEntityManager entityManager;

	/**
	 * Instantiates a new feature index dao impl.
	 */
	public FeatureIndexDaoImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.global.dao.FeatureIndexDao#insertFeature(java.lang.String,
	 * com.global.model.LinkFeature)
	 */
	@Override
	public void insertFeature(String indexName, LinkFeature linkFeature) {
		try {
			if (entityManager.validateIndex(indexName, LinkFeature.LINK_FEATURE_DOCTYPE, LinkFeature.class)) {
				entityManager.saveAll(Lists.newArrayList(linkFeature), indexName, LinkFeature.LINK_FEATURE_DOCTYPE);
			}
		} catch (ElasticException e) {
			logger.error("Error occurred in executing insertFeature method ", e);
		}
	}

	/**
	 * Gets the feature where url is accessible.
	 *
	 * @param url
	 *            the url
	 * @return the feature where url is accessible
	 * @throws DAOException
	 *             the DAO exception
	 */
	public LinkFeature getFeatureWhereURLIsAccessible(String url) throws DAOException {
		LinkFeature matchedLinkFeature = null;
		try {
			EsQuery urlMatchQuery = QueryBuilderHelper.createMatchQuery(LinkFeature.URL, url, EsQueryIntMode.MUST);
			EsQuery cookiesExistQuery = QueryBuilderHelper.createExistsQuery(LinkFeature.COOKIES, EsQueryIntMode.MUST);
			EsQuery responseCodeMatchQuery = QueryBuilderHelper.createMatchQuery(LinkFeature.RESPONSE_CODE,
					LinkFeature.OK_RESPONSE_CODE, EsQueryIntMode.MUST);
			List<EsQuery> matchQueries = new ArrayList<>();
			matchQueries.add(urlMatchQuery);
			matchQueries.add(cookiesExistQuery);
			matchQueries.add(responseCodeMatchQuery);
			QueryBuilder searchQuery = QueryBuilderHelper.search(matchQueries, EsQueryIntMode.MUST);
			SearchResponse response = entityManager.executeQuery(searchQuery, null, 0, 1,
					LinkFeature.LINK_FEATURE_INDEX);
			matchedLinkFeature = ResponseBuilder.getResponse(response, LinkFeature.class).get(0);
		} catch (ElasticException ex) {
			logger.error("Error occurred in executing getFeatureWhereURLIsAccessible method ", ex);
			throw new DAOException(ex);
		}
		return matchedLinkFeature;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.global.dao.FeatureIndexDao#getFeature(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public List<LinkFeature> getFeature(String countryCode, String[] urls) throws DAOException {
		try {
			EsQuery urlMatchQuery = QueryBuilderHelper.createMatchQueries(LinkFeature.URL, Arrays.asList(urls),
					EsQueryIntMode.SHOULD);
			EsQuery countryCodeMatchQuery = QueryBuilderHelper.createMatchQuery(LinkFeature.COUNTRY_CODE, countryCode,
					EsQueryIntMode.MUST);
			List<EsQuery> matchQueries = new ArrayList<>();
			matchQueries.add(urlMatchQuery);
			matchQueries.add(countryCodeMatchQuery);
			QueryBuilder searchQuery = QueryBuilderHelper.search(matchQueries, EsQueryIntMode.MUST);
			SearchResponse response = entityManager.executeQuery(searchQuery, null, 0, urls.length,
					LinkFeature.LINK_FEATURE_INDEX);
			return ResponseBuilder.getResponse(response, LinkFeature.class);
		} catch (ElasticException ex) {
			logger.error("Error occurred in executing getFeature method ", ex);
			throw new DAOException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.global.dao.FeatureIndexDao#updateLinkFeature(com.global.model.
	 * LinkFeature)
	 */
	@Override
	public boolean updateLinkFeature(LinkFeature updatedLinkFeature) throws DAOException {
		try {
			Script updateScript = ScriptBuilder.createScript(LinkFeature.BLOCKED,
					Boolean.valueOf(updatedLinkFeature.getBlocked()).toString());
			entityManager.updateById(updatedLinkFeature.getId(), LinkFeature.LINK_FEATURE_INDEX,
					LinkFeature.LINK_FEATURE_DOCTYPE, updateScript);
			return true;
		} catch (ElasticException e) {
			logger.error("Error in updateLinkFeature ", e);
			throw new DAOException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.global.dao.FeatureIndexDao#getFeature(java.util.Map)
	 */
	@Override
	public List<LinkFeature> getFeature(Map<String, List<String>> countryCodeWiseUrl) throws DAOException {
		try {
			int resultCount = 0;
			List<QueryBuilder> searchQueries = new ArrayList<>();
			for (Map.Entry<String, List<String>> countryCodeAndUrls : countryCodeWiseUrl.entrySet()) {
				EsQuery urlMatchQuery = QueryBuilderHelper.createMatchQueries(LinkFeature.URL,
						countryCodeAndUrls.getValue(), EsQueryIntMode.SHOULD);
				EsQuery countryCodeMatchQuery = QueryBuilderHelper.createMatchQuery(LinkFeature.COUNTRY_CODE,
						countryCodeAndUrls.getKey(), EsQueryIntMode.MUST);
				searchQueries.add(QueryBuilderHelper.search(Lists.newArrayList(urlMatchQuery, countryCodeMatchQuery),
						EsQueryIntMode.MUST));

				resultCount += countryCodeAndUrls.getValue().size();
			}

			QueryBuilder searchQuery = QueryBuilderHelper.unionSearch(searchQueries);
			SearchResponse response = entityManager.executeQuery(searchQuery, null, 0, resultCount,
					LinkFeature.LINK_FEATURE_INDEX);
			return ResponseBuilder.getResponse(response, LinkFeature.class);
		} catch (ElasticException ex) {
			logger.error("Error occurred in executing getFeature method ", ex);
			throw new DAOException(ex);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.global.dao.FeatureIndexDao#updateLinkFeatures(java.util.List,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void updateLinkFeatures(List<LinkFeature> linkFeatures, String propertyName, String newValue)
			throws DAOException {
		try {
			Script updateScript = ScriptBuilder.createScript(propertyName, Boolean.valueOf(newValue).toString());
			entityManager.updateAll(linkFeatures, LinkFeature.LINK_FEATURE_INDEX, LinkFeature.LINK_FEATURE_DOCTYPE,
					updateScript);
		} catch (ElasticException e) {
			logger.error("Error occurred in executing updateLinkFeatures method ", e);
			throw new DAOException(e);
		}
	}
}
