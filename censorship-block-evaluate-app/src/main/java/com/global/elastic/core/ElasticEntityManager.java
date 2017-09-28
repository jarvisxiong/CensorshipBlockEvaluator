/**
 * 
 */
package com.global.elastic.core;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.mapping.put.PutMappingRequestBuilder;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.bulk.byscroll.BulkByScrollResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateAction;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.global.elastic.builder.CompletionBuilder;
import com.global.elastic.builder.GetBuilder;
import com.global.elastic.builder.QueryBuilderHelper;
import com.global.elastic.builder.SearchBuilder;
import com.global.elastic.builder.UpdateByIdBuilder;
import com.global.elastic.builder.UpdateByQueryBuilder;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

/**
 * The Class ElasticEntityManager.
 *
 * @author ankit.gupta4
 */
public class ElasticEntityManager {

	/** The logger. */
	private static final Logger logger = LoggerFactory.getLogger(ElasticEntityManager.class);

	/** Chunk size for the bulk process. */
	private int CHUNK_SIZE = 10000;

	/**
	 * The unique instance of entity manager.
	 */
	private static ElasticEntityManager instance;

	/**
	 * Client.
	 */
	private TransportClient client;

	/**
	 * Bulk processor.
	 */
	private BulkProcessor bulkProcessor;

	/**
	 * Default constructor.
	 */
	private ElasticEntityManager() {
	}

	/**
	 * Get the unique instance of elastic entity manager.
	 * 
	 * @return the unique instance of elastic entity manager.
	 */
	public static ElasticEntityManager getInstance() {
		if (instance == null) {
			instance = new ElasticEntityManager();
		}
		return instance;
	}

	/**
	 * Allows to open an connection with the server elasticsearch.
	 * 
	 * @param server
	 *            the server.
	 * @throws Exception
	 *             if an error occurs.
	 */
	public void openSessionTransport(ElasticServer server) throws Exception {
		int port = server.getPort();
		org.elasticsearch.common.settings.Settings.Builder settingsBuilder = Settings.builder();
		Settings settings = settingsBuilder.build();

		List<String> hosts = server.getHosts();
		for (String host : hosts) {
			client = new PreBuiltTransportClient(settings);
			InetAddress inetAddress = InetAddress.getByName(host);
			client.addTransportAddress(new InetSocketTransportAddress(inetAddress, port));
		}
	}

	/**
	 * Execute an query.
	 *
	 * @param query
	 *            the query
	 * @param filterQueryBuilder
	 *            the filter query builder
	 * @param size
	 *            the size
	 * @param indexName
	 *            the index name
	 * @return the response of query.
	 * @throws ElasticException
	 *             if an error occurs.
	 */
	public SearchResponse executeQuery(QueryBuilder query, QueryBuilder filterQueryBuilder, int from, int size,
			final String indexName) throws ElasticException {
		SearchRequestBuilder searchBuilder = searchRequestBuilder(query, filterQueryBuilder, from, size, indexName);
		return executeQuery(new SearchBuilder(searchBuilder));
	}

	/**
	 * Execute query.
	 *
	 * @param query
	 *            the query
	 * @param filterQueryBuilder
	 *            the filter query builder
	 * @param from
	 *            the from
	 * @param size
	 *            the size
	 * @param indexName
	 *            the index name
	 * @param sortName
	 *            the sort name
	 * @param sortOrder
	 *            the sort order
	 * @return the search response
	 * @throws ElasticException
	 *             the elastic exception
	 */
	public SearchResponse executeQuery(QueryBuilder query, QueryBuilder filterQueryBuilder, int from, int size,
			final String indexName, String sortName, SortOrder sortOrder) throws ElasticException {
		SearchRequestBuilder searchBuilder = searchRequestBuilder(query, filterQueryBuilder, from, size, indexName);
		if (!Strings.isNullOrEmpty(sortName) && sortOrder != null) {
			searchBuilder.addSort(sortName, sortOrder);
		}
		return executeQuery(new SearchBuilder(searchBuilder));
	}

	/**
	 * Execute query.
	 *
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param id
	 *            the id
	 * @return the gets the response
	 * @throws ElasticException
	 *             the elastic exception
	 */
	public GetResponse executeQuery(final String indexName, final String docType, final String id)
			throws ElasticException {
		GetRequestBuilder getBuilder = getRequestBuilder(indexName, docType, id);
		return executeQuery(new GetBuilder(getBuilder));
	}

	/**
	 * Execute a query with completion suggestion.
	 * 
	 * @param parameters
	 *            query parameter
	 * @param completionBuilder
	 *            completion builder.
	 * @return the response.
	 * @throws ElasticException
	 *             if an error occurs.
	 */
	public SearchResponse executeSuggest(String indexName, CompletionBuilder completionBuilder)
			throws ElasticException {
		SuggestBuilder suggestBuilder = new SuggestBuilder();
		Set<Entry<String, CompletionSuggestionBuilder>> entrySet = completionBuilder.entrySet();
		for (Entry<String, CompletionSuggestionBuilder> entry : entrySet) {
			suggestBuilder.addSuggestion(entry.getKey(), entry.getValue().size(10));
		}
		return client.prepareSearch(indexName).suggest(suggestBuilder).get();
	}

	/**
	 * Execute the query.
	 *
	 * @param <T>
	 *            generic response.
	 * @param requestBuilder
	 *            the query to execute.
	 * @return the response
	 * @throws ElasticException
	 *             if an error occurs.
	 */
	private <T extends ActionResponse> T executeQuery(com.global.elastic.builder.Builder<T> requestBuilder)
			throws ElasticException {
		try {
			T response = requestBuilder.get();
			return response;
		} catch (Exception e) {
			if (e.getCause() != null && e.getCause().toString().equals("QueryParsingException")) {
				throw new ElasticException("Invalid Query : incorrect parsing", e);
			} else {
				throw new ElasticException(e.getMessage(), e);
			}
		}
	}

	/**
	 * Save and update document.
	 *
	 * @param source
	 *            the source
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param id
	 *            the id
	 * @return the boolean
	 * @throws ElasticException
	 *             the elastic exception
	 */
	@Deprecated
	public Boolean saveAndUpdateDocument(String source, String indexName, String docType, String id)
			throws ElasticException {
		boolean isSaved = false;
		try {
			IndexRequest indexRequest = new IndexRequest(indexName, docType, id).source(source);
			UpdateRequest updateRequest = new UpdateRequest(indexName, docType, id).doc(source).upsert(indexRequest);
			client.update(updateRequest).get();
			isSaved = true;
		} catch (Exception exception) {
			isSaved = false;
			throw new ElasticException(exception);
		}
		refreshIndex(indexName);
		return isSaved;
	}

	/**
	 * Update document.
	 *
	 * @param source
	 *            the source
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param id
	 *            the id
	 * @return the boolean
	 * @throws ElasticException
	 *             the elastic exception
	 */
	@Deprecated
	public Boolean updateDocument(String source, final String indexName, final String docType, final String id)
			throws ElasticException {
		boolean isSaved = false;
		try {
			UpdateRequest updateRequest = new UpdateRequest(indexName, docType, id).doc(source);
			client.update(updateRequest).get();
			isSaved = true;
		} catch (Exception exception) {
			isSaved = false;
			throw new ElasticException(exception);
		}
		refreshIndex(indexName);
		return isSaved;
	}

	/**
	 * Update all entities using the update-by-query API.
	 *
	 * @param queryBuilder
	 *            is the element to update.
	 * @param script
	 *            the script
	 * @param index
	 *            the index
	 * @return the bulk index by scroll response
	 * @throws ElasticException
	 *             if an error occurs.
	 */
	public BulkByScrollResponse updateByQuery(QueryBuilder queryBuilder, Script script, String index)
			throws ElasticException {

		UpdateByQueryRequestBuilder updateByQuery = new UpdateByQueryRequestBuilder(client,
				UpdateByQueryAction.INSTANCE).source(index).script(script).filter(queryBuilder);

		return executeQuery(new UpdateByQueryBuilder(updateByQuery));
	}

	/**
	 * Update all entities using the update by id.
	 *
	 * @param id
	 *            the id
	 * @param index
	 *            the index
	 * @param docType
	 *            the doc type
	 * @param script
	 *            the script
	 * @return the update response
	 * @throws ElasticException
	 *             if an error occurs.
	 */
	public void updateById(String id, String index, String docType, Script script) throws ElasticException {

		Runnable updater = new Runnable() {
			/**
			 * Run.
			 */
			@Override
			public void run() {
				UpdateRequestBuilder updateRequestBuilder = new UpdateRequestBuilder(client, UpdateAction.INSTANCE,
						index, docType, id);
				updateRequestBuilder.setScript(script);

				try {
					executeQuery(new UpdateByIdBuilder(updateRequestBuilder));
					refreshIndex(index);
				} catch (ElasticException e) {
					logger.error("Error while updating elastic index " + index + " for script " + script, e);
				}

			}
		};
		Thread updateThread = new Thread(updater);
		updateThread.start();
	}

	/**
	 * Save all.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entities
	 *            the entities
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @return the boolean
	 * @throws ElasticException
	 *             the elastic exception
	 */
	public <T extends EsBean> Boolean saveAll(List<T> entities, final String indexName, final String docType)
			throws ElasticException {
		boolean uploadStatus = true;
		BulkProcessor bp = getBulkProcessor();
		List<List<T>> partition = Lists.partition(entities, CHUNK_SIZE);
		partition.stream().forEach(sublist -> {
			sublist.stream().forEach(entity -> {
				IndexRequest indexRequest;
				try {
					String source = EntityMapper.getInstance().getSerialize(entity);
					indexRequest = QueryBuilderHelper.getIndexRequest(indexName, docType, source);
					bp.add(indexRequest);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			});
		});
		try {
			bp.awaitClose(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			uploadStatus = false;
			throw new ElasticException(e);
		}
		refreshIndex(indexName);
		return uploadStatus;
	}

	/**
	 * Update all.
	 *
	 * @param <T>
	 *            the generic type
	 * @param entities
	 *            the entities
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param script
	 *            the script
	 * @return the boolean
	 * @throws ElasticException
	 *             the elastic exception
	 */
	public <T extends EsBean> Boolean updateAll(List<T> entities, final String indexName, final String docType,
			Script script) throws ElasticException {
		boolean uploadStatus = true;
		BulkProcessor bp = getBulkProcessor();
		List<List<T>> partition = Lists.partition(entities, CHUNK_SIZE);
		partition.stream().forEach(sublist -> {
			sublist.stream().forEach(entity -> {
				try {
					UpdateRequest updateRequest = new UpdateRequest(indexName, docType, entity.getId()).script(script);
					bp.add(updateRequest);
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
			});
		});
		try {
			bp.awaitClose(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			uploadStatus = false;
			throw new ElasticException(e);
		}
		refreshIndex(indexName);
		return uploadStatus;
	}

	/**
	 * Search request builder.
	 *
	 * @param queryBuilder
	 *            the query builder
	 * @param filterQueryBuilder
	 *            the filter query builder
	 * @param size
	 *            the size
	 * @param indexName
	 *            the index name
	 * @return the search request builder
	 */
	private SearchRequestBuilder searchRequestBuilder(QueryBuilder queryBuilder, QueryBuilder filterQueryBuilder,
			int from, int size, final String indexName) {
		SearchRequestBuilder searchBuilder = client.prepareSearch(indexName);

		if (queryBuilder != null) {
			searchBuilder.setQuery(queryBuilder);
		}
		if (filterQueryBuilder != null) {
			searchBuilder.setPostFilter(filterQueryBuilder);
		}
		searchBuilder.setFrom(from);
		searchBuilder.setSize(size);
		return searchBuilder;

	}

	/**
	 * Gets the request builder.
	 *
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param id
	 *            the id
	 * @return the request builder
	 */
	private GetRequestBuilder getRequestBuilder(final String indexName, final String docType, final String id) {
		return client.prepareGet(indexName, docType, id);
	}

	/**
	 * Getter for property bulkProcessor.
	 * 
	 * @return Value of property bulkProcessor.
	 */
	private BulkProcessor getBulkProcessor() {
		org.elasticsearch.action.bulk.BulkProcessor.Builder builder = BulkProcessor.builder(client, new Listener() {

			@Override
			public void beforeBulk(long executionId, BulkRequest request) {
				logger.info("Bulk processing started...");
				logger.info("Bulk processing: {} number of actions", request.numberOfActions());
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
			}

			@Override
			public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
				if (response.hasFailures()) {
					logger.error("Bulk processing failures");
					BulkItemResponse[] items = response.getItems();
					for (BulkItemResponse item : items) {
						logger.error("Bulk failed response id {}: {}", item.getId(), item.getFailureMessage());
					}
				}
				logger.info("Bulk  processing completed...");
			}
		});
		bulkProcessor = builder.setBulkActions(CHUNK_SIZE).setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB))
				.setFlushInterval(TimeValue.timeValueSeconds(5)).build();
		return bulkProcessor;
	}

	/**
	 * Gets the bulk processor.
	 *
	 * @return the bulk processor
	 */
	public BulkRequestBuilder getBulkRequestBuilder() {
		return client.prepareBulk();
	}

	/**
	 * Delete the index.
	 * 
	 * @param indexName
	 *            index name to delete.
	 */
	public void deleteIndex(String indexName) {
		DeleteIndexRequestBuilder deleteIndex = this.client.admin().indices().prepareDelete(indexName);
		deleteIndex.get();
	}

	/**
	 * Save document.
	 *
	 * @param source
	 *            the source
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @return the boolean
	 * @throws ElasticException
	 *             the elastic exception
	 */
	@Deprecated
	public <T extends EsBean> Boolean saveDocument(T entity, String indexName, String docType) throws ElasticException {
		boolean isSaved = false;
		try {
			String source = EntityMapper.getInstance().getSerialize(entity);
			IndexRequest indexRequest = new IndexRequest(indexName, docType).source(source);
			client.index(indexRequest).actionGet();
			isSaved = true;
		} catch (Exception exception) {
			isSaved = false;
			throw new ElasticException(exception);
		}
		refreshIndex(indexName);
		return isSaved;
	}

	/**
	 * Checks if is doc exist.
	 *
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param id
	 *            the id
	 * @return the boolean
	 */
	public Boolean isDocExist(String indexName, String docType, String id) {
		boolean isEDocExist = false;
		GetResponse response = client.prepareGet(indexName, docType, id).execute().actionGet();
		if (response.isExists()) {
			isEDocExist = true;
		}
		return isEDocExist;
	}

	/**
	 * Checks if is index exist.
	 *
	 * @param indexName
	 *            the index name
	 * @return the boolean
	 */
	public Boolean isIndexExist(String indexName) {
		IndicesExistsResponse response = client.admin().indices().prepareExists(indexName).execute().actionGet();
		return response.isExists();
	}

	/**
	 * Validate index.
	 *
	 * @param <T>
	 *            the generic type
	 * @param indexName
	 *            the index name
	 * @param docType
	 *            the doc type
	 * @param beanClass
	 *            the bean class
	 * @return the boolean
	 * @throws ElasticException
	 *             the elastic exception
	 */
	public <T extends EsBean> Boolean validateIndex(final String indexName, final String docType, Class<T> beanClass)
			throws ElasticException {
		boolean validateIndex = false;
		try {
			if (isIndexExist(indexName)) {
				validateIndex = true;
			} else {
				createIndex(indexName);
				// createMapping(indexName, docType, beanClass);
				validateIndex = true;
			}
		} catch (Exception e) {
			throw new ElasticException(e);
		}
		return validateIndex;
	}

	/**
	 * Method to create ElasticSearch index.
	 *
	 * @param indexName
	 *            the index name.
	 * @throws ElasticException
	 *             if an error occurs.
	 */
	public void createIndex(String indexName) throws ElasticException {
		CreateIndexRequestBuilder createIndexRequest = this.client.admin().indices().prepareCreate(indexName);
		try {
			CreateIndexResponse response = createIndexRequest.execute().actionGet();
			if (response.isAcknowledged()) {
				logger.info("Index " + indexName + " created");
			} else {
				throw new ElasticException("Index creation failed");
			}
		} catch (Exception e) {
			logger.warn(e.getMessage(), e);
			throw new ElasticException(e);
		}
	}

	public void createMapping(String indexName, String type) {
		String source = null;
		try {
			source = readJsonDefn("");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (source != null) {
			PutMappingRequestBuilder pmrb = client.admin().indices().preparePutMapping(indexName)

					.setType(type);
			pmrb.setSource(source).setType("");
		} else {
			System.out.println("mapping error");
		}
	}

	public String readJsonDefn(String url) throws Exception {
		StringBuffer bufferJSON = new StringBuffer();
		FileInputStream input = new FileInputStream(new File(url).getAbsolutePath());
		DataInputStream inputStream = new DataInputStream(input);
		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

		String line;

		while ((line = br.readLine()) != null) {
			bufferJSON.append(line);
		}
		br.close();
		return bufferJSON.toString();
	}

	/**
	 * Refresh index.
	 *
	 * @param indexName
	 *            the index name
	 */
	public void refreshIndex(String indexName) {
		client.admin().indices().prepareRefresh(indexName).execute().actionGet();
	}
}
