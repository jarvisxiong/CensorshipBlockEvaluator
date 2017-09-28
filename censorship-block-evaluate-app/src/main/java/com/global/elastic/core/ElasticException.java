package com.global.elastic.core;

/**
 * The Class ElasticException.
 */
public class ElasticException extends Exception {

	/**
	 * The serial version.
	 */
	private static final long serialVersionUID = 4259800968053981935L;

	/**
	 * Default constructor.
	 */
	public ElasticException() {
		super();
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            the error message.
	 * @param cause
	 *            the cause.
	 */
	public ElasticException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Default constructor.
	 * 
	 * @param message
	 *            the error message.
	 */
	public ElasticException(String message) {
		super(message);
	}

	/**
	 * Default constructor.
	 * 
	 * @param cause
	 *            the cause.
	 */
	public ElasticException(Throwable cause) {
		super(cause);
	}

}
