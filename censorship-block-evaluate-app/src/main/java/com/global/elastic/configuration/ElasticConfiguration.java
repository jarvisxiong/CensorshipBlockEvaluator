/**
 * 
 */
package com.global.elastic.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.global.elastic.core.ElasticEntityManager;
import com.global.elastic.core.ElasticServer;
import com.google.common.collect.Lists;

/**
 * The Class ElasticConfiguration.
 *
 * @author ankit.gupta4
 */
@Configuration
public class ElasticConfiguration {

	/** The elastic hosts. */
	@Value("${elasticsearch.host}")
	private String elasticHosts;

	/** The elastic port. */
	@Value("${elasticsearch.port}")
	private String elasticPort;

	/**
	 * Entity manager.
	 *
	 * @return the elastic entity manager
	 * @throws Exception the exception
	 */
	@Bean
	public ElasticEntityManager entityManager() throws Exception {
		ElasticEntityManager entityManager = ElasticEntityManager.getInstance();
		ElasticServer elasticServer = elasticServer();
		entityManager.openSessionTransport(elasticServer);
		return entityManager;
	}

	/**
	 * Elastic server.
	 *
	 * @return the elastic server
	 * @throws Exception
	 *             the exception
	 */
	public ElasticServer elasticServer() throws Exception {
		return new ElasticServer(Lists.newArrayList(elasticHosts), Integer.parseInt(elasticPort));
	}

}
