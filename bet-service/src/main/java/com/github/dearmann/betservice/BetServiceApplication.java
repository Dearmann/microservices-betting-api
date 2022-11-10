package com.github.dearmann.betservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BetServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BetServiceApplication.class, args);
	}

}
