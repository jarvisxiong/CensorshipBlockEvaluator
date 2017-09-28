package com.global.elastic.builder;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.global.elastic.core.ElasticException;
import com.global.elastic.core.EntityMapper;
import com.global.elastic.core.EsBean;

/**
 * ResponseBuilder.
 * <P>
 *
 * @author Sopra Steria.
 * @copyright � Airbus 2016. All rights reserved.
 */
public final class ResponseBuilder {

	/**
	 * Default constructor.
	 */
	private ResponseBuilder() {
	}

	/**
	 * Gets an elastic search result.
	 *
	 * @param <T>
	 *            generic type.
	 * @param response
	 *            the response query.
	 * @param aClass
	 *            type of object to create.
	 * @return elastic search result.
	 * @throws ElasticException
	 *             the elastic exception
	 */
	public static <T extends EsBean> List<T> getResponse(SearchResponse response, Class<T> aClass)
			throws ElasticException {
		List<T> searchResults = new ArrayList<>();
		try {
			SearchHits hits = response.getHits();
			if (hits != null) {
				for (SearchHit hit : response.getHits()) {
					searchResults
							.add(EntityMapper.getInstance().getObject(hit.getId(), hit.getSourceAsString(), aClass));
				}
			}
		} catch (Exception ex) {
			throw new ElasticException(ex);
		}
		return searchResults;
	}

}
