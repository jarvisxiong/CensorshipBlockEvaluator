package com.global.dao;

import java.util.List;
import java.util.Map;

import com.global.dao.exception.DAOException;
import com.global.model.LinkFeature;

public interface FeatureIndexDao {

	/**
	 * Insert feature.
	 *
	 * @param indexName
	 *            the index name
	 * @param linkFeature
	 *            the link feature
	 */
	public void insertFeature(String indexName, LinkFeature linkFeature);

	/**
	 * Gets the feature.
	 *
	 * @param countryCode
	 *            the country code
	 * @param urls
	 *            the urls
	 * @return the feature
	 * @throws DAOException
	 *             the DAO exception
	 */
	public List<LinkFeature> getFeature(String countryCode, String[] urls) throws DAOException;

	/**
	 * Gets the feature.
	 *
	 * @param countryCodeWiseUrl
	 *            the country code wise url
	 * @return the feature
	 * @throws DAOException
	 *             the DAO exception
	 */
	public List<LinkFeature> getFeature(Map<String, List<String>> countryCodeWiseUrl) throws DAOException;

	/**
	 * Gets the feature where url is accessible.
	 *
	 * @param url
	 *            the url
	 * @return the feature where url is accessible
	 * @throws DAOException
	 *             the DAO exception
	 */
	public LinkFeature getFeatureWhereURLIsAccessible(String url) throws DAOException;

	/**
	 * Update link feature.
	 *
	 * @param updatedLinkFeature
	 *            the updated link feature
	 * @return true, if successful
	 * @throws DAOException
	 *             the DAO exception
	 */
	public boolean updateLinkFeature(LinkFeature updatedLinkFeature) throws DAOException;

	/**
	 * Update link features.
	 *
	 * @param linkFeatures
	 *            the link features
	 * @param propertyName
	 *            the property name
	 * @param newValue
	 *            the new value
	 * @throws DAOException
	 *             the DAO exception
	 */
	public void updateLinkFeatures(List<LinkFeature> linkFeatures, String propertyName, String newValue)
			throws DAOException;

}
