package com.global.service.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.global.configuration.CensorshipBlockEvaluateConfig;
import com.global.dao.FeatureIndexDao;
import com.global.dao.exception.DAOException;
import com.global.model.LinkFeature;
import com.global.service.FeatureService;
import com.global.service.ServiceException;

/**
 * The Class FeatureServiceImpl.
 */
@Service
public class FeatureServiceImpl implements FeatureService {

	/** The logger. */
	private static Logger logger = LoggerFactory.getLogger(FeatureServiceImpl.class);

	/** The feature dao. */
	@Autowired
	private FeatureIndexDao featureDao;

	/** The config. */
	@Autowired
	private CensorshipBlockEvaluateConfig config;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.global.service.FeatureService#getBlockedStatus(java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Map<String, Boolean> getBlockedStatus(String[] urls, String countryCode) throws ServiceException {
		Map<String, Boolean> urlBlckedMap = null;
		try {
			List<LinkFeature> links = featureDao.getFeature(countryCode, urls);
			if (CollectionUtils.isNotEmpty(links)) {
				urlBlckedMap = links.stream().collect(Collectors.toMap(new Function<LinkFeature, String>() {

					@Override
					public String apply(LinkFeature t) {
						return t.getUrl();
					}

				}, new Function<LinkFeature, Boolean>() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.util.function.Function#apply(java.lang.Object)
					 */
					@Override
					public Boolean apply(LinkFeature t) {
						if (LinkFeature.BLOCKED_DEF_VAL.equals(t.getBlocked())) {
							try {
								if (t.getContent().getResponseCode() != Integer.valueOf(LinkFeature.OK_RESPONSE_CODE)) {
									t.setBlocked(String.valueOf(Boolean.TRUE));
								} else {
									LinkFeature linkFeatureWithAccessibleUrl = featureDao
											.getFeatureWhereURLIsAccessible(t.getUrl());
									int keywordCount = linkFeatureWithAccessibleUrl.getSum_keywordsStatistics();
									int tagsCount = linkFeatureWithAccessibleUrl.getSum_tagsStatistics();
									int contentCount = linkFeatureWithAccessibleUrl.getContent().getLength();

									float relativeKeywordPer = ((float) t.getSum_keywordsStatistics() / keywordCount)
											* 100;
									float relativeTagsPer = ((float) t.getSum_tagsStatistics() / tagsCount) * 100;
									float relativeContentPer = ((float) t.getContent().getLength() / contentCount)
											* 100;

									if (relativeKeywordPer <= config.getBlockedKeywordCountPercentage()
											&& relativeTagsPer <= config.getBlockedTagsCountPercentage()
											&& relativeContentPer <= config.getBlockedContentCountPercentage()) {
										t.setBlocked(String.valueOf(Boolean.TRUE));
									} else {
										t.setBlocked(String.valueOf(Boolean.FALSE));
									}
								}
								featureDao.updateLinkFeature(t);
								return Boolean.valueOf(t.getBlocked());
							} catch (DAOException e) {
								logger.error("Error while calculating blocked status for url {} and coutryCode {}",
										t.getUrl(), t.getCountryCode());
							}
							return false;
						} else {
							return Boolean.valueOf(t.getBlocked());
						}
					}

				}));
			}
		} catch (DAOException ex) {
			logger.error("Error in getBlockedStatus ", ex);
			throw new ServiceException(ex.getMessage(), ex);
		}
		return urlBlckedMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.global.service.FeatureService#calculateAndUpdateBlockedStatus(java.
	 * lang.String)
	 */
	@Override
	public boolean calculateAndUpdateBlockedStatus(String filePath) throws ServiceException {
		Map<String, List<String>> countryWiseUrlList = new HashMap<>();
		try (CSVParser parser = CSVParser.parse(Paths.get(filePath).toFile(), Charset.forName("UTF-8"),
				CSVFormat.DEFAULT)) {
			for (CSVRecord csvLine : parser.getRecords()) {
				String countryCode = csvLine.get(0);
				String url = csvLine.get(1);
				if (countryWiseUrlList.get(countryCode) == null) {
					countryWiseUrlList.put(countryCode, new ArrayList<>());
				}
				countryWiseUrlList.get(countryCode).add(url);
			}
		} catch (IOException e) {
			logger.error("Error while parsing CSV file, method calculateAndUpdateBlockedStatus", e);
			throw new ServiceException(e.getMessage(), e);
		}
		if (MapUtils.isNotEmpty(countryWiseUrlList)) {
			try {
				List<LinkFeature> features = featureDao.getFeature(countryWiseUrlList);
				List<LinkFeature> featuresToUpdate = features.stream()
						.filter(feature -> LinkFeature.BLOCKED_DEF_VAL.equals(feature.getBlocked()))
						.collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(featuresToUpdate)) {
					Map<Boolean, List<LinkFeature>> updateMap = new HashMap<>();
					for (LinkFeature t : featuresToUpdate) {
						if (t.getContent().getResponseCode() != Integer.valueOf(LinkFeature.OK_RESPONSE_CODE)) {
							t.setBlocked(String.valueOf(Boolean.TRUE));
						} else {
							LinkFeature linkFeatureWithAccessibleUrl = featureDao
									.getFeatureWhereURLIsAccessible(t.getUrl());
							int keywordCount = linkFeatureWithAccessibleUrl.getSum_keywordsStatistics();
							int tagsCount = linkFeatureWithAccessibleUrl.getSum_tagsStatistics();
							int contentCount = linkFeatureWithAccessibleUrl.getContent().getLength();

							float relativeKeywordPer = ((float) t.getSum_keywordsStatistics() / keywordCount) * 100;
							float relativeTagsPer = ((float) t.getSum_tagsStatistics() / tagsCount) * 100;
							float relativeContentPer = ((float) t.getContent().getLength() / contentCount) * 100;

							if (relativeKeywordPer <= config.getBlockedKeywordCountPercentage()
									&& relativeTagsPer <= config.getBlockedTagsCountPercentage()
									&& relativeContentPer <= config.getBlockedContentCountPercentage()) {
								t.setBlocked(String.valueOf(Boolean.TRUE));
							} else {
								t.setBlocked(String.valueOf(Boolean.FALSE));
							}
						}
						if (updateMap.get(Boolean.valueOf(t.getBlocked())) == null) {
							updateMap.put(Boolean.valueOf(t.getBlocked()), new ArrayList<>());
						}
						updateMap.get(Boolean.valueOf(t.getBlocked())).add(t);
					}
					for (Boolean key : updateMap.keySet()) {
						featureDao.updateLinkFeatures(updateMap.get(key), LinkFeature.BLOCKED, String.valueOf(key));
					}
				}
				return true;
			} catch (DAOException e) {
				logger.error("Error while retrieving features from elastic, method calculateAndUpdateBlockedStatus", e);
				throw new ServiceException(e.getMessage(), e);
			}
		}
		return false;
	}

}
