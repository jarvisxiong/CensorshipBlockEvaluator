package com.global.service;

import java.util.Map;

public interface FeatureService {

	/**
	 * Gets the blocked status.
	 *
	 * @param urls
	 *            the urls
	 * @param countryCode
	 *            the country code
	 * @return the blocked status
	 * @throws ServiceException
	 *             the service exception
	 */
	public Map<String, Boolean> getBlockedStatus(String[] urls, String countryCode) throws ServiceException;

	/**
	 * Calculate and update blocked status.
	 *
	 * @param filePath
	 *            the file path
	 * @return true, if successful
	 * @throws ServiceException
	 *             the service exception
	 */
	public boolean calculateAndUpdateBlockedStatus(String filePath) throws ServiceException;
}
