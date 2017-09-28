package com.global.ws;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.global.service.FeatureService;
import com.global.service.ServiceException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/censor")

@Api(value = "CensorshipCalculator")
public class CensorshipRestController {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CensorshipRestController.class);

	/** The feature service. */
	@Autowired
	private FeatureService featureService;

	/**
	 * Say hello.
	 *
	 * @param name
	 *            the name
	 * @return the string
	 */
	@GetMapping("/hello")
	@ApiOperation(value = "Say Hello", notes = "Say Hello")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Fields are with validation errors"),
			@ApiResponse(code = 201, message = "") })
	public String sayHello(@RequestParam("name") String name) {
		return name;
	}

	/**
	 * Checks if is censored.
	 *
	 * @param url
	 *            the url
	 * @param countryCode
	 *            the country code
	 * @return true, if is censored
	 */
	@GetMapping("/getCensored")
	@ApiOperation(value = "getCensored", notes = "This service is to check whether specified url is clocked in specified country or not")
	@ApiResponse(code = org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Error occurred while retrieving elastic document from specified country and url. See logs for more details")
	public ResponseEntity<Map<String, Boolean>> getCensored(
			@ApiParam(required = true, example = "www.google.com,www.msn.com,www.linkedin.com") @RequestParam("urls") String urls,
			@ApiParam(required = true, example = "IN") @RequestParam("countryCode") String countryCode) {
		try {
			Map<String, Boolean> urlBlckedMap = featureService.getBlockedStatus(urls.split(","), countryCode);
			if (MapUtils.isNotEmpty(urlBlckedMap)) {
				return new ResponseEntity<Map<String, Boolean>>(urlBlckedMap, HttpStatus.OK);
			} else {
				return new ResponseEntity<Map<String, Boolean>>(HttpStatus.NO_CONTENT);
			}
		} catch (ServiceException e) {
			logger.error("Error while executing isCensored rest service", e);
			return new ResponseEntity<Map<String, Boolean>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Update blocked status.
	 *
	 * @param urlCountryCodeFilePath
	 *            the url country code file path
	 * @return the response entity
	 */
	@GetMapping("/updateBlockedStatus")
	@ApiOperation(value = "updateBlockedStatus", notes = "This service is to update elastic documents for good blocked status for provided country code and url in urlCountryCodeFile")
	public ResponseEntity<Boolean> updateBlockedStatus(
			@ApiParam(required = true, example = "c:/temp/abc.csv", format = "path to csv file") @RequestParam("urlCountryCodeFile") String urlCountryCodeFilePath) {
		try {
			boolean updateResult = featureService.calculateAndUpdateBlockedStatus(urlCountryCodeFilePath);
			return new ResponseEntity<Boolean>(updateResult, HttpStatus.OK);
		} catch (ServiceException e) {
			logger.error("Error while executing updateBlockedStatus rest service", e);
			return new ResponseEntity<Boolean>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
