package com.example.demo.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Configuration
public class SwaggerConfiguration {
	public static final String AUTHORIZATION_HEADER = "Authorization";
	@Value("${spring.profile.value}")
	private String profile;
	@Autowired
    private Environment environment;
	
	private ApiInfo apiInfo() {
		
		
		for(String p : environment.getActiveProfiles()) {
			profile = p;
		}
		
		if(profile == null) {
			System.setProperty("spring.profiles.active", "live");
		}
		
		String title = new StringBuilder().append("Spring REST API - ").append(profile).toString();
		return new ApiInfo(title,
				"My First Swagger UI API",
				"1.0",
				"Terms of service",
				new Contact("leehg", "www.jcoding.kr", "turboguy36@gmail.com"),
				"License of API",
				"API license URL",
				Collections.emptyList());
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()))
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.ant("/api/**"))
				.build();
	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", AUTHORIZATION_HEADER, "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(defaultAuth())
				.build();
	}

	List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope
		= new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}
}
