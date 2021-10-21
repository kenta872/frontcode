package com.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FrontCodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FrontCodeApplication.class, args);

	}

}
