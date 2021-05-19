package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		String profile = System.getProperty("spring.profile.active");
		if(profile == null) {
			System.setProperty("spring.profile.active", "live");
		}
		SpringApplication.run(DemoApplication.class, args);
	}
	
}
