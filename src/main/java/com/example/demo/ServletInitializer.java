package com.example.demo;

import java.util.concurrent.TimeUnit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.util.BongInterceptor;

@Configuration
@ComponentScan(basePackages = {"com.example.demo"})
public class ServletInitializer extends SpringBootServletInitializer implements WebMvcConfigurer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(DemoApplication.class);
	}
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
				.addResourceLocations("classpath:/templates/")
				.setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES));
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		BongInterceptor interceptor = new BongInterceptor();
		registry.addInterceptor(interceptor)
		.addPathPatterns("/*")
		.excludePathPatterns("/login", "/signup");
	}
}
