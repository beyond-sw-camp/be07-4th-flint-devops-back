package com.hotel.flint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FlintApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlintApplication.class, args);
	}

}
