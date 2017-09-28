package com.global;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The Class CensorshipServiceApplication.
 */
@SpringBootApplication
@ComponentScan(basePackages = { "com.global" })
public class CensorshipBlockEvaluateApp {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(CensorshipBlockEvaluateApp.class, args);
	}

}
