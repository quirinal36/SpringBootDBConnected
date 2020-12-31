package com.example.demo.conf;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {
	@Bean
    public Docket exampleApi() {
		Set<String> protocols = new HashSet<>();
		protocols.add("http");
		protocols.add("https");
        return new Docket(DocumentationType.SWAGGER_2)
        		.protocols(protocols)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.demo.api"))
                
                .paths(regex("/api.*"))
                .build()
                .apiInfo(metaInfo());
    }
	private ApiInfo metaInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "Spring Boot Swagger Example API",
                "Spring Boot Swagger Example Api Creator",
                "1.0",
                "Terms of Service",
                new Contact("yourname", "https://github.com/yourlink",
                        "youremail@yourdomain.com"),
                "Apache License Version 2.0",
                "https://www.apache.org/licesen.html",
                new ArrayList<>()
        );

        return apiInfo;
    }
}
