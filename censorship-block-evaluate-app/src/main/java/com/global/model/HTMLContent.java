package com.global.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * The Class HTMLContent.
 */
public class HTMLContent implements Serializable {

	private static final long serialVersionUID = 1L;

	/** The country code. */
	private String countryCode;

	/** The link. */
	private String link;

	/** The content. */
	private StringBuilder content;

	/** The lines. */
	private List<String> lines;

	/** The cookies. */
	private String cookies;

	/** The length. */
	private Integer length;

	/** The response code. */
	private int responseCode;

	/** The proxy host. */
	private String proxyHost;

	/** The proxy port. */
	private String proxyPort;

	/** The response header. */
	private Map<String, List<String>> responseHeader = null;

	/**
	 * Instantiates a new HTML content.
	 */
	public HTMLContent() {
		super();
	}

	/**
	 * Instantiates a new HTML content.
	 *
	 * @param countryCode
	 *            the country code
	 * @param link
	 *            the link
	 */
	public HTMLContent(String countryCode, String link) {
		this.countryCode = countryCode;
		this.link = link;
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
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets the link.
	 *
	 * @param link
	 *            the link to set
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public StringBuilder getContent() {
		return content;
	}

	/**
	 * Sets the content.
	 *
	 * @param content
	 *            the content to set
	 */
	public void setContent(StringBuilder content) {
		this.content = content;
	}

	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public List<String> getLines() {
		return lines;
	}

	/**
	 * Sets the lines.
	 *
	 * @param lines
	 *            the lines to set
	 */
	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	/**
	 * Gets the length.
	 *
	 * @return the length
	 */
	public Integer getLength() {
		return length;
	}

	/**
	 * Sets the length.
	 *
	 * @param length
	 *            the length to set
	 */
	public void setLength(Integer length) {
		this.length = length;
	}

	/**
	 * Gets the response code.
	 *
	 * @return the responseCode
	 */
	public int getResponseCode() {
		return responseCode;
	}

	/**
	 * Sets the response code.
	 *
	 * @param responseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * Gets the response header.
	 *
	 * @return the responseHeader
	 */
	public Map<String, List<String>> getResponseHeader() {
		return responseHeader;
	}

	/**
	 * Sets the response header.
	 *
	 * @param responseHeader
	 *            the responseHeader to set
	 */
	public void setResponseHeader(Map<String, List<String>> responseHeader) {
		this.responseHeader = responseHeader;
	}

	/**
	 * @return the cookies
	 */
	public String getCookies() {
		return cookies;
	}

	/**
	 * @param cookies
	 *            the cookies to set
	 */
	public void setCookies(String cookies) {
		this.cookies = cookies;
	}

	/**
	 * @return the proxyHost
	 */
	public String getProxyHost() {
		return proxyHost;
	}

	/**
	 * @param proxyHost
	 *            the proxyHost to set
	 */
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	/**
	 * @return the proxyPort
	 */
	public String getProxyPort() {
		return proxyPort;
	}

	/**
	 * @param proxyPort
	 *            the proxyPort to set
	 */
	public void setProxyPort(String proxyPort) {
		this.proxyPort = proxyPort;
	}
}
