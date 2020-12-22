package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.example.demo.service.SolamonUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {
	@Autowired
	SolamonUserDetailsService userDetailService;
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/login", "/signup").permitAll()
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
				.invalidateHttpSession(true)
			.and()
			.httpBasic();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
	}
}
