package com.erp.journals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JournalsApplication {

	public static void main(String[] args) {

		SpringApplication.run(JournalsApplication.class, args);
	}

}
