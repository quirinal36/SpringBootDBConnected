package com.example.demo.filters;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import javax.security.auth.message.AuthException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.service.SolamonUserDetailsService;
import com.example.demo.util.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter{
	@Autowired
	private SolamonUserDetailsService userDetailsService;
	@Autowired
	private JwtUtil jwtUtil;
	private static final Pattern BEARER = Pattern.compile("^Bearer$", Pattern.CASE_INSENSITIVE);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authorizationHeader = request.getHeader("Authorization");
		log.info("authorizationHeader>>"+ authorizationHeader);
		String username = null;
		String jwt = null;
		log.info(authorizationHeader);
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			username = jwtUtil.extractUsername(jwt);
		}
		log.info("username: " + username);
		if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			boolean isValid = jwtUtil.validateToken(jwt, userDetails);
			log.info("isValid: " + isValid);
			if(isValid) {
				Claims claims = null;

				try {
					claims = jwtUtil.extractAllClaims(jwt);
				} catch (JwtException e) {
//					throw new AuthException().MalformedJwt(jwt);
				}
				Set<GrantedAuthority> roles = new HashSet<>();
				String role = (String) claims.get("role");
				roles.add(new SimpleGrantedAuthority("ROLE_" + role));
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, roles);
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				for(GrantedAuthority c : usernamePasswordAuthenticationToken.getAuthorities()) {
					log.info("auth: "+c.getAuthority());
				}
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}
