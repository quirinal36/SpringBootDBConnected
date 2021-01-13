package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		String profile = System.getProperty("spring.profilke.active");
		if(profile == null) {
			System.setProperty("spring.profilke.active", "prod");
		}
		SpringApplication.run(DemoApplication.class, args);
	}
	
}
