/**
 * 
 */
package com.global.model;

import java.io.Serializable;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.global.elastic.core.EsBean;

// TODO: Auto-generated Javadoc
/**
 * The Class LinkFeature.
 *
 * @author ankit
 */
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkFeature extends EsBean implements Serializable {

	/** The Constant LINK_FEATURE_INDEX. */
	public static final String LINK_FEATURE_INDEX = "censorship_feature_index";

	/** The Constant LINK_FEATURE_DOCTYPE. */
	public static final String LINK_FEATURE_DOCTYPE = "feature";

	/** The Constant URL. */
	public static final String URL = "url";

	/** The Constant COUNTRY_CODE. */
	public static final String COUNTRY_CODE = "countryCode";

	/** The Constant COOKIES. */
	public static final String COOKIES = "content.cookies";

	/** The Constant RESPONSE_CODE. */
	public static final String RESPONSE_CODE = "content.responseCode";
	
	/** The Constant BLOCKED. */
	public static final String BLOCKED = "blocked";
	
	/** The Constant BLOCKED_DEF_VAL. */
	public static final String BLOCKED_DEF_VAL = "NA";
	
	/** The Constant OK_RESPONSE_CODE. */
	public static final String OK_RESPONSE_CODE = "200";

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The country code. */
	private String countryCode;

	/** The url. */
	private String url;

	/** The keywords statistics. */
	private Map<String, Integer> keywordsStatistics;

	/** The sum keywords statistics. */
	private Integer sum_keywordsStatistics;

	/** The tags statistics. */
	private Map<String, Integer> tagsStatistics;

	/** The sum tags statistics. */
	private Integer sum_tagsStatistics;

	/** The content. */
	private HTMLContent content;

	/** The blocked. */
	private String blocked = "NA";

	
	/**
	 * Instantiates a new link feature.
	 */
	public LinkFeature() {
		super();
	}

	/**
	 * Instantiates a new link feature.
	 *
	 * @param htmlContent
	 *            the html content
	 */
	public LinkFeature(HTMLContent htmlContent) {
		this.url = htmlContent.getLink();
		this.countryCode = htmlContent.getCountryCode();
		this.content = htmlContent;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Sets the url.
	 *
	 * @param url
	 *            the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Gets the country code.
	 *
	 * @return the countryCode
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * Sets the country code.
	 *
	 * @param countryCode
	 *            the countryCode to set
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/**
	 * Gets the keywords statistics.
	 *
	 * @return the keywordsStatistics
	 */
	public Map<String, Integer> getKeywordsStatistics() {
		return keywordsStatistics;
	}

	/**
	 * Sets the keywords statistics.
	 *
	 * @param keywordsStatistics
	 *            the keywordsStatistics to set
	 */
	public void setKeywordsStatistics(Map<String, Integer> keywordsStatistics) {
		this.keywordsStatistics = keywordsStatistics;
	}

	/**
	 * Gets the tags statistics.
	 *
	 * @return the tagsStatistics
	 */
	public Map<String, Integer> getTagsStatistics() {
		return tagsStatistics;
	}

	/**
	 * Sets the tags statistics.
	 *
	 * @param tagsStatistics
	 *            the tagsStatistics to set
	 */
	public void setTagsStatistics(Map<String, Integer> tagsStatistics) {
		this.tagsStatistics = tagsStatistics;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public HTMLContent getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content
	 *            the content to set
	 */
	public void setContent(HTMLContent content) {
		this.content = content;
	}

	/**
	 * Gets the sum_keywords statistics.
	 *
	 * @return the sum_keywordsStatistics
	 */
	public Integer getSum_keywordsStatistics() {
		return sum_keywordsStatistics;
	}

	/**
	 * Sets the sum_keywords statistics.
	 *
	 * @param sum_keywordsStatistics
	 *            the sum_keywordsStatistics to set
	 */
	public void setSum_keywordsStatistics(Integer sum_keywordsStatistics) {
		this.sum_keywordsStatistics = sum_keywordsStatistics;
	}

	/**
	 * Gets the sum_tags statistics.
	 *
	 * @return the sum_tagsStatistics
	 */
	public Integer getSum_tagsStatistics() {
		return sum_tagsStatistics;
	}

	/**
	 * Sets the sum_tags statistics.
	 *
	 * @param sum_tagsStatistics
	 *            the sum_tagsStatistics to set
	 */
	public void setSum_tagsStatistics(Integer sum_tagsStatistics) {
		this.sum_tagsStatistics = sum_tagsStatistics;
	}

	/**
	 * Gets the blocked.
	 *
	 * @return the blocked
	 */
	public String getBlocked() {
		return blocked;
	}

	/**
	 * Sets the blocked.
	 *
	 * @param blocked
	 *            the blocked to set
	 */
	public void setBlocked(String blocked) {
		this.blocked = blocked;
	}
}
