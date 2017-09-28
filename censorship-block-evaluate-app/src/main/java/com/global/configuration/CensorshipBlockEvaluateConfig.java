package com.global.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * The Class CensorshipBlockEvaluateConfig.
 */
@Configuration
public class CensorshipBlockEvaluateConfig {

	/** The blocked keyword count percentage. */
	@Value("${blocked.url.keyword.percentage}")
	private float blockedKeywordCountPercentage;

	/** The blocked tags count percentage. */
	@Value("${blocked.url.tags.percentage}")
	private float blockedTagsCountPercentage;

	/** The blocked content count percentage. */
	@Value("${blocked.url.content.percentage}")
	private float blockedContentCountPercentage;

	/**
	 * Gets the blocked keyword count percentage.
	 *
	 * @return the blockedKeywordCountPercentage
	 */
	public float getBlockedKeywordCountPercentage() {
		return blockedKeywordCountPercentage;
	}

	/**
	 * Gets the blocked tags count percentage.
	 *
	 * @return the blockedTagsCountPercentage
	 */
	public float getBlockedTagsCountPercentage() {
		return blockedTagsCountPercentage;
	}

	/**
	 * Gets the blocked content count percentage.
	 *
	 * @return the blockedContentCountPercentage
	 */
	public float getBlockedContentCountPercentage() {
		return blockedContentCountPercentage;
	}

}
