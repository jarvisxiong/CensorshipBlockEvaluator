/**
 * 
 */
package com.global.elastic.core;

import java.util.List;

/**
 * The Class ElasticServer.
 *
 * @author ankit.gupta4
 */
public final class ElasticServer {

	/** The host name. */
	private List<String> hosts;

	/** The port. */
	private int port;

	/**
	 * Instantiates a new elastic server.
	 *
	 * @param hosts
	 *            the hosts
	 * @param port
	 *            the port
	 */
	public ElasticServer(List<String> hosts, int port) {
		this.hosts = hosts;
		this.port = port;
	}

	/**
	 * Gets the hosts.
	 *
	 * @return the hosts
	 */
	public List<String> getHosts() {
		return hosts;
	}

	/**
	 * Gets the port.
	 *
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
}
