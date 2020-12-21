package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class Security extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/", "/login").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login")
			.loginProcessingUrl("/doLogin")
			.usernameParameter("login")
			.passwordParameter("password")
			.successHandler(loginSuccessHandler)
			.and()
			.logout()
				.logoutUrl("/doLogout")
				.logoutSuccessUrl("/login")
			.and()
			.httpBasic();
	}
}
