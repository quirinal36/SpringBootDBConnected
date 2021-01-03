package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.filters.JwtRequestFilter;
import com.example.demo.service.SolamonUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {
	@Autowired
	SolamonUserDetailsService userDetailService;
	@Autowired
	private LoginSuccessHandler loginSuccessHandler;
	@Autowired
	private JwtRequestFilter jwtRequestFilter;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
			.authorizeRequests()
			.antMatchers("/login", "/signup", "/api/*", "/v2/*", "/swagger-ui/index.html", "/swagger-ui/", "/swagger-ui.html").permitAll()
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
			.httpBasic()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService);
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
}
