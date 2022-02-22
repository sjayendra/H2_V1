package com.jay.h2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication


public class H2Application {

	private static final Logger LOGGER=LoggerFactory.getLogger(H2Application.class);

	public static void main(String[] args) {
		SpringApplication.run(H2Application.class, args);

		LOGGER.info("Simple log statement with inputs {}, {} and {}", 1,2,3);
	}

}
